package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.receiver.AbstractReceiver;

public class RegisterCommand extends AbstractCommand {

    /**
     * Constructor.
     *
     * @param receiver
     */
    public RegisterCommand(AbstractReceiver receiver) {
        super(receiver);
    }
}
