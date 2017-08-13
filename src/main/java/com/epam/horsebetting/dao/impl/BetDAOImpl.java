package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.BetDAO;
import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BetDAOImpl extends AbstractDAO<Bet> implements BetDAO {

    /**
     * SQL queries for BetDAOImpl.
     */
    private static final String SQL_ADD_BET = "INSERT INTO `bets` (user_id, participant_id, amount) VALUES (?,?,?);";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public BetDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new bet.
     *
     * @param bet
     * @param userId
     * @throws DAOException
     */
    @Override
    public void create(Bet bet, int userId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_BET)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, bet.getParticipantId());
            preparedStatement.setBigDecimal(3, bet.getAmount());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new bet to the database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't add a new bet. " + e.getMessage(), e);
        }
    }
}
