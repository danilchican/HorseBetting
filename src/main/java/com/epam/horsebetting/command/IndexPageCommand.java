package com.epam.horsebetting.command;

import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.receiver.AbstractReceiver;
import com.epam.horsebetting.config.PageConfig;
import com.epam.horsebetting.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexPageCommand extends AbstractCommand {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Constructor.
     *
     * @param receiver
     */
    public IndexPageCommand(AbstractReceiver receiver) {
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
        LOGGER.log(Level.DEBUG, "Execute() method of " + this.getClass().getName());

        String commandName = String.valueOf(request.findRequestAttribute(COMMAND_INSTANCE_NAME));
        Router router;
        String page;

        try {
            page = PageConfig.getInstance().takePage(PageConfig.PageConfigType.WELCOME);
            receiver.action(CommandType.findByTag(commandName), request);
            router = new Router(page, Router.RouteType.FORWARD);
        } catch (ReceiverException e) {
            page = PageConfig.getInstance().takePage(PageConfig.PageConfigType.NOT_FOUND);
            router = new Router(page, Router.RouteType.REDIRECT);
            LOGGER.log(Level.ERROR, e);
        }

        request.insertRequestAttribute(Router.ROUTER_INSTANCE_NAME, router);
    }
}
