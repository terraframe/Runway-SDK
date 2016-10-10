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
package com.runwaysdk.dataaccess;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.BusinessInfo;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.cache.CacheCodeException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.InheritanceException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.RelationshipDefinitionException;

public class MdRelationshipTest extends TestCase
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

  private static MdRelationshipDAO      abstractParentMdTree1;

  private static MdRelationshipDAO      abstractParentMdTree2;

  private static MdRelationshipDAO      abstractMdRelationship1;

  private static MdGraphDAO             abstractMdGraph1;

  private static MdAttributeConcreteDAO mdAttribute;

  private static MdBusinessDAO          mdBusinessClass1;

  private static MdBusinessDAO          mdBusinessParentClass1;

  private static MdBusinessDAO          mdBusinessChildClass1;

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(MdRelationshipTest.suite()));
  }

  /**
   * 
   * @return
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(MdRelationshipTest.class);

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
   * Creates an abstract relationship that will be extended in various test
   * cases.
   */
  public static void classSetUp()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    TypeInfo ABSTRACT_MD_TREE_1 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AbstractMdTree1");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, ABSTRACT_MD_TREE_1.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, ABSTRACT_MD_TREE_1.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, BusinessInfo.CLASS);
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, BusinessInfo.CLASS);
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent1");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild1");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();
    abstractParentMdTree1 = mdTree;

    mdAttribute = MdAttributeBooleanDAO.newInstance();
    mdAttribute.setValue(MdAttributeBooleanInfo.NAME, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttribute.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, abstractParentMdTree1.getId());
    mdAttribute.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttribute.apply();

    mdBusinessClass1 = MdBusinessDAO.newInstance();
    mdBusinessClass1.setValue(MdBusinessInfo.PACKAGE, "junit.test");
    mdBusinessClass1.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusinessClass1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "junit.test.Class1");
    mdBusinessClass1.setGenerateMdController(false);
    mdBusinessClass1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessClass1.apply();

    mdBusinessParentClass1 = MdBusinessDAO.newInstance();
    mdBusinessParentClass1.setValue(MdBusinessInfo.PACKAGE, "junit.test");
    mdBusinessParentClass1.setValue(MdBusinessInfo.NAME, "ParentClass1");
    mdBusinessParentClass1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "junit.test.ParentClass1");
    mdBusinessParentClass1.setGenerateMdController(false);
    mdBusinessParentClass1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessParentClass1.apply();

    mdBusinessChildClass1 = MdBusinessDAO.newInstance();
    mdBusinessChildClass1.setValue(MdBusinessInfo.PACKAGE, "junit.test");
    mdBusinessChildClass1.setValue(MdBusinessInfo.NAME, "ChildClass1");
    mdBusinessChildClass1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "junit.test.ChildClass1");
    mdBusinessChildClass1.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusinessParentClass1.getId());
    mdBusinessChildClass1.setGenerateMdController(false);
    mdBusinessChildClass1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdBusinessChildClass1.apply();

    TypeInfo ABSTRACT_MD_TREE_2 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AbstractMdTree2");

    // Now for a new Relationship
    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, ABSTRACT_MD_TREE_2.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, ABSTRACT_MD_TREE_2.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdBusinessParentClass1.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdBusinessParentClass1.definesType());
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdBusinessParentClass1.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, mdBusinessParentClass1.definesType());
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent2");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild2");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();
    abstractParentMdTree2 = mdTree;

    TypeInfo ABSTRACT_MD_RELATIONSHIP_1 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "AbstractMdRelationship1");

    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, ABSTRACT_MD_RELATIONSHIP_1.getTypeName());
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, ABSTRACT_MD_RELATIONSHIP_1.getPackageName());
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "AbstractParent3");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "AbstractChild3");
    mdRelationship.setGenerateMdController(false);
    mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.apply();
    abstractMdRelationship1 = mdRelationship;

    TypeInfo ABSTRACT_MD_GRAPH_1 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ConcreteMdGraph");

    MdGraphDAO mdGraph = MdGraphDAO.newInstance();
    mdGraph.setValue(MdGraphInfo.NAME, ABSTRACT_MD_GRAPH_1.getTypeName());
    mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdGraph.setValue(MdGraphInfo.PACKAGE, ABSTRACT_MD_GRAPH_1.getPackageName());
    mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Graph Relationship");
    mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
    mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "*");
    mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
    mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
    mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
    mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
    mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent4");
    mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild4");
    mdGraph.setGenerateMdController(false);
    mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdGraph.apply();
    abstractMdGraph1 = mdGraph;

    TypeInfo CONCRETE_MD_TREE_1 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ConcreteMdTree1");

    MdBusinessDAOIF testClassMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    // Now for a new Relationship
    mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, CONCRETE_MD_TREE_1.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, ABSTRACT_MD_TREE_1.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testClassMdBusiness.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, BusinessInfo.CLASS);
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, testClassMdBusiness.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, BusinessInfo.CLASS);
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent1");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild1");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();

    //
  }

  /**
   * Deletes the abstract relationship.
   */
  public static void classTearDown()
  {
    if (abstractParentMdTree1 != null && abstractParentMdTree1.isAppliedToDB())
    {
      TestFixtureFactory.delete(abstractParentMdTree1);
    }

    if (abstractParentMdTree2 != null && abstractParentMdTree2.isAppliedToDB())
    {
      TestFixtureFactory.delete(abstractParentMdTree2);
    }

    if (abstractMdGraph1 != null && abstractMdGraph1.isAppliedToDB())
    {
      TestFixtureFactory.delete(abstractMdGraph1);
    }

    if (abstractMdRelationship1 != null && abstractMdRelationship1.isAppliedToDB())
    {
      TestFixtureFactory.delete(abstractMdRelationship1);
    }

    if (mdBusinessClass1 != null && mdBusinessClass1.isAppliedToDB())
    {
      TestFixtureFactory.delete(mdBusinessClass1);
    }

    if (mdBusinessChildClass1 != null && mdBusinessChildClass1.isAppliedToDB())
    { 
      TestFixtureFactory.delete(mdBusinessChildClass1);
    }
    
    if (mdBusinessParentClass1 != null && mdBusinessParentClass1.isAppliedToDB())
    {
      TestFixtureFactory.delete(mdBusinessParentClass1);
    }
  }

  /**
   * Creates a relationship type who's parent is not a relationship
   */
  public void testNewMdRelationshipWithInvalidParentType()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ExtendConcreteRelationship");

    MdBusinessDAOIF commonMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();

    try
    {
      mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
      mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
      mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
      mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, commonMdBusinessIF.getId());
      mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
      mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
      mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
      mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
      mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
      mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
      mdTree.setGenerateMdController(false);
      mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTree.apply();
      fail("A Relationship type was defined incorrectly.  Relationship was defined to extend a Class.  Relationships can only extend other Relationships.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdTree != null && mdTree.isAppliedToDB() == true)
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Only abstract relationships may be extended.
   */
  public void testNewMdRelationshipExtendsConcrete()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo concreteRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ConcreteRelationship");

    // Now for a new Relationship
    MdTreeDAO concreteParentMdTree = MdTreeDAO.newInstance();
    concreteParentMdTree.setValue(MdTreeInfo.NAME, concreteRelationship.getTypeName());
    concreteParentMdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    concreteParentMdTree.setValue(MdTreeInfo.PACKAGE, concreteRelationship.getPackageName());
    concreteParentMdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    concreteParentMdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    concreteParentMdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    concreteParentMdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    concreteParentMdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    concreteParentMdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    concreteParentMdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    concreteParentMdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    concreteParentMdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    concreteParentMdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    concreteParentMdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent5");
    concreteParentMdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild5");
    concreteParentMdTree.setGenerateMdController(false);
    concreteParentMdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    concreteParentMdTree.apply();

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "InvalidParentTypeRelationship");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, concreteParentMdTree.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "ConcreteParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "ConcreteChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      fail("A Relationship type was defined incorrectly.  Only abstract Relationships may be extended.  A Relationship was created that extends a concrete Relationship.");

    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      if (concreteParentMdTree != null && concreteParentMdTree.isAppliedToDB() == true)
      {
        concreteParentMdTree.delete();
      }

      if (mdTree != null && mdTree.isAppliedToDB() == true)
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Creates a relationship where the class of the parent objects are not
   * compatible with the class on the parent relationship.
   */
  public void testNewMdRelationshipInvalidClassForParentObjects()
  {
    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadParentClass");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree2.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdBusinessClass1.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdBusinessChildClass1.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "points to \"" + EntityMasterTestSetup.REFERENCE_CLASS.getType() + "\" class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  The parent class on the Relationship must be a subtype of the parent class on the parent Relationship.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
  }

  /**
   * Creates a relationship where the class of the child objects are not
   * compatable with the class on the parent relationship.
   */
  public void testNewMdRelationshipInvalidClassForChildObjects()
  {
    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadParentClass");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree2.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, mdBusinessChildClass1.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, mdBusinessClass1.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  The child class on the Relationship must be a subtype of the child class on the parent Relationship.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
  }

  /**
   * Creates a relationship where the parent cardinality is a negative value.
   */
  public void testNewMdRelationshipInvalidParentCardinalityValue()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "-1");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "1");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  An incorrect cardinality value was specified.  Valid values are a valid integer greater than 1 or the '*' symbol for an unlimited value.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
  }

  /**
   * Creates a relationship where the parent cardinality is greater than the
   * parent cardinality on the super relationship.
   */
  public void testNewMdRelationshipCardinalityGreaterThanSuper1()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  An incorrect cardinality value was specified.  The cardinality cannot be greater than the super cardinality.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
  }

  /**
   * Creates a relationship where the cardinality is greater than the
   * cardinality of a child relationship.
   */
  public void testNewMdRelationshipCardinalityLessThanChild1()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipGoodCardinality");

    // Now for a new Relationship where the PARENT_CARDINALITY is the same as
    // its super cardinality
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();

    abstractParentMdTree1 = MdRelationshipDAO.get(abstractParentMdTree1.getId()).getBusinessDAO();
    
    try
    {
      abstractParentMdTree1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "5");
      abstractParentMdTree1.apply();

      fail("A Relationship was modified incorrectly.  An incorrect cardinality value was specified that conflicts with an inherited cardinality.  The inerited cardinality cannot be greater than the super cardinality.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      // restore the value of the parent.
      abstractParentMdTree1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "10");
      abstractParentMdTree1.apply();

      if (mdTree != null)
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Creates a relationship where the cardinality is greater than the
   * cardinality of a child relationship.
   */
  public void testNewMdRelationshipCardinalityLessThanChild2()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipGoodCardinality");

    // Now for a new Relationship where the PARENT_CARDINALITY is the same as
    // its super cardinality
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();

    try
    {
      abstractParentMdTree1.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "1000");
      abstractParentMdTree1.apply();

      fail("A Relationship was modified incorrectly.  An incorrect cardinality value was specified that conflicts with an inherited cardinality.  The inerited cardinality cannot be greater than the super cardinality.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      // restore the value of the parent.
      abstractParentMdTree1.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
      abstractParentMdTree1.apply();

      if (mdTree != null)
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Correctly modifies a relationship by setting the cardinality greater than
   * the cardinality on a child relationship.
   */
  public void testNewMdRelationshipCardinalityGreaterThanChild()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipGoodCardinality2");

    // Now for a new Relationship where the PARENT_CARDINALITY is the same as
    // its super cardinality
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdTree.apply();

    try
    {
      abstractParentMdTree1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "15");
      abstractParentMdTree1.apply();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("A correctly defined Relationship was not created.  The child cardinality lass than the parent cardinality, yet the core rejected it.");
    }
    catch (Exception e)
    {
      fail("A correctly defined Relationship was not created.  Error: " + e.getMessage());
    }
    finally
    {
      // restore the value of the parent.
      abstractParentMdTree1.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "10");
      abstractParentMdTree1.apply();

      if (mdTree != null)
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Creates a relationship where the the cardinality is greater than the super
   * type for the parent objects.
   */
  public void testNewMdRelationshipParentCardinalityGreaterThanSuper2()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "11");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  An incorrect cardinality value was specified.  The child cardinality cannot be greater than the super cardinality.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
  }

  /**
   * Correctly defines a relationship type where the parent cardinality is less
   * than the cardinality on the super relationship type.
   */
  public void testNewMdRelationshipLessThanParentCardinality1()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "5");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("A correctly defined Relationship was not created.  The child cardinality is less than the parent cardinality, yet the core rejected it.");
    }
    catch (Exception e)
    {
      fail("A correctly defined Relationship was not created.  Error: " + e.getMessage());
    }
  }

  /**
   * Correctly defines a relationship type where the child cardinality is less
   * than the cardinality on the super relationship type.
   */
  public void testNewMdRelationshipLessThanParentCardinality2()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "5");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("A correctly defined Relationship was not created.  The child cardinality is less than the parent cardinality, yet the core rejected it.");
    }
    catch (Exception e)
    {
      fail("A correctly defined Relationship was not created.  Error: " + e.getMessage());
    }
  }

  /**
   * Correctly defines a relationship type where the child and parent
   * cardinalities are equal to the cardinalities on the super relationship
   * type.
   */
  public void testNewMdRelationshipEqualCardinalityWithParent()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelationshipBadCardinality");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Users");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("A correctly defined Relationship was not created.  The child cardinality equal to the parent cardinality, yet the core rejected it.");
    }
    catch (Exception e)
    {
      fail("A correctly defined Relationship was not created.  Error: " + e.getMessage());
    }
  }

  /**
   * Creates a relationship where the the cardinality is greater than the super
   * type for the parent objects.
   */
  public void testNewMdRelationshipAbstractCacheEverything()
  {
    MdBusinessDAOIF testType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());
    MdBusinessDAOIF referenceType = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.REFERENCE_CLASS.getType());

    TypeInfo newRelationship = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelBadCacheAlgorithm");

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();
    mdTree.setValue(MdTreeInfo.NAME, newRelationship.getTypeName());
    mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.PACKAGE, newRelationship.getPackageName());
    mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
    mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.TRUE);
    mdTree.setValue(MdTreeInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, testType.getId());
    mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
    mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceType.getId());
    mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Class");
    mdTree.setValue(MdTreeInfo.PARENT_METHOD, "TestParent");
    mdTree.setValue(MdTreeInfo.CHILD_METHOD, "TestChild");
    mdTree.setGenerateMdController(false);
    mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdTree.apply();
      mdTree.delete();
      fail("A Relationship type was defined incorrectly.  An abstract relationship cannot have a cache all algorithm.");
    }
    catch (CacheCodeException e)
    {
      // This is expected
    }
  }

  /**
   * Test to make sure you cannot extend an MdRelationship with an MdGraph.
   */
  public void testExtendMdRelationshipWithMdGraph()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdTreeInfo.NAME, "SomeTestGraphForJUnit");
      mdGraph.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdTreeInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractMdRelationship1.getId());
      mdGraph.setValue(MdTreeInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdTreeInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent6");
      mdGraph.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild6");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();

      fail("A graph was allowed to extend a relationship.  Graphs cannot extend relationships.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot extend an MdRelationship with an MdTree.
   */
  public void testExtendMdRelationshipWithMdTree()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();

    try
    {
      mdTree.setValue(MdTreeInfo.NAME, "SomeTestGraphForJUnit");
      mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractMdRelationship1.getId());
      mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
      mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent7");
      mdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild7");
      mdTree.setGenerateMdController(false);
      mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTree.apply();

      fail("A tree was allowed to extend a relationship.  Trees cannot extend relationship.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdTree.isAppliedToDB())
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot extend an MdGraph with a MdRelationship.
   */
  public void testExtendMdGraphWithMdRelationship()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();

    try
    {
      mdRelationship.setValue(MdRelationshipInfo.NAME, "SomeTestGraphForJUnit");
      mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, abstractMdGraph1.getId());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "10");
      mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
      mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "AbstractParent8");
      mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "AbstractChild8");
      mdRelationship.setGenerateMdController(false);
      mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdRelationship.apply();

      fail("A relationship was allowed to extend a graph.  Relationships cannot extend graphs.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdRelationship.isAppliedToDB())
      {
        mdRelationship.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot extend an MdGraph with an MdTree.
   */
  public void testExtendMdGraphWithMdTree()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdTreeDAO mdTree = MdTreeDAO.newInstance();

    try
    {
      mdTree.setValue(MdTreeInfo.NAME, "SomeTestGraphForJUnit");
      mdTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdTree.setValue(MdTreeInfo.SUPER_MD_RELATIONSHIP, abstractMdGraph1.getId());
      mdTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "10");
      mdTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
      mdTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdTree.setValue(MdTreeInfo.PARENT_METHOD, "AbstractParent9");
      mdTree.setValue(MdTreeInfo.CHILD_METHOD, "AbstractChild9");
      mdTree.setGenerateMdController(false);
      mdTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdTree.apply();

      fail("A tree was allowed to extend a graph.  Trees cannot extend graphs.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdTree.isAppliedToDB())
      {
        mdTree.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot extend an MdTree with a MdRelationship.
   */
  public void testExtendMdTreeWithMdRelationship()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);

    // Now for a new Relationship
    MdRelationshipDAO mdRelationship = MdRelationshipDAO.newInstance();

    try
    {
      mdRelationship.setValue(MdRelationshipInfo.NAME, "SomeTestGraphForJUnit");
      mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdRelationship.setValue(MdRelationshipInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "10");
      mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
      mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "AbstractParent10");
      mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "AbstractChild10");
      mdRelationship.setGenerateMdController(false);
      mdRelationship.setValue(MdRelationshipInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdRelationship.apply();

      fail("A relationship was allowed to extend a tree.  Relationship cannot extend trees.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdRelationship.isAppliedToDB())
      {
        mdRelationship.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot extend an MdTree with an MdGraph.
   */
  public void testExtendMdTreeWithMdGraph()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "SomeTestGraphForJUnit");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.SUPER_MD_RELATIONSHIP, abstractParentMdTree1.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent11");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild11");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();

      fail("A graph was allowed to extend a tree.  Graphs cannot extend trees.");
    }
    catch (InheritanceException e)
    {
      // This is expected
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with duplicate parent
   * method names.
   */
  public void testDuplicateParentMethod()
  {
    // Method name conflicts are only checked on classes that compile
    MdBusinessDAO testClassMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType()).getBusinessDAO();
    testClassMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    testClassMdBusiness.apply();
    
    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "MethodRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, testClassMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, testClassMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent1");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild379");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
      mdGraph.apply();

      fail("Created a new MdRelationship with conflicting method names.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      testClassMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testClassMdBusiness.apply();
      
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with duplicate parent
   * method names.
   */
  public void testParentMethod()
  {
    MdBusinessDAOIF testClassMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "MethodRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, mdBusinessClass1.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, mdBusinessClass1.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent1");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild379");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("Unable to create a new MdRelationship with conflicting parent method names of different types.");
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with duplicate child
   * method names of the same type.
   */
  public void testDuplicateChildMethod()
  {
    // Method name conflicts are only checked on classes that compile
    MdBusinessDAO testClassMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType()).getBusinessDAO();
    testClassMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
    testClassMdBusiness.apply();
    
    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "MethodRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, testClassMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, testClassMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent3240");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild1");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.TRUE);
      mdGraph.apply();

      fail("Created a new MdRelationship with conflicting method names.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      testClassMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      testClassMdBusiness.apply();
      
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you can create an MdRelationship with duplicate child
   * method names of a different type.
   */
  public void testChildMethod()
  {
    MdBusinessDAOIF testClassMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "MethodRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, mdBusinessClass1.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, mdBusinessClass1.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, testClassMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "AbstractParent3240");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "AbstractChild1");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();
    }
    catch (RelationshipDefinitionException e)
    {
      fail("Unable to create a relationship with a child duplicate method name but different type");
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with duplicate method
   * names.
   */
  public void testSortOrder()
  {
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "MethodRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "SortParent1");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "SortChild1");
      mdGraph.setValue(MdGraphInfo.SORT_MD_ATTRIBUTE, mdAttribute.getId());
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();

      fail("Created a new MdRelationship with an invalid sortMdAttribute value.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with parent of type
   * Component.
   */
  public void testComponentParent()
  {
    MdBusinessDAOIF componentMdBusiness = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "ComponentRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, componentMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, componentMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "CompParent1");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "CompChild1");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();

      fail("Created a new MdRelationship with a parent of type Component.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }

  /**
   * Test to make sure you cannot create an MdRelationship with parent of type
   * Component.
   */
  public void testComponentChild()
  {
    MdBusinessDAOIF componentMdBusiness = MdBusinessDAO.getMdBusinessDAO(ElementInfo.CLASS);
    MdBusinessDAOIF busObjectMdBusiness = MdBusinessDAO.getMdBusinessDAO(BusinessInfo.CLASS);

    // Now for a new Relationship
    MdGraphDAO mdGraph = MdGraphDAO.newInstance();

    try
    {
      mdGraph.setValue(MdGraphInfo.NAME, "ComponentRelationship");
      mdGraph.setValue(MdGraphInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PACKAGE, EntityMasterTestSetup.JUNIT_PACKAGE);
      mdGraph.setStructValue(MdGraphInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship Graph");
      mdGraph.setValue(MdGraphInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdGraph.setValue(MdGraphInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdGraph.setValue(MdGraphInfo.PARENT_MD_BUSINESS, busObjectMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.PARENT_CARDINALITY, "10");
      mdGraph.setStructValue(MdGraphInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, busObjectMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.CHILD_MD_BUSINESS, componentMdBusiness.getId());
      mdGraph.setValue(MdGraphInfo.CHILD_CARDINALITY, "*");
      mdGraph.setStructValue(MdGraphInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, componentMdBusiness.definesType());
      mdGraph.setValue(MdGraphInfo.PARENT_METHOD, "CompParent1");
      mdGraph.setValue(MdGraphInfo.CHILD_METHOD, "CompChild1");
      mdGraph.setGenerateMdController(false);
      mdGraph.setValue(MdGraphInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdGraph.apply();

      fail("Created a new MdRelationship with a child of type Component.");
    }
    catch (RelationshipDefinitionException e)
    {
      // This is expected
    }
    finally
    {
      if (mdGraph.isAppliedToDB())
      {
        mdGraph.delete();
      }
    }
  }
}
