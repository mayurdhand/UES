package com.db.riskit.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.db.riskit.scheduler.re.ConfigBookingExpieryMailScheduler;
import com.db.riskit.scheduler.rs.RSBookingExpieryMailScheduler;
import com.db.riskit.utils.logging.Logger;

public class ContextListener implements ServletContextListener {
    /** The servlet context with which we are associated. */
    private ServletContext context = null;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log("Context destroyed");
        Logger.log(this.getClass().getName(), Logger.INFO, "Context destroyed");
        ConfigBookingExpieryMailScheduler.getInstance().stop();
        RSBookingExpieryMailScheduler.getInstance().stop();
        this.context = null;
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
        log("Context initialized");
        Logger.log(this.getClass().getName(), Logger.INFO, "Context initialized");
        try{
        	ConfigBookingExpieryMailScheduler.getInstance().start();
        }catch (Exception e) {
			e.printStackTrace();
		}
        try{
        	RSBookingExpieryMailScheduler.getInstance().start();
        }catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void log(String message) {
        if (context != null) {
            context.log("MyServletContextListener: " + message);
        } else {
            System.out.println("MyServletContextListener: " + message);
        }
    }
}