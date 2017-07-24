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
