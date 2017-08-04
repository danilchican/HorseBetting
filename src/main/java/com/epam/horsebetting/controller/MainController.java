package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.RouteNotFoundException;
import com.epam.horsebetting.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.horsebetting.util.Router.ROUTER_INSTANCE_NAME;

@WebServlet(name = "MainController", urlPatterns = "/")
public class MainController extends HttpServlet {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Index page command value.
     */
    private static final String INDEX_PAGE_COMMAND_VALUE = "index::get";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            AbstractCommand command = initCommand(request);
            LOGGER.log(Level.DEBUG, "Initialized command: " + command.getClass().getName());

            RequestContent content = new RequestContent();
            content.extractValues(request);

            command.execute(content);

            Router router = (Router) content.findRequestAttribute(ROUTER_INSTANCE_NAME);

            if (router == null) {
                throw new RouteNotFoundException("Cannot find attribute[" + ROUTER_INSTANCE_NAME + "]. " + "Router is null.");
            }

            LOGGER.log(Level.DEBUG, "Content router: " + router);

            content.removeRequestAttribute(ROUTER_INSTANCE_NAME);
            content.insertValues(request);

            switch (router.getType()) {
                case REDIRECT:
                    response.sendRedirect(getServletContext().getContextPath() + router.getRoute());
                    break;
                case FORWARD:
                    request.getRequestDispatcher(router.getRoute()).forward(request, response);
                    break;
            }
        } catch (IllegalCommandTypeException | RouteNotFoundException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            response.sendError(404);
        }
    }

    /**
     * Init command by command name.
     *
     * @param request
     * @return command instance
     * @throws IllegalCommandTypeException
     */
    static AbstractCommand initCommand(HttpServletRequest request) throws IllegalCommandTypeException {
        String uri = request.getRequestURI();

        /* Generate the main part of command name */
        String commandName = (uri.length() == 1 && uri.startsWith("/"))
                ? INDEX_PAGE_COMMAND_VALUE :
                uri.substring(1, uri.length()).replace('/', '.').toLowerCase();

        /* Add second part called 'method' of the request */
        commandName += "::" + request.getMethod().toLowerCase();

        /* Set attribute to init command */
        request.setAttribute(AbstractCommand.COMMAND_INSTANCE_NAME, commandName);
        LOGGER.log(Level.DEBUG, AbstractCommand.COMMAND_INSTANCE_NAME + ": " + commandName);

        CommandType commandType = CommandType.findByTag(commandName);
        return commandType.getCommand();
    }
}
