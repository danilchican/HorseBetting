package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.UserReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

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
        String name = content.findParameter("name");
        String email = content.findParameter("email");
        String password = content.findParameter("password");
        String passwordConfirmation = content.findParameter("password_confirmation");

        UserValidator validator = new UserValidator();

        if (validator.validateRegistrationForm(name, email, password, passwordConfirmation)) {
            ArrayList<String> errors = new ArrayList<>();
            UserDAOImpl userDAO = new UserDAOImpl(true);

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            User newUser = new User(email, password);
            newUser.setName(name);

            TransactionManager transaction = new TransactionManager(userDAO);
            transaction.beginTransaction();

            try {
                if(!isUserExists(userDAO, newUser)) {
                    User user = userDAO.create(newUser);

                    transaction.commit();
                    this.authenticate(content, user);
                } else {
                    transaction.rollback();

                    errors.add("User already exists. Change your email.");
                    content.insertSessionAttribute("errors", errors);

                    throw new ReceiverException("Cannot register user. User already exists.");
                }
            } catch (DAOException e) {
                transaction.rollback();

                errors.add("Can't create new user.");
                content.insertSessionAttribute("errors", errors);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
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
     * Login a user.
     *
     * @param content
     */
    public void login(RequestContent content) throws ReceiverException {
        String email = content.findParameter("email");
        String password = content.findParameter("password");

        ArrayList<String> errors = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateLoginForm(email, password)) {
            User user;

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                user = userDAO.attempt(email, password);
            } catch (DAOException e) {
                errors.add("Can't login with your credentials.");
                content.insertSessionAttribute("errors", errors);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }

            if(user == null) {
                errors.add("User does not exists or credentials are not correct.");
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
     * Update profile settings.
     *
     * @param content
     */
    @Override
    public void updateProfileSettings(RequestContent content) throws ReceiverException {
        String name = content.findParameter("user-name");

        ArrayList<String> messages = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateUpdateSettingsForm(name)) {
            User authorizedUser = (User)content.findRequestAttribute("user");
            authorizedUser.setName(name);

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSettings(authorizedUser);

                messages.add("Profile settings updated successfully.");
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add("Can't update user settings.");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid user credentials.");
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

    /**
     * Obtain users list ajax.
     *
     * @param content
     * @throws ReceiverException
     */
    @Override
    public void ajaxObtainUsersList(RequestContent content) throws ReceiverException {
        int page = 1;
        String pageNumber = content.findParameter("page");

        //TODO create validators
        if(pageNumber != null) {
            try {
                page = Integer.parseInt(pageNumber);
            } catch (NumberFormatException e) {
                throw new ReceiverException("Page incorrect: " + e.getMessage(), e);
            }
        }

        final int step = 10;
        final int offset = step * (page - 1);

        try(UserDAOImpl userDAO = new UserDAOImpl(false)) {
            List<User> users = userDAO.obtainPart(step, offset);
            LOGGER.log(Level.DEBUG, "Users part: " + Arrays.toString(users.toArray()));
            content.insertJsonAttribute("users", users);
        } catch (DAOException e) {
            throw new ReceiverException("Database Error: " + e.getMessage(), e);
        }
    }

    /**
     * Check if the user exists.
     *
     * @param user
     * @return boolean
     * @throws DAOException
     */
    private boolean isUserExists(UserDAOImpl userDAO, User user) throws DAOException {
        User foundedUser = userDAO.findByEmail(user.getEmail());

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
}
