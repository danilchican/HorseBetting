package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;

public class UserValidator extends AbstractValidator {

    /**
     * Regular expressions for variables.
     */
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";

    /**
     * Messages to valid.
     */
    private static final String NAME_MESSAGE = "must be at least 5 characters " +
            "as well as contain `_` symbol. " +
            "The 1st symbol must be [A-Za-z].";
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

        if (!validateName(name, "name", "Name")) {
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
        return validateName(name, "user-name", "Name");
    }

    /**
     * Validate name. Not required.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    private boolean validateName(String name, String attributeName, String key) {
        return validateDefaultName(name, attributeName, key, NAME_MESSAGE);
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
