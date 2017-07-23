package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface HorseDAO {

    /**
     * Find all horses.
     *
     * @return list of horses
     * @throws DAOException
     */
    List<Horse> findAll() throws DAOException;

    /**
     * Obtain part of horses.
     *
     * @param limit
     * @param offset
     * @return horses
     * @throws DAOException
     */
    List<Horse> obtainPart(int limit, int offset) throws DAOException;

    /**
     * Get total count of horses.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
