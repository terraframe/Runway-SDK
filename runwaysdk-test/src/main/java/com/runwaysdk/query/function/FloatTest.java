/**
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Runway SDK(tm). If not, see <http://www.gnu.org/licenses/>.
 */
package com.runwaysdk.query.function;

import org.junit.Assert;
import org.junit.Test;

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
import com.runwaysdk.query.SelectableFloat;
import com.runwaysdk.session.Request;

public class FloatTest
{
  @Request
  @Test
  public void testFloatEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").EQ(F.MIN(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Eq MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, 1, count);

    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        Assert.fail("Query did not return the expected object.");
      }
    }
  }

  @Request
  @Test
  public void testFloatEqMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ(F.MIN(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Eq MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, 1, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          Assert.fail("Query did not return the expected object.");
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GT(F.MIN(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater than MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMinGtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMinGtList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatGtMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT(F.MIN(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.DESC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMinGtList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = AggregateFunctionMasterSetup.floatMinGtList.size() - 1;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMinGtList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount -= 1;
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GE(F.MIN(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater equals than MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMinGtEqList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }

  }

  @Request
  @Test
  public void testFloatGtEqMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(F.MIN(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.numOfObjects, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMinGtEqList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LT(F.MIN(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Less than MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, 0, count);
  }

  @Request
  @Test
  public void testFloatLtMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT(F.MIN(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LE(F.MIN(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Less than Equal MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, 1, count);

    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        Assert.fail("Query did not return the expected object.");
      }
    }

  }

  @Request
  @Test
  public void testFloatLtEqMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(F.MIN(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, 1, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(0);

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          Assert.fail("Query did not return the expected object.");
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").NE(F.MIN(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMinNotEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMinNotEqList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatNotEqMinAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(F.MIN(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMinNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMinNotEqList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").EQ(F.MAX(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, 1, count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(AggregateFunctionMasterSetup.classObjectList.size() - 1);

    for (BusinessDAOIF object : iterator)
    {
      if (!object.getId().equals(businessDAO.getId()))
      {
        Assert.fail("Query did not return the expected object.");
      }
    }
  }

  @Request
  @Test
  public void testFloatEqMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ(F.MAX(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MAX() check returned wrong result.";
      Assert.assertEquals(errMsg, 1, count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      BusinessDAO businessDAO = AggregateFunctionMasterSetup.classObjectList.get(AggregateFunctionMasterSetup.classObjectList.size() - 1);

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(businessDAO.getId()))
        {
          Assert.fail("Query did not return the expected object.");
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GT(F.MAX(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, 0, count);

  }

  @Request
  @Test
  public void testFloatGtMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT(F.MAX(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MAX() check returned wrong result.";
      Assert.assertEquals(errMsg, 0, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GE(F.MAX(query2.aFloat("comFuncFloat"))));

    long count = query.getCount();

    String errMsg = "Greater equals than MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, 1, count);
  }

  @Request
  @Test
  public void testFloatGtEqMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(F.MAX(comAttributeFloat)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MAX() check returned wrong result.";
      Assert.assertEquals(errMsg, 1, count);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LT(F.MAX(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxLtList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMaxLtList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT(F.MAX(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than MAX() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxLtList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMaxLtList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LE(F.MAX(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than Equal MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxLtEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMaxLtEqList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtEqMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(F.MAX(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxLtEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMaxLtEqList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").NE(F.MAX(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxNotEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatMaxNotEqList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatNotEqMaxAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(F.MAX(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatMaxNotEqList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEqAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").EQ(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Eq than AVG() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatEqAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatEqAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Eq than AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatEqAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GT(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater than Avg() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatGtAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }

  }

  @Request
  @Test
  public void testFloatGtAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatGtAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEqAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").GE(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater equals than AVG() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatGtEqAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }

  }

  @Request
  @Test
  public void testFloatGtEqAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater Eq than AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatGtEqAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LT(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than AVG() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatLtAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatLtAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEqAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").LE(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than Eq AVG() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatLtEqAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtEqAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatLtEqAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEqAvgAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("funcFloat").NE(F.AVG(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Eq AVG() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatNotEqAvgList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatNotEqAvgList.get(loopCount) != Float.parseFloat(businessDAO.getValue("funcFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatNotEqAvgAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(F.AVG(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Eq AVG() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatNotEqAvgList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatNotEqAvgList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatEqCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").EQ(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Eq than COUNT() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatEqCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatEqCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Eq than COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatEqCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").GT(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater than Count() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatGtCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }

  }

  @Request
  @Test
  public void testFloatGtCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatGtCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatGtEqCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").GE(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Greater equals than COUNT() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatGtEqCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }

  }

  @Request
  @Test
  public void testFloatGtEqCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater Eq than COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatGtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatGtEqCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").LT(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than COUNT() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatLtCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatLtCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatLtEqCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").LE(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Less than Eq COUNT() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatLtEqCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatLtEqCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Less than COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatLtEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatLtEqCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testFloatNotEqCountAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.countQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aFloat("countFuncFloat").NE(F.COUNT(query2.aFloat("comFuncFloat"))));
    query.ORDER_BY(query.aFloat("countFuncFloat"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Eq COUNT() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatNotEqCountList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.floatNotEqCountList.get(loopCount) != Float.parseFloat(businessDAO.getValue("countFuncFloat")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @Request
  @Test
  public void testFloatNotEqCountAttribute_Generated()
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
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getCountFuncFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(F.COUNT(comAttributeFloat)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeFloat, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Eq COUNT() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.floatNotEqCountList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        Float floatValue = (Float) objectClass.getMethod("getCountFuncFloat").invoke(object);
        if (AggregateFunctionMasterSetup.floatNotEqCountList.get(loopCount) != floatValue.floatValue())
        {
          Assert.fail("Query did not return the expected object.");
        }
        loopCount += 1;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  //
  // @Request @Test public void testFloatEqStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").EQ(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Eq than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatEqStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatEqStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatEqStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.EQ(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Eq than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatEqStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatEqStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatGtStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").gt(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Greater than StdDev() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatGtStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  //
  //
  // @Request @Test public void testFloatGtStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.gt(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Greater than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatGtStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatGtEqStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").gtEq(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Greater equals than STDDEV() check returned wrong
  // result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtEqStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatGtEqStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  //
  //
  // @Request @Test public void testFloatGtEqStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.gtEq(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Greater Eq than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtEqStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatGtEqStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatLtStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").lt(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Less than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatLtStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatLtStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.lt(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Less than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatLtStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatLtEqStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").ltEq(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Less than Eq STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtEqStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatLtEqStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatLtEqStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.ltEq(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Less than STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtEqStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatLtEqStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatNotEqStdDevAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").NE(new
  // StdDev(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Not Eq STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatNotEqStdDevList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatNotEqStdDevList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatNotEqStdDevAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.NE(new StdDev(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Not Eq STDDEV() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatNotEqStdDevList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatNotEqStdDevList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatEqVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").EQ(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Eq than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatEqVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatEqVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatEqVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.EQ(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Eq than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatEqVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatEqVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatGtVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").gt(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Greater than Variance() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatGtVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  //
  //
  // @Request @Test public void testFloatGtVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.gt(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Greater than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatGtVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatGtEqVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").gtEq(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Greater equals than VARIANCE() check returned wrong
  // result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtEqVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatGtEqVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  //
  //
  // @Request @Test public void testFloatGtEqVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.gtEq(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Greater Eq than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatGtEqVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatGtEqVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatLtVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").lt(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Less than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatLtVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatLtVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.lt(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Less than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatLtVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  //
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatLtEqVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").ltEq(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Less than Eq VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtEqVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatLtEqVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatLtEqVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.ltEq(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Less than VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatLtEqVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatLtEqVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }
  //
  // @Request @Test public void testFloatNotEqVarianceAttribute()
  // {
  // QueryFactory factory = new QueryFactory();
  // BusinessDAOQuery query =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
  // BusinessDAOQuery query2 =
  // factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
  // query.WHERE(query.aFloat("funcFloat").NE(new
  // Variance(query2.aFloat("comFuncFloat"))));
  // query.ORDER_BY(query.aFloat("funcFloat"), OrderBy.SortOrder.ASC);
  //
  // long count = query.getCount();
  //
  // String errMsg = "Not Eq VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatNotEqVarianceList.size(), count);
  //
  // ComponentIterator<BusinessDAOIF> iterator = query.getIterator();
  //
  // int loopCount = 0;
  // for (BusinessDAOIF businessDAO : iterator)
  // {
  // if (AggregateFunctionMasterSetup.floatNotEqVarianceList.get(loopCount) !=
  // Float.parseFloat(businessDAO.getValue("funcFloat")))
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  //
  //
  // @Request @Test public void testFloatNotEqVarianceAttribute_Generated()
  // {
  // try
  // {
  // String type = AggregateFunctionMasterSetup.classQueryInfo.getType();
  // Class<?> objectClass = GenerationFacade.load(type);
  // String queryType = EntityQueryAPIGenerator.getQueryClass(type);
  // Class<?> queryClass = GenerationFacade.load(queryType);
  //
  // String comType = AggregateFunctionMasterSetup.comQueryInfo.getType();
  // String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
  // Class<?> comQueryClass = GenerationFacade.load(comQueryType);
  //
  // QueryFactory factory = new QueryFactory();
  // Object queryObject =
  // queryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // Object comQueryObject =
  // comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
  // AttributeFloatIF attributeFloat =
  // (AttributeFloatIF)queryClass.getMethod("getFuncFloat").invoke(queryObject);
  // AttributeFloatIF comAttributeFloat =
  // (AttributeFloatIF)comQueryClass.getMethod("getComFuncFloat").invoke(comQueryObject);
  // queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,
  // attributeFloat.NE(new Variance(comAttributeFloat)));
  // queryClass.getMethod("ORDER_BY", AttributePrimitiveIF.class,
  // OrderBy.SortOrder.class).invoke(queryObject, attributeFloat,
  // OrderBy.SortOrder.ASC);
  //
  // long count =
  // ((Long)(queryClass.getMethod("getCount").invoke(queryObject))).longValue();
  //
  // String errMsg = "Not Eq VARIANCE() check returned wrong result.";
  // Assert.assertEquals(errMsg,
  // AggregateFunctionMasterSetup.floatNotEqVarianceList.size(), count);
  //
  // // Load the iterator class
  // Object resultIterator =
  // queryClass.getMethod(EntityQueryAPIGenerator.RESULT_SET_METHOD).invoke(queryObject);
  //
  // int loopCount = 0;
  // for (Object object : (Iterable<?>)resultIterator)
  // {
  // objectClass.cast(object);
  // Float floatValue =
  // (Float)objectClass.getMethod("getFuncFloat").invoke(object);
  // if (AggregateFunctionMasterSetup.floatNotEqVarianceList.get(loopCount) !=
  // floatValue.floatValue())
  // {
  // Assert.fail("Query did not return the expected object.");
  // }
  // loopCount += 1;
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }

}
