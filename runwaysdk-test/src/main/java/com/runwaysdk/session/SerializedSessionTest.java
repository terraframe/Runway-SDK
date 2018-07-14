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

import java.io.File;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.util.FileIO;

public class SerializedSessionTest
{
  /**
   * The test user object
   */
  private static UserDAO                 newUser;

  /**
   * The test user object
   */
  private static UserDAO                 inactiveUser;

  /**
   * The test mdBusiness data object
   */
  private static MdBusinessDAO           mdBusiness;

  /**
   * The test mdBusiness2 data object
   */
  private static MdBusinessDAO           mdBusiness2;

  /**
   * The test MdMethod object
   */
  private static MdMethodDAO             mdMethod;

  /**
   * The test mdRelationshiop
   */
  private static MdRelationshipDAO       mdRelationship;

  /**
   * The test mdAttribute of the MdBusiness
   */
  private static MdAttributeConcreteDAO  mdAttribute;

  /**
   * The test businessDAO of the MdBusiness
   */
  private static BusinessDAO             businessDAO;

  /**
   * The test businessDAO2 of the MdBusiness
   */
  private static BusinessDAO             businessDAO2;

  /**
   * The test state1-mdAttribute type tuple
   */
  private static TypeTupleDAO            typeTuple1;

  /**
   * The test state1 of the MdBusiness StateMachine
   */
  private static StateMasterDAO          state1;

  /**
   * The test state2 of the MdBusiness StateMachine
   */
  private static StateMasterDAO          state2;

  /**
   * The test state3 of the MdBusiness StateMachine
   */
  private static StateMasterDAO          state3;

  private static Business                business1;

  @SuppressWarnings("unused")
  private static Business                business2;

  private static MdViewDAO               mdView;

  private static MdDomainDAO             mdDomain;

  private static MdDimensionDAO          mdDimension;

  private static MdAttributeCharacterDAO mdAttributeCharacter;

  /**
   * The username for the user
   */
  private final static String            username       = "smethie";

  /**
   * The password for the user
   */
  private final static String            password       = "1234";

  /**
   * The maximum number of sessions for the user
   */
  private final static int               sessionLimit   = 2;

  /**
   * The amount of time in seconds for the test expiration time of a session
   */
  private final static long              newSessionTime = 10;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    Session.setSessionTime(newSessionTime);

