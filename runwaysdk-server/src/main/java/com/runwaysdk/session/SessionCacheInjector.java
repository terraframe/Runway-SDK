/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.session;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class SessionCacheInjector
{
  /**
   * Dependency injector runway
   */
  private static Injector injector = null;
  
  /**
   * @return Injector which will resolve dependencies
   */
  public synchronized static Injector getInjector()
  { 
    if(injector == null)
    {
      injector = Guice.createInjector(SessionCacheInjector.getSessionModule());
    }
    
    return injector;
  }
  
  /**
   * Finds the optional com.runwaysdk.session.SessionModule and loads its dependencies
   * into the injector.  If the optional module is not found then the default module is loaded into
   * the injector.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  private static Module getSessionModule()
  {
    Module defaultModule =  new Module()
    {
      public void configure(Binder binder)
      {
        SessionCache defaultCache = new BufferedSessionCache(
            new OverflowSessionCache(
                new MemorySessionCache(100, 20000, 100),
                new FileSessionCache( LocalProperties.getSessionCacheDirectory())),
            new MemorySessionCache());

        binder.bind(SessionCache.class).toInstance(defaultCache);  
      }
    };
    
    try
    {
      String type = Constants.ROOT_PACKAGE+".session.SessionModule";
      Class<Module> c = (Class<Module>) LoaderDecorator.load(type);
      
      return c.newInstance();
    }
    catch(Exception e)
    {
      return defaultModule;
    }
    
  }
  
  /**
   * Used for testing: Resets the injector to its original form
   */
  synchronized static void reloadInjector()
  {
    injector = Guice.createInjector(SessionCacheInjector.getSessionModule());
  }
  
  /**
   * Used for testing: Resets the injector with the specified modules
   * 
   * @param modules
   */
  synchronized static void createInjector(Module... modules)
  {
    injector = Guice.createInjector(modules);
  }  
}
