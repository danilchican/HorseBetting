package com.epam.horsebetting.controller;

import com.epam.horsebetting.command.AbstractCommand;
import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.IllegalCommandTypeException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

class CommandInitializer {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Index page command value.
     */
    private static final String INDEX_PAGE_COMMAND_VALUE = "index::get";

    /**
     * Init command by command name.
     *
     * @param request
     * @return command instance
     * @throws IllegalCommandTypeException
     */
    static AbstractCommand init(HttpServletRequest request) throws IllegalCommandTypeException {
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
