package com.epam.horsebetting.tag;

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
        HttpSession session = request.getSession();

        LOGGER.log(Level.DEBUG, "Tag Errors:");
        LOGGER.log(Level.DEBUG, Arrays.toString(errors.toArray()));

        try {
            StringBuilder outerHtml = new StringBuilder();
            outerHtml.append("<h3>Errors:</h3>");
            outerHtml.append("<div class=\"ul\">");

            for (String error : errors) {
                outerHtml.append("<li>");
                outerHtml.append(error);
                outerHtml.append("</li>");
            }

            outerHtml.append("</ul><br/>");
            session.removeAttribute("errors");

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
