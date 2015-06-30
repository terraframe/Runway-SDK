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
import com.runwaysdk.query.SelectableLong;

public class LongTest  extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(LongTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(LongTest.class);

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
  

  public void testLongEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").EQ(F.MIN(query2.aLong("comFuncLong"))));

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

  @SuppressWarnings("unchecked")
  public void testLongEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();   
      Class objectClass = LoaderDecorator.load(type);      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(F.MIN(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq MIN() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable)resultIterator)
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
  
  public void testLongGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GT(F.MIN(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMinGtList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  @SuppressWarnings("unchecked")
  public void testLongGtMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      Class objectClass = LoaderDecorator.load(type);    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(F.MIN(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.DESC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = AggregateFunctionMasterSetup.longMinGtList.size() - 1;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMinGtList.get(loopCount) != longValue.intValue())
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
  
  public void testLongGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GE(F.MIN(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMinGtEqList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();  
      Class objectClass = LoaderDecorator.load(type);    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(F.MIN(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMinGtEqList.get(loopCount) != longValue.intValue())
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

  public void testLongLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LT(F.MIN(query2.aLong("comFuncLong"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(F.MIN(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LE(F.MIN(query2.aLong("comFuncLong"))));

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

  @SuppressWarnings("unchecked")
  public void testLongLtEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);  
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(F.MIN(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable)resultIterator)
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

  public void testLongNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").NE(F.MIN(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMinNotEqList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqMinAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);  
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(F.MIN(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMinNotEqList.get(loopCount) != longValue.intValue())
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
  
  public void testLongEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").EQ(F.MAX(query2.aLong("comFuncLong"))));

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

  @SuppressWarnings("unchecked")
  public void testLongEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();   
      Class objectClass = LoaderDecorator.load(type);      
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(F.MAX(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(AggregateFunctionMasterSetup.classObjectList.size() - 1);

      for (Object object : (Iterable)resultIterator)
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
  
  public void testLongGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GT(F.MAX(query2.aLong("comFuncLong"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  @SuppressWarnings("unchecked")
  public void testLongGtMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(F.MAX(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GE(F.MAX(query2.aLong("comFuncLong"))));

    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, 1, count);
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(F.MAX(comAttributeLong)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LT(F.MAX(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.longMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMaxLtList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();    
      Class objectClass = LoaderDecorator.load(type);   
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(F.MAX(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longMaxLtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMaxLtList.get(loopCount) != longValue.intValue())
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

  public void testLongLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LE(F.MAX(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.longMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMaxLtEqList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongLtEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(F.MAX(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMaxLtEqList.get(loopCount) != longValue.intValue())
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

  public void testLongNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").NE(F.MAX(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longMaxNotEqList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqMaxAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();  
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(F.MAX(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longMaxNotEqList.get(loopCount) != longValue.intValue())
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

  public void testLongEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").EQ(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longEqAvgList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longEqAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longEqAvgList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longEqAvgList.get(loopCount) != longValue.intValue())
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

  public void testLongGtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GT(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Avg() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longGtAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();     
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtAvgList.get(loopCount) != longValue.intValue())
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

  public void testLongGtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").GE(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longGtEqAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtEqAvgList.get(loopCount) != longValue.intValue())
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

  public void testLongLtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LT(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtAvgList.get(loopCount) != longValue.intValue())
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

  public void testLongLtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").LE(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtEqAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtEqAvgList.get(loopCount) != longValue.intValue())
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
  
  public void testLongNotEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("funcLong").NE(F.AVG(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("funcLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longNotEqAvgList.get(loopCount) != Long.parseLong(businessDAO.getValue("funcLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqAvgAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(F.AVG(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longNotEqAvgList.get(loopCount) != longValue.intValue())
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


  public void testLongEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").EQ(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longEqCountList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longEqCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longEqCountList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longEqCountList.get(loopCount) != longValue.intValue())
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

  public void testLongGtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").GT(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Count() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longGtCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();     
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtCountList.get(loopCount) != longValue.intValue())
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

  public void testLongGtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").GE(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longGtEqCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtEqCountList.get(loopCount) != longValue.intValue())
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

  public void testLongLtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").LT(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtCountList.get(loopCount) != longValue.intValue())
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

  public void testLongLtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").LE(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtEqCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtEqCountList.get(loopCount) != longValue.intValue())
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
  
  public void testLongNotEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("countFuncLong").NE(F.COUNT(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("countFuncLong"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longNotEqCountList.get(loopCount) != Long.parseLong(businessDAO.getValue("countFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqCountAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.countQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getCountFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(F.COUNT(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getCountFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longNotEqCountList.get(loopCount) != longValue.intValue())
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


  public void testLongEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").EQ(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Eq than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longEqSumList.size(), sum);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {     
      if (AggregateFunctionMasterSetup.longEqSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longEqSumList.size(), sum);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longEqSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }

  public void testLongGtSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").GT(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();
    
    String errMsg = "Greater than Sum() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
 
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {     
      if (AggregateFunctionMasterSetup.longGtSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();     
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtSumList.size(), sum);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongGtEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").GE(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Greater equals than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longGtEqSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longGtEqSumList.size(), sum);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longGtEqSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongLtSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").LT(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Less than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
      
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongLtEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").LE(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Less than Eq SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longLtEqSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testLongLtEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longLtEqSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longLtEqSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testLongNotEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aLong("sumFuncLong").NE(F.SUM(query2.aLong("comFuncLong"))));
    query.ORDER_BY(query.aLong("sumFuncLong"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Not Eq SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.longNotEqSumList.get(loopSum) != Long.parseLong(businessDAO.getValue("sumFuncLong")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getSumFuncLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComFuncLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(F.SUM(comAttributeLong)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeLong, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.longNotEqSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Long longValue = (Long)objectClass.getMethod("getSumFuncLong").invoke(object);
        if (AggregateFunctionMasterSetup.longNotEqSumList.get(loopSum) != longValue.intValue())
        {
          fail("Query did not return the expected object.");
        }
        loopSum += 1;
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  
}
