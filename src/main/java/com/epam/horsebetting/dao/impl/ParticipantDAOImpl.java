package com.epam.horsebetting.dao.impl;

import com.epam.horsebetting.config.SQLFieldConfig;
import com.epam.horsebetting.dao.AbstractDAO;
import com.epam.horsebetting.dao.ParticipantDAO;
import com.epam.horsebetting.entity.Participant;
import com.epam.horsebetting.exception.DAOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ParticipantDAOImpl extends AbstractDAO<Participant> implements ParticipantDAO {

    /**
     * SQL queries for ParticipantDAOImpl.
     */
    private static final String SQL_FIND_PARTICIPANTS_BY_RACE_ID = "SELECT * FROM `participants` WHERE `race_id`=?;";

    /**
     * Default constructor connection.
     *
     * @param isForTransaction
     */
    public ParticipantDAOImpl(boolean isForTransaction) {
        super(isForTransaction);
    }

    /**
     * Create a new participant.
     *
     * @param participant
     * @return participant
     * @throws DAOException
     */
    @Override
    public Participant create(Participant participant) throws DAOException {
        return null;
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

        return participant;
    }
}
