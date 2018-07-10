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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.ValueObject;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ValueQueryStructTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new QueryMasterSetup(ValueQueryStructTest.suite(), QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ValueQueryStructTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  public static void classSetUp() 
  {
  }

  public static void classTearDown() 
  {
  }

  /**
   * No setup needed
   * non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {

  }

  /**
   * No teardown neaded
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
  }
  /*
  public void testStructEqualID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      AttributeStruct aStruct = query.aStruct("queryStruct");
      vQ.SELECT(aStruct, query.id("objectId"));
      vQ.WHERE(aStruct.EQ(QueryMasterSetup.mdStruct.getId()));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(aStruct);
      vQ.WHERE(aStruct.EQ(""));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  */
  /*
  public void testStructNotEqualID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").NE(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct"));
      vQ.WHERE(query.aStruct("queryStruct").NE(QueryMasterSetup.childRefQueryObject.getId()));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  */
  /*
  public void testStructInID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").IN(QueryMasterSetup.childRefQueryObject.getId(), QueryMasterSetup.childRefQueryObject2.getId(), ""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct"));
      vQ.WHERE(query.aStruct("queryStruct").IN("purple monkey dishwasher", QueryMasterSetup.childRefQueryObject2.getId(), ""));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testStructNiID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").NI(QueryMasterSetup.childRefQueryObject2.getId(), QueryMasterSetup.testQueryObject1.getId(), QueryMasterSetup.relQueryObject1.getId()));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct"));
      vQ.WHERE(query.aStruct("queryStruct").NI(QueryMasterSetup.childRefQueryObject.getId(), QueryMasterSetup.childRefQueryObject2.getId(), QueryMasterSetup.relQueryObject1.getId()));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  */
  public void testStructEnumerationContainsAll()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }
      
      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testStructEnumerationContainsAny()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testStructEnumerationContainsExactly()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsExactly(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testStructEnumerationNotContainsAll()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testStructEnumerationNotContainsAny()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.kansasItemId));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that should NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration"));
      vQ.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testBooleanEqualBoolean()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(true));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query for false values that should find exactly 2 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(false));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testBooleanEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query for false values that should find exactly 2 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testBooleanEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryBoolean");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryBoolean", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(""));
      
      i = vQ.getIterator();

      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query to find all objects with non-null queryBoolean values
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryBoolean", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testBooleanNotEqualBoolean()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(false));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query for false values that should find exactly 2 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(true));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testBooleanNotEqualString()
  {
    try
    {
      // perform a query for true values that should find exactly 2 matches
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));
      
      OIterator<ValueObject> i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query for false values that should find exactly 2 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aBoolean("structQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));
  
      i = vQ.getIterator();

      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testCharacterEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("basic character value"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("wrong character value"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testCharacterEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQi("BASIC CHARACTER VALUE"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQi("WRONG CHARACTER VALUE"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterEqualStringInArray()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").IN("wrong value 1", "basic character value", "wrong value 2"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "BASIC CHARACTER VALUE"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").INi("wrong value 1", "BASIC CHARACTER VALUE", "wrong value 2"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryCharacter");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryCharacter", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 3 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryCharacter", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterLikeString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKE("%character%"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKE("%character"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKEi("%CHARACTER%"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKEi("%CHARACTER"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NE("wrong character value"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
       // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NE("basic character value"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NEi("WRONG CHARACTER VALUE"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
       // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NEi("BASIC CHARACTER VALUE"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NI("wrong character value 1", "wrong character value 2", "wrong character value 3"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
       // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NI("wrong value", "basic character value", "wrong value 2"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NIi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
       // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NIi("WRONG VALUE", "BASIC CHARACTER VALUE", "WRONG VALUE 2"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotLikeString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKE("%wrong%"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKE("%character%"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testCharacterNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform another query that should find 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKEi("%WRONG%"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aCharacter("structQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKEi("%CHARACTER%"), query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("")));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ("2008-11-06"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ("2006-05-05"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateGTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT("2008-11-05"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT("2008-11-07"));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateGEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-06"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date less than the stored
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-05"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-07"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryDate");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDate", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE(""));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDate", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDateLTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT(date));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT("2008-11-07"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT("2008-11-05"));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateLEDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date less than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date greater than the stored
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-06"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date less than the stored
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-07"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query with a date greater than the stored
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-05"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateNotEqualDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE("2008-11-05"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDate("structQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE("2008-11-06"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 11:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ("2008-11-06 12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ("2008-11-06 11:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeGTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 11:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testDateTimeGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT("2008-11-06 11:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT("2008-11-06 12:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeGEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-06 12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-05 12:00:00"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-07 12:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryDateTime");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDateTime", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE(""));
  
      i = vQ.getIterator();
      
      if(i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDateTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeLTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT("2008-11-07 12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT("2008-11-05 12:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeLEDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-11-06 12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-11-07 12:00:00"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-11-05 12:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeNotEqualDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get( query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE(date), query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("") ));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("2008-11-05 12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute date time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDateTime("structQueryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get( query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("2008-11-06 12:00:00"), query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("") ));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute date time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalEqualDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ(new BigDecimal(300.5)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ(new BigDecimal(301.5)));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ("301.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalGTDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT(new BigDecimal(300)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT(new BigDecimal(301)));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT("301"));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalGEDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(300.5)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(300)));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(301)));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("300"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryDecimal");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDecimal", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDecimal", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalLTDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT(new BigDecimal(301)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT(new BigDecimal(300)));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT("301"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT("300"));
      
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalLEDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(300.5)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(301)));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(99)));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("301"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("99"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalNotEqualDecimal()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE(new BigDecimal(301)));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE(new BigDecimal(300.5)));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDecimalNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE("301"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute decimal values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDecimal("structQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE("300.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleEqualDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ(300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ(301.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ("301.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleGTDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT(30.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT(320.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT("30.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT("320.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleGEDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
   // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE(300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE(30.5));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
   // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE(320.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
   // perform a query that WILL find a match based on equal to
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("30.5"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
   // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("320.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryDouble");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDouble", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryDouble", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleLTDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT(320.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT(30.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT("320.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT("30.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleLEDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE(300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE(320.5));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE(30.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equal
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("320.5"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("30.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleNotEqualDouble()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE(301.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE(300.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testDoubleNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE("301.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute double values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aDouble("structQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE("300.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatEqualFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ((float)300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ((float)301.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ("301.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGTFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT((float)30.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT((float)321.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT("30.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT("321.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatGEFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)31.5));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)321.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }

  public void testFloatGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("31.5"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("321.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryFloat");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryFloat", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryFloat", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatLTFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT((float)320.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT((float)31.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT("320.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT("31.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatLEFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)300.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)301.5));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)31.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("300.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on less than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("301.5"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("31.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatNotEqualFloat()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE((float)310.5));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE((float)300.5));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testFloatNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE("310.5"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute float values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aFloat("structQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE("300.5"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerEqualInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ(300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ(301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerGTInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT(30));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT(301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT("30"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerGEInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(30));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("30"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryInteger");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryInteger", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryInteger", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerLTInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT(301));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT(30));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT("301"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT("30"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerLEInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(301));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(30));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("301"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("30"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerNotEqualInteger()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE(301));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE(300));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testIntegerNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE("301"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute integer values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aInteger("structQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE("300"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
 
  public void testLongEqualLong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ((long)300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ((long)301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongGTLong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT((long)30));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT((long)301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT("30"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongGELong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)30));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)301));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("30"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("301"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryLong");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryLong", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryLong", origValue);
        QueryMasterSetup.testQueryObject1.apply();
        
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryLong", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testLongLTLong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT((long)320));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT((long)30));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT("320"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT("30"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongLELong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)300));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)320));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)30));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("300"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("320"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("30"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongNotEqualLong()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE((long)30));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE((long)300));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testLongNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE("30"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute long values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aLong("structQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE("300"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQ("basic text value"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE("basic text value"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQi("BASIC TEXT VALUE"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQi("WRONG TEXT VALUE"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextEqualStringInArray()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").IN("BASIC text value", "basic text value", "wrong text value"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").IN("BASIC text value", "BASIC TEXT value", "BASIC TEXT VALUE"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").INi("WRONG TEXT VALUE", "BASIC TEXT VALUE", "wrong TEXT value 2"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextEqualNull()
  {
    OIterator<ValueObject> i = null;
    
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryText");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryText", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryText", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testTextLikeString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKE("%text%"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKE("%text"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKEi("%TEXT%"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKEi("%TEXT"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE("wrong text value"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE("basic text value"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextNotEqualString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NEi("WRONG TEXT VALUE"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NEi("BASIC TEXT VALUE"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextNotEqualStringInArray()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NI("BASIC text value", "BASIC TEXT value", "BASIC TEXT VALUE"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NI("BASIC text value", "basic text value", "wrong text value"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextNotEqualStringInArray_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NIi("BASIC text value", "BASIC TEXT VALUE", "BASIC TEXT value"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTextNotLikeString_IgnoreCase()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKEi("%WRONG%"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute text values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aText("structQueryText"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKEi("%TEXT%"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ("12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ("11:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeGTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();

      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeGTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT("11:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT("14:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeGETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeGEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("12:00:00"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("14:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeEqualNull()
  {
    OIterator<ValueObject> i = null;
    String origValue = QueryMasterSetup.testQueryObject1.getStructValue("queryStruct", "structQueryTime");
    QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryTime", "");
    QueryMasterSetup.testQueryObject1.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ(""));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.testQueryObject1.setStructValue("queryStruct", "structQueryTime", origValue);
      QueryMasterSetup.testQueryObject1.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeLTTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeLTString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT("14:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT("10:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeLETime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeLEString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("12:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL find a match based on greater than
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("14:00:00"));
  
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform a query that WILL NOT find a match
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("10:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeNotEqualTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("10:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE(date));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE(date));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
  
  public void testTimeNotEqualString()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE("10:00:00"));
      
      i = vQ.getIterator();
      
      if(!i.hasNext())
      {
        fail("A query based on attribute time values did not return any results when it should have");
      }

      while (i.hasNext())
      {
        if (!i.next().getValue("objectId").equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("One of the objects returned by the query had an Id not equal to testQueryObject1.");
        }
      }
      
      // perform another query that should find 0 matches
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aStruct("queryStruct").aTime("structQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE("12:00:00"));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (i != null)
        i.close();
    }
  }
}
