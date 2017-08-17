package com.epam.horsebetting.validator;

import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.tag.OldInputFormAttributeTag;
import com.epam.horsebetting.util.MessageWrapper;

import java.util.HashMap;
import java.util.Locale;
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
     * Message manager to find the right message.
     */
    MessageConfig messageManager;

    /**
     * Prefix to get validation message from resource manager.
     */
    static final String VALIDATION_PREFIX = "validation.";
    static final String VALIDATION_REQUIRED = ".required";
    static final String VALIDATION_INCORRECT = ".incorrect";

    /**
     * Regular expressions for variables.
     */
    static final String DEFAULT_NAME_REGEX = "[a-zA-Zа-яА-ЯёЁ ]{4,}";
    static final String DEFAULT_STRING_REGEX = "[а-яА-ЯёЁ\\w\\d\\s,]+";
    static final String DEFAULT_INTEGER_REGEX = "\\d+";
    static final String DEFAULT_BIG_DECIMAL_REGEX = "\\d+(,|.\\d+)?";
    private static final String DEFAULT_DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";

    /**
     * Default constructor.
     */
    AbstractValidator(Locale locale) {
        this.errors = new MessageWrapper();
        this.oldInput = new HashMap<>();
        this.messageManager = new MessageConfig(locale);
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
        return validateString(number, attributeName, key, saveInput, DEFAULT_INTEGER_REGEX);
    }

    /**
     * Validate default name.
     *
     * @param name
     * @param attributeName
     * @param key
     * @return boolean
     */
    boolean validateDefaultName(String name, String attributeName, String key, boolean saveInput) {
        return validateString(name, attributeName, key, saveInput, DEFAULT_STRING_REGEX);
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
        return validateString(date, attributeName, key, saveInput, DEFAULT_DATE_FORMAT_REGEX);
    }

    /**
     * Validate string by format.
     *
     * @param value
     * @param attributeName
     * @param key
     * @param saveInput
     * @param format
     * @return boolean
     */
    boolean validateString(String value, String attributeName, String key, boolean saveInput, String format) {
        if (value != null && !value.trim().isEmpty()) {
            if (saveInput) {
                this.putOldData(OldInputFormAttributeTag.PREFIX + attributeName, value);
            }

            if (!value.matches(format)) {
                this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_INCORRECT));
                return false;
            }

            return true;
        }

        this.addErrorMessage(messageManager.get(VALIDATION_PREFIX + key + VALIDATION_REQUIRED));
        return false;
    }
}
