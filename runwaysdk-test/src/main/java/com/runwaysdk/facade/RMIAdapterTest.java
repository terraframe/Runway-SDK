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

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.request.RMIClientRequest;

@RunWith(ClasspathTestRunner.class)
public class RMIAdapterTest extends AbstractAdapterTest
{
  @BeforeClass
  public static void classSetUp()
  {
    TestRMIUtil.startServer();
    
    systemSession = ClientSession.createUserSession("rmiDefault", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();
  }

  @AfterClass
  public static void classTearDown()
  {
    systemSession.logout();

    ( (RMIClientRequest) systemSession.getRequest() ).unbindRMIClientRequest();
    RemoteAdapterServer.stopServer();
  }

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession("rmiDefault", new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession("rmiDefault", userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }
}
