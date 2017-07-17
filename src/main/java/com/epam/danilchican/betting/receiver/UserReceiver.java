package com.epam.danilchican.betting.receiver;

import com.epam.danilchican.betting.dao.UserDAO;
import com.epam.danilchican.betting.entity.User;
import com.epam.danilchican.betting.exception.DatabaseException;
import com.epam.danilchican.betting.exception.ReceiverException;
import com.epam.danilchican.betting.request.RequestContent;
import com.epam.danilchican.betting.type.RoleType;
import com.epam.danilchican.betting.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

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

        this.setPageSubTitle("Регистрация");
        super.setDefaultContentAttributes(content);

        String name = content.findParameter("name");
        String email = content.findParameter("email");
        String password = content.findParameter("password");
        String passwordConfirmation = content.findParameter("password_confirmation");

        LOGGER.log(Level.DEBUG, "User data[name="
                + name + ",email="
                + email + ",password="
                + password + ", confirmation="
                + passwordConfirmation);

        UserValidator validator = new UserValidator();

        if (validator.validateRegistration(name, email, password, passwordConfirmation)) {
            User createdUser = null;

            try (UserDAO userDAO = new UserDAO()) {
                User user = new User();

                user.setEmail(email);
                user.setName(name);
                user.setPassword(password);
                user.setRole(RoleType.CLIENT);

                createdUser = userDAO.create(user);
            } catch (DatabaseException e) {
                throw new ReceiverException("Database Error", e);
            }

            this.auth(content, createdUser);
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            LOGGER.log(Level.DEBUG, "Old inputs:");

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
                LOGGER.log(Level.DEBUG, entry.getKey() + ": " + entry.getValue());
            }

            throw new ReceiverException("Invalid registration data.");
        }
    }

    /**
     * Authenticate user in system.
     *
     * @param content
     * @param user
     * @throws ReceiverException
     */
    private void auth(RequestContent content, User user) throws ReceiverException {
        if(user == null) {
            throw new ReceiverException("Cannot authenticate user. User is null.");
        }

        LOGGER.log(Level.DEBUG, "Auth user: " + user);
        content.insertSessionAttribute("authorized", user.getId());
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
