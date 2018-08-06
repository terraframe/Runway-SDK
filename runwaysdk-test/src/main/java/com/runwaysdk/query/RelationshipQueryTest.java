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

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.RelationshipQuery;
import com.runwaysdk.business.generation.BusinessQueryAPIGenerator;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class RelationshipQueryTest
{
  protected static BusinessDAO     testQueryObject2    = null;

  protected static BusinessDAO     relQueryObject2     = null;

  protected static RelationshipDAO connectionInstance2 = null;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    testQueryObject2 = BusinessDAO.newInstance(QueryMasterSetup.childMdBusiness.definesType());
    testQueryObject2.addItem("queryEnumeration", QueryMasterSetup.coloradoItemId);
    testQueryObject2.addItem("queryEnumeration", QueryMasterSetup.californiaItemId);
    testQueryObject2.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    testQueryObject2.setValue("queryCharacter", "other character value");
    testQueryObject2.setValue("queryInteger", "100");
    testQueryObject2.apply();

    ///////////////////////////////////////////////////////////////////////////////////////////
    relQueryObject2 = BusinessDAO.newInstance(QueryMasterSetup.relQueryInfo.getType());
    relQueryObject2.addItem("relQueryEnumeration", QueryMasterSetup.californiaItemId);
    relQueryObject2.setValue("relQueryBoolean", MdAttributeBooleanInfo.TRUE);
    relQueryObject2.setValue("relQueryCharacter", "some other value");
    relQueryObject2.setValue("relQueryInteger", "500");
    relQueryObject2.apply();

    connectionInstance2 = relQueryObject2.addParent(testQueryObject2.getOid(), QueryMasterSetup.connectionMdRel.definesType());

    connectionInstance2.addItem("conQueryEnumeration", QueryMasterSetup.californiaItemId);
    connectionInstance2.setValue("conQueryBoolean", MdAttributeBooleanInfo.TRUE);
    connectionInstance2.setValue("conQueryCharacter", "con other char value");
    connectionInstance2.setValue("conQueryText", "con text value");
    connectionInstance2.setValue("conQueryDateTime", "2009-12-06 13:00:00");
    connectionInstance2.setValue("conQueryDate", "2009-12-06");
    connectionInstance2.setValue("conQueryTime", "13:00:00");
    connectionInstance2.setValue("conQueryInteger", "400");
    connectionInstance2.setValue("conQueryLong", "400");
    connectionInstance2.setValue("conQueryFloat", "400.5");
    connectionInstance2.setValue("conQueryDecimal", "400.5");
    connectionInstance2.setValue("conQueryDouble", "400.5");

    connectionInstance2.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    connectionInstance2.delete();
    relQueryObject2.delete();
    testQueryObject2.delete();
  }

  /**
   * Performs a generic query to return all instances of a relationship type.
   */
  @Request
  @Test
  public void testQueryRelationshipDAO()
  {
    try
    {
      QueryFactory factory = new QueryFactory();
      RelationshipDAOQuery query = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());

      OIterator<RelationshipDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Performs a generic query to return all instances of a business
   * relationship.
   */
  @Request
  @Test
  public void testQueryRelationship()
  {
    try
    {
      QueryFactory factory = new QueryFactory();
      RelationshipQuery query = factory.relationshipQuery(QueryMasterSetup.connectionQueryInfo.getType());

      OIterator<com.runwaysdk.business.Relationship> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (com.runwaysdk.business.Relationship object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Performs a type safe query to return all instances of a business
   * relationship.
   */

  @Request
  @Test
  public void testQueryRelationship_Generated()
  {
    try
    {
      String type = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(type);
      String queryType = EntityQueryAPIGenerator.getQueryClass(type);
      Class<?> relQueryClass = LoaderDecorator.load(queryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()) && !relId.equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Performs a query to return an instance of a relationship type.
   */
  @Request
  @Test
  public void testQueryGetRelationship()
  {
    try
    {
      RelationshipDAOQuery query = RelationshipDAOQuery.getRelationshipInstance(QueryMasterSetup.testQueryObject1.getOid(), QueryMasterSetup.relQueryObject1.getOid(), QueryMasterSetup.connectionMdRel.definesType());

      OIterator<RelationshipDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Performs a query to return an instance of a relationship type.
   */
  @Request
  @Test
  public void testQueryGetRelationshipId()
  {
    try
    {
      RelationshipDAOQuery query = RelationshipDAOQuery.getRelationshipInstance(QueryMasterSetup.connectionInstance1.getOid());

      OIterator<RelationshipDAOIF> iterator = query.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
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
  public void testQueryHasChild()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.hasChild(childMdQuery));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
      relQuery.WHERE(relQuery.hasChild(childMdQuery));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryHasChild_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String badRelType = QueryMasterSetup.badConnectionQueryInfo.getType();
      String badRelQueryType = EntityQueryAPIGenerator.getQueryClass(badRelType);
      Class<?> badRelQueryClass = LoaderDecorator.load(badRelQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object condition = relQueryClass.getMethod("hasChild", childQueryClass).invoke(relQueryObject, childQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()) && !relId.equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object badRelQueryObject = badRelQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      condition = badRelQueryClass.getMethod("hasChild", childQueryClass).invoke(badRelQueryObject, childQueryObject);
      badRelQueryClass.getMethod("WHERE", Condition.class).invoke(badRelQueryObject, condition);

      resultIterator = badRelQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(badRelQueryObject);

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
  public void testQueryDoesNotHaveChild()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.aCharacter("relQueryCharacter").EQ("some other value"));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.doesNotHaveChild(childMdQuery));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.doesNotHaveChild(childMdQuery));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryDoesNotHaveChild_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) childQueryClass.getMethod("getRelQueryCharacter").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, attributeChar.EQ("some other value"));
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object condition = relQueryClass.getMethod("doesNotHaveChild", childQueryClass).invoke(relQueryObject, childQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      condition = relQueryClass.getMethod("doesNotHaveChild", childQueryClass).invoke(relQueryObject, childQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

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
  public void testQueryHasParent()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.parentQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.parentQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryHasParent_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.selectedMdBusiness.definesType();
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String badRelType = QueryMasterSetup.badConnectionQueryInfo.getType();
      String badRelQueryType = EntityQueryAPIGenerator.getQueryClass(badRelType);
      Class<?> badRelQueryClass = LoaderDecorator.load(badRelQueryType);

      QueryFactory factory = new QueryFactory();
      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object condition = relQueryClass.getMethod("hasParent", parentQueryClass).invoke(relQueryObject, parentQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()) && !relId.equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object badRelQueryObject = badRelQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      condition = badRelQueryClass.getMethod("hasParent", parentQueryClass).invoke(badRelQueryObject, parentQueryObject);
      badRelQueryClass.getMethod("WHERE", Condition.class).invoke(badRelQueryObject, condition);

      resultIterator = badRelQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(badRelQueryObject);

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
  public void testQueryDoesNotHaveParent()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childMdBusiness.definesType());
      parentMdQuery.WHERE(parentMdQuery.aCharacter("queryCharacter").EQ("other character value"));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.doesNotHaveParent(parentMdQuery));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.parentQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.doesNotHaveParent(parentMdQuery));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryDoesNotHaveParent_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.selectedMdBusiness.definesType();
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) parentQueryClass.getMethod("getQueryCharacter").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, attributeChar.EQ("other character value"));
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      Object condition = relQueryClass.getMethod("doesNotHaveParent", parentQueryClass).invoke(relQueryObject, parentQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()) && !relId.equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      condition = relQueryClass.getMethod("doesNotHaveParent", parentQueryClass).invoke(relQueryObject, parentQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, condition);

      resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

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
  public void testQueryChildId()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.childId().EQ(QueryMasterSetup.relQueryObject1.getOid()));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.childId().EQ(QueryMasterSetup.testQueryObject1.getOid()));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryChildId_Generated()
  {
    try
    {
      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) relQueryClass.getMethod("childId").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeCharacter.EQ(QueryMasterSetup.relQueryObject1.getOid()));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) relQueryClass.getMethod("childId").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeCharacter.EQ(QueryMasterSetup.testQueryObject1.getOid()));

      resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

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
  public void testQueryParentId()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.parentId().EQ(QueryMasterSetup.testQueryObject1.getOid()));

      OIterator<RelationshipDAOIF> iterator = relQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (RelationshipDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.connectionInstance1.getOid()) && !object.getOid().equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.parentId().EQ(QueryMasterSetup.relQueryObject1.getOid()));

      iterator = relQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryParentId_Generated()
  {
    try
    {
      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      Class<?> relClass = LoaderDecorator.load(relType);
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      AttributeCharacter attributeCharacter = (AttributeCharacter) relQueryClass.getMethod("parentId").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeCharacter.EQ(QueryMasterSetup.testQueryObject1.getOid()));

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        relClass.cast(object);
        String relId = (String) relClass.getMethod("getOid").invoke(object);
        if (!relId.equals(QueryMasterSetup.connectionInstance1.getOid()) && !relId.equals(connectionInstance2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeCharacter = (AttributeCharacter) relQueryClass.getMethod("parentId").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeCharacter.EQ(QueryMasterSetup.relQueryObject1.getOid()));

      resultIterator = relQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(relQueryObject);

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
  public void testQueryIsChildIn()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsChildIn_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("queryParent3").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq badGenRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("badQueryParent3").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, badGenRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsNotChildIn()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsNotChildIn_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq badGenRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "badQueryParent3").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, badGenRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsChildInRelationshipAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con character value"));
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("wrong con character value"));
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsChildInRelationshipAttribute_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con character value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("queryParent3", relQueryClass).invoke(childQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("wrong con character value"));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("queryParent3", relQueryClass).invoke(childQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsNotChildInRelationshipAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con other char value"));
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      // Every relationship of this type has the integer attribute equaling 400.
      // Hence, there are
      // no instances that are not in that set.
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("400"));
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsNotChildInRelationshipAttribute_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con other char value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", relQueryClass).invoke(childQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      // Every relationship of this type has the integer attribute equaling 400.
      // Hence, there are
      // no instances that are not in that set.
      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(400));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", relQueryClass).invoke(childQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsChildInRelationshipAndParentAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aCharacter("queryCharacter").EQ("some character value"));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con character value"));
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aCharacter("queryCharacter").EQ("wrong some character value"));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("wrong con character value"));
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsChildInRelationshipAndParentAttribute_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String parentType = QueryMasterSetup.childQueryInfo.getType();
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String superParentType = QueryMasterSetup.selectedMdBusiness.definesType();
      String superParentQueryType = EntityQueryAPIGenerator.getQueryClass(superParentType);
      Class<?> superParentQueryClass = LoaderDecorator.load(superParentQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar parentAttributeChar = (SelectableChar) parentQueryClass.getMethod("getQueryCharacter").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeChar.EQ("some character value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("queryParent3", superParentQueryClass, relQueryClass).invoke(childQueryObject, parentQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("wrong character value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      parentAttributeChar = (SelectableChar) parentQueryClass.getMethod("getQueryCharacter").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeChar.EQ("wrong character value"));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod("queryParent3", superParentQueryClass, relQueryClass).invoke(childQueryObject, parentQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsNotChildInParentAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aCharacter("queryCharacter").EQ("some other value"));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aInteger("queryInteger").EQ("100"));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsNotChildInParentAttribute_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String parentType = QueryMasterSetup.childQueryInfo.getType();
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String superParentType = QueryMasterSetup.selectedMdBusiness.definesType();
      String superParentQueryType = EntityQueryAPIGenerator.getQueryClass(superParentType);
      Class<?> superParentQueryClass = LoaderDecorator.load(superParentQueryType);

      QueryFactory factory = new QueryFactory();
      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar parentAttributeChar = (SelectableChar) parentQueryClass.getMethod("getQueryCharacter").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeChar.EQ("some other value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", superParentQueryClass).invoke(childQueryObject, parentQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger parentAttributeInteger = (SelectableInteger) parentQueryClass.getMethod("getQueryInteger").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeInteger.EQ(100));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", superParentQueryClass).invoke(childQueryObject, parentQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsNotChildInRelationshipAndParentAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aCharacter("queryCharacter").EQ("some other value"));
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con other char value"));
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.relQueryObject1.getOid()) && !object.getOid().equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.aInteger("queryInteger").EQ("100"));
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("400"));
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsNotChildInRelationshipAndParentAttribute_Generated()
  {
    try
    {
      String childType = QueryMasterSetup.relQueryInfo.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      String relType = QueryMasterSetup.connectionQueryInfo.getType();
      String relQueryType = EntityQueryAPIGenerator.getQueryClass(relType);
      Class<?> relQueryClass = LoaderDecorator.load(relQueryType);

      String parentType = QueryMasterSetup.childQueryInfo.getType();
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String superParentType = QueryMasterSetup.selectedMdBusiness.definesType();
      String superParentQueryType = EntityQueryAPIGenerator.getQueryClass(superParentType);
      Class<?> superParentQueryClass = LoaderDecorator.load(superParentQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con other char value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar parentAttributeChar = (SelectableChar) parentQueryClass.getMethod("getQueryCharacter").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeChar.EQ("some other value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", superParentQueryClass, relQueryClass).invoke(childQueryObject, parentQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);

      if (!hasNext)
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childId = (String) childClass.getMethod("getOid").invoke(object);
        if (!childId.equals(QueryMasterSetup.relQueryObject1.getOid()) && !childId.equals(relQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger parentAttributeInteger = (SelectableInteger) parentQueryClass.getMethod("getQueryInteger").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, parentAttributeInteger.EQ(100));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryParent3", superParentQueryClass, relQueryClass).invoke(childQueryObject, parentQueryObject, relQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

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
  public void testQueryIsParentIn()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()) && !object.getOid().equals(testQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
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
  public void testQueryIsParentIn_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      QueryFactory factory = new QueryFactory();
      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3").invoke(parentQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()) && !parentId.equals(testQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq badGenRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("badQueryChild3").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, badGenRelQuery);

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
  public void testQueryIsNotParentIn()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.badConnectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()) && !object.getOid().equals(testQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));

      iterator = parentMdQuery.getIterator();

      if (iterator.hasNext())
      {
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
  public void testQueryIsNotParentIn_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      QueryFactory factory = new QueryFactory();
      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "badQueryChild3").invoke(parentQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()) && !parentId.equals(testQueryObject2.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq badGenRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3").invoke(parentQueryObject);
      parentQueryClass.getMethod("WHERE", Condition.class).invoke(parentQueryObject, badGenRelQuery);

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

  /**
   * Queries by an attribute on the relationship and on the child object.
   **/
  @Request
  @Test
  public void testQueryIsParentInRelationshipAndChildAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con character value"));
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
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("wrong character value"));
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

  @Request
  @Test
  public void testQueryIsParentInRelationshipAndChildAttribute_Generated()
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

      String childType = QueryMasterSetup.relQueryInfo.getType();
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con character value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar childAttributeChar = (SelectableChar) childQueryClass.getMethod("getRelQueryCharacter").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeChar.EQ("child character value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", childQueryClass, relQueryClass).invoke(parentQueryObject, childQueryObject, relQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("wrong con character value"));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      childAttributeChar = (SelectableChar) childQueryClass.getMethod("getRelQueryCharacter").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeChar.EQ("wrong child character value"));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionEq) parentQueryClass.getMethod("queryChild3", childQueryClass, relQueryClass).invoke(parentQueryObject, childQueryObject, relQueryObject);
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

  /**
   * Queries by an attribute on the relationship and on the child object.
   **/
  @Request
  @Test
  public void testQueryIsParentNotInRelationshipAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con other char value"));
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("400"));
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
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
  public void testQueryIsNotParentInRelationshipAttribute_Generated()
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
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con other char value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(400));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", relQueryClass).invoke(parentQueryObject, relQueryObject);
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

  /**
   * Queries by an attribute on the relationship and on the child object.
   **/
  @Request
  @Test
  public void testQueryIsParentNotInChildAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQ("some other value"));
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ("500"));
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
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
  public void testQueryIsParentNotInChildAttribute_Generated()
  {
    try
    {
      String parentType = QueryMasterSetup.childQueryInfo.getType();
      Class<?> parentClass = LoaderDecorator.load(parentType);
      String parentQueryType = EntityQueryAPIGenerator.getQueryClass(parentType);
      Class<?> parentQueryClass = LoaderDecorator.load(parentQueryType);

      String childType = QueryMasterSetup.relQueryInfo.getType();
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar childAttributeChar = (SelectableChar) childQueryClass.getMethod("getRelQueryCharacter").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeChar.EQ("some other value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", childQueryClass).invoke(parentQueryObject, childQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger childAttributeInteger = (SelectableInteger) childQueryClass.getMethod("getRelQueryInteger").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeInteger.EQ(500));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", childQueryClass).invoke(parentQueryObject, childQueryObject);
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

  /**
   * Queries by an attribute on the relationship and on the child object.
   **/
  @Request
  @Test
  public void testQueryIsParentNotInRelationshipAndChildAttribute()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aCharacter("conQueryCharacter").EQ("con other char value"));
      BusinessDAOQuery childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aCharacter("relQueryCharacter").EQ("some other value"));
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
      relQuery.WHERE(relQuery.hasChild(childQuery));

      OIterator<BusinessDAOIF> iterator = parentMdQuery.getIterator();

      if (!iterator.hasNext())
      {
        Assert.fail("A query did not return any results when it should have");
      }

      for (BusinessDAOIF object : iterator)
      {
        if (!object.getOid().equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.childQueryInfo.getType());
      relQuery = factory.relationshipDAOQuery(QueryMasterSetup.connectionQueryInfo.getType());
      relQuery.WHERE(relQuery.aInteger("conQueryInteger").EQ("400"));
      childQuery = factory.businessDAOQuery(QueryMasterSetup.relQueryInfo.getType());
      childQuery.WHERE(childQuery.aInteger("relQueryInteger").EQ("500"));
      parentMdQuery.WHERE(parentMdQuery.isNotParentIn(relQuery));
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
  public void testQueryIsNotParentInRelationshipAndChildAttribute_Generated()
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

      String childType = QueryMasterSetup.relQueryInfo.getType();
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar attributeChar = (SelectableChar) relQueryClass.getMethod("getConQueryCharacter").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeChar.EQ("con other char value"));

      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableChar childAttributeChar = (SelectableChar) childQueryClass.getMethod("getRelQueryCharacter").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeChar.EQ("some other value"));

      Object parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", childQueryClass, relQueryClass).invoke(parentQueryObject, childQueryObject, relQueryObject);
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
        String parentId = (String) parentClass.getMethod("getOid").invoke(object);
        if (!parentId.equals(QueryMasterSetup.testQueryObject1.getOid()))
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      relQueryObject = relQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger attributeInteger = (SelectableInteger) relQueryClass.getMethod("getConQueryInteger").invoke(relQueryObject);
      relQueryClass.getMethod("WHERE", Condition.class).invoke(relQueryObject, attributeInteger.EQ(400));

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SelectableInteger childAttributeInteger = (SelectableInteger) childQueryClass.getMethod("getRelQueryInteger").invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, childAttributeInteger.EQ(500));

      parentQueryObject = parentQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      genRelQuery = (SubSelectBasicConditionNotEq) parentQueryClass.getMethod(BusinessQueryAPIGenerator.NOT_IN_RELATIONSHIP_PREFIX + "queryChild3", childQueryClass, relQueryClass).invoke(parentQueryObject, childQueryObject, relQueryObject);
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
