package com.epam.horsebetting.command.auth;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.config.PageConfig;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogoutCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public LogoutCommand(AbstractReceiver receiver) {
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
        String page;

        try {
            receiver.action(CommandType.findByTag(commandName), request);
            page = PageConfig.getInstance().takeAddress(PageConfig.Page.AUTH_LOGIN);
        } catch (ReceiverException e) {
            LOGGER.log(Level.ERROR, e);
            page = PageConfig.ROOT;
        }

        Router router = new Router(page, Router.RouteType.REDIRECT);
        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}
