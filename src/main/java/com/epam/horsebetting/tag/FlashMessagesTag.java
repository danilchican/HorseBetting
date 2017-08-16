package com.epam.horsebetting.tag;

import com.epam.horsebetting.type.FlashMessageType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_MESSAGES;

@SuppressWarnings("serial")
public class FlashMessagesTag extends TagSupport {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Session messages.
     */
    private ArrayList<String> messages;

    /**
     * Attribute name of displaying content.
     */
    private FlashMessageType type;

    /**
     * Set session messages.
     *
     * @param messages
     */
    public void setMessages(ArrayList<String> messages) {
        this.messages = messages;
    }

    /**
     * Set attribute name of type.
     *
     * @param type
     */
    public void setType(FlashMessageType type) {
        this.type = type;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        HttpSession session = request.getSession();

        LOGGER.log(Level.DEBUG, "Tag messages[type=" + type + "]:");
        LOGGER.log(Level.DEBUG, Arrays.toString(messages.toArray()));

        try {
            StringBuilder outerHtml = new StringBuilder();
            outerHtml.append("<div class=\"alert ").append(type.getBlockClassName());
            outerHtml.append("\">");

            if(messages.size() > 1) {
                outerHtml.append("<ul>");

                for (String message : messages) {
                    outerHtml.append("<li>");
                    outerHtml.append(message);
                    outerHtml.append("</li>");
                }

                outerHtml.append("</ul>");
            } else {
                for (String message : messages) {
                    outerHtml.append(message);
                }
            }

            outerHtml.append("</div>");

            switch(type) {
                case ERROR:
                    session.removeAttribute(REQUEST_ERRORS);
                    break;
                case MESSAGE:
                    session.removeAttribute(REQUEST_MESSAGES);
                    break;
            }

            JspWriter out = pageContext.getOut();
            out.write(outerHtml.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
