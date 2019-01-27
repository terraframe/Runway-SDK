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
package com.runwaysdk.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({
//  EnumerationMasterQueryTest.class,
//  ValueQueryTest.class,
//  ValueQueryReferenceTest.class,
//  ValueQueryMultiReferenceTest.class,
//  ValueQueryMultiTermTest.class,
//  ValueQueryEnumTest.class,
  ValueQueryStructTest.class,
//  MdTableQueryTest.class,
//  
//  ViewQueryTest.class,
//  ExtraQueryTest.class,
//  AttributeComparisonTest.class,
//  AttributeQueryTest.class,
//  EnumerationQueryTest.class,
//  ReferenceQueryTest.class,
//  StructQueryTest.class,
//  RelationshipQueryTest.class,
//  RelationshipAttributeQuery.class,
//  ChildAttributeRelationshipQueryTest.class
})
public class ChildQueryTestSuite
{
  private static QueryMasterSetup setup = new QueryMasterSetup(QueryMasterSetup.childQueryInfo.getType(), QueryMasterSetup.childRefQueryInfo.getType());

  @Request
  @BeforeClass
  public static void suiteClassSetup() throws Exception
  {
    setup.setUp();
  }

  @Request
  @AfterClass
  public static void suiteTearDown() throws Exception
  {
    setup.tearDown();
  }

}
