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
/*
 * Created on Jun 22, 2005
 */
package com.runwaysdk.business.ontology;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * 
 * @author Richard Rowlands
 * @since 2.0.17
 **/
public class OntologyTestSuite extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(OntologyTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite testSuite = new TestSuite(OntologyTestSuite.class.getSimpleName());
    testSuite.setName(OntologyTestSuite.class.getName());

    testSuite.addTest(DatabaseAllPathsStrategyTest.suite());

    return testSuite;
  }
}
