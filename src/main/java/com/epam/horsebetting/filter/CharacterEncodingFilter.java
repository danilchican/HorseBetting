package com.epam.horsebetting.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class CharacterEncodingFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Default character encoding.
     */
    public static final String CHARACTER_ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String currentEncoding = request.getCharacterEncoding();

        if (!CHARACTER_ENCODING.equalsIgnoreCase(currentEncoding)) {
            request.setCharacterEncoding(CHARACTER_ENCODING);
            response.setCharacterEncoding(CHARACTER_ENCODING);
        }

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
