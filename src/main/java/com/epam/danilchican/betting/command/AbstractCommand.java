package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.receiver.AbstractReceiver;
import com.epam.danilchican.betting.request.RequestContent;

/**
 * Base abstract class for all commands.
 *
 * @author Vladislav Danilchik <danilchican@mail.ru>
 */
public abstract class AbstractCommand {

    /**
     * Command receiver.
     */
    private AbstractReceiver receiver;

    /**
     * Constructor.
     *
     * @param receiver
     */
    public AbstractCommand(AbstractReceiver receiver) {
        this.receiver = receiver;
    }

    /**
     * Get command receiver.
     *
     * @return receiver
     */
    public AbstractReceiver getReceiver() {
        return receiver;
    }

    /**
     * Execute command with request.
     *
     * @param request
     * @see RequestContent
     */
    public void execute(RequestContent request) throws IllegalCommandTypeException {
        String commandName = String.valueOf(request.findRequestAttribute("command"));

        receiver.action(CommandType.findByTag(commandName), request);
    }
}
