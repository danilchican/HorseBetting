package com.epam.horsebetting.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Config {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Resource bundle for retrieving environment settings.
     */
    private ResourceBundle dbBundle;

    /**
     * Properties file dir.
     */
    public static final String BUNDLE_ENVIRONMENT_DIR = "config";

    /**
     * Properties file name.
     */
    public static final String BUNDLE_ENVIRONMENT_NAME = "env";

    /**
     * Default app variables.
     */
    private static final String DEFAULT_APP_URL = "http://localhost:8080";
    private static final String DEFAULT_APP_NAME = "No Name App";
    private static final String DEFAULT_APP_LOCALE = "ru";

    /**
     * Default constructor with getting bundle.
     */
    public Config() {
        dbBundle = ResourceBundle.getBundle(BUNDLE_ENVIRONMENT_DIR + "/" + BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Find website url from properties file.
     *
     * @return website url
     */
    public String findURL() {
        return retrievePropValue("APP_URL", DEFAULT_APP_URL);
    }

    /**
     * Find application name from properties file.
     *
     * @return application name
     */
    public String findAppName() {
        return retrievePropValue("APP_NAME", DEFAULT_APP_NAME);
    }

    /**
     * Find locale from properties file.
     *
     * @return locale
     */
    public String findLocale() {
        return retrievePropValue("APP_LOCALE", DEFAULT_APP_LOCALE);
    }

    /**
     * Retrieve property value.
     *
     * @param propName
     * @param defaultValue
     * @return property value
     */
    private String retrievePropValue(String propName, String defaultValue) {
        try {
            return dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        return defaultValue;
    }
}
