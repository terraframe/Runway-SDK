/**
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
 */
package com.runwaysdk.request;

import java.lang.reflect.Method;

import com.runwaysdk.constants.RequestManagerInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;

/**
 * A convience class which bridges RequestMangager and ClientRequest
 * methods between the server and the client through the use
 * of reflection. This is done for compilation reasons.
 * These methods just delegate to existing methods written
 * for the client.
 *  
 * @author Justin
 *
 */
public class ConnectionLabelFacade
{  
 
  /**
   * Returns the default Connection
   * @return
   */
  public static ConnectionLabel getConnection()
  {
    try
    {
      Class<?> c = LoaderDecorator.load(RequestManagerInfo.CLASS);
      Method method = c.getMethod("getDefaultConnection");
      ConnectionLabel connection = (ConnectionLabel) method.invoke(null);
      
      return connection;
    }
    catch (Exception e)
    {
      throw new ClientRequestException(e);
    }
  }
  
  /**
   * Returns the Connection mapped to the label
   * 
   * @param label The label of the connection
   * @return
   */
  public static ConnectionLabel getConnection(String label)
  {
    try
    {
      Class<?> c = LoaderDecorator.load(RequestManagerInfo.CLASS);
      Method method = c.getMethod("getConnection", String.class);
      ConnectionLabel connection = (ConnectionLabel) method.invoke(null, label);
      
      return connection;
    }
    catch (Exception e)
    {
      throw new ClientRequestException(e);
    }
  }
}
