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
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.io.XMLImporter;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class WebServiceViewDTOMethodTest extends InvokeViewDTOMethodTest
{
  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   *
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(WebServiceViewDTOMethodTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(InvokeViewDTOMethodTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        systemSession =
          ClientSession.createUserSession("facadeGenerationTest", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
        clientRequest = systemSession.getRequest();

        sessionTypeName = "SessionViewWS";
        sessionType = pack +"."+sessionTypeName;
        sessionDTOtype = sessionType + ComponentDTOGenerator.DTO_SUFFIX;

        bagTypeName = "BagViewWS";
        bagType  = pack +"."+bagTypeName;
        bagDTO = bagType + ComponentDTOGenerator.DTO_SUFFIX;

        stateTypeName = "AllStatesViewWS";
        stateType = pack + "."+stateTypeName;

        utilParamTypeName = "UtilParamViewWS";
        utilParamType = pack +"."+utilParamTypeName;
        utilParamDTO = utilParamType + ComponentDTOGenerator.DTO_SUFFIX;

        viewParamTypeName = "ViewParamViewWS";
        viewParamType = pack +"."+viewParamTypeName;
        viewParamDTO = viewParamType + ComponentDTOGenerator.DTO_SUFFIX;

        moreSetup();

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
}
