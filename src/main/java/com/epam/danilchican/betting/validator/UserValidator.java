package com.epam.danilchican.betting.validator;

import java.util.ArrayList;

public class UserValidator {

    /**
     * List of errors.
     */
    private ArrayList<String> errors;

    /**
     * Regular expressions for variables.
     */
    private static final String NAME_REGEX = "[a-zA-Z]\\w{4,}";
    private static final String EMAIL_REGEX = "\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+";
    private static final String PASSWORD_REGEX = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}";

    /**
     * Default constructor.
     */
    public UserValidator() {
        this.errors = new ArrayList<>();
    }

    /**
     * Validate registration data.
     *
     * @param name
     * @param email
     * @param password
     * @param passwordConfirmation
     * @return
     */
    public boolean validateRegistration(String name, String email, String password, String passwordConfirmation) {
        return validateName(name)
                && validateEmail(email)
                && validatePassword(password)
                && validateConfirmation(password, passwordConfirmation);
    }

    /**
     * Validate name. Not required.
     *
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        if (name != null) {
            if (!name.matches(NAME_REGEX)) {
                this.errors.add("Must be at least 5 characters as well as contain `_` symbol. The 1st symbol must be [A-Za-z].");
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
    public boolean validateEmail(String email) {
        if (email == null) {
            this.errors.add("Email is required.");
            return false;
        }

        if (!email.matches(EMAIL_REGEX)) {
            this.errors.add("Email is invalid.");
            return false;
        }

        return true;
    }

    /**
     * Validate password. Required.
     *
     * @param password
     * @return boolean
     */
    public boolean validatePassword(String password) {
        if (password == null) {
            this.errors.add("Password is required.");
            return false;
        }

        if (!password.matches(PASSWORD_REGEX)) {
            this.errors.add("Your password must be at least 6 characters as well as contain at least one uppercase, one lowercase, and one number.");
            return false;
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
    public boolean validateConfirmation(String password, String passwordConfirmation) {

        if (password != null
                && passwordConfirmation != null
                && password.equals(passwordConfirmation)) {
            return true;
        }

        this.errors.add("Password not confirmed!");
        return false;
    }

    /**
     * Get list of errors.
     *
     * @return errors
     */
    public ArrayList<String> getErrors() {
        return errors;
    }
}
