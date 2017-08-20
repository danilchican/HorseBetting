package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
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
    private static final String SQL_ADD_HORSE = "INSERT INTO `horses` (name, age, gender, suit_id) VALUES (?,?,?,?);";
    private static final String SQL_UPDATE_HORSE = "UPDATE `horses` SET `name`=?, `age`=?, `gender`=?, `suit_id`=? WHERE `id`=?;";
    private static final String SQL_REMOVE_HORSE = "DELETE FROM `horses` WHERE `id`=?;";
    private static final String SQL_FIND_HORSE_BY_ID = "SELECT * FROM `horses` WHERE `id`=? LIMIT 1;";
    private static final String SQL_FIND_HORSE_BY_NAME = "SELECT * FROM `horses` WHERE `name`=? LIMIT 1;";
    private static final String SQL_SELECT_HORSES = "SELECT * FROM `horses` ";
    private static final String SQL_COUNT_HORSES = "SELECT COUNT(*) AS `total` FROM `horses`;";
    private static final String SQL_SELECT_PART_HORSES = "SELECT `h`.`id`, `h`.`name`, `h`.`suit_id`, " +
            "`s`.`name` AS `suit_name`, `h`.`age`, `h`.`gender` FROM `horses` AS `h` " +
            "LEFT JOIN `suits` AS `s` ON `h`.`suit_id`=`s`.`id` LIMIT ? OFFSET ?;";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public HorseDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new horse.
     *
     * @param horse
     * @return horse
     * @throws DAOException
     */
    @Override
    public Horse create(Horse horse) throws DAOException {
        Horse createdHorse;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_ADD_HORSE)) {
            preparedStatement.setString(1, horse.getName());
            preparedStatement.setByte(2, horse.getAge());
            preparedStatement.setBoolean(3, horse.getGender());
            preparedStatement.setInt(4, horse.getSuitId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't add a new horse to the database.");
            }

            createdHorse = findByName(horse.getName());
        } catch (SQLException e) {
            throw new DAOException("Can't add a new horse. " + e.getMessage(), e);
        }

        return createdHorse;
    }

    /**
     * Update horse.
     *
     * @param horse
     * @throws DAOException
     */
    @Override
    public void update(Horse horse) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_HORSE)) {
            preparedStatement.setString(1, horse.getName());
            preparedStatement.setByte(2, horse.getAge());
            preparedStatement.setBoolean(3, horse.getGender());
            preparedStatement.setInt(4, horse.getSuitId());
            preparedStatement.setInt(5, horse.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't update horse.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't update horse. " + e.getMessage(), e);
        }
    }

    /**
     * Remove horse.
     *
     * @param horse
     * @throws DAOException
     */
    @Override
    public void remove(Horse horse) throws DAOException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_REMOVE_HORSE)) {
            preparedStatement.setInt(1, horse.getId());

            if (preparedStatement.executeUpdate() != 1) {
                throw new DAOException("Can't remove horse from the database.");
            }
        } catch (SQLException e) {
            throw new DAOException("Can't remove horse. " + e.getMessage(), e);
        }
    }

    /**
     * Find horse by id.
     *
     * @param id
     * @return horse
     */
    @Override
    public Horse find(int id) throws DAOException {
        ResultSet resultSet;
        Horse horse = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_HORSE_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                horse = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find horse by id[" + id + "]. " + e.getMessage(), e);
        }

        return horse;
    }

    /**
     * Find horse by name.
     *
     * @param name
     * @return horse
     */
    @Override
    public Horse findByName(String name) throws DAOException {
        ResultSet resultSet;
        Horse horse = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_HORSE_BY_NAME)) {
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                horse = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find horse: " + e.getMessage(), e);
        }

        return horse;
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
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve horses list. " + e.getMessage(), e);
        }

        return foundedHorses;
    }

    /**
     * Find all horses.
     *
     * @return horses
     * @throws DAOException
     */
    @Override
    public List<Horse> findAll() throws DAOException {
        List<Horse> foundedHorses = new ArrayList<>();
        ResultSet horses;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_HORSES)) {
            horses = preparedStatement.executeQuery();

            while (horses.next()) {
                Horse horse = extractFrom(horses);
                foundedHorses.add(horse);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve horses list. " + e.getMessage(), e);
        }

        return foundedHorses;
    }

    /**
     * Get total count of horses.
     *
     * @return total count
     * @throws DAOException
     */
    @Override
    public int getTotalCount() throws DAOException {
        ResultSet result;
        int totalCount = 0;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_COUNT_HORSES)) {
            result = preparedStatement.executeQuery();

            while (result.next()) {
                totalCount = result.getInt(SQLFieldConfig.Common.TOTAL);
                LOGGER.log(Level.DEBUG, "Count of horses: " + totalCount);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve total count. " + e.getMessage(), e);
        }

        return totalCount;
    }

    /**
     * Extract horse data from result set to horse instance.
     *
     * @param horseDataSet
     * @return horse instance
     * @throws SQLException
     */
    private Horse extractFrom(ResultSet horseDataSet) throws SQLException {
        Horse horse = new Horse();

        horse.setId(horseDataSet.getInt(SQLFieldConfig.Horse.ID));
        horse.setSuitId(horseDataSet.getInt(SQLFieldConfig.Horse.SUIT_ID));
        horse.setName(horseDataSet.getString(SQLFieldConfig.Horse.NAME));
        horse.setAge(horseDataSet.getByte(SQLFieldConfig.Horse.AGE));
        horse.setGender(horseDataSet.getBoolean(SQLFieldConfig.Horse.GENDER));

        if (hasColumn(horseDataSet, SQLFieldConfig.Horse.SUIT_NAME)) {
            horse.insertAttribute(SQLFieldConfig.Horse.SUIT_NAME,
                    horseDataSet.getString(SQLFieldConfig.Horse.SUIT_NAME));
        }

        return horse;
    }
}
