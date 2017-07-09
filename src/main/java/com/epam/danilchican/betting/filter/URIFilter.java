package com.epam.danilchican.betting.filter;

import com.epam.danilchican.betting.util.parser.URIParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "URIFilter", servletNames = {"MainController"}, urlPatterns = {"/*"})
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
        HttpServletResponse res = (HttpServletResponse) resp;

        URIParser parser = new URIParser();

        String uri = String.valueOf(req.getAttribute("uriAddress"));
        LOGGER.log(Level.DEBUG, "URI: " + uri);

        if(uri.matches(URI_REGEX) || uri.startsWith("/assets")) {
            ArrayList<String> uriChunks = parser.parseURI(uri);

            req.setAttribute("uriChunks", uriChunks);

            LOGGER.log(Level.DEBUG, "URIFilter has worked.");
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "404 error page.");
            res.sendRedirect("/error");
        }
    }

    @Override
    public void destroy() {
    }
}
