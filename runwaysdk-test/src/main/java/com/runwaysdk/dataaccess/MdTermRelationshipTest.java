/**
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
 */
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.Assert;

import com.runwaysdk.constants.AssociationType;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTermRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdTermRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.RelationshipDefinitionException;

public class MdTermRelationshipTest extends TestCase
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

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdTermRelationshipTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  /**
   * Set the testObject to a new Instance of the TEST type.
   */
  protected static void classSetUp()
  {
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * @see TestCase#tearDown()
   */
  protected static void classTearDown()
  {
  }

  public void testCreateAndGetMdTermRelationship()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
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

  public void testInvalidParentType()
  {
    MdBusinessDAO parentMdBusiness = TestFixtureFactory.createMdBusiness1();
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
      mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      mdTerm.apply();

      try
      {
        MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
        mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
        mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
        mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, parentMdBusiness.getId());
        mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
        mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
        mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
        mdTermRelationship.setGenerateMdController(false);
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

  public void testInvalidChildType()
  {
    MdBusinessDAO childMdBusiness = TestFixtureFactory.createMdBusiness1();
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
      mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      mdTerm.apply();

      try
      {
        MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
        mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
        mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
        mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
        mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, childMdBusiness.getId());
        mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
        mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
        mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
        mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
        mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
        mdTermRelationship.setGenerateMdController(false);
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

  public void testRelationshipAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.RELATIONSHIP.getId());
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getId(), result.getParentId());
        Assert.assertNotNull(child.getId(), result.getChildId());
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

  public void testTreeAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getId());
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getId(), result.getParentId());
        Assert.assertNotNull(child.getId(), result.getChildId());
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

  public void testGraphAssociationInstance()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getId());
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType());
        relationship.apply();

        RelationshipDAOIF result = RelationshipDAO.get(relationship.getId());

        Assert.assertNotNull(result);
        Assert.assertNotNull(parent.getId(), result.getParentId());
        Assert.assertNotNull(child.getId(), result.getChildId());
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

  public void testFailGraphAssociationValidation()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.GRAPH.getId());
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType());
        relationship.apply();

        try
        {
          RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType()).apply();

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

  public void testFailTreeAssociationValidation()
  {
    MdTermDAO mdTerm = MdTermDAO.newInstance();
    mdTerm.setValue(MdTermInfo.NAME, "Term");
    mdTerm.setValue(MdTermInfo.PACKAGE, "com.test");
    mdTerm.setStructValue(MdTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdTerm.setStructValue(MdTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdTerm.setValue(MdTermInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTerm.setValue(MdTermInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTerm.setValue(MdTermInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
    mdTerm.apply();

    try
    {
      MdTermRelationshipDAO mdTermRelationship = MdTermRelationshipDAO.newInstance();
      mdTermRelationship.setValue(MdTreeInfo.NAME, "LocatedIn");
      mdTermRelationship.setValue(MdTreeInfo.PACKAGE, "com.test");
      mdTermRelationship.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Located In");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Parent Term");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdTerm.getId());
      mdTermRelationship.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTermRelationship.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child Term");
      mdTermRelationship.setValue(MdTreeInfo.PARENT_METHOD, "ParentTerm");
      mdTermRelationship.setValue(MdTreeInfo.CHILD_METHOD, "ChildTerm");
      mdTermRelationship.addItem(MdTermRelationshipInfo.ASSOCIATION_TYPE, AssociationType.TREE.getId());
      mdTermRelationship.setGenerateMdController(false);
      mdTermRelationship.apply();

      try
      {
        BusinessDAO parent = BusinessDAO.newInstance(mdTerm.definesType());
        parent.apply();

        BusinessDAO child = BusinessDAO.newInstance(mdTerm.definesType());
        child.apply();

        RelationshipDAO relationship = RelationshipDAO.newInstance(parent.getId(), child.getId(), mdTermRelationship.definesType());
        relationship.apply();

        try
        {
          RelationshipDAO.newInstance(child.getId(), parent.getId(), mdTermRelationship.definesType()).apply();

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
