package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.UserDAO;
import com.epam.horsebetting.entity.Role;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.type.RoleType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAOImpl extends AbstractDAO<User> implements UserDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for UserDAOImpl.
     */
    private static final String SQL_FIND_USER_BY_EMAIL = "SELECT * FROM `users` WHERE `email`=? LIMIT 1;";
    private static final String SQL_FIND_USER_BY_REMEMBER_TOKEN = "SELECT * FROM `users` WHERE `remember_token`=? LIMIT 1;";
    private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM `users` WHERE `id`=? LIMIT 1;";
    private static final String SQL_FIND_ROLE_BY_ID = "SELECT * FROM `roles` WHERE `id`=? LIMIT 1;";
    private static final String SQL_SELECT_ALL_ROLES = "SELECT * FROM `roles` ORDER BY `id` ASC;";
    private static final String SQL_ATTEMPT_AUTH = "SELECT * FROM `users` WHERE `email`=? AND `password`=? LIMIT 1;";
    private static final String SQL_UPDATE_USER_SETTINGS = "UPDATE `users` SET `name`=? WHERE `id`=?;";
    private static final String SQL_UPDATE_USER_SECURITY = "UPDATE `users` SET `password`=? WHERE `id`=?;";
    private static final String SQL_UPDATE_USER_ROLE = "UPDATE `users` SET `role_id`=? WHERE `id`=?;";
    private static final String SQL_UPDATE_USER_BALANCE = "UPDATE `users` SET `balance`=? WHERE `id`=?;";
    private static final String SQL_UPDATE_USER_REMEMBER_TOKEN = "UPDATE `users` SET `remember_token`=? WHERE `id`=?;";
    private static final String SQL_COUNT_USERS = "SELECT COUNT(*) AS `total` FROM `users`;";
    private static final String SQL_ADD_USER = "INSERT INTO `users` (name, email, password, role_id, balance) VALUES (?,?,?,?,?);";
    private static final String SQL_SELECT_PART_USERS =
            "SELECT `id`, `role_id`, `name`, `email`, `balance`, `remember_token`, `created_at` FROM `users` LIMIT ? OFFSET ?;";

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
            preparedStatement.setBigDecimal(5, user.getBalance());

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
     * Find all user's roles.
     *
     * @return list of roles
     * @throws DAOException
     */
    @Override
    public List<Role> findAllRoles() throws DAOException {
        List<Role> foundedRoles = new ArrayList<>();
        ResultSet roles;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ROLES)) {
            roles = preparedStatement.executeQuery();

            while (roles.next()) {
                Role role = new Role();
                role.setId(roles.getInt(SQLFieldConfig.Role.ID));
                role.setName(roles.getString(SQLFieldConfig.Role.NAME));
                foundedRoles.add(role);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve role list. " + e.getMessage(), e);
        }

        return foundedRoles;
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
            throw new DAOException("Can't find user[id=" + id + "]. " + e.getMessage(), e);
        }

        return user;
    }

    /**
     * Find Role by id.
     *
     * @param id
     * @return role
     */
    @Override
    public Role findRole(int id) throws DAOException {
        ResultSet resultSet;
        Role role = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ROLE_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getInt(SQLFieldConfig.Role.ID));
                role.setName(resultSet.getString(SQLFieldConfig.Role.NAME));
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find role[id=" + id + "]. " + e.getMessage(), e);
        }

        return role;
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
            throw new DAOException("Can't find user[email=" + email + "]. " + e.getMessage(), e);
        }

        return user;
    }

    /**
     * Find user by remember token.
     *
     * @param rememberToken
     * @return user
     */
    @Override
    public User findByRememberToken(String rememberToken) throws DAOException {
        ResultSet resultSet;
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_REMEMBER_TOKEN)) {
            preparedStatement.setString(1, rememberToken);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find user[rememberToken=" + rememberToken + "]. " + e.getMessage(), e);
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
        User user = extractWithoutPassFrom(userDataSet);
        user.setPassword(userDataSet.getString(SQLFieldConfig.User.PASSWORD));

        return user;
    }

    /**
     * Extract user data from result set
     * to user instance
     * without password.
     *
     * @param userDataSet
     * @return
     * @throws SQLException
     */
    @Override
    public User extractWithoutPassFrom(ResultSet userDataSet) throws SQLException {
        User user = new User();

        user.setId(userDataSet.getInt(SQLFieldConfig.User.ID));
        user.setRole(RoleType.findById(userDataSet.getInt(SQLFieldConfig.User.ROLE_ID)));
        user.setBalance(userDataSet.getBigDecimal(SQLFieldConfig.User.BALANCE));
        user.setName(userDataSet.getString(SQLFieldConfig.User.NAME));
        user.setEmail(userDataSet.getString(SQLFieldConfig.User.EMAIL));
        user.setRememberToken(userDataSet.getString(SQLFieldConfig.User.REMEMBER_TOKEN));
        user.setCreatedAt(userDataSet.getTimestamp(SQLFieldConfig.User.CREATED_AT));

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
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't attempt to login with user's credentials.", e);
        }

        return user;
    }

    /**
     * Update user's role.
     *
     * @param userId
     * @param roleId
     * @throws DAOException
     */
    @Override
    public void updateRole(int userId, int roleId) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_ROLE)) {
            preparedStatement.setInt(1, roleId);
            preparedStatement.setInt(2, userId);

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update user's role.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update user's role. " + e.getMessage(), e);
        }
    }

    /**
     * Update user settings.
     *
     * @param user
     * @throws DAOException
     */
    @Override
    public void updateSettings(User user) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_SETTINGS)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update user's settings.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update user's settings. " + e.getMessage(), e);
        }
    }

    /**
     * Update user's security.
     *
     * @param user
     * @throws DAOException
     */
    @Override
    public void updateSecurity(User user) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_SECURITY)) {
            preparedStatement.setString(1, user.getPassword());
            preparedStatement.setInt(2, user.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update user's security.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update user's security. " + e.getMessage(), e);
        }
    }

    /**
     * Update user's balance.
     *
     * @param user
     * @throws DAOException
     */
    @Override
    public void updateBalance(User user) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_BALANCE)) {
            preparedStatement.setBigDecimal(1, user.getBalance());
            preparedStatement.setInt(2, user.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update user's balance.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update user's balance. " + e.getMessage(), e);
        }
    }

    /**
     * Update users balance.
     *
     * @param users
     * @throws DAOException
     */
    @Override
    public void updateBalanceForGroup(List<User> users) throws DAOException {
        int affectedUsers[];

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_BALANCE)) {
            for (User user : users) {
                preparedStatement.setBigDecimal(1, user.getBalance());
                preparedStatement.setInt(2, user.getId());

                preparedStatement.addBatch();
            }

            affectedUsers = preparedStatement.executeBatch();
            LOGGER.log(Level.DEBUG, "Count of updated users:" + Arrays.toString(affectedUsers));
        } catch (SQLException e) {
            throw new DAOException("Cannot update winners balance. " + e.getMessage(), e);
        }
    }

    /**
     * Set remember token of user.
     *
     * @param user
     * @throws DAOException
     */
    @Override
    public void setRememberToken(User user) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_USER_REMEMBER_TOKEN)) {
            preparedStatement.setString(1, user.getRememberToken());
            preparedStatement.setInt(2, user.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update user's remember_token.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update user's remember_token. " + e.getMessage(), e);
        }
    }

    /**
     * Get total count of users.
     *
     * @return total count
     * @throws DAOException
     */
    @Override
    public int getTotalCount() throws DAOException {
        ResultSet result;
        int totalCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_USERS)) {
            result = preparedStatement.executeQuery();

            while (result.next()) {
                totalCount = result.getInt(SQLFieldConfig.Common.TOTAL);
                LOGGER.log(Level.DEBUG, "Count of users: " + totalCount);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve total count. " + e.getMessage(), e);
        }

        return totalCount;
    }
}
