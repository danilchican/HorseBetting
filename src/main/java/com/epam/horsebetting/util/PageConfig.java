package com.epam.horsebetting.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PageConfig {

    public enum PageConfigType {
        WELCOME, AUTH_LOGIN, AUTH_REGISTER, NOT_FOUND
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
        this.jspBundle = ResourceBundle.getBundle(Config.BUNDLE_ENVIRONMENT_DIR + "/" + BUNDLE_NAVIGATION_NAME);
    }

    /**
     * Get instance of PageConfig.
     *
     * @return instance
     */
    public static PageConfig getInstance() {
        if(instance == null) {
            instance = new PageConfig();
            LOGGER.log(Level.INFO, "PageConfig instance created!");
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
