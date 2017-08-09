package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserValidator extends AbstractValidator {

    /**
     * Regular expressions for variables.
     */
    private static final String NAME_REGEX = "[a-zA-Z]\\w{4,}";
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";

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

        if (!validateName(name)) {
            isValidate = false;
        }

        if (!validateEmail(email)) {
            isValidate = false;
        }

        if (!validatePassword(password)) {
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

        if (!validateEmail(email)) {
            isValidate = false;
        }

        if (!validatePassword(password)) {
            isValidate = false;
        }

        return isValidate;
    }

    /**
     * Validate name. Not required.
     *
     * @param name
     * @return boolean
     */
    private boolean validateName(String name) {
        if (name == null) {
            this.addErrorMessage("Name is required.");
            return false;
        }

        if (!name.isEmpty()) {
            this.putOldData(OldInputFormAttributeTag.PREFIX + "name", name);

            if (!name.matches(NAME_REGEX)) {
                this.addErrorMessage("Name must be at least 5 characters as well as contain `_` symbol. The 1st symbol must be [A-Za-z].");
                return false;
            }
        }

        return true;
    }

    /**
     * Validate email. Required.
     *
     * @param email
     * @return boolean
     */
    private boolean validateEmail(String email) {
        if (email == null) {
            this.addErrorMessage("Email is required.");
            return false;
        }

        if (!email.isEmpty()) {
            this.putOldData(OldInputFormAttributeTag.PREFIX + "email", email);

            if (!email.matches(EMAIL_REGEX)) {
                this.addErrorMessage("Email is invalid.");
                return false;
            }
        }

        return true;
    }

    /**
     * Validate password. Required.
     *
     * @param password
     * @return boolean
     */
    private boolean validatePassword(String password) {
        if (password == null) {
            this.addErrorMessage("Password is required.");
            return false;
        }

        if (!password.isEmpty()) {
            if (!password.matches(PASSWORD_REGEX)) {
                this.addErrorMessage("Your password must be at least 6 characters as well as contain at least one lowercase and one number.");
                return false;
            }
        }

        return true;
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
