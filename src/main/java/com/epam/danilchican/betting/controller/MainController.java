package com.epam.danilchican.betting.controller;

import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.command.ICommand;
import com.epam.danilchican.betting.exception.CommandTypeNotFoundException;
import com.epam.danilchican.betting.invoker.Invoker;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandValue = String.valueOf(request.getAttribute("command"));

        try {
            CommandType commandType = CommandType.findByTag(commandValue);
            ICommand command = commandType.getCurrentCommand();

            Invoker invoker = new Invoker(command);
            RequestContent wrapReqContent = invoker.invoke(request);

            request.getRequestDispatcher("/jsp/welcome.jsp").forward(request, response);
        } catch (CommandTypeNotFoundException e) {
            LOGGER.log(Level.ERROR, "Command[" + commandValue + "] not found!");
            response.sendError(404);
        }
    }
}
