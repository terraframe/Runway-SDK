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

@com.runwaysdk.business.ClassSignature(hash = -1785262308)
public interface JSONSummationProxyIF extends com.runwaysdk.constants.ClientRequestMarker, com.runwaysdk.generation.loader.
{
  public java.lang.String arrayInOut(java.lang.String sessionId, java.lang.String array);
  
  public java.lang.String compareStates(java.lang.String sessionId, java.lang.String state1, java.lang.String state2);
  
  public java.lang.String concatUtilChar(java.lang.String sessionId, java.lang.String util);
  
  public java.lang.String concatViewChar(java.lang.String sessionId, java.lang.String view);
  
  public java.lang.String dateInOut(java.lang.String sessionId, java.lang.String date);
  
  public java.lang.String doNothing(java.lang.String sessionId);
  
  public java.lang.String facadeForceException(java.lang.String sessionId);
  
  public java.lang.String getNullInteger(java.lang.String sessionId, java.lang.String nullObj);
  
  public java.lang.String getState(java.lang.String sessionId, java.lang.String state);
  
  public java.lang.String sumIntegerValues(java.lang.String sessionId, java.lang.String testClassArr);
  
}
