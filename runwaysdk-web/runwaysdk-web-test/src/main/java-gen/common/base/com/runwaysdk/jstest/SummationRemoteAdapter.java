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

@com.runwaysdk.business.ClassSignature(hash = 851809795)
public interface SummationRemoteAdapter extends java.rmi.Remote, com.runwaysdk.generation.loader.
{
  public static final String SERVICE_NAME = "java:com.runwaysdk.jstest.SummationRemoteAdapter";
  public java.lang.Integer[][] arrayInOut(String sessionId, java.lang.Integer[][] array) throws java.rmi.RemoteException;
  
  public java.lang.Boolean compareStates(String sessionId, com.runwaysdk.business.EnumDTO state1, com.runwaysdk.business.EnumDTO state2) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.ComponentDTO concatUtilChar(String sessionId, com.runwaysdk.business.ComponentDTO util) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.ComponentDTO concatViewChar(String sessionId, com.runwaysdk.business.ComponentDTO view) throws java.rmi.RemoteException;
  
  public java.util.Date dateInOut(String sessionId, java.util.Date date) throws java.rmi.RemoteException;
  
  public void doNothing(String sessionId) throws java.rmi.RemoteException;
  
  public void facadeForceException(String sessionId) throws java.rmi.RemoteException;
  
  public java.lang.Integer getNullInteger(String sessionId, com.runwaysdk.business.ComponentDTO nullObj) throws java.rmi.RemoteException;
  
  public com.runwaysdk.business.EnumDTO getState(String sessionId, com.runwaysdk.business.EnumDTO state) throws java.rmi.RemoteException;
  
  public java.lang.Integer sumIntegerValues(String sessionId, com.runwaysdk.business.ComponentDTO[] testClassArr) throws java.rmi.RemoteException;
  
}
