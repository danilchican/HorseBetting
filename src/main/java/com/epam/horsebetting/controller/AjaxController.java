package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.request.RequestContent;
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
import java.util.ArrayList;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;

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

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<String> errors = new ArrayList<>();
        String json;

        RequestContent content = new RequestContent();

        try {
            AbstractCommand command = CommandInitializer.init(request);
            LOGGER.log(Level.DEBUG, "Initialized AJAX command: " + command.getClass().getName());

            content.extractValues(request);
            command.execute(content);
        } catch (CommandTypeNotFoundException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            errors.add("Route does not exist.");
            content.insertJsonAttribute(REQUEST_ERRORS, errors);
        }

        json = new Gson().toJson(content.getJsonData());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(json);
    }
}
