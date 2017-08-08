package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.RaceDAOImpl;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.RaceReceiver;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        String title = content.findParameter("race-title");
        String place = content.findParameter("race-place");

        BigDecimal minRate = new BigDecimal(content.findParameter("race-min-rate"));
        int trackLength = Integer.parseInt(content.findParameter("race-track-length"));

        boolean isFinished = "1".equals(content.findParameter("race-is-finished"));

        // TODO move to constant
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parsedDate;

        Timestamp betEndDate = null;
        Timestamp startedAt = null;

        try {
            parsedDate = dateFormat.parse(content.findParameter("race-bet-end-date"));
            betEndDate = new Timestamp(parsedDate.getTime());

            parsedDate = dateFormat.parse(content.findParameter("race-started-at"));
            startedAt = new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            //...
        }

        //TODO Create validators

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

        try (RaceDAOImpl raceDAO = new RaceDAOImpl()) {
            Race createdRace = raceDAO.create(race);
            LOGGER.log(Level.DEBUG, "Created race: " + createdRace);
        } catch (DAOException e) {
            errors.add("Can't create new race.");
            content.insertSessionAttribute("errors", errors);

            // TODO add saving old inputs

            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }
}
