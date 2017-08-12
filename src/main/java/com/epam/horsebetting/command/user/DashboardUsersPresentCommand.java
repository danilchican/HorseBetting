package com.epam.horsebetting.command.user;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.config.PageConfig;
import com.epam.horsebetting.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashboardUsersPresentCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public DashboardUsersPresentCommand(AbstractReceiver receiver) {
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
        String page;

        try {
            receiver.action(CommandType.findByTag(commandName), request);
            page = PageConfig.getInstance().takePage(PageConfig.PageConfigType.DASHBOARD_USERS_INDEX);
        } catch (ReceiverException e) {
            page = PageConfig.getInstance().takePage(PageConfig.PageConfigType.NOT_FOUND);
            LOGGER.log(Level.ERROR, e);
        }

        Router router = new Router(page, Router.RouteType.FORWARD);
        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}
