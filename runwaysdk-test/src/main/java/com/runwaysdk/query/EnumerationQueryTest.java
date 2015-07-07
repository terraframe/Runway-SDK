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

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class EnumerationQueryTest extends TestCase
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
    junit.textui.TestRunner.run(new EntityMasterTestSetup(EnumerationQueryTest.suite()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(EnumerationQueryTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() {}

      protected void tearDown(){}
    };

    return wrapper;
  }

  public void testEnumerationContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.connecticutItemId));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());

      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      Method method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition =
        (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CT");
      method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsAny(QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsAny(QueryMasterSetup.connecticutItemId));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());

      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CT");
      Method method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition =
        (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT");
      method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationContainsExactly()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationContainsExactly_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());

      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      Method method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition =
        (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA", "CT");
      method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationNotContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").notContainsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationNotContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());

      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition =
        (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationNotContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").notContainsAny(QueryMasterSetup.connecticutItemId, QueryMasterSetup.kansasItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationNotContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition =
        (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testEnumerationBooleanEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationBooleanEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(true));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").EQ(false));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationBooleanEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean)stateEnumMasterClassIF.getMethod("getEnumQueryBoolean").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeBoolean = (SelectableBoolean)stateEnumMasterClassIF.getMethod("getEnumQueryBoolean").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testEnumerationBooleanNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationBooleanNotEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(false));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aBoolean("enumQueryBoolean").NE(true));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationBooleanNotEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = (MdBusinessDAOIF)MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean)stateEnumMasterClassIF.getMethod("getEnumQueryBoolean").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(false));

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
          fail("The objects returned by a query based on attribute enumeration values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeBoolean = (SelectableBoolean)stateEnumMasterClassIF.getMethod("getEnumQueryBoolean").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(true));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("enum character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQ("wrong character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("enum character value"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("wrong character value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
     fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQi("ENUM CHARACTER VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").EQi("WRONG CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("ENUM CHARACTER VALUE"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("WRONG CHARACTER VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").INi("WRONG VALUE 1", "ENUM CHARACTER VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "ENUM CHARACTER VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").IN("wrong value 1", "enum character value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "enum character value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKEi("%CHARACTER%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKEi("%CHARACTER"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKEi("%CHARACTER%"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKEi("%CHARACTER"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKE("%character%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").LIKE("%character"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumerationcharacter values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKE("%character%"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKE("%character"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NEi("WRONG CHARACTER STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NEi("ENUM CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi("WRONG CHARACTER STRING"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi("ENUM CHARACTER VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NE("wrong character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NE("enum character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE("wrong character value"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE("enum character value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NIi("WRONG 1", "ENUM CHARACTER VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NIi("WRONG 1", "ENUM CHARACTER VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NI("wrong 1", "enum character value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NI("wrong 1", "wrong 2", "wrong 3"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NI("wrong 1", "enum character value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumerence character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKEi("%CHARACTER%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKEi("%WRONG%"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKEi("%CHARACTER%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationCharacterNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aCharacter("enumQueryCharacter").NLIKE("%character%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationCharacterNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKE("%wrong%"));

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
          fail("The objects returned by a query based on attribute enumeration character values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeCharacter = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryCharacter").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKE("%character%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ("2006-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ("2006-05-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-05-05",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-05-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-05"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }


      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE("2006-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT("2006-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT("2006-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-07"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE("2006-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT("2006-11-07"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT("2006-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-07",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE("2006-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE("2006-11-06"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDate("enumQueryDate").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-05",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

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
          fail("The objects returned by a query based on attribute enumeration date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2006-11-06",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDate").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ("2006-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ("2006-05-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-05-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-05-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-05 13:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE("2006-11-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }


      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-07 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT("2006-11-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2006-12-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-11-06 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-12-07 13:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE("2006-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();

      // perform a query that WILL find a match based on equals
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      // Load the iterator class
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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT("2006-12-07 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT("2006-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-07 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("2006-12-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE("2006-11-06 12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDateTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDateTime("enumQueryDateTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDateTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-12-05 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

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
          fail("The objects returned by a query based on attribute enumeration datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2006-11-06 12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryDateTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ("200.5"));

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
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ("201.5"));

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

  public void testEnumerationDecimalEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ(new BigDecimal(200.5)));

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
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").EQ(new BigDecimal(201.5)));

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
  public void testEnumerationDecimalEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(200.5)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(201.5)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT(new BigDecimal(200)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GT(new BigDecimal(201)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDecimalGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(200)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(201)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("200"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(200.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(200)));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").GE(new BigDecimal(201)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDecimalGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equal
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(200.5)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(200)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(201)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT(new BigDecimal(201)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LT(new BigDecimal(200)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDecimalLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(201)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(200)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("201"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(200.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(202)));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").LE(new BigDecimal(199)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDecimalLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(200.5)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(201)));

      // Load the iterator class
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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(199)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDecimalNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE(new BigDecimal(201)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDecimal("enumQueryDecimal").NE(new BigDecimal(200.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDecimalNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(201)));

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
          fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)stateEnumMasterClassIF.getMethod("getEnumQueryDecimal").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(200.5)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ("201.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").EQ(201.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(200.5));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(201.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT((double)200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GT((double)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double)200));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("200"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE((double)200));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").GE((double)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(200.5));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double)200));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT((double)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LT((double)200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT((double)201));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT((double)200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less then
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("201"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less then
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE((double)201));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").LE((double)199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(200.5));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less then
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double)201));

      // Load the iterator class
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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double)199));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationDoubleNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE((double)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aDouble("enumQueryDouble").NE(200.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationDoubleNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double)201));

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
          fail("The objects returned by a query based on attribute enumeration double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDouble = (SelectableDouble)stateEnumMasterClassIF.getMethod("getEnumQueryDouble").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE(200.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ("201.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ((float)200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").EQ((float)201.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float)200.5));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float)201.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT((float)200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GT((float)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float)200));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("200"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float)200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float)200));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").GE((float)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)200.5));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)200));

      // Load the iterator class
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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT((float)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LT((float)200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float)201));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float)200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("201"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE((float)200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").LE((float)199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float)200.5));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float)199));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationFloatNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE((float)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aFloat("enumQueryFloat").NE((float)200.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationFloatNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float)201));

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
          fail("The objects returned by a query based on attribute enumeration float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeFloat = (SelectableFloat)stateEnumMasterClassIF.getMethod("getEnumQueryFloat").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float)200.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").EQ(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(200));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT("199"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT(199));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GT(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(199));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("199"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(199));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").GE(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(200));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(199));

      // Load the iterator class
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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT(201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LT(200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(201));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("201"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(201));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").LE(199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(200));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(201));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(199));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationIntegerNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE(201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aInteger("enumQueryInteger").NE(200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationIntegerNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(201));

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
          fail("The objects returned by a query based on attribute enumeration integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeInteger = (SelectableInteger)stateEnumMasterClassIF.getMethod("getEnumQueryInteger").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ((long)200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").EQ((long)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long)200));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT("199"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT((long)199));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GT((long)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long)199));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("199"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long)200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long)199));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").GE((long)201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)200));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)199));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)201));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT((long)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LT((long)200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long)201));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long)200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("201"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long)200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long)201));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").LE((long)199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)200));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)201));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)199));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationLongNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE((long)201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aLong("enumQueryLong").NE((long)200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationLongNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long)201));

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
          fail("The objects returned by a query based on attribute enumeration long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeLong = (SelectableLong)stateEnumMasterClassIF.getMethod("getEnumQueryLong").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long)200));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQ("enum text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQ("wrong text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").EQ("enum clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").EQ("wrong clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("enum text value"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("wrong text value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("enum clob value"));

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
          fail("The objects returned by a query based on attribute enumeration clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("wrong clob value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQi("ENUM TEXT VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").EQi("WRONG TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").EQi("ENUM CLOB VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").EQi("WRONG CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testEnumerationTextEqIgnoreCase_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("ENUM TEXT VALUE"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("WRONG TEXT VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobEqIgnoreCase_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("ENUM CLOB VALUE"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("WRONG CLOB VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testEnumerationTextInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").IN("wrong value 1", "enum text value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").IN("wrong value 1", "enum clob value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "enum text value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "enum clob value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").INi("WRONG VALUE 1", "ENUM TEXT VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").INi("WRONG VALUE 1", "ENUM CLOB VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "ENUM TEXT VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "ENUM CLOB VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKE("%text%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKE("%text"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").LIKE("%clob%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").LIKE("%clob"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text%"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob%"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").LIKEi("%CLOB%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").LIKEi("%CLOB"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKEi("%TEXT%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").LIKEi("%TEXT"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT%"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB%"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE("wrong text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NE("enum text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NE("wrong clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NE("enum clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("wrong text value"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("enum text value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("wrong clob value"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("enum clob value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NEi("WRONG TEXT STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NEi("ENUM TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NEi("WRONG CLOB STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NEi("ENUM CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("WRONG TEXT STRING"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("ENUM TEXT VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("WRONG CLOB STRING"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("ENUM CLOB VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NI("wrong 1", "enum text value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NI("wrong 1", "enum clob value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "wrong 2", "wrong 3"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "enum text value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "wrong 2", "wrong 3"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "enum clob value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testEnumerationTextNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NIi("WRONG 1", "ENUM TEXT VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NIi("WRONG 1", "ENUM CLOB VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NIi("WRONG 1", "ENUM TEXT VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "ENUM CLOB VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKE("%text%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NLIKE("%clob%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%wrong%"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%text%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%wrong%"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%clob%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTextNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aText("enumQueryText").NLIKEi("%TEXT%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationClobNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aClob("enumQueryClob").NLIKEi("%CLOB%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testEnumerationTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%WRONG%"));

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
          fail("The objects returned by a query based on attribute enumeration text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeText = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryText").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%TEXT%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationClobNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%WRONG%"));

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
          fail("The objects returned by a query based on attribute enumeration Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeClob = (SelectableChar)stateEnumMasterClassIF.getMethod("getEnumQueryClob").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%CLOB%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testEnumerationTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ("13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT("11:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("11:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE("11:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));
      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("13:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE("11:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));
      // perform a query that WILL find a match based on equ
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);


      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));
      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aTime("enumQueryTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableMoment attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeDate = (SelectableMoment)stateEnumMasterClassIF.getMethod("getEnumQueryTime").invoke(attributeEnumeration);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDate.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEnumerationReferenceEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.SYSTEM_USER_ID));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aEnumeration("queryEnumeration").aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.PUBLIC_USER_ID));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testEnumerationReferenceEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // Load the reference query class
      MdBusinessDAOIF usersMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(UserInfo.CLASS);
      String usersQueryRefIFType =  BusinessQueryAPIGenerator.getRefInterfaceCompiled(usersMdBusinessMasterIF);
      Class usersQueryRefClassIF = LoaderDecorator.load(usersQueryRefIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableReference attributeReference = (SelectableReference)stateEnumMasterClassIF.getMethod("getCreatedBy").invoke(attributeEnumeration);
      Condition condition = (Condition)usersQueryRefClassIF.getMethod("EQ", String.class).invoke(attributeReference, ServerConstants.SYSTEM_USER_ID);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeReference = (SelectableReference)stateEnumMasterClassIF.getMethod("getCreatedBy").invoke(attributeEnumeration);
      condition = (Condition)usersQueryRefClassIF.getMethod("EQ", String.class).invoke(attributeReference, ServerConstants.PUBLIC_USER_ID);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testEnumerationRefEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdBusinessDAOIF stateMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.stateEnumMdBusiness.definesType());
      String stateEnumMasterIFType =  BusinessQueryAPIGenerator.getEnumInterfaceCompiled(stateMdBusinessMasterIF);
      Class stateEnumMasterClassIF = LoaderDecorator.load(stateEnumMasterIFType);

      // Load the reference query class
      MdBusinessDAOIF assignableMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(SingleActorDAOIF.CLASS);
      String assignableQueryRefIFType =  BusinessQueryAPIGenerator.getRefInterfaceCompiled(assignableMdBusinessMasterIF);
      Class assignableQueryRefClassIF = LoaderDecorator.load(assignableQueryRefIFType);

      Class assignableClass = LoaderDecorator.load(SingleActorDAOIF.CLASS);

      // Instantiate a business object of type Users of the system user.
      Class usersClass = LoaderDecorator.load(UserInfo.CLASS);
      Object busObjUser = usersClass.getMethod("get", String.class).invoke(null, ServerConstants.SYSTEM_USER_ID);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      SelectableReference attributeReference = (SelectableReference)stateEnumMasterClassIF.getMethod("getCreatedBy").invoke(attributeEnumeration);

      Condition condition = (Condition)assignableQueryRefClassIF.getMethod("EQ", assignableClass).invoke(attributeReference, busObjUser);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

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
          fail("The objects returned by a query based on attribute enumeration time values are incorrect.");
        }
      }

      busObjUser = usersClass.getMethod("get", String.class).invoke(null, ServerConstants.PUBLIC_USER_ID);

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = queryClass.getMethod("getQueryEnumeration").invoke(queryObject);
      attributeReference = (SelectableReference)stateEnumMasterClassIF.getMethod("getCreatedBy").invoke(attributeEnumeration);
      condition = (Condition)assignableQueryRefClassIF.getMethod("EQ", assignableClass).invoke(attributeReference, busObjUser);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute enumeration time values returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

}
