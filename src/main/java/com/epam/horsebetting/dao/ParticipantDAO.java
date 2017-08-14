package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Participant;
import com.epam.horsebetting.exception.DAOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ParticipantDAO {

    /**
     * Create a new participant.
     *
     * @param participant
     * @return participant
     * @throws DAOException
     */
    Participant create(Participant participant) throws DAOException;

    /**
     * Find a participant by id.
     *
     * @param id
     * @return participant
     * @throws DAOException
     */
    Participant find(int id) throws DAOException;

    /**
     * Find participant by race id.
     *
     * @param id
     * @return participants
     */
    List<Participant> findByRaceId(int id) throws DAOException;

    /**
     * Extract participant data from result set to participant instance.
     *
     * @param dataSet
     * @return participant instance
     * @throws SQLException
     */
    Participant extractFrom(ResultSet dataSet) throws SQLException;
}
