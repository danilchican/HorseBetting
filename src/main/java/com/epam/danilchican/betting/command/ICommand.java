package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.request.RequestHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Base interface for all commands.
 *
 * @author Vladislav Danilchik <danilchican@mail.ru>
 */
public interface ICommand {

    /**
     * Execute command with request.
     *
     * @param request
     * @see HttpServletRequest
     * @see RequestHandler
     */
    RequestHandler execute(HttpServletRequest request);
}
