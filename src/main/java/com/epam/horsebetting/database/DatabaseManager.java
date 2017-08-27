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

import static com.epam.horsebetting.config.EnvironmentConfig.DEFAULT_DELIMITER;

class DatabaseManager {

    private enum DatabaseProperties {
        DB_DRIVER,
        DB_CONNECTION,
        DB_HOST,
        DB_PORT,
        DB_DATABASE,
        DB_USERNAME,
        DB_PASSWORD,
        DB_USE_SSL,
        DB_CHARACTER_ENCODING,
        DB_REWRITE_BATCHED_STATEMENTS,
        DB_POOL_SIZE
    }

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
     * Constants.
     */
    private static final String USER_PROP = "user";
    private static final String PASSWORD_PROP = "password";
    private static final String USE_SSL_PROP = "useSSL";

    /**
     * Resource bundle for retrieving database settings.
     */
    private ResourceBundle dbBundle;

    /**
     * Default constructor with getting bundle.
     */
    DatabaseManager() {
        dbBundle = ResourceBundle.getBundle(EnvironmentConfig.BUNDLE_ENVIRONMENT_DIR +
                DEFAULT_DELIMITER + EnvironmentConfig.BUNDLE_ENVIRONMENT_NAME);
    }

    /**
     * Get database properties.
     *
     * @return props
     */
    Properties getProps() {
        Properties props = new Properties();

        String propertyValue = retrievePropValue(DatabaseProperties.DB_USERNAME, DEFAULT_DB_USERNAME);
        props.put(USER_PROP, propertyValue);

        propertyValue = retrievePropValue(DatabaseProperties.DB_PASSWORD, DEFAULT_DB_PASSWORD);
        props.put(PASSWORD_PROP, propertyValue);

        propertyValue = retrievePropValue(DatabaseProperties.DB_USE_SSL, DEFAULT_DB_USE_SSL);
        props.put(USE_SSL_PROP, propertyValue);

        return props;
    }

    /**
     * Get database url.
     *
     * @return url
     */
    String getConnectionQuery() {
        String driver = retrievePropValue(DatabaseProperties.DB_DRIVER, DEFAULT_DB_DRIVER);
        String connection = retrievePropValue(DatabaseProperties.DB_CONNECTION, DEFAULT_DB_CONNECTION);
        String host = retrievePropValue(DatabaseProperties.DB_HOST, DEFAULT_DB_HOST);
        String port = retrievePropValue(DatabaseProperties.DB_PORT, DEFAULT_DB_PORT);
        String database = retrievePropValue(DatabaseProperties.DB_DATABASE, DEFAULT_DB_DATABASE);
        String characterEncoding = retrievePropValue(DatabaseProperties.DB_CHARACTER_ENCODING, DEFAULT_DB_CHARACTER_ENCODING);

        boolean rewriteBatchedStatements;
        String propName = String.valueOf(DatabaseProperties.DB_REWRITE_BATCHED_STATEMENTS);

        try {
            rewriteBatchedStatements = Boolean.valueOf(dbBundle.getString(propName));
        } catch (MissingResourceException e) {
            rewriteBatchedStatements = DEFAULT_DB_REWRITE_BATCHED_STATEMENTS;
            LOGGER.log(Level.ERROR, "Can't find " + propName + " prop", e);
        }

        return String.format("%s:%s://%s:%s/%s?characterEncoding=%s&rewriteBatchedStatements=%s",
                driver, connection, host, port, database, characterEncoding, rewriteBatchedStatements);
    }

    /**
     * Retrieve property value.
     *
     * @param property
     * @param defaultValue
     * @return property value
     */
    private String retrievePropValue(DatabaseProperties property, String defaultValue) {
        try {
            return dbBundle.getString(String.valueOf(property));
        } catch (MissingResourceException e) {
            LOGGER.log(Level.ERROR, "Can't find " + property + " prop", e);
        }

        return defaultValue;
    }

    /**
     * Get pool size.
     *
     * @return pool size
     */
    int getPoolSize() {
        String propName = String.valueOf(DatabaseProperties.DB_POOL_SIZE);
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
