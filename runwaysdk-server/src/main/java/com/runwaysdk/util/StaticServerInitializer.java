/**
 * 
 */
package com.runwaysdk.util;

import com.runwaysdk.business.generation.LoaderDecoratorException;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.generation.loader.LoaderDecorator;

/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
 * 
 * This file is part of Runway SDK(tm).
 * 
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
public class StaticServerInitializer implements ServerInitializerIF
{
  public synchronized void init()
  {
    String[] expansionModules = CommonProperties.getServerExpansionModules();

    for (String expansionModule : expansionModules)
    {
      Class<?> serverInitializerClass = LoaderDecorator.load(expansionModule.trim());

      try
      {
        serverInitializerClass.getMethod("init").invoke(null);
      }
      catch (Throwable e)
      {
        String errorMessage = "Failed to initialize module [" + expansionModule + "]";
        throw new ProgrammingErrorException(errorMessage, e);
      }
    }

    // FIXME: This is dumb and breaks client/server separation. This
    // initialization should be done somewhere client-side.
    try
    {
      Class<?> serverInitializerClass = LoaderDecorator.load(ServerInitializer.class.getPackage().getName() + ".ClientInitializer");

      try
      {
        serverInitializerClass.getMethod("init").invoke(null);
      }
      catch (Throwable e)
      {
        String errorMessage = "Failed to execute ClientInitializer.";
        throw new ProgrammingErrorException(errorMessage, e);
      }
    }
    catch (LoaderDecoratorException e)
    {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.runwaysdk.util.ServerInitializerIF#rebuild()
   */
  @Override
  public void rebuild()
  {
    // Do nothing
  }
}
