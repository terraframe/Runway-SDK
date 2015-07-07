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

@com.runwaysdk.business.ClassSignature(hash = 1574503449)
public abstract class JSONSummationProxy implements JSONSummationProxyIF, com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "com.runwaysdk.jstest.JSONSummationProxy";
  
  public static JSONSummationProxy getProxy()
  {
    return getProxy(com.runwaysdk.request.ConnectionLabelFacade.getConnection());
  }
  
  public static JSONSummationProxy getProxy(java.lang.String label)
  {
    return getProxy(com.runwaysdk.request.ConnectionLabelFacade.getConnection(label));
  }
  
  public static JSONSummationProxy getProxy(com.runwaysdk.request.ConnectionLabel connection)
  {
    if(com.runwaysdk.request.ConnectionLabel.Type.JAVA.equals(connection.getType()))
    {
      try
      {
        Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationProxy");
        Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());
        return (JSONSummationProxy) object;
      }
      catch (Exception e)
      {
        throw new com.runwaysdk.request.ClientRequestException(e);
      }
    }
    else if(com.runwaysdk.request.ConnectionLabel.Type.RMI.equals(connection.getType()))
    {
      return new JSONRMISummationProxy(connection.getLabel(), connection.getAddress());
    }
    else
    {
      return new JSONWebServiceSummationClientRequest(connection.getLabel(), connection.getAddress());
    }
  }
  
}
