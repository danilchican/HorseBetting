package com.epam.danilchican.betting.dao;

import com.epam.danilchican.betting.entity.User;
import com.epam.danilchican.betting.exception.DatabaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    private static final Logger LOGGER = LogManager.getLogger();


    private static final String SQL_ADD_USER_QUERY = "INSERT INTO `users` (name, email, password, role_id) VALUES (?,?,?,?);";

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
     * Create new entity.
     *
     * @param user
     * @return boolean
     * @throws DatabaseException
     */
    @Override
    public boolean create(User user) throws DatabaseException {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(SQL_ADD_USER_QUERY);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, user.getRoleId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

        return true;
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
}
