package com.epam.horsebetting.listener;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Locale;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    /**
     * Logger to write logs.
     */
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        Locale locale = new Locale("ru", "RU");
        event.getSession().setAttribute("locale", locale);
        LOGGER.log(Level.INFO, "Created locale by default: " + locale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {}
}
