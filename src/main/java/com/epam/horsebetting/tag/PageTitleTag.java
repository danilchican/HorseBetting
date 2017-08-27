package com.epam.horsebetting.tag;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class PageTitleTag extends TagSupport {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Title of the page.
     */
    private String title;

    /**
     * Subtitle of the page if exists.
     */
    private String subTitle;

    /**
     * Set the title of the page.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the subTitle of the page.
     *
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @Override
    public int doStartTag() throws JspException {
        final String DELIMITER = " | ";
        LOGGER.log(Level.DEBUG, this.getClass().getName() + "[title: " + title + ", subTitle: " + subTitle + "]");
        
        try{
            StringBuilder resultTitle = new StringBuilder();
            resultTitle.append(title);

            JspWriter out = pageContext.getOut();

            if(!subTitle.isEmpty()) {
                resultTitle.append(DELIMITER).append(subTitle);
            }

            out.write(resultTitle.toString());
        } catch(IOException e) {
            throw new JspException(e.getMessage());
        }

        return SKIP_BODY;
    }
    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
