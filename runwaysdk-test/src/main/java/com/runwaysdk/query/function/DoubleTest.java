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
import com.runwaysdk.query.SelectableDouble;

public class DoubleTest  extends TestCase
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
    junit.textui.TestRunner.run(new AggregateFunctionMasterSetup(DoubleTest.suite()));
  }
  
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(DoubleTest.class);

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
  

  public void testDoubleEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").EQ(F.MIN(query2.aDouble("comFuncDouble"))));

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
  public void testDoubleEqMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(F.MIN(comAttributeDouble)));
    
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
  
  public void testDoubleGtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GT(F.MIN(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMinGtList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
  
  @SuppressWarnings("unchecked")
  public void testDoubleGtMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT(F.MIN(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.DESC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMinGtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = AggregateFunctionMasterSetup.doubleMinGtList.size() - 1;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMinGtList.get(loopCount) != doubleValue.doubleValue())
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
  
  public void testDoubleGtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GE(F.MIN(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMinGtEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
    
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtEqMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(F.MIN(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMinGtEqList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LT(F.MIN(query2.aDouble("comFuncDouble"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    assertEquals(errMsg, 0, count);
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT(F.MIN(comAttributeDouble)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MIN() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleLtEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LE(F.MIN(query2.aDouble("comFuncDouble"))));

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
  public void testDoubleLtEqMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(F.MIN(comAttributeDouble)));
    
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

  public void testDoubleNotEqMinAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").NE(F.MIN(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMinNotEqList.size(), count);
 
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMinNotEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleNotEqMinAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(F.MIN(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);
      
      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMinNotEqList.get(loopCount) != doubleValue.doubleValue())
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
  
  public void testDoubleEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").EQ(F.MAX(query2.aDouble("comFuncDouble"))));

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
  public void testDoubleEqMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(F.MAX(comAttributeDouble)));
    
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
  
  public void testDoubleGtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GT(F.MAX(query2.aDouble("comFuncDouble"))));

    long count = query.getCount();
    
    String errMsg = "Greater than MAX() check returned wrong result.";
    assertEquals(errMsg, 0, count);

  }
  
  @SuppressWarnings("unchecked")
  public void testDoubleGtMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT(F.MAX(comAttributeDouble)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleGtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GE(F.MAX(query2.aDouble("comFuncDouble"))));

    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    assertEquals(errMsg, 1, count);
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtEqMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(F.MAX(comAttributeDouble)));
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than MAX() check returned wrong result.";
      assertEquals(errMsg, 1, count);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleLtMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LT(F.MAX(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.doubleMaxLtList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMaxLtList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT(F.MAX(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than MAX() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMaxLtList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMaxLtList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LE(F.MAX(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    assertEquals(errMsg,  AggregateFunctionMasterSetup.doubleMaxLtEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMaxLtEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleLtEqMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(F.MAX(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMaxLtEqList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMaxLtEqList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleNotEqMaxAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").NE(F.MAX(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMaxNotEqList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleMaxNotEqList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleNotEqMaxAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(F.MAX(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
    
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg =  "Not Equal MIN() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleMaxNotEqList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").EQ(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleEqAvgList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testDoubleEqAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleEqAvgList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleEqAvgList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleGtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GT(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Avg() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleGtAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleGtAvgList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleGtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").GE(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleGtEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtEqAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleGtEqAvgList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LT(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleLtAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleLtAvgList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").LE(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleLtEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtEqAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleLtEqAvgList.get(loopCount) != doubleValue.doubleValue())
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
  
  public void testDoubleNotEqAvgAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("funcDouble").NE(F.AVG(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("funcDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq AVG() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleNotEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleNotEqAvgList.get(loopCount) != Double.parseDouble(businessDAO.getValue("funcDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testDoubleNotEqAvgAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(F.AVG(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq AVG() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleNotEqAvgList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleNotEqAvgList.get(loopCount) != doubleValue.doubleValue())
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


  public void testDoubleEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").EQ(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Eq than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleEqCountList.size(), count);
    
    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testDoubleEqCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleEqCountList.size(), count);    
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleEqCountList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleGtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").GT(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();
    
    String errMsg = "Greater than Count() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleGtCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleGtCountList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleGtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").GE(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Greater equals than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleGtEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
    
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtEqCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Greater Eq than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleGtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleGtEqCountList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").LT(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleLtCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleLtCountList.get(loopCount) != doubleValue.doubleValue())
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

  public void testDoubleLtEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").LE(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Less than Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleLtEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }
 
  @SuppressWarnings("unchecked")
  public void testDoubleLtEqCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Less than COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleLtEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleLtEqCountList.get(loopCount) != doubleValue.doubleValue())
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
  
  public void testDoubleNotEqCountAttribute()
  { 
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aDouble("countFuncDouble").NE(F.COUNT(query2.aDouble("comFuncDouble"))));
    query.ORDER_BY(query.aDouble("countFuncDouble"), OrderBy.SortOrder.ASC);
    
    long count = query.getCount();

    String errMsg = "Not Eq COUNT() check returned wrong result.";
    assertEquals(errMsg, AggregateFunctionMasterSetup.doubleNotEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();
    
    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.doubleNotEqCountList.get(loopCount) != Double.parseDouble(businessDAO.getValue("countFuncDouble")))
      {
        fail("Query did not return the expected object.");
      }
      loopCount += 1;
    } 
  }

  @SuppressWarnings("unchecked")
  public void testDoubleNotEqCountAttribute_Generated()
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
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getCountFuncDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComFuncDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(F.COUNT(comAttributeDouble)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeDouble, OrderBy.SortOrder.ASC);
      
      long count = ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
   
      String errMsg = "Not Eq COUNT() check returned wrong result.";
      assertEquals(errMsg, AggregateFunctionMasterSetup.doubleNotEqCountList.size(), count);
      
      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        Double doubleValue = (Double)objectClass.getMethod("getCountFuncDouble").invoke(object);
        if (AggregateFunctionMasterSetup.doubleNotEqCountList.get(loopCount) != doubleValue.doubleValue())
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
