package com.epam.danilchican.betting.controller;

import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.command.ICommand;
import com.epam.danilchican.betting.exception.IllegalCommandTypeException;
import com.epam.danilchican.betting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String uri = request.getRequestURI();

        String commandValue = (uri.length() == 1 && uri.startsWith("/"))
                ? INDEX_PAGE_COMMAND_VALUE :
                uri.substring(1, uri.length()).replace('/', '.').toLowerCase();

        request.setAttribute("command", commandValue);
        LOGGER.log(Level.DEBUG, "Command: " + commandValue);

        try {
            CommandType commandType = CommandType.findByTag(commandValue);
            ICommand command = commandType.getCurrentCommand();

            RequestContent requestContent = new RequestContent();
            requestContent.extractValues(request);

            requestContent = command.execute(requestContent);
            request.getRequestDispatcher("/jsp/welcome.jsp").forward(request, response);
        } catch (IllegalCommandTypeException e) {
            LOGGER.log(Level.ERROR, "Command[" + commandValue + "] not found!");
            response.sendError(404);
        }
    }
}
