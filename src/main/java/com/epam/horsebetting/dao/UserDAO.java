package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.util.HashManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    /**
     * SQL queries for UserDAO.
     */
    private static final String SQL_ADD_USER_QUERY = "INSERT INTO `users` (name, email, password, role_id) VALUES (?,?,?,?);";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM `users` WHERE `email`=? LIMIT 1;";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM `users` WHERE `id`=? LIMIT 1;";
    private static final String SQL_ATTEMPT_AUTH_QUERY = "SELECT * FROM `users` WHERE `email`=? AND `password`=? LIMIT 1;";

    /**
     * Find all entities.
     *
     * @return entity list
     * @throws DAOException
     */
    @Override
    public List<User> findAll() throws DAOException {
        return null;
    }

    /**
     * Find entity by id.
     *
     * @param id
     * @return entity
     */
    @Override
    public User find(int id) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = fillUserData(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e); // add comment as param to constructor.
        }

        return user;
    }

    /**
     * Find entity by email.
     *
     * @param email
     * @return entity
     */
    public User findByEmail(String email) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = fillUserData(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return user;
    }

    /**
     * Create new entity.
     *
     * @param user
     * @return boolean
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
     * Delete entity by id.
     *
     * @param id
     * @return boolean
     */
    @Override
    public boolean delete(int id) {
        return false;
    }

    /**
     * Delete entity by object.
     *
     * @param entity
     * @return boolean
     */
    @Override
    public boolean delete(User entity) {
        return false;
    }

    /**
     * Update entity.
     *
     * @param entity
     * @return updated entity
     */
    @Override
    public User update(User entity) {
        return null;
    }

    /**
     * Fill user data by result set.
     *
     * @param userData
     * @return
     * @throws SQLException
     */
    private User fillUserData(ResultSet userData) throws SQLException {
        User user = new User();

        user.setId(userData.getInt("id"));
        user.setRole(RoleType.findById(userData.getInt("role_id")));
        user.setName(userData.getString("name"));
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));
        user.setCreatedAt(userData.getString("created_at"));

        return user;
    }

    /**
     * Attempt user credentials.
     *
     * @param email
     * @param password
     * @return user instance
     * @throws DAOException
     */
    public User attempt(String email, String password) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ATTEMPT_AUTH_QUERY)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, HashManager.make(password));
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = fillUserData(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException(e); // add comment constructor
        }

        return user;
    }
}
