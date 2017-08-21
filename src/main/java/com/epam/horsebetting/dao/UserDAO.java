package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
     * Find all users.
     *
     * @return list of users
     * @throws DAOException
     */
    List<User> findAll() throws DAOException;

    /**
     * Obtain part of users.
     *
     * @param limit
     * @param offset
     * @return users
     * @throws DAOException
     */
    List<User> obtainPart(int limit, int offset) throws DAOException;

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
     * Extract user's data from result set to user instance.
     *
     * @param userDataSet
     * @return user
     * @throws SQLException
     */
    User extractFrom(ResultSet userDataSet) throws SQLException;

    /**
     * Extract user's data from result set to user instance
     * without password.
     *
     * @param userDataSet
     * @return
     * @throws SQLException
     */
    User extractWithoutPassFrom(ResultSet userDataSet) throws SQLException;

    /**
     * Attempt user's credentials.
     *
     * @param email
     * @param password
     * @return user
     * @throws DAOException
     */
    User attempt(String email, String password) throws DAOException;

    /**
     * Update user's settings.
     *
     * @param user
     * @throws DAOException
     */
    void updateSettings(User user) throws DAOException;

    /**
     * Update user's security.
     *
     * @param user
     * @throws DAOException
     */
    void updateSecurity(User user) throws DAOException;

    /**
     * Update user's balance.
     *
     * @param user
     * @throws DAOException
     */
    void updateBalance(User user) throws DAOException;

    /**
     * Update users balance.
     *
     * @param users
     * @throws DAOException
     */
    void updateBalanceForGroup(List<User> users) throws DAOException;

    /**
     * Set remember token of user.
     *
     * @param user
     * @throws DAOException
     */
    void setRememberToken(User user) throws DAOException;

    /**
     * Get total count of users.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
