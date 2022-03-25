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
package com.runwaysdk.session;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.dataaccess.io.SharedTestDataManager;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  MemorySessionTest.class,
  IntegratedMemorySessionTest.class,
  OverflowSessionTest.class,
  IntegratedOverflowTest.class,  
  FileSessionTest.class,
  IntegratedFileSessionTest.class,
  BufferedSessionTest.class,
  IntegratedBufferedSessionTest.class,  
  MethodTest.class,
  IntegratedMethodTest.class,
  LocaleManagerTest.class,
  IntegratedSessionTest.class,
  SessionTest.class
})
public class SessionTestSuite
{
  public static final String TEST_DATA_PREFIX = "SessionTestSuite";
  
  public static final String MD_DIMENSION_NAME = TEST_DATA_PREFIX + "D1";
  
  @Request
  @AfterClass
  public static void classTearDown()
  {
    if (! "true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")))
    {
      MdDimensionDAO dimension = SharedTestDataManager.getMdDimensionIfExist(MD_DIMENSION_NAME);
      
      if (dimension != null)
      {
        dimension.delete();
      }
    }
  }
}
