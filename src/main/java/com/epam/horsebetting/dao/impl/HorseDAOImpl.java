package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.HorseDAO;
import com.epam.horsebetting.entity.Horse;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HorseDAOImpl extends AbstractDAO<Horse> implements HorseDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for HorseDAOImpl.
     */
    private static final String SQL_SELECT_ALL_HORSES = "SELECT * FROM `horses`;";
    private static final String SQL_SELECT_PART_HORSES = "SELECT * FROM `horses` LIMIT ? OFFSET ?;";

    /**
     * Find all horses.
     *
     * @return list of horses
     * @throws DAOException
     */
    @Override
    public List<Horse> findAll() throws DAOException {
        List<Horse> foundedHorses = new ArrayList<>();
        ResultSet horses;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_HORSES)) {
            horses = preparedStatement.executeQuery();

            while (horses.next()) {
                Horse horse = extractFrom(horses);
                foundedHorses.add(horse);
                LOGGER.log(Level.DEBUG, "Horse was added to list: " + horse);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve horses list. " + e.getMessage(), e);
        }

        return foundedHorses;
    }

    /**
     * Obtain part of horses.
     *
     * @param limit
     * @param offset
     * @return horses
     * @throws DAOException
     */
    @Override
    public List<Horse> obtainPart(int limit, int offset) throws DAOException {
        List<Horse> foundedHorses = new ArrayList<>();
        ResultSet horses;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PART_HORSES)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            horses = preparedStatement.executeQuery();

            while (horses.next()) {
                Horse horse = extractFrom(horses);
                foundedHorses.add(horse);
                LOGGER.log(Level.DEBUG, "Horse was added to list: " + horse);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve horses list. " + e.getMessage(), e);
        }

        return foundedHorses;
    }

    /**
     * Extract horse data from result set to horse instance.
     *
     * @param horseDataSet
     * @return horse instance
     * @throws SQLException
     */
    @Override
    public Horse extractFrom(ResultSet horseDataSet) throws SQLException {
        Horse horse = new Horse();

        horse.setId(horseDataSet.getInt("id"));
        horse.setSuitId(horseDataSet.getInt("suit_id"));
        horse.setName(horseDataSet.getString("name"));
        horse.setAge(horseDataSet.getByte("age"));
        horse.setSex(horseDataSet.getBoolean("password"));

        return horse;
    }
}
