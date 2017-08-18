package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Entity;
import com.epam.horsebetting.database.ConnectionPool;
import com.epam.horsebetting.database.ProxyConnection;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class AbstractDAO<T extends Entity> implements AutoCloseable {

    /**
     * Used connection.
     */
    protected ProxyConnection connection;

    /**
     * Default constructor connection.
     */
    public AbstractDAO(boolean isForTransaction) {
        if(!isForTransaction) {
            this.connection = ConnectionPool.getInstance().fetchConnection();
        }
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
     * Check if the Result set has column.
     *
     * @param set
     * @param columnName
     * @return boolean
     * @throws SQLException
     */
    protected boolean hasColumn(ResultSet set, String columnName) throws SQLException {
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();

        for (int x = 1; x <= columns; x++) {
            String column = meta.getColumnLabel(x);
            if (columnName.equals(column)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return back connection into pool.
     */
    @Override
    public void close() throws DAOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DAOException("Cannot close connection. " + e.getMessage(), e);
            }
        }
    }
}
