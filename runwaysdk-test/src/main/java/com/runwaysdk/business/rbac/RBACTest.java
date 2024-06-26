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
package com.runwaysdk.business.rbac;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.session.Request;

public class RBACTest
{
  /**
   * Rolename for the master role
   */
  private static String                    MASTER    = "runway.Master";

  /**
   * Rolename for the executive role
   */
  private static String                    EXECUTIVE = "runway.Executive";

  /**
   * Rolename for the tester role
   */
  private static String                    TESTER    = "runway.Tester";

  /**
   * Rolename for the secretary role
   */
  private static String                    SECRETARY = "runway.Secretary";

  /**
   * Rolename for the worker role
   */
  private static String                    WORKER    = "runway.Worker";

  /**
   * The test user
   */
  private static UserDAO                   newUser;

  /**
   * The test MethodActor
   */
  private static MethodActorDAO            methodActor;

  /**
   * The test MdRelationship
   */
  private static MdRelationshipDAO         mdTree;

  /**
   * The test MdBusiness
   */
  private static MdBusinessDAO             mdBusiness;

  /**
   * The test mdBusiness2 data object
   */
  private static MdBusinessDAO             mdBusiness2;

  /**
   * The test MdMethod
   */
  private static MdMethodDAO               mdMethod;

  /**
   * The oid of the test attribute
   */
  private static MdAttributeCharacterDAO   mdAttributeCharacter;

  /**
   * The test MethodActor
   */
  private static MethodActorDAO            methodActor_Util;

  /**
   * The test MdUtil
   */
  private static MdUtilDAO                 mdUtil;

  /**
   * The test MdMethod
   */
  private static MdMethodDAO               mdMethod_Util;

  /**
   * The oid of the test attribute
   */
  private static MdAttributeCharacterDAO   mdAttributeCharacter_Util;

  /**
   * The test MethodActor
   */
  private static MethodActorDAO            methodActor_View;

  /**
   * The test MdView
   */
  private static MdViewDAO                 mdView;

  /**
   * The test MdMethod
   */
  private static MdMethodDAO               mdMethod_View;

  private static MdDomainDAO               mdDomain;

  private static MdDimensionDAO            mdDimension;

  private static MdAttributeDimensionDAOIF mdAttributeDimension;

  /**
   * The oid of the test attribute
   */
  private static MdAttributeCharacterDAO   mdAttributeCharacter_View;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    // Create a new user
    newUser = UserDAO.newInstance();
    newUser.setValue("username", "TESTER132423");
    newUser.setValue("password", "justin");
    newUser.apply();

