/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved. 
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
 ******************************************************************************/
package com.runwaysdk.session;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class SessionTestSuite extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(SessionTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(SessionTest.suite());
    suite.addTest(IntegratedSessionTest.suite());
    suite.addTest(FileSessionTest.suite());
    suite.addTest(IntegratedFileSessionTest.suite());
    suite.addTest(BufferedSessionTest.suite());
    suite.addTest(IntegratedBufferedSessionTest.suite());
    suite.addTest(OverflowSessionTest.suite());
    suite.addTest(IntegratedOverflowTest.suite());
    suite.addTest(MemorySessionTest.suite());
    suite.addTest(IntegratedMemorySessionTest.suite());
    suite.addTest(MethodTest.suite());
    suite.addTest(IntegratedMethodTest.suite());
    suite.addTest(LocaleManagerTest.suite());

    return suite;
  }

}
