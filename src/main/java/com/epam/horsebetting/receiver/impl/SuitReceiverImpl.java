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
import com.epam.horsebetting.validator.CommonValidator;
import com.epam.horsebetting.validator.SuitValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.*;

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String pageNumber = content.findParameter(RequestFieldConfig.Common.PAGE_FIELD);
        CommonValidator validator = new CommonValidator(locale);

        if (validator.validatePage(pageNumber)) {
            final int step = 10;
            final int page = Integer.parseInt(pageNumber);
            final int offset = step * (page - 1);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                List<Suit> suits = suitDAO.obtainPart(step, offset);

                content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, true);
                content.insertJsonAttribute("suits", suits);

                LOGGER.log(Level.DEBUG, "Obtained suits part: " + Arrays.toString(suits.toArray()));
            } catch (DAOException e) {
                messages.add(messageResource.get("error.undefined"));

                content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
                content.insertJsonAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
            content.insertJsonAttribute(REQUEST_ERRORS, validator.getErrors());
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String name = content.findParameter(RequestFieldConfig.Suit.NAME_FIELD);
        SuitValidator validator = new SuitValidator(locale);

        if (validator.validateName(name)) {
            Suit suit = new Suit(name);
            LOGGER.log(Level.DEBUG, "Want create suit: " + suit);

            SuitDAOImpl suitDAO = new SuitDAOImpl(true);

            TransactionManager transaction = new TransactionManager(suitDAO);
            transaction.beginTransaction();

            try {
                if (!isSuitNameUnique(suit.getName(), suitDAO)) {
                    Suit createdSuit = suitDAO.create(suit);

                    transaction.commit();
                    LOGGER.log(Level.DEBUG, "Created suit: " + createdSuit);

                    messages.add(messageResource.get("dashboard.suit.create.success"));

                    content.insertJsonAttribute("suit", createdSuit);
                    content.insertJsonAttribute(REQUEST_MESSAGES, messages);

                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, true);
                } else {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.suit.create.duplicated"));

                    content.insertJsonAttribute(REQUEST_ERRORS, messages);
                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.suit.create.fail"));

                content.insertJsonAttribute(REQUEST_ERRORS, messages);
                content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertJsonAttribute(REQUEST_ERRORS, validator.getErrors());
            content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String idNumber = content.findParameter(RequestFieldConfig.Suit.ID_FIELD);
        String name = content.findParameter(RequestFieldConfig.Suit.NAME_FIELD);

        SuitValidator validator = new SuitValidator(locale);

        if (validator.validateSuit(idNumber, name)) {
            final int id = Integer.parseInt(idNumber);

            Suit suit = new Suit(id);
            suit.setName(name);

            SuitDAOImpl suitDAO = new SuitDAOImpl(true);

            TransactionManager transaction = new TransactionManager(suitDAO);
            transaction.beginTransaction();

            try {
                if(canUpdateSuit(suit, suitDAO)) {
                    suitDAO.update(suit);

                    transaction.commit();

                    messages.add(messageResource.get("dashboard.suit.update.success"));
                    content.insertJsonAttribute(REQUEST_MESSAGES, messages);
                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, true);
                } else {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.suit.update.undefined"));
                    content.insertJsonAttribute(REQUEST_ERRORS, messages);
                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);

                    throw new ReceiverException("Suit with such id does not exist or name is not unique.");
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.suit.update.fail"));
                content.insertJsonAttribute(REQUEST_ERRORS, messages);
                content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertJsonAttribute(REQUEST_ERRORS, validator.getErrors());
            content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        String idNumber = content.findParameter(RequestFieldConfig.Suit.ID_FIELD);
        SuitValidator validator = new SuitValidator(locale);

        if (validator.validateId(idNumber)) {
            final int id = Integer.parseInt(idNumber);
            Suit suit = new Suit(id);

            LOGGER.log(Level.DEBUG, "Want remove suit: " + suit);

            SuitDAOImpl suitDAO = new SuitDAOImpl(true);

            TransactionManager transaction = new TransactionManager(suitDAO);
            transaction.beginTransaction();

            try {
                if(isSuitExist(suit.getId(), suitDAO)) {
                    suitDAO.remove(suit);
                    transaction.commit();

                    messages.add(messageResource.get("dashboard.suit.remove.success"));
                    content.insertJsonAttribute(REQUEST_MESSAGES, messages);
                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, true);
                } else {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.suit.remove.undefined"));
                    content.insertJsonAttribute(REQUEST_ERRORS, messages);
                    content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.suit.remove.fail"));
                content.insertJsonAttribute(REQUEST_ERRORS, messages);
                content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertJsonAttribute(REQUEST_ERRORS, validator.getErrors());
            content.insertJsonAttribute(AJAX_REQUEST_SUCCESS, false);
        }
    }

    /**
     * Check if can update suit.
     *
     * @param suit
     * @param suitDAO
     * @return boolean
     * @throws DAOException
     */
    private boolean canUpdateSuit(Suit suit, SuitDAOImpl suitDAO) throws DAOException {
        return isSuitExist(suit.getId(), suitDAO) && !isSuitNameUnique(suit.getName(), suitDAO);
    }

    /**
     * Check if the suit exist in storage.
     *
     * @param id
     * @param suitDAO
     * @return boolean
     */
    private boolean isSuitExist(int id, SuitDAOImpl suitDAO) throws DAOException {
        return suitDAO.find(id) != null;
    }

    /**
     * Check if the suit name is unique in storage.
     *
     * @param name
     * @param suitDAO
     * @return boolean
     * @throws DAOException
     */
    private boolean isSuitNameUnique(String name, SuitDAOImpl suitDAO) throws DAOException {
        return suitDAO.findByName(name) != null;
    }
}
