package com.epam.danilchican.betting.tag;

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

@SuppressWarnings("serial")
public class SessionErrorsTag extends TagSupport {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Session errors storage.
     */
    private ArrayList<String> errors;

    /**
     * Http session instance.
     */
    private HttpSession session;

    /**
     * Set session errors.
     *
     * @param errors
     */
    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        this.session = request.getSession();
        LOGGER.log(Level.DEBUG, "Session received!");

        LOGGER.log(Level.DEBUG, "Tag Errors:");
        LOGGER.log(Level.DEBUG, Arrays.toString(errors.toArray()));

        try {
            StringBuilder outerHtml = new StringBuilder();
            outerHtml.append("<div class=\"row\">");

            for (String error : errors) {
                outerHtml.append("<p>");
                outerHtml.append(error);
                outerHtml.append("</p>");
            }

            outerHtml.append("</div>");
            this.session.removeAttribute("errors");

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
