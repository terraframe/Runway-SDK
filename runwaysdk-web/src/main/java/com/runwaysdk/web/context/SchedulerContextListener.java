package com.runwaysdk.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.runwaysdk.system.scheduler.SchedulerManager;

public class SchedulerContextListener implements ServletContextListener
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
