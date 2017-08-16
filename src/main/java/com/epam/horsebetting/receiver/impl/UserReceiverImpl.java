package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.EnvironmentConfig;
import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.PasswordRecoverDAOImpl;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.PasswordRecover;
import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.receiver.UserReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.DateFormatter;
import com.epam.horsebetting.util.HashManager;
import com.epam.horsebetting.util.MailSender;
import com.epam.horsebetting.util.MessageWrapper;
import com.epam.horsebetting.validator.UserValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.*;

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

            Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
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
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Cannot register user. User already exists.");
                }
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("user.default.error"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());

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

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateLoginForm(email, password)) {
            User user;

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                user = userDAO.attempt(email, password);
            } catch (DAOException e) {
                messages.add(messageResource.get("user.credentials.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }

            if (user == null) {
                messages.add(messageResource.get("user.credentials.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Cannot authenticate user. User is null.");
            }

            this.authenticate(content, user);
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());

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
        Object authorized = content.findSessionAttribute(SESSION_AUTHORIZED);

        if (authorized != null) {
            content.removeSessionAttribute(SESSION_AUTHORIZED);
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

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateUpdateSettingsForm(name)) {
            User authorizedUser = (User) content.findRequestAttribute("user");
            authorizedUser.setName(name);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSettings(authorizedUser);

                messages.add(messageResource.get("profile.settings.update.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.settings.update.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
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

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateSecurityForm(password, confirmation)) {
            User authorizedUser = (User) content.findRequestAttribute("user");
            authorizedUser.setPassword(password);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateSecurity(authorizedUser);

                messages.add(messageResource.get("profile.password.change.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.password.change.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
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

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        if (validator.validateUpdateProfileBalanceForm(paymentAmount)) {
            User authorizedUser = (User) content.findRequestAttribute("user");

            BigDecimal amount = new BigDecimal(paymentAmount);
            BigDecimal newBalance = authorizedUser.getBalance().add(amount);

            authorizedUser.setBalance(newBalance);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                userDAO.updateBalance(authorizedUser);

                messages.add(messageResource.get("profile.payment.replenish.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } catch (DAOException e) {
                messages.add(messageResource.get("profile.payment.replenish.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
            throw new ReceiverException("Invalid user data.");
        }
    }

    /**
     * Send email to reset password by link.
     *
     * @param content
     */
    @Override
    public void sendEmailToResetPassword(RequestContent content) throws ReceiverException {
        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);

        MessageWrapper messages = new MessageWrapper();
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        UserValidator validator = new UserValidator();

        if (validator.validateResetPasswordForm(email)) {
            User user = new User();
            user.setEmail(email);

            UserDAOImpl userDAO = new UserDAOImpl(true);
            PasswordRecoverDAOImpl recoverDAO = new PasswordRecoverDAOImpl(true);

            TransactionManager transaction = new TransactionManager(userDAO, recoverDAO);
            transaction.beginTransaction();

            try {
                if ((user = userDAO.findByEmail(email)) != null) {
                    EnvironmentConfig envResource = new EnvironmentConfig();

                    String hashData = user.getId() + user.getEmail() + user.getPassword() + new Date().getTime();
                    String token = HashManager.make(hashData);

                    PasswordRecover recover = new PasswordRecover(user.getEmail(), token);
                    recoverDAO.create(recover);

                    String message = String.format(
                            "%s <a href=\"%s/password/link?token=%s\">%s/password/link?token=%s</a>",
                            messageResource.get("password.reset.message"),
                            envResource.obtainURL(), token,
                            envResource.obtainURL(), token
                    );

                    MailSender mail = new MailSender();
                    mail.send(email, messageResource.get("password.reset.subject"), message);

                    messages.add(messageResource.get("password.reset.notification.success"));
                    content.insertSessionAttribute(REQUEST_MESSAGES, messages);
                } else {
                    transaction.rollback();

                    for (Map.Entry<String, String> entry : validator.getOldInput()) {
                        content.insertSessionAttribute(entry.getKey(), entry.getValue());
                    }

                    messages.add(messageResource.get("user.credentials.fail"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Cannot send reset password link. User does not exist.");
                }
            } catch (MessagingException e) {
                transaction.rollback();

                messages.add(messageResource.get("password.reset.notification.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Send message to email error: " + e.getMessage(), e);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("user.credentials.fail"));
                content.insertSessionAttribute(REQUEST_ERRORS, messages);

                throw new ReceiverException("Database Error: " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());

            for (Map.Entry<String, String> entry : validator.getOldInput()) {
                content.insertSessionAttribute(entry.getKey(), entry.getValue());
            }

            throw new ReceiverException("Invalid email to reset password.");
        }
    }

    /**
     * Reset password.
     *
     * @param content
     */
    @Override
    public void resetPassword(RequestContent content) throws ReceiverException {
        String token = content.findParameter(RequestFieldConfig.Common.PASSWORD_RESET_TOKEN);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String confirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        if (token == null) {
            throw new ReceiverException("Token does not exist.");
        }

        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        EnvironmentConfig env = new EnvironmentConfig();
        UserValidator validator = new UserValidator();

        UserDAOImpl userDAO = new UserDAOImpl(true);
        PasswordRecoverDAOImpl recoverDAO = new PasswordRecoverDAOImpl(true);

        TransactionManager transaction = new TransactionManager(userDAO, recoverDAO);
        transaction.beginTransaction();

        try {
            PasswordRecover recover = recoverDAO.findByToken(token);

            if (recover == null) {
                transaction.rollback();
                throw new ReceiverException("Cannot find recover by token[" + token + "].");
            }

            final int oneHour = 36_000_00;
            final int minutesPerHour = 60;

            Timestamp currDate = new Timestamp(new Date().getTime() + oneHour);

            final int maxExpireMinutes = Integer.parseInt(env.obtainTokenExpirationTime()) * minutesPerHour;
            final int currentDiffMinutes = (int) DateFormatter.calcDateDiff(recover.getCreatedAt(), currDate, TimeUnit.MINUTES);

            if (currentDiffMinutes > maxExpireMinutes) {
                transaction.rollback();
                throw new ReceiverException("Token expired.");
            }

            if (validator.validateSecurityForm(password, confirmation)) {
                User user = userDAO.findByEmail(recover.getEmail());
                user.setPassword(password);

                userDAO.updateSecurity(user);
                recoverDAO.remove(recover);

                transaction.commit();
                this.authenticate(content, user);

                messages.add(messageResource.get("profile.password.change.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } else {
                transaction.rollback();

                content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
                throw new ReceiverException("Invalid user data.");
            }
        } catch (DAOException e) {
            transaction.rollback();

            messages.add(messageResource.get("profile.password.change.fail"));
            content.insertSessionAttribute(REQUEST_MESSAGES, messages);

            throw new ReceiverException("Database Error. " + e.getMessage(), e);
        } finally {
            transaction.endTransaction();
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
        return userDAO.findByEmail(user.getEmail()) != null;
    }

    /**
     * Authenticate user in system.
     *
     * @param content
     * @param user
     * @throws ReceiverException
     */
    private void authenticate(RequestContent content, User user) throws ReceiverException {
        LOGGER.log(Level.DEBUG, "Authorized user: " + user);
        content.insertSessionAttribute(SESSION_AUTHORIZED, user.getId());
    }
}
