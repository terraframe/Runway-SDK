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
package com.runwaysdk.facade;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.dataaccess.ProgrammingErrorException;
import com.runwaysdk.web.json.JSONRMIAdapter;

public class RemoteAdapterServer
{
  private static RemoteAdapterServer instance = null;

  private int                        port;

  private Registry                   registry;

  private RemoteAdapterServer()
  {
    this(new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
  }

  /**
   * Constructs a new server.
   *
   */
  private RemoteAdapterServer(RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
  {
    // get the server info
    String rmiAdapterService = CommonProperties.getRMIService();
    String jsonRMIAdapterService = CommonProperties.getJSONRMIService();

    this.port = CommonProperties.getRMIPort();

    try
    {
      this.registry = LocateRegistry.createRegistry(port);

      this.publish(rmiAdapterService, new RMIAdapter(0, csf, ssf));
      this.publish(jsonRMIAdapterService, new JSONRMIAdapter(0, csf, ssf));
    }
    catch (RemoteException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  private void publish(String name, Remote service) throws RemoteException, AccessException
  {
    try
    {
      registry.rebind(name, service);
    }
    catch (ExportException e)
    {
      UnicastRemoteObject.unexportObject(service, true);

      registry.rebind(name, service);
    }
  }

  /**
   * Starts the server
   *
   */
  public static synchronized void startServer(RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
  {
    if (instance == null)
    {
      instance = new RemoteAdapterServer(csf, ssf);
    }
  }

  /**
   * Starts the server
   *
   */
  public static synchronized void startServer()
  {
    if (instance == null)
    {
      instance = new RemoteAdapterServer();
    }
  }

  /**
   * Stops the server
   *
   */
  public static synchronized void stopServer()
  {
    try
    {
      if (instance != null)
      {
        UnicastRemoteObject.unexportObject(instance.registry, true);
        instance = null;
      }
    }
    catch (RemoteException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  /**
   * Binds a new adapter to the rmi registry
   * 
   * @param adapter
   * @param adapterName
   */
  public static synchronized void bindNewAdapter(Remote adapter, String adapterName)
  {
    try
    {
      if (instance != null)
      {
        instance.registry.rebind(adapterName, adapter);
      }
    }
    catch (AccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (RemoteException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static Remote lookup(String name)
  {
    try
    {
      return instance.registry.lookup(name);
    }
    catch (AccessException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (RemoteException e)
    {
      throw new ProgrammingErrorException(e);
    }
    catch (NotBoundException e)
    {
      throw new ProgrammingErrorException(e);
    }
  }

  public static synchronized void printServiceNames()
  {
    if (instance != null)
    {
      try
      {
        for (String name : instance.registry.list())
        {
          System.out.println(name);
        }
      }
      catch (AccessException e)
      {
        throw new ProgrammingErrorException(e);
      }
      catch (RemoteException e)
      {
        throw new ProgrammingErrorException(e);
      }
    }
  }

  /**
   * Starts the RMI Server through a normal java run.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    startServer();
  }
}
