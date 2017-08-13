package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface BetReceiver {

    /**
     * Create a new bet.
     *
     * @param content
     * @throws ReceiverException
     */
    void createBet(RequestContent content) throws ReceiverException;
}
