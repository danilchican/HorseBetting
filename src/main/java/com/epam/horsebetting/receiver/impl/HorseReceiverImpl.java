package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.HorseDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.HorseReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.type.HorseGenderType;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.HorseValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_MESSAGES;
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String name = content.findParameter(RequestFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(RequestFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(RequestFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(RequestFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator(locale);

        if (validator.validateCreateHorse(name, genderAttr, ageAttr, suitAttr)) {
            boolean gender = HorseGenderType.isMale(genderAttr);
            byte age = Byte.parseByte(ageAttr);
            int suitId = Integer.parseInt(suitAttr);

            Horse horse = new Horse(name);

            horse.setSuitId(suitId);
            horse.setAge(age);
            horse.setGender(gender);

            LOGGER.log(Level.DEBUG, "Want create horse: " + horse);

            HorseDAOImpl horseDAO = new HorseDAOImpl(true);

            TransactionManager transaction = new TransactionManager(horseDAO);
            transaction.beginTransaction();

            try {
                if (horseDAO.findByName(name) == null) {
                    Horse createdHorse = horseDAO.create(horse);
                    transaction.commit();

                    messages.add(messageResource.get("dashboard.horse.create.success"));
                    content.insertSessionAttribute(REQUEST_MESSAGES, messages);

                    LOGGER.log(Level.DEBUG, "Created horse: " + createdHorse);
                } else {
                    String errorMessage = messageResource.get("dashboard.horse.create.duplicated");
                    transaction.rollback();

                    messages.add(errorMessage);
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException(errorMessage);
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.horse.create.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                LOGGER.log(Level.DEBUG, "Old input values: " + validator.getOldInput());
                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        try (HorseDAOImpl horseDAO = new HorseDAOImpl(false)) {
            List<Horse> horses = horseDAO.findAll();

            content.insertJsonAttribute("success", true);
            content.insertJsonAttribute("horses", horses);
        } catch (DAOException e) {
            messages.add(messageResource.get("error.undefined"));

            content.insertJsonAttribute("success", false);
            content.insertJsonAttribute(REQUEST_ERRORS, messages);

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        String idValue = content.findParameter(RequestFieldConfig.Horse.ID_FIELD);
        HorseValidator validator = new HorseValidator(locale);

        if (validator.validateRemoveHorse(idValue)) {
            int id = Integer.parseInt(idValue);

            Horse suit = new Horse(id);
            LOGGER.log(Level.DEBUG, "Want remove horse: " + suit);

            HorseDAOImpl horseDAO = new HorseDAOImpl(true);

            TransactionManager transaction = new TransactionManager(horseDAO);
            transaction.beginTransaction();

            try {
                if (horseDAO.find(id) != null) {
                    horseDAO.remove(suit);
                    transaction.commit();

                    messages.add(messageResource.get("dashboard.horse.remove.success"));
                    content.insertSessionAttribute(REQUEST_MESSAGES, messages);
                } else {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.horse.remove.undefined"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException(messageResource.get("dashboard.horse.undefined"));
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.horse.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String idAttr = content.findParameter(RequestFieldConfig.Horse.ID_FIELD);
        String name = content.findParameter(RequestFieldConfig.Horse.NAME_FIELD);
        String genderAttr = content.findParameter(RequestFieldConfig.Horse.GENDER_FIELD);
        String ageAttr = content.findParameter(RequestFieldConfig.Horse.AGE_FIELD);
        String suitAttr = content.findParameter(RequestFieldConfig.Horse.SUIT_FIELD);

        HorseValidator validator = new HorseValidator(locale);

        if (validator.validateUpdateHorse(idAttr, name, genderAttr, ageAttr, suitAttr)) {
            int id = Integer.parseInt(idAttr);
            int suitId = Integer.parseInt(suitAttr);

            byte age = Byte.parseByte(ageAttr);
            boolean gender = HorseGenderType.isMale(genderAttr);

            Horse horse = new Horse(id);

            horse.setName(name);
            horse.setSuitId(suitId);
            horse.setAge(age);
            horse.setGender(gender);

            LOGGER.log(Level.DEBUG, "Want update horse: " + horse);

            HorseDAOImpl horseDAO = new HorseDAOImpl(true);

            TransactionManager transaction = new TransactionManager(horseDAO);
            transaction.beginTransaction();

            try {
                if (horseDAO.find(id) != null) {
                    horseDAO.update(horse);
                    transaction.commit();

                    messages.add(messageResource.get("dashboard.horse.update.success"));
                    content.insertSessionAttribute(REQUEST_MESSAGES, messages);
                } else {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.horse.undefined"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException(messageResource.get("dashboard.horse.undefined"));
                }

            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("dashboard.horse.update.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());

            LOGGER.log(Level.DEBUG, "Old input values: " + validator.getOldInput());
            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid horse data.");
        }
    }
}