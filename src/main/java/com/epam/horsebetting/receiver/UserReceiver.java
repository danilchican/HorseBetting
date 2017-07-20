package com.epam.horsebetting.receiver;

import com.epam.horsebetting.entity.User;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface UserReceiver {

    /**
     * Register a new user.
     *
     * @param content
     */
    void register(RequestContent content) throws ReceiverException;

    /**
     * Login a user.
     *
     * @param content
     */
    void login(RequestContent content) throws ReceiverException;

    /**
     * Logout a user.
     *
     * @param content
     */
    void logout(RequestContent content) throws ReceiverException;
}
