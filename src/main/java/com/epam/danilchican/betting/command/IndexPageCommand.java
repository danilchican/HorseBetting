package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.receiver.AbstractReceiver;

public class IndexPageCommand extends AbstractCommand {

    /**
     * Constructor.
     *
     * @param receiver
     */
    public IndexPageCommand(AbstractReceiver receiver) {
        super(receiver);
    }
}
