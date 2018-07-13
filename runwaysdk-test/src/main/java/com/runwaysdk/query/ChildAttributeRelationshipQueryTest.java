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

public class ChildAttributeRelationshipQueryTest
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAll_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsAny(QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsAny(QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsAny_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").containsExactly(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId, QueryMasterSetup.connecticutItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInEnumerationContainsExactly_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CA", "CO");
      Method method = stateEnumQueryIFClass.getMethod("containsExactly", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").notContainsAll(QueryMasterSetup.coloradoItemId, QueryMasterSetup.kansasItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").notContainsAll(QueryMasterSetup.californiaItemId, QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testEnumerationNotContainsAll_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CO", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAll", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").notContainsAny(QueryMasterSetup.connecticutItemId, QueryMasterSetup.kansasItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery.WHERE(childQuery.aEnumeration("relQueryEnumeration").notContainsAny(QueryMasterSetup.coloradoItemId));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testEnumerationNotContainsAny_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      String stateEnumType = QueryMasterSetup.stateEnum_all.getType();
      Class stateEnumClass = LoaderDecorator.load(stateEnumType);
      Object[] enumConstants = stateEnumClass.getEnumConstants();

      MdEnumerationDAOIF stateMdEnumIF = MdEnumerationDAO.getMdEnumerationDAO(QueryMasterSetup.stateEnum_all.getType());
      String stateEnumQueryIFType = BusinessQueryAPIGenerator.getEnumSubInterfaceCompiled(stateMdEnumIF);
      Class stateEnumQueryIFClass = LoaderDecorator.load(stateEnumQueryIFType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

      Object enumArray = QueryMasterSetup.getStateEnumConstants(stateEnumClass, enumConstants, "CT", "KS");
      Method method = stateEnumQueryIFClass.getMethod("notContainsAny", Array.newInstance(stateEnumClass, 0).getClass());
      Condition condition = (Condition) method.invoke(attributeEnumeration, enumArray);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeEnumeration = relQueryClass.getMethod("getRelQueryEnumeration").invoke(relQueryObject);

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").EQ(MdAttributeBooleanInfo.TRUE));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").EQ(MdAttributeBooleanInfo.FALSE));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").EQ(true));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").EQ(false));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInBooleanEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getRelQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.EQ(true));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getRelQueryBoolean").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").NE(MdAttributeBooleanInfo.FALSE));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").NE(MdAttributeBooleanInfo.TRUE));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").NE(false));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aBoolean("relQueryBoolean").NE(true));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInBooleanNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableBoolean attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getRelQueryBoolean").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeBoolean.NE(false));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeBoolean = (SelectableBoolean) relQueryClass.getMethod("getRelQueryBoolean").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQ("child character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQ("wrong character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("child character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQi("CHILD CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQi("WRONG CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQi("CHILD CHARACTER VALUE"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").IN("wrong value 1", "child character value", "wrong value 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").IN("wrong value 1", "wrong value 2", "wrong value 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.IN("wrong value 1", "child character value", "wrong value 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").INi("WRONG VALUE 1", "CHILD CHARACTER VALUE", "WRONG VALUE 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterInIngoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.INi("WRONG VALUE 1", "CHILD CHARACTER VALUE", "WRONG VALUE 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").LIKE("%character%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").LIKE("%character"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKE("%character%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").LIKEi("%CHARACTER%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").LIKEi("%CHARACTER"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.LIKEi("%CHARACTER%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NE("wrong character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NE("child character value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NE("wrong character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NE("child character value"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NEi("WRONG CHARACTER STRING"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NEi("CHILD CHARACTER VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NEi("WRONG CHARACTER STRING"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NEi("CHILD CHARACTER VALUE"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NI("wrong 1", "wrong 2", "wrong 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NI("wrong 1", "child character value", "wrong 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NI("wrong 1", "wrong 2", "wrong 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NI("wrong 1", "child character value", "wrong 2"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NIi("WRONG 1", "WRONG 2", "WRONG 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NIi("WRONG 1", "CHILD CHARACTER VALUE", "WRONG 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NIi("WRONG 1", "CHILD CHARACTER VALUE", "WRONG 2"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NLIKE("%wrong%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NLIKE("%character%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKE("%wrong%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NLIKEi("%WRONG%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").NLIKEi("%CHARACTER%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInCharacterNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.NLIKEi("%WRONG%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getRelQueryCharacter").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").EQ("child text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").EQ("wrong text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQ("child text value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").EQi("CHILD TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").EQi("WRONG TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.EQi("CHILD TEXT VALUE"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").IN("wrong value 1", "child text value", "wrong value 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").IN("wrong value 1", "wrong value 2", "wrong value 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.IN("wrong value 1", "child text value", "wrong value 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").INi("WRONG VALUE 1", "CHILD TEXT VALUE", "WRONG VALUE 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").INi("WRONG VALUE 1", "WRONG VALUE 2", "WRONG VALUE 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.INi("WRONG VALUE 1", "CHILD TEXT VALUE", "WRONG VALUE 2"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").LIKE("%text%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").LIKE("%text"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextLikeStringGenerated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKE("%text%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").LIKEi("%TEXT%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").LIKEi("%TEXT"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.LIKEi("%TEXT%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NE("wrong text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NE("child text value"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NE("wrong text value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NE("child text value"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NEi("WRONG TEXT STRING"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NEi("CHILD TEXT VALUE"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotEqIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NEi("WRONG TEXT STRING"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NEi("CHILD TEXT VALUE"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NI("wrong 1", "wrong 2", "wrong 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NI("wrong 1", "child text value", "wrong 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotInStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NI("wrong 1", "wrong 2", "wrong 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NI("wrong 1", "child text value", "wrong 2"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NIi("WRONG 1", "WRONG 2", "WRONG 3"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NIi("WRONG 1", "CHILD TEXT VALUE", "WRONG 2"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotInIgnoreCaseStringArray_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NIi("WRONG 1", "WRONG 2", "WRONG 3"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NIi("WRONG 1", "CHILD TEXT VALUE", "WRONG 2"));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NLIKE("%wrong%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NLIKE("%text%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotLikeString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKE("%wrong%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NLIKEi("%WRONG%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aText("relQueryText").NLIKEi("%TEXT%"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTextNotLikeIgnoreCaseString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeText.NLIKEi("%WRONG%"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeText = (SelectableChar) relQueryClass.getMethod("getRelQueryText").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").EQ("2004-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").EQ("2004-05-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-05-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-05-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GT("2004-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GT("2004-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE("2004-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE("2004-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE("2004-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LT("2004-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LT("2004-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
  public void testQueryIsParentInDateTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE("2004-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE("2004-12-07 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE("2004-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
  public void testQueryIsParentInDateTimeLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeltEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-07 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").NE("2004-12-05 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").NE("2004-12-06 13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDateTime("relQueryDateTime").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateTimeNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-05 13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATETIME_FORMAT).parse("2004-12-06 13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDateTime").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").EQ("2004-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").EQ("2004-05-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-05-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").EQ(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-05-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GT("2004-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GT("2004-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE("2004-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE("2004-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE("2004-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").GE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
  public void testQueryIsParentInDateStringLt()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LT("2004-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LT("2004-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LT(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
  public void testQueryIsParentInDateLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE("2004-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE("2004-12-07"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE("2004-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
  public void testQueryIsParentInDateLtEq()
  {
    try
    {
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").LE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-07", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").NE("2004-12-05"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").NE("2004-12-06"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDate("relQueryDate").NE(date));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDateNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-05", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(date));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      date = new SimpleDateFormat(Constants.DATE_FORMAT).parse("2004-12-06", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryDate").invoke(relQueryObject);
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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").EQ("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").EQ("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").EQ(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").EQ(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.EQ(time));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GT("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GT("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GT(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GT(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GT(time));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").GE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(time));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.GE(time));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LT("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LT("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LT(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LT(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LT(time));

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
  public void testQueryIsParentInTimeLtEqString()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE("14:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
  public void testQueryIsParentInTimeLtEq()
  {
    try
    {
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").LE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("14:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(time));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.LE(time));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").NE("12:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").NE("13:00:00"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").NE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aTime("relQueryTime").NE(time));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInTimeNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      Date time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("12:00:00", new java.text.ParsePosition(0));

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableMoment attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(time));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      time = new SimpleDateFormat(Constants.TIME_FORMAT).parse("13:00:00", new java.text.ParsePosition(0));

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeMoment = (SelectableMoment) relQueryClass.getMethod("getRelQueryTime").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeMoment.NE(time));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ(500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GT("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GT(499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GT(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GT(499));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GT(501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE(500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE(499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").GE(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(499));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.GE(501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LT(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LT(500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LT(501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LT(500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE(500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").LE(499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(501));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.LE(499));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").NE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").NE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").NE(501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").NE(500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInIntegerNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.NE(501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeInteger = (SelectableInteger) relQueryClass.getMethod("getRelQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.NE(500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").EQ("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").EQ("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").EQ((long) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").EQ((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.EQ((long) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.EQ((long) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GT("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GT((long) 499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GT((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GT((long) 499));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GT((long) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE((long) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE((long) 499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").GE((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 499));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.GE((long) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LT((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LT((long) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LT((long) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LT((long) 500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE((long) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").LE((long) 499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 501));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.LE((long) 499));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").NE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").NE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").NE((long) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aLong("relQueryLong").NE((long) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInLongNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableLong attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.NE((long) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeLong = (SelectableLong) relQueryClass.getMethod("getRelQueryLong").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeLong.NE((long) 500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").EQ("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").EQ("501.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").EQ((float) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").EQ((float) 501.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.EQ((float) 500.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.EQ((float) 501.5));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GT((float) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GT((float) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GT((float) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GT((float) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE((float) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE((float) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").GE((float) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 500));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.GE((float) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LT((float) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LT((float) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LT((float) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LT((float) 500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE((float) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE((float) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").LE((float) 499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 500.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 501));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.LE((float) 499));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").NE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").NE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").NE((float) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aFloat("relQueryFloat").NE((float) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInFloatNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableFloat attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.NE((float) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeFloat = (SelectableFloat) relQueryClass.getMethod("getRelQueryFloat").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeFloat.NE((float) 500.5));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").EQ("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").EQ("501.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").EQ(new BigDecimal(500.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").EQ(new BigDecimal(501.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.EQ(new BigDecimal(500.5)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.EQ(new BigDecimal(501.5)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GT(new BigDecimal(500)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GT(new BigDecimal(501)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GT(new BigDecimal(500)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GT(new BigDecimal(501)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE(new BigDecimal(500.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE(new BigDecimal(500)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").GE(new BigDecimal(501)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(500)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(500)));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.GE(new BigDecimal(501)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LT(new BigDecimal(501)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LT(new BigDecimal(500)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LT(new BigDecimal(501)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LT(new BigDecimal(500)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE(new BigDecimal(500.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE(new BigDecimal(501)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").LE(new BigDecimal(499)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(500.5)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(501)));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.LE(new BigDecimal(499)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").NE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").NE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").NE(new BigDecimal(501)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDecimal("relQueryDecimal").NE(new BigDecimal(500.5)));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDecimalNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDecimal attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.NE(new BigDecimal(501)));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDecimal = (SelectableDecimal) relQueryClass.getMethod("getRelQueryDecimal").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDecimal.NE(new BigDecimal(500.5)));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").EQ("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").EQ("501.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").EQ((double) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").EQ((double) 501.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.EQ((double) 500.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.EQ((double) 501.5));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GT((double) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GT((double) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleGt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GT((double) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GT((double) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE((double) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE((double) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").GE((double) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleGtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 500));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 500));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.GE((double) 501));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LT("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LT("500"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LT((double) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LT((double) 500));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleLt_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LT((double) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LT((double) 500));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE("499"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE((double) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE((double) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));
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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").LE((double) 499));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleLtEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 500.5));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 501));

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

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.LE((double) 499));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").NE("501"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").NE("500.5"));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").NE((double) 501));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aDouble("relQueryDouble").NE((double) 500.5));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInDoubleNotEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableDouble attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.NE((double) 501));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
      {
        parentClass.cast(object);
        String parentId = (String) parentClass.getMethod("getId").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getId()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeDouble = (SelectableDouble) relQueryClass.getMethod("getRelQueryDouble").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeDouble.NE((double) 500.5));

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
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.SYSTEM_USER_ID));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aReference(ElementInfo.CREATED_BY).EQ(ServerConstants.PUBLIC_USER_ID));
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInReferenceEqString_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeReference.EQ(ServerConstants.SYSTEM_USER_ID));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
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

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Request
  @Test
  public void testQueryIsParentInReferenceEq_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.relQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class relQueryClass = LoaderDecorator.load(relQueryType);

      // Load the reference query class
      MdBusinessDAOIF assignableMdBusinessMasterIF = MdBusinessDAO.getMdBusinessDAO(SingleActorDAOIF.CLASS);
      String assignableQueryRefIFType = BusinessQueryAPIGenerator.getRefInterfaceCompiled(assignableMdBusinessMasterIF);
      Class assignableQueryRefClassIF = LoaderDecorator.load(assignableQueryRefIFType);

      Class AssignableClass = LoaderDecorator.load(SingleActorDAOIF.CLASS);

      // Instantiate a business object of type Users of the system user.
      Class usersClass = LoaderDecorator.load(UserInfo.CLASS);
      Object busObjUser = usersClass.getMethod("get", String.class).invoke(null, ServerConstants.SYSTEM_USER_ID);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableReference attributeReference = (SelectableReference) relQueryClass.getMethod("getCreatedBy").invoke(relQueryObject);

      Condition condition = (Condition) assignableQueryRefClassIF.getMethod("EQ", AssignableClass).invoke(attributeReference, busObjUser);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, genRelQuery);

      // Load the iterator class
      Class iteratorClass = OIterator.class;
      Object resultIterator = parentQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(parentQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable) resultIterator)
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
      condition = (Condition) assignableQueryRefClassIF.getMethod("EQ", AssignableClass).invoke(attributeReference, busObjUser);
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
