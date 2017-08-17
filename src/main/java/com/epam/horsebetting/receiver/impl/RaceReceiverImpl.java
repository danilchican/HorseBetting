package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.dao.impl.ParticipantDAOImpl;
import com.epam.horsebetting.dao.impl.RaceDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.RaceReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.DateFormatter;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.RaceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_MESSAGES;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;

public class RaceReceiverImpl extends AbstractReceiver implements RaceReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Create a new race.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void createRace(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatter.DEFAULT_DATE_FORMAT);

        String title = content.findParameter(RequestFieldConfig.Race.TITLE_FIELD);
        String place = content.findParameter(RequestFieldConfig.Race.PLACE_FIELD);
        String minRateAttr = content.findParameter(RequestFieldConfig.Race.MIN_RATE_FIELD);
        String trackLengthAttr = content.findParameter(RequestFieldConfig.Race.TRACK_LENGTH_FIELD);
        String betEndDateAttr = content.findParameter(RequestFieldConfig.Race.BET_END_DATE_FIELD);
        String startedAtAttr = content.findParameter(RequestFieldConfig.Race.STARTED_AT_FIELD);

        RaceValidator validator = new RaceValidator();

        if (validator.validateCreateRaceForm(title, place, minRateAttr, trackLengthAttr, betEndDateAttr, startedAtAttr)) {
            BigDecimal minRate = new BigDecimal(minRateAttr);
            int trackLength = Integer.parseInt(trackLengthAttr);

            ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);
            RaceDAOImpl raceDAO = new RaceDAOImpl(true);
            TransactionManager transaction = new TransactionManager(raceDAO);

            try {
                Date parsedDate = dateFormat.parse(betEndDateAttr);
                Timestamp betEndDate = new Timestamp(parsedDate.getTime());

                parsedDate = dateFormat.parse(startedAtAttr);
                Timestamp startedAt = new Timestamp(parsedDate.getTime());

                HashMap<Integer, BigDecimal> raceHorses = new HashMap<>();

                String[] jockeys = content.findParameterValues(RequestFieldConfig.Race.SELECTED_HORSES_FIELD);
                String[] coeffs = content.findParameterValues(RequestFieldConfig.Race.HORSE_COEFFICIENTS_FIELD);

                if (validator.validateJockeys(jockeys, coeffs)) {
                    for (int i = 0; i < jockeys.length; i++) {
                        raceHorses.put(Integer.parseInt(jockeys[i]), new BigDecimal(coeffs[i]));
                    }
                } else {
                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

                    content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
                    throw new ReceiverException("Race horses are not completed.");
                }

                Race race = new Race();

                race.setTitle(title);
                race.setPlace(place);
                race.setMinRate(minRate);
                race.setTrackLength(trackLength);
                race.setBetEndDate(betEndDate);
                race.setStartedAt(startedAt);

                LOGGER.log(Level.DEBUG, "Want create race: " + race);

                transaction.beginTransaction();

                Race createdRace = raceDAO.create(race);
                participantDAO.create(raceHorses, createdRace);

                transaction.commit();

                messages.add(messageResource.get("dashboard.race.create.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);

                LOGGER.log(Level.DEBUG, "Created race: " + createdRace);
            } catch (ParseException e) {
                transaction.rollback();

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                messages.add(messageResource.get("dashboard.race.create.parse.fail"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);

                throw new ReceiverException("Cannot parse timestamps.");
            } catch (DAOException e) {
                transaction.rollback();

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                messages.add(messageResource.get("dashboard.race.create.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
            throw new ReceiverException("Race data is invalid.");
        }
    }

    /**
     * Edit an existing race.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void editRace(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        String status = content.findParameter(RequestFieldConfig.Race.STATUS_FIELD);
        String raceNum = content.findParameter(RequestFieldConfig.Race.ID_FIELD);

        String[] participantsIds = content.findParameterValues(RequestFieldConfig.Race.SELECTED_HORSES_FIELD);
        String[] coeffs = content.findParameterValues(RequestFieldConfig.Race.HORSE_COEFFICIENTS_FIELD);

        RaceValidator validator = new RaceValidator();
        HashMap<Integer, BigDecimal> raceJockeys = new HashMap<>();

        if (validator.validateEditRaceForm(status, participantsIds, coeffs)) {
            final int raceId = Integer.parseInt(raceNum);

            for (int i = 0; i < participantsIds.length; i++) {
                raceJockeys.put(Integer.parseInt(participantsIds[i]), new BigDecimal(coeffs[i]));
            }

            RaceDAOImpl raceDAO = new RaceDAOImpl(true);
            ParticipantDAOImpl participantDAO = new ParticipantDAOImpl(true);

            TransactionManager transaction = new TransactionManager(raceDAO, participantDAO);
            transaction.beginTransaction();

            try {
                Race race = raceDAO.find(raceId);

                if (race == null) {
                    transaction.rollback();

                    messages.add(messageResource.get("dashboard.race.not_found"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Race[" + raceId + "] not found!");
                }

                if(race.isAvailable()) {
                    //TODO validate status
                    race.setStatus(status);
                    raceDAO.update(race);

                    // TODO check IDs of participants
                    participantDAO.update(raceJockeys);

                    // TODO return money to clients
                    transaction.commit();

                    messages.add(messageResource.get("dashboard.race.update.success"));
                    content.insertSessionAttribute(REQUEST_MESSAGES, messages);
                } else {
                    messages.add(messageResource.get("dashboard.race.update.fail"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);
                }

            } catch (DAOException e) {
                transaction.rollback();

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                messages.add(messageResource.get("dashboard.race.update.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
            throw new ReceiverException("Race horses are not completed.");
        }
    }
}
