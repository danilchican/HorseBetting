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
     * SQL queries for SuitDAOImpl.
     */
    private static final String SQL_ADD_SUIT = "INSERT INTO `suits` (name) VALUES (?);";
    private static final String SQL_FIND_SUIT_BY_NAME = "SELECT * FROM `suits` WHERE `name`=? LIMIT 1;";
    private static final String SQL_SELECT_SUITS = "SELECT * FROM `suits`;";
    private static final String SQL_SELECT_PART_SUITS = "SELECT * FROM `suits` ORDER BY `id` LIMIT ? OFFSET ?;";

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
     * Obtain part of suits.
     *
     * @param limit
     * @param offset
     * @return suits
     * @throws DAOException
     */
    @Override
    public List<Suit> obtainPart(int limit, int offset) throws DAOException {
        List<Suit> foundedSuits = new ArrayList<>();
        ResultSet suits;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PART_SUITS)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);

            suits = preparedStatement.executeQuery();

            while (suits.next()) {
                Suit suit = extractFrom(suits);
                foundedSuits.add(suit);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot obtain part of suits. " + e.getMessage(), e);
        }

        return foundedSuits;
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
