package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.FormFieldConfig;
import com.epam.horsebetting.dao.impl.BetDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.BetReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.validator.BetValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;

public class BetReceiverImpl extends AbstractReceiver implements BetReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Create a new bet.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void createBet(RequestContent content) throws ReceiverException {
        BetValidator validator = new BetValidator();
        ArrayList<String> messages = new ArrayList<>();

        String amount = content.findParameter(FormFieldConfig.Bet.AMOUNT);
        String participant = content.findParameter(FormFieldConfig.Bet.PARTICIPANT_ID);

        if (validator.validateCreateBet(amount, participant)) {
            int participantId = Integer.parseInt(participant);
            BigDecimal betAmount = new BigDecimal(amount);

            User authorizedUser = (User)content.findRequestAttribute("user");

            Bet bet = new Bet();
            bet.setAmount(betAmount);
            bet.setParticipantId(participantId);

            LOGGER.log(Level.DEBUG, "Want create bet: " + bet);

            BetDAOImpl betDAO = new BetDAOImpl(true);

            TransactionManager transaction = new TransactionManager(betDAO);
            transaction.beginTransaction();

            try {
                betDAO.create(bet, authorizedUser.getId());

                transaction.commit();

                content.insertJsonAttribute("message", "Bet created successfully.");
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add("Can't create bet.");
                content.insertJsonAttribute("errors", messages);
                content.insertJsonAttribute("success", false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertJsonAttribute("errors", validator.getErrors());
            content.insertJsonAttribute("success", false);
        }
    }
}
