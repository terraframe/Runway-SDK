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
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.request.MockWebServiceClientRequest;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MockWebServiceAdapterTest extends AdapterTest
{

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(MockWebServiceAdapterTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MockWebServiceAdapterTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        systemSession =
          ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
        clientRequest = new MockWebServiceClientRequest(systemSession, systemSession.getSessionId());
        classSetUp();
        finalizeSetup();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession("default", new Locale[]{CommonProperties.getDefaultLocale()});
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession("default", userName, password, new Locale[]{CommonProperties.getDefaultLocale()});
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return new MockWebServiceClientRequest(clientSession, clientSession.getSessionId());
  }
}
