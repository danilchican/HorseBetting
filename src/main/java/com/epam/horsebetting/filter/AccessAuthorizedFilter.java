package com.epam.horsebetting.filter;

import com.epam.horsebetting.config.MessageConfig;
import com.google.gson.Gson;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_ERRORS;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_LOCALE;
import static com.epam.horsebetting.controller.AjaxController.CONTENT_TYPE;
import static com.epam.horsebetting.filter.CharacterEncodingFilter.CHARACTER_ENCODING;

public class AccessAuthorizedFilter implements Filter {

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

        Locale locale = (Locale)request.getSession().getAttribute(SESSION_LOCALE);
        MessageConfig messageResource = new MessageConfig(locale);

        HashMap<String, String> errors = new HashMap<>();

        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(SESSION_AUTHORIZED);

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");

        if (userObj != null) {
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "User not authenticated. Redirected to login.");

            if(request.getRequestURI().startsWith("/ajax")) {
                errors.put(REQUEST_ERRORS, messageResource.get("user.not_auth"));
                String json = new Gson().toJson(errors);

                response.setCharacterEncoding(CHARACTER_ENCODING);
                response.setContentType(CONTENT_TYPE);
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
