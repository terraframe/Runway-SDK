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

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class ExtraQueryTest extends TestCase
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
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(ExtraQueryTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() {}

      protected void tearDown(){}
    };

    return wrapper;
  }
  
  public void testQueryAnd_Condition()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      query.
        WHERE(query.aBoolean("queryBoolean").EQ(true).
        AND(query.aBoolean("queryBoolean").NE(false))
      );

      OIterator<BusinessDAOIF> iterator = query.getIterator();
      
      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }
      

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      query.
        WHERE(query.aBoolean("queryBoolean").EQ(true).
        AND(query.aBoolean("queryBoolean").NE(true))
      );

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }   
  }
  
  
  public void testQueryAnd_Condition_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      AttributeBoolean attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);  
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("AND", Condition.class).invoke(queryObject, attributeBoolean.NE(false));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }
      
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);  
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("AND", Condition.class).invoke(queryObject, attributeBoolean.NE(true));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }   
  }
  
  public void testQueryOr_Condition()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());

      query.
        WHERE(query.aBoolean("queryBoolean").EQ(true).
        OR(query.aBoolean("queryBoolean").EQ(false))
      );
      
      OIterator<BusinessDAOIF> iterator = query.getIterator();
      
      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }
      

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      
      query.
        WHERE(query.aBoolean("queryBoolean").EQ(false).
        OR(query.aBoolean("queryBoolean").EQ(false))
      );

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }   
  }

  
  public void testQueryOr_Condition_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);

      AttributeBoolean attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);  
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("OR", Condition.class).invoke(queryObject, attributeBoolean.NE(false));
      
      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }
      
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }
      
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);  
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));
      attributeBoolean = (AttributeBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      queryClass.getMethod("OR", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute boolean values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }  
  }
}
