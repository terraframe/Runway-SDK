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
package com.runwaysdk.query.function;

import java.math.BigDecimal;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableDecimal;

public class DecimalTest  extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(DecimalTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DecimalTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() 
      { 
      }

      protected void tearDown() 
      { 
      }
    };

    return wrapper;
  }
  

  public void testDecimalEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").EQ(F.MIN(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();

    String errMsg = "Eq MIN() check returned wrong result.";
    assertEquals(errMsg, 1, count);
    
    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        fail("Query did not return the expected object.");
      }
    }
  }

  
  public void testDecimalEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();   
      Class<?> objectClass = LoaderDecorator.load(type);      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(F.MIN(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq MIN() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          fail("Query did not return the expected object.");
        }
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GT(F.MIN(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMinGtList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  
  public void testDecimalGtMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      Class<?> objectClass = LoaderDecorator.load(type);    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(F.MIN(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.DESC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = AggregateFunctionMasterSetup.decimalMinGtList.size() - 1;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMinGtList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount -= 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GE(F.MIN(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMinGtEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  
  public void testDecimalGtEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();  
      Class<?> objectClass = LoaderDecorator.load(type);    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(F.MIN(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMinGtEqList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LT(F.MIN(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  
  public void testDecimalLtMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(F.MIN(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LE(F.MIN(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();

    String errMsg = "Less than Equal MIN() check returned wrong result.";
    assertEquals(errMsg, 1, count);
    
    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        fail("Query did not return the expected object.");
      }
    }
    
  }

  
  public void testDecimalLtEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);  
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(F.MIN(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          fail("Query did not return the expected object.");
        }
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").NE(F.MIN(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMinNotEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testDecimalNotEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);  
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(F.MIN(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMinNotEqList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").EQ(F.MAX(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 1, count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(AggregateFunctionMasterSetup.classObjectList.size() - 1);
    
    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        fail("Query did not return the expected object.");
      }
    }
  }

  
  public void testDecimalEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();   
      Class<?> objectClass = LoaderDecorator.load(type);      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(F.MAX(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(AggregateFunctionMasterSetup.classObjectList.size() - 1);

      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          fail("Query did not return the expected object.");
        }
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GT(F.MAX(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  
  public void testDecimalGtMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(F.MAX(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GE(F.MAX(query2.aDecimal("comFuncDecimal"))));

    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, 1, count);
  }

  
  public void testDecimalGtEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(F.MAX(comAttributeDecimal)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LT(F.MAX(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.decimalMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMaxLtList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  
  public void testDecimalLtMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      Class<?> objectClass = LoaderDecorator.load(type);   
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(F.MAX(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMaxLtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMaxLtList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LE(F.MAX(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.decimalMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMaxLtEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testDecimalLtEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(F.MAX(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMaxLtEqList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").NE(F.MAX(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalMaxNotEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testDecimalNotEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();  
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(F.MAX(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalMaxNotEqList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").EQ(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalEqAvgList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testDecimalEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalEqAvgList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalEqAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalGtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GT(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Avg() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalGtAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testDecimalGtAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalGtAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalGtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").GE(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalGtEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testDecimalGtEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalGtEqAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LT(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalLtAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testDecimalLtAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalLtAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").LE(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalLtEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testDecimalLtEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalLtEqAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalNotEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("funcDecimal").NE(F.AVG(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("funcDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalNotEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalNotEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testDecimalNotEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(F.AVG(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalNotEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalNotEqAvgList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  

  public void testDecimalEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").EQ(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalEqCountList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testDecimalEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalEqCountList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalEqCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testDecimalGtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").GT(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Count() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalGtCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testDecimalGtCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();     
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalGtCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalGtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").GE(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalGtEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testDecimalGtEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalGtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalGtEqCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").LT(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalLtCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testDecimalLtCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalLtCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").LE(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalLtEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testDecimalLtEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalLtEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalLtEqCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testDecimalNotEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDecimal("countFuncDecimal").NE(F.COUNT(query2.aDecimal("comFuncDecimal"))));
    query.ORDER_BY(query.aDecimal("countFuncDecimal"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.decimalNotEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.decimalNotEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDecimal")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testDecimalNotEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getCountFuncDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComFuncDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(F.COUNT(comAttributeDecimal)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDecimal, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.decimalNotEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        BigDecimal decimalValue = (BigDecimal)objectClass.getMethod("getCountFuncDecimal").invoke(object);
        if (AggregateFunctionMasterSetup.decimalNotEqCountList.get(loopCount) != decimalValue.doubleValue())
        {
          fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
}
