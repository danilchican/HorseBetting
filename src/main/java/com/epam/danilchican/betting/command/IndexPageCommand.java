package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.receiver.AbstractReceiver;
import com.epam.danilchican.betting.request.RequestContent;

public class IndexPageCommand extends AbstractCommand {

    /**
     * Constructor.
     *
     * @param receiver
     */
    public IndexPageCommand(AbstractReceiver receiver) {
        super(receiver);
    }

    /**
     * Execute command with request.
     *
     * @param request
     * @see RequestContent
     */
    @Override
    public void execute(RequestContent request) throws IllegalCommandTypeException {
        // validation data
        super.execute(request);
        // navigate to page
    }
}
