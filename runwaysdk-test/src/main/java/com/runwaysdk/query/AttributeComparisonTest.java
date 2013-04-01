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
package com.runwaysdk.query;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class AttributeComparisonTest extends TestCase
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
    junit.textui.TestRunner.run(new QueryMasterSetup(AttributeComparisonTest.suite(), QueryMasterSetup.parentQueryInfo.getType(), QueryMasterSetup.parentRefQueryInfo.getType()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(AttributeComparisonTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() { }

      protected void tearDown() { }
    };

    return wrapper;
  }

  public void testBooleanEqAttributeBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(query2.aBoolean("refQueryBoolean")));

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
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aBoolean("queryBoolean").EQ(query2.aBoolean("comBoolean")));

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

  @SuppressWarnings("unchecked")
  public void testBooleanEqAttributeBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableBoolean attributeBoolean = (SelectableBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      SelectableBoolean refAttributeBoolean = (SelectableBoolean)refQueryClass.getMethod("getRefQueryBoolean").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(refAttributeBoolean));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean)comQueryClass.getMethod("getComBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(comAttributeBoolean));

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

  public void testBooleanNotEqAttributeBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aBoolean("queryBoolean").NE(query2.aBoolean("comBoolean")));

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
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aBoolean("queryBoolean").NE(query2.aBoolean("refQueryBoolean")));

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

  @SuppressWarnings("unchecked")
  public void testBooleanNotEqAttributeBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      SelectableBoolean comAttributeBoolean = (SelectableBoolean)comQueryClass.getMethod("getComBoolean").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(comAttributeBoolean));

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Class iteratorClass = OIterator.class;
      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute boolean values are incorrect.");
        }
      }


      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeBoolean = (SelectableBoolean)queryClass.getMethod("getQueryBoolean").invoke(queryObject);
      SelectableBoolean refAttributeBoolean = (SelectableBoolean)refQueryClass.getMethod("getRefQueryBoolean").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(refAttributeBoolean));

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

  public void testCharacterEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").EQ(query2.aCharacter("comCharacter")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").EQ(query2.aCharacter("refQueryCharacter")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testCharacterEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar comAttributeCharacter = (SelectableChar)comQueryClass.getMethod("getComCharacter").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ(comAttributeCharacter));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar refAttributeCharacter = (SelectableChar)refQueryClass.getMethod("getRefQueryCharacter").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ(refAttributeCharacter));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testCharacterEqIgnoreCaseAttribute()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comCharacter", "SOME CHARACTER VALUE");
    QueryMasterSetup.compareQueryObject.apply();
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").EQi(query2.aCharacter("comCharacter")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").EQi(query2.aCharacter("refQueryCharacter")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comCharacter", "some character value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testCharacterEqIgnoreCaseAttribute_Generated()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comCharacter", "SOME CHARACTER VALUE");
    QueryMasterSetup.compareQueryObject.apply();
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar comAttributeCharacter = (SelectableChar)comQueryClass.getMethod("getComCharacter").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi(comAttributeCharacter));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar refAttributeCharacter = (SelectableChar)refQueryClass.getMethod("getRefQueryCharacter").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi(refAttributeCharacter));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comCharacter", "some character value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testCharacterNotEqAttribute()
  {
   try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").NE(query2.aCharacter("refQueryCharacter")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").NE(query2.aCharacter("comCharacter")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  @SuppressWarnings("unchecked")
  public void testCharacterNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableChar attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar refAttributeCharacter = (SelectableChar)refQueryClass.getMethod("getRefQueryCharacter").invoke(refQueryObject);

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE(refAttributeCharacter));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar comAttributeCharacter = (SelectableChar)comQueryClass.getMethod("getComCharacter").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE(comAttributeCharacter));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testCharacterNotEqIgnoreCaseAttribute()
  {
   try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").NEi(query2.aCharacter("refQueryCharacter")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aCharacter("queryCharacter").NEi(query2.aCharacter("comCharacter")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
  @SuppressWarnings("unchecked")
  public void testCharacterNotEqIgnoreCaseAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableChar attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar refAttributeCharacter = (SelectableChar)refQueryClass.getMethod("getRefQueryCharacter").invoke(refQueryObject);

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi(refAttributeCharacter));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (SelectableChar)queryClass.getMethod("getQueryCharacter").invoke(queryObject);
      SelectableChar comAttributeCharacter = (SelectableChar)comQueryClass.getMethod("getComCharacter").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi(comAttributeCharacter));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testTextEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aText("queryText").EQ(query2.aText("comText")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aText("queryText").EQ(query2.aText("refQueryText")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testClobEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").EQ(query2.aClob("comClob")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").EQ(query2.aClob("refQueryClob")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testTextEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar comAttributeText = (SelectableChar)comQueryClass.getMethod("getComText").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ(comAttributeText));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar refAttributeText = (SelectableChar)refQueryClass.getMethod("getRefQueryText").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ(refAttributeText));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testClobEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar comAttributeClob = (SelectableChar)comQueryClass.getMethod("getComClob").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ(comAttributeClob));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Clob values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar refAttributeClob = (SelectableChar)refQueryClass.getMethod("getRefQueryClob").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ(refAttributeClob));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testTextNotEqAttribute()
  {
    String originalValue = QueryMasterSetup.compareQueryObject.getValue("comText");

    QueryMasterSetup.compareQueryObject.setValue("comText", "some text value");
    QueryMasterSetup.compareQueryObject.apply();

   try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aText("queryText").NE(query2.aText("refQueryText")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aText("queryText").NE(query2.aText("comText")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // uppercase the current value for this ignore case test
      QueryMasterSetup.compareQueryObject.setValue("comText", originalValue);
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testClobNotEqAttribute()
  {
    String originalValue = QueryMasterSetup.compareQueryObject.getValue("comClob");

    QueryMasterSetup.compareQueryObject.setValue("comClob", "some clob value");
    QueryMasterSetup.compareQueryObject.apply();

   try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").NE(query2.aClob("refQueryClob")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").NE(query2.aClob("comClob")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // uppercase the current value for this ignore case test
      QueryMasterSetup.compareQueryObject.setValue("comClob", originalValue);
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testTextNotEqAttribute_Generated()
  {
    String originalValue = QueryMasterSetup.compareQueryObject.getValue("comText");

    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comText", "some text value");
    QueryMasterSetup.compareQueryObject.apply();

   try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableChar attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar refAttributeText = (SelectableChar)refQueryClass.getMethod("getRefQueryText").invoke(refQueryObject);

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE(refAttributeText));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar comAttributeText = (SelectableChar)comQueryClass.getMethod("getComText").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE(comAttributeText));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // uppercase the current value for this ignore case test
      QueryMasterSetup.compareQueryObject.setValue("comText", originalValue);
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testClobNotEqAttribute_Generated()
  {
    String originalValue = QueryMasterSetup.compareQueryObject.getValue("comClob");

    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comClob", "some clob value");
    QueryMasterSetup.compareQueryObject.apply();

   try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableChar attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar refAttributeClob = (SelectableChar)refQueryClass.getMethod("getRefQueryClob").invoke(refQueryObject);

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE(refAttributeClob));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar comAttributeClob = (SelectableChar)comQueryClass.getMethod("getComClob").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE(comAttributeClob));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // uppercase the current value for this ignore case test
      QueryMasterSetup.compareQueryObject.setValue("comClob", originalValue);
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testTextEqIgnoreCaseAttribute()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comText", "SOME TEXT VALUE");
    QueryMasterSetup.compareQueryObject.apply();
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aText("queryText").EQi(query2.aText("comText")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aText("queryText").EQi(query2.aText("refQueryText")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comText", "some text value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testClobEqIgnoreCaseAttribute()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comClob", "SOME CLOB VALUE");
    QueryMasterSetup.compareQueryObject.apply();
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").EQi(query2.aClob("comClob")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aClob("queryClob").EQi(query2.aClob("refQueryClob")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comClob", "some clob value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }


  @SuppressWarnings("unchecked")
  public void testTextEqIgnoreCaseAttribute_Generated()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comText", "SOME TEXT VALUE");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar comAttributeText = (SelectableChar)comQueryClass.getMethod("getComText").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi(comAttributeText));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on text values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeText = (SelectableChar)queryClass.getMethod("getQueryText").invoke(queryObject);
      SelectableChar refAttributeText = (SelectableChar)refQueryClass.getMethod("getRefQueryText").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi(refAttributeText));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comText", "some text value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testClobEqIgnoreCaseAttribute_Generated()
  {
    // uppercase the current value for this ignore case test
    QueryMasterSetup.compareQueryObject.setValue("comClob", "SOME CLOB VALUE");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar comAttributeClob = (SelectableChar)comQueryClass.getMethod("getComClob").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi(comAttributeClob));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Clob values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeClob = (SelectableChar)queryClass.getMethod("getQueryClob").invoke(queryObject);
      SelectableChar refAttributeClob = (SelectableChar)refQueryClass.getMethod("getRefQueryClob").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi(refAttributeClob));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      // now lowercase the value back to what it was
      QueryMasterSetup.compareQueryObject.setValue("comClob", "some clob value");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateTimeEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").EQ(query2.aDateTime("comDateTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").EQ(query2.aDateTime("refQueryDateTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(comAttributeDateTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment refAttributeDateTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryDateTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.EQ(refAttributeDateTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateTimeGtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT(query2.aDateTime("refQueryDateTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query3 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT(query3.aDateTime("comDateTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeGtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment refAttributeDateTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryDateTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(refAttributeDateTime));


      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);


      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(comAttributeDateTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateTimeGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GE(query2.aDateTime("refQueryDateTime")));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on equals
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query3 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GE(query3.aDateTime("comDateTime")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2007-12-05 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query3 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GE(query3.aDateTime("comDateTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeGtEqAttribute_Generated()
  {

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // test greater than
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment refAttributeDateTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryDateTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(refAttributeDateTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      // Test Equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GE(comAttributeDateTime));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2007-12-05 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();


      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.GT(comAttributeDateTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateTimeLtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-07 13:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").LT(query2.aDateTime("comDateTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query2.WHERE(query2.aDateTime("comDateTime").LT(query.aDateTime("queryDateTime")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeLtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-07 13:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LT(comAttributeDateTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      comQueryClass.getMethod("WHERE", Condition.class).invoke(comQueryObject, comAttributeDateTime.LT(attributeDateTime));

      resultIterator  = comQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(comQueryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateTimeLtEqAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-07 13:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").LE(query2.aDateTime("comDateTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-05 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").LE(query2.aDateTime("comDateTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeLtEqAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-07 13:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(comAttributeDateTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-05 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.LE(comAttributeDateTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateTimeNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").NE(query2.aDateTime("refQueryDateTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").NE(query2.aDateTime("comDateTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateTimeNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment refAttributeDateTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryDateTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(refAttributeDateTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on datetime values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDateTime = (SelectableMoment)queryClass.getMethod("getQueryDateTime").invoke(queryObject);
      SelectableMoment comAttributeDateTime = (SelectableMoment)comQueryClass.getMethod("getComDateTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDateTime.NE(comAttributeDateTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").EQ(query2.aDate("comDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").EQ(query2.aDate("refQueryDate")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(comAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment refAttributeDate = (SelectableMoment)refQueryClass.getMethod("getRefQueryDate").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(refAttributeDate));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateGtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").GT(query2.aDate("refQueryDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query3 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").GT(query3.aDate("comDate")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateGtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment refAttributeDate = (SelectableMoment)refQueryClass.getMethod("getRefQueryDate").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(refAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(comAttributeDate));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDateGtEqAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-01");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").GE(query2.aDate("comDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query2.WHERE(query2.aDate("comDate").GE(query.aDate("queryDate")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateGtEqAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-01");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(comAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      comQueryClass.getMethod("WHERE", Condition.class).invoke(comQueryObject, comAttributeDate.GE(attributeDate));

      resultIterator  = comQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(comQueryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateLtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-07");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").LT(query2.aDate("comDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query2.WHERE(query2.aDate("comDate").LT(query.aDate("queryDate")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateLtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-07");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(comAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      comQueryClass.getMethod("WHERE", Condition.class).invoke(comQueryObject, comAttributeDate.LT(attributeDate));

      resultIterator  = comQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(comQueryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDateLtEqAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-07");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").LE(query2.aDate("comDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on equals
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").LE(query2.aDate("comDate")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-05");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").LE(query2.aDate("comDate")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateLtEqAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-07");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on less than
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(comAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();


      // perform a query that WILL find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(comAttributeDate));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-05");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(comAttributeDate));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDate", "2006-12-06");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }


  public void testDateNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").NE(query2.aDate("refQueryDate")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDate("queryDate").NE(query2.aDate("comDate")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDateNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment refAttributeDate = (SelectableMoment)refQueryClass.getMethod("getRefQueryDate").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(refAttributeDate));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on date values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDate = (SelectableMoment)queryClass.getMethod("getQueryDate").invoke(queryObject);
      SelectableMoment comAttributeDate = (SelectableMoment)comQueryClass.getMethod("getComDate").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(comAttributeDate));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testTimeEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").EQ(query2.aTime("comTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").EQ(query2.aTime("refQueryTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeEqAttribute_Generatted()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.EQ(comAttributeTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.EQ(refAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testTimeGtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").GT(query2.aTime("refQueryTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").GT(query2.aTime("comTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GT(refAttributeTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GT(comAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testTimeGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").GE(query2.aTime("refQueryTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on equals
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").GE(query2.aTime("comTime")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute date values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").GE(query2.aTime("comTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(refAttributeTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(comAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.GE(comAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testTimeLtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").LT(query2.aTime("comTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").LT(query2.aTime("refQueryTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeLtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LT(comAttributeTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LT(refAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testTimeLtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").LE(query2.aTime("comTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").LE(query2.aTime("comTime")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").LE(query2.aTime("refQueryTime")));


      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeLtEqAttribute_Grenerated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();
      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LE(comAttributeTime));

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comTime", "14:00:00");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LE(comAttributeTime));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.LT(refAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comTime", "13:00:00");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testTimeNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").NE(query2.aTime("refQueryTime")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aTime("queryTime").NE(query2.aTime("comTime")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testTimeNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment refAttributeTime = (SelectableMoment)refQueryClass.getMethod("getRefQueryTime").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.NE(refAttributeTime));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeTime = (SelectableMoment)queryClass.getMethod("getQueryTime").invoke(queryObject);
      SelectableMoment comAttributeTime = (SelectableMoment)comQueryClass.getMethod("getComTime").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeTime.NE(comAttributeTime));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testIntegerEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").EQ(query2.aInteger("comInteger")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").EQ(query2.aInteger("refQueryInteger")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testIntegerEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(comAttributeInteger));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(refAttributeInteger));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testIntegerGtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aInteger("refQueryInteger").GT(query2.aInteger("comInteger")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.childRefQueryObject.getId()) &&
            !object.getId().equals(QueryMasterSetup.childRefQueryObject2.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").GT(query2.aInteger("refQueryInteger")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testIntegerGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comInteger", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      Class refClass = LoaderDecorator.load(refType);
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComInteger").invoke(comQueryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeInteger.GT(comAttributeInteger));

      Class iteratorClass = OIterator.class;
      Object resultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        refClass.cast(object);
        String objectId = (String)refClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.childRefQueryObject.getId()) &&
            !objectId.equals(QueryMasterSetup.childRefQueryObject2.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }


      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(refAttributeInteger));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comInteger", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testIntegerGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").GE(query2.aInteger("comInteger")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comInteger", "95");
      QueryMasterSetup.compareQueryObject.apply();

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").GE(query2.aInteger("comInteger")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").GE(query2.aInteger("refQueryInteger")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comInteger", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testIntegerGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(comAttributeInteger));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comInteger", "95");
      QueryMasterSetup.compareQueryObject.apply();

      // Find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(comAttributeInteger));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(refAttributeInteger));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comInteger", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }


  public void testIntegerLtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").LE(query2.aInteger("refQueryInteger")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "100");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on equals
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").LE(query2.aInteger("refQueryInteger")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "200");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aInteger("refQueryInteger").LE(query.aInteger("queryInteger")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "200");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testIntegerLtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on less than
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(refAttributeInteger));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "200");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(refAttributeInteger));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeInteger.LE(attributeInteger));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryInteger", "200");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  public void testIntegerNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").NE(query2.aInteger("refQueryInteger")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aInteger("queryInteger").NE(query2.aInteger("comInteger")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testIntegerNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger refAttributeInteger = (SelectableInteger)refQueryClass.getMethod("getRefQueryInteger").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(refAttributeInteger));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on integer values are incorrect.");
        }
      }

      // Find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger)queryClass.getMethod("getQueryInteger").invoke(queryObject);
      SelectableInteger comAttributeInteger = (SelectableInteger)comQueryClass.getMethod("getComInteger").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(comAttributeInteger));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").EQ(query2.aLong("comLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").EQ(query2.aLong("refQueryLong")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(comAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ(refAttributeLong));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testLongGtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comLong", "95");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").GT(query2.aLong("comLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").GT(query2.aLong("refQueryLong")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comLong", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comLong", "95");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(comAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT(refAttributeLong));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comLong", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testLongGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").GE(query2.aLong("comLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comLong", "95");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").GE(query2.aLong("comLong")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").GE(query2.aLong("refQueryLong")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comLong", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(comAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comLong", "95");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(comAttributeLong));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE(refAttributeLong));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comLong", "100");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testLongLtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").LT(query2.aLong("refQueryLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").LT(query2.aLong("comLong")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongLtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(refAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT(comAttributeLong));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testLongLtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").LE(query2.aLong("refQueryLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "100");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on equals
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").LE(query2.aLong("refQueryLong")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "200");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aLong("refQueryLong").LE(query.aLong("queryLong")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "200");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongLtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(refAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "100");
      QueryMasterSetup.childRefQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE(refAttributeLong));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "200");
      QueryMasterSetup.childRefQueryObject.apply();

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeLong.LE(attributeLong));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryLong", "200");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  public void testLongNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").NE(query2.aLong("refQueryLong")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aLong("queryLong").NE(query2.aLong("comLong")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testLongNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong refAttributeLong = (SelectableLong)refQueryClass.getMethod("getRefQueryLong").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(refAttributeLong));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      // Find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong)queryClass.getMethod("getQueryLong").invoke(queryObject);
      SelectableLong comAttributeLong = (SelectableLong)comQueryClass.getMethod("getComLong").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE(comAttributeLong));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testFloatEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").EQ(query2.aFloat("comFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").EQ(query2.aFloat("refQueryFloat")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ(comAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat) refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeFloat.EQ(refAttributeFloat));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testFloatGtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comFloat", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").GT(query2.aFloat("comFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").GT(query2.aFloat("refQueryFloat")));;

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comFloat", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat) comQueryClass.getMethod("getComFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeFloat.GT(comAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat) refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeFloat.GT(refAttributeFloat));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testFloatGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").GE(query2.aFloat("comFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on greater than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").GE(query2.aFloat("comFloat")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").GE(query2.aFloat("refQueryFloat")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat)comQueryClass.getMethod("getComFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(comAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      comAttributeFloat = (SelectableFloat)comQueryClass.getMethod("getComFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(comAttributeFloat));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE(refAttributeFloat));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comFloat", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testFloatLtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").LT(query2.aFloat("refQueryFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aFloat("refQueryFloat").LT(query.aFloat("queryFloat")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatLtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT(refAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeFloat.LT(attributeFloat));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testFloatLtEqAttribute()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").LE(query2.aFloat("refQueryFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on less than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").LE(query2.aFloat("refQueryFloat")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aFloat("refQueryFloat").LE(query.aFloat("queryFloat")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatLtEqAttribute_Generated()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(refAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE(refAttributeFloat));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeFloat.LE(attributeFloat));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryFloat", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  public void testFloatNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").NE(query2.aFloat("refQueryFloat")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aFloat("queryFloat").NE(query2.aFloat("comFloat")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testFloatNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat refAttributeFloat = (SelectableFloat)refQueryClass.getMethod("getRefQueryFloat").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(refAttributeFloat));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Float values are incorrect.");
        }
      }

      // Find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat)queryClass.getMethod("getQueryFloat").invoke(queryObject);
      SelectableFloat comAttributeFloat = (SelectableFloat)comQueryClass.getMethod("getComFloat").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE(comAttributeFloat));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").EQ(query2.aDecimal("comDecimal")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").EQ(query2.aDecimal("refQueryDecimal")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal) comQueryClass.getMethod("getComDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(comAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal) refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDecimal.EQ(refAttributeDecimal));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testDecimalGtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").GT(query2.aDecimal("comDecimal")));


      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").GT(query2.aDecimal("refQueryDecimal")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal) comQueryClass.getMethod("getComDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDecimal.GT(comAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal) refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDecimal.GT(refAttributeDecimal));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDecimalGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").GE(query2.aDecimal("comDecimal")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Decimal values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on greater than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").GE(query2.aDecimal("comDecimal")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").GE(query2.aDecimal("refQueryDecimal")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(comAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(comAttributeDecimal));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(refAttributeDecimal));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDecimalLtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").LT(query2.aDecimal("refQueryDecimal")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").LT(query2.aDecimal("comDecimal")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalLtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(refAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeDecimal.LT(attributeDecimal));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDecimalLtEqAttribute()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").LE(query2.aDecimal("refQueryDecimal")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Decimal values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on less than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").LE(query2.aDecimal("refQueryDecimal")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aDecimal("refQueryDecimal").LE(query.aDecimal("queryDecimal")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalLtEqAttribute_Generated()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(refAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(refAttributeDecimal));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeDecimal.LE(attributeDecimal));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDecimal", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  public void testDecimalNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").NE(query2.aDecimal("refQueryDecimal")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDecimal("queryDecimal").NE(query2.aDecimal("comDecimal")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDecimalNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal refAttributeDecimal = (SelectableDecimal)refQueryClass.getMethod("getRefQueryDecimal").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(refAttributeDecimal));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Decimal values are incorrect.");
        }
      }

      // Find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal)queryClass.getMethod("getQueryDecimal").invoke(queryObject);
      SelectableDecimal comAttributeDecimal = (SelectableDecimal)comQueryClass.getMethod("getComDecimal").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(comAttributeDecimal));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").EQ(query2.aDouble("comDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").EQ(query2.aDouble("refQueryDouble")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble) comQueryClass.getMethod("getComDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDouble.EQ(comAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble) refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDouble.EQ(refAttributeDouble));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleGtAttribute()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDouble", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").GT(query2.aDouble("comDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").GT(query2.aDouble("refQueryDouble")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDouble", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtAttribute_Generated()
  {
    QueryMasterSetup.compareQueryObject.setValue("comDouble", "100");
    QueryMasterSetup.compareQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble) comQueryClass.getMethod("getComDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDouble.GT(comAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble) refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject,attributeDouble.GT(refAttributeDouble));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDoubleGtEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").GE(query2.aDouble("comDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Double values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDouble", "100");
      QueryMasterSetup.compareQueryObject.apply();

      // perform a query that WILL find a match based on greater than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").GE(query2.aDouble("comDouble")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").GE(query2.aDouble("refQueryDouble")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDecimal", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleGtEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(comAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      QueryMasterSetup.compareQueryObject.setValue("comDouble", "100");
      QueryMasterSetup.compareQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(comAttributeDouble));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(refAttributeDouble));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.compareQueryObject.setValue("comDouble", "100.5");
      QueryMasterSetup.compareQueryObject.apply();
    }
  }

  public void testDoubleLtAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").LT(query2.aDouble("refQueryDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").LT(query2.aDouble("comDouble")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleLtAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT(refAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeDouble.LT(attributeDouble));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testDoubleLtEqAttribute()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").LE(query2.aDouble("refQueryDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Double values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      // perform a query that WILL find a match based on less than
      factory = new QueryFactory();
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").LE(query2.aDouble("refQueryDouble")));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute Double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query2.WHERE(query2.aDouble("refQueryDouble").LE(query.aDouble("queryDouble")));

      iterator = query2.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleLtEqAttribute_Generated()
  {
    QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "100.5");
    QueryMasterSetup.childRefQueryObject.apply();

    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      QueryFactory factory = new QueryFactory();

      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(refAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(refAttributeDouble));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on long values are incorrect.");
        }
      }

      refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      refQueryClass.getMethod("WHERE", Condition.class).invoke(refQueryObject, refAttributeDouble.LE(attributeDouble));

      Class refIteratorClass = OIterator.class;
      Object refResultIterator = refQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(refQueryObject);

      hasNext = (Boolean)refIteratorClass.getMethod("hasNext").invoke(refResultIterator);
      if (hasNext)
      {
        refIteratorClass.getMethod("close").invoke(refResultIterator);
        fail("A query based on attribute long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
    finally
    {
      QueryMasterSetup.childRefQueryObject.setValue("refQueryDouble", "200.5");
      QueryMasterSetup.childRefQueryObject.apply();
    }
  }

  public void testDoubleNotEqAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      BusinessDAOQuery query2 = factory.businessDAOQuery(QueryMasterSetup.childRefQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").NE(query2.aDouble("refQueryDouble")));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query2 = factory.businessDAOQuery(QueryMasterSetup.compareQueryInfo.getType());
      query.WHERE(query.aDouble("queryDouble").NE(query2.aDouble("comDouble")));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDoubleNotEqAttribute_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String refType = QueryMasterSetup.childRefQueryInfo.getType();
      String refQueryType = EntityQueryAPIGenerator.getQueryClass(refType);
      Class refQueryClass = LoaderDecorator.load(refQueryType);

      String comType = QueryMasterSetup.compareQueryInfo.getType();
      String comQueryType = EntityQueryAPIGenerator.getQueryClass(comType);
      Class comQueryClass = LoaderDecorator.load(comQueryType);

      QueryFactory factory = new QueryFactory();

      // Find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object refQueryObject = refQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble refAttributeDouble = (SelectableDouble)refQueryClass.getMethod("getRefQueryDouble").invoke(refQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(refAttributeDouble));

      Class iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if(!hasNext)
      {
        fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable)resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String)objectClass.getMethod("getId").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on Double values are incorrect.");
        }
      }

      // Find a match based on equals
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object comQueryObject = comQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble)queryClass.getMethod("getQueryDouble").invoke(queryObject);
      SelectableDouble comAttributeDouble = (SelectableDouble)comQueryClass.getMethod("getComDouble").invoke(comQueryObject);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(comAttributeDouble));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute Double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
}
