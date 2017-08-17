package com.epam.horsebetting.validator;

import com.epam.horsebetting.tag.OldInputFormAttributeTag;
import com.epam.horsebetting.util.MessageWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractValidator {

    /**
     * List of errors.
     */
    private MessageWrapper errors;

    /**
     * Old input from data.
     */
    private HashMap<String, String> oldInput;

    /**
     * Regular expressions for variables.
     */
    static final String DEFAULT_STRING_REGEX = "[а-яА-ЯёЁ\\w\\d\\s,]+";
    static final String DEFAULT_INTEGER_REGEX = "\\d+";
    static final String DEFAULT_BIG_DECIMAL_REGEX = "\\d+(,|.\\d+)?";
    private static final String DEFAULT_DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";

    /**
     * Default constructor.
     */
    AbstractValidator() {
        this.errors = new MessageWrapper();
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
    public MessageWrapper getErrors() {
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
        if (number != null && !number.trim().isEmpty()) {
            if (saveInput) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, number);
            }

            if (!number.matches(DEFAULT_INTEGER_REGEX)) {
                this.addErrorMessage(key + " is incorrect.");
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is empty.");
        return false;
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
            if (byteNumber != null && !byteNumber.trim().isEmpty()) {
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
        return validateString(name, attributeName, key, errorMessage, saveInput, DEFAULT_STRING_REGEX);
    }

    /**
     * Validate fate format.
     *
     * @param date
     * @param attributeName
     * @param key
     * @param saveInput
     * @return boolean
     */
    boolean validateDate(String date, String attributeName, String key, boolean saveInput) {
        return validateString(date, attributeName, key, "should have date format.", saveInput, DEFAULT_DATE_FORMAT_REGEX);
    }

    /**
     * Validate string by format.
     *
     * @param value
     * @param attributeName
     * @param key
     * @param errorMessage
     * @param saveInput
     * @param format
     * @return boolean
     */
    boolean validateString(String value, String attributeName, String key, String errorMessage,
                           boolean saveInput, String format) {
        if (value != null && !value.trim().isEmpty()) {
            if (saveInput) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, value);
            }

            if (!value.matches(format)) {
                this.addErrorMessage(key + (errorMessage != null ? " " + errorMessage : ""));
                return false;
            }

            return true;
        }

        this.addErrorMessage(key + " is required.");
        return false;
    }
}
