package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.HorseReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.HorseValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;

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
        String name = content.findParameter(RequestFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(RequestFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(RequestFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(RequestFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateCreateHorse(name, genderAttr, ageAttr, suitAttr)) {
            boolean gender = "male".equals(genderAttr);
            byte age = Byte.parseByte(ageAttr);
            int suitId = Integer.parseInt(suitAttr);

            Horse horse = new Horse(name);

            horse.setSuitId(suitId);
            horse.setAge(age);
            horse.setGender(gender);

            LOGGER.log(Level.DEBUG, "Want create horse: " + horse);

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                Horse createdHorse = horseDAO.create(horse);

                messages.add(messageResource.get("dashboard.horse.create.success"));
                content.insertSessionAttribute("messages", messages);

                LOGGER.log(Level.DEBUG, "Created horse: " + createdHorse);
            } catch (DAOException e) {
                messages.add(messageResource.get("dashboard.horse.create.fail"));
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
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
            List<Horse> horses = horseDAO.findAll();

            content.insertJsonAttribute("success", true);
            content.insertJsonAttribute("horses", horses);
        } catch (DAOException e) {
            messages.add(messageResource.get("error.undefined"));

            content.insertJsonAttribute("success", false);
            content.insertJsonAttribute("errors", messages);

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
        String idValue = content.findParameter(RequestFieldConfig.Horse.ID_FIELD);

        HorseValidator validator = new HorseValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateRemoveHorse(idValue)) {
            int id = Integer.parseInt(idValue);

            Horse suit = new Horse(id);
            LOGGER.log(Level.DEBUG, "Want remove horse: " + suit);

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                horseDAO.remove(suit);

                messages.add(messageResource.get("dashboard.horse.remove.success"));
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("dashboard.horse.remove.fail"));
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
        String idAttr = content.findParameter(RequestFieldConfig.Horse.ID_FIELD);
        String name = content.findParameter(RequestFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(RequestFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(RequestFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(RequestFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

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

            try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
                horseDAO.update(horse);

                messages.add(messageResource.get("dashboard.horse.update.success"));
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("dashboard.horse.update.fail"));
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