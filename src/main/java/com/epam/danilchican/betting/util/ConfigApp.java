package com.epam.danilchican.betting.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ConfigApp {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Resource bundle for retrieving database settings.
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

    /**
     * Default constructor with getting bundle.
     */
    public ConfigApp() {
        dbBundle = ResourceBundle.getBundle(ConfigApp.BUNDLE_ENVIRONMENT_DIR + "/" + ConfigApp.BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Find website url from properties file.
     *
     * @return website url
     */
    public String findURL() {
        String propName = "";

        try {
            propName = "APP_URL";
            return dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        return DEFAULT_APP_URL;
    }
}
