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

import com.runwaysdk.ClientSession;
import com.runwaysdk.TestSuiteTF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.request.MockWebServiceClientRequest;

import junit.extensions.TestSetup;
import junit.framework.Test;

public class MockWebServiceMessageTest extends MessageTest
{
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(MockWebServiceMessageTest.suite());
  }

  public static Test suite()
  {
    TestSuiteTF suite = new TestSuiteTF();
    suite.addTestSuite(MockWebServiceMessageTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        label = "default";
        systemSession =
          ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
        clientRequest = new MockWebServiceClientRequest(systemSession, systemSession.getSessionId());
        clientRequest.setKeepMessages(false);

        classSetUp();
        changeStubSource();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    ClientRequestIF clientRequestIF = new MockWebServiceClientRequest(clientSession, clientSession.getSessionId());
    clientRequestIF.setKeepMessages(false);

    return clientRequestIF;
  }
}
