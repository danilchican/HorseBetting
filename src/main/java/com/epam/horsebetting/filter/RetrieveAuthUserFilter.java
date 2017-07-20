package com.epam.horsebetting.filter;

import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.dao.UserDAO;
import com.epam.horsebetting.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "RetrieveAuthUserFilter", urlPatterns = "/*")
public class RetrieveAuthUserFilter implements Filter {

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

        LOGGER.log(Level.DEBUG, "Started getting authorized user info.");

        Object userObj = session.getAttribute("authorized");

        if (userObj != null) {
            int userId = Integer.parseInt(String.valueOf(userObj));
            User user = null;

            try (UserDAO userDAO = new UserDAO()) {
                user = userDAO.find(userId);
            } catch (DAOException e) {
                throw new ServletException("Cannot retrieve data about authorized user.", e);
            }

            session.setAttribute("user", user);
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
