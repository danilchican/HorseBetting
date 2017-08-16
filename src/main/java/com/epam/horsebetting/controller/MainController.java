package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.exception.RouteNotFoundException;
import com.epam.horsebetting.router.Router;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.horsebetting.router.Router.ROUTER_INSTANCE_NAME;

@WebServlet(name = "MainController", urlPatterns = "/")
public class MainController extends HttpServlet {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            AbstractCommand command = CommandInitializer.init(request);
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
        } catch (CommandTypeNotFoundException | RouteNotFoundException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
