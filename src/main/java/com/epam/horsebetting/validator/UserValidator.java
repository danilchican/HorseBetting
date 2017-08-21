package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.math.BigDecimal;
import java.util.Locale;

public class UserValidator extends AbstractValidator {

    /**
     * Constant values.
     */
    private static final int MIN_BALANCE_AMOUNT = 5;

    /**
     * Regular expressions for variables.
     */
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";

    /**
     * Constants of user validation data.
     */
    private static final String PREFIX = "user.";
    private static final String USER_NAME = PREFIX + "name";
    private static final String USER_EMAIL = PREFIX + "email";
    private static final String USER_PASSWORD = PREFIX + "password";
    private static final String USER_PAYMENT = PREFIX + "payment";
    private static final String USER_CONFIRMATION = PREFIX + "confirmation";
    private static final String USER_REMEMBER = PREFIX + "remember";

    private static final String SWITCH_ON = "on";

    /**
     * Default constructor.
     *
     * @param locale
     */
    public UserValidator(Locale locale) {
        super(locale);
    }

    /**
     * Validate registration data.
     *
     * @param name
     * @param email
     * @param password
     * @param passwordConfirmation
     * @return boolean
     */
    public boolean validateRegistrationForm(String name, String email, String password, String passwordConfirmation) {
        boolean isValidate = true;

        if (!validateName(name, RequestFieldConfig.User.NAME_FIELD, USER_NAME, true)) {
            isValidate = false;
        }

        if (!validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, USER_EMAIL)) {
            isValidate = false;
        }

        if (!validatePassword(password, USER_PASSWORD)) {
            isValidate = false;
        }

        if (!validateConfirmation(password, passwordConfirmation)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate login form.
     *
     * @param email
     * @param password
     * @param remember
     * @return boolean
     */
    public boolean validateLoginForm(String email, String password, String remember) {
        boolean isValidate = true;

        if (!validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, USER_EMAIL)) {
            isValidate = false;
        }

        if (!validatePassword(password, USER_PASSWORD)) {
            isValidate = false;
        }

        if (!validateRememberField(remember, USER_REMEMBER)) {
            isValidate = false;
        }

        return isValidate;
    }

    private boolean validateRememberField(String remember, String key) {
        if (remember != null) {
            if(SWITCH_ON.equals(remember)) {
                return true;
            }

            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_INCORRECT));
            return false;
        }

        return true;
    }

    /**
     * Validate update settings form.
     *
     * @param name
     * @return boolean
     */
    public boolean validateUpdateSettingsForm(String name) {
        return validateName(name, RequestFieldConfig.User.NAME_FIELD, USER_NAME, false);
    }

    /**
     * Validate security form.
     *
     * @param password
     * @param confirmation
     * @return boolean
     */
    public boolean validateSecurityForm(String password, String confirmation) {
        boolean isValidate = true;

        if (!validatePassword(password, USER_PASSWORD)) {
            isValidate = false;
        }

        if (!validateConfirmation(password, confirmation)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate profile balance form.
     *
     * @param amount
     * @return boolean
     */
    public boolean validateUpdateProfileBalanceForm(String amount) {
        String message = messageManager.get(VALIDATION_PREFIX + USER_PAYMENT + VALIDATION_INCORRECT)
                + " " + MIN_BALANCE_AMOUNT + "$.";

        try {
            if (amount != null && !amount.trim().isEmpty()) {
                BigDecimal b = new BigDecimal(amount);

                if (b.compareTo(new BigDecimal(MIN_BALANCE_AMOUNT)) != -1) {
                    return true;
                }

                this.addErrorMessage(message);
                return false;
            }
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + USER_PAYMENT + VALIDATION_REQUIRED));
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(message);
            return false;
        }
    }

    /**
     * Validate reset password form.
     *
     * @param email
     * @return boolean
     */
    public boolean validateResetPasswordForm(String email) {
        return validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, USER_EMAIL);
    }

    /**
     * Validate name.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateName(String name, String attributeName, String key, boolean saveInput) {
        return validateString(name, attributeName, key, saveInput, DEFAULT_NAME_REGEX);
    }

    /**
     * Validate email.
     *
     * @param email
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateEmail(String email, String attributeName, String key) {
        return validateString(email, attributeName, key, true, EMAIL_REGEX);
    }

    /**
     * Validate password.
     *
     * @param password
     * @param key
     * @return boolean
     */
    private boolean validatePassword(String password, String key) {
        return validateString(password, RequestFieldConfig.User.PASSWORD_FIELD, key, false, PASSWORD_REGEX);
    }

    /**
     * Validate password and confirmation.
     *
     * @param password
     * @param passwordConfirmation
     * @return boolean
     */
    private boolean validateConfirmation(String password, String passwordConfirmation) {
        if (password.equals(passwordConfirmation)) {
            return true;
        }

        this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + USER_CONFIRMATION + VALIDATION_INCORRECT));
        return false;
    }
}
