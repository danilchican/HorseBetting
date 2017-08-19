package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.RaceDAO;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class RaceDAOImpl extends AbstractDAO<Race> implements RaceDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for RaceDAOImpl.
     */
    private static final String SQL_SELECT_PART_RACES = "SELECT * FROM `races` ORDER BY `started_at` ASC LIMIT ? OFFSET ?;";
    private static final String SQL_FIND_RACE_BY_TITLE = "SELECT * FROM `races` WHERE `title`=? LIMIT 1;";
    private static final String SQL_FIND_RACE_BY_PARTICIPANT = "SELECT `r`.* FROM `races` AS `r` " +
            "JOIN `participants` AS `p` ON `r`.`id`=`p`.`race_id` WHERE `p`.`id`=? LIMIT 1;";
    private static final String SQL_FIND_RACE_BY_ID = "SELECT * FROM `races` WHERE `id`=? LIMIT 1;";
    private static final String SQL_COUNT_RACES = "SELECT COUNT(*) AS `total` FROM `races`;";
    private static final String SQL_UPDATE_RACE = "UPDATE `races` SET `status`=? WHERE `id`=?;";
    private static final String SQL_SELECT_NEAREST_RACES = "SELECT * FROM `races` WHERE `status` != ?" +
            " AND `started_at` > NOW() ORDER BY `started_at` ASC LIMIT ?;";
    private static final String SQL_ADD_RACE = "INSERT INTO `races` " +
            "(title, place, min_rate, track_length, bet_end_date, started_at) VALUES (?,?,?,?,?,?);";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public RaceDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new race.
     *
     * @param race
     * @return race
     * @throws DAOException
     */
    @Override
    public Race create(Race race) throws DAOException {
        Race createdRace;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_RACE)) {
            preparedStatement.setString(1, race.getTitle());
            preparedStatement.setString(2, race.getPlace());
            preparedStatement.setBigDecimal(3, race.getMinRate());
            preparedStatement.setInt(4, race.getTrackLength());
            preparedStatement.setTimestamp(5, race.getBetEndDate());
            preparedStatement.setTimestamp(6, race.getStartedAt());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new race to the database.");
            }

            createdRace = findByTitle(race.getTitle());
        } catch (SQLException e) {
            throw new DAOException("Can't add a new race. " + e.getMessage(), e);
        }

        return createdRace;
    }

    /**
     * Update race.
     *
     * @param race
     * @throws DAOException
     */
    @Override
    public void update(Race race) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_RACE)) {
            preparedStatement.setString(1, race.getStatus());
            preparedStatement.setInt(2, race.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update race.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update race. " + e.getMessage(), e);
        }
    }

    /**
     * Find race by id.
     *
     * @param id
     * @return race
     */
    @Override
    public Race find(int id) throws DAOException {
        ResultSet resultSet;
        Race race = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RACE_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                race = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find race[id=" + id + "]. " + e.getMessage(), e);
        }

        return race;
    }

    /**
     * Find race by title.
     *
     * @param title
     * @return race
     */
    @Override
    public Race findByTitle(String title) throws DAOException {
        ResultSet resultSet;
        Race race = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RACE_BY_TITLE)) {
            preparedStatement.setString(1, title);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                race = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find race: " + e.getMessage(), e);
        }

        return race;
    }

    /**
     * Find race by participant id.
     *
     * @param participantId
     * @return race
     */
    @Override
    public Race findByParticipant(int participantId) throws DAOException {
        ResultSet resultSet;
        Race race = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RACE_BY_PARTICIPANT)) {
            preparedStatement.setInt(1, participantId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                race = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find race: " + e.getMessage(), e);
        }

        return race;
    }

    /**
     * Obtain part of races.
     *
     * @param limit
     * @param offset
     * @return races
     * @throws DAOException
     */
    @Override
    public List<Race> obtainPart(int limit, int offset) throws DAOException {
        List<Race> foundedRaces = new ArrayList<>();
        ResultSet races;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PART_RACES)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            races = preparedStatement.executeQuery();

            while (races.next()) {
                Race race = extractFrom(races);
                foundedRaces.add(race);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve races list. " + e.getMessage(), e);
        }

        return foundedRaces;
    }

    /**
     * Obtain nearest of races.
     *
     * @param limit
     * @param offset
     * @return races
     * @throws DAOException
     */
    @Override
    public List<Race> obtainNearest(int limit, int offset) throws DAOException {
        List<Race> foundedRaces = new ArrayList<>();
        ResultSet races;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_NEAREST_RACES)) {
            preparedStatement.setBoolean(1, true);
            preparedStatement.setInt(2, limit);
            races = preparedStatement.executeQuery();

            while (races.next()) {
                Race race = extractFrom(races);
                foundedRaces.add(race);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve races list. " + e.getMessage(), e);
        }

        return foundedRaces;
    }

    /**
     * Get total count of races.
     *
     * @return total count
     * @throws DAOException
     */
    @Override
    public int getTotalCount() throws DAOException {
        ResultSet result;
        int totalCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_RACES)) {
            result = preparedStatement.executeQuery();

            while (result.next()) {
                totalCount = result.getInt(SQLFieldConfig.TOTAL);
                LOGGER.log(Level.DEBUG, "Count of races: " + totalCount);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve total count. " + e.getMessage(), e);
        }

        return totalCount;
    }

    /**
     * Extract race data from result set to race instance.
     *
     * @param raceDataSet
     * @return race instance
     * @throws SQLException
     */
    @Override
    public Race extractFrom(ResultSet raceDataSet) throws SQLException {
        Race race = new Race();

        race.setId(raceDataSet.getInt(SQLFieldConfig.Race.ID));
        race.setTitle(raceDataSet.getString(SQLFieldConfig.Race.TITLE));
        race.setPlace(raceDataSet.getString(SQLFieldConfig.Race.PLACE));
        race.setMinRate(raceDataSet.getBigDecimal(SQLFieldConfig.Race.MIN_RATE));
        race.setTrackLength(raceDataSet.getInt(SQLFieldConfig.Race.TRACK_LENGTH));
        race.setStatus(raceDataSet.getString(SQLFieldConfig.Race.STATUS));
        race.setBetEndDate(raceDataSet.getTimestamp(SQLFieldConfig.Race.BET_END_DATE));
        race.setStartedAt(raceDataSet.getTimestamp(SQLFieldConfig.Race.STARTED_AT));
        race.setCreatedAt(raceDataSet.getTimestamp(SQLFieldConfig.Race.CREATED_AT));

        return race;
    }
}
