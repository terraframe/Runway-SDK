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

/**
 * Class to hold information about the different connections to be used with
 * proxies.
 */
public class ConnectionLabel
{
  public enum Type {
    
    /**
     * The allowed value (in XML) to denote a Java ClientRequest
     */
    JAVA("Java"),

    /**
     * The allowed value (in XML) to denote an RMI ClientRequest
     */
    RMI("RMI"),

    /**
     * The allowed value (in XML) to denote a Web Service ClientRequest
     */
    WEB_SERVICE("WebService");

    private String type;

    private Type(String type)
    {
      this.type = type;
    }

    public String getType()
    {
      return type;
    }
    
    public static Type dereference(String type)
    {
      if(type.equals(JAVA.getType()))
        return JAVA;
      else if(type.equals(RMI.getType()))
        return RMI;
      else if(type.equals(WEB_SERVICE.getType()))
        return WEB_SERVICE;
      else
      {
        String error = "The type of clientRequest ["+type+"] is invalid";
        throw new ClientRequestException(error);
      }
    }
  }

  /**
   * The label of the clientRequest.
   */
  private String label;

  /**
   * The type of the connection.
   */
  private Type type;

  /**
   * The address of the connection. Note that this will be probably be
   * emtpy/null if the connection type is a Java connection.
   */
  private String address;

  /**
   * Constructor to set the label, type, and address of a connection.
   * 
   * @param type
   * @param address
   */
  public ConnectionLabel(String label, Type type, String address)
  {
    this.label = label;
    this.type = type;
    
    if(!address.endsWith("/"))
    {
      address = address + "/";
    }
    
    this.address = address;
  }
  
  public Type getType()
  {
    return type;
  }
  
  public String getAddress()
  {
    return address;
  }
  
  public String getLabel()
  {
    return label;
  }
}
