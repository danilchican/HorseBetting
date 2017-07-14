package com.epam.danilchican.betting.receiver;

import com.epam.danilchican.betting.command.CommandType;
import com.epam.danilchican.betting.request.RequestContent;

public abstract class AbstractReceiver {

    final protected String pageTitle = "HorseBetting.com";

    protected String pageSubTitle;

    /**
     * Execute receiver action.
     *
     * @param type
     * @param content
     */
    public final void action(CommandType type, RequestContent content) {
        type.doReceiver(content);
        setDefaultContentAttributes(content);
    }

    /**
     * Set default content attributes.
     *
     * @param content
     */
    private void setDefaultContentAttributes(RequestContent content) {
        content.insertRequestAttribute("pageTitle", pageTitle);
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
