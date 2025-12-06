package it.unisa.nexware.application.listeners;

import it.unisa.nexware.storage.utils.DriverManagerConnectionPool;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DriverManagerConnectionPool.shutdown();
    }

}