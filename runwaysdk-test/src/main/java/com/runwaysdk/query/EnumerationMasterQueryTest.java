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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;

@RunWith(ClasspathTestRunner.class)
public class EnumerationMasterQueryTest
{
  @Request
  @Test
  public void testEnumMasterAllItems()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_all.getType());
      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems.size(), childMdQuery.getCount());

      for (BusinessDAOIF object : iterator)
      {
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems)
        {
          if (object.getOid().equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that WILL NOT find a match
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_all.getType());
      relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      if (iterator.hasNext())
      {
        iterator.close();
        Assert.fail("A query returned objects when it shouldn't have.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumMasterAllItems_Generated()
  {
    try
    {
      String enumQueryMethodName = QueryMasterSetup.stateMdEnumeration_all.getQueryMethodName();

      String childType = QueryMasterSetup.stateClass.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod(enumQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      long resultCount = (Long) childQueryClass.getMethod("getCount").invoke(childQueryObject);

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems.size(), resultCount);

      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childOid = (String) childClass.getMethod("getOid").invoke(object);
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems)
        {
          if (childOid.equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      String negateQueryMethodName = QueryMasterSetup.stateMdEnumeration_all.getNegatedQueryMethodName();

      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genNotRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(negateQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genNotRelQuery);

      // Load the iterator class
      Class<?> iteratorClass = OIterator.class;
      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      Boolean hasNext = (Boolean) iteratorClass.getMethod("hasNext").invoke(resultIterator);
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
  public void testEnumMasterSomeItems1()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_east.getType());

      parentMdQuery.WHERE(parentMdQuery.aCharacter(MdTypeInfo.NAME).EQ(QueryMasterSetup.stateMdEnumeration_east.getTypeName()).AND(parentMdQuery.aCharacter(MdTypeInfo.PACKAGE).EQ(QueryMasterSetup.stateMdEnumeration_east.getPackage())));

      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_east.size(), childMdQuery.getCount());

      for (BusinessDAOIF object : iterator)
      {
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_east)
        {
          if (object.getOid().equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that returns the list of items that are not in the
      // enumeration
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_east.getType());

      parentMdQuery.WHERE(parentMdQuery.aCharacter(MdTypeInfo.NAME).EQ(QueryMasterSetup.stateMdEnumeration_east.getTypeName()).AND(parentMdQuery.aCharacter(MdTypeInfo.PACKAGE).EQ(QueryMasterSetup.stateMdEnumeration_east.getPackage())));

      relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_west.size(), childMdQuery.getCount());

      for (BusinessDAOIF object : iterator)
      {
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_west)
        {
          if (object.getOid().equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
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
  public void testEnumMasterSomeItems1_Generated()
  {
    try
    {
      String enumQueryMethodName = QueryMasterSetup.stateMdEnumeration_east.getQueryMethodName();

      String childType = QueryMasterSetup.stateClass.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod(enumQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      long resultCount = (Long) childQueryClass.getMethod("getCount").invoke(childQueryObject);

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_east.size(), resultCount);

      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childOid = (String) childClass.getMethod("getOid").invoke(object);
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_east)
        {
          if (childOid.equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      String negateQueryMethodName = QueryMasterSetup.stateMdEnumeration_east.getNegatedQueryMethodName();
      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genNotRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(negateQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genNotRelQuery);

      resultCount = (Long) childQueryClass.getMethod("getCount").invoke(childQueryObject);

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_west.size(), resultCount);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childOid = (String) childClass.getMethod("getOid").invoke(object);
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_west)
        {
          if (childOid.equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
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
  public void testEnumMasterSomeItems2()
  {
    try
    {
      // perform a query that WILL find a match
      QueryFactory factory = new QueryFactory();
      BusinessDAOQuery childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      BusinessDAOQuery parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_west.getType());

      parentMdQuery.WHERE(parentMdQuery.aCharacter(MdTypeInfo.NAME).EQ(QueryMasterSetup.stateMdEnumeration_west.getTypeName()).AND(parentMdQuery.aCharacter(MdTypeInfo.PACKAGE).EQ(QueryMasterSetup.stateMdEnumeration_west.getPackage())));

      RelationshipDAOQuery relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      OIterator<BusinessDAOIF> iterator = childMdQuery.getIterator();

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_west.size(), childMdQuery.getCount());

      for (BusinessDAOIF object : iterator)
      {
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_west)
        {
          if (object.getOid().equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      // perform a query that returns the list of items that are not in the
      // enumeration
      childMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateClass.getType());
      parentMdQuery = factory.businessDAOQuery(QueryMasterSetup.stateMdEnumeration_west.getType());

      parentMdQuery.WHERE(parentMdQuery.aCharacter(MdTypeInfo.NAME).EQ(QueryMasterSetup.stateMdEnumeration_west.getTypeName()).AND(parentMdQuery.aCharacter(MdTypeInfo.PACKAGE).EQ(QueryMasterSetup.stateMdEnumeration_west.getPackage())));

      relQuery = factory.relationshipDAOQuery(RelationshipTypes.ENUMERATION_ATTRIBUTE_ITEM.getType());
      childMdQuery.WHERE(childMdQuery.isNotChildIn(relQuery));
      relQuery.WHERE(relQuery.hasParent(parentMdQuery));

      iterator = childMdQuery.getIterator();

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_east.size(), childMdQuery.getCount());

      for (BusinessDAOIF object : iterator)
      {
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_east)
        {
          if (object.getOid().equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
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
  public void testEnumMasterSomeItems2_Generated()
  {
    try
    {
      String enumQueryMethodName = QueryMasterSetup.stateMdEnumeration_west.getQueryMethodName();

      String childType = QueryMasterSetup.stateClass.getType();
      Class<?> childClass = LoaderDecorator.load(childType);
      String childQueryType = EntityQueryAPIGenerator.getQueryClass(childType);
      Class<?> childQueryClass = LoaderDecorator.load(childQueryType);

      QueryFactory factory = new QueryFactory();
      Object childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionEq genRelQuery = (SubSelectBasicConditionEq) childQueryClass.getMethod(enumQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genRelQuery);

      long resultCount = (Long) childQueryClass.getMethod("getCount").invoke(childQueryObject);

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_west.size(), resultCount);

      Object resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childOid = (String) childClass.getMethod("getOid").invoke(object);
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_west)
        {
          if (childOid.equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
        {
          Assert.fail("The objects returned by a query based on relationship type are incorrect.");
        }
      }

      String negateQueryMethodName = QueryMasterSetup.stateMdEnumeration_west.getNegatedQueryMethodName();
      childQueryObject = childQueryClass.getConstructor(QueryFactory.class).newInstance(factory);
      SubSelectBasicConditionNotEq genNotRelQuery = (SubSelectBasicConditionNotEq) childQueryClass.getMethod(negateQueryMethodName).invoke(childQueryObject);
      childQueryClass.getMethod("WHERE", Condition.class).invoke(childQueryObject, genNotRelQuery);

      resultCount = (Long) childQueryClass.getMethod("getCount").invoke(childQueryObject);

      Assert.assertEquals("Query returned the wrong number of results", (long) QueryMasterSetup.enumerationItems_east.size(), resultCount);

      resultIterator = childQueryClass.getMethod(EntityQueryAPIGenerator.ITERATOR_METHOD).invoke(childQueryObject);

      for (Object object : (Iterable<?>) resultIterator)
      {
        childClass.cast(object);
        String childOid = (String) childClass.getMethod("getOid").invoke(object);
        boolean foundMatch = false;
        for (EnumerationItemDAO enumItem : QueryMasterSetup.enumerationItems_east)
        {
          if (childOid.equals(enumItem.getOid()))
          {
            foundMatch = true;
          }
        }

        if (!foundMatch)
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
}
