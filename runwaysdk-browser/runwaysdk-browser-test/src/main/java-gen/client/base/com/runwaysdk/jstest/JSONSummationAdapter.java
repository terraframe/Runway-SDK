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
package com.runwaysdk.jstest;

@com.runwaysdk.business.ClassSignature(hash = 1662871713)
public class JSONSummationAdapter implements com.runwaysdk.generation.loader.Reloadable
{
  private static JSONSummationProxyIF proxy = JSONSummationProxy.getProxy();
  public static java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array)
  {
    return proxy.arrayInOut(sessionId, array);
  }
  
  public static java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2)
  {
    return proxy.compareStates(sessionId, state1, state2);
  }
  
  public static java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util)
  {
    return proxy.concatUtilChar(sessionId, util);
  }
  
  public static java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view)
  {
    return proxy.concatViewChar(sessionId, view);
  }
  
  public static java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date)
  {
    return proxy.dateInOut(sessionId, date);
  }
  
  public static java.lang.String doNothing(java.lang.String sessionId)
  {
    return proxy.doNothing(sessionId);
  }
  
  public static java.lang.String facadeForceException(java.lang.String sessionId)
  {
    return proxy.facadeForceException(sessionId);
  }
  
  public static java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj)
  {
    return proxy.getNullInteger(sessionId, nullObj);
  }
  
  public static java.lang.String getState(java.lang.String sessionId, java.lang.String state)
  {
    return proxy.getState(sessionId, state);
  }
  
  public static java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr)
  {
    return proxy.sumIntegerValues(sessionId, testClassArr);
  }
  
}
