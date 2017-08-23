package com.epam.horsebetting.filter;

import com.epam.horsebetting.config.EnvironmentConfig;
import com.epam.horsebetting.dao.impl.UserDAOImpl;
import com.epam.horsebetting.exception.DAOException;
import com.epam.horsebetting.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static com.epam.horsebetting.config.EnvironmentConfig.DEFAULT_DELIMITER;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.COOKIE_REMEMBER_TOKEN;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.REQUEST_USER;
import static com.epam.horsebetting.config.RequestFieldConfig.Common.SESSION_AUTHORIZED;
import static com.epam.horsebetting.util.DateFormatter.SECONDS_PER_HOUR;

public class AuthUserFilter implements Filter {

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

        Cookie[] cookies = request.getCookies();
        Optional<Cookie> rememberToken = Arrays.stream(cookies)
                .filter(cookie -> COOKIE_REMEMBER_TOKEN.equals(cookie.getName())).findFirst();

        if (rememberToken.isPresent()) {
            EnvironmentConfig env = new EnvironmentConfig();

            try {
                int age = Integer.parseInt(env.obtainRememberTokenExpTime());
                age *= SECONDS_PER_HOUR;

                Cookie rememberCookie = new Cookie(COOKIE_REMEMBER_TOKEN, rememberToken.get().getValue());
                rememberCookie.setMaxAge(age);
                rememberCookie.setPath(DEFAULT_DELIMITER);
                response.addCookie(rememberCookie);
                LOGGER.log(Level.DEBUG, "Expiration time of remember token has been updated.");
            } catch (NumberFormatException e) {
                LOGGER.log(Level.ERROR, "Cannot parse remember token expiration time: " + e.getMessage());
            }
        }

        if (userObj != null) {
            try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                int userId = Integer.parseInt(String.valueOf(userObj));

                User user = userDAO.find(userId);
                request.setAttribute(REQUEST_USER, user);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.ERROR, "Cannot parse user id: " + e.getMessage());
            } catch (DAOException e) {
                throw new ServletException("Cannot retrieve data about authorized user.", e);
            }
        } else {
            if (rememberToken.isPresent()) {
                try (UserDAOImpl userDAO = new UserDAOImpl(false)) {
                    String token = rememberToken.get().getValue();
                    User user = userDAO.findByRememberToken(token);

                    if (user != null) {
                        session.setAttribute(SESSION_AUTHORIZED, user.getId());
                        request.setAttribute(REQUEST_USER, user);
                        LOGGER.log(Level.INFO, "User authorized via remember token.");
                    }
                } catch (DAOException e) {
                    throw new ServletException("Cannot retrieve data about authorized user. " + e.getMessage(), e);
                }
            }
        }

        LOGGER.log(Level.DEBUG, this.getClass().getName() + " has worked.");
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {
    }
}
