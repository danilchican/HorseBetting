package com.epam.horsebetting.receiver.impl;

import com.epam.horsebetting.config.EnvironmentConfig;
import com.epam.horsebetting.config.RequestFieldConfig;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.dao.impl.PasswordRecoverDAOImpl;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.database.TransactionManager;
import com.epam.horsebetting.entity.PasswordRecover;
import com.epam.horsebetting.entity.Role;
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
import javax.servlet.http.Cookie;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.epam.horsebetting.config.EnvironmentConfig.DEFAULT_DELIMITER;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.*;
import static com.epam.horsebetting.util.DateFormatter.MILLISECONDS_PER_HOUR;
import static com.epam.horsebetting.util.DateFormatter.SECONDS_PER_HOUR;

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        String name = content.findParameter(RequestFieldConfig.User.NAME_FIELD);
        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String passwordConfirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        UserValidator validator = new UserValidator(locale);

        if (validator.validateRegistrationForm(name, email, password, passwordConfirmation)) {
            MessageWrapper messages = new MessageWrapper();
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String remember = content.findParameter(RequestFieldConfig.User.REMEMBER_FIELD);

        UserValidator validator = new UserValidator(locale);

        if (validator.validateLoginForm(email, password, remember)) {

            UserDAOImpl userDAO = new UserDAOImpl(true);

            TransactionManager transaction = new TransactionManager(userDAO);
            transaction.beginTransaction();

            try {
                User user = userDAO.attempt(email, HashManager.make(password));

                if (user == null) {
                    transaction.rollback();
                    messages.add(messageResource.get("user.credentials.fail"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Cannot authenticate user. User is null.");
                }

                String rememberToken = user.getRememberToken();

                if (remember != null) {
                    if (rememberToken == null) {
                        String hashData = user.getId() + user.getEmail() + user.getPassword() + new Date().getTime();
                        rememberToken = HashManager.make(hashData);

                        user.setRememberToken(rememberToken);
                        userDAO.setRememberToken(user);
                    }

                    if (!this.setRememberTokenCookie(content, rememberToken)) {
                        transaction.rollback();
                        messages.add(messageResource.get("cookie.remember_token.fail"));
                        content.insertSessionAttribute(REQUEST_ERRORS, messages);

                        throw new ReceiverException("Cannot set remember token as Cookie.");
                    }
                }

                transaction.commit();

                this.authenticate(content, user);
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
        Cookie rememberCookie = new Cookie(COOKIE_REMEMBER_TOKEN, null);
        rememberCookie.setMaxAge(0);
        rememberCookie.setPath(DEFAULT_DELIMITER);

        if (authorized != null) {
            content.removeSessionAttribute(SESSION_AUTHORIZED);
            content.insertCookie(COOKIE_REMEMBER_TOKEN, rememberCookie);
        }
    }

    /**
     * Update profile settings.
     *
     * @param content
     */
    @Override
    public void updateProfileSettings(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String name = content.findParameter(RequestFieldConfig.User.NAME_FIELD);

        UserValidator validator = new UserValidator(locale);

        if (validator.validateUpdateSettingsForm(name)) {
            String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));
            int userId = Integer.parseInt(userIdAttr);

            User authorizedUser = new User(userId);
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String confirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        UserValidator validator = new UserValidator(locale);

        if (validator.validateSecurityForm(password, confirmation)) {
            String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));
            int userId = Integer.parseInt(userIdAttr);

            User authorizedUser = new User(userId);
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String paymentAmount = content.findParameter(RequestFieldConfig.User.PAYMENT_AMOUNT_FIELD);

        UserValidator validator = new UserValidator(locale);

        if (validator.validateUpdateProfileBalanceForm(paymentAmount)) {
            String userIdAttr = String.valueOf(content.findSessionAttribute(SESSION_AUTHORIZED));
            int userId = Integer.parseInt(userIdAttr);

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                User user = userDAO.find(userId);

                BigDecimal amount = new BigDecimal(paymentAmount);
                BigDecimal balance = user.getBalance().add(amount);

                user.setBalance(balance);
                userDAO.updateBalance(user);

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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);

        MessageWrapper messages = new MessageWrapper();
        MessageConfig messageResource = new MessageConfig(locale);

        String email = content.findParameter(RequestFieldConfig.User.EMAIL_FIELD);
        UserValidator validator = new UserValidator(locale);

        if (validator.validateResetPasswordForm(email)) {
            User user = new User();
            user.setEmail(email);

            UserDAOImpl userDAO = new UserDAOImpl(true);
            PasswordRecoverDAOImpl recoverDAO = new PasswordRecoverDAOImpl(true);

            TransactionManager transaction = new TransactionManager(userDAO, recoverDAO);
            transaction.beginTransaction();

            try {
                user = userDAO.findByEmail(email);

                if (user != null) {
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
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        String token = content.findParameter(RequestFieldConfig.Common.PASSWORD_RESET_TOKEN);
        String password = content.findParameter(RequestFieldConfig.User.PASSWORD_FIELD);
        String confirmation = content.findParameter(RequestFieldConfig.User.CONFIRMATION_FIELD);

        if (token == null) {
            throw new ReceiverException("Token does not exist.");
        }

        EnvironmentConfig env = new EnvironmentConfig();
        UserValidator validator = new UserValidator(locale);

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

            final int minutesPerHour = 60;

            Timestamp currDate = new Timestamp(new Date().getTime() + MILLISECONDS_PER_HOUR);

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
     * Update user role.
     *
     * @param content
     */
    @Override
    public void updateRole(RequestContent content) throws ReceiverException {
        Locale locale = (Locale) content.findSessionAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);
        MessageWrapper messages = new MessageWrapper();

        String userIdAttr = content.findParameter(RequestFieldConfig.User.ID_FIELD);
        String userRoleIdAttr = content.findParameter(RequestFieldConfig.User.ROLE_FIELD);

        UserValidator validator = new UserValidator(locale);
        UserDAOImpl userDAO = new UserDAOImpl(true);

        TransactionManager transaction = new TransactionManager(userDAO);
        transaction.beginTransaction();

        if (validator.validateUpdateRoleForm(userIdAttr, userRoleIdAttr)) {
            try {
                int userId = Integer.parseInt(userIdAttr);
                int roleId = Integer.parseInt(userRoleIdAttr);

                User user = userDAO.find(userId);
                Role role = userDAO.findRole(roleId);

                if(user == null) {
                    transaction.rollback();
                    messages.add(messageResource.get("user.not_found"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Cannot find user. User is null.");
                }

                if(role == null) {
                    transaction.rollback();
                    messages.add(messageResource.get("validation.user.role.incorrect"));
                    content.insertSessionAttribute(REQUEST_ERRORS, messages);

                    throw new ReceiverException("Cannot find role[id=" + roleId + "].");
                }

                userDAO.updateRole(userId, roleId);
                transaction.commit();

                messages.add(messageResource.get("dashboard.user.role.update.success"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);
            } catch (DAOException e) {
                transaction.rollback();

                messages.add(messageResource.get("profile.password.change.fail"));
                content.insertSessionAttribute(REQUEST_MESSAGES, messages);

                throw new ReceiverException("Database Error. " + e.getMessage(), e);
            } finally {
                transaction.endTransaction();
            }
        } else {
            content.insertSessionAttribute(REQUEST_ERRORS, validator.getErrors());
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

        String language = content.findParameter(RequestFieldConfig.Common.LANG_FIELD);
        LOGGER.log(Level.DEBUG, "Received language: " + language);

        Locale locale = (language != null && language.equals("en"))
                ? new Locale("en", "US")
                : new Locale("ru", "RU");

        content.insertSessionAttribute(SESSION_LOCALE, locale);
        LOGGER.log(Level.INFO, "The new locale was set: " + locale);
    }

    /**
     * Set remember token cookie.
     *
     * @param content
     * @param rememberToken
     * @return boolean
     */
    private boolean setRememberTokenCookie(RequestContent content, String rememberToken) throws ReceiverException {
        EnvironmentConfig env = new EnvironmentConfig();

        try {
            int age = Integer.parseInt(env.obtainRememberTokenExpTime());
            age *= SECONDS_PER_HOUR;

            Cookie rememberCookie = new Cookie(COOKIE_REMEMBER_TOKEN, rememberToken);
            rememberCookie.setMaxAge(age);
            rememberCookie.setPath(DEFAULT_DELIMITER);
            content.insertCookie(COOKIE_REMEMBER_TOKEN, rememberCookie);

            return true;
        } catch (NumberFormatException e) {
            LOGGER.log(Level.ERROR, "Cannot parse remember token expiration time: " + e.getMessage());
            return false;
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
