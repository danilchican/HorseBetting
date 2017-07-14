package com.epam.danilchican.betting.controller;

import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.command.AbstractCommand;
import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.exception.RouteNotFoundException;
import com.epam.danilchican.betting.request.RequestContent;
import com.epam.danilchican.betting.util.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.danilchican.betting.command.AbstractCommand.COMMAND_INSTANCE_NAME;
import static com.epam.danilchican.betting.util.Router.ROUTER_INSTANCE_NAME;

@WebServlet(name = "MainController", urlPatterns = "/")
public class MainController extends HttpServlet {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Index page command value.
     */
    private static final String INDEX_PAGE_COMMAND_VALUE = "index";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            AbstractCommand command = initCommand(request);

            RequestContent content = new RequestContent();
            content.extractValues(request);

            command.execute(content);
            navigateToPage(content, request, response);
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
    private AbstractCommand initCommand(HttpServletRequest request) throws IllegalCommandTypeException {
        String uri = request.getRequestURI();

        String commandName = (uri.length() == 1 && uri.startsWith("/"))
                ? INDEX_PAGE_COMMAND_VALUE :
                uri.substring(1, uri.length()).replace('/', '.').toLowerCase();

        request.setAttribute(COMMAND_INSTANCE_NAME, commandName);
        LOGGER.log(Level.DEBUG, COMMAND_INSTANCE_NAME + ": " + commandName);

        CommandType commandType = CommandType.findByTag(commandName);
        return commandType.getCommand();
    }

    /**
     * Navigate to page by Router instance.
     *
     * @param content
     * @param request
     * @param response
     * @throws RouteNotFoundException
     * @throws IOException
     * @throws ServletException
     */
    private void navigateToPage(RequestContent content, HttpServletRequest request, HttpServletResponse response)
            throws RouteNotFoundException, IOException, ServletException {
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
    }
}
