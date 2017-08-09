package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.UserDAO;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.util.HashManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for UserDAOImpl.
     */
    private static final String SQL_ADD_USER = "INSERT INTO `users` (name, email, password, role_id) VALUES (?,?,?,?);";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM `users` WHERE `email`=? LIMIT 1;";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM `users` WHERE `id`=? LIMIT 1;";
    private static final String SQL_ATTEMPT_AUTH = "SELECT * FROM `users` WHERE `email`=? AND `password`=? LIMIT 1;";
    private static final String SQL_SELECT_ALL_USERS = "SELECT `id`, `role_id`, `name`, `email`, `balance`, `created_at` FROM `users`;";
    private static final String SQL_SELECT_PART_USERS = "SELECT `id`, `role_id`, `name`, `email`, `balance`, `created_at` FROM `users` LIMIT ? OFFSET ?;";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public UserDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new user.
     *
     * @param user
     * @return user
     * @throws DAOException
     */
    @Override
    public User create(User user) throws DAOException {
        User createdUser;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_USER)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRoleId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new user to the database.");
            }

            createdUser = findByEmail(user.getEmail());
        } catch (SQLException e) {
            throw new DAOException("Can't register new user. " + e.getMessage(), e);
        }

        return createdUser;
    }

    /**
     * Find all users.
     *
     * @return list of users
     * @throws DAOException
     */
    @Override
    public List<User> findAll() throws DAOException {
        List<User> foundedUsers = new ArrayList<>();
        ResultSet users;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_USERS)) {
            users = preparedStatement.executeQuery();

            while (users.next()) {
                User user = extractWithoutPassFrom(users);
                foundedUsers.add(user);
                LOGGER.log(Level.DEBUG, "User was added to list: " + user);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve users list. " + e.getMessage(), e);
        }

        return foundedUsers;
    }

    /**
     * Obtain part of users.
     *
     * @param limit
     * @param offset
     * @return users
     * @throws DAOException
     */
    @Override
    public List<User> obtainPart(int limit, int offset) throws DAOException {
        List<User> foundedUsers = new ArrayList<>();
        ResultSet users;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PART_USERS)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            users = preparedStatement.executeQuery();

            while (users.next()) {
                User user = extractWithoutPassFrom(users);
                foundedUsers.add(user);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot obtain part of users. " + e.getMessage(), e);
        }

        return foundedUsers;
    }

    /**
     * Find user by id.
     *
     * @param id
     * @return user
     */
    @Override
    public User find(int id) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find user by id[" + id + "]. " + e.getMessage(), e);
        }

        return user;
    }

    /**
     * Find user by email.
     *
     * @param email
     * @return user
     */
    @Override
    public User findByEmail(String email) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return user;
    }

    /**
     * Extract user data from result set to user instance.
     *
     * @param userDataSet
     * @return user
     * @throws SQLException
     */
    @Override
    public User extractFrom(ResultSet userDataSet) throws SQLException {
        User user = new User();

        user.setId(userDataSet.getInt("id"));
        user.setRole(RoleType.findById(userDataSet.getInt("role_id")));
        user.setBalance(userDataSet.getBigDecimal("balance"));
        user.setName(userDataSet.getString("name"));
        user.setEmail(userDataSet.getString("email"));
        user.setPassword(userDataSet.getString("password"));
        user.setCreatedAt(userDataSet.getTimestamp("created_at"));

        return user;
    }

    /**
     * Extract user data from result set to user instance
     * without password.
     *
     * @param userDataSet
     * @return
     * @throws SQLException
     */
    @Override
    public User extractWithoutPassFrom(ResultSet userDataSet) throws SQLException {
        User user = new User();

        user.setId(userDataSet.getInt("id"));
        user.setRole(RoleType.findById(userDataSet.getInt("role_id")));
        user.setBalance(userDataSet.getBigDecimal("balance"));
        user.setName(userDataSet.getString("name"));
        user.setEmail(userDataSet.getString("email"));
        user.setCreatedAt(userDataSet.getTimestamp("created_at"));

        return user;
    }

    /**
     * Attempt user credentials.
     *
     * @param email
     * @param password
     * @return user
     * @throws DAOException
     */
    @Override
    public User attempt(String email, String password) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ATTEMPT_AUTH)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, HashManager.make(password));

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't attempt to login with user credentials.", e);
        }

        return user;
    }
}
