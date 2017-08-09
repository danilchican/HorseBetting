package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;

import java.math.BigDecimal;
import java.util.HashMap;
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
     * Find race by title.
     *
     * @param title
     * @return race
     */
    Race findByTitle(String title) throws DAOException;

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
     * Create horses to race.
     *
     * @param horses
     * @param race
     * @throws DAOException
     */
    void createHorsesToRace(HashMap<Integer, BigDecimal> horses, Race race) throws DAOException;

    /**
     * Get total count of races.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
