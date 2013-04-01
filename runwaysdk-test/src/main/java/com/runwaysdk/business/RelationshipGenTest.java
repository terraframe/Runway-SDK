/*******************************************************************************
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
 ******************************************************************************/
package com.runwaysdk.business;

import java.util.ArrayList;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdGraphInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipConstraintException;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdGraphDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.OIterator;

/**
 * A relationship between two types entails the generation of several (five) methods in
 * both the parent and child base classes. This suite tests these methods. For example,
 * this suite creates the relationship Ceo Promotes Employee, and then tests:
 *
 * Ceo.addPromotedEmployee(Employee) - Promtes
 * Ceo.removePromotedEmployee(Employee) - void
 * Ceo.getPromotedEmployeeRel(Employee) - List<Promotes>
 * Ceo.getAllPromotedEmployee() - List<Employee>
 * Ceo.getAllPromotedEmployeeRel() - List<Promotes>
 *
 * Similar methods in the child (Employee) are tested.
 *
 * @author Eric G
 */
public class RelationshipGenTest extends TestCase
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

  private static final String       pack = "test.company";
  private static MdBusinessDAO      employee;
  private static MdBusinessDAO      ceo;
  private static MdBusinessDAO      manager;
  private static MdBusinessDAO      peon;
  private static MdBusinessDAO      task;
  private static MdRelationshipDAO  promotes;
  private static MdGraphDAO         assigns;
  private static MdTreeDAO          completes;

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[]
        {
          TestConstants.Path.schema_xsd,
          TestConstants.Path.metadata_xml
        }
      );

    junit.textui.TestRunner.run(RelationshipGenTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(RelationshipGenTest.class);

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

  private static void classSetUp()
  {
    employee = MdBusinessDAO.newInstance();
    employee.setValue(MdBusinessInfo.NAME,                  "Employee");
    employee.setValue(MdBusinessInfo.PACKAGE,               pack);
    employee.setValue(MdBusinessInfo.EXTENDABLE,            MdAttributeBooleanInfo.TRUE);
    employee.setValue(MdBusinessInfo.ABSTRACT,              MdAttributeBooleanInfo.TRUE);
    employee.setStructValue(MdBusinessInfo.DISPLAY_LABEL,   MdAttributeLocalInfo.DEFAULT_LOCALE, "Employee");
    employee.apply();

    ceo = MdBusinessDAO.newInstance();
    ceo.setValue(MdBusinessInfo.NAME,                  "CEO");
    ceo.setValue(MdBusinessInfo.PACKAGE,               pack);
    ceo.setValue(MdBusinessInfo.EXTENDABLE,            MdAttributeBooleanInfo.FALSE);
    ceo.setValue(MdBusinessInfo.SUPER_MD_BUSINESS,     employee.getId());
    ceo.setStructValue(MdBusinessInfo.DISPLAY_LABEL,   MdAttributeLocalInfo.DEFAULT_LOCALE,  "CEO");
    ceo.apply();

    manager = MdBusinessDAO.newInstance();
    manager.setValue(MdBusinessInfo.NAME,                   "Manager");
    manager.setValue(MdBusinessInfo.PACKAGE,                pack);
    manager.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.FALSE);
    manager.setValue(MdBusinessInfo.SUPER_MD_BUSINESS,      employee.getId());
    manager.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE,  "Manager");
    manager.apply();

    peon = MdBusinessDAO.newInstance();
    peon.setValue(MdBusinessInfo.NAME,                    "Peon");
    peon.setValue(MdBusinessInfo.PACKAGE,                 pack);
    peon.setValue(MdBusinessInfo.EXTENDABLE,              MdAttributeBooleanInfo.FALSE);
    peon.setValue(MdBusinessInfo.SUPER_MD_BUSINESS,       employee.getId());
    peon.setStructValue(MdBusinessInfo.DISPLAY_LABEL,     MdAttributeLocalInfo.DEFAULT_LOCALE,  "Peon");
    peon.apply();

    task = MdBusinessDAO.newInstance();
    task.setValue(MdBusinessInfo.NAME,                   "Task");
    task.setValue(MdBusinessInfo.PACKAGE,                pack);
    task.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.FALSE);
    task.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE, "Task");
    task.apply();

    promotes = MdRelationshipDAO.newInstance();
    promotes.setValue(MdRelationshipInfo.NAME,                     "Promotes");
    promotes.setValue(MdRelationshipInfo.PACKAGE,                  pack);
    promotes.setStructValue(MdRelationshipInfo.DISPLAY_LABEL,      MdAttributeLocalInfo.DEFAULT_LOCALE, "Promotion");
    promotes.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS,       ceo.getId());
    promotes.setValue(MdRelationshipInfo.PARENT_CARDINALITY,       "*");
    promotes.setValue(MdRelationshipInfo.PARENT_METHOD,            "PromotedBy");
    promotes.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS,        employee.getId());
    promotes.setValue(MdRelationshipInfo.CHILD_CARDINALITY,        "*");
    promotes.setValue(MdRelationshipInfo.CHILD_METHOD,             "PromotedEmployee");
    promotes.apply();

    assigns = MdGraphDAO.newInstance();
    assigns.setValue(MdGraphInfo.NAME,                     "Assigns");
    assigns.setValue(MdGraphInfo.PACKAGE,                  pack);
    assigns.setStructValue(MdGraphInfo.DISPLAY_LABEL,      MdAttributeLocalInfo.DEFAULT_LOCALE, "Task Assignment");
    assigns.setValue(MdGraphInfo.PARENT_MD_BUSINESS,       manager.getId());
    assigns.setValue(MdGraphInfo.PARENT_CARDINALITY,       "1");
    assigns.setValue(MdGraphInfo.PARENT_METHOD,            "AssignedBy");
    assigns.setValue(MdGraphInfo.CHILD_MD_BUSINESS,        task.getId());
    assigns.setValue(MdGraphInfo.CHILD_CARDINALITY,        "*");
    assigns.setValue(MdRelationshipInfo.CHILD_METHOD,      "AssignedTask");
    assigns.apply();

    completes = MdTreeDAO.newInstance();
    completes.setValue(MdTreeInfo.NAME,                     "Completes");
    completes.setValue(MdTreeInfo.PACKAGE,                  pack);
    completes.setStructValue(MdTreeInfo.DISPLAY_LABEL,      MdAttributeLocalInfo.DEFAULT_LOCALE, "Task Completion");
    completes.setValue(MdTreeInfo.PARENT_MD_BUSINESS,       peon.getId());
    completes.setValue(MdTreeInfo.PARENT_CARDINALITY,       "*");
    completes.setValue(MdTreeInfo.PARENT_METHOD,            "CompletedBy");
    completes.setValue(MdTreeInfo.CHILD_MD_BUSINESS,        task.getId());
    completes.setValue(MdTreeInfo.CHILD_CARDINALITY,        "*");
    completes.setValue(MdTreeInfo.CHILD_METHOD,             "CompletedTask");
    completes.apply();
  }

  private static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  /**
   * Adds two realtionships with the same parent-child, which should work
   *
   * @throws Exception
   */
  public void testAddDuplicateRelationship() throws Exception
  {
    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> employeeClass = LoaderDecorator.load(employee.definesType());

    Business mom = (Business)ceoClass.newInstance();
    mom.apply();
    Business kid = (Business)peonClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship)ceoClass.getMethod("addPromotedEmployee", employeeClass).invoke(mom, kid);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    Relationship rel2 = (Relationship)ceoClass.getMethod("addPromotedEmployee", employeeClass).invoke(mom, kid);
    rel2.apply();
    RelationshipDAOIF oracle2 = RelationshipDAO.get(rel2.getId());

    if (oracle2.getId().equals(oracle.getId()))
      fail("Two Relationships created with the same id");

    if (!oracle.getParentId().equals(mom.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle.getChildId().equals(kid.getId()))
      fail("Child reference mismatch in addChild");

    if (!oracle2.getParentId().equals(mom.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle2.getChildId().equals(kid.getId()))
      fail("Child reference mismatch in addChild");
  }

  /**
   * Attempts to create two Trees with the same parent child, which should throw an
   * Exception
   *
   * @throws Exception
   */
  public void testInvalidDuplicateRelationship() throws Exception
  {
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());

    Business dad = (Business)peonClass.newInstance();
    dad.apply();
    Business kid = (Business)taskClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship)peonClass.getMethod("addCompletedTask", taskClass).invoke(dad, kid);
    rel.apply();

    Relationship rel2 = (Relationship)peonClass.getMethod("addCompletedTask", taskClass).invoke(dad, kid);
    try
    {
      rel2.apply();
      fail("Duplicate relationship (same parent-child) allowed in a Tree");
    }
    catch (RelationshipConstraintException e)
    {
      // This is expected
    }
  }

  /**
   * Tests addParent() in a relationship
   *
   * @throws Exception
   */
  public void testAddParentRelationship() throws Exception
  {
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());

    Business child = (Business) peonClass.newInstance();
    child.apply();
    Business parent = (Business) ceoClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) peonClass.getMethod("addPromotedBy", ceoClass).invoke(child, parent);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addParent");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addParent");
  }

  /**
   * Tests addParent() in a graph
   *
   * @throws Exception
   */
  public void testAddParentGraph() throws Exception
  {
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Class<?> managerClass = LoaderDecorator.load(manager.definesType());

    Business child = (Business) taskClass.newInstance();
    child.apply();
    Business parent = (Business) managerClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) taskClass.getMethod("addAssignedBy", managerClass).invoke(child, parent);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addParent");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addParent");
  }

  /**
   * Tests addParent() in a tree
   *
   * @throws Exception
   */
  public void testAddParentTree() throws Exception
  {
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());

    Business child = (Business) taskClass.newInstance();
    child.apply();
    Business parent = (Business) peonClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) taskClass.getMethod("addCompletedBy", peonClass).invoke(child, parent);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addParent");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addParent");
  }

  /**
   * Tests addChild() in a relationship
   *
   * @throws Exception
   */
  public void testAddChildRelationship() throws Exception
  {
    Class<?> employeeClass = LoaderDecorator.load(employee.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());

    Business child = (Business) peonClass.newInstance();
    child.apply();
    Business parent = (Business) ceoClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) ceoClass.getMethod("addPromotedEmployee", employeeClass).invoke(parent, child);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addChild");
  }

  /**
   * Tests addChild() in a graph
   *
   * @throws Exception
   */
  public void testAddChildGraph() throws Exception
  {
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Class<?> managerClass = LoaderDecorator.load(manager.definesType());

    Business child = (Business) taskClass.newInstance();
    child.apply();
    Business parent = (Business) managerClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) managerClass.getMethod("addAssignedTask", taskClass).invoke(parent, child);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addChild");
  }

  /**
   * Tests addChild() in a tree
   *
   * @throws Exception
   */
  public void testAddChildTree() throws Exception
  {
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());

    Business child = (Business) taskClass.newInstance();
    child.apply();
    Business parent = (Business) peonClass.newInstance();
    parent.apply();

    Relationship rel = (Relationship) peonClass.getMethod("addCompletedTask", taskClass).invoke(parent, child);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(parent.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle.getChildId().equals(child.getId()))
      fail("Child reference mismatch in addChild");
  }

  /**
   * Tests removeChild() on a relationship
   *
   * @throws Exception
   */
  public void testRemoveChildRelationship() throws Exception
  {
    String parentID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String childID = BusinessDAO.newInstance(peon.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> employeeClass = LoaderDecorator.load(employee.definesType());
    Business parent = (Business) ceoClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) peonClass.getMethod("get", String.class).invoke(null, childID);
    ceoClass.getMethod("removePromotedEmployee", employeeClass).invoke(parent, child);

    try
    {
      RelationshipDAO.get(parentID, childID, promotes.definesType());
      fail("removeChild(Business) failed to remove relationships");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Tests removeChild() on a graph
   *
   * @throws Exception
   */
  public void testRemoveChildGraph() throws Exception
  {
    String parentID = BusinessDAO.newInstance(manager.definesType()).apply();
    String childID = BusinessDAO.newInstance(task.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, assigns.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business parent = (Business) managerClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, childID);
    managerClass.getMethod("removeAssignedTask", taskClass).invoke(parent, child);

    try
    {
      RelationshipDAO.get(parentID, childID, assigns.definesType());
      fail("removeChild(Business) failed to remove the graph");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Tests removeChild() on a tree
   *
   * @throws Exception
   */
  public void testRemoveChildTree() throws Exception
  {
    String parentID = BusinessDAO.newInstance(peon.definesType()).apply();
    String childID = BusinessDAO.newInstance(task.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, completes.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business parent = (Business) peonClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, childID);
    peonClass.getMethod("removeCompletedTask", taskClass).invoke(parent, child);

    try
    {
      RelationshipDAO.get(parentID, childID, completes.definesType());
      fail("removeChild(Business) failed to remove the tree");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Tests removeParent() on a relationship
   *
   * @throws Exception
   */
  public void testRemoveParentRelationship() throws Exception
  {
    String parentID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String childID = BusinessDAO.newInstance(peon.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business parent = (Business) ceoClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) peonClass.getMethod("get", String.class).invoke(null, childID);
    peonClass.getMethod("removePromotedBy", ceoClass).invoke(child, parent);

    try
    {
      RelationshipDAO.get(parentID, childID, promotes.definesType());
      fail("removeParent(Business) failed to remove relationships");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Tests removeParent() on a graph
   *
   * @throws Exception
   */
  public void testRemoveParentGraph() throws Exception
  {
    String parentID = BusinessDAO.newInstance(manager.definesType()).apply();
    String childID = BusinessDAO.newInstance(task.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, assigns.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business parent = (Business) managerClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, childID);
    taskClass.getMethod("removeAssignedBy", managerClass).invoke(child, parent);

    try
    {
      RelationshipDAO.get(parentID, childID, assigns.definesType());
      fail("removeParent(Business) failed to remove the graph");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Tests removeParent() on a tree
   *
   * @throws Exception
   */
  public void testRemoveParentTree() throws Exception
  {
    String parentID = BusinessDAO.newInstance(peon.definesType()).apply();
    String childID = BusinessDAO.newInstance(task.definesType()).apply();
    RelationshipDAO.newInstance(parentID, childID, completes.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business parent = (Business) peonClass.getMethod("get", String.class).invoke(null, parentID);
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, childID);
    taskClass.getMethod("removeCompletedBy", peonClass).invoke(child, parent);

    try
    {
      RelationshipDAO.get(parentID, childID, completes.definesType());
      fail("removeParent(Business) failed to remove the tree");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Calls getRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetRelationships() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();
    String oracle2 = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();
    String oracle3 = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> employeeClass = LoaderDecorator.load(employee.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) ceoClass.getMethod("getPromotedEmployeeRel", employeeClass).invoke(ceo, peon)).getAll();

    if (list.size()!=3)
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!id.equals(oracle) && !id.equals(oracle2) && !id.equals(oracle3))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetRelationshipsEmpty() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> employeeClass = LoaderDecorator.load(employee.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) ceoClass.getMethod("getPromotedEmployeeRel", employeeClass).invoke(ceo, peon)).getAll();

    if (list.size()!=0)
      fail("getRelationships() returned expected an empty list but returned results");
  }

  /**
   * Calls getRel() from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetRelationships() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();
    String oracle2 = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();
    String oracle3 = RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getPromotedByRel", ceoClass).invoke(peon, ceo)).getAll();

    if (list.size()!=3)
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!id.equals(oracle) && !id.equals(oracle2) && !id.equals(oracle3))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getRel() from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetRelationshipsEmpty() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getPromotedByRel", ceoClass).invoke(peon, ceo)).getAll();

    if (list.size()!=0)
      fail("getRelationships() returned expected an empty list but returned results");
  }

  /**
   * Calls getRel() from the parent in a graph relationship
   *
   * @throws Exception
   */
  public void testParentGetGraph() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(managerID, taskID, assigns.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) managerClass.getMethod("getAssignedTaskRel", taskClass).invoke(manager, task);

    if (!test.getId().equals(oracle))
      fail("getRelationship returned the wrong relationship");
  }

  /**
   * Calls getRel() from the parent in a graph relationship
   *
   * @throws Exception
   */
  public void testParentGetGraphNull() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) managerClass.getMethod("getAssignedTaskRel", taskClass).invoke(manager, task);

    if (test!=null)
      fail("getGraph expected null but returned a relationship");
  }

  /**
   * Calls getRel() from the child in a graph relationship
   *
   * @throws Exception
   */
  public void testChildGetGraph() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(managerID, taskID, assigns.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) taskClass.getMethod("getAssignedByRel", managerClass).invoke(task, manager);

    if (!test.getId().equals(oracle))
      fail("getRelationship returned the wrong relationship");
  }

  /**
   * Calls getRel() from the child in a graph relationship
   *
   * @throws Exception
   */
  public void testChildGetGraphNull() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) taskClass.getMethod("getAssignedByRel", managerClass).invoke(task, manager);

    if (test!=null)
      fail("getGraph expected null but returned a relationship");
  }

  /**
   * Calls getRel() from the parent in a tree relationship
   *
   * @throws Exception
   */
  public void testParentGetTree() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(peonID, taskID, completes.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) peonClass.getMethod("getCompletedTaskRel", taskClass).invoke(peon, task);

    if (!test.getId().equals(oracle))
      fail("getRelationship returned the wrong relationship");
  }

  /**
   * Calls getRel() from the parent in a tree relationship
   *
   * @throws Exception
   */
  public void testParentGetTreeNull() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) peonClass.getMethod("getCompletedTaskRel", taskClass).invoke(peon, task);

    if (test!=null)
      fail("getTree expected null but returned a relationship");
  }

  /**
   * Calls getRel() from the child in a tree relationship
   *
   * @throws Exception
   */
  public void testChildGetTree() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String oracle = RelationshipDAO.newInstance(peonID, taskID, completes.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) taskClass.getMethod("getCompletedByRel", peonClass).invoke(task, peon);

    if (!test.getId().equals(oracle))
      fail("getRelationship returned the wrong relationship");
  }

  /**
   * Calls getRel() from the child in a tree relationship
   *
   * @throws Exception
   */
  public void testChildGetTreeNull() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    Relationship test = (Relationship) taskClass.getMethod("getCompletedByRel", peonClass).invoke(task, peon);

    if (test!=null)
      fail("getTree expected null but returned a relationship");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllRelationships() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String ceoID2 = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(ceoID, managerID, promotes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(ceoID, ceoID2, promotes.definesType()).apply());

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    List<Relationship> list = ((OIterator<Relationship>) ceoClass.getMethod("getAllPromotedEmployeeRel").invoke(ceo)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllRelationshipsEmpty() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    List<Relationship> list = ((OIterator<Relationship>) ceoClass.getMethod("getAllPromotedEmployeeRel").invoke(ceo)).getAll();

    if (list.size()!=0)
      fail("getAllRealtionships() expected an empty list but returned results");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllRelationships() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String ceoID2 = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(ceoID, peonID, promotes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(ceoID2, peonID, promotes.definesType()).apply());

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getAllPromotedByRel").invoke(peon)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllRelationshipsEmpty() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getAllPromotedByRel").invoke(peon)).getAll();

    if (list.size()!=0)
      fail("getAllRealtionships() expected an empty list but returned results");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllGraphs() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID2 = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID3 = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID4 = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID5 = BusinessDAO.newInstance(task.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(managerID, taskID, assigns.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(managerID, taskID2, assigns.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(managerID, taskID3, assigns.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(managerID, taskID4, assigns.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(managerID, taskID5, assigns.definesType()).apply());

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    List<Relationship> list = ((OIterator<Relationship>) managerClass.getMethod("getAllAssignedTaskRel").invoke(manager)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllGraphsEmpty() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    List<Relationship> list = ((OIterator<Relationship>) managerClass.getMethod("getAllAssignedTaskRel").invoke(manager)).getAll();

    if (list.size()!=0)
      fail("getAllGraphs() expected an empty list but returned results");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllGraphs() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(managerID, taskID, assigns.definesType()).apply());

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Relationship> list = ((OIterator<Relationship>) taskClass.getMethod("getAllAssignedByRel").invoke(task)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllGraphsNull() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Relationship> list = ((OIterator<Relationship>) taskClass.getMethod("getAllAssignedByRel").invoke(task)).getAll();

    if (list.size()!=0)
      fail("getAllGraphs() expected an empty list but returned results");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllTrees() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID2 = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID3 = BusinessDAO.newInstance(task.definesType()).apply();
    String taskID4 = BusinessDAO.newInstance(task.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(peonID, taskID, completes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(peonID, taskID2, completes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(peonID, taskID3, completes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(peonID, taskID4, completes.definesType()).apply());

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getAllCompletedTaskRel").invoke(peon)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testParentGetAllTreesNull() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peon = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Relationship> list = ((OIterator<Relationship>) peonClass.getMethod("getAllCompletedTaskRel").invoke(peon)).getAll();

    if (list.size()!=0)
      fail("getAllTrees() expected an empty list but returned results");
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllTrees() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    String peonID2 = BusinessDAO.newInstance(peon.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(RelationshipDAO.newInstance(peonID, taskID, completes.definesType()).apply());
    oracle.add(RelationshipDAO.newInstance(peonID2, taskID, completes.definesType()).apply());

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Relationship> list = ((OIterator<Relationship>) taskClass.getMethod("getAllCompletedByRel").invoke(task)).getAll();

    if (list.size()!=oracle.size())
      fail("getRelationships() returned a List of the wrong size");

    for (Relationship relationship : list)
    {
      String id = relationship.getId();
      if (!oracle.remove(id))
        fail("ID in list of Relationships is not one of the expected ids");
    }
  }

  /**
   * Calls getAllRel() from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testChildGetAllTreesNull() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business task = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Relationship> list = ((OIterator<Relationship>) taskClass.getMethod("getAllCompletedByRel").invoke(task)).getAll();

    if (list.size()!=0)
      fail("getAllTrees() expected an empty list but returned results");
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllChildren() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(peonID);
    oracle.add(peonID);
    oracle.add(BusinessDAO.newInstance(ceo.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(manager.definesType()).apply());

    for (String childID : oracle)
      RelationshipDAO.newInstance(ceoID, childID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    List<Business> list = ((OIterator<Business>) ceoClass.getMethod("getAllPromotedEmployee").invoke(ceo)).getAll();

    if (list.size()!=oracle.size() - 1)
      fail("getChildren() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of children is not one of the expected ids");
    }
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllChildrenEmpty() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    List<Business> list = ((OIterator<Business>) ceoClass.getMethod("getAllPromotedEmployee").invoke(ceo)).getAll();

    if (list.size()!=0)
      fail("getChildren() returned a List of the wrong size");
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllGraphChildren() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());

    for (String childID : oracle)
      RelationshipDAO.newInstance(managerID, childID, assigns.definesType()).apply();

    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    List<Business> list = ((OIterator<Business>) managerClass.getMethod("getAllAssignedTask").invoke(manager)).getAll();

    if (list.size()!=oracle.size())
      fail("getChildren() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of children is not one of the expected ids");
    }
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllGraphChildrenEmpty() throws Exception
  {
    String managerID = BusinessDAO.newInstance(manager.definesType()).apply();
    Class<?> managerClass = LoaderDecorator.load(manager.definesType());
    Business manager = (Business) managerClass.getMethod("get", String.class).invoke(null, managerID);
    List<Business> list = ((OIterator<Business>) managerClass.getMethod("getAllAssignedTask").invoke(manager)).getAll();

    if (list.size()!=0)
      fail("getChildren() returned a List of the wrong size");
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllTreeChildren() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(task.definesType()).apply());

    for (String childID : oracle)
      RelationshipDAO.newInstance(peonID, childID, completes.definesType()).apply();

    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peonObj = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Business> list = ((OIterator<Business>) peonClass.getMethod("getAllCompletedTask").invoke(peonObj)).getAll();

    if (list.size()!=oracle.size())
      fail("getChildren() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of children is not one of the expected ids");
    }
  }

  /**
   * Calls getAllChildren(type) from the parent in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllTreeChildrenEmpty() throws Exception
  {
    String peonID = BusinessDAO.newInstance(peon.definesType()).apply();
    Class<?> peonClass = LoaderDecorator.load(peon.definesType());
    Business peonObj = (Business) peonClass.getMethod("get", String.class).invoke(null, peonID);
    List<Business> list = ((OIterator<Business>) peonClass.getMethod("getAllCompletedTask").invoke(peonObj)).getAll();

    if (list.size()!=0)
      fail("getChildren() returned a List of the wrong size");
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllParents() throws Exception
  {
    String childID = BusinessDAO.newInstance(ceo.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(BusinessDAO.newInstance(ceo.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(ceo.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(ceo.definesType()).apply());

    for (String parentID : oracle)
      RelationshipDAO.newInstance(parentID, childID, promotes.definesType()).apply();

    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business child = (Business) ceoClass.getMethod("get", String.class).invoke(null, childID);
    List<Business> list = ((OIterator<Business>) ceoClass.getMethod("getAllPromotedBy").invoke(child)).getAll();

    if (list.size()!=oracle.size())
      fail("getAllParents() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of parents is not one of the expected ids");
    }
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllParentsEmpty() throws Exception
  {
    String ceoID = BusinessDAO.newInstance(ceo.definesType()).apply();
    Class<?> ceoClass = LoaderDecorator.load(ceo.definesType());
    Business ceo = (Business) ceoClass.getMethod("get", String.class).invoke(null, ceoID);
    List<Business> list = ((OIterator<Business>) ceoClass.getMethod("getAllPromotedBy").invoke(ceo)).getAll();

    if (list.size()!=0)
      fail("getAllParents() returned a List of the wrong size");
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllGraphParents() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(BusinessDAO.newInstance(manager.definesType()).apply());

    for (String parentID : oracle)
      RelationshipDAO.newInstance(parentID, taskID, assigns.definesType()).apply();

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Business> list = ((OIterator<Business>) taskClass.getMethod("getAllAssignedBy").invoke(child)).getAll();

    if (list.size()!=oracle.size())
      fail("getAllParents() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of parents is not one of the expected ids");
    }
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllGraphParentsEmpty() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business manager = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Business> list = ((OIterator<Business>) taskClass.getMethod("getAllAssignedBy").invoke(manager)).getAll();

    if (list.size()!=0)
      fail("getAllParents() returned a List of the wrong size");
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllTreeParents() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    List<String> oracle = new ArrayList<String>();
    oracle.add(BusinessDAO.newInstance(peon.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(peon.definesType()).apply());
    oracle.add(BusinessDAO.newInstance(peon.definesType()).apply());

    for (String parentID : oracle)
      RelationshipDAO.newInstance(parentID, taskID, completes.definesType()).apply();

    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Business> list = ((OIterator<Business>) taskClass.getMethod("getAllCompletedBy").invoke(child)).getAll();

    if (list.size()!=oracle.size())
      fail("getAllParents() returned a List of the wrong size");

    for (Business object : list)
    {
      String id = object.getId();
      if (!oracle.remove(id))
        fail("ID in list of parents is not one of the expected ids");
    }
  }

  /**
   * Calls testGetAllParents(type) from the child in a relationship
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testGetAllTreeParentsEmpty() throws Exception
  {
    String taskID = BusinessDAO.newInstance(task.definesType()).apply();
    Class<?> taskClass = LoaderDecorator.load(task.definesType());
    Business child = (Business) taskClass.getMethod("get", String.class).invoke(null, taskID);
    List<Business> list = ((OIterator<Business>) taskClass.getMethod("getAllCompletedBy").invoke(child)).getAll();

    if (list.size()!=0)
      fail("getAllParents() returned a List of the wrong size");
  }
}
