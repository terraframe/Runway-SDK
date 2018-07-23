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
package com.runwaysdk.session;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;

@RunWith(ClasspathTestRunner.class)
public class MethodTest
{
  /**
   * The test assignable method object
   */
  private static MethodActorDAO         methodActor;

  /**
   * The test mdMethod object
   */
  private static MdMethodDAO            mdMethod;

  /**
   * The test MdMethod object
   */
  private static MdMethodDAO            mdMethod2;

  /**
   * The test mdBusiness data object
   */
  private static MdBusinessDAO          mdBusiness;

  /**
   * The test mdBusiness2 data object
   */
  private static MdBusinessDAO          mdBusiness2;

  /**
   * The test mdRelationshiop
   */
  private static MdRelationshipDAO      mdTree;

  /**
   * The test mdAttribute of the MdBusiness
   */
  private static MdAttributeConcreteDAO mdAttribute;

  /**
   * The test businessDAO of the MdBusiness
   */
  private static BusinessDAO            businessDAO;

  /**
   * The test state1-mdAttribute type tuple
   */
  private static TypeTupleDAO           typeTuple1;

  /**
   * The test state1 of the MdBusiness StateMachine
   */
  private static StateMasterDAO         state1;

  /**
   * The test state2 of the MdBusiness StateMachine
   */
  private static StateMasterDAO         state2;

  /**
   * The test state3 of the MdBusiness StateMachine
   */
  private static StateMasterDAO         state3;

  private static Business               business1;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    // Create a new MdBusiness
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();

    // Create a new user
    methodActor = TestFixtureFactory.createMethodActor(mdMethod);
    methodActor.apply();

    // Create an MdAttribute on the MdBusiness
    mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttribute.apply();

    // Create a StateMachine for the MdBusiness
    MdStateMachineDAO mdState = TestFixtureFactory.createMdStateMachine(mdBusiness);
    mdState.apply();

    // Add states to the state machine
    state1 = mdState.addState("State1", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state1.apply();

    state2 = mdState.addState("State2", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state2.apply();

    state3 = mdState.addState("State3", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state3.apply();

    // Add transitions between states
    RelationshipDAO transition1 = mdState.addTransition("transition1", state1.getId(), state2.getId());
    transition1.apply();

    RelationshipDAO transition2 = mdState.addTransition("transition2", state2.getId(), state3.getId());
    transition2.apply();

    RelationshipDAO transition3 = mdState.addTransition("transition3", state3.getId(), state1.getId());
    transition3.apply();

    // Create a new MdBusiness
    mdBusiness2 = TestFixtureFactory.createMdBusiness2();
    mdBusiness2.apply();

    // Create a new relationship
    mdTree = TestFixtureFactory.createMdTree(mdBusiness2, mdBusiness2);
    mdTree.apply();

    // Create a businessDAO of MdBusiness
    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.apply();

    business1 = Business.getBusiness(businessDAO.getId());

    typeTuple1 = TestFixtureFactory.createTypeTuple(state1, mdAttribute);
    typeTuple1.apply();

    mdMethod2 = TestFixtureFactory.createMdMethod(mdBusiness, "checkout2");
    mdMethod2.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(typeTuple1);
    TestFixtureFactory.delete(mdTree);
    TestFixtureFactory.delete(mdMethod);
    TestFixtureFactory.delete(mdMethod2);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
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
    Set<RelationshipDAOIF> set = methodActor.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser
      methodActor.revokeAllPermissions(reference.getChildId());
    }

    // Clear any lingering sessions
    MethodFacade.clear();
  }

  /**
   * Test session access
   */
  @Request
  @Test
  public void testCheckAccess()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    // Check various access of the user
    boolean access = MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1);
    boolean no_access = MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);

    Assert.assertEquals(true, access);
    Assert.assertEquals(false, no_access);
  }

  /**
   * Test closing a session
   */
  @Request
  @Test
  public void testCloseSession()
  {
    MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);
    Assert.assertEquals(true, MethodCache.getMethodCache().contains(mdMethod));

    int size = PermissionObserver.size();
    MethodFacade.closeMethod(mdMethod);

    Assert.assertEquals(false, MethodCache.getMethodCache().contains(mdMethod));
    Assert.assertEquals(size - 1, PermissionObserver.size());
  }

  /**
   * Test clearing out all sessions
   */
  @Request
  @Test
  public void testClearSessions()
  {
    MethodFacade.clear();

    Assert.assertEquals(0, MethodCache.getMethodCache().size());
    Assert.assertEquals(0, PermissionObserver.size());
  }

  /**
   * Test the permissions based upon the current state of an object
   */
  @Request
  @Test
  public void testStatePermissions()
  {
    methodActor.grantPermission(Operation.READ, state1.getId());
    methodActor.grantPermission(Operation.PROMOTE, state1.getId());

    // Ensure that user state permissions where executed
    Assert.assertTrue(MethodFacade.checkAccess(mdMethod, Operation.READ, business1));
    Assert.assertTrue(MethodFacade.checkAccess(mdMethod, Operation.PROMOTE, business1));

    // Ensure extra permissions are not given
    Assert.assertFalse(MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
  }

  @Request
  @Test
  public void testStateAttributePermissions()
  {
    methodActor.grantPermission(Operation.READ, typeTuple1.getId());

    Assert.assertTrue(MethodFacade.checkAttributeAccess(mdMethod, Operation.READ, business1, mdAttribute));
    Assert.assertFalse(MethodFacade.checkAttributeAccess(mdMethod, Operation.WRITE, business1, mdAttribute));
  }

  @Request
  @Test
  public void testRelationshipPermission()
  {
    methodActor.grantPermission(Operation.ADD_CHILD, mdTree.getId());

    Assert.assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
    Assert.assertTrue(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_CHILD, business1, mdTree.getId()));
  }

  @Request
  @Test
  public void testPromotePermissions()
  {
    methodActor.grantPermission(Operation.PROMOTE, state2.getId());

    Assert.assertTrue(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition1"));
    Assert.assertFalse(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition2"));
  }

  /**
   * Ensure that changes to permissions are automatically updated to the
   * MethodCache
   */
  @Request
  @Test
  public void testReloadPermissions()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
    Assert.assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getId());

    Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
  }

  /**
   * Ensure that changes to role permissions are automatically updated to the
   * MethodCache
   */
  @Request
  @Test
  public void testReloadRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole("runway.Master", "Master");

    try
    {
      // Grant permissions to the user on the md class
      role.assignMember(methodActor);

      methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

      Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
      Assert.assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

      // Grant permissions to the user on the md class
      role.grantPermission(Operation.WRITE, mdBusiness.getId());

      Assert.assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
    }
    catch (Throwable t)
    {
      throw new RuntimeException(t);
    }
    finally
    {
      role.delete();
    }
  }

  /**
   * Ensure that the MethodCache works when not permissions are given exist
   */
  @Request
  @Test
  public void testNoPermissions()
  {
    Assert.assertFalse(MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
    Assert.assertFalse(MethodFacade.checkAttributeAccess(mdMethod, Operation.WRITE, business1, mdAttribute));
    Assert.assertFalse(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition2"));
    Assert.assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
  }

  @Request
  @Test
  public void testExecutePermissions()
  {
    methodActor.grantPermission(Operation.EXECUTE, mdMethod2.getId());

    Assert.assertEquals(true, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

  @Request
  @Test
  public void testNoExecutePermissions()
  {
    Assert.assertEquals(false, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

}
