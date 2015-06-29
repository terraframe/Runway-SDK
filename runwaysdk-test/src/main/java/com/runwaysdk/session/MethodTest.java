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
package com.runwaysdk.session;

import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

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

public class MethodTest extends TestCase
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
   * A suite() takes <b>this </b> <code>AttributeTest.class</code> and wraps it
   * in <code>MasterTestSetup</code>. The returned class is a suite of all the
   * tests in <code>AttributeTest</code>, with the global setUp() and tearDown()
   * methods from <code>MasterTestSetup</code>.
   * 
   * @return A suite of tests wrapped in global setUp and tearDown methods
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTestSuite(MethodTest.class);

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
   * The setup done before the test suite is run
   */
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
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception
  {
  }

  /**
   * Delete all MetaData objects which were created in the class
   * 
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
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
  public void testCheckAccess()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    // Check various access of the user
    boolean access = MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1);
    boolean no_access = MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);

    assertEquals(true, access);
    assertEquals(false, no_access);
  }

  /**
   * Test closing a session
   */
  public void testCloseSession()
  {
    MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1);
    assertEquals(true, MethodCache.getMethodCache().contains(mdMethod));

    int size = PermissionObserver.size();
    MethodFacade.closeMethod(mdMethod);

    assertEquals(false, MethodCache.getMethodCache().contains(mdMethod));
    assertEquals(size - 1, PermissionObserver.size());
  }

  /**
   * Test clearing out all sessions
   */
  public void testClearSessions()
  {
    MethodFacade.clear();

    assertEquals(0, MethodCache.getMethodCache().size());
    assertEquals(0, PermissionObserver.size());
  }

  /**
   * Test the permissions based upon the current state of an object
   */
  public void testStatePermissions()
  {
    methodActor.grantPermission(Operation.READ, state1.getId());
    methodActor.grantPermission(Operation.PROMOTE, state1.getId());

    // Ensure that user state permissions where executed
    assertTrue(MethodFacade.checkAccess(mdMethod, Operation.READ, business1));
    assertTrue(MethodFacade.checkAccess(mdMethod, Operation.PROMOTE, business1));

    // Ensure extra permissions are not given
    assertFalse(MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
  }

  public void testStateAttributePermissions()
  {
    methodActor.grantPermission(Operation.READ, typeTuple1.getId());

    assertTrue(MethodFacade.checkAttributeAccess(mdMethod, Operation.READ, business1, mdAttribute));
    assertFalse(MethodFacade.checkAttributeAccess(mdMethod, Operation.WRITE, business1, mdAttribute));
  }

  public void testRelationshipPermission()
  {
    methodActor.grantPermission(Operation.ADD_CHILD, mdTree.getId());

    assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
    assertTrue(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_CHILD, business1, mdTree.getId()));
  }

  public void testPromotePermissions()
  {
    methodActor.grantPermission(Operation.PROMOTE, state2.getId());

    assertTrue(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition1"));
    assertFalse(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition2"));
  }

  /**
   * Ensure that changes to permissions are automatically updated to the
   * MethodCache
   */
  public void testReloadPermissions()
  {
    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

    assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
    assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

    // Grant permissions to the user on the md class
    methodActor.grantPermission(Operation.WRITE, mdBusiness.getId());

    assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
  }

  /**
   * Ensure that changes to role permissions are automatically updated to the
   * MethodCache
   */
  public void testReloadRolePermissions()
  {
    RoleDAO role = RoleDAO.createRole("runway.Master", "Master");

    try
    {
      // Grant permissions to the user on the md class
      role.assignMember(methodActor);

      methodActor.grantPermission(Operation.DELETE, mdBusiness.getId());

      assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.DELETE, business1));
      assertEquals(false, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));

      // Grant permissions to the user on the md class
      role.grantPermission(Operation.WRITE, mdBusiness.getId());

      assertEquals(true, MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
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
  public void testNoPermissions()
  {
    assertFalse(MethodFacade.checkAccess(mdMethod, Operation.WRITE, business1));
    assertFalse(MethodFacade.checkAttributeAccess(mdMethod, Operation.WRITE, business1, mdAttribute));
    assertFalse(MethodFacade.checkPromoteAccess(mdMethod, business1, "transition2"));
    assertFalse(MethodFacade.checkRelationshipAccess(mdMethod, Operation.ADD_PARENT, business1, mdTree.getId()));
  }

  public void testExecutePermissions()
  {
    methodActor.grantPermission(Operation.EXECUTE, mdMethod2.getId());

    assertEquals(true, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

  public void testNoExecutePermissions()
  {
    assertEquals(false, MethodFacade.checkExecuteAccess(mdMethod, mdMethod2));
  }

}
