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

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableMoment;

public class DateTimeTest extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(DateTimeTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DateTimeTest.class);

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
  

  public void testDateTimeEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").EQ(F.MIN(query2.aDateTime("comFuncDateTime"))));

    long count = query.getCount();

    String errMsg = "Eq MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMinEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeEqMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(F.MIN(comAttributeDateTime)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMinEqList.get(loopCount) != date.getTime())
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
  
  public void testDateTimeGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").GT(F.MIN(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMinGtList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  @SuppressWarnings("unchecked")
  public void testDateTimeGtMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(F.MIN(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMinGtList.get(loopCount) != date.getTime())
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
  
  public void testDateTimeGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").GE(F.MIN(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMinGtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeGtEqMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(F.MIN(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMinGtEqList.get(loopCount) != date.getTime())
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

  public void testDateTimeLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").LT(F.MIN(query2.aDateTime("comFuncDateTime"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  @SuppressWarnings("unchecked")
  public void testDateTimeLtMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LT(F.MIN(comAttributeDateTime)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateTimeLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").LE(F.MIN(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMinLtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeLtEqMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(F.MIN(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMinGtEqList.get(loopCount) != date.getTime())
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

  public void testDateTimeNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").NE(F.MIN(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMinNotEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeNotEqMinAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(F.MIN(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMinNotEqList.get(loopCount) != date.getTime())
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
  
  public void testDateTimeEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").EQ(F.MAX(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMaxEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeEqMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(F.MAX(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMaxEqList.get(loopCount) != date.getTime())
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
  
  public void testDateTimeGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").GT(F.MAX(query2.aDateTime("comFuncDateTime"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  @SuppressWarnings("unchecked")
  public void testDateTimeGtMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(F.MAX(comAttributeDateTime)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateTimeGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").GE(F.MAX(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxGtEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMaxGtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeGtEqMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(F.MAX(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxGtEqList.size(), count);
      
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMaxGtEqList.get(loopCount) != date.getTime())
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

  public void testDateTimeLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").LT(F.MAX(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.dateTimeMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMaxLtList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  @SuppressWarnings("unchecked")
  public void testDateTimeLtMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LT(F.MAX(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxLtList.size(), count);

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMaxLtList.get(loopCount) != date.getTime())
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

  public void testDateTimeLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").LE(F.MAX(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.dateTimeMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMaxLtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeLtEqMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(F.MAX(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMaxLtEqList.get(loopCount) != date.getTime())
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

  public void testDateTimeNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDateTime("funcDateTime").NE(F.MAX(query2.aDateTime("comFuncDateTime"))));
    query.ORDER_BY(query.aDateTime("funcDateTime"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse(businessDAO.getValue("funcDateTime"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateTimeMaxNotEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeNotEqMaxAttribute_Generated()
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
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getFuncDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComFuncDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(F.MAX(comAttributeDateTime)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDateTime, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateTimeMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDateTime").invoke(object);
        if (AggregateFunctionMasterSetup.dateTimeMaxNotEqList.get(loopCount) != date.getTime())
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
