package com.epam.danilchican.betting.receiver;

import com.epam.danilchican.betting.dao.UserDAO;
import com.epam.danilchican.betting.entity.User;
import com.epam.danilchican.betting.exception.DatabaseException;
import com.epam.danilchican.betting.exception.ReceiverException;
import com.epam.danilchican.betting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserReceiver extends AbstractReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Register a new user.
     *
     * @param content
     */
    public void register(RequestContent content) throws ReceiverException {
        LOGGER.log(Level.INFO, "Execution register() method: " + this.getClass().getName());

        String name = String.valueOf(content.findParameter("name"));
        String email = String.valueOf(content.findParameter("email"));
        String password = String.valueOf(content.findParameter("password"));
        String passwordConfirmation = String.valueOf(content.findParameter("password_confirmation"));

        LOGGER.log(Level.DEBUG, "Attributes: [email: " + email + ", name: " + name + ", password: " + password + "]");

        try (UserDAO userDAO = new UserDAO()) {
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(password);
            user.setRoleId(1);

            userDAO.create(user);
        } catch (DatabaseException e) {
            throw new ReceiverException("Database Error", e);
        }
    }

    /**
     * Login a user.
     *
     * @param content
     */
    public void login(RequestContent content) {
        // calling DAO
        // set data to content
    }

    /**
     * Logout a user.
     *
     * @param content
     */
    public void logout(RequestContent content) {
        // calling DAO
        // set data to content
    }
}
