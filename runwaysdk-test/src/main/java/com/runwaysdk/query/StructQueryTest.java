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
import com.runwaysdk.business.generation.StructQueryAPIGenerator;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;

public class StructQueryTest extends TestCase
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
    junit.textui.TestRunner.run(new EntityMasterTestSetup(StructQueryTest.suite()));
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(StructQueryTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() {}

      protected void tearDown(){}
    };

    return wrapper;
  }

  public void testStructEnumerationContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructEnumerationContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableStruct attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      Object attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition)method.invoke(attributeEnumeration, enumArray);
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
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructEnumerationContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructEnumerationContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableStruct attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      Object attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition)method.invoke(attributeEnumeration, enumArray);
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
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA");
      method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructEnumerationContainsExactly()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsExactly(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructEnumerationContainsExactly_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableStruct attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      Object attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition)method.invoke(attributeEnumeration, enumArray);
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
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA", "CT");
      method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructEnumerationNotContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructEnumerationNotContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableStruct attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      Object attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition)method.invoke(attributeEnumeration, enumArray);
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
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructEnumerationNotContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.kansasItemId));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aEnumeration("structQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructEnumerationNotContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType =  BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableStruct attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      Object attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition)method.invoke(attributeEnumeration, enumArray);
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
          fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = (SelectableStruct)queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeEnumeration = basicStructQueryClassIF.getMethod("getStructQueryEnumeration").invoke(attributeStruct);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO");
      method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition)method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructBooleanEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(true));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(false));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructBooleanEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructBooleanEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean)basicStructQueryClassIF.getMethod("getStructQueryBoolean").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeBoolean = (SelectableBoolean)basicStructQueryClassIF.getMethod("getStructQueryBoolean").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructBooleanNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructBooleanNotEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(false));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aBoolean("structQueryBoolean").NE(true));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructBooleanNotEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean)basicStructQueryClassIF.getMethod("getStructQueryBoolean").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeBoolean = (SelectableBoolean)basicStructQueryClassIF.getMethod("getStructQueryBoolean").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(true));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("basic character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQ("wrong character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("basic character value"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQ("wrong character value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQi("BASIC CHARACTER VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").EQi("WRONG CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("BASIC CHARACTER VALUE"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.EQi("WRONG CHARACTER VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").INi("WRONG VALUE 1", "BASIC CHARACTER VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "BASIC CHARACTER VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").IN("wrong value 1", "basic character value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "basic character value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKEi("%CHARACTER%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKEi("%CHARACTER"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKEi("%CHARACTER"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKE("%character%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").LIKE("%character"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute structcharacter values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.LIKE("%character"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NEi("WRONG CHARACTER STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NEi("BASIC CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NEi("BASIC CHARACTER VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NE("wrong character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NE("basic character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NE("basic character value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructCharacterNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NIi("WRONG 1", "BASIC CHARACTER VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NIi("WRONG 1", "BASIC CHARACTER VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructCharacterNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NI("wrong 1", "basic character value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NI("wrong 1", "basic character value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute basicerence character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKEi("%CHARACTER%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKEi("%CHARACTER%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructCharacterNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aCharacter("structQueryCharacter").NLIKE("%character%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructCharacterNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeCharacter = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryCharacter").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeCharacter.NLIKE("%character%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ("2008-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ("2008-05-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute basicernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructDateEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-05-05",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute basicernce field date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-05-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-05"));
      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE("2008-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));
      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }
      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT("2008-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT("2008-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-07"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE("2008-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }
      // perform a query that WILL find a match based on less than
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT("2008-11-07"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT("2008-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateLt()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-07",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE("2008-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE("2008-11-06"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDate("structQueryDate").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-05",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2008-11-06",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDate").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ("2008-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ("2008-05-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-05-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-05-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-05 13:00:00"));

       iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE("2008-11-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));

       iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT("2008-11-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2008-12-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeGt()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-07 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-11-06 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-12-07 13:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE("2008-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-07 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT("2008-12-07 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT("2008-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-07 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-07 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-05 13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("2008-12-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE("2008-11-06 12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDateTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-05 13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDateTime("structQueryDateTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDateTimeNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-12-05 13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2008-11-06 12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryDateTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ("300.5"));

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
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ("301.5"));

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

  public void testStructDecimalEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ(new BigDecimal(300.5)));

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
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").EQ(new BigDecimal(301.5)));

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
  public void testStructDecimalEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(300.5)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(301.5)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT(new BigDecimal(300)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GT(new BigDecimal(301)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDecimalGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(300)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(301)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("300"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(300.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(300)));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").GE(new BigDecimal(301)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDecimalGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(300.5)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(300)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }


      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(301)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT(new BigDecimal(301)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LT(new BigDecimal(300)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDecimalLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(301)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(300)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("301"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE("299"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(300.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(301)));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").LE(new BigDecimal(299)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDecimalLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(300.5)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(301)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(299)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE("300.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDecimalNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE(new BigDecimal(301)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDecimal("structQueryDecimal").NE(new BigDecimal(300.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDecimalNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(301)));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDecimal = (SelectableDecimal)basicStructQueryClassIF.getMethod("getStructQueryDecimal").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(300.5)));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ("301.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ(300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").EQ(301.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testStructDoubleEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(301.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT((double)300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GT((double)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDoubleGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("300"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE(300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE((double)300));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").GE((double)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDoubleGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDoubleLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT((double)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LT((double)300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("301"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE("299"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE(300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE((double)301));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").LE((double)299));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDoubleLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double)299));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructDoubleNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE("300.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructDoubleNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE((double)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aDouble("structQueryDouble").NE((double)300.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructDoubleNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeDouble = (SelectableDouble)basicStructQueryClassIF.getMethod("getStructQueryDouble").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double)300.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ("301.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ((float)300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").EQ((float)301.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float)300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float)301.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT((float)300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GT((float)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("300"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)300));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").GE((float)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equalsls
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT((float)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LT((float)300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float)300));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("300.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("301"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE("299"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)300.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)301));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").LE((float)299));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equalsls
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float)300.5));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float)299));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE("300.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructFloatNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE((float)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aFloat("structQueryFloat").NE((float)300.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructFloatNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeFloat = (SelectableFloat)basicStructQueryClassIF.getMethod("getStructQueryFloat").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float)300.5));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ(300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").EQ(301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT("299"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT(299));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GT(301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(299));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("299"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(299));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").GE(301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(299));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT(301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LT(300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(300));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("301"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE("299"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(301));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").LE(299));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(299));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructIntegerNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE(301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aInteger("structQueryInteger").NE(300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructIntegerNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on not equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeInteger = (SelectableInteger)basicStructQueryClassIF.getMethod("getStructQueryInteger").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(300));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ((long)300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").EQ((long)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT("299"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT((long)299));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GT((long)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long)299));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("299"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE("301"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)299));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").GE((long)301));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)299));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long)301));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT((long)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LT((long)300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long)300));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("300"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("301"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE("299"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)300));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)301));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").LE((long)299));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)300));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long)299));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE("301"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE("300"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructLongNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE((long)301));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aLong("structQueryLong").NE((long)300));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructLongNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long)301));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeLong = (SelectableLong)basicStructQueryClassIF.getMethod("getStructQueryLong").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long)300));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQ("basic text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQ("wrong text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").EQ("basic clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").EQ("wrong clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextEqString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("basic text value"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("wrong text value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobEqString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("basic clob value"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("wrong clob value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructTextEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQi("BASIC TEXT VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").EQi("WRONG TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").EQi("BASIC CLOB VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").EQi("WRONG CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextEqIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("BASIC TEXT VALUE"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("WRONG TEXT VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobEqIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("BASIC CLOB VALUE"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("WRONG CLOB VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").IN("wrong value 1", "basic text value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").IN("wrong value 1", "basic clob value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextInStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "basic text value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobInStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "basic clob value", "wrong value 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").INi("WRONG VALUE 1", "BASIC TEXT VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").INi("WRONG VALUE 1", "BASIC CLOB VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "BASIC TEXT VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobInIgnoreCaseStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "BASIC CLOB VALUE", "WRONG VALUE 2"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructTextLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKE("%text%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKE("%text"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").LIKE("%clob%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").LIKE("%clob"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextLikeString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%text%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%text"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobLikeString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%clob%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%clob"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKEi("%TEXT%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").LIKEi("%TEXT"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").LIKEi("%CLOB%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").LIKEi("%CLOB"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%TEXT%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%TEXT"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobLikeIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%CLOB%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%CLOB"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE("wrong text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NE("basic text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NE("wrong clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NE("basic clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextNotEqString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("wrong text value"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("basic text value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotEqString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("wrong clob value"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("basic clob value"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NEi("WRONG TEXT STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NEi("BASIC TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NEi("WRONG CLOB STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NEi("BASIC CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("WRONG TEXT STRING"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("BASIC TEXT VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotEqIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("WRONG CLOB STRING"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("BASIC CLOB VALUE"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  public void testStructTextNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NI("wrong 1", "basic text value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NI("wrong 1", "basic clob value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextNotInStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "wrong 2", "wrong 3"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "basic text value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotInStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "wrong 2", "wrong 3"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "basic clob value", "wrong 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NIi("WRONG 1", "BASIC TEXT VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NIi("WRONG 1", "BASIC CLOB VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTextNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "BASIC TEXT VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "BASIC CLOB VALUE", "WRONG 2"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKE("%text%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NLIKE("%clob%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testStructTextNotLikeString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%wrong%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%text%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotLikeString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%wrong%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%clob%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTextNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aText("structQueryText").NLIKEi("%TEXT%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructClobNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aClob("structQueryClob").NLIKEi("%CLOB%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }


  @SuppressWarnings("unchecked")
  public void testStructTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%WRONG%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryText").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%TEXT%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructClobNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on greater than
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%WRONG%"));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeChar = (SelectableChar)basicStructQueryClassIF.getMethod("getStructQueryClob").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%CLOB%"));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ("13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT("11:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeGt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("11:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeGtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeLt_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("13:00:00"));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE("11:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));

      iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeLtEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testStructTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if(!iterator.hasNext())
      {
        fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          fail("The objects returned by a query based on attribute struct time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aStruct("queryStruct").aTime("structQueryTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        fail("A query based on attribute struct time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  public void testStructTimeNotEq_Generated()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00",  new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class queryClass = LoaderDecorator.load(queryType);

      MdStructDAOIF mdStructIF = (MdStructDAOIF)MdStructDAO.getMdStructDAO(QueryMasterSetup.mdStruct.definesType());
      String basicStructQueryIFType =  StructQueryAPIGenerator.getAttrStructInterfaceCompiled(mdStructIF);
      Class basicStructQueryClassIF = LoaderDecorator.load(basicStructQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

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
          fail("The objects returned by a query based on attribute struct values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00",  new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeStruct = queryClass.getMethod("getQueryStruct").invoke(queryObject);
      attributeMoment = (SelectableMoment)basicStructQueryClassIF.getMethod("getStructQueryTime").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator  = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean)iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        fail("A query based on attribute struct values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }
}
