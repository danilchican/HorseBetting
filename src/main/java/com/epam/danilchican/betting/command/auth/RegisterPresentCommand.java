package com.epam.danilchican.betting.command.auth;

import com.epam.danilchican.betting.command.AbstractCommand;
import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.receiver.AbstractReceiver;
import com.epam.danilchican.betting.request.RequestContent;
import com.epam.danilchican.betting.type.RouteType;
import com.epam.danilchican.betting.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegisterPresentCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public RegisterPresentCommand(AbstractReceiver receiver) {
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

        super.execute(request);

        Router router = new Router("/jsp/auth/register.jsp", RouteType.FORWARD);
        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}