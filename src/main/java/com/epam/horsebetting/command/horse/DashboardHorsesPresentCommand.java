package com.epam.horsebetting.command.horse;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.config.PageConfig;
import com.epam.horsebetting.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DashboardHorsesPresentCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public DashboardHorsesPresentCommand(AbstractReceiver receiver) {
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
            page = PageConfig.getInstance().takePage(PageConfig.Page.DASHBOARD_HORSES_INDEX);
        } catch (ReceiverException e) {
            page = PageConfig.getInstance().takePage(PageConfig.Page.NOT_FOUND);
            LOGGER.log(Level.ERROR, e);
        }

        Router router = new Router(page, Router.RouteType.FORWARD);
        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}
