package com.epam.danilchican.betting.dao;

import com.epam.danilchican.betting.database.ConnectionPool;
import com.epam.danilchican.betting.database.ProxyConnection;
import com.epam.danilchican.betting.entity.Entity;
import com.epam.danilchican.betting.exception.DatabaseException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class AbstractDAO<T extends Entity> implements AutoCloseable {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Used connection.
     */
    ProxyConnection connection;

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
     * @throws DatabaseException
     */
    public abstract List<T> findAll() throws DatabaseException;

    /**
     * Find entity by id.
     *
     * @param id
     * @return entity
     */
    public abstract T find(int id) throws DatabaseException;

    /**
     * Create new entity.
     *
     * @param entity
     * @return T
     * @throws DatabaseException
     */
    public abstract T create(T entity) throws DatabaseException;

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
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            LOGGER.log(Level.ERROR, e);
        }
    }
}
