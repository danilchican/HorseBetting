package com.epam.danilchican.betting.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "URIFilter", urlPatterns = "/*")
public class URIFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

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
        String urlQuery = String.valueOf(req.getAttribute("urlQuery"));

        if(urlQuery.startsWith("/assets")) {
            chain.doFilter(req, resp);
        } else if (urlQuery.matches(URI_REGEX)) {
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
