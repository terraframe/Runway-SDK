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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.Condition;
import com.runwaysdk.query.F;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.OrderBy;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.Selectable;
import com.runwaysdk.query.SelectableBoolean;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class BooleanTest
{
  @Request
  @Test
  public void testIntegerEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aBoolean("funcBoolean").EQ(F.MIN(query2.aBoolean("comFuncBoolean"))));
    query.ORDER_BY(query.aBoolean("funcBoolean"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Eq MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMinEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.booleanMinEqList.get(loopCount) != Boolean.parseBoolean(businessDAO.getValue("funcBoolean")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testIntegerEqMinAttribute_Generated()
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
      SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getFuncBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean) comQueryClass.getMethod("getComFuncBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(F.MIN(comAttributeBoolean)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeBoolean, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Eq MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMinEqList.size(), count);

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        Boolean booleanValue = (Boolean) objectClass.getMethod("getFuncBoolean").invoke(object);
        if (AggregateFunctionMasterSetup.booleanMinEqList.get(loopCount) != booleanValue.booleanValue())
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
  public void testIntegerNotEqMinAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aBoolean("funcBoolean").NE(F.MIN(query2.aBoolean("comFuncBoolean"))));
    query.ORDER_BY(query.aBoolean("funcBoolean"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMinNotEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.booleanMinNotEqList.get(loopCount) != Boolean.parseBoolean(businessDAO.getValue("funcBoolean")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testIntegerNotEqMinAttribute_Generated()
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
      SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getFuncBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean) comQueryClass.getMethod("getComFuncBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(F.MIN(comAttributeBoolean)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeBoolean, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMinNotEqList.size(), count);

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        Boolean booleanValue = (Boolean) objectClass.getMethod("getFuncBoolean").invoke(object);
        if (AggregateFunctionMasterSetup.booleanMinNotEqList.get(loopCount) != booleanValue.booleanValue())
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
  public void testIntegerEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aBoolean("funcBoolean").EQ(F.MAX(query2.aBoolean("comFuncBoolean"))));

    long count = query.getCount();

    String errMsg = "Greater than MAX() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMaxEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.booleanMaxEqList.get(loopCount) != Boolean.parseBoolean(businessDAO.getValue("funcBoolean")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testIntegerEqMaxAttribute_Generated()
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
      SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getFuncBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean) comQueryClass.getMethod("getComFuncBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(F.MAX(comAttributeBoolean)));

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Greater than MAX() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMaxEqList.size(), count);

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        Boolean booleanValue = (Boolean) objectClass.getMethod("getFuncBoolean").invoke(object);
        if (AggregateFunctionMasterSetup.booleanMaxEqList.get(loopCount) != booleanValue.booleanValue())
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
  public void testIntegerNotEqMaxAttribute()
  {
    QueryFactory factory = new QueryFactory();
    BusinessDAOQuery query = factory.businessDAOQuery(AggregateFunctionMasterSetup.classQueryInfo.getType());
    BusinessDAOQuery query2 = factory.businessDAOQuery(AggregateFunctionMasterSetup.comQueryInfo.getType());
    query.WHERE(query.aBoolean("funcBoolean").NE(F.MAX(query2.aBoolean("comFuncBoolean"))));
    query.ORDER_BY(query.aBoolean("funcBoolean"), OrderBy.SortOrder.ASC);

    long count = query.getCount();

    String errMsg = "Not Equal MIN() check returned wrong result.";
    Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMaxNotEqList.size(), count);

    OIterator<BusinessDAOIF> iterator = query.getIterator();

    int loopCount = 0;
    for (BusinessDAOIF businessDAO : iterator)
    {
      if (AggregateFunctionMasterSetup.booleanMaxNotEqList.get(loopCount) != Boolean.parseBoolean(businessDAO.getValue("funcBoolean")))
      {
        Assert.fail("Query did not return the expected object.");
      }
      loopCount += 1;
    }
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testIntegerNotEqMaxAttribute_Generated()
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
      SelectableBoolean attributeBoolean = (SelectableBoolean) queryClass.getMethod("getFuncBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean) comQueryClass.getMethod("getComFuncBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(F.MAX(comAttributeBoolean)));
      queryClass.getMethod("ORDER_BY", Selectable.class, OrderBy.SortOrder.class).invoke(queryObject, attributeBoolean, OrderBy.SortOrder.ASC);

      long count = ( (Long) ( queryClass.getMethod("getCount").invoke(queryObject) ) ).longValue();

      String errMsg = "Not Equal MIN() check returned wrong result.";
      Assert.assertEquals(errMsg, AggregateFunctionMasterSetup.booleanMaxNotEqList.size(), count);

      // Load the iterator class
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      int loopCount = 0;
      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        Boolean booleanValue = (Boolean) objectClass.getMethod("getFuncBoolean").invoke(object);
        if (AggregateFunctionMasterSetup.booleanMaxNotEqList.get(loopCount) != booleanValue.booleanValue())
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

}
