package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;

import java.util.List;

public interface RaceDAO {

    /**
     * Find all races.
     *
     * @return list of races
     * @throws DAOException
     */
    List<Race> findAll() throws DAOException;

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
     * Get total count of races.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
