package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.SuitDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.SuitReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.validator.SuitValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        String pageNumber = content.findParameter("page");
        SuitValidator validator = new SuitValidator();

        ArrayList<String> errors;

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
                errors = new ArrayList<>();
                errors.add("Something went wrong...");

                content.insertJsonAttribute("success", false);
                content.insertJsonAttribute("errors", errors);

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
        SuitValidator validator = new SuitValidator();
        ArrayList<String> messages = new ArrayList<>();

        String name = content.findParameter("name");

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

                messages.add("Suit created successfully");

                content.insertJsonAttribute("suit", createdSuit);
                content.insertJsonAttribute("messages", messages);
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add("Can't create suit.");
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
        ArrayList<String> messages = new ArrayList<>();
        SuitValidator validator = new SuitValidator();

        String idNumber = content.findParameter("id");
        String name = content.findParameter("name");

        if(validator.validateSuit(idNumber, name)) {
            final int id = Integer.parseInt(idNumber);

            Suit suit = new Suit(id);
            suit.setName(name);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                suitDAO.update(suit);

                messages.add("Suit updated successfully");
                content.insertJsonAttribute("messages", messages);
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                messages.add("Can't update current suit");
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
        ArrayList<String> messages = new ArrayList<>();
        SuitValidator validator = new SuitValidator();

        String idNumber = content.findParameter("id");

        if (validator.validateId(idNumber)) {
            final int id = Integer.parseInt(idNumber);
            Suit suit = new Suit(id);

            LOGGER.log(Level.DEBUG, "Want remove suit: " + suit);

            try (SuitDAOImpl suitDAO = new SuitDAOImpl(false)) {
                suitDAO.remove(suit);

                messages.add("Suit removed successfully");
                content.insertJsonAttribute("messages", messages);
                content.insertJsonAttribute("success", true);
            } catch (DAOException e) {
                messages.add("Can't remove current suit");
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
