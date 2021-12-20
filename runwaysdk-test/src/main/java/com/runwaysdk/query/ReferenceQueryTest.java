/**
 * Copyright (c) 2022 TerraFrame, Inc. All rights reserved.
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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.StructQueryAPIGenerator;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.MdStructDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class ReferenceQueryTest
{
  @Request
  @Test
  public void testNullReference()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();

      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF referenceMdBusiness = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      Class<?> referenceClass = LoaderDecorator.load(referenceMdBusiness.definesType());

      String referenceQueryInterfaceType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(referenceMdBusiness);
      Class<?> referenceQueryInterface = LoaderDecorator.load(referenceQueryInterfaceType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) referenceQueryInterface.getMethod("EQ", referenceClass).invoke(attributeReference, new Object[] { null });

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      Assert.assertFalse(hasNext);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testNotNullReference()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();

      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF referenceMdBusiness = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      Class<?> referenceClass = LoaderDecorator.load(referenceMdBusiness.definesType());

      String referenceQueryInterfaceType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(referenceMdBusiness);
      Class<?> referenceQueryInterface = LoaderDecorator.load(referenceQueryInterfaceType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) referenceQueryInterface.getMethod("NE", referenceClass).invoke(attributeReference, new Object[] { null });

      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      Assert.assertTrue(hasNext);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Object attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsAny(QueryMasterSetup.californiaItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Object attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA");
      method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsExactly()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsExactly(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationContainsExactly_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Object attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA", "CT");
      method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationNotContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAll(QueryMasterSetup.connecticutItemId, QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationNotContainsAll_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Object attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationNotContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAny(QueryMasterSetup.californiaItemId, QueryMasterSetup.kansasItemId));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aEnumeration("refQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEnumerationNotContainsAny_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Object attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeEnumeration = refQueryObjIFClass.getMethod("getRefQueryEnumeration").invoke(attributeReference);
      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO");
      method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").EQ(QueryMasterSetup.childRefQueryObject.getOid()));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").EQ(QueryMasterSetup.testQueryObject1.getOid()));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) refQueryObjIFClass.getMethod("EQ", String.class).invoke(attributeReference, QueryMasterSetup.childRefQueryObject.getOid());
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      condition = (Condition) refQueryObjIFClass.getMethod("EQ", String.class).invoke(attributeReference, QueryMasterSetup.testQueryObject1.getOid());
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEqComponent()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").EQ(QueryMasterSetup.childRefQueryObject));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").EQ(QueryMasterSetup.testQueryObject1));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceEqComponent_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // Instantiate a business object of type Users of the system user.
      Class<?> referenceClass = LoaderDecorator.load(QueryMasterSetup.childRefQueryObject.getType());
      Object reference = referenceClass.getMethod("get", String.class).invoke(null, QueryMasterSetup.childRefQueryObject.getOid());

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) refQueryObjIFClass.getMethod("EQ", referenceClass).invoke(attributeReference, reference);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      reference = referenceClass.getMethod("get", String.class).invoke(null, QueryMasterSetup.childRefQueryObject2.getOid());

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      condition = (Condition) refQueryObjIFClass.getMethod("EQ", referenceClass).invoke(attributeReference, reference);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
        ;
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").NE(QueryMasterSetup.testQueryObject1.getOid()));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").NE(QueryMasterSetup.childRefQueryObject.getOid()));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) refQueryObjIFClass.getMethod("NE", String.class).invoke(attributeReference, QueryMasterSetup.childRefQueryObject2.getOid());
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      condition = (Condition) refQueryObjIFClass.getMethod("NE", String.class).invoke(attributeReference, QueryMasterSetup.childRefQueryObject.getOid());
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceNotEqComponent()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").NE(QueryMasterSetup.testQueryObject1));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").NE(QueryMasterSetup.childRefQueryObject));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceNotEqComponent_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // Instantiate a business object of type Users of the system user.
      Class<?> referenceClass = LoaderDecorator.load(QueryMasterSetup.childRefQueryObject.getType());
      Object reference = referenceClass.getMethod("get", String.class).invoke(null, QueryMasterSetup.childRefQueryObject2.getOid());

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      Condition condition = (Condition) refQueryObjIFClass.getMethod("NE", referenceClass).invoke(attributeReference, reference);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      reference = referenceClass.getMethod("get", String.class).invoke(null, QueryMasterSetup.childRefQueryObject.getOid());

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      condition = (Condition) refQueryObjIFClass.getMethod("NE", referenceClass).invoke(attributeReference, reference);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, condition);

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceStructBooleanEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(true));
      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aStruct("queryStruct").aBoolean("structQueryBoolean").EQ(false));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceStructBooleanEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // Load the struct object query class
      MdStructDAOIF basicMdBusinessIF = (MdStructDAOIF) MdStructDAO.getMdStructDAO(QueryMasterSetup.structInfo.getType());
      String basicIFType = StructQueryAPIGenerator.getAttrStructInterfaceCompiled(basicMdBusinessIF);
      Class<?> basicIFClass = LoaderDecorator.load(basicIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableStruct attributeStruct = (SelectableStruct) refQueryObjIFClass.getMethod("getQueryStruct").invoke(attributeReference);
      SelectableBoolean attributeBoolean = (SelectableBoolean) basicIFClass.getMethod("getStructQueryBoolean").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeStruct = (SelectableStruct) refQueryObjIFClass.getMethod("getQueryStruct").invoke(attributeReference);
      attributeBoolean = (SelectableBoolean) basicIFClass.getMethod("getStructQueryBoolean").invoke(attributeStruct);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceReferenceEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.SYSTEM_USER_ID));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aReference(ElementInfo.CREATED_BY).oid().EQ(ServerConstants.PUBLIC_USER_ID));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceReferenceEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableReference attributeRefRef = (SelectableReference) refQueryObjIFClass.getMethod("getCreatedBy").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeRefRef.EQ(ServerConstants.SYSTEM_USER_ID));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeRefRef = (SelectableReference) refQueryObjIFClass.getMethod("getCreatedBy").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeRefRef.EQ(ServerConstants.PUBLIC_USER_ID));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(true));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(false));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean) refQueryObjIFClass.getMethod("getRefQueryBoolean").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(true));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeBoolean = (SelectableBoolean) refQueryObjIFClass.getMethod("getRefQueryBoolean").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.EQ(false));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanNotEqBoolean()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(false));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aBoolean("refQueryBoolean").NE(true));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceBooleanNotEqBoolean_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableBoolean attributeBoolean = (SelectableBoolean) refQueryObjIFClass.getMethod("getRefQueryBoolean").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(false));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeBoolean = (SelectableBoolean) refQueryObjIFClass.getMethod("getRefQueryBoolean").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeBoolean.NE(true));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQ("ref character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQ("wrong character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("ref character value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQ("wrong character value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQi("REF CHARACTER VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").EQi("WRONG CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("REF CHARACTER VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.EQi("WRONG CHARACTER VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").INi("WRONG VALUE 1", "REF CHARACTER VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "REF CHARACTER VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").IN("wrong value 1", "ref character value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "ref character value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKEi("%CHARACTER%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKEi("%CHARACTER"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%CHARACTER%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKEi("%CHARACTER"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKE("%character%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").LIKE("%character"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute referencecharacter values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%character%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.LIKE("%character"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NEi("WRONG CHARACTER STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NEi("REF CHARACTER VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("WRONG CHARACTER STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NEi("REF CHARACTER VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NE("wrong character value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NE("ref character value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("wrong character value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NE("ref character value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NIi("WRONG 1", "REF CHARACTER VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NIi("WRONG 1", "REF CHARACTER VALUE", "WRONG 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NI("wrong 1", "ref character value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NI("wrong 1", "ref character value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NLIKEi("%CHARACTER%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKEi("%CHARACTER%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference character values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aCharacter("refQueryCharacter").NLIKE("%character%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference character values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceCharacterNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeChar = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryCharacter").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeChar.NLIKE("%character%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").EQ("2007-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").EQ("2007-05-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-05-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-05-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-05"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE("2007-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));
      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GT("2007-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GT("2007-11-07"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on greater than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-06"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-07"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE("2007-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));
      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LT("2007-11-07"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LT("2007-11-05"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-07", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").NE("2007-11-05"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").NE("2007-11-06"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDate("refQueryDate").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-05", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2007-11-06", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ("2007-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ("2007-05-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-05-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-05-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-06 12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-05 13:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE("2007-11-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-07 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT("2007-11-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aDateTime("queryDateTime").GT("2007-12-07 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-07 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-11-06 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-12-07 13:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE("2007-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT("2007-12-07 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LT("2007-11-05 13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-05 13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE("2007-12-05 13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE("2007-11-06 12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference datetime values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDateTime("refQueryDateTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference datetime values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDateTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-12-05 13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2007-11-06 12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryDate").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ("201.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ(new BigDecimal(200.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").EQ(new BigDecimal(201.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(200.5)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.EQ(new BigDecimal(201.5)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT(new BigDecimal(200)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GT(new BigDecimal(201)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(200)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GT(new BigDecimal(201)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("200"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(200.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(200)));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").GE(new BigDecimal(201)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(200.5)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(200)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.GE(new BigDecimal(201)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT(new BigDecimal(201)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LT(new BigDecimal(200)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(201)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LT(new BigDecimal(200)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("201"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(200.5)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(201)));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").LE(new BigDecimal(199)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(200.5)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(201)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.LE(new BigDecimal(199)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE(new BigDecimal(201)));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDecimal("refQueryDecimal").NE(new BigDecimal(200.5)));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDecimalNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDecimal attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(201)));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference decimal values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDecimal = (SelectableDecimal) refQueryObjIFClass.getMethod("getRefQueryDecimal").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDecimal.NE(new BigDecimal(200.5)));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object decimal values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ("201.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").EQ(201.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.EQ(201.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT((double) 200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GT((double) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double) 200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GT((double) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("200"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE((double) 200));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").GE((double) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE(200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.GE((double) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT((double) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LT((double) 200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT((double) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LT((double) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("201"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE(200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE((double) 201));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").LE((double) 199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE(200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.LE((double) 199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE((double) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aDouble("refQueryDouble").NE((double) 200.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceDoubleNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableDouble attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference double values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeDouble = (SelectableDouble) refQueryObjIFClass.getMethod("getRefQueryDouble").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeDouble.NE((double) 200.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object double values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ("201.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ((float) 200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").EQ((float) 201.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float) 200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.EQ((float) 201.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT((float) 200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GT((float) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float) 200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GT((float) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("200"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float) 200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float) 200));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").GE((float) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.GE((float) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT((float) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LT((float) 200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LT((float) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("200.5"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("201"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float) 200.5));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float) 201));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").LE((float) 199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 200.5));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.LE((float) 199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE("200.5"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE((float) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aFloat("refQueryFloat").NE((float) 200.5));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceFloatNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableFloat attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference float values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeFloat = (SelectableFloat) refQueryObjIFClass.getMethod("getRefQueryFloat").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeFloat.NE((float) 200.5));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference float values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").EQ(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.EQ(201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT("199"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT(199));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GT(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not greater than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(199));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GT(201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("199"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(199));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").GE(201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on not greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.GE(201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT(201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LT(200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LT(200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("201"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(201));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").LE(199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on not less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.LE(199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE(201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aInteger("refQueryInteger").NE(200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceIntegerNotEq_Generrated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableInteger attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeInteger = (SelectableInteger) refQueryObjIFClass.getMethod("getRefQueryInteger").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeInteger.NE(200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object integer values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").EQ("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").EQ("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").EQ((long) 200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").EQ((long) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long) 200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.EQ((long) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GT("199"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GT("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GT((long) 199));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GT((long) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on greater than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long) 199));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GT((long) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE("199"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE("201"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long) 200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long) 199));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").GE((long) 201));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongGtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference integer values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.GE((long) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LT("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LT("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LT((long) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LT((long) 200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLt_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on less than
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LT((long) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE("200"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE("201"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE("199"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLtEq()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long) 200));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long) 201));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").LE((long) 199));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongLtEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 200));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on less than
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 201));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.LE((long) 199));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").NE("201"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").NE("200"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").NE((long) 201));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aLong("refQueryLong").NE((long) 200));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceLongNotEq_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on not equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableLong attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long) 201));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference long values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeLong = (SelectableLong) refQueryObjIFClass.getMethod("getRefQueryLong").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeLong.NE((long) 200));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object long values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").EQ("ref text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").EQ("wrong text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").EQ("ref clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").EQ("wrong clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("ref text value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQ("wrong text value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("ref clob value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQ("wrong clob value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").EQi("REF TEXT VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").EQi("WRONG TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").EQi("REF CLOB VALUE"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").EQi("WRONG CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("REF TEXT VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.EQi("WRONG TEXT VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("REF CLOB VALUE"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.EQi("WRONG CLOB VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").IN("wrong value 1", "ref text value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").IN("wrong value 1", "ref clob value", "wrong value 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").IN("wrong value 1", "wrong value 2", "wrong value 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "ref text value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "ref clob value", "wrong value 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").INi("WRONG VALUE 1", "REF TEXT VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").INi("WRONG VALUE 1", "REF CLOB VALUE", "WRONG VALUE 2"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "REF TEXT VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "REF CLOB VALUE", "WRONG VALUE 2"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").LIKE("%text%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").LIKE("%text"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").LIKE("%clob%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").LIKE("%clob"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKE("%text"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKE("%clob"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").LIKEi("%TEXT%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").LIKEi("%TEXT"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").LIKEi("%CLOB%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").LIKEi("%CLOB"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.LIKEi("%TEXT"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.LIKEi("%CLOB"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NE("wrong text value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NE("ref text value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NE("wrong clob value"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NE("ref clob value"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("wrong text value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NE("ref text value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotEqString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("wrong clob value"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NE("ref clob value"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NEi("WRONG TEXT STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NEi("REF TEXT VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NEi("WRONG CLOB STRING"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NEi("REF CLOB VALUE"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("WRONG TEXT STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NEi("REF TEXT VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("WRONG CLOB STRING"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NEi("REF CLOB VALUE"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NI("wrong 1", "ref text value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NI("wrong 1", "wrong 2", "wrong 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NI("wrong 1", "ref clob value", "wrong 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NI("wrong 1", "ref text value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotInStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "wrong 2", "wrong 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NI("wrong 1", "ref clob value", "wrong 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NIi("WRONG 1", "REF TEXT VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NIi("WRONG 1", "REF CLOB VALUE", "WRONG 2"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NIi("WRONG 1", "REF TEXT VALUE", "WRONG 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NIi("WRONG 1", "REF CLOB VALUE", "WRONG 2"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NLIKE("%text%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NLIKE("%wrong%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NLIKE("%clob%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKE("%text%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotLikeString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%wrong%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKE("%clob%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aText("refQueryText").NLIKEi("%TEXT%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NLIKEi("%WRONG%"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aClob("refQueryClob").NLIKEi("%CLOB%"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference text values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeText = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryText").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeText.NLIKEi("%TEXT%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference text values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceClobNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableChar attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%WRONG%"));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference Clob values are incorrect.");
        }
      }

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeClob = (SelectableChar) refQueryObjIFClass.getMethod("getRefQueryClob").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeClob.NLIKEi("%CLOB%"));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute reference Clob values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").EQ("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").EQ("13:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").EQ(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").EQ(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.EQ(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GT("11:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GT("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE("11:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE("14:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match based on greater than
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").GE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeGtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));
      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.GE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LT("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LT("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LT(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LT(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLt_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LT(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE("12:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE("13:00:00"));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE("11:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));

      iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").LE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeLtEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      // Load the iterator class
      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("11:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.LE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").NE("13:00:00"));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").NE("12:00:00"));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").NE(date));

      OIterator<BusinessDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference time values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      query = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      query.WHERE(query.aReference("reference").aTime("refQueryTime").NE(date));

      iterator = query.getIterator();
      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on attribute reference time values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testReferenceTimeNotEq_Generated()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      String type = QueryMasterSetup.childQueryInfo.getType();
      Class<?> objectClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> queryClass = LoaderDecorator.load(queryType);

      // Load the reference query class
      MdBusinessDAOIF refQueryObjMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(QueryMasterSetup.childRefQueryObject.getType());
      String refQueryObjIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(refQueryObjMdBusinessIF);
      Class<?> refQueryObjIFClass = LoaderDecorator.load(refQueryObjIFType);

      // perform a query that WILL find a match based on equals
      QueryFactory factory = new QueryFactory();
      Object queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      SelectableMoment attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        objectClass.cast(object);
        String objectId = (String) objectClass.getMethod("getOid").invoke(object);
        if (!objectId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on attribute reference date values are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      queryObject = queryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeRef = (SelectableReference) queryClass.getMethod("getReference").invoke(queryObject);
      attributeMoment = (SelectableMoment) refQueryObjIFClass.getMethod("getRefQueryTime").invoke(attributeRef);
      queryClass.getMethod("WHERE", Condition.class).invoke(queryObject, attributeMoment.NE(date));

      resultIterator = queryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(queryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on attribute refernce Object date values returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

}
