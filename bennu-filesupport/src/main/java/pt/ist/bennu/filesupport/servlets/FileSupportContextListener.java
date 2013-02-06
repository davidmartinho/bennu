package pt.ist.bennu.filesupport.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import pt.ist.bennu.filesupport.FileDeleterThread;
import pt.ist.bennu.filesupport.domain.FileSupport;
import pt.ist.bennu.service.Service;

@WebListener
public class FileSupportContextListener implements ServletContextListener {
    private static boolean initialized = false;

    @Service
    public void initialize() {
        if (!initialized) {
            FileSupport.getInstance();
            Thread thread = new Thread(new FileDeleterThread());
            thread.start();
            initialized = true;
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        initialize();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
}
