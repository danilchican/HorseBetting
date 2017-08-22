package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface RaceDAO {

    /**
     * Create a new race.
     *
     * @param race
     * @return race
     * @throws DAOException
     */
    Race create(Race race) throws DAOException;

    /**
     * Update race status.
     *
     * @param race
     * @throws DAOException
     */
    void updateStatus(Race race) throws DAOException;

    /**
     * Find race by id.
     *
     * @param id
     * @return race
     */
    Race find(int id) throws DAOException;

    /**
     * Find race by title.
     *
     * @param title
     * @return race
     */
    Race findByTitle(String title) throws DAOException;

    /**
     * Find race by participant id.
     *
     * @param participantId
     * @return race
     */
    Race findByParticipant(int participantId) throws DAOException;

    /**
     * Obtain part of races.
     *
     * @param limit
     * @param offset
     * @return races
     * @throws DAOException
     */
    List<Race> obtainPart(int limit, int offset) throws DAOException;

    /**
     * Obtain nearest of races.
     *
     * @param limit
     * @param offset
     * @return races
     * @throws DAOException
     */
    List<Race> obtainNearest(int limit, int offset) throws DAOException;

    /**
     * Get total count of races.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;

    /**
     * Extract race data from result set to race instance.
     *
     * @param raceDataSet
     * @return race instance
     * @throws SQLException
     */
    Race extractFrom(ResultSet raceDataSet) throws SQLException;
}
