package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.config.MessageConfig;
import com.epam.horsebetting.exception.CommandTypeNotFoundException;
import com.epam.horsebetting.request.RequestContent;
import com.epam.horsebetting.util.MessageWrapper;
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
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;
import static com.epam.horsebetting.filter.CharacterEncodingFilter.CHARACTER_ENCODING;

@WebServlet(name = "AjaxController", urlPatterns = "/ajax/*")
public class AjaxController extends HttpServlet {

    /**
     * Constants.
     */
    public static final String CONTENT_TYPE = "application/json";

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

    /**
     * Process GET and POST request.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MessageWrapper errors = new MessageWrapper();
        Locale locale = (Locale)request.getSession().getAttribute(SESSION_LOCALE);

        MessageConfig messageResource = new MessageConfig(locale);
        RequestContent content = new RequestContent();

        try {
            AbstractCommand command = CommandInitializer.init(request);
            LOGGER.log(Level.DEBUG, "Initialized AJAX command: " + command.getClass().getName());

            content.extractValues(request);
            command.execute(content);
        } catch (CommandTypeNotFoundException e) {
            LOGGER.log(Level.ERROR, e.getMessage(), e);

            errors.add(messageResource.get("route.not_found"));
            content.insertJsonAttribute(REQUEST_ERRORS, errors.findAll());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        String json = new Gson().toJson(content.getJsonData());

        response.setCharacterEncoding(CHARACTER_ENCODING);
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(json);
    }
}
