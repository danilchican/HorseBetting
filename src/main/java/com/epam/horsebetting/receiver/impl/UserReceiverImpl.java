package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.UserReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.MailSender;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import java.math.BigDecimal;
import java.util.*;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;

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
        String name = content.findParameter(RequestFieldConfig.User.NAME_FIELD);
        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String passwordConfirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        UserValidator validator = new UserValidator();

        if (validator.validateRegistrationForm(name, email, password, passwordConfirmation)) {
            MessageWrapper messages = new MessageWrapper();

            Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
            MessageConfig messageResource = new MessageConfig(locale);

            UserDAOImpl userDAO = new UserDAOImpl(true);
            User newUser = new User(email, password);
            newUser.setName(name);

            TransactionManager transaction = new TransactionManager(userDAO);
            transaction.beginTransaction();

            try {
                if (!isUserExists(userDAO, newUser)) {
                    User user = userDAO.create(newUser);

                    transaction.commit();
                    this.authenticate(content, user);
                } else {
                    transaction.rollback();

                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

                    messages.add(messageResource.get("user.exists"));
                    content.insertSessionAttribute("errors", messages);

                    throw new ReceiverException("Cannot register user. User already exists.");
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("user.default.error"));
                content.insertSessionAttribute("errors", messages);

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
        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);

        UserValidator validator = new UserValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateLoginForm(email, password)) {
            User user;

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                user = userDAO.attempt(email, password);
            } catch (DAOException e) {
                messages.add(messageResource.get("user.credentials.fail"));
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }

            if (user == null) {
                messages.add(messageResource.get("user.credentials.fail"));
                content.insertSessionAttribute("errors", messages);

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
        String name = content.findParameter(RequestFieldConfig.User.NAME_FIELD);

        UserValidator validator = new UserValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateUpdateSettingsForm(name)) {
            User authorizedUser = (User) content.findRequestAttribute("user");
            authorizedUser.setName(name);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSettings(authorizedUser);

                messages.add(messageResource.get("profile.settings.update.success"));
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.settings.update.fail"));
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
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String confirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        UserValidator validator = new UserValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateSecurityForm(password, confirmation)) {
            User authorizedUser = (User) content.findRequestAttribute("user");
            authorizedUser.setPassword(password);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSecurity(authorizedUser);

                messages.add(messageResource.get("profile.password.change.success"));
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.password.change.fail"));
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
        String paymentAmount = content.findParameter(RequestFieldConfig.User.PAYMENT_AMOUNT_FIELD);

        UserValidator validator = new UserValidator();
        MessageWrapper messages = new MessageWrapper();

        Locale locale = (Locale)content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateUpdateProfileBalanceForm(paymentAmount)) {
            User authorizedUser = (User) content.findRequestAttribute("user");

            BigDecimal amount = new BigDecimal(paymentAmount);
            BigDecimal newBalance = authorizedUser.getBalance().add(amount);

            authorizedUser.setBalance(newBalance);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateBalance(authorizedUser);

                messages.add(messageResource.get("profile.payment.replenish.success"));
                content.insertSessionAttribute("messages", messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.payment.replenish.fail"));
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());
            throw new ReceiverException("Invalid user data.");
        }
    }

    /**
     * TODO Reset password by sending reset link to email.
     *
     * @param content
     */
    @Override
    public void resetPassword(RequestContent content) throws ReceiverException {
        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);

        ArrayList<String> messages = new ArrayList<>();
        UserValidator validator = new UserValidator();

        if (validator.validateResetPasswordForm(email)) {
            User user = new User();
            user.setEmail(email);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                if (isUserExists(userDAO, user)) {
                    String message = "";
                    // TODO create generation message

                    try {
                        MailSender mail = new MailSender();
                        mail.send(email, "Reset password", message);

                        messages.add("Email is successfully sent.");
                        content.insertSessionAttribute("messages", messages);
                    } catch (MessagingException e) {
                        messages.add("Cannot send password link. Please try later.");
                        content.insertSessionAttribute("errors", messages);

                        throw new ReceiverException("Send message to email error: " + e.getMessage(), e);
                    }
                } else {
                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

                    messages.add("User does not exist with this email.");
                    content.insertSessionAttribute("errors", messages);

                    throw new ReceiverException("Cannot send reset password link. User does not exist.");
                }
            } catch (DAOException e) {
                messages.add("Can't find user with this email.");
                content.insertSessionAttribute("errors", messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute("errors", validator.getErrors());

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid email to reset password.");
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

        String language = content.findParameter(RequestFieldConfig.Common.LANG_FIELD);
        LOGGER.log(Level.DEBUG, "Received language: " + language);

        Locale locale = (language != null && language.equals("en"))
                ? new Locale("en", "US")
                : new Locale("ru", "RU");

        content.insertSessionAttribute(SESSION_LOCALE, locale);
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
