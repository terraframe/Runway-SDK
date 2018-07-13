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

import java.util.HashSet;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.Mutable;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.SingleActorDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.rbac.UserDAOIF;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.MdDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.DomainTupleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdDomainDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;

public class SessionTest
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
  protected void tearDown() throws Exception
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
  }

  /**
   * Test the creation of a new session
   */
  @Request
  @Test
  public void testCreateSession()
  {
    // Check for invalid user-password errors
    try
    {
      SessionFacade.logIn(username, "invalid", new Locale[] { CommonProperties.getDefaultLocale() });
      Assert.fail("An invalid login was accepted.");
    }
    catch (InvalidLoginException e)
    {
      // This is expected
    }

    try
    {
      SessionFacade.logIn("baldh", password, new Locale[] { CommonProperties.getDefaultLocale() });
      Assert.fail("An invalid login was accepted.");
    }
    catch (InvalidLoginException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testSessionDimension()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SessionFacade.setDimension(mdDimension.getKey(), sessionId);

    SessionIF session = SessionFacade.getSession(sessionId);

    MdDimensionDAOIF dimension = session.getDimension();

    Assert.assertEquals(mdDimension.getId(), dimension.getId());
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

    // Create a session for the user
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    Assert.assertEquals(true, SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
    Assert.assertEquals(false, SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));
  }

  /**
   * Test the automatic clean up of expired sessions
   */
  @Request
  @Test
  public void testCleanUp()
  {
    // Create a new session
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    // Convert the session time(sec) to miliseconds and
    // add enough time to ensure that the a session will expire
    long sessionTime = ( Session.getSessionTime() + 10 ) * 1000;
    long waitTime = System.currentTimeMillis() + sessionTime;

    // Get the public session
    SessionIF publicSession = SessionFacade.getPublicSession();
    Assert.assertNotNull(publicSession);

    // Ensure the session is active
    Assert.assertEquals(true, SessionFacade.containsSession(sessionId));

    // Wait until the session is expired
    while (System.currentTimeMillis() < waitTime)
      ;

    // Ensure that the public session has not been cleaned up
    Assert.assertTrue(SessionFacade.containsSession(publicSession.getId()));
    Assert.assertEquals(publicSession.getId(), SessionFacade.getPublicSession().getId());

    try
    {
      SessionFacade.getSession(sessionId);

      Assert.fail("Expired sessions was not cleaned up");
    }
    catch (InvalidSessionException e)
    {
      // This is expected
    }
  }

  /**
   * Test closing a session
   */
  @Request
  @Test
  public void testCloseSession()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SessionFacade.closeSession(sessionId);

    // Ensure that the session has been cleaned up
    Assert.assertFalse(SessionFacade.containsSession(sessionId));

    // Ensure that closing the public session does nothing
    SessionIF publicSession = SessionFacade.getPublicSession();

    SessionFacade.closeSession(publicSession.getId());
    Assert.assertTrue(SessionFacade.containsSession(publicSession.getId()));
    Assert.assertEquals(publicSession.getId(), SessionFacade.getPublicSession().getId());
  }

  /**
   * Test renewing an existing session
   */
  @Request
  @Test
  public void testRenewSession()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    long waitTime = System.currentTimeMillis() + ( Session.getSessionTime() / 2 ) * 1000;

    while (System.currentTimeMillis() < waitTime)
      ;

    // Check that the session has yet to expire
    boolean access = false;

    try
    {
      access = SessionFacade.checkAccess(sessionId, Operation.WRITE, business1);
    }
    catch (InvalidSessionException e)
    {
      // Fails if the session has already expired
      Assert.fail("The session expired prematurely");
    }

    // Renew session 1 before it expires
    SessionFacade.renewSession(sessionId);

    waitTime = System.currentTimeMillis() + ( Session.getSessionTime() / 2 + 2 ) * 1000;

    // Wait until after the session would have expired if it was not renewed
    while (System.currentTimeMillis() < waitTime)
      ;

    // Check that the session 1 still exists, aka it was renewed
    try
    {
      access = SessionFacade.checkAccess(sessionId, Operation.WRITE, business1);
    }
    catch (InvalidSessionException e)
    {
      // Fails if checkAccess throws an exception
      Assert.fail("Session failed to renew");
    }

    Assert.assertEquals(false, access);
  }

  /**
   * Test renewing an expired session
   */
  @Request
  @Test
  public void testRenewExpired()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SessionFacade.closeSession(sessionId);

    // Check that renewing an expired session does not work
    try
    {
      SessionFacade.renewSession(sessionId);

      // Fails if renewSession works on an expired session
      Assert.fail("Failed to throw an exception when renewing an expired session");
    }
    catch (InvalidSessionException e)
    {
      // This is expected
    }
  }

  /**
   * Test clearing out all sessions
   */
  @Request
  @Test
  public void testClearSessions()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    // Get the public session
    SessionIF publicSession = SessionFacade.getPublicSession();
    Assert.assertNotNull(publicSession);

    SessionFacade.clearSessions();

    // Ensure that the public session has not been cleared from the cache
    Assert.assertTrue(SessionFacade.containsSession(publicSession.getId()));
    Assert.assertEquals(publicSession.getId(), SessionFacade.getPublicSession().getId());

    try
    {
      // Ensure that the session has been cleaned up
      SessionFacade.checkAccess(sessionId, Operation.WRITE, business1);

      // Fails if checkAccess does not throw a exception
      Assert.fail("Failed to throw expired sessions exception");
    }
    catch (InvalidSessionException e)
    {
      // This is expected
    }
  }

  /**
   * Test iterating over sessions
   */
  @Request
  @Test
  public void testIterateSessions()
  {
    Set<String> sessions = new HashSet<String>();

    // 1. Delete all existing sessions
    SessionFacade.clearSessions();

    // 2. Ensure that the iterator returns no sessions.
    SessionIterator it = SessionFacade.getIterator();
    Assert.assertFalse(it.hasNext());
    boolean didFail = false;
    try
    {
      it.next();
    }
    catch (NoSuchElementException e)
    {
      didFail = true;
    }
    Assert.assertTrue(didFail);

    // 3. Create the allotted number of sessions
    for (int i = 0; i < sessionLimit; i++)
    {
      try
      {
        sessions.add(SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() }));
      }
      catch (InvalidSessionException e)
      {
        Assert.fail("A valid login failed.");
      }
    }

    // 4. Ensure that the iterator returns the correct sessions.
    int count = 0;
    it = SessionFacade.getIterator();
    while (it.hasNext())
    {
      SessionIF session = it.next();
      Assert.assertNotNull(session);
      Assert.assertTrue(sessions.contains(session.getId()));
      count++;
    }
    Assert.assertEquals(sessionLimit, count);
  }

  /**
   * Test session limits
   */
  @Request
  @Test
  public void testSessionLimit()
  {
    // Create the allotted number of sessions
    for (int i = 0; i < sessionLimit; i++)
    {
      try
      {
        SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
      }
      catch (InvalidSessionException e)
      {
        Assert.fail("A valid login failed.");
      }
    }

    SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    // Create an extra session
    try
    {
      SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
      Assert.fail("Too many sessions login pass.");
    }
    catch (MaximumSessionsException e)
    {
      // This is expected
    }
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
   * Test the admin permissions of the session layer
   */
  @Request
  @Test
  public void testAdminPermissions()
  {
    Assert.assertFalse(newUser.isAdministrator());

    // Add the newUser to the administrator role
    RoleDAO adminRole = RoleDAO.findRole(RoleDAO.ADMIN_ROLE).getBusinessDAO();
    adminRole.assignMember(newUser);

    Assert.assertTrue(newUser.isAdministrator());

    // login the newUser
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    // Check that all permissions are granted
    Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.PROMOTE, business1));
    Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business2));
    Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute));
    Assert.assertTrue(SessionFacade.checkPromoteAccess(sessionId, business1, "transition1"));
    Assert.assertTrue(SessionFacade.checkRelationshipAccess(sessionId, Operation.DELETE_PARENT, business1, mdRelationship.getId()));
    adminRole.deassignMember(newUser);
    SessionFacade.closeSession(sessionId);
  }

  /**
   * Test the getUser method of the session facade
   */
  @Request
  @Test
  public void testGetUser()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    UserDAOIF publicUser = UserDAO.getPublicUser();

    Assert.assertEquals(publicUser, SessionFacade.getUser(sessionId));

    sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    Assert.assertEquals(newUser, SessionFacade.getUser(sessionId));
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

  @Request
  @Test
  public void testRelationshipPermissionWithInvalidParent()
  {
    Mutable mutable = BusinessFacade.newRelationship("0", "0", mdRelationship.definesType());

    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mutable, mdRelationship.definesAttribute("id")));
    }
    finally
    {
      SessionFacade.closeSession(sessionId);
    }
  }

  @Request
  @Test
  public void testOwnerRelationshipPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId()));
      Assert.assertFalse(SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId()));
    }
    finally
    {
      role.revokeAllPermissions(mdRelationship.getId());
    }

  }

  @Request
  @Test
  public void testPromotePermissions()
  {
    newUser.grantPermission(Operation.PROMOTE, state2.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertTrue(SessionFacade.checkPromoteAccess(sessionId, business1, "transition1"));
      Assert.assertFalse(SessionFacade.checkPromoteAccess(sessionId, business1, "transition2"));
    }
    finally
    {
      newUser.revokeAllPermissions(state2.getId());
    }
  }

  @Request
  @Test
  public void testExecutePermissions()
  {
    newUser.grantPermission(Operation.EXECUTE, mdMethod.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertEquals(true, SessionFacade.checkMethodAccess(sessionId, Operation.EXECUTE, business1, mdMethod));
    }
    finally
    {
      newUser.revokeAllPermissions(mdMethod.getId());
    }
  }

  @Request
  @Test
  public void testNoExecutePermissions()
  {
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    Assert.assertEquals(false, SessionFacade.checkMethodAccess(sessionId, Operation.EXECUTE, business1, mdMethod));
  }

  @Request
  @Test
  public void testOwnerExecutePermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.EXECUTE, mdMethod.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.assertEquals(true, SessionFacade.checkMethodAccess(sessionId, Operation.EXECUTE, business1, mdMethod));
    }
    finally
    {
      role.revokeAllPermissions(mdMethod.getId());
    }

  }

  @Request
  @Test
  public void testNoUser()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SingleActorDAOIF user = SessionFacade.getUser(sessionId);

    UserDAOIF publicUser = UserDAO.getPublicUser();

    Assert.assertEquals(publicUser.getId(), user.getId());
  }

  @Request
  @Test
  public void testForceCleanUp()
  {
    String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SessionCache cache = SessionFacade.getSessionCache();

    Session session = cache.getSession(sessionId);
    session.expire();

    cache.addSession(session);

    SessionFacade.cleanUp();

    Assert.assertFalse(SessionFacade.containsSession(sessionId));
    Assert.assertFalse(SessionFacade.containsSession(null));
  }

  /**
   * Ensure public permissions are not overwritten when an user logs in
   */
  @Request
  @Test
  public void testLoadPermissions()
  {
    UserDAO publicUser = UserDAO.findUser(UserDAO.PUBLIC_USER_NAME).getBusinessDAO();

    publicUser.grantPermission(Operation.READ, mdBusiness.getId());

    try
    {
      PermissionCache.instance();

      newUser.grantPermission(Operation.WRITE, mdBusiness.getId());

      try
      {
        String sessionId = SessionFacade.logIn(UserInfo.PUBLIC_USER_NAME, ServerConstants.PUBLIC_USER_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

        Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
      }
      finally
      {
        newUser.revokeAllPermissions(mdBusiness.getId());
      }

    }
    finally
    {
      publicUser.revokeAllPermissions(mdBusiness.getId());
      PermissionCache.instance();
    }

  }

  @Request
  @Test
  public void testSessionCountCleanUp()
  {
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionCache cache = SessionFacade.getSessionCache();

    // Ensure that the user has been logged in
    Assert.assertEquals(true, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(1, cache.getUserSessionCount(newUser.getId()));

    SessionFacade.closeSession(sessionId);

    Assert.assertEquals(false, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(0, cache.getUserSessionCount(newUser.getId()));
  }

  /**
   * Test the behavoir of logging in different users to the same session
   */
  @Request
  @Test
  public void testSameSessionMultiLogin()
  {
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionCache cache = SessionFacade.getSessionCache();

    // Ensure that the user has been logged in
    Assert.assertEquals(true, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(1, cache.getUserSessionCount(newUser.getId()));

    SessionFacade.changeLogin(sessionId, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);

    Assert.assertEquals(false, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(0, cache.getUserSessionCount(newUser.getId()));

    Assert.assertEquals(true, cache.isLoggedIn(ServerConstants.SYSTEM_USER_ID));
    Assert.assertEquals(1, cache.getUserSessionCount(ServerConstants.SYSTEM_USER_ID));

    SessionFacade.closeSession(sessionId);
  }

  @Request
  @Test
  public void testSetUser()
  {
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionCache cache = SessionFacade.getSessionCache();
    UserDAOIF admin = UserDAO.get(ServerConstants.SYSTEM_USER_ID);

    // Ensure that the user has been logged in
    Assert.assertEquals(true, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(1, cache.getUserSessionCount(newUser.getId()));

    SessionFacade.setUser(sessionId, admin.getId());

    Assert.assertEquals(false, cache.isLoggedIn(newUser.getId()));
    Assert.assertEquals(0, cache.getUserSessionCount(newUser.getId()));

    Assert.assertEquals(true, cache.isLoggedIn(ServerConstants.SYSTEM_USER_ID));
    Assert.assertEquals(1, cache.getUserSessionCount(ServerConstants.SYSTEM_USER_ID));

    SessionFacade.closeSession(sessionId);
  }

  /**
   * Ensure an inactive user cannot log in
   */
  @Request
  @Test
  public void testInactiveLogin()
  {
    try
    {
      SessionFacade.logIn(inactiveUser.getUsername(), password, new Locale[] { CommonProperties.getDefaultLocale() });

      Assert.fail("Able to log in with an inactive session");
    }
    catch (InactiveUserException e)
    {
      // Expected
    }
  }

  /**
   * Ensure an inactive user cannot log in
   */
  @Request
  @Test
  public void testPublicSessionLogin()
  {
    SessionIF publicSession = SessionFacade.getPublicSession();
    try
    {
      SessionFacade.changeLogin(publicSession.getId(), username, password);

      Assert.fail("Able to log in with an inactive session");
    }
    catch (InvalidLoginException e)
    {
      // Expected
    }
  }

  @Request
  @Test
  public void testCloseOnRequest()
  {
    String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionIF session = SessionFacade.getSession(sessionId);

    Assert.assertEquals(false, session.closeOnEndOfRequest());

    SessionFacade.setCloseOnEndOfRequest(sessionId, true);

    session = SessionFacade.getSession(sessionId);

    Assert.assertEquals(true, session.closeOnEndOfRequest());

    SessionFacade.closeSession(sessionId);
  }

  @Request
  @Test
  public void testMutable()
  {
    Mutable mutable = BusinessFacade.newSessionComponent(mdView.definesType());
    mutable.setValue(mdAttributeCharacter.definesAttribute(), "I am a test");

    String sessionId = SessionFacade.logIn(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    SessionFacade.put(sessionId, mutable);
    Session session = (Session) SessionFacade.getSession(sessionId);

    Mutable ret = session.get(mutable.getId());

    Assert.assertEquals(mutable.getValue(mdAttributeCharacter.definesAttribute()), ret.getValue(mdAttributeCharacter.definesAttribute()));

    SessionFacade.closeSession(sessionId);
  }

  @Request
  @Test
  public void testAttributeDimensionPermission()
  {
    MdAttributeDimensionDAOIF mdAttributeDimension = mdAttributeCharacter.getMdAttributeDimension(mdDimension);

    newUser.grantPermission(Operation.READ, mdAttributeDimension.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      // Set the dimension of the session
      SessionFacade.setDimension(mdDimension.getKey(), sessionId);

      try
      {
        Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttributeCharacter));
        Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttributeCharacter));
      }
      finally
      {
        SessionFacade.closeSession(sessionId);
      }
    }
    finally
    {
      newUser.revokePermission(Operation.READ, mdAttributeDimension.getId());
    }
  }

  @Request
  @Test
  public void testClassDimensionPermission()
  {
    MdClassDimensionDAOIF mdClassDimension = mdDimension.getMdClassDimension(mdBusiness);

    newUser.grantPermission(Operation.READ, mdClassDimension.getId());

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      // Set the dimension of the session
      SessionFacade.setDimension(mdDimension.getKey(), sessionId);

      try
      {
        Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
        Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));
      }
      finally
      {
        SessionFacade.closeSession(sessionId);
      }
    }
    finally
    {
      newUser.revokePermission(Operation.READ, mdClassDimension.getId());
    }
  }

  @Request
  @Test
  public void testDomainPermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdBusiness.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean checkAccess = false;
    boolean checkAccess2 = false;

    try
    {
      newUser.grantPermission(Operation.DELETE, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      checkAccess = SessionFacade.checkAccess(sessionId, Operation.DELETE, business1);
      checkAccess2 = SessionFacade.checkAccess(sessionId, Operation.CREATE, business1);

      SessionFacade.closeSession(sessionId);
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(checkAccess);
    Assert.assertFalse(checkAccess2);
  }

  @Request
  @Test
  public void testDomainStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean checkAccess = false;
    boolean checkAccess2 = false;

    try
    {
      newUser.grantPermission(Operation.DELETE, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      checkAccess = SessionFacade.checkAccess(sessionId, Operation.DELETE, business1);
      checkAccess2 = SessionFacade.checkAccess(sessionId, Operation.CREATE, business1);
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(checkAccess);
    Assert.assertFalse(checkAccess2);
  }

  @Request
  @Test
  public void testDomainAttributePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdAttribute.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean granted = false;
    boolean denied = false;

    try
    {
      newUser.grantPermission(Operation.READ, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute);
      denied = SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute);
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testDomainAttributeStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdAttribute.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean granted = false;
    boolean denied = false;

    try
    {
      newUser.grantPermission(Operation.READ, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute);
      denied = SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute);
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testDomainPromotePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state2.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean granted = false;
    boolean denied = false;

    try
    {
      newUser.grantPermission(Operation.PROMOTE, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkPromoteAccess(sessionId, business1, "transition1");
      denied = SessionFacade.checkPromoteAccess(sessionId, business1, "transition2");
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testDomainRelationshipPermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdRelationship.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean granted = false;
    boolean denied = false;

    try
    {
      newUser.grantPermission(Operation.ADD_PARENT, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId());
      denied = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId());
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testDomainRelationshipStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdRelationship.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    boolean granted = false;
    boolean denied = false;

    try
    {
      newUser.grantPermission(Operation.ADD_PARENT, tuple.getId());

      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId());
      denied = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId());
    }
    finally
    {
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testOwnerDomainPermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdBusiness.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.DELETE, tuple.getId());

    boolean checkAccess = false;
    boolean checkAccess2 = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      checkAccess = SessionFacade.checkAccess(sessionId, Operation.DELETE, business1);
      checkAccess2 = SessionFacade.checkAccess(sessionId, Operation.CREATE, business1);
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(checkAccess);
    Assert.assertFalse(checkAccess2);
  }

  @Request
  @Test
  public void testOwnerDomainStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.DELETE, tuple.getId());

    boolean checkAccess = false;
    boolean checkAccess2 = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      checkAccess = SessionFacade.checkAccess(sessionId, Operation.DELETE, business1);
      checkAccess2 = SessionFacade.checkAccess(sessionId, Operation.CREATE, business1);
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(checkAccess);
    Assert.assertFalse(checkAccess2);
  }

  @Request
  @Test
  public void testOwnerDomainAttributePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdAttribute.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.READ, tuple.getId());

    boolean granted = false;
    boolean denied = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute);
      denied = SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute);
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testOwnerDomainAttributeStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdAttribute.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.READ, tuple.getId());

    boolean granted = false;
    boolean denied = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkAttributeAccess(sessionId, Operation.READ, business1, mdAttribute);
      denied = SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, business1, mdAttribute);
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testOwnerDomainPromotePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state2.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.PROMOTE, tuple.getId());

    boolean granted = false;
    boolean denied = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkPromoteAccess(sessionId, business1, "transition1");
      denied = SessionFacade.checkPromoteAccess(sessionId, business1, "transition2");
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testOwnerDomainRelationshipPermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdRelationship.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.ADD_PARENT, tuple.getId());

    boolean granted = false;
    boolean denied = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId());
      denied = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId());
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testOwnerDomainRelationshipStatePermission()
  {
    DomainTupleDAO tuple = DomainTupleDAO.newInstance();
    tuple.setValue(DomainTupleDAO.DOMAIN, mdDomain.getId());
    tuple.setValue(DomainTupleDAO.METADATA, mdRelationship.getId());
    tuple.setValue(DomainTupleDAO.STATE_MASTER, state1.getId());
    tuple.setStructValue(DomainTupleDAO.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testTuple");
    tuple.apply();

    RoleDAO role = RoleDAO.findRole(RoleDAO.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.ADD_PARENT, tuple.getId());

    boolean granted = false;
    boolean denied = false;

    try
    {
      String sessionId = SessionFacade.logIn(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

      granted = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_PARENT, business1, mdRelationship.getId());
      denied = SessionFacade.checkRelationshipAccess(sessionId, Operation.ADD_CHILD, business1, mdRelationship.getId());
    }
    finally
    {
      role.revokeAllPermissions(tuple.getId());
      tuple.delete();
    }

    Assert.assertTrue(granted);
    Assert.assertFalse(denied);
  }

  @Request
  @Test
  public void testDenyPermission()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    try
    {
      UserDAO user = TestFixtureFactory.createUser();
      user.apply();

      try
      {
        role.assignMember(user);

        role.grantPermission(Operation.DENY_WRITE, mdBusiness.getId());
        role.grantPermission(Operation.DENY_CREATE, mdBusiness.getId());
        role.grantPermission(Operation.DENY_DELETE, mdBusiness.getId());
        role.grantPermission(Operation.DENY_READ, mdBusiness.getId());

        role.grantPermission(Operation.DENY_WRITE, mdAttribute.getId());
        role.grantPermission(Operation.DENY_READ, mdAttribute.getId());

        String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));

          Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
          Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
        }
        finally
        {
          SessionFacade.closeSession(sessionId);
        }
      }
      finally
      {
        user.delete();
      }

    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReadAndWriteAllPermission()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    try
    {
      UserDAO user = TestFixtureFactory.createUser();
      user.apply();

      try
      {
        role.assignMember(user);

        role.grantPermission(Operation.READ_ALL, mdBusiness.getId());
        role.grantPermission(Operation.WRITE_ALL, mdBusiness.getId());

        String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));

          Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
          Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
        }
        finally
        {
          SessionFacade.closeSession(sessionId);
        }
      }
      finally
      {
        user.delete();
      }

    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testReadAllPrecedence()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    try
    {
      UserDAO user = TestFixtureFactory.createUser();
      user.apply();

      try
      {
        role.assignMember(user);

        role.grantPermission(Operation.READ_ALL, mdBusiness.getId());
        role.grantPermission(Operation.WRITE_ALL, mdBusiness.getId());
        role.grantPermission(Operation.DENY_READ, mdAttribute.getId());

        String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
          Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));

          Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
          Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
        }
        finally
        {
          SessionFacade.closeSession(sessionId);
        }
      }
      finally
      {
        user.delete();
      }

    }
    finally
    {
      role.delete();
    }
  }

  @Request
  @Test
  public void testSibilingRolePermissionPrecedence()
  {
    RoleDAO role = TestFixtureFactory.createRole1();
    role.apply();

    try
    {
      RoleDAO role2 = TestFixtureFactory.createRole2();
      role2.apply();

      try
      {

        UserDAO user = TestFixtureFactory.createUser();
        user.apply();

        try
        {
          role.assignMember(user);
          role2.assignMember(user);

          role.grantPermission(Operation.READ, mdAttribute.getId());
          role.grantPermission(Operation.DENY_WRITE, mdAttribute.getId());
          role2.grantPermission(Operation.DENY_READ, mdAttribute.getId());
          role2.grantPermission(Operation.WRITE, mdAttribute.getId());

          String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

          try
          {
            Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
            Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
          }
          finally
          {
            SessionFacade.closeSession(sessionId);
          }
        }
        finally
        {
          user.delete();
        }

      }
      finally
      {
        role2.delete();
      }

    }
    finally
    {
      role.delete();
    }
  }

  /**
   * Test allowing an inherited permission lower in the inheritance chain
   */
  @Request
  @Test
  public void testAllowRoleInheritance()
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

        UserDAO user = TestFixtureFactory.createUser();
        user.apply();

        try
        {
          subRole.assignMember(user);

          superRole.grantPermission(Operation.DENY_WRITE, mdBusiness.getId());
          superRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getId());
          superRole.grantPermission(Operation.DENY_DELETE, mdBusiness.getId());
          superRole.grantPermission(Operation.DENY_READ, mdBusiness.getId());

          superRole.grantPermission(Operation.DENY_WRITE, mdAttribute.getId());
          superRole.grantPermission(Operation.DENY_READ, mdAttribute.getId());

          subRole.grantPermission(Operation.WRITE, mdBusiness.getId());
          subRole.grantPermission(Operation.CREATE, mdBusiness.getId());
          subRole.grantPermission(Operation.DELETE, mdBusiness.getId());
          subRole.grantPermission(Operation.READ, mdBusiness.getId());

          subRole.grantPermission(Operation.WRITE, mdAttribute.getId());
          subRole.grantPermission(Operation.READ, mdAttribute.getId());

          String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

          try
          {
            Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
            Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
            Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
            Assert.assertTrue(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));

            Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
            Assert.assertTrue(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
          }
          finally
          {
            SessionFacade.closeSession(sessionId);
          }
        }
        finally
        {
          user.delete();
        }
      }
      finally
      {
        subRole.delete();
      }
    }
    finally
    {
      superRole.delete();
    }
  }

  /**
   * Test denying an inherited permission lower in the inheritance chain
   */
  @Request
  @Test
  public void testDenyRoleInheritance()
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

        UserDAO user = TestFixtureFactory.createUser();
        user.apply();

        try
        {
          subRole.assignMember(user);

          superRole.grantPermission(Operation.WRITE, mdBusiness.getId());
          superRole.grantPermission(Operation.CREATE, mdBusiness.getId());
          superRole.grantPermission(Operation.DELETE, mdBusiness.getId());
          superRole.grantPermission(Operation.READ, mdBusiness.getId());

          superRole.grantPermission(Operation.WRITE, mdAttribute.getId());
          superRole.grantPermission(Operation.READ, mdAttribute.getId());

          subRole.grantPermission(Operation.DENY_WRITE, mdBusiness.getId());
          subRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getId());
          subRole.grantPermission(Operation.DENY_DELETE, mdBusiness.getId());
          subRole.grantPermission(Operation.DENY_READ, mdBusiness.getId());

          subRole.grantPermission(Operation.DENY_WRITE, mdAttribute.getId());
          subRole.grantPermission(Operation.DENY_READ, mdAttribute.getId());

          String sessionId = SessionFacade.logIn(TestFixConst.TEST_USER, TestFixConst.TEST_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

          try
          {
            Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.CREATE, business1));
            Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.DELETE, business1));
            Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.READ, business1));
            Assert.assertFalse(SessionFacade.checkAccess(sessionId, Operation.WRITE, business1));

            Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.WRITE, mdAttribute));
            Assert.assertFalse(SessionFacade.checkAttributeAccess(sessionId, Operation.READ, mdAttribute));
          }
          finally
          {
            SessionFacade.closeSession(sessionId);
          }
        }
        finally
        {
          user.delete();
        }
      }
      finally
      {
        subRole.delete();
      }
    }
    finally
    {
      superRole.delete();
    }
  }
}
