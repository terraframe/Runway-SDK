/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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
package com.runwaysdk.facade;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.controller.URLConfigurationManagerTest;
import com.runwaysdk.mvc.DelegatingServletTest;
import com.runwaysdk.mvc.DispatcherServletTest;
import com.runwaysdk.mvc.RestBodyResponseTest;
import com.runwaysdk.mvc.RestResponseTest;
import com.runwaysdk.mvc.ViewResponseTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  IndicatorAdapterTest.class, 
  AdapterTest.class, 
  NoSourceAdapterTest.class, 
  ViewDTOAdapterTest.class, 
  UtilDTOAdapterTest.class, 
  InvokeMethodTest.class, 
  InvokeUtilDTOMethodTest.class, 
  InvokeViewDTOMethodTest.class, 
  MessageTest.class, 
  RMIAdapterTest.class, 
  RMIViewDTOAdapterTest.class, 
  RMIUtilDTOAdapterTest.class, 
  RMIInvokeMethodTest.class, 
  RMIInvokeUtilDTOMethodTest.class, 
  RMIInvokeViewDTOMethodTest.class, 
  RMIMessageTest.class, 
  JSONInvokeMethodTest.class, 
  JSONRMIInvokeMethodTest.class, 
  URLConfigurationManagerTest.class, 
  RestBodyResponseTest.class, 
  RestResponseTest.class, 
  ViewResponseTest.class, 
  DispatcherServletTest.class, 
  DelegatingServletTest.class, 
 })
public class FacadeTestSuite
{
}
