package com.epam.horsebetting.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Instance of ConnectionPool.
     */
    private static ConnectionPool instance;

    /**
     * Instance creation flag.
     */
    private static AtomicBoolean createdInstance = new AtomicBoolean(false);

    /**
     * Reentrant lock.
     */
    private static ReentrantLock lock = new ReentrantLock();

    /**
     * Secure connections queue.
     */
    private BlockingQueue<ProxyConnection> connections;

    /**
     * Database manager instance.
     */
    private DatabaseManager dbManager;

    /**
     * Default constructor.
     */
    private ConnectionPool() {
        dbManager = new DatabaseManager();
        dbManager.registerDriver();

        Properties props = dbManager.getProps();

        final int poolSize = dbManager.getPoolSize();
        String dbUrl = dbManager.getURL();

        connections = new ArrayBlockingQueue<>(poolSize);

        for (int i = 0; i < poolSize; i++) {
            try {
                Connection connection = DriverManager.getConnection(dbUrl, props);
                ProxyConnection proxyConnection = new ProxyConnection(connection);

                connections.offer(proxyConnection);
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Connection hasn't been initialized!", e);
            }
        }
    }

    /**
     * Get instance of ConnectionPool.
     *
     * @return instance
     */
    public static ConnectionPool getInstance() {
        if (!createdInstance.get()) {
            try {
                lock.lock();

                if (instance == null) {
                    instance = new ConnectionPool();
                    createdInstance.set(true);

                    LOGGER.log(Level.INFO, "ConnectionPool instance created!");
                }
            } finally {
                lock.unlock();
            }
        }

        return instance;
    }

    /**
     * Fetch available connection from connection list.
     *
     * @return connection
     */
    public ProxyConnection fetchConnection() {
        ProxyConnection connection = null;

        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, "Can't fetch a connection!", e);
        }

        return connection;
    }

    /**
     * Release used connection.
     *
     * @param connection
     * @see ProxyConnection
     */
    public void releaseConnection(ProxyConnection connection) {
        try {
            connections.put(connection);
        } catch (InterruptedException e) {
            LOGGER.log(Level.ERROR, "Can't release the connection!", e);
        }
    }

    /**
     * Close connection pool.
     */
    public void close() {
        for (int i = 0; i < dbManager.getPoolSize(); i++) {
            try {
                ProxyConnection connection = connections.take();
                connection.realClose();
            } catch (SQLException | InterruptedException e) {
                LOGGER.log(Level.ERROR, "Can't close the connection", e);
            }
        }

        dbManager.deregisterDrivers();
        LOGGER.log(Level.INFO, "ConnectionPool closed!");
    }
}
