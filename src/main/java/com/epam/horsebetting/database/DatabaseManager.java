package com.epam.horsebetting.database;

import com.epam.horsebetting.util.ConfigApp;
import com.mysql.jdbc.Driver;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

class DatabaseManager {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Default variables for database.
     */
    private static final String DEFAULT_DB_DRIVER = "jdbc";
    private static final String DEFAULT_DB_CONNECTION = "mysql";
    private static final String DEFAULT_DB_HOST = "127.0.0.1";
    private static final String DEFAULT_DB_PORT = "3306";
    private static final String DEFAULT_DB_DATABASE = "horsebetting";
    private static final String DEFAULT_DB_USERNAME = "root";
    private static final String DEFAULT_DB_PASSWORD = "root";
    private static final String DEFAULT_DB_USE_SSL = "false";
    private static final int DEFAULT_DB_POOL_SIZE = 10;

    /**
     * Resource bundle for retrieving database settings.
     */
    private ResourceBundle dbBundle;

    /**
     * Default constructor with getting bundle.
     */
    DatabaseManager() {
        dbBundle = ResourceBundle.getBundle(ConfigApp.BUNDLE_ENVIRONMENT_DIR + "/" + ConfigApp.BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Get database properties.
     *
     * @return props
     */
    Properties getProps() {
        Properties props = new Properties();
        String propName = "";

        try {
            propName = "DB_USERNAME";
            props.put("user", dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            props.put("user", DEFAULT_DB_USERNAME);
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_PASSWORD";
            props.put("password", dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            props.put("password", DEFAULT_DB_PASSWORD);
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_USE_SSL";
            props.put("useSSL", dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            props.put("useSSL", DEFAULT_DB_USE_SSL);
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        return props;
    }

    /**
     * Get database url.
     *
     * @return url
     */
    String getURL() {
        String url;
        String propName = "";

        String driver;
        String connection;
        String host;
        String port;
        String database;

        try {
            propName = "DB_DRIVER";
            driver = dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            driver = DEFAULT_DB_DRIVER;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_CONNECTION";
            connection = dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            connection = DEFAULT_DB_CONNECTION;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_HOST";
            host = dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            host = DEFAULT_DB_HOST;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_PORT";
            port = dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            port = DEFAULT_DB_PORT;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        try {
            propName = "DB_DATABASE";
            database = dbBundle.getString(propName);
        } catch (MissingResourceException e) {
            database = DEFAULT_DB_DATABASE;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        url = driver + ":" + connection + "://" + host + ":" + port + "/" + database;
        return url;
    }

    /**
     * Get pool size.
     *
     * @return pool size
     */
    int getPoolSize() {
        String propName = "DB_POOL_SIZE";
        int poolSize;

        try {
            poolSize = Integer.parseInt(dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            poolSize = DEFAULT_DB_POOL_SIZE;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        return poolSize;
    }

    /**
     * Register driver with the DriverManager.
     *
     * @see DriverManager
     * @see Driver
     */
    void registerDriver() {
        String errorMessage = "Can't register the Driver";

        try {
            DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            LOGGER.log(Level.FATAL, errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * Drops a driver from the DriverManager's list.
     *
     * @see DriverManager
     * @see java.sql.Driver
     */
    void deregisterDrivers() {
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();

        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();

            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Can't deregister drivers", e);
            }
        }
    }
}
