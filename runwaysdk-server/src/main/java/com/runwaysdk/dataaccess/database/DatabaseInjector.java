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
package com.runwaysdk.dataaccess.database;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.dataaccess.database.general.AbstractDatabase;

/**
 * Google Guice implementation for dependency injection of the {@link Database}
 * 
 * @author Justin Smethie
 */
public class DatabaseInjector
{
  /**
   * Guice injector used for dependency injection
   */
  private Injector injector;
  
  /**
   * Creates a new DatabaseInjector.  The only binding in the injector
   * is between {@link AbstractDatabase} and the concrete implementation
   * specified in database.properties.
   */
  public DatabaseInjector()
  {
    this.injector = Guice.createInjector(this.getModule());
  }
  
  private final Module getModule()
  {
    return new Module()
    {
      public void configure(Binder binder)
      {
        binder.bind(AbstractDatabase.class).to(DatabaseProperties.getDatabaseClass());
      }
    };
  }
  
  /**
   * @return The a new instance of a @{link Database} with all of its dependencies
   * already injected.
   */
  public Database getDatabase()
  {
    return injector.getInstance(Database.class);
  }
}
