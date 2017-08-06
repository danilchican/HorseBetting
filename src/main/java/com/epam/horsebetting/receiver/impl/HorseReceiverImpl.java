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
        boolean gender = Boolean.valueOf(content.findParameter("gender"));

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
}
