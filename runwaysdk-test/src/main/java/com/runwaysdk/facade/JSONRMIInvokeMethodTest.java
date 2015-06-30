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
package com.runwaysdk.facade;

import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.request.RMIClientRequest;
import com.runwaysdk.web.json.JSONRMIClientRequest;

public class JSONRMIInvokeMethodTest extends JSONInvokeMethodTest
{
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(JSONRMIInvokeMethodTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(JSONRMIInvokeMethodTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        RemoteAdapterServer.startServer();

        jsonProxy = new JSONRMIClientRequest("default", "//localhost:" + CommonProperties.getRMIPort() + "/");

        systemSession = ClientSession.createUserSession("rmiDefault", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          clientRequest = systemSession.getRequest();
          classSetUp();

          noPermissionSession = ClientSession.createUserSession("rmiDefault", "smethie", "aaa", new Locale[] { CommonProperties.getDefaultLocale() });
          noPermissionRequest = noPermissionSession.getRequest();
          finalizeSetup();
        }
        catch (Exception e)
        {
          systemSession.logout();
        }
      }

      protected void tearDown()
      {
        classTearDown();
        ( (RMIClientRequest) clientRequest ).unbindRMIClientRequest();
        RemoteAdapterServer.stopServer();
      }
    };

    return wrapper;
  }
}
