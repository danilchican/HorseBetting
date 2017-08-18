package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.exception.DAOException;

import java.util.List;

public interface BetDAO {

    /**
     * Create a new bet.
     *
     * @param bet
     * @param userId
     * @throws DAOException
     */
    void create(Bet bet, int userId) throws DAOException;

    /**
     * Find bet by id.
     *
     * @param id
     * @return bet
     */
    Bet find(int id) throws DAOException;

    /**
     * Remove bet by id.
     *
     * @param id
     * @throws DAOException
     */
    void remove(int id) throws DAOException;

    /**
     * Obtain part of bets.
     *
     * @param userId
     * @param limit
     * @param offset
     * @return bets
     * @throws DAOException
     */
    List<Bet> obtainPart(int userId, int limit, int offset) throws DAOException;

    /**
     * Find all bets of race by race id.
     *
     * @return bets
     * @throws DAOException
     */
    List<Bet> findAllOfRace(int raceId) throws DAOException;

    /**
     * Get total count of bets.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;

    /**
     * Get total count of bets for user.
     *
     * @return total count for user
     * @throws DAOException
     */
    int getTotalForUser(int id) throws DAOException;
}
