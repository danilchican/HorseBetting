package com.epam.danilchican.betting.command;

import javax.servlet.http.HttpServletRequest;

public interface ICommand {

    /**
     * Execute command with request.
     *
     * @param request
     * @see HttpServletRequest
     */
    void execute(HttpServletRequest request);
}
