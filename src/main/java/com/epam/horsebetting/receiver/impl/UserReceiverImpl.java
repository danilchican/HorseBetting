package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.FormFieldConfig;
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

import java.math.BigDecimal;
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
        String name = content.findParameter(FormFieldConfig.User.NAME_FIELD);
        String email = content.findParameter(FormFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(FormFieldConfig.User.PASSWORD_FIELD);
        String passwordConfirmation = content.findParameter(FormFieldConfig.User.CONFIRMATION_FIELD);

        UserValidator validator = new UserValidator();

        if (validator.validateRegistrationForm(name, email, password, passwordConfirmation)) {
            ArrayList<String> errors = new ArrayList<>();
            UserDAOImpl userDAO = new UserDAOImpl(true);

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

                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

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
        String email = content.findParameter(FormFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(FormFieldConfig.User.PASSWORD_FIELD);

        ArrayList<String> errors = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateLoginForm(email, password)) {
            User user;

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
        String name = content.findParameter(FormFieldConfig.User.NAME_FIELD);

        ArrayList<String> messages = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateUpdateSettingsForm(name)) {
            User authorizedUser = (User)content.findRequestAttribute("user");
            authorizedUser.setName(name);

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
            throw new ReceiverException("Invalid user data.");
        }
    }

    /**
     * Update user's security.
     *
     * @param content
     */
    @Override
    public void updateSecurity(RequestContent content) throws ReceiverException {
        String password = content.findParameter(FormFieldConfig.User.PASSWORD_FIELD);
        String confirmation = content.findParameter(FormFieldConfig.User.CONFIRMATION_FIELD);

        ArrayList<String> messages = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if(validator.validateSecurityForm(password, confirmation)) {
            User authorizedUser = (User)content.findRequestAttribute("user");
            authorizedUser.setPassword(password);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSecurity(authorizedUser);

                messages.add("Password changed successfully.");
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add("Can't change the password.");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());
            throw new ReceiverException("Invalid user data.");
        }
    }

    /**
     * Update profile balance.
     *
     * @param content
     */
    @Override
    public void updateProfileBalance(RequestContent content) throws ReceiverException {
        String paymentAmount = content.findParameter(FormFieldConfig.User.PAYMENT_AMOUNT_FIELD);

        ArrayList<String> messages = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateUpdateProfileBalanceForm(paymentAmount)) {
            User authorizedUser = (User)content.findRequestAttribute("user");

            BigDecimal amount = new BigDecimal(paymentAmount);
            BigDecimal newBalance = authorizedUser.getBalance().add(amount);

            authorizedUser.setBalance(newBalance);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateBalance(authorizedUser);

                messages.add("Profile balance updated successfully.");
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add("Can't update user balance.");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());
            throw new ReceiverException("Invalid user data.");
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

        String language = content.findParameter(FormFieldConfig.Locale.LANG_FIELD);
        LOGGER.log(Level.DEBUG, "Received language: " + language);

        Locale locale = (language != null && language.equals("en"))
                ? new Locale("en", "US")
                : new Locale("ru", "RU");

        content.insertSessionAttribute("locale", locale);
        LOGGER.log(Level.INFO, "The new locale was set: " + locale);
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
