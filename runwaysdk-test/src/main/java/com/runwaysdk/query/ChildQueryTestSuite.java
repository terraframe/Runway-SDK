package com.runwaysdk.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.runwaysdk.session.Request;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  EnumerationMasterQueryTest.class,
  ValueQueryTest.class,
  ValueQueryReferenceTest.class,
  ValueQueryMultiReferenceTest.class,
  ValueQueryMultiTermTest.class,
  ValueQueryEnumTest.class,
  ValueQueryStructTest.class,
  MdTableQueryTest.class,
  
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