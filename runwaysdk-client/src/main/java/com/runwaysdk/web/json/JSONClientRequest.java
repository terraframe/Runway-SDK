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
package com.runwaysdk.web.json;


import com.runwaysdk.constants.JSONClientRequestIF;
import com.runwaysdk.constants.JSONJavaRequestInfo;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.request.ClientRequestException;
import com.runwaysdk.request.ClientRequestManager;
import com.runwaysdk.request.ConnectionLabel;

public abstract class JSONClientRequest implements JSONClientRequestIF
{
  
  /**
   * 
   */
  private static final long serialVersionUID = -4596851363541335529L;

  /**
   * The label of the clientRequest.
   */
  private String label;
  
  /**
   * The address of the clientRequest
   */
  private String address;
  
  /**
   * Constructor
   *
   */
  public JSONClientRequest(String label, String address)
  {
    this.label = label;
    if(!address.endsWith("/"))
      address = address + "/";
    
    this.address = address;
  }
  
  protected String getLabel()
  {
    return label;
  }
  
  protected String getAddress()
  {
    return address;
  }
  
  /**
   * Returns the default clientRequest
   * 
   * @return
   */
  public static JSONClientRequestIF getDefaultRequest()
  {
    ConnectionLabel connection = ClientRequestManager.getDefaultConnection();
    
    return getRequest(connection);
  }
  
  public static JSONClientRequestIF getRequest(String label)
  {
    ConnectionLabel connection = ClientRequestManager.getConnection(label);
    
    return getRequest(connection);
  }
  
  public static JSONClientRequestIF getRequest(ConnectionLabel connection)
  {
    JSONClientRequestIF jsonClientRequestIF = null;
    
    ConnectionLabel.Type type = connection.getType();
    if(type.equals(ConnectionLabel.Type.JAVA))
    {
      // we must use reflection here so that the runwaysdk client jar can
      // be compiled without the JSONJavaClientRequest -> JSONJavaController dependency.
      try
      {
        Class<?> clazz = LoaderDecorator.load(JSONJavaRequestInfo.CLASS);
        Object object = clazz.getConstructor(String.class, String.class).newInstance(connection.getLabel(), connection.getAddress());
        return (JSONClientRequestIF) object;
      }
      catch (Exception e)
      {
        throw new ClientRequestException(e);
      }
    }
    else if(type.equals(ConnectionLabel.Type.RMI))
    {
      return new JSONRMIClientRequest(connection.getLabel(), connection.getAddress());
    }
    
    return jsonClientRequestIF;
  }

}
