package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.exception.ReceiverException;
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
    protected AbstractReceiver receiver;

    /**
     * Command name instance.
     */
    public static String COMMAND_INSTANCE_NAME = "commandName";

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
    public abstract void execute(RequestContent request) throws IllegalCommandTypeException;
}
