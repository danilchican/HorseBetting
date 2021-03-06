package com.epam.horsebetting.filter;

import com.epam.horsebetting.config.PageConfig;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;

public class AccessGuestFilter implements Filter {

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

        HttpSession session = request.getSession();
        Object userObj = session.getAttribute(SESSION_AUTHORIZED);
        String page;

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");

        if (userObj == null) {
            chain.doFilter(req, resp);
        } else {
            LOGGER.log(Level.DEBUG, "User already authenticated. Redirected to profile.");
            page = PageConfig.getInstance().takeAddress(PageConfig.Page.PROFILE_INDEX);
            response.sendRedirect(page);
        }
    }

    @Override
    public void destroy() {
    }
}
