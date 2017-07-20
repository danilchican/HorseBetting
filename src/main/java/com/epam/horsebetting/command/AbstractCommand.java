package com.epam.horsebetting.command;

import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.request.RequestContent;

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
    public static final String COMMAND_INSTANCE_NAME = "commandName";

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
