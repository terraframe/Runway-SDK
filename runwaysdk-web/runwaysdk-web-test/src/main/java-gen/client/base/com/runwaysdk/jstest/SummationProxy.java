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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 307846891)
public abstract class SummationProxy implements SummationClientRequestIF, com.runwaysdk.generation.loader.
{
  public static SummationProxy getClientRequest()
  {
    return getClientRequest(com.runwaysdk.request.ConnectionLabelFacade.getConnection());
  }
  
  public static SummationProxy getClientRequest(java.lang.String label)
  {
    return getClientRequest(com.runwaysdk.request.ConnectionLabelFacade.getConnection(label));
  }
  
  public static SummationProxy getClientRequest(com.runwaysdk.request.ConnectionLabel connection)
  {
    if(com.runwaysdk.request.ConnectionLabel.Type.JAVA.equals(connection.getType()))
    {
      try
      {
        Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationProxy");
        Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());
        return (SummationProxy) object;
      }
      catch (Exception e)
      {
        throw new com.runwaysdk.request.ClientRequestException(e);
      }
    }
    else if(com.runwaysdk.request.ConnectionLabel.Type.RMI.equals(connection.getType()))
    {
      return new RMISummationProxy(connection.getLabel(), connection.getAddress());
    }
    else
    {
      return new WebServiceSummationProxy(connection.getLabel(), connection.getAddress());
    }
  }
  
}
