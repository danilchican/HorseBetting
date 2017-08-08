package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.HorseReceiver;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HorseReceiverImpl extends AbstractReceiver implements HorseReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Create a new horse.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void createHorse(RequestContent content) throws ReceiverException {
        String name = content.findParameter("horse-name");
        boolean gender = "male".equals(content.findParameter("gender"));

        byte age = Byte.parseByte(content.findParameter("horse-age"));
        int suitId = Integer.parseInt(content.findParameter("horse-suit"));

        //TODO Create validators

        Horse horse = new Horse(name);

        horse.setSuitId(suitId);
        horse.setAge(age);
        horse.setGender(gender);

        LOGGER.log(Level.DEBUG, "Want create horse: " + horse);

        ArrayList<String> errors = new ArrayList<>();

        try (HorseDAOImpl horseDAO = new HorseDAOImpl()) {
            Horse createdHorse = horseDAO.create(horse);
            LOGGER.log(Level.DEBUG, "Created horse: " + createdHorse);
        } catch (DAOException e) {
            errors.add("Can't create new horse.");
            content.insertSessionAttribute("errors", errors);

            // TODO add saving old inputs

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }

    /**
     * Obtain horses list ajax.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void ajaxObtainHorsesList(RequestContent content) throws ReceiverException {
        try (HorseDAOImpl horseDAO = new HorseDAOImpl()) {
            List<Horse> horses = horseDAO.findAll();
            content.insertJsonAttribute("horses", horses);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }

    /**
     * Remove horse.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void removeHorse(RequestContent content) throws ReceiverException {
        int id = Integer.parseInt(content.findParameter("horse-id"));

        ArrayList<String> messages = new ArrayList<>();

        //TODO Create validators

        Horse suit = new Horse(id);
        LOGGER.log(Level.DEBUG, "Want remove horse: " + suit);

        try (HorseDAOImpl suitDAO = new HorseDAOImpl()) {
            boolean result = suitDAO.remove(suit);

            if (result) {
                messages.add("Horse removed successfully");
            } else {
                messages.add("Can't remove current horse");
            }

            content.insertSessionAttribute("messages", messages);
            content.insertSessionAttribute("success", result);
        } catch (DAOException e) {
            messages.add("Can't remove current horse");
            content.insertSessionAttribute("messages", messages);
            content.insertSessionAttribute("success", false);

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }

    /**
     * Update horse.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void updateHorse(RequestContent content) throws ReceiverException {
        int id = Integer.parseInt(content.findParameter("horse-id"));

        String name = content.findParameter("horse-name");
        boolean gender = "male".equals(content.findParameter("gender"));

        byte age = Byte.parseByte(content.findParameter("horse-age"));
        int suitId = Integer.parseInt(content.findParameter("horse-suit"));

        // TODO Create validators

        Horse horse = new Horse(id);

        horse.setName(name);
        horse.setSuitId(suitId);
        horse.setAge(age);
        horse.setGender(gender);

        LOGGER.log(Level.DEBUG, "Want update horse: " + horse);

        ArrayList<String> errors = new ArrayList<>();

        try (HorseDAOImpl horseDAO = new HorseDAOImpl()) {
            boolean isUpdated = horseDAO.update(horse);

            if (isUpdated) {
                errors.add("Horse updated successfully");
            } else {
                errors.add("Can't update current horse");
            }
        } catch (DAOException e) {
            errors.add("Can't update horse.");
            content.insertSessionAttribute("errors", errors);

            // TODO add saving old inputs

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }
}