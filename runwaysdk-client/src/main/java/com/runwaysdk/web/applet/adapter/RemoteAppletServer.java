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
package com.runwaysdk.web.applet.adapter;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

import com.runwaysdk.ClientException;
import com.runwaysdk.constants.ClientProperties;

public class RemoteAppletServer
{
  /**
   * Applet RMI registery on the application server
   */
  private Registry                  registry       = null;

  /**
   * Applet port number for the RMI registery
   */
  private int                       port;

  /**
   * Singleton instance
   */
  private static RemoteAppletServer instance       = null;

  /**
   * Constructs a new server.
   * 
   */
  private RemoteAppletServer()
  {
    port = ClientProperties.getRMIAppletPort();
    
    try
    {
      try
      {
        registry = LocateRegistry.createRegistry(port);
      }
      catch (ExportException e)
      {
        //A RMI registry is already running in the jvm
        registry = LocateRegistry.getRegistry(port);        
      }
      
      Remote remoteReference = new RMIAppletAdapter();
      
      // get each name to bind from the name list
      
      registry.rebind(ClientProperties.getRMIAppletName() , remoteReference);
    }
    catch (RemoteException e)
    {
      throw new ClientException(e);
    }
  }

  /**
   * Starts the server
   * 
   */
  private static synchronized RemoteAppletServer instance()
  {
    if (instance == null)
    {
      instance = new RemoteAppletServer();
    }
    
    return instance;
  }
  
  public static synchronized void startServer()
  {
    RemoteAppletServer.instance();
  }

  /**
   * Stops the server
   * 
   */
  public static synchronized void stopServer()
  {
    try
    {
      UnicastRemoteObject.unexportObject(instance().registry, true);
      instance = null;
    }
    catch (RemoteException e)
    {
      throw new ClientException(e);
    }
  }
}
