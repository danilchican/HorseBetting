package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.receiver.AbstractReceiver;

public class AuthCommand extends AbstractCommand {

    /**
     * Constructor.
     *
     * @param receiver
     */
    public AuthCommand(AbstractReceiver receiver) {
        super(receiver);
    }
}
