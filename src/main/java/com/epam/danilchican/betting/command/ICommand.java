package com.epam.danilchican.betting.command;

import com.epam.danilchican.betting.request.RequestContent;

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
     * @see RequestContent
     */
    RequestContent execute(RequestContent request);
}
