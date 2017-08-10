package com.epam.horsebetting.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EnvironmentConfig {

    private enum EnvironmentProperties {
        APP_URL,
        APP_NAME,
        APP_LOCALE
    }

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
    public EnvironmentConfig() {
        dbBundle = ResourceBundle.getBundle(BUNDLE_ENVIRONMENT_DIR + "/" + BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Find website url from properties file.
     *
     * @return website url
     */
    public String findURL() {
        return retrievePropValue(EnvironmentProperties.APP_URL, DEFAULT_APP_URL);
    }

    /**
     * Find application name from properties file.
     *
     * @return application name
     */
    public String findAppName() {
        return retrievePropValue(EnvironmentProperties.APP_NAME, DEFAULT_APP_NAME);
    }

    /**
     * Find locale from properties file.
     *
     * @return locale
     */
    public String findLocale() {
        return retrievePropValue(EnvironmentProperties.APP_LOCALE, DEFAULT_APP_LOCALE);
    }

    /**
     * Retrieve property value.
     *
     * @param property
     * @param defaultValue
     * @return property value
     */
    private String retrievePropValue(EnvironmentProperties property, String defaultValue) {
        try {
            return dbBundle.getString(String.valueOf(property));
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find " + property + " prop", e);
        }

        return defaultValue;
    }
}
