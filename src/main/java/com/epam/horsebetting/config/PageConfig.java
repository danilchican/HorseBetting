package com.epam.horsebetting.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static com.epam.horsebetting.config.EnvironmentConfig.DEFAULT_DELIMITER;

public class PageConfig {

    public enum PageConfigType {
        WELCOME, AUTH_LOGIN, AUTH_REGISTER, NOT_FOUND,
        RACES_INDEX, RACES_VIEW, RESET_PASSWORD, RESET_LINK_PASSWORD,
        PROFILE_INDEX, PROFILE_SETTINGS, PROFILE_PAYMENT,
        PROFILE_BETS, PROFILE_BETS_VIEW,
        DASHBOARD_INDEX, DASHBOARD_SUITS_INDEX, DASHBOARD_USERS_INDEX,
        DASHBOARD_HORSES_INDEX, DASHBOARD_HORSES_CREATE, DASHBOARD_HORSES_EDIT,
        DASHBOARD_RACES_INDEX, DASHBOARD_RACES_CREATE, DASHBOARD_RACES_EDIT
    }

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * PageConfig instance.
     */
    private static PageConfig instance;

    /**
     * Resource bundle for retrieving pages paths settings.
     */
    private ResourceBundle jspBundle;

    /**
     * Properties file name.
     */
    private static final String BUNDLE_NAVIGATION_NAME = "pages";
    private static final String DEFAULT_NOT_FOUND_PATH = "/jsp/errors/404.jsp";

    /**
     * Default private constructor.
     */
    private PageConfig() {
        this.jspBundle = ResourceBundle.getBundle(EnvironmentConfig.BUNDLE_ENVIRONMENT_DIR
                + DEFAULT_DELIMITER + BUNDLE_NAVIGATION_NAME);
    }

    /**
     * Get instance of PageConfig.
     *
     * @return instance
     */
    public static PageConfig getInstance() {
        if (instance == null) {
            instance = new PageConfig();
            LOGGER.log(Level.INFO, PageConfig.class + " instance created!");
        }

        return instance;
    }

    /**
     * Get page path by name.
     *
     * @param page
     * @return path
     */
    public String takePage(PageConfigType page) {
        try {
            return jspBundle.getString(page.toString());
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find page name: " + page, e);
        }

        return DEFAULT_NOT_FOUND_PATH;
    }
}
