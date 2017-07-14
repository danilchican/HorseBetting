package com.epam.danilchican.betting.receiver;

import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.request.RequestContent;

public abstract class AbstractReceiver {

    /**
     * Execute receiver action.
     *
     * @param type
     * @param content
     */
    public final void action(CommandType type, RequestContent content) {
        type.doReceiver(content);
    }
}
