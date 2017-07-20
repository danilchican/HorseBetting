package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Entity;
import com.epam.horsebetting.database.ConnectionPool;
import com.epam.horsebetting.database.ProxyConnection;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<T extends Entity> implements AutoCloseable {

    /**
     * Used connection.
     */
    protected ProxyConnection connection;

    /**
     * Default constructor connection.
     */
    public AbstractDAO() {
        this.connection = ConnectionPool.getInstance().fetchConnection();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     */
    @Override
    public void close() throws DAOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DAOException(e);
            }
        }
    }
}
