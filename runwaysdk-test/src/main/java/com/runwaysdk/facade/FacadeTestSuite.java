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

import junit.framework.Test;
import junit.framework.TestSuite;

public class FacadeTestSuite extends TestSuite
{

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(FacadeTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(AdapterTest.suite());
    suite.addTest(ViewDTOAdapterTest.suite());
    suite.addTest(UtilDTOAdapterTest.suite());
    suite.addTest(InvokeMethodTest.suite());
    suite.addTest(InvokeUtilDTOMethodTest.suite());
    suite.addTest(InvokeViewDTOMethodTest.suite());
    suite.addTest(FacadeGenerationTest.suite());
    suite.addTest(MessageTest.suite());

    suite.addTest(RMIAdapterTest.suite());
    suite.addTest(RMIViewDTOAdapterTest.suite());
    suite.addTest(RMIUtilDTOAdapterTest.suite());
    suite.addTest(RMIInvokeMethodTest.suite());
    suite.addTest(RMIInvokeUtilDTOMethodTest.suite());
    suite.addTest(RMIInvokeViewDTOMethodTest.suite());
    suite.addTest(RMIFacadeGenerationTest.suite());
    suite.addTest(RMIMessageTest.suite());

    suite.addTest(JSONInvokeMethodTest.suite());
    suite.addTest(JSONRMIInvokeMethodTest.suite());

    // suite.addTest(ConversionTest.suite());
    // if (TestProperties.getMockWebServiceTests())
    // {
    // suite.addTest(MockWebServiceAdapterTest.suite());
    // suite.addTest(MockWebServiceViewDTOAdapterTest.suite());
    // suite.addTest(MockWebServiceUtilDTOAdapterTest.suite());
    // suite.addTest(MockWebServiceInvokeMethodTest.suite());
    // suite.addTest(MockWebServiceUtilDTOMethodTest.suite());
    // suite.addTest(MockWebServiceViewDTOMethodTest.suite());
    // suite.addTest(MockWebServiceFacadeGenerationTest.suite());
    // suite.addTest(MockWebServiceMessageTest.suite());
    // }
    //
    // // web service tests (requires a servlet)
    // if (TestProperties.getWebServiceTests())
    // {
    // suite.addTest(WebServiceAdapterTest.suite());
    // suite.addTest(WebServiceUtilDTOAdapterTest.suite());
    // suite.addTest(WebServiceViewDTOAdapterTest.suite());
    // suite.addTest(WebServiceInvokeMethodTest.suite());
    // suite.addTest(WebServiceUtilDTOMethodTest.suite());
    // suite.addTest(WebServiceViewDTOMethodTest.suite());
    // suite.addTest(WebServiceFacadeGenerationTest.suite());
    // suite.addTest(WebServiceMessageTest.suite());
    // }
    //
    // suite.addTest(JSONWebServiceInvokeMethodTest.suite());
    // suite.addTest(SeleniumTestSuite.suite());

    return suite;
  }
}
