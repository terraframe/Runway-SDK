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
import com.runwaysdk.query.SelectableInteger;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

public class IntegerTest  extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(IntegerTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(IntegerTest.class);

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
  

  public void testIntegerEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").EQ(F.MIN(query2.aInteger("comFuncInteger"))));

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

  
  public void testIntegerEqMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(F.MIN(comAttributeInteger)));
    
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
  
  public void testIntegerGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GT(F.MIN(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMinGtList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  
  public void testIntegerGtMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(F.MIN(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.DESC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = AggregateFunctionMasterSetup.integerMinGtList.size() - 1;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMinGtList.get(loopCount) != integerValue.intValue())
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
  
  public void testIntegerGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GE(F.MIN(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMinGtEqList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  
  public void testIntegerGtEqMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(F.MIN(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMinGtEqList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LT(F.MIN(query2.aInteger("comFuncInteger"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  
  public void testIntegerLtMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(F.MIN(comAttributeInteger)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testIntegerLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LE(F.MIN(query2.aInteger("comFuncInteger"))));

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

  
  public void testIntegerLtEqMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(F.MIN(comAttributeInteger)));
    
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

  public void testIntegerNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").NE(F.MIN(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMinNotEqList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testIntegerNotEqMinAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(F.MIN(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMinNotEqList.get(loopCount) != integerValue.intValue())
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
  
  public void testIntegerEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").EQ(F.MAX(query2.aInteger("comFuncInteger"))));

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

  
  public void testIntegerEqMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(F.MAX(comAttributeInteger)));
    
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
  
  public void testIntegerGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GT(F.MAX(query2.aInteger("comFuncInteger"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  
  public void testIntegerGtMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(F.MAX(comAttributeInteger)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testIntegerGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GE(F.MAX(query2.aInteger("comFuncInteger"))));

    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, 1, count);
  }

  
  public void testIntegerGtEqMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(F.MAX(comAttributeInteger)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testIntegerLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LT(F.MAX(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.integerMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMaxLtList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  
  public void testIntegerLtMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(F.MAX(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerMaxLtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMaxLtList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LE(F.MAX(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.integerMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMaxLtEqList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testIntegerLtEqMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(F.MAX(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMaxLtEqList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").NE(F.MAX(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerMaxNotEqList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  
  public void testIntegerNotEqMaxAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(F.MAX(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerMaxNotEqList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").EQ(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqAvgList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerEqAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testIntegerEqAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqAvgList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerEqAvgList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerGtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GT(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Avg() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerGtAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testIntegerGtAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtAvgList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerGtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").GE(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerGtEqAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testIntegerGtEqAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtEqAvgList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LT(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testIntegerLtAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtAvgList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").LE(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtEqAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testIntegerLtEqAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtEqAvgList.get(loopCount) != integerValue.intValue())
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
  
  public void testIntegerNotEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("funcInteger").NE(F.AVG(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("funcInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerNotEqAvgList.get(loopCount) != Integer.parseInt(businessDAO.getValue("funcInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testIntegerNotEqAvgAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(F.AVG(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerNotEqAvgList.get(loopCount) != integerValue.intValue())
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


  public void testIntegerEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").EQ(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqCountList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerEqCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testIntegerEqCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqCountList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerEqCountList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerGtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").GT(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Count() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerGtCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testIntegerGtCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtCountList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerGtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").GE(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerGtEqCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  
  public void testIntegerGtEqCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtEqCountList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").LT(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testIntegerLtCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtCountList.get(loopCount) != integerValue.intValue())
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

  public void testIntegerLtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").LE(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtEqCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  
  public void testIntegerLtEqCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtEqCountList.get(loopCount) != integerValue.intValue())
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
  
  public void testIntegerNotEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("countFuncInteger").NE(F.COUNT(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("countFuncInteger"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerNotEqCountList.get(loopCount) != Integer.parseInt(businessDAO.getValue("countFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  
  public void testIntegerNotEqCountAttribute_Generated()
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
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getCountFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(F.COUNT(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getCountFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerNotEqCountList.get(loopCount) != integerValue.intValue())
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


  public void testIntegerEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").EQ(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Eq than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqSumList.size(), sum);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {     
      if (AggregateFunctionMasterSetup.integerEqSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }

  
  public void testIntegerEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerEqSumList.size(), sum);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerEqSumList.get(loopSum) != integerValue.intValue())
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

  public void testIntegerGtSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").GT(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();
    
    String errMsg = "Greater than Sum() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
 
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {     
      if (AggregateFunctionMasterSetup.integerGtSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
    
  }

  
  public void testIntegerGtSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();     
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtSumList.size(), sum);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtSumList.get(loopSum) != integerValue.intValue())
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

  public void testIntegerGtEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").GE(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Greater equals than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerGtEqSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
    
  }

  
  public void testIntegerGtEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerGtEqSumList.size(), sum);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerGtEqSumList.get(loopSum) != integerValue.intValue())
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

  public void testIntegerLtSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").LT(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Less than SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }
 
  
  public void testIntegerLtSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtSumList.get(loopSum) != integerValue.intValue())
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

  public void testIntegerLtEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").LE(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Less than Eq SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerLtEqSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }
 
  
  public void testIntegerLtEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerLtEqSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerLtEqSumList.get(loopSum) != integerValue.intValue())
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
  
  public void testIntegerNotEqSumAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.sumQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aInteger("sumFuncInteger").NE(F.SUM(query2.aInteger("comFuncInteger"))));
    query.ORDER_BY(query.aInteger("sumFuncInteger"), OrderBy.SortOrder.ASC);
    
    long sum = query.getCount();

    String errMsg = "Not Eq SUM() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqSumList.size(), sum);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopSum = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.integerNotEqSumList.get(loopSum) != Integer.parseInt(businessDAO.getValue("sumFuncInteger")))
      {
        fail("Query did not return the expected object.");
      }
      loopSum += 1;
    } 
  }

  
  public void testIntegerNotEqSumAttribute_Generated()
  { 
    try
    {
      String type = AggregateFunctionMasterSetup.sumQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type); 
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);  
      Class<?> queryClass = LoaderDecorator.load(queryType);
    
      String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);  
      Class<?> comQueryClass = LoaderDecorator.load(comQueryType);
    
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getSumFuncInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComFuncInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(F.SUM(comAttributeInteger)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeInteger, OrderBy.SortOrder.ASC);
      
      long sum = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq SUM() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.integerNotEqSumList.size(), sum);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopSum = 0;
      for (Object object : (Iterable<?>)resultIterator)
      {
        objectClass.cast(object);
        Integer integerValue = (Integer)objectClass.getMethod("getSumFuncInteger").invoke(object);
        if (AggregateFunctionMasterSetup.integerNotEqSumList.get(loopSum) != integerValue.intValue())
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
