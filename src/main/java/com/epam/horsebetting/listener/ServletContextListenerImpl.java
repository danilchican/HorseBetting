package com.epam.horsebetting.listener;

import com.epam.horsebetting.database.ConnectionPool;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListenerImpl implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext context = ev.getServletContext();

        ConnectionPool.getInstance();
        context.log("Context: Initialized with connection pool.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
        ServletContext context = ev.getServletContext();

        ConnectionPool.getInstance().destroy();
        context.log("Context: Connection pool destroyed.");
    }
}
