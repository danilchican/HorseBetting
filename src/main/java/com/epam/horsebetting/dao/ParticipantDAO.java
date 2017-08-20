package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Participant;
import com.epam.horsebetting.entity.Race;
import com.epam.horsebetting.exception.DAOException;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ParticipantDAO {

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
     * Create participants to race.
     *
     * @param participants
     * @param race
     */
    void create(HashMap<Integer, BigDecimal> participants, Race race) throws DAOException;

    /**
     * Update participants of race.
     *
     * @param participants
     */
    void update(HashMap<Integer, BigDecimal> participants) throws DAOException;

    /**
     * Update winner of race.
     *
     * @param raceId
     * @param winnerId
     */
    void updateWinner(int raceId, int winnerId) throws DAOException;

    /**
     * Extract participant data from result set
     * to participant instance.
     *
     * @param dataSet
     * @return participant instance
     * @throws SQLException
     */
    Participant extractFrom(ResultSet dataSet) throws SQLException;
}
