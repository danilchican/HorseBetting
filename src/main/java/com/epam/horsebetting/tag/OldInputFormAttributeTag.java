package com.epam.horsebetting.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class OldInputFormAttributeTag extends TagSupport {

    /**
     * Session attribute name.
     */
    private String name;

    /**
     * Old input prefix.
     */
    public static final String PREFIX = "_old_";

    /**
     * Set session attribute name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = PREFIX + name;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        HttpSession session = request.getSession();

        try {
            Object attributeObj = session.getAttribute(name);
            String attributeValue = "";

            if(attributeObj != null) {
                attributeValue = String.valueOf(attributeObj);
                session.removeAttribute(name);
            }

            JspWriter out = pageContext.getOut();
            out.write(attributeValue);
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
