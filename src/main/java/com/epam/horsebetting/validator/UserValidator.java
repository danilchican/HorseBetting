package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.RequestFieldConfig;

import java.math.BigDecimal;
import java.util.Locale;

public class UserValidator extends AbstractValidator {

    /**
     * Regular expressions for variables.
     */
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";
    private static final int MIN_BALANCE_AMOUNT = 5;

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

        if (!validateName(name, RequestFieldConfig.User.NAME_FIELD, "user.name", true)) {
            isValidate = false;
        }

        if (!validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, "user.email")) {
            isValidate = false;
        }

        if (!validatePassword(password, "user.password")) {
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
     * @return boolean
     */
    public boolean validateLoginForm(String email, String password) {
        boolean isValidate = true;

        if (!validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, "user.email")) {
            isValidate = false;
        }

        if (!validatePassword(password, "user.password")) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate update settings form.
     *
     * @param name
     * @return boolean
     */
    public boolean validateUpdateSettingsForm(String name) {
        return validateName(name, RequestFieldConfig.User.NAME_FIELD, "user.name", false);
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

        if (!validatePassword(password, "user.password")) {
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
        String message = messageManager.get(VALIDATION_PREFIX + "user.payment.incorrect")
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
            this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "user.payment.required"));
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
        return validateEmail(email, RequestFieldConfig.User.EMAIL_FIELD, "user.email");
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

        this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + "user.confirmation.incorrect"));
        return false;
    }
}
