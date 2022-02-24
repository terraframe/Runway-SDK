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

import com.runwaysdk.constants.CommonProperties;

public class ServerInitializerFacade
{
  private static ServerInitializerIF instance = null;

  public static synchronized void init()
  {
    instance().init();
  }

  public static synchronized void rebuild()
  {
    instance().rebuild();
  }

  private static synchronized ServerInitializerIF instance()
  {
    if (instance == null)
    {
      if (CommonProperties.getServerModulesLoader() != null)
      {
        instance = new ServerInitializer();
      }
      else
      {
        instance = new StaticServerInitializer();
      }
    }

    return instance;
  }

}
