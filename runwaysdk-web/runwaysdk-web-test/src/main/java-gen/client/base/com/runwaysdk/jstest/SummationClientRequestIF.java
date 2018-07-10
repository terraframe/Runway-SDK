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

@com.runwaysdk.business.ClassSignature(hash = 728497518)
public interface SummationClientRequestIF extends com.runwaysdk.constants.ClientRequestMarker, com.runwaysdk.generation.loader.
{
  public static final String CLASS = "com.runwaysdk.jstest.Summation";
  public java.lang.Integer[][] arrayInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.lang.Integer[][] array);
  
  public java.lang.Boolean compareStates(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state1, com.runwaysdk.jstest.StatesDTO state2);
  
  public com.runwaysdk.jstest.TestUtilDTO concatUtilChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestUtilDTO util);
  
  public com.runwaysdk.jstest.TestViewDTO concatViewChar(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestViewDTO view);
  
  public java.util.Date dateInOut(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, java.util.Date date);
  
  public void doNothing(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF);
  
  public void facadeForceException(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF);
  
  public java.lang.Integer getNullInteger(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO nullObj);
  
  public com.runwaysdk.jstest.StatesDTO getState(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.StatesDTO state);
  
  public java.lang.Integer sumIntegerValues(com.runwaysdk.constants.ClientRequestIF ___clientRequestIF, com.runwaysdk.jstest.TestClassDTO[] testClassArr);
  
}
