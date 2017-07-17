package com.epam.danilchican.betting.dao;

import com.epam.danilchican.betting.entity.User;
import com.epam.danilchican.betting.exception.DatabaseException;
import com.epam.danilchican.betting.type.RoleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SQL_ADD_USER_QUERY = "INSERT INTO `users` (name, email, password, role_id) VALUES (?,?,?,?);";
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM `users` WHERE `email`=? LIMIT 1;";

    /**
     * Find all entities.
     *
     * @return entity list
     * @throws DatabaseException
     */
    @Override
    public List<User> findAll() throws DatabaseException {
        return null;
    }

    /**
     * Find entity by id.
     *
     * @param id
     * @return entity
     */
    @Override
    public User find(int id) {
        return null;
    }

    /**
     * Find entity by email.
     *
     * @param email
     * @return entity
     */
    public User findByEmail(String email) throws DatabaseException {
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        User user = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = fillUserData(resultSet);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return user;
    }

    /**
     * Create new entity.
     *
     * @param user
     * @return boolean
     * @throws DatabaseException
     */
    @Override
    public User create(User user) throws DatabaseException {
        PreparedStatement preparedStatement;
        User createdUser = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_USER_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRoleId());

            preparedStatement.executeUpdate();
            createdUser = findByEmail(user.getEmail());
        } catch (SQLException e) {
            throw new DatabaseException(e);
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
}
