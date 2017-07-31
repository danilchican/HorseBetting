package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import com.epam.horsebetting.exception.RouteNotFoundException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.Router;
import com.google.gson.Gson;
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

@WebServlet(name = "AjaxController", urlPatterns = "/ajax/*")
public class AjaxController extends HttpServlet {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json;

        try {
            AbstractCommand command = MainController.initCommand(request);
            LOGGER.log(Level.DEBUG, "Initialized command: " + command.getClass().getName());

            RequestContent content = new RequestContent();
            content.extractValues(request);

            command.execute(content);
            json = new Gson().toJson(content.getJsonData());
        } catch (IllegalCommandTypeException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);
            json = new Gson().toJson("Something went wrong...");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
