package com.epam.horsebetting.command.suit;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AjaxDashboardCreateSuitCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public AjaxDashboardCreateSuitCommand(AbstractReceiver receiver) {
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
        String commandName = String.valueOf(request.findRequestAttribute(COMMAND_INSTANCE_NAME));

        try {
            receiver.action(CommandType.findByTag(commandName), request);
        } catch (ReceiverException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
    }
}
