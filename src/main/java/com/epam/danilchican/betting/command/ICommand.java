package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.request.RequestContent;

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
     * @see RequestContent
     */
    RequestContent execute(RequestContent request);
}
