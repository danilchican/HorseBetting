package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserDAO {

    /**
     * Create a new user.
     *
     * @param user
     * @return user
     * @throws DAOException
     */
    User create(User user) throws DAOException;

    /**
     * Find user by id.
     *
     * @param id
     * @return user
     */
    User find(int id) throws DAOException;

    /**
     * Find user by email.
     *
     * @param email
     * @return user
     */
    User findByEmail(String email) throws DAOException;

    /**
     * Extract user data from result set to user instance.
     *
     * @param userDataSet
     * @return user
     * @throws SQLException
     */
    User extractFrom(ResultSet userDataSet) throws SQLException;

    /**
     * Attempt user credentials.
     *
     * @param email
     * @param password
     * @return user
     * @throws DAOException
     */
    User attempt(String email, String password) throws DAOException;
}
