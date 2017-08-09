package com.epam.horsebetting.validator;

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
}
