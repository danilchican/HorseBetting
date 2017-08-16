package com.epam.horsebetting.filter;

import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;

public class AuthUserFilter implements Filter {

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
        HttpSession session = request.getSession();

        Object userObj = session.getAttribute(SESSION_AUTHORIZED);

        if (userObj != null) {
            int userId = Integer.parseInt(String.valueOf(userObj));

            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                User user = userDAO.find(userId);
                request.setAttribute("user", user);
            } catch (DAOException e) {
                throw new ServletException("Cannot retrieve data about authorized user.", e);
            }
        }

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
