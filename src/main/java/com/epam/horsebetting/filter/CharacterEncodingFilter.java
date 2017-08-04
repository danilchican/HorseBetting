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
    private static final String characterEncoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String currentEncoding = request.getCharacterEncoding();

        if (!characterEncoding.equalsIgnoreCase(currentEncoding)) {
            request.setCharacterEncoding(characterEncoding);
            response.setCharacterEncoding(characterEncoding);
        }

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
