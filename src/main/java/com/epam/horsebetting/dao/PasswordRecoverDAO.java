package com.epam.horsebetting.dao;

import com.epam.horsebetting.entity.PasswordRecover;
import com.epam.horsebetting.exception.DAOException;

public interface PasswordRecoverDAO {

    /**
     * Create a new recover.
     *
     * @param recover
     * @throws DAOException
     */
    void create(PasswordRecover recover) throws DAOException;

    /**
     * Find recover by token.
     *
     * @param token
     * @return recover
     */
    PasswordRecover findByToken(String token) throws DAOException;
}
