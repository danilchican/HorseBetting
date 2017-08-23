package com.epam.horsebetting.receiver;

import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.config.EnvironmentConfig;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;

public abstract class AbstractReceiver {

    /**
     * Page sub title.
     */
    private String pageSubTitle;

    /**
     * Execute receiver action.
     *
     * @param type
     * @param content
     */
    public final void action(CommandType type, RequestContent content) throws ReceiverException {
        type.doReceiver(content);
    }

    /**
     * Set default content attributes.
     *
     * @param content
     */
    protected void setDefaultContentAttributes(RequestContent content) {
        EnvironmentConfig env = new EnvironmentConfig();
        content.insertRequestAttribute("pageTitle", env.obtainAppName());
        content.insertRequestAttribute("pageSubTitle", pageSubTitle);
    }

    /**
     * Set page sub title.
     *
     * @param pageSubTitle
     */
    public void setPageSubTitle(String pageSubTitle) {
        this.pageSubTitle = pageSubTitle;
    }
}
