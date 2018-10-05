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
package com.runwaysdk.dataaccess;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  EntityAttributeTest.class,
  EntityAttributeMultiReferenceTest.class,
  EntityAttributeMultiTermTest.class,
  EnumerationTest.class,
  RelationshipTest.class,
  MdRelationshipTest.class,
  MdAttributeTest.class,
  RegexTest.class,
  EncryptionTest.class,
  MdDimensionTest.class
})
public class CacheNothingTestSuite
{
  private static EntityMasterTestSetup entityMasterTestSetup = new EntityMasterTestSetup(EntityCacheMaster.CACHE_NOTHING.getCacheCode());

  @Request
  @BeforeClass
  public static void classSetup()
  {
    entityMasterTestSetup.setUp();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    entityMasterTestSetup.tearDown();
  }

}
