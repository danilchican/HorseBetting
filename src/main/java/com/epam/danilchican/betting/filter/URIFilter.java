package com.epam.danilchican.betting.filter;

import com.epam.danilchican.betting.util.parser.URIParser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter(filterName = "URIFilter", servletNames = {"MainController"}, urlPatterns = {"/*"})
public class URIFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String uri = String.valueOf(req.getAttribute("uriAddress"));
        URIParser parser = new URIParser();
        ArrayList<String> uriChunks = parser.parseURI(uri);

        req.setAttribute("uriChunks", uriChunks);

        LOGGER.log(Level.DEBUG, "URIFilter has worked.");
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