    // Create a new user
    newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, username);
    newUser.setValue(UserInfo.PASSWORD, password);
    newUser.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    newUser.apply();

    inactiveUser = UserDAO.newInstance();
    inactiveUser.setValue(UserInfo.USERNAME, "InactiveUser");
    inactiveUser.setValue(UserInfo.PASSWORD, password);
    inactiveUser.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    inactiveUser.setValue(UserInfo.INACTIVE, MdAttributeBooleanInfo.TRUE);
    inactiveUser.apply();

    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    // Create a new MdBusiness
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

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
    mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness2, mdBusiness2);
    mdRelationship.apply();

    mdView = TestFixtureFactory.createMdView1();
    mdView.apply();

    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdView);
    mdAttributeCharacter.apply();

    mdDomain = TestFixtureFactory.createMdDomain();
    mdDomain.apply();

    // Create a businessDAO of MdBusiness
    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.getAttribute(ElementInfo.OWNER).setValue(newUser.getId());
    businessDAO.getAttribute(ElementInfo.DOMAIN).setValue(mdDomain.getId());
    businessDAO.apply();

    businessDAO2 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO2.getAttribute(ElementInfo.OWNER).setValue(newUser.getId());
    businessDAO2.getAttribute(ElementInfo.DOMAIN).setValue(mdDomain.getId());
    businessDAO2.apply();

    business1 = Business.getBusiness(businessDAO.getId());
    business2 = Business.getBusiness(businessDAO2.getId());

    typeTuple1 = TestFixtureFactory.createTypeTuple(state1, mdAttribute);
    typeTuple1.apply();

    mdMethod = TestFixtureFactory.createMdMethod(mdBusiness);
    mdMethod.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    TestFixtureFactory.delete(typeTuple1);
    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdBusiness2);
    TestFixtureFactory.delete(mdView);
    TestFixtureFactory.delete(mdDomain);
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(inactiveUser);
    TestFixtureFactory.delete(newUser);

    Session.setSessionTime(CommonProperties.getSessionTime());
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
    Set<RelationshipDAOIF> set = newUser.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser
      newUser.revokeAllPermissions(reference.getChildId());
    }

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    set = role.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any businessDAO permissions given to newUser
      role.revokeAllPermissions(reference.getChildId());
    }

    UserDAOIF publicUser = UserDAO.getPublicUser();

    set = publicUser.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any businessDAO permissions given to newUser
      role.revokeAllPermissions(reference.getChildId());
    }

    // Clear any lingering sessions
    SessionFacade.clearSessions();

    FileIO.deleteDirectory(new File(LocalProperties.getPermissionCacheDirectory()));
  }

  /**
   * Test session access
   */
  @Request
  @Test
  public void testCheckAccess()
  {
    // Grant permissions to the user on the md class
    newUser.grantPermission(Operation.DELETE, mdBusiness.getId());

    new PermissionBuilder(newUser).serialize();

    // Create a session for the user
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    Assert.assertEquals(true, SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
    Assert.assertEquals(false, SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));
  }

  /**
   * Test PUBLIC role permission
   */
  @Request
  @Test
  public void testPublicPermissions()
  {
    RoleDAO publicRole = RoleDAO.findRole(RoleDAO.PUBLIC_ROLE).getBusinessDAO();

    publicRole.grantPermission(Operation.READ, mdBusiness.getId());

    new PermissionBuilder(publicRole).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

      // Check that the public permissions are still granted
      Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));

      SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      // Check that the public permissions are still granted
      Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
    }
    finally
    {
      publicRole.revokeAllPermissions(mdBusiness.getId());
    }
  }

  /**
   * Test the Owner permissions
   */
  @Request
  @Test
  public void testOwnerPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();

    // Grant permissions ot the owner role
    role.grantPermission(Operation.CREATE, mdBusiness.getId());

    new PermissionBuilder(role).serialize();

    try
    {
      // Ensure the owner was correctly set
      Assert.assertEquals(newUser.getId(), businessDAO.getValue(ElementInfo.OWNER));

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      // Ensure owner permission were granted
      Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
      Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));
    }
    finally
    {
      role.revokeAllPermissions(mdBusiness.getId());
    }
  }

  /**
   * Test the permissions based upon the current state of an object
   */
  @Request
  @Test
  public void testStatePermissions()
  {
    // Add permissions to the owner role for the entry state
    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.READ, state1.getId());
    newUser.grantPermission(Operation.PROMOTE, state1.getId());

    new PermissionBuilder(role).serialize();
    new PermissionBuilder(newUser).serialize();

    try
    {

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      // Ensure that user state permissions where executed
      Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
      // Ensure that owner state permissions work
      Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.PROMOTE, business1));
      // Ensure extra permissions are not given
      Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));
    }
    finally
    {
      newUser.revokeAllPermissions(state1.getId());
      role.revokeAllPermissions(state1.getId());
    }
  }

  @Request
  @Test
  public void testStateAttributePermissions()
  {
    newUser.grantPermission(Operation.READ, typeTuple1.getId());

    new PermissionBuilder(newUser).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute));
      Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute));
    }
    finally
    {
      newUser.revokeAllPermissions(typeTuple1.getId());
    }
  }

  @Request
  @Test
  public void testAttributePermissions()
  {
    newUser.grantPermission(Operation.READ, mdAttribute.getId());

    new PermissionBuilder(newUser).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
      Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
    }
    finally
    {
      newUser.revokeAllPermissions(mdAttribute.getId());
    }

  }

  @Request
  @Test
  public void testOwnerAttributePermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.READ, mdAttribute.getId());

    new PermissionBuilder(role).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute));
      Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute));
    }
    finally
    {
      role.revokeAllPermissions(mdAttribute.getId());
    }

  }

  @Request
  @Test
  public void testOwnerStateAttributePermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.READ, typeTuple1.getId());

    new PermissionBuilder(role).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute));
      Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute));
    }
    finally
    {
      role.revokeAllPermissions(typeTuple1.getId());
    }

  }

  @Request
  @Test
  public void testRelationshipPermission()
  {
    newUser.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());

    new PermissionBuilder(newUser).serialize();

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertFalse(SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId()));
      Assert.assertTrue(SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId()));
    }
    finally
    {
      newUser.revokeAllPermissions(mdRelationship.getId());
    }
  }
}
