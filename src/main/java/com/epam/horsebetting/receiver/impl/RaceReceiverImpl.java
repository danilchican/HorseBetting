package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.dao.impl.RaceDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.RaceReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.DateFormatter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
        String title = content.findParameter(RequestFieldConfig.Race.TITLE_FIELD);
        String place = content.findParameter(RequestFieldConfig.Race.PLACE_FIELD);

        BigDecimal minRate = new BigDecimal(content.findParameter(RequestFieldConfig.Race.MIN_RATE_FIELD));
        int trackLength = Integer.parseInt(content.findParameter(RequestFieldConfig.Race.TRACK_LENGTH_FIELD));

        boolean isFinished = "1".equals(content.findParameter(RequestFieldConfig.Race.FINISHED_FIELD));

        SimpleDateFormat dateFormat = new SimpleDateFormat(DateFormatter.DEFAULT_DATE_FORMAT);
        Date parsedDate;

        Timestamp betEndDate;
        Timestamp startedAt;

        try {
            parsedDate = dateFormat.parse(content.findParameter(RequestFieldConfig.Race.BET_END_DATE_FIELD));
            betEndDate = new Timestamp(parsedDate.getTime());

            parsedDate = dateFormat.parse(content.findParameter(RequestFieldConfig.Race.STARTED_AT_FIELD));
            startedAt = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            // TODO fix
            throw new ReceiverException("Cannot retrieve dates.");
        }

        // TODO add checking of numbers
        HashMap<Integer, BigDecimal> raceHorses = new HashMap<>();

        String[] horses = content.findParameterValues(RequestFieldConfig.Race.SELECTED_HORSES_FIELD);
        String[] coeffs = content.findParameterValues(RequestFieldConfig.Race.HORSE_COEFFICIENTS_FIELD);

        if(horses.length == coeffs.length) {
            for (int i = 0; i < horses.length; i++) {
                raceHorses.put(Integer.parseInt(horses[i]), new BigDecimal(coeffs[i]));
            }
        } else {
            // TODO fix
            throw new ReceiverException("Race horses are not completed.");
        }

        // TODO Create validators

        Race race = new Race();

        race.setTitle(title);
        race.setPlace(place);
        race.setMinRate(minRate);
        race.setTrackLength(trackLength);
        race.setFinished(isFinished);

        race.setBetEndDate(betEndDate);
        race.setStartedAt(startedAt);

        LOGGER.log(Level.DEBUG, "Want create race: " + race);
        ArrayList<String> errors = new ArrayList<>();

        RaceDAOImpl raceDAO = new RaceDAOImpl(true);

        TransactionManager transaction = new TransactionManager(raceDAO);
        transaction.beginTransaction();

        try {
            Race createdRace = raceDAO.create(race);
            LOGGER.log(Level.DEBUG, "Created race: " + createdRace);

            // TODO save horses
            raceDAO.createHorsesToRace(raceHorses, createdRace);

            transaction.commit();
        } catch (DAOException e) {
            transaction.rollback();

            errors.add("Can't create new race.");
            content.insertSessionAttribute("errors", errors);

            // TODO add saving old inputs
            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
        }
    }
}
