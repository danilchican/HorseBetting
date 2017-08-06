package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.SuitDAOImpl;
import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.SuitReceiver;
import com.epam.horsebetting.request.RequestContent;
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
        int page = 1;
        String pageNumber = content.findParameter("page");

        //TODO create validators
        if (pageNumber != null) {
            try {
                page = Integer.parseInt(pageNumber);
            } catch (NumberFormatException e) {
                throw new ReceiverException("Page incorrect: " + e.getMessage(), e);
            }
        }

        final int step = 10;
        final int offset = step * (page - 1);

        try (SuitDAOImpl suitDAO = new SuitDAOImpl()) {
            List<Suit> suits = suitDAO.obtainPart(step, offset);
            LOGGER.log(Level.DEBUG, "Suits part: " + Arrays.toString(suits.toArray()));
            content.insertJsonAttribute("suits", suits);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error: " + e.getMessage(), e);
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
        String name = content.findParameter("name");
        ArrayList<String> messages = new ArrayList<>();

        //TODO Create validators

        Suit suit = new Suit(name);
        LOGGER.log(Level.DEBUG, "Want create suit: " + suit);

        try (SuitDAOImpl suitDAO = new SuitDAOImpl()) {
            Suit createdSuit = suitDAO.create(suit);

            LOGGER.log(Level.DEBUG, "Created suit: " + createdSuit);

            messages.add("Suit created successfully");

            content.insertJsonAttribute("suit", createdSuit);
            content.insertJsonAttribute("messages", messages);
            content.insertJsonAttribute("success", true);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error: " + e.getMessage(), e);
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

        int id = Integer.parseInt(content.findParameter("id"));
        String name = content.findParameter("name");

        //TODO Create validators

        Suit suit = new Suit(id);
        suit.setName(name);

        try (SuitDAOImpl suitDAO = new SuitDAOImpl()) {
            boolean result = suitDAO.update(suit);

            if (result) {
                messages.add("Suit updated successfully");
            } else {
                messages.add("Can't update current suit");
            }

            content.insertJsonAttribute("messages", messages);
            content.insertJsonAttribute("success", result);
        } catch (DAOException e) {
            messages.add("Can't update current suit");
            content.insertJsonAttribute("messages", messages);
            content.insertJsonAttribute("success", false);

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
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
        int id = Integer.parseInt(content.findParameter("id"));
        ArrayList<String> messages = new ArrayList<>();

        //TODO Create validators

        Suit suit = new Suit(id);
        LOGGER.log(Level.DEBUG, "Want remove suit: " + suit);

        try (SuitDAOImpl suitDAO = new SuitDAOImpl()) {
            boolean result = suitDAO.remove(suit);

            if (result) {
                messages.add("Suit removed successfully");
            } else {
                messages.add("Can't remove current suit");
            }

            content.insertJsonAttribute("messages", messages);
            content.insertJsonAttribute("success", result);
        } catch (DAOException e) {
            messages.add("Can't remove current suit");
            content.insertJsonAttribute("messages", messages);
            content.insertJsonAttribute("success", false);

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }
}
