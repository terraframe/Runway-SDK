/*******************************************************************************
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
 ******************************************************************************/
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
import com.runwaysdk.query.SelectablePrimitive;

public class DateTest extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(DateTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DateTest.class);

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
  

  public void testDateEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").EQ(F.MIN(query2.aDate("comFuncDate"))));

    long count = query.getCount();

    String errMsg = "Eq MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMinEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateEqMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(F.MIN(comAttributeDate)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMinEqList.get(loopCount) != date.getTime())
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
  
  public void testDateGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").GT(F.MIN(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMinGtList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  @SuppressWarnings("unchecked")
  public void testDateGtMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(F.MIN(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMinGtList.get(loopCount) != date.getTime())
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
  
  public void testDateGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").GE(F.MIN(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMinGtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testDateGtEqMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(F.MIN(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMinGtEqList.get(loopCount) != date.getTime())
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

  public void testDateLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").LT(F.MIN(query2.aDate("comFuncDate"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  @SuppressWarnings("unchecked")
  public void testDateLtMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(F.MIN(comAttributeDate)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").LE(F.MIN(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMinLtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testDateLtEqMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(F.MIN(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMinGtEqList.get(loopCount) != date.getTime())
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

  public void testDateNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").NE(F.MIN(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMinNotEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateNotEqMinAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(F.MIN(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMinNotEqList.get(loopCount) != date.getTime())
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
  
  public void testDateEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").EQ(F.MAX(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMaxEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateEqMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(F.MAX(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMaxEqList.get(loopCount) != date.getTime())
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
  
  public void testDateGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").GT(F.MAX(query2.aDate("comFuncDate"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  @SuppressWarnings("unchecked")
  public void testDateGtMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(F.MAX(comAttributeDate)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").GE(F.MAX(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxGtEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMaxGtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateGtEqMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(F.MAX(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxGtEqList.size(), count);
      
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMaxGtEqList.get(loopCount) != date.getTime())
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

  public void testDateLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").LT(F.MAX(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.dateMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMaxLtList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  @SuppressWarnings("unchecked")
  public void testDateLtMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(F.MAX(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxLtList.size(), count);

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMaxLtList.get(loopCount) != date.getTime())
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

  public void testDateLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").LE(F.MAX(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.dateMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMaxLtEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateLtEqMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(F.MAX(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMaxLtEqList.get(loopCount) != date.getTime())
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

  public void testDateNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDate("funcDate").NE(F.MAX(query2.aDate("comFuncDate"))));
    query.ORDER_BY(query.aDate("funcDate"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse(businessDAO.getValue("funcDate"),  new java.text.ParsePosition(0));
      if (AggregateFunctionMasterSetup.dateMaxNotEqList.get(loopCount) != date.getTime())
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateNotEqMaxAttribute_Generated()
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
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getFuncDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComFuncDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(F.MAX(comAttributeDate)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDate, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.dateMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Date date = (Date)objectClass.getMethod("getFuncDate").invoke(object);
        if (AggregateFunctionMasterSetup.dateMaxNotEqList.get(loopCount) != date.getTime())
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
