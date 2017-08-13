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
     * Get total count of bets.
     *
     * @return total count
     * @throws DAOException
     */
    int getTotalCount() throws DAOException;
}
