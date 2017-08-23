package com.epam.horsebetting.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RouteFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * URI regular expression.
     */
    private static final String URI_REGEX = "((\\/[\\w\\-]*)+)(\\/)?(\\?[\\w\\-]+\\=[\\w\\-]*)?(\\&[\\w\\-]+\\=[\\w\\-]*)*$";

    /**
     * Constants.
     */
    private static final String ASSETS_ROOT = "/assets";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String queryString = request.getQueryString();
        String urlQuery = request.getRequestURI() + ((queryString != null) ? ("?" + queryString) : "");

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");

        if (urlQuery.startsWith(ASSETS_ROOT)) {
            chain.doFilter(req, resp);
        } else if (urlQuery.matches(URI_REGEX)) {
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.ERROR, "Page not found! " + urlQuery);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {
    }
}
