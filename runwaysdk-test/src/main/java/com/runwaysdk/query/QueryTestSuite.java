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

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.query.function.AggregateFunctionTestSuite;

public class QueryTestSuite extends TestSuite
{
  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(QueryTestSuite.suite());
  }

  public static Test suite()
  {
    TestSuite totalSuite = new TestSuite();

    TestSuite suite = new TestSuite();

    // run all tests using the defined parent entity
    suite.addTest(ViewQueryTest.suite());
    suite.addTest(ExtraQueryTest.suite());
    suite.addTest(AttributeComparisonTest.suite());
    suite.addTest(AttributeQueryTest.suite());
    suite.addTest(EnumerationQueryTest.suite());
    suite.addTest(ReferenceQueryTest.suite());
    suite.addTest(StructQueryTest.suite());
    suite.addTest(RelationshipQueryTest.suite());
    suite.addTest(RelationshipAttributeQuery.suite());
    suite.addTest(ChildAttributeRelationshipQueryTest.suite());
    TestSuite parentSuite = new TestSuite();
    parentSuite.addTest(new QueryMasterSetup(suite, QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType()));
    totalSuite.addTest(parentSuite);

    // run all tests using the defined child entity
    suite = new TestSuite();
    // This test only needs to be executed once
    suite.addTest(EnumerationMasterQueryTest.suite());

    suite.addTest(ValueQueryTest.suite());
    suite.addTest(ValueQueryReferenceTest.suite());
    suite.addTest(ValueQueryMultiReferenceTest.suite());
    suite.addTest(ValueQueryMultiTermTest.suite());
    suite.addTest(ValueQueryEnumTest.suite());
    suite.addTest(ValueQueryStructTest.suite());
    
    suite.addTest(MdTableQueryTest.suite());
    
    suite.addTest(ViewQueryTest.suite());    
    suite.addTest(ExtraQueryTest.suite());
    suite.addTest(AttributeComparisonTest.suite());
    suite.addTest(AttributeQueryTest.suite());
    suite.addTest(EnumerationQueryTest.suite());
    suite.addTest(MultiReferenceQueryTest.suite());
    suite.addTest(MultiTermQueryTest.suite());
    suite.addTest(ReferenceQueryTest.suite());
    suite.addTest(StructQueryTest.suite());
    suite.addTest(RelationshipQueryTest.suite());
    suite.addTest(RelationshipAttributeQuery.suite());
    suite.addTest(ChildAttributeRelationshipQueryTest.suite());
    
    TestSuite childSuite = new TestSuite();
    childSuite.addTest(new QueryMasterSetup(suite, QueryMasterSetup.childQueryInfo.getType(), QueryMasterSetup.childRefQueryInfo.getType()));
    totalSuite.addTest(childSuite);
    


    totalSuite.addTest(StandaloneStructQueryTest.suite());
    totalSuite.addTest(AggregateFunctionTestSuite.suite());

    return totalSuite;
  }
}
