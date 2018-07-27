package com.runwaysdk.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
  ViewQueryTest.class,
  ExtraQueryTest.class,
  AttributeComparisonTest.class,
  AttributeQueryTest.class,
  EnumerationQueryTest.class,
  ReferenceQueryTest.class,
  StructQueryTest.class,
  RelationshipQueryTest.class,
  RelationshipAttributeQuery.class,
  ChildAttributeRelationshipQueryTest.class
})
public class ParentQueryTestSuite
{
  private static QueryMasterSetup setup = new QueryMasterSetup(QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType());

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
