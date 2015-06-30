/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.dataaccess.cache.globalcache.ehcache;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * A ServletContextListener that shuts down Ehcache.
 * <p/>
 * Add the following to web.xml in your web application:
 * 
 * <pre>
 * &lt;listener&gt;
 *      &lt;listener-class&gt;com.runwaysdk.dataaccess.cache.globalcache.ehcache.CacheShutdownListener&lt;/listener-class&gt;
 * &lt;/listener&gt;
 * <p/>
 * </pre>
 * 
 * @author Eric Grunzke
 */
public class CacheShutdownListener implements ServletContextListener
{
  /**
   * Notification that the web application is ready to process requests.
   * 
   * @param servletContextEvent
   */
  public void contextInitialized(ServletContextEvent servletContextEvent)
  {
    // nothing required
  }

  /**
   * Notification that the servlet context is about to be shut down.
   * <p/>
   * Calls {@link CacheShutdown#shutdown()}
   * 
   * @param servletContextEvent
   */
  public void contextDestroyed(ServletContextEvent servletContextEvent)
  {
    CacheShutdown.shutdown();
  }
}
