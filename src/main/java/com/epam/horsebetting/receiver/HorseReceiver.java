package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface HorseReceiver {

    /**
     * Create a new horse.
     *
     * @param content
     * @throws ReceiverException
     */
    void createHorse(RequestContent content) throws ReceiverException;

    /**
     * Remove horse.
     *
     * @param content
     * @throws ReceiverException
     */
    void removeHorse(RequestContent content) throws ReceiverException;

    /**
     * Update horse.
     *
     * @param content
     * @throws ReceiverException
     */
    void updateHorse(RequestContent content) throws ReceiverException;
}
