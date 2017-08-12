package com.epam.horsebetting.receiver;

import com.epam.horsebetting.command.CommandType;
import com.epam.horsebetting.exception.ReceiverException;
import com.epam.horsebetting.request.RequestContent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractReceiver {

    /**
     * Page title.
     */
    private static final String PAGE_TITLE = "HorseBetting.com";

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
        content.insertRequestAttribute("pageTitle", PAGE_TITLE);
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
