package com.epam.horsebetting.database;

import com.epam.horsebetting.dao.AbstractDAO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionManager {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * DAOs of transaction.
     */
    private List<AbstractDAO> daos;

    /**
     * Connection to execute transaction.
     */
    private ProxyConnection connection;

    /**
     * Constructor.
     */
    public TransactionManager(AbstractDAO dao, AbstractDAO ... daos) {
        this.daos = new ArrayList<>();
        connection = ConnectionPool.getInstance().fetchConnection();

        this.daos.add(dao);
        Collections.addAll(this.daos, daos);
    }

    /**
     * Transaction begin.
     */
    public void beginTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't begin transaction. " + e.getMessage(), e);
        }

        LOGGER.log(Level.INFO, "Transaction has been begin.");
        daos.forEach(dao -> dao.setConnection(connection));
    }

    /**
     * Transaction end.
     */
    public void endTransaction() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't finish transaction", e);
        }

        LOGGER.log(Level.INFO, "Transaction has been end.");
        ConnectionPool.getInstance().releaseConnection(connection);
    }

    /**
     * Makes all changes made since the previous
     * commit/rollback permanent and releases any database locks
     * currently held by this <code>Connection</code> object.
     * This method should be
     * used only when auto-commit mode has been disabled.
     */
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't commit transaction", e);
        }
    }

    /**
     * Undoes all changes made in the current transaction
     * and releases any database locks currently held
     * by this <code>Connection</code> object.
     * This method should be
     * used only when auto-commit mode has been disabled.
     */
    public void rollback() {
        try {
            connection.rollback();
            LOGGER.log(Level.INFO, "Transaction has been rolled back.");
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Can't rollback transaction", e);
        }
    }
}
