package com.epam.horsebetting.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageConfig {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Resource bundle for retrieving messages.
     */
    private ResourceBundle bundle;

    /**
     * Properties file name.
     */
    private static final String BUNDLE_MESSAGES_DIR = "localization";
    private static final String BUNDLE_MESSAGES_NAME = "messages";

    /**
     * Default constructor.
     */
    public MessageConfig(Locale locale) {
        this.bundle = ResourceBundle.getBundle(BUNDLE_MESSAGES_DIR + "/" + BUNDLE_MESSAGES_NAME, locale);
    }

    /**
     * Get message by key.
     *
     * @param key
     * @return message
     */
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find message[name=" + key + "]", e);
        }

        return null;
    }
}
