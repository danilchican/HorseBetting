package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.RaceDAO;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RaceDAOImpl extends AbstractDAO<Race> implements RaceDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for RaceDAOImpl.
     */
    private static final String SQL_SELECT_ALL_RACES = "SELECT * FROM `races`;";
    private static final String SQL_SELECT_PART_RACES = "SELECT * FROM `races` LIMIT ? OFFSET ?;";
    private static final String SQL_COUNT_RACES = "SELECT COUNT(*) AS `total` FROM `races`;";

    /**
     * Find all races.
     *
     * @return list of races
     * @throws DAOException
     */
    @Override
    public List<Race> findAll() throws DAOException {
        List<Race> foundedRaces = new ArrayList<>();
        ResultSet races;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_RACES)) {
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
                LOGGER.log(Level.DEBUG, "Race was added to list: " + race);
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
                totalCount = result.getInt("total");
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
    private Race extractFrom(ResultSet raceDataSet) throws SQLException {
        Race race = new Race();

        race.setId(raceDataSet.getInt("id"));
        race.setTitle(raceDataSet.getString("title"));
        race.setPlace(raceDataSet.getString("place"));
        race.setMinRate(raceDataSet.getBigDecimal("min_rate"));
        race.setTrackLength(raceDataSet.getInt("track_length"));
        race.setFinished(raceDataSet.getBoolean("is_finished"));
        race.setStartedAt(raceDataSet.getTimestamp("started_at"));
        race.setCreatedAt(raceDataSet.getTimestamp("created_at"));

        return race;
    }
}