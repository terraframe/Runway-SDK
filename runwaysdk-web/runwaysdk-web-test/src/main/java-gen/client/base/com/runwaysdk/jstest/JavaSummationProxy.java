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

@com.runwaysdk.business.ClassSignature(hash = 1385297704)
public class JavaSummationProxy extends SummationProxy implements com.runwaysdk.generation.loader.
{
  
  private String label;
  private String address;
  
  private static final long serialVersionUID = 1385297704;
  
  public JavaSummationProxy(java.lang.String label, java.lang.String address)
  {
    this.label = label;
    this.address = address;
  }
  
  public java.lang.Integer[][] arrayInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.lang.Integer[][] array)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.lang.Integer[][] _array = array;
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("arrayInOut", java.lang.String.class,java.lang.Integer[][].class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    java.lang.Integer[][] ___output = null;
    try
    {
      ___output = (java.lang.Integer[][]) method.invoke(null, ___clientRequestIF.getSessionId(), _array);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer[][])me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public java.lang.Boolean compareStates(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state1, com.runwaysdk.jstest.StatesDTO state2)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state1 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state1);
    com.runwaysdk.business.EnumDTO _state2 = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state2);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("compareStates", java.lang.String.class,com.runwaysdk.business.EnumDTO.class,com.runwaysdk.business.EnumDTO.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    java.lang.Boolean ___output = null;
    try
    {
      ___output = (java.lang.Boolean) method.invoke(null, ___clientRequestIF.getSessionId(), _state1, _state2);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Boolean)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public com.runwaysdk.jstest.TestUtilDTO concatUtilChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestUtilDTO util)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _util = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(util);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("concatUtilChar", java.lang.String.class,com.runwaysdk.business.ComponentDTO.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    com.runwaysdk.business.ComponentDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.ComponentDTO) method.invoke(null, ___clientRequestIF.getSessionId(), _util);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.ComponentDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.TestUtilDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestUtilDTO", ___output);
  }
  
  public com.runwaysdk.jstest.TestViewDTO concatViewChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestViewDTO view)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO _view = (com.runwaysdk.business.ComponentDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(view);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("concatViewChar", java.lang.String.class,com.runwaysdk.business.ComponentDTO.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    com.runwaysdk.business.ComponentDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.ComponentDTO) method.invoke(null, ___clientRequestIF.getSessionId(), _view);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.ComponentDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.TestViewDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.TestViewDTO", ___output);
  }
  
  public java.util.Date dateInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.util.Date date)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    java.util.Date _date = date;
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("dateInOut", java.lang.String.class,java.util.Date.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    java.util.Date ___output = null;
    try
    {
      ___output = (java.util.Date) method.invoke(null, ___clientRequestIF.getSessionId(), _date);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.util.Date)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public void doNothing(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("doNothing", java.lang.String.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    try
    {
      method.invoke(null, ___clientRequestIF.getSessionId());
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
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
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("facadeForceException", java.lang.String.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    try
    {
      method.invoke(null, ___clientRequestIF.getSessionId());
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
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
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("getNullInteger", java.lang.String.class,com.runwaysdk.business.ComponentDTO.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    java.lang.Integer ___output = null;
    try
    {
      ___output = (java.lang.Integer) method.invoke(null, ___clientRequestIF.getSessionId(), _nullObj);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
  public com.runwaysdk.jstest.StatesDTO getState(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.EnumDTO _state = (com.runwaysdk.business.EnumDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(state);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("getState", java.lang.String.class,com.runwaysdk.business.EnumDTO.class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    com.runwaysdk.business.EnumDTO ___output = null;
    try
    {
      ___output = (com.runwaysdk.business.EnumDTO) method.invoke(null, ___clientRequestIF.getSessionId(), _state);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (com.runwaysdk.business.EnumDTO)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return (com.runwaysdk.jstest.StatesDTO) com.runwaysdk.transport.conversion.ConversionFacade.convertToTypeSafe(___clientRequestIF, "com.runwaysdk.jstest.StatesDTO", ___output);
  }
  
  public java.lang.Integer sumIntegerValues(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO[] testClassArr)
  {
    com.runwaysdk.ClientRequest.clearNotifications((com.runwaysdk.ClientRequest)___clientRequestIF);
    com.runwaysdk.business.ComponentDTO[] _testClassArr = (com.runwaysdk.business.ComponentDTO[]) com.runwaysdk.transport.conversion.ConversionFacade.convertGeneric(testClassArr);
    
    java.lang.Class clazz;
    java.lang.reflect.Method method;
    try
    {
      clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JavaSummationAdapter");
      method = clazz.getMethod("sumIntegerValues", java.lang.String.class,com.runwaysdk.business.ComponentDTO[].class);
    }
    catch (java.lang.Throwable e)
    {
      throw new com.runwaysdk.ClientProgrammingErrorException(e);
    }
    java.lang.Integer ___output = null;
    try
    {
      ___output = (java.lang.Integer) method.invoke(null, ___clientRequestIF.getSessionId(), _testClassArr);
    }
    catch(java.lang.Throwable e)
    {
      java.lang.RuntimeException rte = com.runwaysdk.transport.conversion.ClientConversionFacade.buildThrowable(e, ___clientRequestIF, false);
      if (rte instanceof com.runwaysdk.MessageExceptionDTO)
      {
        com.runwaysdk.MessageExceptionDTO me = (com.runwaysdk.MessageExceptionDTO)rte;
        ___output = (java.lang.Integer)me.getReturnObject();
        com.runwaysdk.ClientRequest.setMessages((com.runwaysdk.ClientRequest)___clientRequestIF, me);
      }
      else
      {
        throw rte;
      }
    }
    return ___output;
  }
  
}
