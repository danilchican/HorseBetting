package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.dao.impl.BetDAOImpl;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
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

        String amount = content.findParameter(RequestFieldConfig.Bet.AMOUNT);
        String participant = content.findParameter(RequestFieldConfig.Bet.PARTICIPANT_ID);

        // TODO create validate participant existing
        if (validator.validateCreateBet(amount, participant)) {
            int participantId = Integer.parseInt(participant);
            BigDecimal betAmount = new BigDecimal(amount);

            User authorizedUser = (User) content.findRequestAttribute("user");

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

    /**
     * Remove a bet.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void removeBet(RequestContent content) throws ReceiverException {
        BetValidator validator = new BetValidator();
        ArrayList<String> messages = new ArrayList<>();

        String idAttr = content.findParameter(RequestFieldConfig.Bet.ID);

        // TODO create validate id existing
        // TODO remove only if race not started!!!
        if (validator.validateId(idAttr)) {
            int id = Integer.parseInt(idAttr);

            User authorizedUser = (User) content.findRequestAttribute("user");

            BetDAOImpl betDAO = new BetDAOImpl(true);
            UserDAOImpl userDAO = new UserDAOImpl(true);

            LOGGER.log(Level.DEBUG, "Want remove bet: id=" + id);

            TransactionManager transaction = new TransactionManager(betDAO, userDAO);
            transaction.beginTransaction();

            try {
                User user = userDAO.find(authorizedUser.getId());
                Bet bet = betDAO.find(id);

                BigDecimal balance = user.getBalance().add(bet.getAmount());
                user.setBalance(balance);

                betDAO.remove(id);
                userDAO.updateBalance(user);

                transaction.commit();

                messages.add("Bet removed successfully.");
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add("Can't remove bet.");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());
        }
    }
}
