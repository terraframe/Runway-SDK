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

@com.runwaysdk.business.ClassSignature(hash = -2023437043)
public class WebServiceSummationProxy extends SummationProxy implements com.runwaysdk.generation.loader.Reloadable
{
  
  private java.lang.String label;
  private java.lang.String address;
  
  private static final long serialVersionUID = -2023437043;
  
  public WebServiceSummationProxy(String label, String address)
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
  
  public java.lang.Integer[][] arrayInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.lang.Integer[][] array)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.lang.Integer[][] _array = array;
    org.w3c.dom.Document __array = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_array, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __array};
    java.lang.Integer[][] ___primitiveObject = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("arrayInOut", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___primitiveObject = (java.lang.Integer[][])me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___primitiveObject == null)
    {
      return (java.lang.Integer[][])com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    else
    {
      return ___primitiveObject;
    }
  }
  
  public java.lang.Boolean compareStates(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state1, com.runwaysdk.jstest.StatesDTO state2)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state1 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state1);
    org.w3c.dom.Document __state1 = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_state1, false);
    
    com.runwaysdk.business.EnumDTO _state2 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state2);
    org.w3c.dom.Document __state2 = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_state2, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __state1, __state2};
    java.lang.Boolean ___primitiveObject = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("compareStates", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___primitiveObject = (java.lang.Boolean)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___primitiveObject == null)
    {
      return (java.lang.Boolean)com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    else
    {
      return ___primitiveObject;
    }
  }
  
  public com.runwaysdk.jstest.TestUtilDTO concatUtilChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestUtilDTO util)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _util = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(util);
    org.w3c.dom.Document __util = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_util, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __util};
    Object ___generic = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("concatUtilChar", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___generic = me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___generic == null)
    {
      ___generic = com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    return (com.runwaysdk.jstest.TestUtilDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestUtilDTO", ___generic);
  }
  
  public com.runwaysdk.jstest.TestViewDTO concatViewChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestViewDTO view)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _view = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(view);
    org.w3c.dom.Document __view = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_view, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __view};
    Object ___generic = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("concatViewChar", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___generic = me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___generic == null)
    {
      ___generic = com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    return (com.runwaysdk.jstest.TestViewDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestViewDTO", ___generic);
  }
  
  public java.util.Date dateInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.util.Date date)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.util.Date _date = date;
    org.w3c.dom.Document __date = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_date, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __date};
    java.util.Date ___primitiveObject = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("dateInOut", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___primitiveObject = (java.util.Date)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___primitiveObject == null)
    {
      return (java.util.Date)com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    else
    {
      return ___primitiveObject;
    }
  }
  
  public void doNothing(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId()};
    try
    {
      ___call.invoke("doNothing", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
  }
  
  public void facadeForceException(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId()};
    try
    {
      ___call.invoke("facadeForceException", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
  }
  
  public java.lang.Integer getNullInteger(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO nullObj)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _nullObj = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(nullObj);
    org.w3c.dom.Document __nullObj = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_nullObj, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __nullObj};
    java.lang.Integer ___primitiveObject = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("getNullInteger", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___primitiveObject = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___primitiveObject == null)
    {
      return (java.lang.Integer)com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    else
    {
      return ___primitiveObject;
    }
  }
  
  public com.runwaysdk.jstest.StatesDTO getState(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state);
    org.w3c.dom.Document __state = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_state, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __state};
    Object ___generic = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("getState", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___generic = me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___generic == null)
    {
      ___generic = com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    return (com.runwaysdk.jstest.StatesDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.StatesDTO", ___generic);
  }
  
  public java.lang.Integer sumIntegerValues(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO[] testClassArr)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO[] _testClassArr = (com.runwaysdk.business.ComponentDTO[]) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(testClassArr);
    org.w3c.dom.Document __testClassArr = com.runwaysdk.transport.conversion.ConversionFacade.getDocumentFromObject(_testClassArr, false);
    
    org.apache.axis.client.Call ___call = newCall();
    Object[] ___params = {___clientRequestIF.getSessionId(), __testClassArr};
    java.lang.Integer ___primitiveObject = null;
    org.w3c.dom.Document ___output = null;
    try
    {
      ___output = (org.w3c.dom.Document) ___call.invoke("sumIntegerValues", ___params);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, true);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___primitiveObject = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    if (___primitiveObject == null)
    {
      return (java.lang.Integer)com.runwaysdk.transport.conversion.ConversionFacade.getObjectFromDocument(___clientRequestIF, ___output);
    }
    else
    {
      return ___primitiveObject;
    }
  }
  
}
