package com.epam.danilchican.betting.command;

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
     */
    void execute(HttpServletRequest request);
}
