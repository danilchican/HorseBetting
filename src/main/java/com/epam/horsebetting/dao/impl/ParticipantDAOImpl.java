package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.ParticipantDAO;
import com.epam.horsebetting.entity.Participant;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ParticipantDAOImpl extends AbstractDAO<Participant> implements ParticipantDAO {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * SQL queries for ParticipantDAOImpl.
     */
    private static final String SQL_FIND_PARTICIPANTS_BY_RACE_ID = "SELECT `p`.`id`, `p`.`horse_id`, " +
            "`p`.`race_id`, `p`.`coefficient`, `p`.`is_winner`, `h`.`name` AS `jockey` FROM `participants` AS `p` " +
            "LEFT JOIN `horses` AS `h` ON `p`.`horse_id`=`h`.`id` WHERE `p`.`race_id`=?;";
    private static final String SQL_FIND_PARTICIPANT_BY_ID = "SELECT `p`.`id`, `p`.`horse_id`, " +
            "`p`.`race_id`, `p`.`coefficient`, `p`.`is_winner`, `h`.`name` AS `jockey` FROM `participants` AS `p` " +
            "LEFT JOIN `horses` AS `h` ON `p`.`horse_id`=`h`.`id` WHERE `p`.`id`=?;";
    private static final String SQL_INSERT_PARTICIPANTS = "INSERT INTO `participants` " +
            "(`horse_id`, `race_id`, `coefficient`)" + " VALUES (?,?,?);";
    private static final String SQL_UPDATE_PARTICIPANTS = "UPDATE `participants` SET `coefficient`=? WHERE `id`=?;";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public ParticipantDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Find a participant by id.
     *
     * @param id
     * @return participant
     * @throws DAOException
     */
    @Override
    public Participant find(int id) throws DAOException {
        ResultSet resultSet;
        Participant participant = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PARTICIPANT_BY_ID)) {
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                participant = extractFrom(resultSet);
            }
        } catch (SQLException e) {
            throw new DAOException("Can't find participant by id[" + id + "]. " + e.getMessage(), e);
        }

        return participant;
    }

    /**
     * Find participant by race id.
     *
     * @param id
     * @return participants
     */
    @Override
    public List<Participant> findByRaceId(int id) throws DAOException {
        List<Participant> foundedHorses = new ArrayList<>();
        ResultSet participants;

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_PARTICIPANTS_BY_RACE_ID)) {
            preparedStatement.setInt(1, id);
            participants = preparedStatement.executeQuery();

            while (participants.next()) {
                Participant participant = extractFrom(participants);
                foundedHorses.add(participant);
            }
        } catch (SQLException e) {
            throw new DAOException("Cannot retrieve participants list. " + e.getMessage(), e);
        }

        return foundedHorses;
    }

    /**
     * Create participants to race.
     *
     * @param participants
     * @param race
     */
    @Override
    public void create(HashMap<Integer, BigDecimal> participants, Race race) throws DAOException {
        int affectedHorses[];

        try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PARTICIPANTS)) {
            for (Map.Entry<Integer, BigDecimal> participant : participants.entrySet()) {
                statement.setInt(1, participant.getKey());
                statement.setInt(2, race.getId());
                statement.setBigDecimal(3, participant.getValue());

                statement.addBatch();
            }

            affectedHorses = statement.executeBatch();
            LOGGER.log(Level.DEBUG, "Count of inserted participants to race:" + Arrays.toString(affectedHorses));
        } catch (SQLException e) {
            throw new DAOException("Cannot save participants to race. " + e.getMessage(), e);
        }
    }

    /**
     * Update participants of race.
     *
     * @param participants
     */
    @Override
    public void update(HashMap<Integer, BigDecimal> participants) throws DAOException {
        int affectedHorses[];

        try (PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PARTICIPANTS)) {
            for (Map.Entry<Integer, BigDecimal> participant : participants.entrySet()) {
                statement.setBigDecimal(1, participant.getValue());
                statement.setInt(2, participant.getKey());

                statement.addBatch();
            }

            affectedHorses = statement.executeBatch();
            LOGGER.log(Level.DEBUG, "Count of updated participants to race:" + Arrays.toString(affectedHorses));
        } catch (SQLException e) {
            throw new DAOException("Cannot update participants to race. " + e.getMessage(), e);
        }
    }

    /**
     * Extract participant data from result set to participant instance.
     *
     * @param dataSet
     * @return participant instance
     * @throws SQLException
     */
    @Override
    public Participant extractFrom(ResultSet dataSet) throws SQLException {
        Participant participant = new Participant();

        participant.setId(dataSet.getInt(SQLFieldConfig.Participant.ID));
        participant.setHorseId(dataSet.getInt(SQLFieldConfig.Participant.HORSE_ID));
        participant.setRaceId(dataSet.getInt(SQLFieldConfig.Participant.RACE_ID));
        participant.setCoefficient(dataSet.getBigDecimal(SQLFieldConfig.Participant.COEFFICIENT));
        participant.setWinner(dataSet.getBoolean(SQLFieldConfig.Participant.IS_WINNER));

        if (hasColumn(dataSet, SQLFieldConfig.Participant.JOCKEY)) {
            participant.insertAttribute(SQLFieldConfig.Participant.JOCKEY,
                    dataSet.getString(SQLFieldConfig.Participant.JOCKEY));
        }

        return participant;
    }
}
