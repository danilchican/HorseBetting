package com.epam.horsebetting.receiver;

import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public interface RaceReceiver {

    /**
     * Create a new race.
     *
     * @param content
     * @throws ReceiverException
     */
    void createRace(RequestContent content) throws ReceiverException;

    /**
     * Edit an existing race.
     *
     * @param content
     * @throws ReceiverException
     */
    void editRace(RequestContent content) throws ReceiverException;
}
