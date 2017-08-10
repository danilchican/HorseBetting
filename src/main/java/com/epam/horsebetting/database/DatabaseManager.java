package com.epam.horsebetting.database;

import com.epam.horsebetting.config.EnvironmentConfig;
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
    private static final String DEFAULT_DB_CHARACTER_ENCODING = "utf-8";

    private static final boolean DEFAULT_DB_REWRITE_BATCHED_STATEMENTS = true;
    private static final int DEFAULT_DB_POOL_SIZE = 10;

    /**
     * Resource bundle for retrieving database settings.
     */
    private ResourceBundle dbBundle;

    /**
     * Default constructor with getting bundle.
     */
    DatabaseManager() {
        dbBundle = ResourceBundle.getBundle(EnvironmentConfig.BUNDLE_ENVIRONMENT_DIR +
                "/" + EnvironmentConfig.BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Get database properties.
     *
     * @return props
     */
    Properties getProps() {
        Properties props = new Properties();

        String propertyValue = retrievePropValue("DB_USERNAME", DEFAULT_DB_USERNAME);
        props.put("user", propertyValue);

        propertyValue = retrievePropValue("DB_PASSWORD", DEFAULT_DB_PASSWORD);
        props.put("password", propertyValue);

        propertyValue = retrievePropValue("DB_USE_SSL", DEFAULT_DB_USE_SSL);
        props.put("useSSL", propertyValue);

        return props;
    }

    /**
     * Get database url.
     *
     * @return url
     */
    String getURL() {
        String driver = retrievePropValue("DB_DRIVER", DEFAULT_DB_DRIVER);
        String connection = retrievePropValue("DB_CONNECTION", DEFAULT_DB_CONNECTION);
        String host = retrievePropValue("DB_HOST", DEFAULT_DB_HOST);
        String port = retrievePropValue("DB_PORT", DEFAULT_DB_PORT);
        String database = retrievePropValue("DB_DATABASE", DEFAULT_DB_DATABASE);
        String characterEncoding = retrievePropValue("DB_CHARACTER_ENCODING", DEFAULT_DB_CHARACTER_ENCODING);

        String propName = "";

        boolean rewriteBatchedStatements;

        try {
            propName = "DB_REWRITE_BATCHED_STATEMENTS";
            rewriteBatchedStatements = Boolean.valueOf(dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            rewriteBatchedStatements = DEFAULT_DB_REWRITE_BATCHED_STATEMENTS;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        String url = driver + ":" + connection + "://" + host + ":" + port + "/" + database +
                "?characterEncoding=" + characterEncoding +
                "&rewriteBatchedStatements" + rewriteBatchedStatements;

        return url;
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
