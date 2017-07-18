package com.epam.horsebetting.receiver;

import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DatabaseException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.validator.UserValidator;
import com.epam.horsebetting.dao.UserDAO;
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
                throw new ReceiverException("Database Error: " + e.getMessage(), e);
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
        if (user == null) {
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
    public void login(RequestContent content) throws ReceiverException {
        LOGGER.log(Level.INFO, "Execution login() method: " + this.getClass().getName());

        this.setPageSubTitle("Авторизация");
        super.setDefaultContentAttributes(content);

        String email = content.findParameter("email");
        String password = content.findParameter("password");

        LOGGER.log(Level.DEBUG, "User data[name=" + email + ",password=" + password + "]");

        UserValidator validator = new UserValidator();

        if (validator.validateLogin(email, password)) {
            User createdUser;

            try (UserDAO userDAO = new UserDAO()) {
                createdUser = userDAO.attempt(email, password);
            } catch (DatabaseException e) {
                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }

            this.auth(content, createdUser);
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            LOGGER.log(Level.DEBUG, "Old inputs:");

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
                LOGGER.log(Level.DEBUG, entry.getKey() + ": " + entry.getValue());
            }

            throw new ReceiverException("Invalid authentication data.");
        }
    }

    /**
     * Logout a user.
     *
     * @param content
     */
    public void logout(RequestContent content) throws ReceiverException {
        LOGGER.log(Level.INFO, "Execution logout() method: " + this.getClass().getName());

        this.setPageSubTitle("Авторизация");
        super.setDefaultContentAttributes(content);

        Object userObj = content.findSessionAttribute("authorized");

        if (userObj != null) {
            int userId = Integer.parseInt(String.valueOf(userObj));
            User user;

            try (UserDAO userDAO = new UserDAO()) {
                user = userDAO.find(userId);
            } catch (DatabaseException e) {
                throw new ReceiverException("Cannot retrieve data about authorized user.", e);
            }

            if(user == null) {
              throw new ReceiverException("User not found.");
            }

            content.removeSessionAttribute("authorized");
        }
    }
}
