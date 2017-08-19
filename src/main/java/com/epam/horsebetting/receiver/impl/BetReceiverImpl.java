package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.dao.impl.BetDAOImpl;
import com.epam.horsebetting.dao.impl.ParticipantDAOImpl;
import com.epam.horsebetting.dao.impl.RaceDAOImpl;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.entity.Participant;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.BetReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.BetValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.*;

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String amount = content.findParameter(RequestFieldConfig.Bet.AMOUNT);
        String participantAttr = content.findParameter(RequestFieldConfig.Bet.PARTICIPANT_ID);
        String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));

        BetValidator validator = new BetValidator(locale);

        if (validator.validateCreateBet(amount, participantAttr)) {
            BigDecimal betAmount = new BigDecimal(amount);

            final int participantId = Integer.parseInt(participantAttr);
            final int userId = Integer.parseInt(userIdAttr);
            final int oneHour = 36_000_00;

            Timestamp currDate = new Timestamp(new Date().getTime() + oneHour);

            BetDAOImpl betDAO = new BetDAOImpl(true);
            UserDAOImpl userDAO = new UserDAOImpl(true);
            RaceDAOImpl raceDAO = new RaceDAOImpl(true);
            ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);

            TransactionManager transaction = new TransactionManager(betDAO, raceDAO, userDAO, participantDAO);
            transaction.beginTransaction();

            try {
                User user = userDAO.find(userId);
                Participant participant = participantDAO.find(participantId);
                Race race;

                if(user == null) {
                    messages.add(messageResource.get("user.not_found"));
                    content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                    content.insertJsonAttribute("success", false);

                    throw new ReceiverException(messageResource.get("user.not_found"));
                }

                if (participant == null) {
                    messages.add(messageResource.get("dashboard.participant.undefined"));
                    content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                    content.insertJsonAttribute("success", false);

                    throw new ReceiverException(messageResource.get("dashboard.participant.undefined"));
                }

                if(betDAO.findForUserByParticipant(user.getId(), participant.getId()) == null) {
                    race = raceDAO.find(participant.getRaceId());

                    if (race == null) {
                        messages.add(messageResource.get("dashboard.race.not_found"));
                        content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                        content.insertJsonAttribute("success", false);

                        throw new ReceiverException(messageResource.get("dashboard.race.not_found"));
                    }

                    if(race.isAvailable() && currDate.compareTo(race.getBetEndDate()) == -1) {
                        if(betAmount.compareTo(race.getMinRate()) != -1) {
                            if(user.getBalance().compareTo(betAmount) != -1) {
                                user.setBalance(user.getBalance().subtract(betAmount));
                                userDAO.updateBalance(user);

                                Bet bet = new Bet();
                                bet.setAmount(betAmount);
                                bet.setParticipantId(participantId);

                                LOGGER.log(Level.DEBUG, "Want create bet: " + bet);

                                betDAO.create(bet, userId);
                                transaction.commit();

                                content.insertJsonAttribute("message", messageResource.get("dashboard.bet.create.success"));
                                content.insertJsonAttribute("success", true);
                            } else {
                                messages.add(messageResource.get("dashboard.bet.create.balance_less"));
                                content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                                content.insertJsonAttribute("success", false);

                                throw new ReceiverException(messageResource.get("dashboard.bet.create.balance_less"));
                            }
                        } else {
                            messages.add(messageResource.get("dashboard.bet.create.amount_less"));
                            content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                            content.insertJsonAttribute("success", false);

                            throw new ReceiverException(messageResource.get("dashboard.bet.create.amount_less"));
                        }
                    } else {
                        messages.add(messageResource.get("dashboard.bet.race.finished"));
                        content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                        content.insertJsonAttribute("success", false);

                        throw new ReceiverException(messageResource.get("dashboard.bet.race.finished"));
                    }
                } else {
                    messages.add(messageResource.get("dashboard.participant.duplicated"));
                    content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                    content.insertJsonAttribute("success", false);

                    throw new ReceiverException(messageResource.get("dashboard.participant.duplicated"));
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.bet.create.fail"));
                content.insertJsonAttribute(REQUEST_ERRORS, messages.findAll());
                content.insertJsonAttribute("success", false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertJsonAttribute(REQUEST_ERRORS, validator.getErrors());
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        BetValidator validator = new BetValidator(locale);
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
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add("Can't remove bet.");
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
        }
    }
}
