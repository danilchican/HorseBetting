package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.UserReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class UserReceiverImpl extends AbstractReceiver implements UserReceiver {

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
        this.setPageSubTitle("Регистрация");
        super.setDefaultContentAttributes(content);

        String name = content.findParameter("name");
        String email = content.findParameter("email");
        String password = content.findParameter("password");
        String passwordConfirmation = content.findParameter("password_confirmation");

        UserValidator validator = new UserValidator();

        if (validator.validateRegistrationForm(name, email, password, passwordConfirmation)) {
            User newUser = new User(email, password);
            newUser.setName(name);

            ArrayList<String> errors = new ArrayList<>();

            try (UserDAOImpl userDAO = new UserDAOImpl()) {
                if(!isUserExists(newUser)) {
                    User user = userDAO.create(newUser);
                    this.authenticate(content, user);
                } else {
                    errors.add("User already exists. Change your email.");
                    content.insertSessionAttribute("errors", errors);

                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

                    throw new ReceiverException("Cannot register user. User already exists.");
                }
            } catch (DAOException e) {
                errors.add("Can't create new user.");
                content.insertSessionAttribute("errors", errors);

                for (Map.Entry<String, String> entry : validator.getOldInput()) {
                    content.insertSessionAttribute(entry.getKey(), entry.getValue());
                }

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid registration data.");
        }
    }

    /**
     * Check if the user exists.
     *
     * @param user
     * @return boolean
     * @throws DAOException
     */
    private boolean isUserExists(User user) throws DAOException {
        User foundedUser;

        try (UserDAOImpl userDAO = new UserDAOImpl()) {
            foundedUser = userDAO.findByEmail(user.getEmail());
        }

        return foundedUser != null;
    }

    /**
     * Authenticate user in system.
     *
     * @param content
     * @param user
     * @throws ReceiverException
     */
    private void authenticate(RequestContent content, User user) throws ReceiverException {
        LOGGER.log(Level.DEBUG, "Auth user: " + user);
        content.insertSessionAttribute("authorized", user.getId());
    }

    /**
     * Login a user.
     *
     * @param content
     */
    public void login(RequestContent content) throws ReceiverException {
        this.setPageSubTitle("Авторизация");
        super.setDefaultContentAttributes(content);

        String email = content.findParameter("email");
        String password = content.findParameter("password");

        UserValidator validator = new UserValidator();

        if (validator.validateLoginForm(email, password)) {
            User user;

            try (UserDAOImpl userDAO = new UserDAOImpl()) {
                user = userDAO.attempt(email, password);
            } catch (DAOException e) {
                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }

            if(user == null) {
                ArrayList<String> errors = new ArrayList<String>() {{
                   add("User does not exists or credentials are not correct.");
                }};

                content.insertSessionAttribute("errors", errors);
                throw new ReceiverException("Cannot authenticate user. User is null.");
            }

            this.authenticate(content, user);
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid user credentials.");
        }
    }

    /**
     * Logout a user.
     *
     * @param content
     */
    public void logout(RequestContent content) throws ReceiverException {
        Object authorized = content.findSessionAttribute("authorized");

        if (authorized != null) {
            content.removeSessionAttribute("authorized");
        }
    }

    /**
     * Change locale.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void changeLocale(RequestContent content) {
        super.setDefaultContentAttributes(content);

        String language = content.findParameter("lang");
        LOGGER.log(Level.DEBUG, "Received language: " + language);

        Locale locale = (language != null && language.equals("en"))
                ? new Locale("en", "US")
                : new Locale("ru", "RU");

        content.insertSessionAttribute("locale", locale);
        LOGGER.log(Level.INFO, "The new locale was set: " + locale);
    }
}
