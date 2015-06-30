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

@com.runwaysdk.business.ClassSignature(hash = -1586149706)
public interface JSONSummationRemoteAdapter extends java.rmi.Remote, com.runwaysdk.generation.loader.Reloadable
{
  public static final String SERVICE_NAME = "json:com.runwaysdk.jstest.JSONSummationRemoteAdapter";
  public java.lang.String arrayInOut(String sessionId, java.lang.String array) throws java.rmi.RemoteException;
  
  public java.lang.String compareStates(String sessionId, java.lang.String state1, java.lang.String state2) throws java.rmi.RemoteException;
  
  public java.lang.String concatUtilChar(String sessionId, java.lang.String util) throws java.rmi.RemoteException;
  
  public java.lang.String concatViewChar(String sessionId, java.lang.String view) throws java.rmi.RemoteException;
  
  public java.lang.String dateInOut(String sessionId, java.lang.String date) throws java.rmi.RemoteException;
  
  public java.lang.String doNothing(String sessionId) throws java.rmi.RemoteException;
  
  public java.lang.String facadeForceException(String sessionId) throws java.rmi.RemoteException;
  
  public java.lang.String getNullInteger(String sessionId, java.lang.String nullObj) throws java.rmi.RemoteException;
  
  public java.lang.String getState(String sessionId, java.lang.String state) throws java.rmi.RemoteException;
  
  public java.lang.String sumIntegerValues(String sessionId, java.lang.String testClassArr) throws java.rmi.RemoteException;
  
}
