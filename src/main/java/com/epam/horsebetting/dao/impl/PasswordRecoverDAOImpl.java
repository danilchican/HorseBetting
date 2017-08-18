package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.PasswordRecoverDAO;
import com.epam.horsebetting.entity.PasswordRecover;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordRecoverDAOImpl extends AbstractDAO<PasswordRecover>  implements PasswordRecoverDAO {

    /**
     * SQL queries for SuitDAOImpl.
     */
    private static final String SQL_FIND_RECOVER_BY_TOKEN = "SELECT * FROM `password_resets` WHERE `token`=? LIMIT 1;";
    private static final String SQL_ADD_RECOVER = "INSERT INTO `password_resets` (email, token) VALUES (?,?);";
    private static final String SQL_REMOVE_RECOVER = "DELETE FROM `password_resets` WHERE `token`=?;";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public PasswordRecoverDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new recover.
     *
     * @param recover
     * @throws DAOException
     */
    @Override
    public void create(PasswordRecover recover) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_RECOVER)) {
            preparedStatement.setString(1, recover.getEmail());
            preparedStatement.setString(2, recover.getToken());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new recover to the database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't add a new recover. " + e.getMessage(), e);
        }
    }

    /**
     * Remove a recover.
     *
     * @param recover
     * @throws DAOException
     */
    @Override
    public void remove(PasswordRecover recover) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_RECOVER)) {
            preparedStatement.setString(1, recover.getToken());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't remove recover from the database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't remove recover. " + e.getMessage(), e);
        }
    }

    /**
     * Find recover by token.
     *
     * @param token
     * @return recover
     */
    @Override
    public PasswordRecover findByToken(String token) throws DAOException {
        ResultSet resultSet;
        PasswordRecover recover = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_RECOVER_BY_TOKEN)) {
            preparedStatement.setString(1, token);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                recover = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find recover[token=" + token + "]. " + e.getMessage(), e);
        }

        return recover;
    }

    /**
     * Extract data from result set to password recover.
     *
     * @param dataSet
     * @return recover
     * @throws SQLException
     */
    private PasswordRecover extractFrom(ResultSet dataSet) throws SQLException {
        PasswordRecover recover = new PasswordRecover();

        recover.setId(dataSet.getInt(SQLFieldConfig.PasswordRecover.ID));
        recover.setEmail(dataSet.getString(SQLFieldConfig.PasswordRecover.EMAIL));
        recover.setToken(dataSet.getString(SQLFieldConfig.PasswordRecover.TOKEN));
        recover.setCreatedAt(dataSet.getTimestamp(SQLFieldConfig.PasswordRecover.CREATED_AT));

        return recover;
    }
}
