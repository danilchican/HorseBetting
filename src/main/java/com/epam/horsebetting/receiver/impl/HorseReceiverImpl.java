package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.FormFieldConfig;
import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.HorseReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.validator.HorseValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        String name = content.findParameter(FormFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(FormFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(FormFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(FormFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator();

        if (validator.validateCreateHorse(name, genderAttr, ageAttr, suitAttr)) {
            boolean gender = "male".equals(genderAttr);
            byte age = Byte.parseByte(ageAttr);
            int suitId = Integer.parseInt(suitAttr);

            ArrayList<String> messages = new ArrayList<>();

            Horse horse = new Horse(name);

            horse.setSuitId(suitId);
            horse.setAge(age);
            horse.setGender(gender);

            LOGGER.log(Level.DEBUG, "Want create horse: " + horse);

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                Horse createdHorse = horseDAO.create(horse);

                messages.add("Horse has been created successfully.");
                content.insertSessionAttribute("messages", messages);

                LOGGER.log(Level.DEBUG, "Created horse: " + createdHorse);
            } catch (DAOException e) {
                messages.add("Can't create new horse.");
                content.insertSessionAttribute("errors", messages);

                LOGGER.log(Level.DEBUG, "Old input values: " + validator.getOldInput());
                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            LOGGER.log(Level.DEBUG, "Old input values: " + validator.getOldInput());
            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid horse data.");
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
        ArrayList<String> errors = new ArrayList<>();

        try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
            List<Horse> horses = horseDAO.findAll();

            content.insertJsonAttribute("success", true);
            content.insertJsonAttribute("horses", horses);
        } catch (DAOException e) {
            errors.add("Something went wrong...");

            content.insertJsonAttribute("success", false);
            content.insertJsonAttribute("errors", errors);

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
        HorseValidator validator = new HorseValidator();
        ArrayList<String> messages = new ArrayList<>();

        String idValue = content.findParameter(FormFieldConfig.Horse.ID_FIELD);

        if (validator.validateRemoveHorse(idValue)) {
            int id = Integer.parseInt(idValue);

            Horse suit = new Horse(id);
            LOGGER.log(Level.DEBUG, "Want remove horse: " + suit);

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                boolean result = horseDAO.remove(suit);

                if (result) {
                    messages.add("Horse removed successfully");
                } else {
                    messages.add("Can't remove current horse");
                }

                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add("Can't remove current horse");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());
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
        String idAttr = content.findParameter(FormFieldConfig.Horse.ID_FIELD);
        String name = content.findParameter(FormFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(FormFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(FormFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(FormFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator();

        if (validator.validateUpdateHorse(idAttr, name, genderAttr, ageAttr, suitAttr)) {
            int id = Integer.parseInt(idAttr);
            int suitId = Integer.parseInt(suitAttr);

            byte age = Byte.parseByte(ageAttr);
            boolean gender = "male".equals(genderAttr);

            Horse horse = new Horse(id);

            horse.setName(name);
            horse.setSuitId(suitId);
            horse.setAge(age);
            horse.setGender(gender);

            LOGGER.log(Level.DEBUG, "Want update horse: " + horse);

            ArrayList<String> messages = new ArrayList<>();

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                horseDAO.update(horse);

                messages.add("Horse updated successfully");
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add("Something went wrong...");
                content.insertSessionAttribute("errors", messages);

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            LOGGER.log(Level.DEBUG, "Old input values: " + validator.getOldInput());
            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid horse data.");
        }
    }
}