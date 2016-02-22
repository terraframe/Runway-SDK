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
package com.runwaysdk.business;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.RunwayVersionTest;
import com.runwaysdk.business.rbac.RBACTest;
import com.runwaysdk.business.state.StateTest;
import com.runwaysdk.format.CustomFormatTest;
import com.runwaysdk.format.DefaultFormatTest;
import com.runwaysdk.format.LocalizedFormatTest;

public class BusinessTestSuite extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(BusinessTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite(BusinessTestSuite.class.getSimpleName());

    // Test classes where the cache algorithm for the test and reference classes are cached.
    
    suite.addTest(ExpressionAttributeTest.suite());
    suite.addTest(StateTest.suite());
    suite.addTest(RBACTest.suite());
    suite.addTest(EntityGenTest.suite());
    suite.addTest(EntityMultiReferenceGenTest.suite());
    suite.addTest(EntityMultiTermGenTest.suite());
    suite.addTest(TransientMultiReferenceGenTest.suite());
    suite.addTest(TransientMultiTermGenTest.suite());
    suite.addTest(UtilGenTest.suite());
    suite.addTest(ViewGenTest.suite());
    suite.addTest(RelationshipGenTest.suite());
    suite.addTest(BusinessLocking.suite());
    suite.addTest(ControllerGenTest.suite());
    suite.addTest(JSPRollbackTest.suite());
    suite.addTest(VirtualAttributeGenTest.suite());
    suite.addTest(DefaultFormatTest.suite());
    suite.addTest(LocalizedFormatTest.suite());
    suite.addTest(CustomFormatTest.suite());
    suite.addTestSuite(RunwayVersionTest.class);
    suite.addTestSuite(NoSourceGenTest.class);

   return suite;

  }

}
