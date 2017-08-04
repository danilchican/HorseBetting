package com.epam.horsebetting.listener;

import com.epam.horsebetting.database.ConnectionPool;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServletContextListenerImpl implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ConnectionPool.getInstance();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ConnectionPool.getInstance().destroy();
    }
}
