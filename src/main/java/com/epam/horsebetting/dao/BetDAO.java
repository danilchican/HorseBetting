package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.Bet;
import com.epam.horsebetting.exception.DAOException;

public interface BetDAO {

    /**
     * Create a new bet.
     *
     * @param bet
     * @param userId
     * @throws DAOException
     */
    void create(Bet bet, int userId) throws DAOException;
}
