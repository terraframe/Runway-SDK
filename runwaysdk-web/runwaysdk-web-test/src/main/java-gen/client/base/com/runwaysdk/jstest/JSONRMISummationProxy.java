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

@com.runwaysdk.business.ClassSignature(hash = 2110567799)
public class JSONRMISummationProxy extends JSONSummationProxy implements com.runwaysdk.generation.loader.
{
  public static final String CLASS = "com.runwaysdk.jstest.JSONRMISummationProxy";
  
  private java.lang.String label;
  private java.lang.String address;
  private JSONSummationRemoteAdapter adapter;
  
  private static final long serialVersionUID = 2110567799;
  
  public JSONRMISummationProxy(java.lang.String label, java.lang.String address)
  {
    this.label = label;
    this.address = address;
    try
    {
      this.adapter = (JSONSummationRemoteAdapter) java.rmi.Naming.lookup(this.address + JSONSummationRemoteAdapter.SERVICE_NAME);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.RemoteException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.NotBoundException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
  }
  
  public void unbindRMIProxy()
  {
    try
    {
      java.rmi.Naming.unbind(address + JSONSummationRemoteAdapter.SERVICE_NAME);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.RemoteException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
    catch (java.rmi.NotBoundException e)
    {
      throw new com.runwaysdk.request.RMIClientException(e);
    }
  }
  
  public java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array)
  {
    try
    {
      return adapter.arrayInOut(sessionId, array);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2)
  {
    try
    {
      return adapter.compareStates(sessionId, state1, state2);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util)
  {
    try
    {
      return adapter.concatUtilChar(sessionId, util);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view)
  {
    try
    {
      return adapter.concatViewChar(sessionId, view);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date)
  {
    try
    {
      return adapter.dateInOut(sessionId, date);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String doNothing(java.lang.String sessionId)
  {
    try
    {
      return adapter.doNothing(sessionId);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String facadeForceException(java.lang.String sessionId)
  {
    try
    {
      return adapter.facadeForceException(sessionId);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj)
  {
    try
    {
      return adapter.getNullInteger(sessionId, nullObj);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String getState(java.lang.String sessionId, java.lang.String state)
  {
    try
    {
      return adapter.getState(sessionId, state);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr)
  {
    try
    {
      return adapter.sumIntegerValues(sessionId, testClassArr);
    }
    catch(java.lang.RuntimeException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
    catch(java.rmi.RemoteException e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
}
