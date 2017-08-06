package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface SuitDAO {

    /**
     * Create a new suit.
     *
     * @param suit
     * @return suit
     * @throws DAOException
     */
    Suit create(Suit suit) throws DAOException;


    /**
     * Update suit.
     *
     * @param suit
     * @return boolean
     */
    boolean update(Suit suit) throws DAOException;

    /**
     * Remove suit.
     *
     * @param suit
     * @return boolean
     * @throws DAOException
     */
    boolean remove(Suit suit) throws DAOException;


    /**
     * Obtain part of suits.
     *
     * @param limit
     * @param offset
     * @return suits
     * @throws DAOException
     */
    List<Suit> obtainPart(int limit, int offset) throws DAOException;

    /**
     * Find all suits.
     *
     * @return list of suits
     * @throws DAOException
     */
    List<Suit> findAll() throws DAOException;


    /**
     * Find suit by name.
     *
     * @param name
     * @return suit
     */
    Suit findByName(String name) throws DAOException;

    /**
     * Extract suit data from result set to suit instance.
     *
     * @param suitDataSet
     * @return suit
     * @throws SQLException
     */
    Suit extractFrom(ResultSet suitDataSet) throws SQLException;
}
