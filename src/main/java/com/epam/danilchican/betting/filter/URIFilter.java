package com.epam.danilchican.betting.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "URIFilter", servletNames = {"MainController"}, urlPatterns = {"/*"})
public class URIFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Index page command value.
     */
    private static final String INDEX_PAGE_COMMAND_VALUE = "index.page";

    /**
     * URI regular expression.
     */
    private static final String URI_REGEX = "((\\/[\\w\\-]*)+)(\\/)?(\\?[a-zA-Z\\d]+\\=[\\w\\-]*)?(\\&[a-zA-z\\d]+\\=[\\w\\-]*)?$";

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpServletRequest request = (HttpServletRequest) req;

        String urlQuery = String.valueOf(req.getAttribute("urlQuery"));

        if (urlQuery.matches(URI_REGEX)) {
            String uri = request.getRequestURI();

            uri = (uri.length() == 1 && uri.startsWith("/"))
                    ? INDEX_PAGE_COMMAND_VALUE :
                    uri.substring(1, uri.length()).replace('/', '.').toLowerCase();

            LOGGER.log(Level.DEBUG, "urlQuery: " + urlQuery);
            LOGGER.log(Level.DEBUG, "Command uri: " + uri);

            req.setAttribute("command", uri);

            LOGGER.log(Level.DEBUG, "URIFilter has worked.");
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "404 error page.");
            response.sendError(404);
        }
    }

    @Override
    public void destroy() {
    }
}
