package com.epam.horsebetting.tag;

import com.epam.horsebetting.command.AbstractCommand;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.PAGE_FIELD;

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
     * Postfix of the request URI.
     */
    private String postfix;

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

    /**
     * Set postfix of the request URI.
     *
     * @param postfix
     */
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public int doStartTag() throws JspException {
        if(limit >= total) {
            return SKIP_BODY;
        }

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        Paginator paginator = new Paginator(limit, total);

        Object commandURIObj = request.getAttribute(AbstractCommand.COMMAND_URI_NAME);
        String commandURI = commandURIObj == null ? "" : String.valueOf(commandURIObj);

        paginator.setRequestURI(commandURI);

        if(postfix != null) {
            paginator.setPostfix(postfix);
        }

        String pageParam = request.getParameter(PAGE_FIELD);

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
