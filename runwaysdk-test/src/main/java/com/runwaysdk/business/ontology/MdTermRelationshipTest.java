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
package com.runwaysdk.business.ontology;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.TermInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateGraphPathException;
import com.runwaysdk.dataaccess.MdTermRelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.RelationshipRecursionException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.RelationshipDefinitionException;
import com.runwaysdk.session.Request;

public class MdTermRelationshipTest
{
  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
  }

  /**
   * If testObject was applied, it is removed from the database.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
  }

  @Request
  @Test
  public void testCreateAndGetMdTermRelationship()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        MdTermRelationshipDAOIF result = MdTermRelationshipDAO.getMdTermRelationshipDAO(mdTermRelationship.definesType());

        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(MdTermInfo.NAME), mdTermRelationship.getValue(MdTermInfo.NAME));
        Assert.assertEquals(result.getValue(MdTermInfo.PACKAGE), mdTermRelationship.getValue(MdTermInfo.PACKAGE));
        Assert.assertEquals(result.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationship.getStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
        Assert.assertEquals(result.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), mdTermRelationship.getStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE));
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  @Request
  @Test
  public void testInvalidParentType()
  {
    MdBusinessDAO parentMdBusiness = TestFixtureFactory.createMdBusiness1();
    parentMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.apply();

    try
    {
      MdTermDAO mdTerm = MdTermDAO.newInstance();
      mdTerm.setValue(MdTermInfo.NAME, "Term");
      mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
      mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
      mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTerm.apply();

      try
      {
        MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
        mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
        mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
        mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, parentMdBusiness.getOid());
        mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
        mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
        mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getOid());
        mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
        mdTermRelationship.apply();

        TestFixtureFactory.delete(mdTermRelationship);

        Assert.fail("Able to create a MdTermRelationship with a non MdTerm parent type");
      }
      catch (Exception e)
      {
        Assert.assertTrue(e instanceof RelationshipDefinitionException);
      }
      finally
      {
        TestFixtureFactory.delete(mdTerm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(parentMdBusiness);
    }
  }

  @Request
  @Test
  public void testInvalidChildType()
  {
    MdBusinessDAO childMdBusiness = TestFixtureFactory.createMdBusiness1();
    childMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    childMdBusiness.apply();

    try
    {
      MdTermDAO mdTerm = MdTermDAO.newInstance();
      mdTerm.setValue(MdTermInfo.NAME, "Term");
      mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
      mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
      mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
      mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTerm.apply();

      try
      {
        MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
        mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
        mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
        mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
        mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, childMdBusiness.getOid());
        mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
        mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getOid());
        mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
        mdTermRelationship.apply();

        TestFixtureFactory.delete(mdTermRelationship);

        Assert.fail("Able to create a MdTermRelationship with a non MdTerm child type");
      }
      catch (Exception e)
      {
        Assert.assertTrue(e instanceof RelationshipDefinitionException);
      }
      finally
      {
        TestFixtureFactory.delete(mdTerm);
      }
    }
    finally
    {
      TestFixtureFactory.delete(childMdBusiness);
    }
  }

  @Request
  @Test
  public void testRelationshipAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getOid());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getOid(), result.getParentOid());
        Assert.assertNotNull(child.getOid(), result.getChildOid());
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  @Request
  @Test
  public void testTreeAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getOid());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getOid(), result.getParentOid());
        Assert.assertNotNull(child.getOid(), result.getChildOid());
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  @Request
  @Test
  public void testGraphAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getOid());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getOid(), result.getParentOid());
        Assert.assertNotNull(child.getOid(), result.getChildOid());
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  @Request
  @Test
  public void testFailGraphAssociationValidation()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
        relationship.apply();

        try
        {
          RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType()).apply();

          Assert.fail("Able to create a MdTermRelationship with an association type of Graph where the parent and child exist multiple times");
        }
        catch (Exception e)
        {
          Assert.assertTrue("Expected type of DuplicateGraphPathException but was " + e.getClass().getName(), ( e instanceof DuplicateGraphPathException ));
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

  @Request
  @Test
  public void testFailTreeAssociationValidation()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    mdTerm.setValue(MdTermInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getOid());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getOid());
      mdTermRelationship.setValue(MdTermRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 1");
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.setStructValue(TermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Term 2");
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getOid(), child.getOid(), mdTermRelationship.definesType());
        relationship.apply();

        try
        {
          RelationshipDAO.newInstance(child.getOid(), parent.getOid(), mdTermRelationship.definesType()).apply();

          Assert.fail("Able to create a MdTermRelationship with an association type of Graph where the parent and child exist multiple times");
        }
        catch (RelationshipRecursionException e)
        {
          // This is expected
        }
      }
      finally
      {
        TestFixtureFactory.delete(mdTermRelationship);
      }
    }
    finally
    {
      TestFixtureFactory.delete(mdTerm);
    }
  }

}
