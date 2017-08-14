package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.BetDAO;
import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BetDAOImpl extends AbstractDAO<Bet> implements BetDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for BetDAOImpl.
     */
    private static final String SQL_FIND_BET_BY_ID = "SELECT * FROM `bets` WHERE `id`=? LIMIT 1;";
    private static final String SQL_REMOVE_BET_BY_ID = "DELETE FROM `bets` WHERE `id`=?;";
    private static final String SQL_ADD_BET = "INSERT INTO `bets` (user_id, participant_id, amount) VALUES (?,?,?);";
    private static final String SQL_SELECT_PART_BETS = "SELECT `b`.`id`, `b`.`user_id`, `b`.`participant_id`, " +
            "`b`.`amount`, `b`.`created_at`, `h`.`name` AS `participant_name` FROM `bets` AS `b` " +
            "JOIN `participants` AS `p` ON `b`.`participant_id`=`p`.`id` " +
            "JOIN `horses` AS `h` ON `p`.`horse_id`=`h`.`id` " +
            "WHERE `b`.`user_id`=? LIMIT ? OFFSET ?;";
    private static final String SQL_COUNT_BETS = "SELECT COUNT(*) AS `total` FROM `bets`;";

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

    /**
     * Find bet by id.
     *
     * @param id
     * @return bet
     */
    @Override
    public Bet find(int id) throws DAOException {
        ResultSet resultSet;
        Bet bet = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BET_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bet = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find bet by id[" + id + "]. " + e.getMessage(), e);
        }

        return bet;
    }

    /**
     * Remove bet by id.
     *
     * @param id
     * @throws DAOException
     */
    @Override
    public void remove(int id) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_BET_BY_ID)) {
            preparedStatement.setInt(1, id);

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't remove bet from the database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't remove bet. " + e.getMessage(), e);
        }
    }

    /**
     * Obtain part of bets.
     *
     * @param userId
     * @param limit
     * @param offset
     * @return bets
     * @throws DAOException
     */
    @Override
    public List<Bet> obtainPart(int userId, int limit, int offset) throws DAOException {
        List<Bet> foundedBets = new ArrayList<>();
        ResultSet bets;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PART_BETS)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, limit);
            preparedStatement.setInt(3, offset);
            bets = preparedStatement.executeQuery();

            while (bets.next()) {
                Bet bet = extractFrom(bets);
                foundedBets.add(bet);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve horses list. " + e.getMessage(), e);
        }

        return foundedBets;
    }

    /**
     * Get total count of bets.
     *
     * @return total count
     * @throws DAOException
     */
    @Override
    public int getTotalCount() throws DAOException {
        ResultSet result;
        int totalCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_BETS)) {
            result = preparedStatement.executeQuery();

            while (result.next()) {
                totalCount = result.getInt(SQLFieldConfig.TOTAL);
                LOGGER.log(Level.DEBUG, "Count of bets: " + totalCount);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve total count. " + e.getMessage(), e);
        }

        return totalCount;
    }

    /**
     * Extract bet data from result set to bet instance.
     *
     * @param dataSet
     * @return bet instance
     * @throws SQLException
     */
    private Bet extractFrom(ResultSet dataSet) throws SQLException {
        Bet bet = new Bet();

        bet.setId(dataSet.getInt(SQLFieldConfig.Bet.ID));
        bet.setUserId(dataSet.getInt(SQLFieldConfig.Bet.USER_ID));
        bet.setParticipantId(dataSet.getInt(SQLFieldConfig.Bet.PARTICIPANT_ID));
        bet.setAmount(dataSet.getBigDecimal(SQLFieldConfig.Bet.AMOUNT));
        bet.setCreatedAt(dataSet.getTimestamp(SQLFieldConfig.Bet.CREATED_AT));

        if (hasColumn(dataSet, SQLFieldConfig.Bet.PARTICIPANT_NAME)) {
            bet.insertAttribute(SQLFieldConfig.Bet.PARTICIPANT_NAME,
                    dataSet.getString(SQLFieldConfig.Bet.PARTICIPANT_NAME));
        }

        return bet;
    }
}
