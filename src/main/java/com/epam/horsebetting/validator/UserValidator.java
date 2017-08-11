package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;

import java.math.BigDecimal;

public class UserValidator extends AbstractValidator {

    /**
     * Regular expressions for variables.
     */
    private static final String NAME_REGEX = "[a-zA-Zа-яА-ЯёЁ ]{4,}";
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";
    private static final int MIN_BALANCE_AMOUNT = 5;

    /**
     * Messages to valid.
     */
    private static final String NAME_MESSAGE = "must be at least 5 characters " +
            "as well as contain symbols and spaces.";
    private static final String PASSWORD_MESSAGE = "Your password must be at least 6 characters " +
            "as well as contain at least one lowercase and one number.";

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

        if (!validateName(name, "name", "Name", true)) {
            isValidate = false;
        }

        if (!validateEmail(email, "email", "Email")) {
            isValidate = false;
        }

        if (!validatePassword(password, "Password")) {
            isValidate = false;
        }

        if (!validateConfirmation(password, passwordConfirmation)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate authentication data.
     *
     * @param email
     * @param password
     * @return boolean
     */
    public boolean validateLoginForm(String email, String password) {
        boolean isValidate = true;

        if (!validateEmail(email, "email", "Email")) {
            isValidate = false;
        }

        if (!validatePassword(password, "Password")) {
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
        return validateDefaultName(name, "user-name", "Name", NAME_MESSAGE, false);
    }

    /**
     * Validate update security form.
     *
     * @param password
     * @param confirmation
     * @return boolean
     */
    public boolean validateSecurityForm(String password, String confirmation) {
        boolean isValidate = true;

        if (!validatePassword(password, "Password")) {
            isValidate = false;
        }

        if (!validateConfirmation(password, confirmation)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate
     *
     * @param amount
     * @return boolean
     */
    public boolean validateUpdateProfileBalanceForm(String amount) {
        try {
            if(amount != null && !amount.trim().isEmpty()) {
                BigDecimal b = new BigDecimal(amount);

                if(b.compareTo(new BigDecimal(MIN_BALANCE_AMOUNT)) != -1) {
                    return true;
                }

                this.addErrorMessage("Payment amount should be equal or greater than " + MIN_BALANCE_AMOUNT + "$.");
                return false;
            }

            this.addErrorMessage("Payment amount is empty.");
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage("Payment amount is incorrect.");
            return false;
        }
    }

    /**
     * Validate name. Not required.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateName(String name, String attributeName, String key, boolean saveInput) {
        if (name != null && !name.trim().isEmpty()) {
            if(saveInput) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, name);
            }

            if (!name.matches(NAME_REGEX)) {
                this.addErrorMessage(key + " " + NAME_MESSAGE);
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }

    /**
     * Validate email. Required.
     *
     * @param email
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateEmail(String email, String attributeName, String key) {
        if (email != null && !email.trim().isEmpty()) {
            this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, email);

            if (!email.matches(EMAIL_REGEX)) {
                this.addErrorMessage(key + " is invalid.");
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }

    /**
     * Validate password. Required.
     *
     * @param password
     * @param key
     * @return boolean
     */
    private boolean validatePassword(String password, String key) {
        if (password != null && !password.trim().isEmpty()) {
            if (!password.matches(PASSWORD_REGEX)) {
                this.addErrorMessage(PASSWORD_MESSAGE);
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }

    /**
     * Validate password and it confirmation. Required.
     *
     * @param password
     * @param passwordConfirmation
     * @return boolean
     */
    private boolean validateConfirmation(String password, String passwordConfirmation) {
        if (password.equals(passwordConfirmation)) {
            return true;
        }

        this.addErrorMessage("Password not confirmed!");
        return false;
    }
}
