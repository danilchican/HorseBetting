package com.epam.horsebetting.listener;

import com.epam.horsebetting.database.ConnectionPool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextListenerImpl implements ServletContextListener {

    /**
     * Servlet context.
     */
    private ServletContext context;

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        context = ev.getServletContext();

        ConnectionPool.getInstance();
        context.log("Context: Initialized with connection pool.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        ConnectionPool.getInstance().destroy();
        context.log("Context: Connection pool destroyed.");
    }
}
