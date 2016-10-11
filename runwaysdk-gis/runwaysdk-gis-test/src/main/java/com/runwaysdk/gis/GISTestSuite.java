/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK GIS(tm).
 *
 * Runway SDK GIS(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK GIS(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK GIS(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.gis;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.gis.business.GISBusinessTest;
import com.runwaysdk.gis.business.GISControllerGenTest;
import com.runwaysdk.gis.dataaccess.GISDataAccessTest;
import com.runwaysdk.gis.dataaccess.GISWebFormDAOTest;
import com.runwaysdk.gis.dataaccess.io.GISSaxParseTest;
import com.runwaysdk.gis.dto.GISAdapterTest;
import com.runwaysdk.gis.dto.GISRMIAdapterTest;
import com.runwaysdk.gis.dto.GISRMIViewAdapterTest;
import com.runwaysdk.gis.dto.GISRMIVirtualAdapterTest;
import com.runwaysdk.gis.dto.GISViewAdapterTest;
import com.runwaysdk.gis.dto.GISVirtualAdapterTest;
import com.runwaysdk.gis.geo.GeoEntityTest;
import com.runwaysdk.gis.geo.UniversalTest;
import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  GISDataAccessTest.class,
  GISBusinessTest.class,
  GISAdapterTest.class,
//  GISRMIAdapterTest.class,
  GISViewAdapterTest.class,
  GISRMIViewAdapterTest.class,
  GISVirtualAdapterTest.class,
  GISRMIVirtualAdapterTest.class,
  GISControllerGenTest.class,
  GISWebFormDAOTest.class,
  GeoEntityTest.class,
  UniversalTest.class,
  GISSaxParseTest.class
})
public class GISTestSuite
{
  @BeforeClass
  @Request
  public static void setUp() throws Exception {
    GISMasterTestSetup.doSetUp(true);
  }
  
  @AfterClass
  @Request
  public static void tearDown() throws Exception {
    GISMasterTestSetup.doTearDown(true);
  }
  
//  public static Test suite()
//  {
//    TestSuite gisTestSuite = new TestSuite();
//
//    TestSuite suite = new TestSuite();
//    suite.addTest(GISDataAccess.suite());
//    suite.addTest(GISBusiness.suite());
//    suite.addTest(GISAdapterTest.suite());
//    suite.addTest(GISRMIAdapterTest.suite());
//    suite.addTest(GISViewAdapterTest.suite());
//    suite.addTest(GISRMIViewAdapterTest.suite());
//    suite.addTest(GISVirtualAdapterTest.suite());
//    suite.addTest(GISRMIVirtualAdapterTest.suite());
//    suite.addTest(GISControllerGenTest.suite());
//    suite.addTest(GISWebFormDAO.suite());
////    suite.addTestSuite(GeoEntityTest.class);
////    suite.addTestSuite(UniversalTest.class);
//    
//    gisTestSuite.addTest(new GISMasterTestSetup(suite));
//
//    gisTestSuite.addTest(GISSaxParseTest.suite());
//
//    return gisTestSuite;
//  }
}
