package com.epam.horsebetting.filter;

import com.epam.horsebetting.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_USER;

public class AccessAdministratorFilter implements Filter {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        Object userObj = request.getAttribute(REQUEST_USER);

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");

        if (userObj != null) {
            User user = (User)userObj;

            if(user.isAdministrator()) {
                chain.doFilter(req, resp);
            } else {
                LOGGER.log(Level.DEBUG, "User haven't permissions. Redirected to profile.");
                response.sendRedirect("/profile");
            }
        } else {
            LOGGER.log(Level.DEBUG, "User not authenticated. Redirected to login.");
            response.sendRedirect("/auth/login");
        }
    }

    @Override
    public void destroy() {
    }
}
