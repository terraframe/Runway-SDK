/**
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.ValueObject;

public class ValueQueryReferenceTest extends TestCase
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
    junit.textui.TestRunner.run(new QueryMasterSetup(ValueQueryReferenceTest.suite(), QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ValueQueryReferenceTest.class);

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
  
  public void testReferenceEqualID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").EQ(QueryMasterSetup.childRefQueryObject.getId()));
      
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
      
      vQ.SELECT(query.aReference("reference"));
      vQ.WHERE(query.aReference("reference").EQ(""));
  
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
  
  public void testReferenceNotEqualID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").NE(""));
      
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
      
      vQ.SELECT(query.aReference("reference"));
      vQ.WHERE(query.aReference("reference").NE(QueryMasterSetup.childRefQueryObject.getId()));
  
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
  
  public void testReferenceInID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").IN(QueryMasterSetup.childRefQueryObject.getId(), QueryMasterSetup.childRefQueryObject2.getId(), ""));
      
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
      
      vQ.SELECT(query.aReference("reference"));
      vQ.WHERE(query.aReference("reference").IN("purple monkey dishwasher", QueryMasterSetup.childRefQueryObject2.getId(), ""));
  
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
  
  public void testReferenceNiID()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").NI(QueryMasterSetup.childRefQueryObject2.getId(), QueryMasterSetup.testQueryObject1.getId(), QueryMasterSetup.relQueryObject1.getId()));
      
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
      
      vQ.SELECT(query.aReference("reference"));
      vQ.WHERE(query.aReference("reference").NI(QueryMasterSetup.childRefQueryObject.getId(), QueryMasterSetup.childRefQueryObject2.getId(), QueryMasterSetup.relQueryObject1.getId()));
  
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
  
  public void testReferenceEnumerationContainsAll()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      
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
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
  
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

  public void testReferenceEnumerationContainsAny()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      
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
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId));
  
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
  
  public void testReferenceEnumerationContainsExactly()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsExactly(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      
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
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
  
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
  
  public void testReferenceEnumerationNotContainsAll()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));
      
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
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
  
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
  
  public void testReferenceEnumerationNotContainsAny()
  {
    OIterator<ValueObject> i = null;
    
    try
    {
      // perform a query that should find a match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.kansasItemId));
      
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
      
      vQ.SELECT(query.aReference("reference").aEnumeration("refQueryEnumeration"));
      vQ.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));
  
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(true));
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(false));
  
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryBoolean");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryBoolean", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query for null values
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryBoolean", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(false));
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(true));
  
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));
      
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
      
      vQ.SELECT(query.aReference("reference").aBoolean("refQueryBoolean"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQ("ref character value"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQ("wrong character value"));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQi("REF CHARACTER VALUE"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQi("WRONG CHARACTER VALUE"));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").IN("wrong value 1", "ref character value", "wrong value 2"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3", "REF CHARACTER VALUE"));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").INi("wrong value 1", "REF CHARACTER VALUE", "wrong value 2"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryCharacter");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryCharacter", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryCharacter", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKE("%character%"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKE("%character"));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKEi("%CHARACTER%"));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKEi("%CHARACTER"));
  
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NE("wrong character value"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NE("ref character value"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NEi("WRONG CHARACTER VALUE"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NEi("REF CHARACTER VALUE"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NI("wrong character value 1", "wrong character value 2", "wrong character value 3"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NI("wrong value", "ref character value", "wrong value 2"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NIi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NIi("WRONG VALUE", "REF CHARACTER VALUE", "WRONG VALUE 2"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NLIKE("%wrong%"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NLIKE("%character%"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NLIKEi("%WRONG%"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
      
      vQ.SELECT(query.aReference("reference").aCharacter("refQueryCharacter"), query.id("objectId"));
      vQ.WHERE(OR.get(query.aReference("reference").aCharacter("refQueryCharacter").NLIKEi("%CHARACTER%"), query.aReference("reference").aCharacter("refQueryCharacter").EQ("")));
      
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").EQ(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").EQ(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").EQ("2007-11-06"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").EQ("2006-05-05"));
  
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GT(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GT(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GT("2007-11-05"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GT("2007-11-07"));
      
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));
  
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-06"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-05"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-07"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryDate");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDate", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").NE(""));
      
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDate", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDateLTDate()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LT(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LT(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LT("2007-11-07"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LT("2007-11-05"));
      
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query with a date equal to the stored
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));
  
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-06"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-07"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-05"));
  
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
    Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").NE(date));
      
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
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").NE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").NE("2007-11-05"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDate("refQueryDate"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDate("refQueryDate").NE("2007-11-06"));
  
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 11:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ("2007-11-06 12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ("2007-11-06 11:00:00"));
  
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 11:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT("2007-11-06 11:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT("2007-11-06 12:00:00"));
  
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));
  
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-07 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-06 12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-05 12:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-07 12:00:00"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryDateTime");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDateTime", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDateTime", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
      if (i != null)
        i.close();
    }
  }
  
  public void testDateTimeLTDateTime()
  {
    OIterator<ValueObject> i = null;
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-07 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT("2007-11-07 12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT("2007-11-05 12:00:00"));
  
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-07 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));
  
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-11-06 12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-11-07 12:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-11-05 12:00:00"));
  
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
    Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 12:00:00",  new java.text.ParsePosition(0));
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE(date));
      
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
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00",  new java.text.ParsePosition(0));
      
      vQ = qf.valueQuery();
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get( query.aReference("reference").aDateTime("refQueryDateTime").NE(date), query.aReference("reference").aDateTime("refQueryDateTime").NE("") ));
  
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE("2007-11-05 12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDateTime("refQueryDateTime"), query.id("objectId"));
      vQ.WHERE(AND.get( query.aReference("reference").aDateTime("refQueryDateTime").NE("2007-11-06 12:00:00"), query.aReference("reference").aDateTime("refQueryDateTime").NE("") ));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ(new BigDecimal(200.5)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ(new BigDecimal(201.5)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ("201.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT(new BigDecimal(200)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT(new BigDecimal(201)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT("201"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(200.5)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(200)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(201)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("200"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("201"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryDecimal");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT(new BigDecimal(201)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT(new BigDecimal(200)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT("201"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(200.5)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(201)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(99)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("99"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE(new BigDecimal(201)));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE(new BigDecimal(200.5)));
  
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE("201"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDecimal("refQueryDecimal"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE("200.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ(200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ(201.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ("201.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT(20.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT(220.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT("20.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT("220.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE(200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE(20.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE(220.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("20.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("220.5"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryDouble");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT(220.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT(20.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT("220.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT("20.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE(200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE(220.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE(20.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("220.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("20.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE(201.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE(200.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE("201.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aDouble("refQueryDouble"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE("200.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ((float)200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ((float)201.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ("201.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT((float)20.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT((float)221.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT("20.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT("221.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float)200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float)21.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float)221.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("21.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("221.5"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryFloat");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT((float)220.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT((float)21.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT("220.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT("21.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float)200.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float)201.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float)21.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("200.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("201.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("21.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE((float)210.5));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE((float)200.5));
  
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE("210.5"));
      
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
      
      vQ.SELECT(query.aReference("reference").aFloat("refQueryFloat"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE("200.5"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ(200));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ(201));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT(20));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT(201));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT("20"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(200));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(20));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(201));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("201"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryInteger");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT(201));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT(20));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT("201"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(200));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(201));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(20));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE(201));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE(200));
  
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE("201"));
      
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
      
      vQ.SELECT(query.aReference("reference").aInteger("refQueryInteger"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE("200"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").EQ((long)200));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").EQ((long)201));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").EQ("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").EQ("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GT((long)20));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GT((long)201));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GT("20"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GT("201"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long)200));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long)20));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long)201));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").GE("201"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryLong");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").NE(""));
  
      i = vQ.getIterator();
      
      if (i.hasNext())
      {
        QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", origValue);
        QueryMasterSetup.childRefQueryObject.apply();
        
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LT((long)220));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LT((long)20));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LT("220"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LT("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long)200));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long)220));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long)20));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE("200"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE("220"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").LE("20"));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").NE((long)20));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").NE((long)200));
  
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").NE("20"));
      
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
      
      vQ.SELECT(query.aReference("reference").aLong("refQueryLong"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aLong("refQueryLong").NE("200"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").EQ("ref text value"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NE("ref text value"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").EQi("REF TEXT VALUE"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").EQi("WRONG TEXT VALUE"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").IN("REF text value", "ref text value", "wrong text value"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").IN("REF text value", "REF TEXT value", "REF TEXT VALUE"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").INi("WRONG TEXT VALUE", "REF TEXT VALUE", "wrong TEXT value 2"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").INi("WRONG TEXT VALUE", "WRONG TEXT VALUE 2", "WRONG TEXT VALUE 3"));
  
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
    
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryText");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryText", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryText", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").LIKE("%text%"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").LIKE("%text"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").LIKEi("%TEXT%"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").LIKEi("%TEXT"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NE("wrong text value"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NE("ref text value"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NEi("WRONG TEXT VALUE"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NEi("REF TEXT VALUE"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NI("REF text value", "REF TEXT value", "REF TEXT VALUE"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NI("REF text value", "ref text value", "wrong text value"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NIi("WRONG text value", "WRONG TEXT value 2", "WRONG TEXT VALUE 3"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NIi("REF text value", "REF TEXT VALUE", "REF TEXT value"));
  
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NLIKEi("%WRONG%"));
      
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
      
      vQ.SELECT(query.aReference("reference").aText("refQueryText"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aText("refQueryText").NLIKEi("%TEXT%"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").EQ(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").EQ(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").EQ("12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").EQ("11:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GT(date));
      
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

      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GT(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GT("11:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GT("14:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE("12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE("12:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").GE("14:00:00"));
  
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
    String origValue = QueryMasterSetup.childRefQueryObject.getValue("refQueryTime");
    QueryMasterSetup.childRefQueryObject.setValue("refQueryTime", "");
    QueryMasterSetup.childRefQueryObject.apply();
    
    try
    {
      // perform a query that should find exactly 1 match
      QueryFactory qf = new QueryFactory();
      
      ValueQuery vQ = qf.valueQuery();
      BusinessDAOQuery query = qf.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").EQ(""));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").NE(""));
  
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
      QueryMasterSetup.childRefQueryObject.setValue("refQueryTime", origValue);
      QueryMasterSetup.childRefQueryObject.apply();
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LT(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LT(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LT("14:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LT("10:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE("12:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE("14:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").LE("10:00:00"));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").NE(date));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").NE(date));
  
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").NE("10:00:00"));
      
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
      
      vQ.SELECT(query.aReference("reference").aTime("refQueryTime"), query.id("objectId"));
      vQ.WHERE(query.aReference("reference").aTime("refQueryTime").NE("12:00:00"));
  
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
