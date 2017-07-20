package com.epam.horsebetting.receiver;

import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractReceiver {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Page title.
     */
    private final String pageTitle = "HorseBetting.com";

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
    void setDefaultContentAttributes(RequestContent content) {
        content.insertRequestAttribute("pageTitle", pageTitle);
        content.insertRequestAttribute("pageSubTitle", pageSubTitle);
        LOGGER.log(Level.INFO, "Default content attribute were set!");
    }

    /**
     * Set page sub title.
     *
     * @param pageSubTitle
     */
    void setPageSubTitle(String pageSubTitle) {
        this.pageSubTitle = pageSubTitle;
    }
}
