package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface SuitReceiver {

    /**
     * Obtain suits list ajax.
     *
     * @param content
     * @throws ReceiverException
     */
    void ajaxObtainSuitsList(RequestContent content) throws ReceiverException;

    /**
     * Create a new suit.
     *
     * @param content
     * @throws ReceiverException
     */
    void createSuit(RequestContent content) throws ReceiverException;


    /**
     * Update suit.
     *
     * @param content
     * @throws ReceiverException
     */
    void updateSuit(RequestContent content) throws ReceiverException;

    /**
     * Remove suit.
     *
     * @param content
     * @throws ReceiverException
     */
    void removeSuit(RequestContent content) throws ReceiverException;
}
