package com.epam.horsebetting.command.auth;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SendMailToResetPasswordCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public SendMailToResetPasswordCommand(AbstractReceiver receiver) {
        super(receiver);
    }

    /**
     * Execute command with request.
     *
     * @param request
     * @see RequestContent
     */
    @Override
    public void execute(RequestContent request) throws CommandTypeNotFoundException {
        String commandName = String.valueOf(request.findRequestAttribute(COMMAND_INSTANCE_NAME));

        try {
            receiver.action(CommandType.findByTag(commandName), request);
        } catch (ReceiverException e) {
            LOGGER.log(Level.ERROR, e);
        }

        Router router = new Router("/password/reset", Router.RouteType.REDIRECT);
        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}