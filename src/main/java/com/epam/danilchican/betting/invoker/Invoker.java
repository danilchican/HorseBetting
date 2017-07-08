package com.epam.danilchican.betting.invoker;

import com.epam.danilchican.betting.command.ICommand;
import com.epam.danilchican.betting.request.RequestContent;

import javax.servlet.http.HttpServletRequest;

/**
 * The Invoker for all commands.
 *
 * @author Vladislav Danilchik <danilchican@mail.ru>
 */
public class Invoker {

    /**
     * Command interface to execute command.
     */
    private ICommand command;

    /**
     * Constructor.
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
     * @return requestHandler
     * @see RequestContent
     */
    public RequestContent invoke(HttpServletRequest request) {
        RequestContent requestContent = new RequestContent();
        requestContent.extractValues(request);

        return command.execute(requestContent);
    }
}
