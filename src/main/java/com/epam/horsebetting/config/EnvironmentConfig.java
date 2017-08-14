package com.epam.horsebetting.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class EnvironmentConfig {

    private enum EnvironmentProperties {
        APP_URL, APP_NAME, APP_LOCALE,
        MAIL_HOST, MAIL_PORT, MAIL_SENDER, MAIL_PASSWORD,
        MAIL_AUTH, MAIL_STARTTLS_ENABLE
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
     * Default mail variables.
     */
    private static final String DEFAULT_MAIL_HOST = "smtp.gmail.com";
    private static final String DEFAULT_MAIL_PORT = "587";
    private static final String DEFAULT_MAIL_SENDER = "root@gmail.com";
    private static final String DEFAULT_MAIL_PASSWORD = "root";
    private static final String DEFAULT_MAIL_AUTH = "true";
    private static final String DEFAULT_MAIL_STARTTLS_ENABLE = "true";

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
    public String obtainURL() {
        return retrievePropValue(EnvironmentProperties.APP_URL, DEFAULT_APP_URL);
    }

    /**
     * Find application name from properties file.
     *
     * @return application name
     */
    public String obtainAppName() {
        return retrievePropValue(EnvironmentProperties.APP_NAME, DEFAULT_APP_NAME);
    }

    /**
     * Find locale from properties file.
     *
     * @return locale
     */
    public String obtainLocale() {
        return retrievePropValue(EnvironmentProperties.APP_LOCALE, DEFAULT_APP_LOCALE);
    }

    /**
     * Find mail host from properties file.
     *
     * @return host
     */
    public String obtainMailHost() {
        return retrievePropValue(EnvironmentProperties.MAIL_HOST, DEFAULT_MAIL_HOST);
    }

    /**
     * Find mail port from properties file.
     *
     * @return host
     */
    public String obtainMailPort() {
        return retrievePropValue(EnvironmentProperties.MAIL_PORT, DEFAULT_MAIL_PORT);
    }

    /**
     * Find mail sender from properties file.
     *
     * @return host
     */
    public String obtainMailSender() {
        return retrievePropValue(EnvironmentProperties.MAIL_SENDER, DEFAULT_MAIL_SENDER);
    }

    /**
     * Find mail password from properties file.
     *
     * @return host
     */
    public String obtainMailPassword() {
        return retrievePropValue(EnvironmentProperties.MAIL_PASSWORD, DEFAULT_MAIL_PASSWORD);
    }

    /**
     * Find mail auth value from properties file.
     *
     * @return host
     */
    public String obtainMailAuth() {
        return retrievePropValue(EnvironmentProperties.MAIL_AUTH, DEFAULT_MAIL_AUTH);
    }

    /**
     * Find mail starttls enable value from properties file.
     *
     * @return host
     */
    public String obtainMailEnableStartTLS() {
        return retrievePropValue(EnvironmentProperties.MAIL_AUTH, DEFAULT_MAIL_STARTTLS_ENABLE);
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
