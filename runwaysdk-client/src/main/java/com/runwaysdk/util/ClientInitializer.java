/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.util;

import com.runwaysdk.CommonExceptionProcessor;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ExceptionConstants;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class ClientInitializer implements ClientInitializerIF
{
  public static synchronized void init()
  {
    String[] expansionModules = CommonProperties.getClientExpansionModules();

    for (String expansionModule : expansionModules)
    {
      Class<?> clientInitializerClass = LoaderDecorator.load(expansionModule.trim());

      // Create a new instance of a collection
      try
      {
        clientInitializerClass.getMethod("init").invoke(null);
      }
      catch (Throwable e)
      {
        String errMsg = "Falid to initialize module ["+expansionModule+"]";
        CommonExceptionProcessor.processException(ExceptionConstants.ProgrammingErrorException.getExceptionClass(), errMsg, e);
      }
    }
  }

}
