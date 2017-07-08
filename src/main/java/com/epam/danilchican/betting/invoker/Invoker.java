package com.epam.danilchican.betting.invoker;

import com.epam.danilchican.betting.command.ICommand;

import javax.servlet.http.HttpServletRequest;

public class Invoker {

    /**
     * Command interface to execute command.
     */
    private ICommand command;

    /**
     * Set command by constructor param.
     *
     * @param command
     */
    public Invoker(ICommand command) {
        this.command = command;
    }

    /**
     * Invoke command execution.
     *
     * @param request
     * @see HttpServletRequest
     */
    public void invoke(HttpServletRequest request) {
        command.execute(request);
    }
}
