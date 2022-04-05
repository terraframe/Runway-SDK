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
package com.runwaysdk;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.business.BusinessTestSuite;
import com.runwaysdk.business.MultiThreadTestSuite;
import com.runwaysdk.business.ontology.OntologyTestSuite;
import com.runwaysdk.dataaccess.DataAccessTestSuite;
import com.runwaysdk.facade.FacadeTestSuite;
import com.runwaysdk.query.QueryTestSuite;
import com.runwaysdk.session.SessionTestSuite;
import com.runwaysdk.system.scheduler.SchedulerTest;
import com.runwaysdk.vault.VaultTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  DataAccessTestSuite.class,
  BusinessTestSuite.class,
  FacadeTestSuite.class,
  VaultTestSuite.class,
  QueryTestSuite.class,
  MultiThreadTestSuite.class,
  OntologyTestSuite.class,
  SessionTestSuite.class,
  GraphTestSuite.class,
  SchedulerTest.class
})
public class UeberTestSuite
{
  // nothing
}

// public class UeberTestSuite
// {
//
// public static void main(String args[])
// {
// if (DatabaseProperties.getDatabaseClass().equals(HsqlDB.class))
// {
// XMLImporter.main(new String[] { TestConstants.Path.schema_xsd,
// TestConstants.Path.metadata_xml });
// }
//
// junit.textui.TestRunner.run(UeberTestSuite.suite());
// }
//
// public static void HsqlTestRun()
// {
// junit.textui.TestRunner.run(UeberTestSuite.suite());
// }
//
// public static Test suite()
// {
// TestSuite suite = new TestSuite("UeberTestSuite");
//
// suite.addTest(DataAccessTestSuite.suite());
//
//// suite.addTest(SessionTestSuite.suite());
////
//// suite.addTest(BusinessTestSuite.suite());
////
//// suite.addTest(FacadeTestSuite.suite());
////
//// suite.addTest(VaultTestSuite.suite());
////
//// suite.addTest(QueryTestSuite.suite());
////
//// suite.addTest(MultiThreadTestSuite.suite());
////
//// suite.addTestSuite(CommonExceptionTest.class);
////
//// suite.addTestSuite(LoggingTest.class);
////
//// // suite.addTestSuite(SchedulerTest.class);
////
//// suite.addTest(OntologyTestSuite.suite());
//
// return suite;
// }
//
// }
