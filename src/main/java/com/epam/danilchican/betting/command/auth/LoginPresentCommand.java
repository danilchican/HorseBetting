package com.epam.danilchican.betting.command.auth;

import com.epam.danilchican.betting.command.AbstractCommand;
import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.exception.ReceiverException;
import com.epam.danilchican.betting.receiver.AbstractReceiver;
import com.epam.danilchican.betting.request.RequestContent;
import com.epam.danilchican.betting.type.RouteType;
import com.epam.danilchican.betting.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginPresentCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public LoginPresentCommand(AbstractReceiver receiver) {
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
        LOGGER.log(Level.INFO, "Processing execute() method of " + this.getClass().getName());

        String commandName = String.valueOf(request.findRequestAttribute(COMMAND_INSTANCE_NAME));
        Router router;

        try {
            receiver.action(CommandType.findByTag(commandName), request);
            router = new Router("/jsp/auth/login.jsp", RouteType.FORWARD);
        } catch (ReceiverException e) {
            LOGGER.log(Level.ERROR, e);
            router = new Router("/jsp/errors/404.jsp", RouteType.FORWARD);
        }

        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}
