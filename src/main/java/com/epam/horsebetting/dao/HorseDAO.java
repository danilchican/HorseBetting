package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;

import java.util.List;

public interface HorseDAO {

    /**
     * Create a new horse.
     *
     * @param horse
     * @return horse
     * @throws DAOException
     */
    Horse create(Horse horse) throws DAOException;

    /**
     * Update horse.
     *
     * @param horse
     * @throws DAOException
     */
    void update(Horse horse) throws DAOException;

    /**
     * Remove horse.
     *
     * @param horse
     * @throws DAOException
     */
    void remove(Horse horse) throws DAOException;

    /**
     * Find horse by id.
     *
     * @param id
     * @return horse
     */
    Horse find(int id) throws DAOException;

    /**
     * Find horse by name.
     *
     * @param name
     * @return horse
     */
    Horse findByName(String name) throws DAOException;

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
     * Find all horses.
     *
     * @return horses
     * @throws DAOException
     */
    List<Horse> findAll() throws DAOException;

    /**
     * Get total count of horses.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
