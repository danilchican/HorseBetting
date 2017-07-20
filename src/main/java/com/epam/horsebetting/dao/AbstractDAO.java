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
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

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
     * Set connection.
     *
     * @param connection
     */
    public void setConnection(ProxyConnection connection) {
        this.connection = connection;
    }

    /**
     * Find all entities.
     *
     * @return entity list
     * @throws DAOException
     */
    public abstract List<T> findAll() throws DAOException;

    /**
     * Find entity by id.
     *
     * @param id
     * @return entity
     */
    public abstract T find(int id) throws DAOException;

    /**
     * Create new entity.
     *
     * @param entity
     * @return T
     * @throws DAOException
     */
    public abstract T create(T entity) throws DAOException;

    /**
     * Delete entity by id.
     *
     * @param id
     * @return boolean
     */
    public abstract boolean delete(int id);

    /**
     * Delete entity by object.
     *
     * @param entity
     * @return boolean
     */
    public abstract boolean delete(T entity);

    /**
     * Update entity.
     *
     * @param entity
     * @return updated entity
     */
    public abstract T update(T entity);

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
