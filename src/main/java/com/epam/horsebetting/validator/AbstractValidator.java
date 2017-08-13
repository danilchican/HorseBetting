package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractValidator {

    /**
     * List of errors.
     */
    private ArrayList<String> errors;

    /**
     * Old input from data.
     */
    private HashMap<String, String> oldInput;

    /**
     * Regular expressions for variables.
     */
    static final String DEFAULT_NAME_REGEX = "[a-zA-Zа-яА-ЯёЁ0-9 ]+";

    /**
     * Default constructor.
     */
    AbstractValidator() {
        this.errors = new ArrayList<>();
        this.oldInput = new HashMap<>();
    }

    /**
     * Put old pair key/value to old input data.
     *
     * @param key
     * @param value
     */
    void putOldData(String key, String value) {
        this.oldInput.put(key, value);
    }

    /**
     * Add error message.
     *
     * @param message
     */
    void addErrorMessage(String message) {
        this.errors.add(message);
    }

    /**
     * Get list of errors.
     *
     * @return errors
     */
    public ArrayList<String> getErrors() {
        return errors;
    }

    /**
     * Get old inputs..
     *
     * @return errors
     */
    public Set<Map.Entry<String, String>> getOldInput() {
        return oldInput.entrySet();
    }

    /**
     * Validate integer number.
     *
     * @param number
     * @param attributeName
     * @param key
     * @return boolean
     */
    boolean validateInteger(String number, String attributeName, String key, boolean saveInput) {
        try {
            if(number != null && !number.trim().isEmpty()) {
                if(saveInput) {
                    this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, number);
                }

                int n = Integer.parseInt(number);
                return true;
            }

            this.addErrorMessage(key + " is empty.");
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(key + " is incorrect.");
            return false;
        }
    }

    /**
     * Validate byte.
     *
     * @param byteNumber
     * @param attributeName
     * @param key
     * @return boolean
     */
    boolean validateByte(String byteNumber, String attributeName, String key) {
        try {
            if(byteNumber != null && !byteNumber.trim().isEmpty()) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, byteNumber);

                byte b = Byte.parseByte(byteNumber);
                return true;
            }

            this.addErrorMessage(key + " is empty.");
            return false;
        } catch (NumberFormatException e) {
            this.addErrorMessage(key + " is incorrect.");
            return false;
        }
    }

    /**
     * Validate default name.
     *
     * @param name
     * @param attributeName
     * @param key
     * @param errorMessage
     * @return boolean
     */
    boolean validateDefaultName(String name, String attributeName, String key, String errorMessage, boolean saveInput) {
        if (name != null && !name.trim().isEmpty()) {
            if(saveInput) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, name);
            }

            if (!name.matches(DEFAULT_NAME_REGEX)) {
                this.addErrorMessage(key + (errorMessage != null ? " " + errorMessage : ""));
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }
}
