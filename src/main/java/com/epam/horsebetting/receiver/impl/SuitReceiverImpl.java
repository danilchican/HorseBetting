package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.SuitDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.SuitReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.SuitValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;

public class SuitReceiverImpl extends AbstractReceiver implements SuitReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Obtain suits list ajax.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void ajaxObtainSuitsList(RequestContent content) throws ReceiverException {
        String pageNumber = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);

        SuitValidator validator = new SuitValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validatePage(pageNumber)) {
            final int step = 10;
            final int page = Integer.parseInt(pageNumber);
            final int offset = step * (page - 1);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                List<Suit> suits = suitDAO.obtainPart(step, offset);

                content.insertJsonAttribute("success", true);
                content.insertJsonAttribute("suits", suits);

                LOGGER.log(Level.DEBUG, "Obtained suits part: " + Arrays.toString(suits.toArray()));
            } catch (DAOException e) {

                messages.add(messageResource.get("error.undefined"));

                content.insertJsonAttribute("success", false);
                content.insertJsonAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertJsonAttribute("success", false);
            content.insertJsonAttribute("errors", validator.getErrors());
        }
    }

    /**
     * Create a new suit.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void createSuit(RequestContent content) throws ReceiverException {
        String name = content.findParameter(RequestFieldConfig.Suit.NAME_FIELD);

        SuitValidator validator = new SuitValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateName(name)) {
            Suit suit = new Suit(name);
            LOGGER.log(Level.DEBUG, "Want create suit: " + suit);

            SuitDAOImpl suitDAO = new SuitDAOImpl(true);

            TransactionManager transaction = new TransactionManager(suitDAO);
            transaction.beginTransaction();

            try {
                Suit createdSuit = suitDAO.create(suit);

                transaction.commit();
                LOGGER.log(Level.DEBUG, "Created suit: " + createdSuit);

                messages.add(messageResource.get("dashboard.suit.create.success"));

                content.insertJsonAttribute("suit", createdSuit);
                content.insertJsonAttribute("messages", messages);
                LOGGER.log(Level.DEBUG, "Messages: " + messages.findAll());
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.suit.create.fail"));
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
     * Find suit by id.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void updateSuit(RequestContent content) throws ReceiverException {
        String idNumber = content.findParameter(RequestFieldConfig.Suit.ID_FIELD);
        String name = content.findParameter(RequestFieldConfig.Suit.NAME_FIELD);

        MessageWrapper messages = new MessageWrapper();
        SuitValidator validator = new SuitValidator();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if(validator.validateSuit(idNumber, name)) {
            final int id = Integer.parseInt(idNumber);

            Suit suit = new Suit(id);
            suit.setName(name);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                suitDAO.update(suit);

                messages.add(messageResource.get("dashboard.suit.update.success"));
                content.insertJsonAttribute("messages", messages);
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                messages.add(messageResource.get("dashboard.suit.update.fail"));
                content.insertJsonAttribute("errors", messages);
                content.insertJsonAttribute("success", false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertJsonAttribute("errors", validator.getErrors());
            content.insertJsonAttribute("success", false);
        }
    }

    /**
     * Remove suit.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void removeSuit(RequestContent content) throws ReceiverException {
        String idNumber = content.findParameter(RequestFieldConfig.Suit.ID_FIELD);

        SuitValidator validator = new SuitValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateId(idNumber)) {
            final int id = Integer.parseInt(idNumber);
            Suit suit = new Suit(id);

            LOGGER.log(Level.DEBUG, "Want remove suit: " + suit);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                suitDAO.remove(suit);

                messages.add(messageResource.get("dashboard.suit.remove.success"));
                content.insertJsonAttribute("messages", messages);
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                messages.add(messageResource.get("dashboard.suit.remove.fail"));
                content.insertJsonAttribute("errors", messages);
                content.insertJsonAttribute("success", false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertJsonAttribute("errors", validator.getErrors());
            content.insertJsonAttribute("success", false);
        }
    }
}
