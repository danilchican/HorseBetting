package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.SuitDAO;
import com.epam.horsebetting.entity.Suit;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuitDAOImpl extends AbstractDAO<Suit> implements SuitDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for UserDAOImpl.
     */
    private static final String SQL_ADD_SUIT = "INSERT INTO `suits` (name) VALUES (?);";
    private static final String SQL_FIND_SUIT_BY_NAME = "SELECT * FROM `suits` WHERE `name`=? LIMIT 1;";
    private static final String SQL_SELECT_SUITS = "SELECT * FROM `suits`;";

    /**
     * Create a new suit.
     *
     * @param suit
     * @return suit
     * @throws DAOException
     */
    @Override
    public Suit create(Suit suit) throws DAOException {
        Suit createdUser;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_SUIT)) {
            preparedStatement.setString(1, suit.getName());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new suit to the database.");
            }

            createdUser = findByName(suit.getName());
        } catch (SQLException e) {
            throw new DAOException("Can't register new user. " + e.getMessage(), e);
        }

        return createdUser;
    }

    /**
     * Find all suits.
     *
     * @return list of suits
     * @throws DAOException
     */
    @Override
    public List<Suit> findAll() throws DAOException {
        List<Suit> foundedSuits = new ArrayList<>();
        ResultSet users;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_SUITS)) {
            users = preparedStatement.executeQuery();

            while (users.next()) {
                Suit suit = extractFrom(users);
                foundedSuits.add(suit);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve suits list. " + e.getMessage(), e);
        }

        return foundedSuits;
    }

    /**
     * Find suit by name.
     *
     * @param name
     * @return suit
     */
    @Override
    public Suit findByName(String name) throws DAOException {
        ResultSet resultSet;
        Suit suit = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_SUIT_BY_NAME)) {
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                suit = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return suit;
    }

    /**
     * Extract suit data from result set to suit instance.
     *
     * @param suitDataSet
     * @return suit
     * @throws SQLException
     */
    @Override
    public Suit extractFrom(ResultSet suitDataSet) throws SQLException {
        Suit suit = new Suit();

        suit.setId(suitDataSet.getInt("id"));
        suit.setName(suitDataSet.getString("name"));

        return suit;
    }
}
