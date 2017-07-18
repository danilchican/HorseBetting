package com.epam.horsebetting.filter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AccessAuthPageFilter", urlPatterns = "/profile/*")
public class AccessAuthPageFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();

        LOGGER.log(Level.DEBUG, "Started getting authorized user info. " + this.getClass().getName());
        Object userObj = session.getAttribute("authorized");

        if (userObj != null) {
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "User not authenticated. Redirected to login.");
            response.sendRedirect("/auth/login");
        }
    }

    @Override
    public void destroy() {
    }
}
