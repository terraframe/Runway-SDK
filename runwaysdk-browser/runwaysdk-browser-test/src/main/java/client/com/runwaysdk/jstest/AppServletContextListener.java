package com.runwaysdk.jstest;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.runwaysdk.system.scheduler.SchedulerManager;

public class AppServletContextListener implements ServletContextListener
{
  @Override
  public void contextInitialized(ServletContextEvent arg0) {
    SchedulerManager.start();
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent arg0) {
    SchedulerManager.shutdown();
  }
}
