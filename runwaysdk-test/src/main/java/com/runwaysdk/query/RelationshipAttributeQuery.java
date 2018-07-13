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
package com.runwaysdk.query;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdEnumerationDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

public class RelationshipAttributeQuery
{
  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAll_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CT");
      method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsAny(QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsAny(QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAny_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT");
      method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsExactly()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsExactly_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO", "CT");
      method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAll()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").notContainsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAll_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAny()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").notContainsAny(QueryMasterSetup.connecticutItemId, QueryMasterSetup.kansasItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aEnumeration("conQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumerationNotContainsAny_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class<?> stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class<?> stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getConQueryEnumeration").invoke(relQueryObject);

      enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "CA");
      method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").EQ(true));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").EQ(false));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getConQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.EQ(true));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getConQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.EQ(false));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").NE(false));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aBoolean("conQueryBoolean").NE(true));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInBooleanNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getConQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.NE(false));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getConQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.NE(true));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("wrong character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("wrong character value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQi("CON CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQi("WRONG CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQi("CON CHARACTER VALUE"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQi("WRONG CHARACTER VALUE"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").IN("wrong value 1", "con character value", "wrong value 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.IN("wrong value 1", "con character value", "wrong value 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIgnoreCaseCharacterInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").INi("WRONG VALUE 1", "CON CHARACTER VALUE", "WRONG VALUE 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterInIngoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.INi("WRONG VALUE 1", "CON CHARACTER VALUE", "WRONG VALUE 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").LIKE("%character%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").LIKE("%character"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKE("%character%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKE("%character"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").LIKEi("%CHARACTER%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").LIKEi("%CHARACTER"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKEi("%CHARACTER%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKEi("%CHARACTER"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NE("wrong character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NE("con character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NE("wrong character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NE("con character value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NEi("WRONG CHARACTER STRING"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NEi("CON CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NEi("WRONG CHARACTER STRING"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NEi("CON CHARACTER VALUE"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NI("wrong 1", "con character value", "wrong 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NI("wrong 1", "wrong 2", "wrong 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NI("wrong 1", "con character value", "wrong 2"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NIi("WRONG 1", "CON CHARACTER VALUE", "WRONG 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NIi("WRONG 1", "CON CHARACTER VALUE", "WRONG 2"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NLIKE("%wrong%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NLIKE("%character%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKE("%wrong%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKE("%character%"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NLIKEi("%WRONG%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").NLIKEi("%CHARACTER%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKEi("%WRONG%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKEi("%CHARACTER%"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").EQ("con text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").EQ("wrong text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQ("con text value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQ("wrong text value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").EQi("CON TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").EQi("WRONG TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQi("CON TEXT VALUE"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQi("WRONG TEXT VALUE"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").IN("wrong value 1", "con text value", "wrong value 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.IN("wrong value 1", "con text value", "wrong value 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.IN("wrong value 1", "wrong value 2", "wrong value 3"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").INi("WRONG VALUE 1", "CON TEXT VALUE", "WRONG VALUE 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.INi("WRONG VALUE 1", "CON TEXT VALUE", "WRONG VALUE 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").LIKE("%text%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").LIKE("%text"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextLikeStringGenerated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKE("%text%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKE("%text"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").LIKEi("%TEXT%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").LIKEi("%TEXT"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKEi("%TEXT%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKEi("%TEXT"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NE("wrong text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NE("con text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NE("wrong text value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NE("con text value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotEqIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NEi("WRONG TEXT STRING"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NEi("CON TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NEi("WRONG TEXT STRING"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NEi("CON TEXT VALUE"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotInStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NI("wrong 1", "wrong 2", "wrong 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NI("wrong 1", "con text value", "wrong 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NI("wrong 1", "wrong 2", "wrong 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NI("wrong 1", "con text value", "wrong 2"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotInIgnoreCaseStringArray()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NIi("WRONG 1", "CON TEXT VALUE", "WRONG 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NIi("WRONG 1", "CON TEXT VALUE", "WRONG 2"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotLikeString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NLIKE("%wrong%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NLIKE("%text%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKE("%wrong%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKE("%text%"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotLikeIgnoreCaseString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NLIKEi("%WRONG%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aText("conQueryText").NLIKEi("%TEXT%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKEi("%WRONG%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getConQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKEi("%TEXT%"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").EQ("2009-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").EQ("2009-05-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-05-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-05-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GT("2009-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GT("2009-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE("2009-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE("2009-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE("2009-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LT("2009-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LT("2009-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeltEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE("2009-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE("2009-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE("2009-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeltEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeltEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-07 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").NE("2009-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").NE("2009-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDateTime("conQueryDateTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateTimeNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-05 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2009-12-06 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").EQ("2009-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").EQ("2009-05-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-05-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-05-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GT("2009-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GT("2009-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE("2009-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE("2009-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE("2009-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LT("2009-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LT("2009-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateltEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE("2009-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE("2009-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE("2009-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateltEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateltEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-07", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").NE("2009-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").NE("2009-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDate("conQueryDate").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDateNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-05", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2009-12-06", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").EQ("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").EQ("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GT("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GT("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LT("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LT("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeLt()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeltEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeltEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeltEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").NE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").NE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeNotEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aTime("conQueryTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInTimeNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getConQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ(400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GT("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GT(399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GT(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GT(399));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GT(401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE(400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE(399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").GE(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LT(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LT(400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LT(401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LT(400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE(400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").LE(399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").NE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").NE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").NE(401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").NE(400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInIntegerNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.NE(401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.NE(400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").EQ("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").EQ("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").EQ((long) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").EQ((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.EQ((long) 400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.EQ((long) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GT("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GT((long) 399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GT((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GT((long) 399));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GT((long) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE((long) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE((long) 399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").GE((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LT((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LT((long) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LT((long) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LT((long) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE((long) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").LE((long) 399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").NE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").NE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").NE((long) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aLong("conQueryLong").NE((long) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInLongNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.NE((long) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getConQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.NE((long) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").EQ("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").EQ("401.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").EQ((float) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").EQ((float) 401.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.EQ((float) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.EQ((float) 401.5));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GT((float) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GT((float) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GT((float) 400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GT((float) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE((float) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE((float) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").GE((float) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LT((float) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LT((float) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LT((float) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LT((float) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE((float) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE((float) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").LE((float) 399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").NE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").NE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").NE((float) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aFloat("conQueryFloat").NE((float) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInFloatNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.NE((float) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getConQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.NE((float) 400.5));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").EQ("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").EQ("401.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").EQ(new BigDecimal(400.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").EQ(new BigDecimal(401.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.EQ(new BigDecimal(400.5)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.EQ(new BigDecimal(401.5)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GT(new BigDecimal(400)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GT(new BigDecimal(401)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GT(new BigDecimal(400)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GT(new BigDecimal(401)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE(new BigDecimal(400.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE(new BigDecimal(400)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").GE(new BigDecimal(401)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(400.5)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(400)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(401)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LT(new BigDecimal(401)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LT(new BigDecimal(400)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LT(new BigDecimal(401)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LT(new BigDecimal(400)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE(new BigDecimal(400.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE(new BigDecimal(401)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").LE(new BigDecimal(399)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(400.5)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(401)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(399)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").NE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").NE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").NE(new BigDecimal(401)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDecimal("conQueryDecimal").NE(new BigDecimal(400.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDecimalNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.NE(new BigDecimal(401)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getConQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.NE(new BigDecimal(400.5)));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").EQ("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").EQ("401.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").EQ((double) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").EQ((double) 401.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.EQ((double) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.EQ((double) 401.5));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GT((double) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GT((double) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GT((double) 400));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GT((double) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE((double) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE((double) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").GE((double) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLtString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LT("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LT("400"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LT((double) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LT((double) 400));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LT((double) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LT((double) 400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE("399"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLtEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE((double) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE((double) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").LE((double) 399));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 400.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 401));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 399));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleNotEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").NE("401"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").NE("400.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleNotEq()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").NE((double) 401));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aDouble("conQueryDouble").NE((double) 400.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInDoubleNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.NE((double) 401));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getConQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.NE((double) 400.5));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInReferenceEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.SYSTEM_USER_ID));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getId().equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.PUBLIC_USER_ID));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInReferenceEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeReference.EQ(ServerConstants.SYSTEM_USER_ID));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeReference.EQ(ServerConstants.PUBLIC_USER_ID));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testQueryIsParentInReferenceEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      // Load the reference query class
      MdBusinessDAOIF assignableMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(SingleActorDAOIF.CLASS);
      String assignableQueryReferenceIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(assignableMdBusinessMasterIF);
      Class<?> assignableQueryReferenceClassIF = LoaderDecorator.load(assignableQueryReferenceIFType);

      Class<?> AssignableClass = LoaderDecorator.load(SingleActorDAOIF.CLASS);

      // Instantiate a business object of type Users of the system user.
      Class<?> usersClass = LoaderDecorator.load(UserInfo.CLASS);
      Object busObjUser = usersClass.getMethod("get", String.class).invoke(null, ServerConstants.SYSTEM_USER_ID);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);

      Condition condition = (Condition) assignableQueryReferenceClassIF.getMethod("EQ", AssignableClass).invoke(attributeReference, busObjUser);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;

      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      busObjUser = usersClass.getMethod("get", String.class).invoke(null, ServerConstants.PUBLIC_USER_ID);

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);
      condition = (Condition) assignableQueryReferenceClassIF.getMethod("EQ", AssignableClass).invoke(attributeReference, busObjUser);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (hasNext)
      {
        iteratorClass.getMethod("close").invoke(resultIterator);
        Assert.fail("A query based on relationships returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }
}
