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
/*
 * Created on Jun 22, 2005
 */
package com.runwaysdk.dataaccess;

import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.RunwayException;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.cache.ObjectCache;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.RelationshipDAOFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;

/**
 * J-Unit tests for class Relationship.
 * 
 * @author Eric G
 * @version $Revision 1.0 $
 * @since
 **/
public class RelationshipTest extends TestCase
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

  private static final TypeInfo TEST_RELATIONSHIP                = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationship");

  private static final TypeInfo TEST_RELATIONSHIP_GRAPH          = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipGraph");

  private static final TypeInfo TEST_RELATIONSHIP_TREE           = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipTree");

  private static final TypeInfo TEST_RELATIONSHIP_TREE_RECURSION = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipRecursion");

  private static final TypeInfo TEST_ABSTRACT_TREE_RELATIONSHIP  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipAbstract");

  private static final TypeInfo TEST_CONCRETE_TREE_RELATIONSHIP1 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipConcrete1");

  private static final TypeInfo TEST_CONCRETE_TREE_RELATIONSHIP2 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestRelationshipConcrete2");

  /**
   * <code>testObject</code> is a BusinessDAO that is mapped to a new instance
   * of the MasterTestSetup.TEST_CLASS class for each test. Values are set and
   * tested on it.
   */
  private BusinessDAO           testObject;

  /**
   * <code>reference</code> is a BusinessDAO that is mapped to a new instance of
   * the MasterTestSetup.REFERENCE_CLASS class for each test. Values are set and
   * tested on it.
   */
  private BusinessDAO           reference;

  /**
   * The launch point for the Junit tests.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(RelationshipTest.suite()));
  }

  /**
   * A suite() takes <b>this </b> <code>RelationshipTest.class</code> and wraps
   * it in <code>MasterTestSetup</code>. The returned class is a suite of all
   * the tests in <code>RelationshipTest</code>, with the global setUp() and
   * tearDown() methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(RelationshipTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        classSetUp();
      }

      protected void tearDown()
      {
      }
    };

    return wrapper;
  }

  public static void classSetUp()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    // Now for a new Relationship
    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, TEST_RELATIONSHIP.getTypeName());
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, TEST_RELATIONSHIP.getPackageName());
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, testType.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "TestParent1");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "TestChild1");
    mdRelationship.apply();

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();
    mdGraph.setValue(MdGraphInfo.NAME, TEST_RELATIONSHIP_GRAPH.getTypeName());
    mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdGraph.setValue(MdGraphInfo.PACKAGE, TEST_RELATIONSHIP_GRAPH.getPackageName());
    mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Graph Relationship");
    mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, testType.getId());
    mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "*");
    mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
    mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "TestParent2");
    mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "TestChild2");
    mdGraph.apply();

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, TEST_RELATIONSHIP_TREE.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, TEST_RELATIONSHIP_TREE.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Tree Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent3");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild3");
    mdTree.apply();

    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, TEST_RELATIONSHIP_TREE_RECURSION.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, TEST_RELATIONSHIP_TREE_RECURSION.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship for recursion");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "RecursionParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "RecursionChild");
    mdTree.apply();

    MdTreeDAO mdTreeTestAbstract = MdTreeDAO.newInstance();
    mdTreeTestAbstract.setValue(MdTreeInfo.NAME, TEST_ABSTRACT_TREE_RELATIONSHIP.getTypeName());
    mdTreeTestAbstract.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTreeTestAbstract.setValue(MdTreeInfo.PACKAGE, TEST_ABSTRACT_TREE_RELATIONSHIP.getPackageName());
    mdTreeTestAbstract.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Abstract Relationship");
    mdTreeTestAbstract.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTreeTestAbstract.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTreeTestAbstract.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdTreeTestAbstract.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTreeTestAbstract.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTreeTestAbstract.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" class");
    mdTreeTestAbstract.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTreeTestAbstract.setValue(MdTreeInfo.CHILD_CARDINALITY, "2");
    mdTreeTestAbstract.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTreeTestAbstract.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent");
    mdTreeTestAbstract.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild");
    mdTreeTestAbstract.apply();

    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, TEST_CONCRETE_TREE_RELATIONSHIP1.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, TEST_CONCRETE_TREE_RELATIONSHIP1.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Concrete Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, mdTreeTestAbstract.getId());
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "2");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "ConcreteParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "ConcreteChild");
    mdTree.apply();

    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, TEST_CONCRETE_TREE_RELATIONSHIP2.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, TEST_CONCRETE_TREE_RELATIONSHIP2.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Concrete Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, mdTreeTestAbstract.getId());
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "2");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "Concrete2Parent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "Concrete2Child");
    mdTree.apply();

  }

  /**
   * Set the testObject to a new Instance of the TEST class.
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    testObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject.apply();
    reference = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * @see TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    super.tearDown();

    if (!testObject.isNew())
    {
      TestFixtureFactory.delete(testObject);
    }

    if (!reference.isNew())
    {
      TestFixtureFactory.delete(reference);
    }
  }

  /**
   * Constructor for AttributeTest.
   * 
   * @param name
   */
  public RelationshipTest(String name)
  {
    super(name);
  }

  /**
   * Tests a relationship with a child of the wrong type.
   */
  public void testRelationshipInvalidChildType()
  {
    RelationshipDAO relDAO = null;

    try
    {
      relDAO = testObject.addChild(testObject, TEST_RELATIONSHIP_TREE.getType());
      relDAO.apply();

      fail(TEST_RELATIONSHIP_TREE.getType() + " accepted an invalid child type.");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a relationship with a parent of the wrong type.
   */
  public void testRelationshipInvalidParentType()
  {
    RelationshipDAO relDAO = null;

    try
    {
      relDAO = reference.addChild(reference, TEST_RELATIONSHIP_TREE.getType());
      relDAO.apply();

      fail(TEST_RELATIONSHIP_TREE.getType() + " accepted an invalid parent type.");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
    finally
    {
      if (relDAO != null && !relDAO.isNew())
      {
        TestFixtureFactory.delete(relDAO);
      }
    }
  }

  /**
   * Tests a relationship by violating the parent cardinality.
   */
  public void testRelationshipParentCardinality()
  {
    RelationshipDAO relationshipDAO = testObject.addChild(reference, TEST_RELATIONSHIP_TREE.getType());
    relationshipDAO.apply();

    BusinessDAO parent = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent.apply();

    try
    {
      RelationshipDAO failure = parent.addChild(reference, TEST_RELATIONSHIP_TREE.getType());
      failure.apply();
      fail("Relationship.validate() accepted that violates parent cardinality.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(parent);
      TestFixtureFactory.delete(relationshipDAO);
    }
  }

  /**
   * Tests a relationship by violating the child cardinality.
   */
  public void testRelationshipChildCardinality()
  {
    RelationshipDAO relationshipDAO = testObject.addChild(reference, TEST_RELATIONSHIP_TREE.getType());
    relationshipDAO.apply();

    BusinessDAO child2 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child2.apply();

    try
    {
      RelationshipDAO failure = testObject.addChild(child2, TEST_RELATIONSHIP_TREE.getType());
      failure.apply();
      fail("Relationship.validate() accepted that violates child cardinality.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(relationshipDAO);
      TestFixtureFactory.delete(child2);
    }
  }

  /**
   * Tests adding a recursive relationship.
   */
  public void testRelationshipRecursion()
  {

    BusinessDAO reference2 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference2.apply();

    RelationshipDAO relationshipDAO = reference.addChild(reference2, TEST_RELATIONSHIP_TREE_RECURSION.getType());
    relationshipDAO.apply();

    try
    {
      RelationshipDAO failure = reference2.addChild(reference, TEST_RELATIONSHIP_TREE_RECURSION.getType());
      failure.apply();

      fail("RelationshipFactory.recursiveLinkCheck() accepted recursive relationship where the child is the parent of it's parent.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(relationshipDAO);
      TestFixtureFactory.delete(reference2);
    }
  }

  /**
   * Tests to make sure that using BusinessDAO.addParent() works correctly.
   */
  public void testRelationshipAddParent()
  {
    RelationshipDAO relationshipDAO = reference.addParent(testObject, TEST_RELATIONSHIP_TREE.getType());
    relationshipDAO.apply();

    // check database directly for ID match on parent/child
    RelationshipDAOIF rel = RelationshipDAOFactory.get(relationshipDAO.getId());
    if (rel == null)
    {
      fail("RelationshipFactory.get() returned null");
    }

    if (rel.getParentId().compareTo(testObject.getId()) != 0 || rel.getChildId().compareTo(reference.getId()) != 0)
    {
      fail("BusinessDAO.addParent() did not correctly save the information to the database.");
    }

    // check if caching is disabled and if it is, the cache should hold nothing
    MdRelationshipDAOIF mdr = MdRelationshipDAO.getMdRelationshipDAO(TEST_RELATIONSHIP_TREE.getType());
    AttributeEnumerationIF attr = (AttributeEnumerationIF) mdr.getAttributeIF(MdRelationshipInfo.CACHE_ALGORITHM);
    BusinessDAOIF[] dataArr = attr.dereference();
    if (Integer.parseInt(dataArr[0].getValue(EntityCacheMaster.CACHE_CODE)) == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
    {
      // make sure cache holds nothing for children objects of the relationship
      List<RelationshipDAOIF> childList = ObjectCache.getChildrenFromCache(testObject.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (childList.size() != ObjectCache.getChildren(testObject.getId(), TEST_RELATIONSHIP_TREE.getType()).size() - 1)
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildrenFromCache()");
      }

      // make sure cache holds nothing for parent objects of the relationship
      List<RelationshipDAOIF> parentList = ObjectCache.getParentsFromCache(reference.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (parentList.size() != ObjectCache.getParents(reference.getId(), TEST_RELATIONSHIP_TREE.getType()).size() - 1)
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildFromCache()");
      }
    }
    else
    {
      // make sure cache holds nothing for children objects of the relationship
      List<RelationshipDAOIF> childList = ObjectCache.getChildrenFromCache(testObject.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (childList.size() == ObjectCache.getChildren(testObject.getId(), TEST_RELATIONSHIP_TREE.getType()).size())
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildrenFromCache()");
      }

      // make sure cache holds nothing for parent objects of the relationship
      List<RelationshipDAOIF> parentList = ObjectCache.getParentsFromCache(reference.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (parentList.size() == ObjectCache.getParents(reference.getId(), TEST_RELATIONSHIP_TREE.getType()).size())
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildFromCache()");
      }
    }

    try
    {
      if (!reference.isChildOf(testObject, TEST_RELATIONSHIP_TREE.getType()))
      {
        fail("BusinessDAO.isChildOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
      else if (!testObject.isParentOf(reference, TEST_RELATIONSHIP_TREE.getType()))
      {
        fail("BusinessDAO.isParentOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Tests to make sure that using BusinessDAO.addChild() works correctly.
   */
  public void testRelationshipAddChild()
  {
    RelationshipDAO relationshipDAO = testObject.addChild(reference, TEST_RELATIONSHIP_TREE.getType());
    relationshipDAO.apply();

    // check database directly for ID match on parent/child
    RelationshipDAOIF rel = RelationshipDAOFactory.get(relationshipDAO.getId());
    if (rel == null)
    {
      fail("RelationshipFactory.get() returned null");
    }

    if (rel.getParentId().compareTo(testObject.getId()) != 0 || rel.getChildId().compareTo(reference.getId()) != 0)
    {
      fail("BusinessDAO.addParent() did not correctly save the information to the database.");
    }

    // check if caching is disabled and if it is, the cache should hold nothing
    MdRelationshipDAOIF mdr = MdRelationshipDAO.getMdRelationshipDAO(TEST_RELATIONSHIP_TREE.getType());
    AttributeEnumerationIF attr = (AttributeEnumerationIF) mdr.getAttributeIF(MdRelationshipInfo.CACHE_ALGORITHM);
    BusinessDAOIF[] dataArr = attr.dereference();
    if (Integer.parseInt(dataArr[0].getValue(EntityCacheMaster.CACHE_CODE)) == EntityCacheMaster.CACHE_NOTHING.getCacheCode())
    {
      // make sure cache holds nothing for children objects of the relationship
      List<RelationshipDAOIF> childList = ObjectCache.getChildrenFromCache(testObject.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (childList.size() != ObjectCache.getChildren(testObject.getId(), TEST_RELATIONSHIP_TREE.getType()).size() - 1)
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildrenFromCache()");
      }

      // make sure cache holds nothing for parent objects of the relationship
      List<RelationshipDAOIF> parentList = ObjectCache.getParentsFromCache(reference.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (parentList.size() != ObjectCache.getParents(reference.getId(), TEST_RELATIONSHIP_TREE.getType()).size() - 1)
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildFromCache()");
      }
    }
    else
    {
      // make sure cache holds nothing for children objects of the relationship
      List<RelationshipDAOIF> childList = ObjectCache.getChildrenFromCache(testObject.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (childList.size() == ObjectCache.getChildren(testObject.getId(), TEST_RELATIONSHIP_TREE.getType()).size())
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildrenFromCache()");
      }

      // make sure cache holds nothing for parent objects of the relationship
      List<RelationshipDAOIF> parentList = ObjectCache.getParentsFromCache(reference.getId(), TEST_RELATIONSHIP_TREE.getType());
      if (parentList.size() == ObjectCache.getParents(reference.getId(), TEST_RELATIONSHIP_TREE.getType()).size())
      {
        fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildFromCache()");
      }
    }

    try
    {
      if (!reference.isChildOf(testObject, TEST_RELATIONSHIP_TREE.getType()))
      {
        fail("BusinessDAO.isChildOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
      else if (!testObject.isParentOf(reference, TEST_RELATIONSHIP_TREE.getType()))
      {
        fail("BusinessDAO.isParentOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
    }
    catch (DataAccessException e)
    {
      fail(e.toString());
    }
  }

  /**
   * Deletes a relationship between a parent and child
   */
  public void testRelationshipDelete()
  {
    // create the test to be deleted
    BusinessDAO uniqueChild1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    uniqueChild1.apply();
    BusinessDAO uniqueParent1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    uniqueParent1.apply();

    RelationshipDAO relDAO = uniqueParent1.addChild(uniqueChild1, TEST_RELATIONSHIP_TREE.getType());
    relDAO.apply();

    // number of children
    int numChildren = ObjectCache.getChildren(uniqueParent1.getId(), TEST_RELATIONSHIP_TREE.getType()).size();

    // number of parents
    int numParents = ObjectCache.getParents(uniqueChild1.getId(), TEST_RELATIONSHIP_TREE.getType()).size();

    TestFixtureFactory.delete(relDAO);

    // this should throw an exception since the relationship has been deleted
    try
    {
      RelationshipDAO.get(relDAO.getId());
      uniqueChild1.delete();
      uniqueParent1.delete();
      fail("Relationship.delete() didn't work as expected. Relationship.get() didn't throw an error!");
    }
    catch (DataAccessException e)
    {
      // exception caught, which is what we want!
    }

    // this should return null since there is no relationship
    try
    {
      if (RelationshipDAOFactory.get(relDAO.getId()) != null)
      {
        // clean up
        uniqueChild1.delete();
        uniqueParent1.delete();
        fail("Relationship.delete() didn't work as expected. RelationshipFactory.get() returns the relationship that should have been deleted.");
      }
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }

    // the cache should hold nothing for children since the relationship was
    // deleted
    List<RelationshipDAOIF> childList = ObjectCache.getChildrenFromCache(uniqueParent1.getId(), TEST_RELATIONSHIP_TREE.getType());
    if (childList.size() != numChildren - 1)
    {
      // clean up
      uniqueChild1.delete();
      uniqueParent1.delete();
      fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildrenFromCache()");
    }

    // the cache should hold nothing for parents since the relationship was
    // deleted
    List<RelationshipDAOIF> parentList = ObjectCache.getParentsFromCache(uniqueChild1.getId(), TEST_RELATIONSHIP_TREE.getType());
    if (parentList.size() != numParents - 1)
    {

      // clean up
      uniqueChild1.delete();
      uniqueParent1.delete();
      fail("BusinessDAO.addParent() cached a relationship when it shouldn't have..." + "from RelationshipCache.getChildFromCache()");
    }

    // the below tests should fail
    try
    {
      if (uniqueChild1.isChildOf(uniqueParent1, TEST_RELATIONSHIP_TREE.getType()))
      {

        // clean up
        uniqueChild1.delete();
        uniqueParent1.delete();
        fail("BusinessDAO.isChildOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
      else if (uniqueParent1.isParentOf(uniqueChild1, TEST_RELATIONSHIP_TREE.getType()))
      {
        // clean up
        uniqueChild1.delete();
        uniqueParent1.delete();
        fail("BusinessDAO.isParentOf() did not correctly identify the parent<->child relationship when adding a parent.");
      }
    }
    catch (DataAccessException e)
    {
      // clean up
      uniqueChild1.delete();
      uniqueParent1.delete();
      fail(e.toString());
    }

    // clean up
    uniqueChild1.delete();
    uniqueParent1.delete();
  }

  /**
   * Test to show that given a creation of instances of
   * TEST_RELATIONSHIP.getType(), that the MdBusiness for storing attributes has
   * the same number of instance.
   */
  public void testMdBusinessCorrectNumberOfInstances()
  {
    // create 3 parent objects of type TEST
    BusinessDAO parent1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent1.apply();
    BusinessDAO parent2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent2.apply();
    BusinessDAO parent3 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent3.apply();

    // create 3 child objects of type MasterTestSetup.REFERENCE_CLASS.Type()
    BusinessDAO child1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child1.apply();
    BusinessDAO child2 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child2.apply();
    BusinessDAO child3 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child3.apply();

    // now create 3 new relationships of type TEST_RELATIONSHIP.getType()
    RelationshipDAO rel1 = parent1.addChild(child1.getId(), TEST_RELATIONSHIP_TREE.getType());
    rel1.apply();

    RelationshipDAO rel2 = parent2.addChild(child2.getId(), TEST_RELATIONSHIP_TREE.getType());
    rel2.apply();

    RelationshipDAO rel3 = parent3.addChild(child3.getId(), TEST_RELATIONSHIP_TREE.getType());
    rel3.apply();

    // clean up
    parent1.delete();
    parent2.delete();
    parent3.delete();
    child1.delete();
    child2.delete();
    child3.delete();
  }

  /**
   * Add an attribute (MdAttribute) to this MdRelationship. Verify that the
   * attribute was added to the MdBusiness. Ensure that a relationship of type
   * ENTITY_ATTRIBUTE was created where the parent is the MdBusiness and the
   * child is the MdAttribute.
   */
  public void testAddAttributeToMdRelationship()
  {
    MdRelationshipDAOIF test_relationship = MdRelationshipDAO.getMdRelationshipDAO(TEST_RELATIONSHIP_TREE.getType());

    int numOfDefinedAttributes = test_relationship.definesAttributes().size();

    // create a new attribute
    MdAttributeCharacterDAO attr = MdAttributeCharacterDAO.newInstance();
    attr.setValue(MdAttributeCharacterInfo.NAME, "teaches");
    attr.setValue(MdAttributeCharacterInfo.SIZE, "128");
    attr.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, test_relationship.getId());
    attr.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    attr.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    attr.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Teaches a");
    attr.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    attr.apply();

    // verify the attribute was added to the MdBusiness
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(TEST_RELATIONSHIP_TREE.getType());
    List<? extends MdAttributeConcreteDAOIF> attributes = mdRelationshipIF.definesAttributes();

    // the length must be one + whatever were copied from component (since we
    // only added one!)
    if (attributes.size() != numOfDefinedAttributes + 1)
    {
      fail("The MdRelationship did not register the correct number of attributes");
    }

    // now compare for validity
    boolean foundMatch = false;
    for (MdAttributeConcreteDAOIF mdAttribute : attributes)
    {
      if (attr.getId().compareTo(mdAttribute.getId()) == 0)
      {
        foundMatch = true;
      }
    }
    if (!foundMatch)
    {
      fail("The actual and potential attribute ids didn't match.");
    }

    // verify that a relationship of type CLASS_ATTRIBUTE was created
    // between the new MdBusiness and MdAttribute

    List<RelationshipDAOIF> relations = mdRelationshipIF.getChildren(RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());

    // length must be one
    if (relations.size() != numOfDefinedAttributes + 1)
    {
      fail(TEST_RELATIONSHIP_TREE.getType() + " failed to have any children of relationship type " + RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());
    }

    // compare ids
    foundMatch = false;
    for (RelationshipDAOIF relation : relations)
    {
      BusinessDAOIF potentialAttrIF = relation.getChild();
      if (potentialAttrIF.getId().compareTo(attr.getId()) == 0)
      {
        foundMatch = true;
      }
    }
    if (!foundMatch)
    {
      fail("A relationship of type " + RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType() + " failed to be created between " + TEST_RELATIONSHIP_TREE.getType() + " and " + TEST_RELATIONSHIP_TREE.getType() + ".TEACHES");
    }

    // Now create an instance of TEST_RELATIONSHIP.getType() and makes sure
    // it has the attribute created above.

    BusinessDAO professor = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    professor.apply();
    BusinessDAO student = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    student.apply();

    RelationshipDAO relDAO = professor.addChild(student, TEST_RELATIONSHIP_TREE.getType());
    relDAO.apply();
    MdAttributeConcreteDAOIF attrIF = relDAO.getMdAttributeDAO("teaches");

    if (attrIF.getId().compareTo(attr.getId()) != 0)
    {

      // clean up
      professor.delete();
      student.delete();
      attr.delete();
      fail("Could not retrieve the attribute " + TEST_RELATIONSHIP_TREE.getType() + ".TEACHES for relationship " + TEST_RELATIONSHIP_TREE.getType());
    }

    // clean up
    professor.delete();
    student.delete();
    attr.delete();
  }

  /**
   * 1) Verify the MdRelationship object is deleted 2) Verify that all instances
   * of the relationship have been deleted.
   */
  public void testDeleteRelationshipCascading()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    // create a couple of test objects to partake in the relationship
    BusinessDAO parent = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent.apply();
    BusinessDAO child = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child.apply();

    TypeInfo teachesRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Teaches");

    // create a new relationship
    MdTreeDAO teaches = MdTreeDAO.newInstance();
    teaches.setValue(MdTreeInfo.NAME, teachesRelationship.getTypeName());
    teaches.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    teaches.setValue(MdTreeInfo.PACKAGE, teachesRelationship.getPackageName());
    teaches.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A teaching Relationship 1");
    teaches.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    teaches.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    teaches.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    teaches.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    teaches.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    teaches.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "\"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" class");
    teaches.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    teaches.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    teaches.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    teaches.setValue(MdTreeInfo.PARENT_METHOD, "Teacher");
    teaches.setValue(MdTreeInfo.CHILD_METHOD, "Student");
    teaches.apply();

    // create an attribute for the relationship TEACHES
    MdAttributeCharacterDAO attr = MdAttributeCharacterDAO.newInstance();
    attr.setValue(MdAttributeCharacterInfo.NAME, "flunk");
    attr.setValue(MdAttributeCharacterInfo.SIZE, "128");
    attr.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, teaches.getId());
    attr.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    attr.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "F-");
    attr.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Flunkee");
    attr.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    attr.apply();

    // create a new instance of the teachesRelationship.getType() relationship
    RelationshipDAO relDAO = parent.addChild(child, teachesRelationship.getType());
    relDAO.apply();

    // Now for the fun stuff. Delete the relationship
    // teachesRelationship.getType() and make sure that everything
    // associated with it is also deleted with regard to the meta data.
    TestFixtureFactory.delete(teaches);

    // Now make sure that all instances of the relationship have been deleted.
    if (Database.tableExists(teaches.getTableName()))
    {
      fail("Not all instances of " + teachesRelationship.getType() + " have been correctly deleted.");
    }

    // clean up
    TestFixtureFactory.delete(parent);
    TestFixtureFactory.delete(child);
  }

  /**
   * Tests if a composition relationship holds true. This means if a parent in a
   * relationship is deleted, then all of the children must be deleted as well.
   */
  public void testRelationshipComposition()
  {
    // Create the new data types Teacher, Student, and Pencil
    MdBusinessDAO teacherDO = null;
    MdBusinessDAO studentDO = null;
    MdBusinessDAO pencilDO = null;
    MdBusinessDAO graphiteDO = null;
    MdTreeDAO teachesREL = null;
    MdTreeDAO writesREL = null;
    MdTreeDAO mediumREL = null;

    // Create the Teacher data type
    try
    {
      TypeInfo teacherClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Teacher");

      teacherDO = MdBusinessDAO.newInstance();
      teacherDO.setValue(MdBusinessInfo.NAME, teacherClass.getTypeName());
      teacherDO.setValue(MdBusinessInfo.PACKAGE, teacherClass.getPackageName());
      teacherDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      teacherDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, teacherClass.getTypeName() + " test type");
      teacherDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      teacherDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      teacherDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      teacherDO.apply();

      TypeInfo studentClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Student");

      // Create the Student data type
      studentDO = MdBusinessDAO.newInstance();
      studentDO.setValue(MdBusinessInfo.NAME, studentClass.getTypeName());
      studentDO.setValue(MdBusinessInfo.PACKAGE, studentClass.getPackageName());
      studentDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      studentDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, studentClass.getTypeName() + " Test Type");
      studentDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      studentDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      studentDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      studentDO.apply();

      TypeInfo pencilClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Pencil");

      // Create the Pencil data type
      pencilDO = MdBusinessDAO.newInstance();
      pencilDO.setValue(MdBusinessInfo.NAME, pencilClass.getTypeName());
      pencilDO.setValue(MdBusinessInfo.PACKAGE, pencilClass.getPackageName());
      pencilDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      pencilDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, pencilClass.getTypeName() + " Test Type");
      pencilDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      pencilDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      pencilDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      pencilDO.apply();

      TypeInfo graphiteClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Graphite");

      // Create the Graphite data type
      graphiteDO = MdBusinessDAO.newInstance();
      graphiteDO.setValue(MdBusinessInfo.NAME, graphiteClass.getTypeName());
      graphiteDO.setValue(MdBusinessInfo.PACKAGE, graphiteClass.getPackageName());
      graphiteDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      graphiteDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "graphite Test Type");
      graphiteDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      graphiteDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      graphiteDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      graphiteDO.apply();

      // Create the relationships necessary for composition
      TypeInfo teachesARelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TeachesA");

      // Create the "teacher teaches a student" relationship
      teachesREL = MdTreeDAO.newInstance();
      teachesREL.setValue(MdTreeInfo.NAME, teachesARelationship.getTypeName());
      teachesREL.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
      teachesREL.setValue(MdTreeInfo.PACKAGE, teachesARelationship.getPackageName());
      teachesREL.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A teaching Relationship");
      teachesREL.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      teachesREL.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      teachesREL.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      teachesREL.setValue(MdTreeInfo.PARENT_MD_BUSINESS, teacherDO.getId());
      teachesREL.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      teachesREL.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, teacherClass.getTypeName() + " teaches a student");
      teachesREL.setValue(MdTreeInfo.CHILD_MD_BUSINESS, studentDO.getId());
      teachesREL.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      teachesREL.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, studentClass.getTypeName() + " is taught by a teacher");
      teachesREL.setValue(MdTreeInfo.PARENT_METHOD, "Teacher");
      teachesREL.setValue(MdTreeInfo.CHILD_METHOD, "Student");
      teachesREL.apply();

      TypeInfo writesWithRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "WritesWith");

      // Create the "student writes with a pencil" relationship
      writesREL = MdTreeDAO.newInstance();
      writesREL.setValue(MdTreeInfo.NAME, writesWithRelationship.getTypeName());
      writesREL.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
      writesREL.setValue(MdTreeInfo.PACKAGE, writesWithRelationship.getPackageName());
      writesREL.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A writing relationship");
      writesREL.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      writesREL.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      writesREL.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      writesREL.setValue(MdTreeInfo.PARENT_MD_BUSINESS, studentDO.getId());
      writesREL.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      writesREL.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A student writes with a pencil");
      writesREL.setValue(MdTreeInfo.CHILD_MD_BUSINESS, pencilDO.getId());
      writesREL.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      writesREL.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A pencil is used by a student to write with");
      writesREL.setValue(MdTreeInfo.PARENT_METHOD, "Writer");
      writesREL.setValue(MdTreeInfo.CHILD_METHOD, "Pencil1");
      writesREL.apply();

      TypeInfo mediumRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Medium");

      // Create the "student writes with a pencil" relationship
      mediumREL = MdTreeDAO.newInstance();
      mediumREL.setValue(MdTreeInfo.NAME, mediumRelationship.getTypeName());
      mediumREL.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mediumREL.setValue(MdTreeInfo.PACKAGE, mediumRelationship.getPackageName());
      mediumREL.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A medium relationship");
      mediumREL.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mediumREL.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mediumREL.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mediumREL.setValue(MdTreeInfo.PARENT_MD_BUSINESS, pencilDO.getId());
      mediumREL.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      mediumREL.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A pencil's medium is graphite");
      mediumREL.setValue(MdTreeInfo.CHILD_MD_BUSINESS, graphiteDO.getId());
      mediumREL.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      mediumREL.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Graphite is a pencil's medium");
      mediumREL.setValue(MdTreeInfo.PARENT_METHOD, "Pencil2");
      mediumREL.setValue(MdTreeInfo.CHILD_METHOD, "Graphite");
      mediumREL.apply();

      // Create instances of the data types
      BusinessDAO teacher = BusinessDAO.newInstance(teacherClass.getType());
      teacher.apply();

      BusinessDAO student1 = BusinessDAO.newInstance(studentClass.getType());
      student1.apply();

      BusinessDAO student2 = BusinessDAO.newInstance(studentClass.getType());
      student2.apply();

      BusinessDAO student3 = BusinessDAO.newInstance(studentClass.getType());
      student3.apply();

      BusinessDAO pencil1 = BusinessDAO.newInstance(pencilClass.getType());
      pencil1.apply();

      BusinessDAO pencil2 = BusinessDAO.newInstance(pencilClass.getType());
      pencil2.apply();

      BusinessDAO pencil3 = BusinessDAO.newInstance(pencilClass.getType());
      pencil3.apply();

      BusinessDAO graphite1 = BusinessDAO.newInstance(graphiteClass.getType());
      graphite1.apply();

      BusinessDAO graphite2 = BusinessDAO.newInstance(graphiteClass.getType());
      graphite2.apply();

      BusinessDAO graphite3 = BusinessDAO.newInstance(graphiteClass.getType());
      graphite3.apply();

      // Create instances of the relationships
      RelationshipDAO relDAO;

      relDAO = teacher.addChild(student1, teachesARelationship.getType());
      relDAO.apply();

      relDAO = teacher.addChild(student2, teachesARelationship.getType());
      relDAO.apply();

      relDAO = teacher.addChild(student3, teachesARelationship.getType());
      relDAO.apply();

      relDAO = student1.addChild(pencil1, writesWithRelationship.getType());
      relDAO.apply();

      relDAO = student2.addChild(pencil2, writesWithRelationship.getType());
      relDAO.apply();

      relDAO = student3.addChild(pencil3, writesWithRelationship.getType());
      relDAO.apply();

      relDAO = pencil1.addChild(graphite1, mediumRelationship.getType());
      relDAO.apply();

      relDAO = pencil2.addChild(graphite2, mediumRelationship.getType());
      relDAO.apply();

      relDAO = pencil3.addChild(graphite3, mediumRelationship.getType());
      relDAO.apply();

      // Delete the parent and check for composition reinforcement
      teacher.delete();

      // student check
      List<String> studentIdList = EntityDAO.getEntityIdsDB(studentClass.getType());
      if (studentIdList.size() != 0)
      {
        fail("Deleting the parent (teacher) in a composition relationship did not delete the children.");
      }

      // pencil check
      List<String> pencilIdList = EntityDAO.getEntityIdsDB(pencilClass.getType());
      if (pencilIdList.size() != 0)
      {
        fail("Deleting the parent (student) in a composition relationship did not delete the children.");
      }

      // graphite check (should be 3 since relationship is not composition)
      List<String> graphiteIdList = EntityDAO.getEntityIdsDB(graphiteClass.getType());
      if (graphiteIdList.size() != 3)
      {
        fail("The graphite objects were deleted in a composition relationship that evaluated to false.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (teacherDO != null && !teacherDO.isNew())
      {
        TestFixtureFactory.delete(teacherDO);
      }

      if (studentDO != null && !studentDO.isNew())
      {
        TestFixtureFactory.delete(studentDO);
      }

      if (pencilDO != null && !pencilDO.isNew())
      {
        TestFixtureFactory.delete(pencilDO);
      }

      if (graphiteDO != null && !graphiteDO.isNew())
      {
        TestFixtureFactory.delete(graphiteDO);
      }
    }
  }

  /**
   * A test to make sure that a instance of a struct cannot partake in a
   * relationship.
   */
  public void testBasicInRelationship()
  {
    MdBusinessDAO someDO = null;
    MdBusinessDAO some2DO = null;
    MdStructDAO structDO = null;
    MdTreeDAO badREL = null;

    TypeInfo someClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Something");
    someDO = MdBusinessDAO.newInstance();
    someDO.setValue(MdBusinessInfo.NAME, someClass.getTypeName());
    someDO.setValue(MdBusinessInfo.PACKAGE, someClass.getPackageName());
    someDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    someDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, someClass.getTypeName() + " test type");
    someDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    someDO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    someDO.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    someDO.apply();

    TypeInfo some2Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Something2");
    some2DO = MdBusinessDAO.newInstance();
    some2DO.setValue(MdBusinessInfo.NAME, some2Class.getTypeName());
    some2DO.setValue(MdBusinessInfo.PACKAGE, some2Class.getPackageName());
    some2DO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    some2DO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, some2Class.getTypeName() + " test type");
    some2DO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    some2DO.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    some2DO.setValue(MdElementInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    some2DO.apply();

    TypeInfo structEntityInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "StructEntity");
    structDO = MdStructDAO.newInstance();
    structDO.setValue(MdBusinessInfo.NAME, structEntityInfo.getTypeName());
    structDO.setValue(MdBusinessInfo.PACKAGE, structEntityInfo.getPackageName());
    structDO.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    structDO.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, structEntityInfo.getTypeName() + " Test Type");
    structDO.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
    structDO.apply();

    TypeInfo badRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "Bad");
    badREL = MdTreeDAO.newInstance();
    badREL.setValue(MdTreeInfo.NAME, badRelationship.getTypeName());
    badREL.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
    badREL.setValue(MdTreeInfo.PACKAGE, badRelationship.getPackageName());
    badREL.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A bad Relationship");
    badREL.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    badREL.setValue(MdTreeInfo.PARENT_MD_BUSINESS, someDO.getId());
    badREL.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    badREL.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 1");
    badREL.setValue(MdTreeInfo.PARENT_METHOD, "parentMethod");
    badREL.setValue(MdTreeInfo.CHILD_MD_BUSINESS, some2DO.getId());
    badREL.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    badREL.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 2");
    badREL.setValue(MdTreeInfo.CHILD_METHOD, "childMethod");

    badREL.apply();

    // Try to use a struct in a relationship and test for runtime errors.
    BusinessDAO some = BusinessDAO.newInstance(someClass.getType());
    some.apply();

    BusinessDAO some2 = BusinessDAO.newInstance(some2Class.getType());
    some2.apply();

    EntityDAO structDAO = StructDAO.newInstance(structEntityInfo.getType());
    structDAO.apply();

    try
    {
      RelationshipDAO relDAO = some.addChild(structDAO.getId(), badRelationship.getType());
      relDAO.apply();

      fail("A StructDAO was able to partake in a relationship.");
    }
    catch (RunwayException e)
    {
      // we want to land here.
    }
    finally
    {
      if (someDO != null && !someDO.isNew())
      {
        TestFixtureFactory.delete(someDO);
      }

      if (some2DO != null && !some2DO.isNew())
      {
        TestFixtureFactory.delete(some2DO);
      }

      if (structDO != null && !structDO.isNew())
      {
        TestFixtureFactory.delete(structDO);
      }
    }
  }

  /**
   * When an MdBusiness is deleted, not only should its relationships be
   * removed, but all the relationships of the children as well (since they will
   * be deleted too).
   */
  public void testDeleteMdRelationships()
  {
    MdBusinessDAO A = null;
    MdBusinessDAO B = null;
    MdBusinessDAO C = null;
    MdBusinessDAO D = null;

    MdBusinessDAO W = null;
    MdBusinessDAO X = null;
    MdBusinessDAO Y = null;
    MdBusinessDAO Z = null;

    MdTreeDAO AtoW = null;
    MdTreeDAO BtoX = null;
    MdTreeDAO CtoY = null;
    MdTreeDAO DtoZ = null;

    try
    {

      // Create an two inheritances with the same structure.
      TypeInfo A_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AA");
      A = MdBusinessDAO.newInstance();
      A.setValue(MdBusinessInfo.NAME, A_Class.getTypeName());
      A.setValue(MdBusinessInfo.PACKAGE, A_Class.getPackageName());
      A.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      A.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, A_Class.getTypeName() + " test type");
      A.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      A.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      A.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      A.apply();

      TypeInfo B_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "BB");
      B = MdBusinessDAO.newInstance();
      B.setValue(MdBusinessInfo.NAME, B_Class.getTypeName());
      B.setValue(MdBusinessInfo.PACKAGE, B_Class.getPackageName());
      B.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      B.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, B_Class.getTypeName() + " test type");
      B.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      B.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      B.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      B.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, A.getId());
      B.apply();

      TypeInfo C_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "CC");
      C = MdBusinessDAO.newInstance();
      C.setValue(MdBusinessInfo.NAME, C_Class.getTypeName());
      C.setValue(MdBusinessInfo.PACKAGE, C_Class.getPackageName());
      C.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      C.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, C_Class.getTypeName() + " test type");
      C.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      C.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      C.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      C.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, A.getId());
      C.apply();

      TypeInfo D_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "DD");
      D = MdBusinessDAO.newInstance();
      D.setValue(MdBusinessInfo.NAME, D_Class.getTypeName());
      D.setValue(MdBusinessInfo.PACKAGE, D_Class.getPackageName());
      D.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      D.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, D_Class.getTypeName() + " test type");
      D.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      D.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      D.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      D.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, B.getId());
      D.apply();

      TypeInfo W_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "WW");
      W = MdBusinessDAO.newInstance();
      W.setValue(MdBusinessInfo.NAME, W_Class.getTypeName());
      W.setValue(MdBusinessInfo.PACKAGE, W_Class.getPackageName());
      W.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      W.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, W_Class.getTypeName() + " test type");
      W.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      W.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      W.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      W.apply();

      TypeInfo X_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "XX");
      X = MdBusinessDAO.newInstance();
      X.setValue(MdBusinessInfo.NAME, X_Class.getTypeName());
      X.setValue(MdBusinessInfo.PACKAGE, X_Class.getPackageName());
      X.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      X.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, X_Class.getTypeName() + " test type");
      X.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      X.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      X.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      X.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, W.getId());
      X.apply();

      TypeInfo Y_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "YY");
      Y = MdBusinessDAO.newInstance();
      Y.setValue(MdBusinessInfo.NAME, Y_Class.getTypeName());
      Y.setValue(MdBusinessInfo.PACKAGE, Y_Class.getPackageName());
      Y.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      Y.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, Y_Class.getTypeName() + " test type");
      Y.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      Y.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      Y.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      Y.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, W.getId());
      Y.apply();

      TypeInfo Z_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ZZ");
      Z = MdBusinessDAO.newInstance();
      Z.setValue(MdBusinessInfo.NAME, Z_Class.getTypeName());
      Z.setValue(MdBusinessInfo.PACKAGE, Z_Class.getPackageName());
      Z.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      Z.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, Z_Class.getTypeName() + " test type");
      Z.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      Z.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      Z.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      Z.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, X.getId());
      Z.apply();

      // Relate the two corresponding inheritances.

      TypeInfo AtoW_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AtoW");
      AtoW = MdTreeDAO.newInstance();
      AtoW.setValue(MdTreeInfo.NAME, AtoW_REL.getTypeName());
      AtoW.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.PACKAGE, AtoW_REL.getPackageName());
      AtoW.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A to W");
      AtoW.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      AtoW.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.PARENT_MD_BUSINESS, A.getId());
      AtoW.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent A");
      AtoW.setValue(MdTreeInfo.CHILD_MD_BUSINESS, W.getId());
      AtoW.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child W");
      AtoW.setValue(MdTreeInfo.PARENT_METHOD, "A");
      AtoW.setValue(MdTreeInfo.CHILD_METHOD, "W");
      AtoW.apply();

      TypeInfo BtoX_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "BtoX");
      BtoX = MdTreeDAO.newInstance();
      BtoX.setValue(MdTreeInfo.NAME, BtoX_REL.getTypeName());
      BtoX.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      BtoX.setValue(MdTreeInfo.PACKAGE, BtoX_REL.getPackageName());
      BtoX.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "B to X");
      BtoX.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      BtoX.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      BtoX.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      BtoX.setValue(MdTreeInfo.PARENT_MD_BUSINESS, B.getId());
      BtoX.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      BtoX.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent B");
      BtoX.setValue(MdTreeInfo.CHILD_MD_BUSINESS, X.getId());
      BtoX.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      BtoX.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child X");
      BtoX.setValue(MdTreeInfo.PARENT_METHOD, "B");
      BtoX.setValue(MdTreeInfo.CHILD_METHOD, "X");
      BtoX.apply();

      TypeInfo CtoY_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "CtoY");
      CtoY = MdTreeDAO.newInstance();
      CtoY.setValue(MdTreeInfo.NAME, CtoY_REL.getTypeName());
      CtoY.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      CtoY.setValue(MdTreeInfo.PACKAGE, CtoY_REL.getPackageName());
      CtoY.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "C to Y");
      CtoY.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      CtoY.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      CtoY.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      CtoY.setValue(MdTreeInfo.PARENT_MD_BUSINESS, C.getId());
      CtoY.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      CtoY.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent C");
      CtoY.setValue(MdTreeInfo.CHILD_MD_BUSINESS, Y.getId());
      CtoY.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      CtoY.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child Y");
      CtoY.setValue(MdTreeInfo.PARENT_METHOD, "C");
      CtoY.setValue(MdTreeInfo.CHILD_METHOD, "Y");
      CtoY.apply();

      TypeInfo DtoZ_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "DtoZ");
      DtoZ = MdTreeDAO.newInstance();
      DtoZ.setValue(MdTreeInfo.NAME, DtoZ_REL.getTypeName());
      DtoZ.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      DtoZ.setValue(MdTreeInfo.PACKAGE, DtoZ_REL.getPackageName());
      DtoZ.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "D to Z");
      DtoZ.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      DtoZ.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      DtoZ.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      DtoZ.setValue(MdTreeInfo.PARENT_MD_BUSINESS, D.getId());
      DtoZ.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      DtoZ.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent D");
      DtoZ.setValue(MdTreeInfo.CHILD_MD_BUSINESS, Z.getId());
      DtoZ.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      DtoZ.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child Z");
      DtoZ.setValue(MdTreeInfo.PARENT_METHOD, "D");
      DtoZ.setValue(MdTreeInfo.CHILD_METHOD, "Z");
      DtoZ.apply();

      // now delete the superclass, A, and make sure all relationships are gone.
      TestFixtureFactory.delete(A);

      try
      {
        // these should throw an error because the relationships don't exist
        // anymore
        W.getParents(AtoW_REL.getType());
        X.getParents(BtoX_REL.getType());
        Y.getParents(CtoY_REL.getType());
        Z.getParents(DtoZ_REL.getType());

        fail("A supertype was deleted, but the children still had relationships.");
      }
      catch (DataNotFoundException e)
      {
        // we want to land here
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (W != null && !W.isNew())
      {
        TestFixtureFactory.delete(W);
      }
    }
  }

  /**
   * Test to ensure that a relationship doesn't utilize an MRU cache algorithm.
   */
  public void testRelationshipMRU()
  {
    MdBusinessDAO A = null;
    MdBusinessDAO W = null;
    MdTreeDAO AtoW = null;

    try
    {
      TypeInfo A_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AA");
      A = MdBusinessDAO.newInstance();
      A.setValue(MdBusinessInfo.NAME, A_Class.getTypeName());
      A.setValue(MdBusinessInfo.PACKAGE, A_Class.getPackageName());
      A.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      A.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, A_Class.getTypeName() + " test type");
      A.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      A.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      A.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      A.apply();

      TypeInfo W_Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "WW");
      W = MdBusinessDAO.newInstance();
      W.setValue(MdBusinessInfo.NAME, W_Class.getTypeName());
      W.setValue(MdBusinessInfo.PACKAGE, W_Class.getPackageName());
      W.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      W.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, W_Class.getTypeName() + " test type");
      W.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      W.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      W.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      W.apply();

      TypeInfo AtoW_REL = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AtoW");
      AtoW = MdTreeDAO.newInstance();
      AtoW.setValue(MdTreeInfo.NAME, AtoW_REL.getTypeName());
      AtoW.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_MOST_RECENTLY_USED.getId());
      AtoW.setValue(MdTreeInfo.PACKAGE, AtoW_REL.getPackageName());
      AtoW.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A to W");
      AtoW.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      AtoW.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      AtoW.setValue(MdTreeInfo.PARENT_MD_BUSINESS, A.getId());
      AtoW.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent A");
      AtoW.setValue(MdTreeInfo.CHILD_MD_BUSINESS, W.getId());
      AtoW.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      AtoW.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child W");
      AtoW.apply();

      fail("A relationship was incorrectly able to have an MRU cache algorithm.");
    }
    catch (AttributeValueException e)
    {
      // we want to land here
    }
    finally
    {
      if (A != null && !A.isNew())
      {
        TestFixtureFactory.delete(A);
      }
      if (W != null && !W.isNew())
      {
        TestFixtureFactory.delete(W);
      }
    }
  }

  /**
   * Test to ensure that a relationship doesn't utilize an MRU cache algorithm.
   */
  public void testRelationshipInRelationshipMappingCache()
  {
    TypeInfo TEST_CACHED_RELATIONSHIP = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestCachedRelationship");

    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    // Now for a new Relationship
    MdTreeDAO mdTree = null;

    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, TEST_CACHED_RELATIONSHIP.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, TEST_CACHED_RELATIONSHIP.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Cached Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "CachedParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "CachedChild");
    mdTree.apply();

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "someCharacter");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Character");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdTree.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttrChar.apply();

    BusinessDAO testClassParent = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testClassParent.apply();

    BusinessDAO referenceClassParent = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    referenceClassParent.apply();

    try
    {

      RelationshipDAO relationshipDAO = testClassParent.addChild(referenceClassParent, TEST_CACHED_RELATIONSHIP.getType());
      relationshipDAO.setValue("someCharacter", "value 1");
      relationshipDAO.apply();

      // Fetch the object (from the cache) and change the attribute value()
      relationshipDAO = RelationshipDAO.get(relationshipDAO.getId()).getRelationshipDAO();
      // perform a deep clone. This is how it would work in real life, as people
      // should not be allowed to modify the object that is cached.
      relationshipDAO = relationshipDAO.getRelationshipDAO();
      relationshipDAO.setValue("someCharacter", "value 2");
      relationshipDAO.getRelationshipDAO().apply();

      // Fetch the object from the cache and verify that the value was set
      // correctly
      relationshipDAO = RelationshipDAO.get(relationshipDAO.getId()).getRelationshipDAO();

      if (!relationshipDAO.getValue("someCharacter").equals("value 2"))
      {
        fail("An attribute was modified on an instance of a relationship whose type is cached.  The ObjectCache did not contain the right value.");
      }

      List<RelationshipDAOIF> relationshipArray = testClassParent.getChildren(TEST_CACHED_RELATIONSHIP.getType());

      if (relationshipArray.size() == 0)
      {
        fail("BusinessDAO.getChildren() has a length of zero when it should have a length of one.");
      }

      RelationshipDAOIF relationshipIF = relationshipArray.get(0);

      if (!relationshipIF.getValue("someCharacter").equals("value 2"))
      {
        fail("An attribute was modified on an instance of a relationship whose type is cached.  The RelationshipMappingCache did not contain the right value.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (mdTree != null && !mdTree.isNew())
      {
        TestFixtureFactory.delete(mdTree);
      }

      if (testClassParent != null && !testClassParent.isNew())
      {
        TestFixtureFactory.delete(testClassParent);
      }

      if (referenceClassParent != null && !referenceClassParent.isNew())
      {
        TestFixtureFactory.delete(referenceClassParent);
      }
    }
  }

  /**
   * Test to ensure that you cannot violate a cardinality defined on a parent
   * type.
   */
  public void testRelationshipCardinalityConflictWithSuperAbstractRelationship1()
  {
    BusinessDAO testBusinessDAO = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testBusinessDAO.apply();

    BusinessDAO rDO1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    rDO1.apply();

    BusinessDAO rDO2 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    rDO2.apply();

    BusinessDAO rDO3 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    rDO3.apply();

    BusinessDAO rDO4 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    rDO4.apply();

    RelationshipDAO relationishipDAO = testBusinessDAO.addChild(rDO1, TEST_CONCRETE_TREE_RELATIONSHIP1.getType());
    relationishipDAO.apply();

    relationishipDAO = testBusinessDAO.addChild(rDO2, TEST_CONCRETE_TREE_RELATIONSHIP2.getType());
    relationishipDAO.apply();

    try
    {
      relationishipDAO = testBusinessDAO.addChild(rDO3, TEST_CONCRETE_TREE_RELATIONSHIP1.getType());
      relationishipDAO.apply();
      fail("A parent relationship type cardinality was violated for the parent object type.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      if (testBusinessDAO != null && testBusinessDAO.isAppliedToDB())
      {
        TestFixtureFactory.delete(testBusinessDAO);
      }
      if (rDO1 != null && rDO1.isAppliedToDB())
      {
        TestFixtureFactory.delete(rDO1);
      }
      if (rDO2 != null && rDO2.isAppliedToDB())
      {
        TestFixtureFactory.delete(rDO2);
      }
      if (rDO3 != null && rDO3.isAppliedToDB())
      {
        TestFixtureFactory.delete(rDO3);
      }
      if (rDO4 != null && rDO4.isAppliedToDB())
      {
        TestFixtureFactory.delete(rDO4);
      }
    }
  }

  /**
   * Test to ensure that you cannot violate a cardinality defined on a parent
   * type.
   */
  public void testRelationshipCardinalityConflictWithSuperAbstractRelationship2()
  {
    BusinessDAO reference = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();

    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();

    BusinessDAO testObject2 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject2.apply();

    RelationshipDAO relationishipDAO = reference.addParent(testObject1, TEST_CONCRETE_TREE_RELATIONSHIP1.getType());
    relationishipDAO.apply();

    try
    {
      relationishipDAO = reference.addParent(testObject1, TEST_CONCRETE_TREE_RELATIONSHIP2.getType());
      relationishipDAO.apply();
      fail("A parent relationship type cardinality was violated for the child object type.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      if (reference != null && reference.isAppliedToDB())
      {
        TestFixtureFactory.delete(reference);
      }
      if (testObject1 != null && testObject1.isAppliedToDB())
      {
        TestFixtureFactory.delete(testObject1);
      }
      if (testObject2 != null && testObject2.isAppliedToDB())
      {
        TestFixtureFactory.delete(testObject2);
      }
    }
  }

  /**
   * Test to ensure that you cannot violate a cardinality defined on a parent
   * type.
   */
  public void testFindValidRelationshipByParentAndChild()
  {

    MdBusinessDAOIF mdBusinessIFMdType = MdBusinessDAO.getMdBusinessDAO(MdTypeInfo.CLASS);
    MdBusinessDAOIF mdBusinessIFMdEntity = MdBusinessDAO.getMdBusinessDAO(MdElementInfo.CLASS);

    try
    {
      RelationshipDAO.get(mdBusinessIFMdType.getId(), mdBusinessIFMdEntity.getId(), RelationshipTypes.BUSINESS_INHERITANCE.getType());
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Test to ensure that you cannot violate a cardinality defined on a parent
   * type.
   */
  public void testFindInValidRelationshipByParentAndChild()
  {
    MdBusinessDAOIF mdBusinessIFCommon = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);
    MdBusinessDAOIF mdBusinessIFMdBusiness = MdBusinessDAO.getMdBusinessDAO(MdBusinessInfo.CLASS);

    try
    {
      RelationshipDAO.get(mdBusinessIFCommon.getId(), mdBusinessIFMdBusiness.getId(), RelationshipTypes.BUSINESS_INHERITANCE.getType());
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Test to ensure querying for instances of a super relationship will retrieve
   * the same result as separately querying for all instances of the concrete
   * sub relationships.
   */
  public void testGetAllChildSubRelationshipInstances()
  {
    MdBusinessDAOIF mdBusinessIFMetadata = MdBusinessDAO.getMdBusinessDAO(MetadataInfo.CLASS);

    // Get a list of all concrete relationship types.
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(RelationshipTypes.METADATA_RELATIONSHIP.getType());
    List<MdRelationshipDAOIF> mdRelationshipList = mdRelationshipIF.getAllConcreteSubClasses();

    // For each concrete relationship type, find out how many instances there
    // are to common
    int totalRelationshipCount = 0;
    for (MdRelationshipDAOIF subMdRelationshipIF : mdRelationshipList)
    {
      int relationshipCount = mdBusinessIFMetadata.getChildren(subMdRelationshipIF.definesType()).size();
      totalRelationshipCount += relationshipCount;
    }

    // get the relationship count for all child relationships to common
    int relationshipCount = mdBusinessIFMetadata.getChildren(RelationshipTypes.METADATA_RELATIONSHIP.getType()).size();

    // The method that returns all instances of the super relationship type
    // should be the same as the total
    // instances fetched per type.
    if (totalRelationshipCount != relationshipCount)
    {
      fail("The core failed to retrieve all child instances of relationships that are subtypes of a super relationship.");
    }

  }

  /**
   * Test to ensure querying for instances of a super relationship will retrieve
   * the same result as separately querying for all instances of the concrete
   * sub relationships.
   */
  public void testGetAllParentSubRelationshipInstances()
  {
    MdBusinessDAOIF mdTypeCacheMasterIfCommon = MdBusinessDAO.getMdBusinessDAO(EntityTypes.ENTITY_CACHE_MASTER.getType());

    // Get a list of all concrete relationship types.
    MdRelationshipDAOIF mdRelationshipIF = MdRelationshipDAO.getMdRelationshipDAO(RelationshipTypes.METADATA_RELATIONSHIP.getType());
    List<MdRelationshipDAOIF> mdRelationshipIFList = mdRelationshipIF.getAllConcreteSubClasses();

    // For each concrete relationship type, find out how many instances there
    // are to the class that stores the type cache
    // master list.
    int totalRelationshipCount = 0;
    for (MdRelationshipDAOIF subMdRelationshipIF : mdRelationshipIFList)
    {
      int relationshipCount = mdTypeCacheMasterIfCommon.getParents(subMdRelationshipIF.definesType()).size();
      totalRelationshipCount += relationshipCount;
    }

    // get the relationship count for all parent relationships to common
    int relationshipCount = mdTypeCacheMasterIfCommon.getParents(RelationshipTypes.METADATA_RELATIONSHIP.getType()).size();

    // The method that returns all instances of the super relationship type
    // should be the same as the total
    // instances fetched per type.
    if (totalRelationshipCount != relationshipCount)
    {
      fail("The core failed to retrieve all parent instances of relationships that are subtypes of a super relationship.");
    }
  }

  /**
   * Tests to make sure that a duplicate relationship can be created between a
   * parent and a child.
   */
  public void testDuplicateParentChildForRelationship()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    testObject1.addChild(reference1, TEST_RELATIONSHIP.getType()).apply();

    try
    {
      testObject1.addChild(reference1, TEST_RELATIONSHIP.getType()).apply();
    }
    catch (RelationshipConstraintException e)
    {
      fail("A relationship between a parent and a child was not created, even though it should be allowed. " + "A relationship between the parent and the child already exists, but this relationship type should allow duplicates.");
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  /**
   * Tests to make sure that a duplicate relationship can be created between a
   * parent and a child.
   */
  public void testDuplicateParentChildTree()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    testObject1.addChild(reference1, TEST_RELATIONSHIP_GRAPH.getType()).apply();

    try
    {
      testObject1.addChild(reference1, TEST_RELATIONSHIP_GRAPH.getType()).apply();

      fail("A relationship between a parent and a child was created, but the relationship type is a tree and it therefore should not be allowed.");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  /**
   * Tests if the businessDAO.removeAllChildren() method removes all instances
   * of a parent-child paring.
   */
  public void testRemoveAllChildren()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    RelationshipDAO relationshipDAO1 = null;
    RelationshipDAO relationshipDAO2 = null;
    RelationshipDAO relationshipDAO3 = null;

    try
    {
      relationshipDAO1 = testObject1.addChild(reference1, TEST_RELATIONSHIP.getType());
      relationshipDAO1.apply();

      relationshipDAO2 = testObject1.addChild(reference1, TEST_RELATIONSHIP.getType());
      relationshipDAO2.apply();

      relationshipDAO3 = testObject1.addChild(reference1, TEST_RELATIONSHIP.getType());
      relationshipDAO3.apply();

      List<RelationshipDAOIF> relationshipIFList = testObject1.getChildren(reference1, TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() != 3)
      {
        fail("Relationships were not properly added to the test object");
      }

      testObject1.removeAllChildren(reference1, TEST_RELATIONSHIP.getType());
      relationshipIFList = testObject1.getChildren(reference1, TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() != 0)
      {
        fail("Relationships were not properly deleted from the test object");
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail("An exception was thrown while testing the removal of all relationships with the same parent and child instance pairings. \n" + e.getMessage());
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  /**
   * Tests if the businessDAO.removeChild() method removes an instances of a
   * parent-child paring.
   */
  public void testRemoveChildRelationshipInstance()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    RelationshipDAO relationshipDAO1 = null;

    try
    {
      relationshipDAO1 = testObject1.addChild(reference1, TEST_RELATIONSHIP.getType());
      relationshipDAO1.apply();

      testObject1.removeChild(relationshipDAO1);
      List<RelationshipDAOIF> relationshipIFList = testObject1.getChildren(TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() > 0)
      {
        fail("Relationship were not properly deleted from the test object");
      }

    }
    catch (Exception e)
    {
      fail("An exception was thrown while testing the removal of all relationships with the same parent and child instance pairings.\n" + e.getMessage());
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  /**
   * Tests if the businessDAO.removeAllParents() method removes all instances of
   * a parent-child paring.
   */
  public void testRemoveAllParents()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    RelationshipDAO relationshipDAO1 = null;
    RelationshipDAO relationshipDAO2 = null;
    RelationshipDAO relationshipDAO3 = null;

    try
    {
      relationshipDAO1 = reference1.addParent(testObject1, TEST_RELATIONSHIP.getType());
      relationshipDAO1.apply();

      relationshipDAO2 = reference1.addParent(testObject1, TEST_RELATIONSHIP.getType());
      relationshipDAO2.apply();

      relationshipDAO3 = reference1.addParent(testObject1, TEST_RELATIONSHIP.getType());
      relationshipDAO3.apply();

      List<RelationshipDAOIF> relationshipIFList = reference1.getParents(testObject1, TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() != 3)
      {
        fail("Relationships were not properly added to the test reference");
      }

      reference1.removeAllParents(testObject1, TEST_RELATIONSHIP.getType());
      relationshipIFList = reference1.getParents(testObject1, TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() != 0)
      {
        fail("Relationships were not properly deleted from the test reference");
      }

    }
    catch (Exception e)
    {
      fail("An exception was thrown while testing the removal of all relationships with the same parent and child instance pairings.\n" + e.getMessage());
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  /**
   * Tests if the businessDAO.removeParent() method removes an instances of a
   * parent-child paring.
   */
  public void testRemoveParentRelationshipInstance()
  {
    BusinessDAO testObject1 = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    testObject1.apply();
    BusinessDAO reference1 = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference1.apply();

    RelationshipDAO relationshipDAO1 = null;

    try
    {
      relationshipDAO1 = reference1.addParent(testObject1, TEST_RELATIONSHIP.getType());
      relationshipDAO1.apply();

      reference1.removeParent(relationshipDAO1);
      List<RelationshipDAOIF> relationshipIFList = reference1.getParents(TEST_RELATIONSHIP.getType());
      if (relationshipIFList.size() > 0)
      {
        fail("Relationship were not properly deleted from the test reference");
      }

    }
    catch (Exception e)
    {
      fail("An exception was thrown while testing the removal of all relationships with the same parent and child instance pairings.\n" + e.getMessage());
    }
    finally
    {
      TestFixtureFactory.delete(testObject1);
      TestFixtureFactory.delete(reference1);
    }
  }

  public void testRelationshipWithInvalidChild()
  {
    // create the test to be deleted
    BusinessDAO parent = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    parent.apply();

    BusinessDAO child = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    try
    {
      RelationshipDAO relDAO = RelationshipDAO.newInstance(parent.getId(), child.getId(), TEST_RELATIONSHIP_TREE.getType());
      relDAO.apply();
      
      fail("Able to create relationships with invalid child");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }
  
  public void testRelationshipWithInvalidParent()
  {
    // create the test to be deleted
    BusinessDAO parent = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
    
    BusinessDAO child = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    child.apply();
    
    try
    {
      RelationshipDAO relDAO = RelationshipDAO.newInstance(parent.getId(), child.getId(), TEST_RELATIONSHIP_TREE.getType());
      relDAO.apply();
      
      fail("Able to create relationships with invalid parent");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }
}
