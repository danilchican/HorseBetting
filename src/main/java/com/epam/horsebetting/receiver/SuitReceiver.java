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
}
