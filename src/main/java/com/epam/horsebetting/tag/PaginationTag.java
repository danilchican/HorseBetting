package com.epam.horsebetting.tag;

import com.epam.horsebetting.util.Paginator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@SuppressWarnings("serial")
public class PaginationTag extends TagSupport {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Total count of entities.
     */
    private int total;

    /**
     * Limit to show entities on page.
     */
    private int limit;

    /**
     * Set total count of all entities.
     *
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Set limit to show entities on page.
     *
     * @param limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Paginator paginator = new Paginator(limit, total);

        String pageParam = request.getParameter("page");

        try {
            if(pageParam != null) {
                int page = Integer.parseInt(pageParam);
                paginator.setPage(page);
            }

            JspWriter out = pageContext.getOut();
            out.write(paginator.generate());
        } catch (NumberFormatException e) {
            String message = "Parse GET[page] : " + e.getMessage();
            LOGGER.log(Level.ERROR, message);
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
