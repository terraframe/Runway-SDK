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

@com.runwaysdk.business.ClassSignature(hash = -1531135838)
public class JSONJavaSummationProxy extends JSONSummationProxy implements com.runwaysdk.generation.loader.Reloadable
{
  public static final String CLASS = "com.runwaysdk.jstest.JSONJavaSummationProxy";
  
  private java.lang.String label;
  private java.lang.String address;
  
  private static final long serialVersionUID = -1531135838;
  
  public JSONJavaSummationProxy(java.lang.String label, java.lang.String address)
  {
    this.label = label;
    this.address = address;
  }
  
  public java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("arrayInOut", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, array);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("compareStates", java.lang.String.class,java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, state1, state2);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("concatUtilChar", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, util);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("concatViewChar", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, view);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("dateInOut", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, date);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String doNothing(java.lang.String sessionId)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("doNothing", java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String facadeForceException(java.lang.String sessionId)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("facadeForceException", java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("getNullInteger", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, nullObj);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String getState(java.lang.String sessionId, java.lang.String state)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("getState", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, state);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
  public java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr)
  {
    try
    {
      Class clazz = com.runwaysdk.generation.loader.LoaderDecorator.load("com.runwaysdk.jstest.JSONJavaSummationAdapter");
      java.lang.reflect.Method method = clazz.getMethod("sumIntegerValues", java.lang.String.class,java.lang.String.class);
      return (java.lang.String)method.invoke(null, sessionId, testClassArr);
    }
    catch (java.lang.Throwable e)
    {
      throw com.runwaysdk.transport.conversion.ClientConversionFacade.buildJSONThrowable(e, sessionId, false);
    }
  }
  
}
