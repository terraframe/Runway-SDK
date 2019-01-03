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

@com.runwaysdk.business.ClassSignature(hash = -1415008764)
public class JSONWebServiceSummationClientRequest extends JSONSummationProxy implements com.runwaysdk.generation.loader.
{
  
  private java.lang.String label;
  private java.lang.String address;
  
  private static final long serialVersionUID = -1415008764;
  
  public JSONWebServiceSummationClientRequest(String label, String address)
  {
    this.label = label;
    this.address = address;
  }
  
  private org.apache.axis.client.Call newCall()
  {
    try
    {
      org.apache.axis.client.Service service = new org.apache.axis.client.Service();
      org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
      call.setTargetEndpointAddress(new java.net.URL(this.address + "com.runwaysdk.jstest.WebServiceSummationAdapter"));
      return call;
    }
    catch (javax.xml.rpc.ServiceException e)
    {
      throw new com.runwaysdk.request.WebServiceClientRequestException(e);
    }
    catch (java.net.MalformedURLException e)
    {
      throw new com.runwaysdk.request.WebServiceClientRequestException(e);
    }
  }
  
  public java.lang.String arrayInOut(String sessionId, java.lang.String array)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, array};
       return (java.lang.String) ___call.invoke("arrayInOut", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String compareStates(String sessionId, java.lang.String state1, java.lang.String state2)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, state1, state2};
       return (java.lang.String) ___call.invoke("compareStates", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String concatUtilChar(String sessionId, java.lang.String util)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, util};
       return (java.lang.String) ___call.invoke("concatUtilChar", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String concatViewChar(String sessionId, java.lang.String view)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, view};
       return (java.lang.String) ___call.invoke("concatViewChar", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String dateInOut(String sessionId, java.lang.String date)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, date};
       return (java.lang.String) ___call.invoke("dateInOut", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String doNothing(String sessionId)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId};
       return (java.lang.String) ___call.invoke("doNothing", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String facadeForceException(String sessionId)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId};
       return (java.lang.String) ___call.invoke("facadeForceException", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String getNullInteger(String sessionId, java.lang.String nullObj)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, nullObj};
       return (java.lang.String) ___call.invoke("getNullInteger", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String getState(String sessionId, java.lang.String state)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, state};
       return (java.lang.String) ___call.invoke("getState", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
  public java.lang.String sumIntegerValues(String sessionId, java.lang.String testClassArr)
  {
    try
    {
      org.apache.axis.client.Call ___call = newCall();
      Object[] ___params = {sessionId, testClassArr};
       return (java.lang.String) ___call.invoke("sumIntegerValues", ___params);
    }
    catch(java.lang.Exception e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, true);
    }
  }
  
}