    if (! "true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")))
    {
      mdDimension = TestFixtureFactory.createMdDimension();
      mdDimension.apply();
    }

    // Create a new MdBusiness
    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    // Create a new relationship
    mdTree = TestFixtureFactory.createMdTree(mdBusiness2, mdBusiness2);
    mdTree.apply();

    // Create a new class
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    // Create a new attribute for the class
    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdBusiness);
    mdAttributeCharacter.apply();

    if (! "true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")))
    {
      mdAttributeDimension = mdAttributeCharacter.getMdAttributeDimension(mdDimension);
    }

    mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    // Create a new class
    mdUtil = TestFixtureFactory.createMdUtil1();
    mdUtil.apply();

    // Create a new attribute for the class
    mdAttributeCharacter_Util = TestFixtureFactory.addCharacterAttribute(mdUtil);
    mdAttributeCharacter_Util.apply();

    mdMethod_Util = TestFixtureFactory.createMdMethod(mdUtil);
    mdMethod_Util.apply();

    methodActor_Util = TestFixtureFactory.createMethodActor(mdMethod_Util);
    methodActor_Util.apply();

    // Create a new class
    mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    // Create a new attribute for the class
    mdAttributeCharacter_View = TestFixtureFactory.addCharacterAttribute(mdView);
    mdAttributeCharacter_View.apply();

    mdMethod_View = TestFixtureFactory.createMdMethod(mdView);
    mdMethod_View.apply();

    methodActor_View = TestFixtureFactory.createMethodActor(mdMethod_View);
    methodActor_View.apply();

    mdDomain = TestFixtureFactory.createMdDomain();
    mdDomain.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(methodActor);
    TestFixtureFactory.delete(mdMethod);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdTree);
    TestFixtureFactory.delete(newUser);
    TestFixtureFactory.delete(mdDomain);

    TestFixtureFactory.delete(methodActor_Util);
    TestFixtureFactory.delete(mdMethod_Util);
    TestFixtureFactory.delete(mdUtil);
    TestFixtureFactory.delete(methodActor_View);
    TestFixtureFactory.delete(mdMethod_View);
    TestFixtureFactory.delete(mdView);
    
    if (! "true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")))
    {
      TestFixtureFactory.delete(mdDimension);
    }
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Request
  @Before
  public void setUp() throws Exception
  {
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  @Request
  @After
  public void tearDown() throws Exception
  {

  }

  @Request
  @Test
  public void testLocalesOfDefaultUsers()
  {
    UserDAOIF[] users = new UserDAOIF[] { UserDAO.getPublicUser() };

    for (UserDAOIF user : users)
    {
      Assert.assertEquals(CommonProperties.getDefaultLocaleString(), user.getLocale());
    }
  }

  /**
   * Test the creation and deletion of roles
   */
  @Request
  @Test
  public void testRoleCreate()
  {
    RoleDAO role = RoleDAO.createRole(TESTER, "Tester");

    BusinessDAOIF businessDAO = BusinessDAO.get(role.getOid());

    // Ensure that the role is created
    Assert.assertEquals(businessDAO.getValue(RoleDAO.ROLENAME), TESTER);

    role.delete();
  }

  /**
   * Test the getRole function
   */
  @Request
  @Test
  public void testGetRole()
  {
    RoleDAO role = RoleDAO.createRole(TESTER, "Tester");

    Assert.assertEquals(role.getOid(), RoleDAO.findRole(TESTER).getOid());

    role.delete();
  }

  /**
   * Test assigning and removing a user from a role
   */
  @Request
  @Test
  public void testAssignUser()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(TESTER, "Tester");

    // Assign user to a role
    role.assignMember(newUser);

    List<RelationshipDAOIF> list = newUser.getChildren(RelationshipTypes.ASSIGNMENTS.getType());

    Assert.assertEquals(list.size(), 1);
    Assert.assertEquals(list.get(0).getChildOid(), role.getOid());

    // Deassign user from a role
    role.deassignMember(newUser);

    list = newUser.getChildren(RelationshipTypes.ASSIGNMENTS.getType());

    // Ensure that the relationship no longer works
    Assert.assertEquals(list.size(), 0);

    role.delete();
  }

  /**
   * Test assigning and removing a user from a role
   */
  @Request
  @Test
  public void testAssignMethod()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(TESTER, "Tester");

    // Assign user to a role
    role.assignMember(methodActor);

    List<RelationshipDAOIF> list = methodActor.getChildren(RelationshipTypes.ASSIGNMENTS.getType());

    Assert.assertEquals(list.size(), 1);
    Assert.assertEquals(list.get(0).getChildOid(), role.getOid());

    // Deassign user from a role
    role.deassignMember(methodActor);

    list = methodActor.getChildren(RelationshipTypes.ASSIGNMENTS.getType());

    // Ensure that the relationship no longer works
    Assert.assertEquals(list.size(), 0);

    role.delete();
  }

  /**
   * Test assigning permissions to a class
   */
  @Request
  @Test
  public void testClassPermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Create a list of operations
    List<Operation> operations = new LinkedList<Operation>();
    // operations.add(Operation.PROMOTE);
    operations.add(Operation.WRITE);
    operations.add(Operation.READ);

    // Grant individual permissions to the role
    role.grantPermission(Operation.CREATE, mdBusiness.getOid());
    role.grantPermission(Operation.WRITE, mdBusiness.getOid());

    // Grant a list of permissions to the role
    role.grantPermission(operations, mdBusiness.getOid());

    // Get the list of operations for a permission
    RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdBusiness.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations is correct
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.CREATE));
    // Assert.assertTrue(Operation.containsOperation(enumeration.dereference(),
    // Operation.PROMOTE));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

    // Grant individual permission to a user
    newUser.grantPermission(Operation.READ, mdBusiness.getOid());

    relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdBusiness.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations for a user is correctly set
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

    relationshipDAO.getRelationshipDAO().delete();
    role.delete();

    role.revokePermission(Operation.CREATE, mdBusiness.getOid());
    role.revokePermission(Operation.WRITE, mdBusiness.getOid());
    newUser.revokePermission(Operation.READ, mdBusiness.getOid());
  }

  /**
   * Test assigning permissions to an attribute
   */
  @Request
  @Test
  public void testAttributePermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Create a list of operations
    List<Operation> operations = new LinkedList<Operation>();
    operations.add(Operation.WRITE);
    operations.add(Operation.READ);

    // Grant individual attribute permissions to the role
    role.grantPermission(Operation.GRANT, mdAttributeCharacter.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(operations, mdAttributeCharacter.getOid());

    // Get the list of operations for a permission
    RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdAttributeCharacter.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations is correct
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.GRANT));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));

    // Grant individual permission to a user
    newUser.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdAttributeCharacter.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations for a user is correctly set
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

    relationshipDAO.getRelationshipDAO().delete();
    role.delete();

    role.revokePermission(Operation.GRANT, mdAttributeCharacter.getOid());
    role.revokeAllPermissions(mdAttributeCharacter.getOid());
    newUser.revokePermission(Operation.READ, mdAttributeCharacter.getOid());
  }

  /**
   * Test assigning permissions to an attribute
   */
  @Request
  @Test
  public void testGetAssignedPermissions()
  {
    // Create a new role
    RoleDAO parent = RoleDAO.createRole(MASTER, "Master");

    RoleDAO child = parent.addDescendant(WORKER, "Worker");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);
      operations.add(Operation.GRANT);

      // Grant a list of attribute permissions to the role
      parent.grantPermission(operations, mdAttributeCharacter.getOid());

      // Get the list of operations for a permission
      Set<Operation> permissions = child.getAllPermissions(mdAttributeCharacter);

      // Ensure that the list of operations is correct
      for (Operation operation : operations)
      {
        Assert.assertTrue(permissions.contains(operation));
      }

      // Get the list of explicitly defined operations for a permission
      permissions = child.getAssignedPermissions(mdAttributeCharacter);

      // Ensure that the list of operations is correct
      for (Operation operation : operations)
      {
        Assert.assertFalse(permissions.contains(operation));
      }

    }
    finally
    {
      child.delete();

      parent.revokeAllPermissions(mdAttributeCharacter.getOid());
      parent.delete();
    }
  }

  /**
   * Test assigning permissions to a relationship
   */
  @Request
  @Test
  public void testRelationshipPermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Create a list of operations
    List<Operation> operations = new LinkedList<Operation>();
    operations.add(Operation.DELETE);
    operations.add(Operation.WRITE);
    operations.add(Operation.READ);

    // Grant individual attribute permissions to the role
    role.grantPermission(Operation.CREATE, mdTree.getOid());
    role.grantPermission(Operation.WRITE, mdTree.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(operations, mdTree.getOid());

    // Get the list of operations for a permission
    RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdTree.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations is correct
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.CREATE));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.DELETE));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

    // Grant individual permission to a user
    newUser.grantPermission(Operation.DELETE, mdTree.getOid());

    relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdTree.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

    enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure that the list of operations for a user is correctly set
    Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.DELETE));

    relationshipDAO.getRelationshipDAO().delete();
    role.delete();

    role.revokePermission(Operation.CREATE, mdTree.getOid());
    role.revokePermission(Operation.WRITE, mdTree.getOid());
    newUser.revokePermission(Operation.DELETE, mdTree.getOid());
  }

  /**
   * Test removing permissions from a metaData type
   */
  @Request
  @Test
  public void testRevokePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Create a list of operations
    List<Operation> operations = new LinkedList<Operation>();
    operations.add(Operation.DELETE);
    operations.add(Operation.WRITE);
    operations.add(Operation.READ);

    // Grant a list of attribute permissions to the role
    role.grantPermission(operations, mdBusiness.getOid());

    // Remove permissions
    role.revokeAllPermissions(mdBusiness.getOid());

    // Get the list of operations for a permission
    List<RelationshipDAOIF> list = role.getChildren(RelationshipTypes.TYPE_PERMISSION.getType());

    // Ensure the list is empty
    Assert.assertEquals(list.size(), 0);

    // Regrant permisisons
    role.grantPermission(operations, mdBusiness.getOid());

    // Remove the promote pemission
    role.revokePermission(Operation.DELETE, mdBusiness.getOid());

    RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdBusiness.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);
    AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

    // Ensure the list of permissions granted does not include PROMOTE
    Assert.assertFalse(OperationManager.containsOperation(enumeration.dereference(), Operation.DELETE));

    role.delete();
  }

  /**
   * Test assigned users
   */
  @Request
  @Test
  public void testAssignedUsers()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    String userId = newUser.getOid();

    role.assignMember(newUser);

    Set<SingleActorDAOIF> list = role.assignedActors();

    Assert.assertEquals(list.size(), 1);
    Assert.assertEquals(userId, list.iterator().next().getOid());

    role.delete();
  }

  /**
   * Test assigned users
   */
  @Request
  @Test
  public void testAssignedMethod()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    String memberId = methodActor.getOid();

    role.assignMember(methodActor);

    Set<SingleActorDAOIF> list = role.assignedActors();

    Assert.assertEquals(list.size(), 1);
    Assert.assertEquals(memberId, list.iterator().next().getOid());

    role.delete();
  }

  /**
   * test assigned roles
   */
  @Request
  @Test
  public void testAssignedRoles()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = RoleDAO.createRole(SECRETARY, "Secretary");

    role.assignMember(newUser);
    role2.assignMember(newUser);

    Set<RoleDAOIF> list = newUser.assignedRoles();

    list.contains(role);

    Assert.assertEquals(2, list.size());
    Assert.assertTrue(list.contains(role));
    Assert.assertTrue(list.contains(role2));

    role.delete();
    role2.delete();
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Grant a list of class permissions to the role
    role.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    // Grant a list of relationship permissions to the role
    role.grantPermission(Operation.WRITE, mdTree.getOid());

    Set<RelationshipDAOIF> set = role.getAllPermissions();

    List<String> list = new LinkedList<String>();

    for (RelationshipDAOIF rel : set)
    {
      list.add(rel.getChildOid());
    }

    Assert.assertEquals(list.size(), 3);
    Assert.assertTrue(list.contains(mdBusiness.getOid()));
    Assert.assertTrue(list.contains(mdAttributeCharacter.getOid()));
    Assert.assertTrue(list.contains(mdTree.getOid()));

    role.revokePermission(Operation.DELETE, mdBusiness.getOid());
    role.revokePermission(Operation.READ, mdAttributeCharacter.getOid());

    role.delete();
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    role.assignMember(newUser);

    // Grant a list of class permissions to the role
    newUser.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    // Grant a list of relationship permissions to the role
    role.grantPermission(Operation.WRITE, mdTree.getOid());

    Set<RelationshipDAOIF> set = newUser.getAllPermissions();

    List<String> list = new LinkedList<String>();

    for (RelationshipDAOIF rel : set)
    {
      list.add(rel.getChildOid());
    }

    Assert.assertEquals(list.size(), 3);
    Assert.assertTrue(list.contains(mdBusiness.getOid()));
    Assert.assertTrue(list.contains(mdAttributeCharacter.getOid()));
    Assert.assertTrue(list.contains(mdTree.getOid()));

    role.revokePermission(Operation.READ, mdAttributeCharacter.getOid());
    role.revokePermission(Operation.WRITE, mdTree.getOid());

    newUser.revokeAllPermissions(mdBusiness.getOid());
    newUser.revokePermission(Operation.READ, mdAttributeCharacter.getOid());

    role.delete();
  }

  /**
   * test method permissions
   */
  @Request
  @Test
  public void testMethodPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    role.assignMember(methodActor);

    // Grant a list of class permissions to the role
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    // Grant a list of relationship permissions to the role
    role.grantPermission(Operation.WRITE, mdTree.getOid());

    Set<RelationshipDAOIF> set = methodActor.getAllPermissions();

    List<String> list = new LinkedList<String>();

    for (RelationshipDAOIF rel : set)
    {
      list.add(rel.getChildOid());
    }

    Assert.assertEquals(3, list.size());
    Assert.assertTrue(list.contains(mdBusiness.getOid()));
    Assert.assertTrue(list.contains(mdAttributeCharacter.getOid()));
    Assert.assertTrue(list.contains(mdTree.getOid()));

    role.revokePermission(Operation.READ, mdAttributeCharacter.getOid());
    role.revokePermission(Operation.WRITE, mdTree.getOid());

    methodActor.revokeAllPermissions(mdBusiness.getOid());
    role.delete();
  }

  @Request
  @Test
  public void testOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    role.assignMember(newUser);

    // Grant a list of class permissions to the role
    newUser.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(Operation.READ, mdBusiness.getOid());

    // Grant a list of relationship permissions to the role
    role.grantPermission(Operation.WRITE, mdBusiness.getOid());

    Set<Operation> list = role.getAllPermissions(mdBusiness);

    Assert.assertEquals(list.size(), 2);
    Assert.assertTrue(list.contains(Operation.READ));
    Assert.assertTrue(list.contains(Operation.WRITE));

    list = newUser.getAllPermissions(mdBusiness);

    Assert.assertEquals(list.size(), 3);
    Assert.assertTrue(list.contains(Operation.READ));
    Assert.assertTrue(list.contains(Operation.WRITE));
    Assert.assertTrue(list.contains(Operation.DELETE));

    role.revokePermission(Operation.READ, mdBusiness.getOid());
    role.revokePermission(Operation.WRITE, mdBusiness.getOid());

    newUser.revokeAllPermissions(mdBusiness.getOid());
    newUser.revokePermission(Operation.DELETE, mdBusiness.getOid());
    role.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testAddInheritance()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = RoleDAO.createRole(SECRETARY, "Secretary");

    // Test add inheritance
    role.addInheritance(role2);

    List<RelationshipDAOIF> list = BusinessDAO.get(role.getOid()).getChildren(RoleDAO.ROLE_INHERITANCE);

    Assert.assertEquals(1, list.size());
    Assert.assertEquals(role2.getOid(), list.get(0).getChildOid());

    // Test delete inheritance
    role.deleteInheritance(role2);
    list = BusinessDAO.get(role.getOid()).getChildren(RoleDAO.ROLE_INHERITANCE);

    Assert.assertEquals(0, list.size());

    role.delete();
    role2.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testAddAscendant()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(SECRETARY, "Secretary");

    List<RelationshipDAOIF> list = role.getParents(RoleDAO.ROLE_INHERITANCE);

    Assert.assertEquals(1, list.size());
    Assert.assertEquals(role2.getOid(), list.get(0).getParentOid());

    RoleDAO role3 = role.addDescendant("runway.SuperMan", "SuperMan");

    list = role.getChildren(RoleDAO.ROLE_INHERITANCE);

    Assert.assertEquals(1, list.size());
    Assert.assertEquals(role3.getOid(), list.get(0).getChildOid());

    role.delete();
    role2.delete();
    role3.delete();
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testHierarchyRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    // Grant a list of aclass permissions to the role
    role.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    // Grant a list of relationship permissions to the parent role
    role2.grantPermission(Operation.WRITE, mdTree.getOid());

    // Get a list of all permissions of the child role
    Set<RelationshipDAOIF> set = role.getAllPermissions();

    List<String> list = new LinkedList<String>();

    for (RelationshipDAOIF rel : set)
    {
      list.add(rel.getChildOid());
    }

    Assert.assertEquals(list.size(), 3);
    Assert.assertTrue(list.contains(mdBusiness.getOid()));
    Assert.assertTrue(list.contains(mdAttributeCharacter.getOid()));
    Assert.assertTrue(list.contains(mdTree.getOid()));

    role.revokePermission(Operation.DELETE, mdBusiness.getOid());
    role.revokePermission(Operation.READ, mdAttributeCharacter.getOid());
    role2.revokePermission(Operation.WRITE, mdTree.getOid());

    role.delete();
    role2.delete();
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testHierarchyUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    role.assignMember(newUser);

    // Grant a list of attribute permissions to the role
    newUser.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role2.grantPermission(Operation.READ, mdAttributeCharacter.getOid());

    // Grant a list of attribute permissions to the role
    role2.grantPermission(Operation.WRITE, mdTree.getOid());

    Set<RelationshipDAOIF> set = newUser.getAllPermissions();

    List<String> list = new LinkedList<String>();

    for (RelationshipDAOIF rel : set)
    {
      list.add(rel.getChildOid());
    }

    Assert.assertEquals(list.size(), 3);
    Assert.assertTrue(list.contains(mdBusiness.getOid()));
    Assert.assertTrue(list.contains(mdAttributeCharacter.getOid()));
    Assert.assertTrue(list.contains(mdTree.getOid()));

    newUser.revokePermission(Operation.DELETE, mdBusiness.getOid());

    role.delete();
    role2.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testHierarchyOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    role.assignMember(newUser);

    // Grant a list of attribute permissions to the role
    newUser.grantPermission(Operation.DELETE, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role2.grantPermission(Operation.READ, mdBusiness.getOid());

    // Grant a list of attribute permissions to the role
    role2.grantPermission(Operation.WRITE, mdBusiness.getOid());

    Set<Operation> list = role.getAllPermissions(mdBusiness);

    Assert.assertEquals(2, list.size());
    Assert.assertTrue(list.contains(Operation.READ));
    Assert.assertTrue(list.contains(Operation.WRITE));

    list = newUser.getAllPermissions(mdBusiness);

    Assert.assertEquals(3, list.size());
    Assert.assertTrue(list.contains(Operation.READ));
    Assert.assertTrue(list.contains(Operation.WRITE));
    Assert.assertTrue(list.contains(Operation.DELETE));

    newUser.revokeAllPermissions(mdBusiness.getOid());

    newUser.revokePermission(Operation.DELETE, mdBusiness.getOid());

    role.delete();
    role2.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testCreateSSD()
  {
    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 3);

    BusinessDAOIF businessDAO = BusinessDAO.get(ssd.getOid());

    // Ensure that the ssd is created
    Assert.assertEquals(businessDAO.getValue(SDutyDAO.SDNAME), "Payroll");
    Assert.assertEquals(businessDAO.getValue(SDutyDAO.SDCARDINALITY), "3");

    ssd.delete();
  }

  @Request
  @Test
  public void testFindSSDSet()
  {
    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 3);

    // Test find SSDSet
    SDutyDAO ssd2 = SDutyDAO.findSSDSet("Payroll").getBusinessDAO();

    Assert.assertEquals(ssd.getOid(), ssd2.getOid());

    ssd.delete();
  }

  @Request
  @Test
  public void testInvlaidFindSSDSet()
  {
    try
    {
      SDutyDAO.findSSDSet("Payroll").getBusinessDAO();

      Assert.fail("Able to find an ssd set that does not exists");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testAddSSDRole()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = RoleDAO.createRole(SECRETARY, "Secretary");
    RoleDAO role3 = RoleDAO.createRole(WORKER, "Worker");
    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 1);

    String ssdName = ssd.getSSDSetName();
    Assert.assertEquals("Payroll", ssdName);

    ssd.setSSDSetName("Paygas");

    ssdName = ssd.getSSDSetName();
    Assert.assertEquals("Paygas", ssdName);

    role2.assignMember(newUser);
    role3.assignMember(newUser);

    // Add a new role to a seperation of duty set
    ssd.addSSDRoleMember(role2);

    List<RelationshipDAOIF> relationships = BusinessDAO.get(ssd.getOid()).getParents(SDutyDAO.SDCONFLICTINGROLES);

    Assert.assertEquals(relationships.size(), 1);
    Assert.assertEquals(role2.getOid(), relationships.get(0).getParentOid());

    // Add a non conflicting role
    ssd.addSSDRoleMember(role);

    relationships = BusinessDAO.get(ssd.getOid()).getParents(SDutyDAO.SDCONFLICTINGROLES);
    Assert.assertEquals(2, relationships.size());

    // Add a conflicting role
    try
    {
      ssd.addSSDRoleMember(role3);

      Assert.fail("Accepted a conflicting role");
    }
    catch (RBACExceptionInvalidSSDConstraint e)
    {
      // This is expected
    }

    relationships = BusinessDAO.get(ssd.getOid()).getParents(SDutyDAO.SDCONFLICTINGROLES);

    Assert.assertEquals(relationships.size(), 2);

    // Delete a role from the SSDSet
    ssd.deleteSSDRoleMember(role);

    relationships = BusinessDAO.get(ssd.getOid()).getParents(SDutyDAO.SDCONFLICTINGROLES);

    // Ensure the correct role is deleted
    Assert.assertEquals(1, relationships.size());
    Assert.assertEquals(role2.getOid(), relationships.get(0).getParentOid());

    ssd.delete();
    role.delete();
    role2.delete();
    role3.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testSSDCardinality()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = RoleDAO.createRole(SECRETARY, "Secretary");
    RoleDAO role3 = RoleDAO.createRole(WORKER, "Worker");
    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 3);

    ssd.addSSDRoleMember(role);
    ssd.addSSDRoleMember(role2);
    ssd.addSSDRoleMember(role3);

    role.assignMember(newUser);
    role2.assignMember(newUser);
    role3.assignMember(newUser);

    // Increase the cardinality to a larger value than the existing cardinality
    ssd.setSSDCardinality(5);
    ssd.apply();

    int card = ssd.getSSDSetCardinality();

    BusinessDAOIF businessDAO = BusinessDAO.get(ssd.getOid());

    Assert.assertEquals(businessDAO.getValue(SDutyDAO.SDCARDINALITY), "5");
    Assert.assertEquals(5, card);

    // Decrease the cardinality to a valid amount
    ssd.setSSDCardinality(4);
    ssd.apply();

    card = ssd.getSSDSetCardinality();
    Assert.assertEquals(4, card);

    // Decrease the cardinality to a number which invalidates existing values
    try
    {
      ssd.setSSDCardinality(2);
      ssd.apply();

      Assert.fail("An invalid cardinality was allowed");
    }
    catch (RBACExceptionInvalidSSDCardinality e)
    {
      // This is expected
    }

    // Ensure the cardinality is unchanged
    card = ssd.getSSDSetCardinality();
    Assert.assertEquals(4, card);

    ssd.delete();
    role.delete();
    role2.delete();
    role3.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testSSDAssignUser()
  {
    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 2);

    // Setup role hierarchy
    RoleDAO a = RoleDAO.createRole("runway.A", "A");
    RoleDAO b = a.addDescendant("runway.B", "B");
    RoleDAO c = a.addDescendant("runway.C", "C");
    RoleDAO d = b.addDescendant("runway.D", "D");
    RoleDAO e = b.addDescendant("runway.E", "E");
    RoleDAO f = c.addDescendant("runway.F", "F");
    RoleDAO g = c.addDescendant("runway.G", "G");
    RoleDAO h = c.addDescendant("runway.H", "H");

    // Setup collision sets
    ssd.addSSDRoleMember(c);
    ssd.addSSDRoleMember(d);
    ssd.addSSDRoleMember(e);

    // Assign user to roles
    b.assignMember(newUser);
    e.assignMember(newUser);
    h.assignMember(newUser);

    try
    {
      f.assignMember(newUser);

      Assert.assertTrue(false);
    }
    catch (RBACExceptionSingleActorConflictingRole ex)
    {
      // This is expected
    }

    // Delete role hierarchy
    a.delete();
    b.delete();
    c.delete();
    d.delete();
    e.delete();
    f.delete();
    g.delete();
    h.delete();

    ssd.delete();
  }

  /**
   *
   */
  @Request
  @Test
  public void testInvalidInheritance()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Maseter");
    RoleDAO role2 = RoleDAO.createRole(SECRETARY, "Secretary");
    RoleDAO role3 = RoleDAO.createRole(WORKER, "Worker");

    SDutyDAO ssd = SDutyDAO.createSSDSet("Payroll", 1);

    ssd.addSSDRoleMember(role);
    ssd.addSSDRoleMember(role3);

    role.assignMember(newUser);
    role2.assignMember(newUser);

    try
    {
      role3.addInheritance(role2);

      Assert.assertTrue(false);
    }
    catch (RBACExceptionInheritance e)
    {
      // This is expected
    }

    ssd.delete();
    role.delete();
    role2.delete();
    role3.delete();
  }

  @Request
  @Test
  public void testInvalidAttriubteOperation()
  {
    try
    {
      newUser.grantPermission(Operation.DELETE, mdAttributeCharacter.getOid());

      Assert.fail("able to grant an invalid operation to an mdAttribute");
    }
    catch (RBACExceptionInvalidOperation e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testInvalidMdRelationshipOperation()
  {
    try
    {
      newUser.grantPermission(Operation.EXECUTE, mdTree.getOid());

      Assert.fail("able to grant an invalid operation to an MdRelationship");
    }
    catch (RBACExceptionInvalidOperation e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testInvalidMdBusinessOperation()
  {
    try
    {
      newUser.grantPermission(Operation.PROMOTE, mdBusiness.getOid());

      Assert.fail("able to grant an invalid operation to an MdBusiness");
    }
    catch (RBACExceptionInvalidOperation e)
    {
      // This is expected
    }
  }

  // @Request @Test public void testEmptyTypeTuple()
  // {
  // try
  // {
  // PermissionTuple newTuple = PermissionTuple.newInstance();
  // newTuple.apply();
  //
  // Assert.fail("Able to create a TypeTuple with an empty MetaData attribute");
  // }
  // catch (ProblemException e)
  // {
  // ProblemException problemException = (ProblemException)e;
  //
  // boolean foundDisplayLabel = false;
  // boolean foundStateMaster = false;
  // boolean foundMdAttribute = false;
  //
  // for (ProblemIF problemIF : problemException.getProblems())
  // {
  // if (problemIF instanceof EmptyValueProblem)
  // {
  // EmptyValueProblem emptyValueProblem = (EmptyValueProblem)problemIF;
  // if
  // (emptyValueProblem.getAttributeName().equals(PermissionTupleIF.METADATA))
  // {
  // foundMdAttribute = true;
  // }
  // else if
  // (emptyValueProblem.getAttributeName().equals(PermissionTupleIF.STATE_MASTER))
  // {
  // foundStateMaster = true;
  // }
  // else if
  // (emptyValueProblem.getAttributeName().equals(PermissionTupleIF.DISPLAY_LABEL))
  // {
  // foundDisplayLabel = true;
  // }
  // }
  // }
  //
  // if (problemException.getProblems().size() == 3 &&
  // foundMdAttribute &&
  // foundStateMaster &&
  // foundDisplayLabel)
  // {
  // //Expected to land here
  // }
  // else
  // {
  // Assert.fail(EmptyValueProblem.class.getName()+" was not thrown.");
  // }
  // }
  // }

  // @Request @Test public void testEmptyDomainTuple()
  // {
  // try
  // {
  // DomainTuple newTuple = DomainTuple.newInstance();
  // newTuple.apply();
  //
  // Assert.fail("Able to create a DomainTuple with an empty Domain attribute");
  // }
  // catch (ProblemException e)
  // {
  // ProblemException problemException = (ProblemException)e;
  //
  // boolean foundDisplayLabel = false;
  // boolean foundDomain = false;
  //
  // for (ProblemIF problemIF : problemException.getProblems())
  // {
  // if (problemIF instanceof EmptyValueProblem)
  // {
  // EmptyValueProblem emptyValueProblem = (EmptyValueProblem)problemIF;
  // if (emptyValueProblem.getAttributeName().equals(DomainTupleIF.DOMAIN))
  // {
  // foundDomain = true;
  // }
  // else if
  // (emptyValueProblem.getAttributeName().equals(DomainTupleIF.DISPLAY_LABEL))
  // {
  // foundDisplayLabel = true;
  // }
  // }
  // }
  //
  // if (problemException.getProblems().size() == 3 &&
  // foundDomain &&
  // foundDisplayLabel)
  // {
  // //Expected to land here
  // }
  // else
  // {
  // Assert.fail(EmptyValueProblem.class.getName()+" was not thrown.");
  // }
  // }
  // }

  @Request
  @Test
  public void testOperationManager()
  {
    Operation o = OperationManager.getOperation(Operation.ADD_CHILD.getOid());
    Assert.assertEquals(Operation.ADD_CHILD, o);

    o = OperationManager.getOperation(Operation.ADD_PARENT.getOid());
    Assert.assertEquals(Operation.ADD_PARENT, o);

    o = OperationManager.getOperation(Operation.CREATE.getOid());
    Assert.assertEquals(Operation.CREATE, o);

    o = OperationManager.getOperation(Operation.DELETE.getOid());
    Assert.assertEquals(Operation.DELETE, o);

    o = OperationManager.getOperation(Operation.DELETE_CHILD.getOid());
    Assert.assertEquals(Operation.DELETE_CHILD, o);

    o = OperationManager.getOperation(Operation.DELETE_PARENT.getOid());
    Assert.assertEquals(Operation.DELETE_PARENT, o);

    o = OperationManager.getOperation(Operation.GRANT.getOid());
    Assert.assertEquals(Operation.GRANT, o);

    o = OperationManager.getOperation(Operation.PROMOTE.getOid());
    Assert.assertEquals(Operation.PROMOTE, o);

    o = OperationManager.getOperation(Operation.READ.getOid());
    Assert.assertEquals(Operation.READ, o);

    o = OperationManager.getOperation(Operation.REVOKE.getOid());
    Assert.assertEquals(Operation.REVOKE, o);

    o = OperationManager.getOperation(Operation.WRITE.getOid());
    Assert.assertEquals(Operation.WRITE, o);
  }

  @Request
  @Test
  public void testInvalidOperation()
  {
    try
    {
      OperationManager.getOperation("invalid");

      Assert.fail("Able to load an invalid operation");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testCreateUser()
  {
    UserDAO user = UserDAO.newInstance();
    user.setPassword("secure_password");
    user.setUsername("trashCan");
    user.setSessionLimit("10");
    user.apply();

    Assert.assertEquals("trashCan", user.getSingleActorName());
    Assert.assertTrue(user.compareToPassword("secure_password"));
    Assert.assertFalse(user.compareToPassword("invalid"));
    Assert.assertEquals(10, user.getSessionLimit());
    Assert.assertFalse(user.isAdministrator());

    user.delete();
  }

  @Request
  @Test
  public void testFindUser()
  {
    UserDAO user = UserDAO.findUser(newUser.getSingleActorName()).getBusinessDAO();

    Assert.assertEquals(newUser.getOid(), user.getOid());
  }

  @Request
  @Test
  public void testInvalidFindUser()
  {
    try
    {
      UserDAO.findUser("invalid_name");

      Assert.fail("Able to search a user with an invalid username");
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Test assigning permissions to a class
   */
  @Request
  @Test
  public void testUtilClassPermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    // Create a list of operations
    List<Operation> operations = new LinkedList<Operation>();
    operations.add(Operation.WRITE);
    operations.add(Operation.READ);

    try
    {
      // Grant individual permissions to the role
      role.grantPermission(Operation.CREATE, mdUtil.getOid());
      role.grantPermission(Operation.WRITE, mdUtil.getOid());

      // Grant a list of permissions to the role
      role.grantPermission(operations, mdUtil.getOid());

      // Get the list of operations for a permission
      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdUtil.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations is correct
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.CREATE));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      // Grant individual permission to a user
      newUser.grantPermission(Operation.READ, mdUtil.getOid());

      relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdUtil.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations for a user is correctly set
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      relationshipDAO.getRelationshipDAO().delete();
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.CREATE, mdUtil.getOid());
      role.revokePermission(Operation.WRITE, mdUtil.getOid());

      role.revokeAllPermissions(mdUtil.getOid());

      newUser.revokePermission(Operation.READ, mdUtil.getOid());

      role.delete();
    }
  }

  /**
   * Test assigning permissions to an attribute
   */
  @Request
  @Test
  public void testUtilAttributePermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);

      // Grant individual attribute permissions to the role
      role.grantPermission(Operation.GRANT, mdAttributeCharacter_Util.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(operations, mdAttributeCharacter_Util.getOid());

      // Get the list of operations for a permission
      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdAttributeCharacter_Util.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations is correct
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.GRANT));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));

      // Grant individual permission to a user
      newUser.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdAttributeCharacter_Util.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations for a user is correctly set
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      relationshipDAO.getRelationshipDAO().delete();
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.GRANT, mdAttributeCharacter_Util.getOid());
      role.revokeAllPermissions(mdAttributeCharacter_Util.getOid());

      newUser.revokePermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      role.delete();
    }
  }

  /**
   * Test removing permissions from a metaData type
   */
  @Request
  @Test
  public void testUtilRevokePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.DELETE);
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);

      // Grant a list of attribute permissions to the role
      role.grantPermission(operations, mdUtil.getOid());

      // Remove permissions
      role.revokeAllPermissions(mdUtil.getOid());

      // Get the list of operations for a permission
      List<RelationshipDAOIF> list = role.getChildren(RelationshipTypes.TYPE_PERMISSION.getType());

      // Ensure the list is empty
      Assert.assertEquals(list.size(), 0);

      // Regrant permisisons
      role.grantPermission(operations, mdUtil.getOid());

      // Remove the promote pemission
      role.revokePermission(Operation.DELETE, mdUtil.getOid());

      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdUtil.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);
      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure the list of permissions granted does not include PROMOTE
      Assert.assertFalse(OperationManager.containsOperation(enumeration.dereference(), Operation.DELETE));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testUtilRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      // Grant a list of class permissions to the role
      role.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      Set<RelationshipDAOIF> set = role.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdUtil.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_Util.getOid()));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.DELETE, mdUtil.getOid());
      role.revokePermission(Operation.READ, mdAttributeCharacter_Util.getOid());
      role.delete();
    }
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testUtilUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(newUser);

      // Grant a list of class permissions to the role
      newUser.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      Set<RelationshipDAOIF> set = newUser.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdUtil.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_Util.getOid()));

      newUser.revokeAllPermissions(mdUtil.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdAttributeCharacter_Util.getOid());
      newUser.revokePermission(Operation.DELETE, mdUtil.getOid());

      role.delete();
    }
  }

  /**
   * test method permissions
   */
  @Request
  @Test
  public void testUtilMethodPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(methodActor_Util);

      // Grant a list of class permissions to the role
      methodActor_Util.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      Set<RelationshipDAOIF> set = methodActor_Util.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(mdUtil.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_Util.getOid()));

      methodActor_Util.revokeAllPermissions(mdUtil.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdAttributeCharacter_Util.getOid());
      role.delete();
    }
  }

  @Request
  @Test
  public void testUtilOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(newUser);

      // Grant a list of class permissions to the role
      newUser.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdUtil.getOid());

      // Grant a list of relationship permissions to the role
      role.grantPermission(Operation.WRITE, mdUtil.getOid());

      Set<Operation> list = role.getAllPermissions(mdUtil);

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdUtil);

      Assert.assertEquals(list.size(), 3);
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));
      Assert.assertTrue(list.contains(Operation.DELETE));

      newUser.revokeAllPermissions(mdUtil.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdUtil.getOid());
      role.revokePermission(Operation.WRITE, mdUtil.getOid());

      newUser.revokePermission(Operation.DELETE, mdUtil.getOid());

      role.delete();
    }
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testUtilHierarchyRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      // Grant a list of aclass permissions to the role
      role.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      // Get a list of all permissions of the child role
      Set<RelationshipDAOIF> set = role.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertTrue(list.contains(mdUtil.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_Util.getOid()));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.DELETE, mdUtil.getOid());
      role.revokePermission(Operation.READ, mdAttributeCharacter_Util.getOid());
      role.delete();
      role2.delete();
    }
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testUtilHierarchyUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.READ, mdAttributeCharacter_Util.getOid());

      Set<RelationshipDAOIF> set = newUser.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdUtil.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_Util.getOid()));

    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.DELETE, mdUtil.getOid());

      role.delete();
      role2.delete();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testUtilHierarchyOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.DELETE, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.READ, mdUtil.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.WRITE, mdUtil.getOid());

      Set<Operation> list = role.getAllPermissions(mdUtil);

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdUtil);

      Assert.assertEquals(3, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));
      Assert.assertTrue(list.contains(Operation.DELETE));

      newUser.revokeAllPermissions(mdUtil.getOid());

    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.DELETE, mdUtil.getOid());

      role.delete();
      role2.delete();
    }
  }

  /**
   * Test assigning permissions to a class
   */
  @Request
  @Test
  public void testViewClassPermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);

      // Grant individual permissions to the role
      role.grantPermission(Operation.CREATE, mdView.getOid());
      role.grantPermission(Operation.WRITE, mdView.getOid());

      // Grant a list of permissions to the role
      role.grantPermission(operations, mdView.getOid());

      // Get the list of operations for a permission
      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdView.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations is correct
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.CREATE));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      // Grant individual permission to a user
      newUser.grantPermission(Operation.READ, mdView.getOid());

      relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdView.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations for a user is correctly set
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      relationshipDAO.getRelationshipDAO().delete();
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.CREATE, mdView.getOid());
      role.revokePermission(Operation.WRITE, mdView.getOid());
      role.revokeAllPermissions(mdView.getOid());

      newUser.revokePermission(Operation.READ, mdView.getOid());

      role.delete();
    }
  }

  /**
   * Test assigning permissions to an attribute
   */
  @Request
  @Test
  public void testSessionAttributePermission()
  {
    // Create a new role
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);

      // Grant individual attribute permissions to the role
      role.grantPermission(Operation.GRANT, mdAttributeCharacter_View.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(operations, mdAttributeCharacter_View.getOid());

      // Get the list of operations for a permission
      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdAttributeCharacter_View.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations is correct
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.GRANT));
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.WRITE));

      // Grant individual permission to a user
      newUser.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      relationshipDAO = RelationshipDAO.get(newUser.getOid(), mdAttributeCharacter_View.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);

      enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure that the list of operations for a user is correctly set
      Assert.assertTrue(OperationManager.containsOperation(enumeration.dereference(), Operation.READ));

      relationshipDAO.getRelationshipDAO().delete();
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.GRANT, mdAttributeCharacter_View.getOid());
      role.revokeAllPermissions(mdAttributeCharacter_View.getOid());

      newUser.revokePermission(Operation.READ, mdAttributeCharacter_View.getOid());
      role.delete();
    }
  }

  /**
   * Test removing permissions from a metaData type
   */
  @Request
  @Test
  public void testSessionRevokePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      // Create a list of operations
      List<Operation> operations = new LinkedList<Operation>();
      operations.add(Operation.DELETE);
      operations.add(Operation.WRITE);
      operations.add(Operation.READ);

      // Grant a list of attribute permissions to the role
      role.grantPermission(operations, mdView.getOid());

      // Remove permissions
      role.revokeAllPermissions(mdView.getOid());

      // Get the list of operations for a permission
      List<RelationshipDAOIF> list = role.getChildren(RelationshipTypes.TYPE_PERMISSION.getType());

      // Ensure the list is empty
      Assert.assertEquals(list.size(), 0);

      // Regrant permissions
      role.grantPermission(operations, mdView.getOid());

      // Remove the promote permission
      role.revokePermission(Operation.DELETE, mdView.getOid());

      RelationshipDAOIF relationshipDAO = RelationshipDAO.get(role.getOid(), mdView.getOid(), RelationshipTypes.TYPE_PERMISSION.getType()).get(0);
      AttributeEnumerationIF enumeration = (AttributeEnumerationIF) relationshipDAO.getAttributeIF(ActorDAO.OPERATION_ATTR);

      // Ensure the list of permissions granted does not include PROMOTE
      Assert.assertFalse(OperationManager.containsOperation(enumeration.dereference(), Operation.DELETE));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testSessionRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      // Grant a list of class permissions to the role
      role.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      Set<RelationshipDAOIF> set = role.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdView.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_View.getOid()));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.DELETE, mdView.getOid());
      role.revokePermission(Operation.READ, mdAttributeCharacter_View.getOid());
      role.delete();
    }
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testSessionUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(newUser);

      // Grant a list of class permissions to the role
      newUser.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      Set<RelationshipDAOIF> set = newUser.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdView.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_View.getOid()));

      newUser.revokeAllPermissions(mdView.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdAttributeCharacter_View.getOid());

      newUser.revokePermission(Operation.DELETE, mdView.getOid());
      role.delete();
    }
  }

  /**
   * test method permissions
   */
  @Request
  @Test
  public void testSessionMethodPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(methodActor_View);

      // Grant a list of class permissions to the role
      methodActor_View.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      Set<RelationshipDAOIF> set = methodActor_View.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(mdView.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_View.getOid()));

      methodActor_View.revokeAllPermissions(mdView.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdAttributeCharacter_View.getOid());

      role.delete();
    }
  }

  @Request
  @Test
  public void testSessionOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    try
    {
      role.assignMember(newUser);

      // Grant a list of class permissions to the role
      newUser.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdView.getOid());

      // Grant a list of relationship permissions to the role
      role.grantPermission(Operation.WRITE, mdView.getOid());

      Set<Operation> list = role.getAllPermissions(mdView);

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdView);

      Assert.assertEquals(list.size(), 3);
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));
      Assert.assertTrue(list.contains(Operation.DELETE));

      newUser.revokeAllPermissions(mdView.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.READ, mdView.getOid());
      role.revokePermission(Operation.WRITE, mdView.getOid());
      newUser.revokePermission(Operation.DELETE, mdView.getOid());
      role.delete();
    }
  }

  /**
   * test role permissions
   */
  @Request
  @Test
  public void testSessionHierarchyRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      // Grant a list of aclass permissions to the role
      role.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      // Get a list of all permissions of the child role
      Set<RelationshipDAOIF> set = role.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertTrue(list.contains(mdView.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_View.getOid()));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.revokePermission(Operation.DELETE, mdView.getOid());
      role.revokePermission(Operation.READ, mdAttributeCharacter_View.getOid());
      role.delete();
      role2.delete();
    }
  }

  /**
   * test user permissions
   */
  @Request
  @Test
  public void testSessionHierarchyUserPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.READ, mdAttributeCharacter_View.getOid());

      Set<RelationshipDAOIF> set = newUser.getAllPermissions();

      List<String> list = new LinkedList<String>();

      for (RelationshipDAOIF rel : set)
      {
        list.add(rel.getChildOid());
      }

      Assert.assertEquals(list.size(), 2);
      Assert.assertTrue(list.contains(mdView.getOid()));
      Assert.assertTrue(list.contains(mdAttributeCharacter_View.getOid()));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.DELETE, mdView.getOid());
      role.delete();
      role2.delete();
    }
  }

  /**
   *
   */
  @Request
  @Test
  public void testSessionHierarchyOperationOnObject()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");
    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.DELETE, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.READ, mdView.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.WRITE, mdView.getOid());

      Set<Operation> list = role.getAllPermissions(mdView);

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdView);

      Assert.assertEquals(3, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));
      Assert.assertTrue(list.contains(Operation.DELETE));

      newUser.revokeAllPermissions(mdView.getOid());

    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.DELETE, mdView.getOid());
      role.delete();
      role2.delete();
    }
  }

  @Request
  @Test
  public void testMdAttributeDimension()
  {
    org.junit.Assume.assumeFalse("true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")));
    
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.READ, mdAttributeDimension.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.WRITE, mdAttributeDimension.getOid());

      Set<Operation> list = role.getAllPermissions(mdAttributeDimension);

      Assert.assertEquals(1, list.size());
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdAttributeDimension);

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      newUser.revokeAllPermissions(mdAttributeDimension.getOid());

    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.READ, mdAttributeDimension.getOid());
      role.delete();
      role2.delete();
    }
  }

  @Request
  @Test
  public void testMdClassDimension()
  {
    org.junit.Assume.assumeFalse("true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")));
    
    MdClassDimensionDAOIF mdClassDimension = mdBusiness.getMdClassDimension(mdDimension);

    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.READ, mdClassDimension.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.WRITE, mdClassDimension.getOid());

      Set<Operation> list = role.getAllPermissions(mdClassDimension);

      Assert.assertEquals(1, list.size());
      Assert.assertTrue(list.contains(Operation.WRITE));

      list = newUser.getAllPermissions(mdClassDimension);

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(Operation.READ));
      Assert.assertTrue(list.contains(Operation.WRITE));

      newUser.revokeAllPermissions(mdClassDimension.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.READ, mdClassDimension.getOid());
      role.delete();
      role2.delete();
    }
  }

  @Request
  @Test
  public void testReadAndWriteAllPermissions()
  {
    org.junit.Assume.assumeFalse("true".equals(System.getenv("RUNWAY_TEST_IGNORE_DIMENSION_TESTS")));
    
    MdClassDimensionDAOIF mdClassDimension = mdBusiness.getMdClassDimension(mdDimension);

    RoleDAO role = RoleDAO.createRole(MASTER, "Master");
    RoleDAO role2 = role.addAscendant(EXECUTIVE, "Executive");

    try
    {
      role.assignMember(newUser);

      // Grant a list of attribute permissions to the role
      newUser.grantPermission(Operation.READ_ALL, mdClassDimension.getOid());

      // Grant a list of attribute permissions to the role
      role2.grantPermission(Operation.WRITE_ALL, mdClassDimension.getOid());

      Set<Operation> list = role.getAllPermissions(mdClassDimension);

      Assert.assertEquals(1, list.size());
      Assert.assertTrue(list.contains(Operation.WRITE_ALL));

      list = newUser.getAllPermissions(mdClassDimension);

      Assert.assertEquals(2, list.size());
      Assert.assertTrue(list.contains(Operation.READ_ALL));
      Assert.assertTrue(list.contains(Operation.WRITE_ALL));

      newUser.revokeAllPermissions(mdClassDimension.getOid());
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      newUser.revokePermission(Operation.READ_ALL, mdClassDimension.getOid());
      role.delete();
      role2.delete();
    }

  }

  @Request
  @Test
  public void testDenyPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DENY_WRITE, mdBusiness.getOid());
      role.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());
      role.grantPermission(Operation.DENY_READ, mdBusiness.getOid());
      role.grantPermission(Operation.DENY_DELETE, mdBusiness.getOid());

      Set<Operation> permissions = role.getAllPermissions(mdBusiness);

      Assert.assertEquals(4, permissions.size());
      Assert.assertTrue(permissions.contains(Operation.DENY_WRITE));
      Assert.assertTrue(permissions.contains(Operation.DENY_READ));
      Assert.assertTrue(permissions.contains(Operation.DENY_DELETE));
      Assert.assertTrue(permissions.contains(Operation.DENY_CREATE));
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testOverwritePermissions()
  {
    RoleDAO superRole = TestFixtureFactory.createRole1();
    superRole.apply();

    try
    {
      RoleDAO subRole = TestFixtureFactory.createRole2();
      subRole.apply();

      try
      {
        subRole.addAscendant(superRole);

        superRole.grantPermission(Operation.DENY_WRITE, mdBusiness.getOid());
        superRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());
        superRole.grantPermission(Operation.DENY_DELETE, mdBusiness.getOid());
        superRole.grantPermission(Operation.DENY_READ, mdBusiness.getOid());

        subRole.grantPermission(Operation.WRITE, mdBusiness.getOid());
        subRole.grantPermission(Operation.CREATE, mdBusiness.getOid());
        subRole.grantPermission(Operation.DELETE, mdBusiness.getOid());
        subRole.grantPermission(Operation.READ, mdBusiness.getOid());

        Set<Operation> permissions = subRole.getOperations().get(mdBusiness.getOid());

        Assert.assertEquals(4, permissions.size());
        Assert.assertTrue(permissions.contains(Operation.WRITE));
        Assert.assertTrue(permissions.contains(Operation.READ));
        Assert.assertTrue(permissions.contains(Operation.DELETE));
        Assert.assertTrue(permissions.contains(Operation.CREATE));
      }
      finally
      {
        subRole.delete();
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      superRole.delete();
    }
  }

  @Request
  @Test
  public void testNegatingWritePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DENY_WRITE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.WRITE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReverseNegatingWritePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.WRITE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.DENY_WRITE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testNegatingReadPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DENY_READ, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.READ, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReverseNegatingReadPermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.READ, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.DENY_READ, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testNegatingDeletePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DENY_DELETE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.DELETE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReverseNegatingDeletePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DELETE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.DENY_DELETE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testNegatingCreatePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.CREATE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReverseNegatingCreatePermissions()
  {
    RoleDAO role = RoleDAO.createRole(MASTER, "Master");

    try
    {
      role.grantPermission(Operation.CREATE, mdBusiness.getOid());

      try
      {
        role.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());

        Assert.fail("Able to assign negating permissions");
      }
      catch (RBACExceptionInvalidOperation e)
      {
        // this is expected
      }
    }
    catch (Exception t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      role.delete();
    }
  }
}
