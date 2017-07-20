package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.UserDAO;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.util.HashManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    /**
     * SQL queries for UserDAOImpl.
     */
    private static final String SQL_ADD_USER_QUERY = "INSERT INTO `users` (name, email, password, role_id) VALUES (?,?,?,?);";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM `users` WHERE `email`=? LIMIT 1;";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM `users` WHERE `id`=? LIMIT 1;";
    private static final String SQL_ATTEMPT_AUTH_QUERY = "SELECT * FROM `users` WHERE `email`=? AND `password`=? LIMIT 1;";

    /**
     * Create a new user.
     *
     * @param user
     * @return user
     * @throws DAOException
     */
    @Override
    public User create(User user) throws DAOException {
        User createdUser = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_USER_QUERY)) {
            // modify to transaction
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRoleId());

            if (preparedStatement.executeUpdate() == 1) {
                createdUser = findByEmail(user.getEmail());
            }
        } catch (SQLException e) {
            throw new DAOException(e); // add comment to constructor
        }

        return createdUser;
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
            throw new DAOException(e); // add comment as param to constructor.
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
        user.setName(userDataSet.getString("name"));
        user.setEmail(userDataSet.getString("email"));
        user.setPassword(userDataSet.getString("password"));
        user.setCreatedAt(userDataSet.getString("created_at"));

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

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ATTEMPT_AUTH_QUERY)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, HashManager.make(password));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e); // add comment constructor
        }

        return user;
    }
}
