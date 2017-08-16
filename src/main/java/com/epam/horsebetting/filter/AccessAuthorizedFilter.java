package com.epam.horsebetting.filter;

import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;

public class AccessAuthorizedFilter implements Filter {

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

        HashMap<String, String> errors = new HashMap<>();

        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(SESSION_AUTHORIZED);

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");

        if (userObj != null) {
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "User not authenticated. Redirected to login.");

            if(request.getRequestURI().startsWith("/ajax")) {
                errors.put(REQUEST_ERRORS, "User not authenticated.");
                String json = new Gson().toJson(errors);

                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(json);
            } else {
                response.sendRedirect("/auth/login");
            }
        }
    }

    @Override
    public void destroy() {
    }
}
