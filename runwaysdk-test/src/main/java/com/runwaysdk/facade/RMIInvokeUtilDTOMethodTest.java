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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.Util;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.request.RMIClientRequest;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class RMIInvokeUtilDTOMethodTest extends InvokeUtilDTOMethodTest
{
  @BeforeClass
  @Request
  public static void classSetUp()
  {
    TestRMIUtil.startServer();
    systemSession = ClientSession.createUserSession("rmiDefault", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequest = systemSession.getRequest();

    mdSessionDTO = MdUtilDAO.newInstance();
    bag = MdUtilDAO.newInstance();

    superClassField = MdUtilInfo.SUPER_MD_UTIL;
    getterMethodImplementation = "    return (" + sessionTypeName + ") " + Util.class.getName() + ".get(oid);";
    
    modelSetup();
  }

  @AfterClass
  @Request  
  public static void classTearDown()
  {
    modelTearDown();

    systemSession.logout();

    ( (RMIClientRequest) clientRequest ).unbindRMIClientRequest();
    RemoteAdapterServer.stopServer();
  }
}
