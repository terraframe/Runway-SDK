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
package com.runwaysdk.session;

import java.util.Locale;
import java.util.Set;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import com.runwaysdk.ClientException;
import com.runwaysdk.RunwayExceptionDTO;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessFacade;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.LockException;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.Struct;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.facade.Facade;

public class IntegratedSessionTest extends TestCase
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
   * The test user object
   */
  private static UserDAO                   newUser1;

  /**
   * The test user object
   */
  private static UserDAO                   newUser2;

  private static MdDimensionDAO            mdDimension;

  /**
   * The test MdBusiness data object
   */
  private static MdBusinessDAO             mdBusiness;

  /**
   * Test mdBusiness data object which extends from mdBusiness
   */
  private static MdBusinessDAO             mdBusinessChild;

  /**
   * The test MdBusiness that extends enumeration master.
   */
  private static MdBusinessDAO             enumMasterMdBusiness;

  /**
   * The test MdEnumeration data object.
   */
  private static MdEnumerationDAO          structMdEnumeration;

  /**
   * The test MdStruct for structs.
   */
  private static MdStructDAO               mdStruct;

  /**
   * The test MdRelationship.
   */
  private static MdRelationshipDAO         mdRelationship;

  /**
   * The test MdAttribute of the MdRelationship.
   */
  private static MdAttributeConcreteDAO    relMdAttribute;

  /**
   * The test mdAttribute of the MdBusiness.
   */
  private static MdAttributeConcreteDAO    mdAttribute;

  /**
   * The test MdAttribute Struct of MdBusiness.
   */
  private static MdAttributeConcreteDAO    mdAttributeStruct;

  /**
   * Enumeration attribute on the struct.
   */
  private static MdAttributeEnumerationDAO mdAttributeEnumeration;

  /**
   * The test mdAttribute of mdStruct.
   */
  private static MdAttributeConcreteDAO    mdAttributeCharacter;

  /**
   * The test mdAttributeBlob of mdStruct.
   */
  private static MdAttributeBlobDAO        mdAttributeBlob;

  /**
   * A test relationship of the mdTree.
   */
  private static RelationshipDAO           relationshipDAO;

  /**
   * The test BusinessDAO of the MdBusiness.
   */
  private static BusinessDAO               businessDAO;

  /**
   * The second test BusinessDAO of the MdBusiness.
   */
  private static BusinessDAO               businessDAO2;

  /**
   * The third test BusinessDAO of the MdBusiness. Stays in the first state.
   */
  private static BusinessDAO               businessDAO3;

  /**
   * The fourth test BusinessDAO of mdBusinessChild.
   */
  private static BusinessDAO               businessDAO4;

  /**
   * The fourth test BusinessDAO of mdBusinessChild.
   */
  private static BusinessDAO               businessDAO5;

  /**
   * The first BusinessDAO to delete.
   */
  private static BusinessDAO               deleteObject1;

  /**
   * The second BusinessDAO to delete.
   */
  private static BusinessDAO               deleteObject2;

  /**
   * The third BusinessDAO to delete.
   */
  private static BusinessDAO               deleteObject3;

  /**
   * The fourth BusinessDAO to delete.
   */
  private static BusinessDAO               deleteObject4;

  private static StructDAO                 structDAO;

  /**
   * The test state-mdAttribute tuple.
   */
  private static TypeTupleDAO              typeTuple1;

  /**
   * The test state-mdRelationship tuple
   */
  private static TypeTupleDAO              typeTuple2;

  /**
   * The test state1 of the MdBusiness StateMachine.
   */
  private static StateMasterDAO            state1;

  /**
   * The test state2 of the MdBusiness StateMachine.
   */
  private static StateMasterDAO            state2;

  /**
   * The test state3 of the MdBusiness StateMachine.
   */
  private static StateMasterDAO            state3;

  /**
   * The username for the user .
   */
  private final static String              username1    = "smethie";

  /**
   * The password for the user.
   */
  private final static String              password1    = "1234";

  /**
   * The username for the user .
   */
  private final static String              username2    = "jdoe";

  /**
   * The password for the user.
   */
  private final static String              password2    = "9876";

  /**
   * The maximum number of sessions for the user.
   */
  private final static int                 sessionLimit = 2;

  /**
   * The ID of an enumeration Item.
   */
  private static String                    enumItemId1;

  /**
   * The ID of an enumeration Item.
   */
  private static String                    enumItemId2;

  /**
   * The ID of an enumeration Item.
   */
  private static String                    enumItemId3;

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

    suite.addTestSuite(IntegratedSessionTest.class);

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
    // Create a new user
    newUser1 = UserDAO.newInstance();
    newUser1.setValue(UserInfo.USERNAME, username1);
    newUser1.setValue(UserInfo.PASSWORD, password1);
    newUser1.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    newUser1.apply();

    // Create a new user
    newUser2 = UserDAO.newInstance();
    newUser2.setValue(UserInfo.USERNAME, username2);
    newUser2.setValue(UserInfo.PASSWORD, password2);
    newUser2.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    newUser2.apply();

    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    // Create an enumeration master class
    enumMasterMdBusiness = TestFixtureFactory.createEnumClass1();
    enumMasterMdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter1 = TestFixtureFactory.addCharacterAttribute(enumMasterMdBusiness);
    mdAttributeCharacter1.apply();

    structMdEnumeration = TestFixtureFactory.createMdEnumeation1(enumMasterMdBusiness);
    structMdEnumeration.apply();

    // Create a new MdStruct
    mdStruct = TestFixtureFactory.createMdStruct1();
    mdStruct.apply();

    // Crate a new MdAttribute for the MdStruct
    mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdStruct);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.apply();

    mdAttributeBlob = TestFixtureFactory.addBlobAttribute(mdStruct);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlob.apply();

    // Create a new MdBusiness
    mdBusiness = TestFixtureFactory.createMdBusiness1();
    mdBusiness.apply();

    mdBusinessChild = TestFixtureFactory.createMdBusiness2(mdBusiness);
    mdBusinessChild.apply();

    // Create an MdAttribute on the MdBusiness
    mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness);
    mdAttribute.apply();

    mdAttributeStruct = TestFixtureFactory.addStructAttribute(mdBusiness, mdStruct);
    mdAttributeStruct.apply();

    mdAttributeEnumeration = TestFixtureFactory.addEnumerationAttribute(mdStruct, structMdEnumeration);
    mdAttributeEnumeration.apply();

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

    // Create a new relationship
    mdRelationship = TestFixtureFactory.createMdRelationship1(mdBusiness, mdBusiness);
    mdRelationship.apply();

    relMdAttribute = TestFixtureFactory.addBooleanAttribute(mdRelationship);
    relMdAttribute.apply();

    structDAO = (StructDAO) StructDAO.newInstance(mdStruct.definesType());
    structDAO.apply();

    // Create a businessDAO of MdBusiness
    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    businessDAO.apply();

    businessDAO2 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO2.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO2.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    businessDAO2.apply();

    businessDAO3 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO3.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO3.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    businessDAO3.apply();

    businessDAO4 = BusinessDAO.newInstance(mdBusinessChild.definesType());
    businessDAO4.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO4.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    businessDAO4.apply();

    businessDAO5 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO5.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO5.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    businessDAO5.apply();

    deleteObject1 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject1.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject1.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    deleteObject1.apply();

    deleteObject2 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject2.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject2.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    deleteObject2.apply();

    deleteObject3 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject3.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject3.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    deleteObject3.apply();

    deleteObject4 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject4.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject4.getAttribute(ElementInfo.OWNER).setValue(newUser1.getId());
    deleteObject4.apply();

    // Create some enumeration attribute items.
    BusinessDAO enumItemObject = BusinessDAO.newInstance(enumMasterMdBusiness.definesType());
    enumItemObject.setValue(EnumerationMasterInfo.NAME, "OPTION1");
    enumItemObject.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Option1");
    enumItemObject.setValue(mdAttributeCharacter1.definesAttribute(), "Option 1");
    enumItemId1 = enumItemObject.apply();

    enumItemObject = BusinessDAO.newInstance(enumMasterMdBusiness.definesType());
    enumItemObject.setValue(EnumerationMasterInfo.NAME, "OPTION2");
    enumItemObject.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Option2");
    enumItemObject.setValue(mdAttributeCharacter1.definesAttribute(), "Option 2");
    enumItemId2 = enumItemObject.apply();

    enumItemObject = BusinessDAO.newInstance(enumMasterMdBusiness.definesType());
    enumItemObject.setValue(EnumerationMasterInfo.NAME, "OPTION3");
    enumItemObject.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Option3");
    enumItemObject.setValue(mdAttributeCharacter1.definesAttribute(), "Option 3");
    enumItemId3 = enumItemObject.apply();

    relationshipDAO = RelationshipDAO.newInstance(businessDAO.getId(), businessDAO2.getId(), mdRelationship.definesType());
    relationshipDAO.apply();

    typeTuple1 = TestFixtureFactory.createTypeTuple(state1, mdAttribute);
    typeTuple1.apply();

    typeTuple2 = TestFixtureFactory.createTypeTuple(state1, mdRelationship);
    typeTuple2.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  public static void classTearDown()
  {
    TestFixtureFactory.delete(typeTuple1);
    TestFixtureFactory.delete(typeTuple2);
    TestFixtureFactory.delete(enumMasterMdBusiness);
    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(mdBusinessChild);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdStruct);
    TestFixtureFactory.delete(mdDimension);
    TestFixtureFactory.delete(newUser1);
    TestFixtureFactory.delete(newUser2);
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
    Set<RelationshipDAOIF> set = newUser1.getAllPermissions();
    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser1
      newUser1.revokeAllPermissions(reference.getChildId());
    }

    set = newUser2.getAllPermissions();
    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser2
      newUser2.revokeAllPermissions(reference.getChildId());
    }

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    set = role.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any businessDAO permissions given to newUser
      role.revokeAllPermissions(reference.getChildId());
    }

    // Clear any lingering sessions
    SessionFacade.clearSessions();
  }

  public void testNoCreatePermissions()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      noCreatePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void noCreatePermissions(String sessionId)
  {
    Business test = BusinessFacade.newBusiness(mdBusiness.definesType());

    try
    {
      test.apply();
      fail("Failed to thrown an exception on creation of invalid permisions");
    }
    catch (CreatePermissionException e)
    {
      // This is expected
    }
  }

  public void testNoReadPermissions()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      noReadPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void noReadPermissions(String sessionId)
  {
    try
    {
      Facade.get(sessionId, ElementInfo.ID_VALUE);
      fail("Failed to set readable on DTO when reading an object without read permission.  Object was instantiated.");
    }
    catch (ReadPermissionException e)
    {
      // we want to land here.
    }
  }

  public void testReadPermissions()
  {
    String sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      readPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void readPermissions(String sessionId)
  {
    try
    {
      MutableDTO mutableDTO = Facade.get(sessionId, ElementInfo.ID_VALUE);

      if (!mutableDTO.getId().equals(ElementInfo.ID_VALUE))
      {
        fail("Failed to instantiate an object with read permission.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testNoNewBusinessPermissions()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      noNewBusinessPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void noNewBusinessPermissions(String sessionId)
  {
    try
    {
      EntityDTO entityDTO = Facade.newBusiness(sessionId, MdBusinessInfo.CLASS);

      if (entityDTO.isReadable() || entityDTO.isWritable())
      {
        fail("Created a DTO without permission.");
      }
    }
    catch (CreatePermissionException e)
    {
      // expected
    }
  }

  public void testNewBusinessPermissions()
  {
    String sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      newBusinessPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void newBusinessPermissions(String sessionId)
  {
    try
    {
      EntityDTO entityDTO = Facade.newBusiness(sessionId, MdBusinessInfo.CLASS);

      if (!entityDTO.getType().equals(MdBusinessInfo.CLASS))
      {
        fail("Failed to instantiate a business DTO although adequate permissions were granted. Object was instantiated.");
      }
    }
    catch (PermissionException e)
    {
      fail("Failed to instantiate a business DTO although adequate permissions were granted. Object was not instantiated.");
    }
  }

  public void testNoNewRelationshipPermissions()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
    String systemSessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      noNewRelationshipPermissions(sessionId, systemSessionId);
    }
    finally
    {
      Facade.logout(sessionId);
      Facade.logout(systemSessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void noNewRelationshipPermissions(String sessionId, String systemSessionId)
  {
    try
    {
      BusinessDTO parent = Facade.newBusiness(systemSessionId, mdBusiness.definesType());
      parent = Facade.createBusiness(systemSessionId, parent);

      BusinessDTO child = Facade.newBusiness(systemSessionId, mdBusiness.definesType());
      parent = Facade.createBusiness(systemSessionId, child);

      RelationshipDTO relationshipDTO = Facade.addChild(sessionId, parent.getId(), child.getId(), mdRelationship.definesType());

      if (!relationshipDTO.isReadable() || relationshipDTO.isWritable())
      {
        fail("Created a DTO without permission.");
      }
    }
    catch (CreatePermissionException e)
    {
      // expected
    }
  }

  public void testNewRelationshipPermissions()
  {
    String sessionId = Facade.login(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      newRelationshipPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void newRelationshipPermissions(String sessionId)
  {
    try
    {
      BusinessDTO parent = Facade.newBusiness(sessionId, mdBusiness.definesType());
      parent = Facade.createBusiness(sessionId, parent);

      BusinessDTO child = Facade.newBusiness(sessionId, mdBusiness.definesType());
      child = Facade.createBusiness(sessionId, child);

      RelationshipDTO relationshipDTO = Facade.addChild(sessionId, parent.getId(), child.getId(), mdRelationship.definesType());

      if (!relationshipDTO.getType().equals(mdRelationship.definesType()))
      {
        fail("Failed to instantiate a relationship DTO although adequate permissions were granted. Relationship was instantiated.");
      }
    }
    catch (PermissionException e)
    {
      fail("Failed to instantiate a relationship DTO although adequate permissions were granted. Relationship was not instantiated.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void testTypeCreatePermissions()
  {
    newUser1.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.DELETE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeCreatePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeCreatePermissions(String sessionId)
  {
    Business test = BusinessFacade.newBusiness(mdBusiness.definesType());

    try
    {
      test.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to create a businessDAO with Create Type permissions");
    }

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      fail("Unable to delete a businessDAO with Delete Type permissions");
    }
  }

  @Request(RequestType.SESSION)
  public static void businessDAOCreatePermissions(String sessionId)
  {
    Business test = BusinessFacade.newBusiness(mdBusiness.definesType());

    try
    {
      test.apply();
      fail("Able to create a businessDAO without Create BusinessDAO permissions on the MetaData");
    }
    catch (PermissionException e)
    {
      // This is expected
    }
  }

  @Request(RequestType.SESSION)
  public static void createMdBusiness(String sessionId)
  {
    try
    {
      Business test = BusinessFacade.newBusiness(mdBusiness.definesType());
      test.apply();
    }
    catch (PermissionException e)
    {
      fail(e.getLocalizedMessage());
    }
  }

  public void testOwnerCreatePermissions()
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    owner.grantPermission(Operation.CREATE, mdBusiness.getId());
    owner.grantPermission(Operation.WRITE, mdBusiness.getId());
    owner.grantPermission(Operation.DELETE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      onwerTypeCreatePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void onwerTypeCreatePermissions(String sessionId)
  {
    Business test = null;
    try
    {
      test = BusinessFacade.newBusiness(mdBusiness.definesType());
      test.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to create a businessDAO with Owner Create Type permissions");
    }
    finally
    {
      test.lock();
      test.delete();
    }
  }

  /**
   * Tests logging in with the anonymous user.
   */
  public void testAnonymousLogin()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.loginAnonymous(new Locale[] { CommonProperties.getDefaultLocale() });
    }
    catch (Throwable ex)
    {
      fail(ex.getMessage());
    }

    Facade.logout(sessionId);
  }

  /**
   * Tests logging in with the anonymous user.
   */
  public void testValidChangLogin()
  {
    String sessionId = Facade.loginAnonymous(new Locale[] { CommonProperties.getDefaultLocale() });
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());

    try
    {
      Facade.changeLogin(sessionId, username1, password1);
      writeTypePermissions(sessionId);
    }
    catch (Throwable ex)
    {
      fail(ex.getMessage());
    }

    Facade.logout(sessionId);
  }

  /**
   * Tests logging in with the anonymous user.
   */
  public void testInvalidChangLogin()
  {
    String sessionId = Facade.loginAnonymous(new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidChangLogin(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  /**
   * Tests logging in with the anonymous user.
   */
  @Request(RequestType.SESSION)
  public void invalidChangLogin(String sessionId)
  {
    try
    {
      Facade.changeLogin(sessionId, username1, "invalidPassword");

      fail("Able to change the user on a give session without providing the proper password.");
    }
    catch (Exception ex)
    {
      // We want to land here.
    }
  }

  /**
   * Tests passing in an invalid session id.
   */
  public void testInvalidSessionId()
  {
    String sessionId = "Blah Blah Blah";
    try
    {
      Facade.changeLogin(sessionId, username1, password1);
      fail("An invalid session id was accepted.");
    }
    catch (Throwable e)
    {
      boolean fail = true;
      if (e instanceof RunwayExceptionDTO)
      {
        RunwayExceptionDTO runwayExceptionDTO = (RunwayExceptionDTO) e;

        if (runwayExceptionDTO.getType().equals(InvalidSessionException.class.getName()))
        {
          fail = false;
        }
      }
      if (fail)
      {
        fail(e.getMessage());
      }
    }
  }

  public void testLock()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      noLockTest(sessionId);
      invalidUnlockTest(sessionId);
      validReleaseUserLockTest(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void noLockTest(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    if (test.getLockedBy() != null)
    {
      test.unlock();
    }

    try
    {
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();

      fail("Able to update a Business without acquiring the lock");
    }
    catch (LockException e)
    {
      // This is expected
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidUnlockTest(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.unlock();
      test.apply();

      fail("Able to update a Business without acquiring the lock");
    }
    catch (LockException e)
    {
      // This is Expected
    }
  }

  @Request(RequestType.SESSION)
  public static void validReleaseUserLockTest(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.releaseUserLock(newUser1.getId());
    }
    catch (ClientException e)
    {
      fail(e.getMessage());
    }
  }

  public void testWriteTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void writeTypePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a businessDAO with Type permissions");
    }
  }

  public static void testWriteInheritancePermissions()
  {
    // Give permissions to the super class
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeInheritancePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void writeInheritancePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO4.getId());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a businessDAO with Type permissions");
    }
  }

  public void testWriteStateTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, state1.getId());
    newUser1.grantPermission(Operation.WRITE, typeTuple1.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeStateTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void writeStateTypePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO3.getId());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a with a state type permissions");
    }
  }

  public void testWriteStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeStructTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void writeStructTypePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.setStructValue("testStruct", TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a with a struct type permissions");
    }
  }

  /**
   * Test to ensure that you can set a value on a standalone struct with
   * adequate permissions.Facade
   * 
   */
  public void testValidStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeCharacter.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      validStructTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void validStructTypePermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Yahooo");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot set a value on a standalone struct without
   * adequate permissions.
   * 
   */
  public void testInvalidStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidStructTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void invalidStructTypePermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Yahooo");
      test.apply();

      fail("Able to write a directly to a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // Expected to land here
    }
  }

  /**
   * Test to ensure that you can set a blob on a standalone struct with adequate
   * permissions.
   * 
   */
  public void testValidStructSetBlobPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeBlob.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      validStructSetBlobPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  private static void validStructSetBlobPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable set a blob attribute directly on a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot set a blob on a standalone struct without
   * adequate permissions.
   * 
   */
  public void testInvalidStructSetBlobPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidStructSetBlobPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  private static void invalidStructSetBlobPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
      fail("Able set a blob attribute directly on a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here.
    }
  }

  /**
   * Test to ensure that you can add an enumeration on a standalone struct with
   * adequate permissions.
   * 
   */
  public void testValidStructAddEnumPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      validStructAddEnumPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        Struct test = Struct.get(structDAO.getId());
        test.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  private static void validStructAddEnumPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to add an enumeration item directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot add an enumeration on a standalone struct
   * without adequate permissions.
   * 
   */
  public void testInvalidStructAddEnumPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidStructAddEnumPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        Struct test = Struct.get(structDAO.getId());
        test.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  private static void invalidStructAddEnumPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      fail("Able to add an enumeration item directly to a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // we want to land here
    }
  }

  /**
   * Test to ensure that you can clear all enumerations on a standalone struct
   * with adequate permissions.
   * 
   */
  public void testValidStructClearAllPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getId());

    Struct test = Struct.get(structDAO.getId());
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId2);
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId3);

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      validStructClearAllPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void validStructClearAllPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to clear all enumerations item directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot clear all enumerations on a standalone
   * struct without adequate permissions.
   * 
   */
  public void testInvalidStructClearAllPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    Struct test = Struct.get(structDAO.getId());
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId2);
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId3);

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidStructClearAllPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void invalidStructClearAllPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
      fail("Able to clear all enumerations item directly to a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here
    }
  }

  /**
   * Test to ensure that you can remove an enumeration from a standalone struct
   * with adequate permissions.
   * 
   */
  public void testValidStructRemoveItemPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getId());

    Struct test = Struct.get(structDAO.getId());
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      validStructRemoveItemPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void validStructRemoveItemPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to remove an enumeration item directly from a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot remove an enumeration from a standalone
   * struct without adequate permissions.
   * 
   */
  public void testInvalidStructRemoveItemPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    Struct test = Struct.get(structDAO.getId());
    test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidStructRemoveItemPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void invalidStructRemoveItemPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getId());

    try
    {
      test.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      fail("Able to remove an enumeration item directly from a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here
    }
  }

  public void testWriteStructOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeStructOnStructPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void writeStructOnStructPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you to set a value on a struct that was
   * obtained from an entity.
   */
  public void testWriteStructOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeStructOnStructPermissionsFail(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void writeStructOnStructPermissionsFail(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
      fail("Able to write on a generic struct retrieved from a business entity with inadequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // we want to land here
    }
  }

  /**
   * Tests if permissions allow you to set a blob value on a Struct obtained
   * from a Entity.
   */
  public void testStructSetBlobOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structSetBlobOnStructPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void structSetBlobOnStructPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to set a blob on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from setting a blob value on a Struct
   * obtained from a Entity.
   */
  public void testStructSetBlobOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structSetBlobOnStructPermissionsFail(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void structSetBlobOnStructPermissionsFail(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
      fail("Able to set a blob on a generic struct retrieved from a business entity without adequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here
    }
  }

  /**
   * Tests if permissions allow you to add an enumeration item on a Struct
   * obtained from a Entity.
   */
  public void testStructAddItemOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structAddItemOnStructPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void structAddItemOnStructPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to an enumeration item to an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if inadequate permissions permit you to add an enumeration item on a
   * Struct obtained from a Entity.
   */
  public void testStructAddItemOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structAddItemOnStructPermissionsFail(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  private static void structAddItemOnStructPermissionsFail(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      fail("Able to an enumeration item to an attribute on a generic struct retrieved from a business entity with inadequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // we want to land here
    }
  }

  /**
   * Tests if permissions allow you to remove all enumeration items from a
   * Struct obtained from a Entity.
   */
  public void testStructClearItemsOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    Business test = Business.get(businessDAO.getId());
    final Struct struct = test.getGenericStruct("testStruct");

    test.lock();
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId2);
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId3);
    test.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structClearItemsOnStructPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        Business test = Business.get(businessDAO.getId());
        Struct struct = test.getGenericStruct("testStruct");

        test.lock();
        struct.clearEnum(mdAttributeEnumeration.definesAttribute());
        test.apply();
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  private static void structClearItemsOnStructPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to clear enumeration items from an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from removing all enumeration items from a
   * Struct obtained from a Entity.
   */
  public void testStructClearItemsOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    Business test = Business.get(businessDAO.getId());
    final Struct struct = test.getGenericStruct("testStruct");

    test.lock();
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId2);
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId3);
    test.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structClearItemsOnStructPermissionsFail(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  private static void structClearItemsOnStructPermissionsFail(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
      fail("Able to clear enumeration items from an attribute on a generic struct retrieved from a business entityout with adequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // we want to land here.
    }
  }

  /**
   * Tests if permissions allow you to remove an enumeration item from a Struct
   * obtained from a Entity.
   */
  public void testStructRemoveItemOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getId());

    Business test = Business.get(businessDAO.getId());
    final Struct struct = test.getGenericStruct("testStruct");

    test.lock();
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    test.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structRemoveItemOnStructPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        Business test = Business.get(businessDAO.getId());
        Struct struct = test.getGenericStruct("testStruct");

        test.lock();
        struct.clearEnum(mdAttributeEnumeration.definesAttribute());
        test.apply();
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  private static void structRemoveItemOnStructPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to remove an enumeration item from an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from removing an enumeration item from a
   * Struct obtained from a Entity.
   */
  public void testStructRemoveItemOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    Business test = Business.get(businessDAO.getId());
    final Struct struct = test.getGenericStruct("testStruct");

    test.lock();
    struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
    test.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      structRemoveItemOnStructPermissionsFail(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  private static void structRemoveItemOnStructPermissionsFail(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      fail("Able to remove an enumeration item from an attribute on a generic struct retrieved from a business entity without adequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here.
    }
  }

  public void testInvalidWritePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidWritePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidWritePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
      test.unlock();

      fail("Able to write a with an attribute with WRITE permissions only on the defining MdBusiness");
    }
    catch (AttributeWritePermissionException e)
    {
      // This is expected
    }
    finally
    {
      if (test.getValue(ElementInfo.LOCKED_BY).equals(newUser1.getId()))
      {
        test.releaseUserLock(newUser1.getId());
      }
    }
  }

  public void testInvalidWritePermissions2()
  {
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidWritePermissions2(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidWritePermissions2(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
      test.unlock();

      fail("Able to write a with a WRITE permissions on MdAttribute, but not the defining MdBusiness");
    }
    catch (WritePermissionException e)
    {
      // This is expected
    }
  }

  public void testTypeAddChildPermissions()
  {
    newUser1.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    Business parent = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeAddChildPermissions(sessionId, parent);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeAddChildPermissions(String sessionId, Business parent)
  {
    Business child = Business.get(businessDAO2.getId());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a child with type ADD_CHILD permissions. \n" + e.getMessage());
    }

    rel.delete();
  }

  public void testInvalidTypeAddChildPermissions()
  {
    Business parent = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidTypeAddChildPermissions(sessionId, parent);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidTypeAddChildPermissions(String sessionId, Business parent)
  {
    Business child = Business.get(businessDAO2.getId());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
      fail("Able to add a child without type ADD_CHILD permissions. \n");
    }
    catch (Throwable e)
    {
      if (!e.getMessage().contains(AddChildPermissionException.class.getName()))
      {
        // This is expected
      }
    }
  }

  public void testTypeWriteChildPermissions()
  {
    newUser1.grantPermission(Operation.WRITE_CHILD, mdRelationship.getId());
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getId());

    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeWriteChildPermissions(sessionId, relationship.getId());
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        TestFixtureFactory.delete(relationship);
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  public static void typeWriteChildPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with type WRITE_CHILD permissions. \n" + e.getMessage());
    }
  }

  public void testInvalidWriteRelationshipPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getId());

    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidWriteRelationsPermissions(sessionId, relationship.getId());
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        TestFixtureFactory.delete(relationship);
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  public static void invalidWriteRelationsPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
      fail("Able to modify a child without type WRITE_CHILD permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  public void testTypeWriteRelationshipPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdRelationship.getId());
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getId());

    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeWriteRelationshipPermissions(sessionId, relationship.getId());
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        TestFixtureFactory.delete(relationship);
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  public static void typeWriteRelationshipPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  public void testTypeDeleteAllChildrenPermissions()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();
    Relationship relationship2 = parent.addChild(child, mdRelationship.definesType());
    relationship2.apply();
    Relationship relationship3 = parent.addChild(child, mdRelationship.definesType());
    relationship3.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteAllChildrenPermissions(sessionId, parent, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteAllChildrenPermissions(String sessionId, Business parent, Business child)
  {
    try
    {
      parent.removeAllChildren(child, mdRelationship.definesType());
    }
    catch (PermissionException e)
    {
      fail("Unable to remove children with the DELETE_CHILD permission. \n" + e.getMessage());
    }
  }

  public void testTypeDeleteChildPermission()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteChildPermission(sessionId, parent, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  public static void typeDeleteChildPermission(String sessionId, Business parent, Relationship relationship1)
  {
    try
    {
      parent.removeChild(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Unable to remove a given relationship with the DELETE_CHILD permission. \n" + e.getMessage());
    }
  }

  public void testTypeDeleteAllChildrenPermissions_missingDeleteChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();
    Relationship relationship2 = parent.addChild(child, mdRelationship.definesType());
    relationship2.apply();
    Relationship relationship3 = parent.addChild(child, mdRelationship.definesType());
    relationship3.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteAllChildrenPermissions1(sessionId, parent, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteAllChildrenPermissions1(String sessionId, Business parent, Business child)
  {
    try
    {
      parent.removeAllChildren(child, mdRelationship.definesType());
    }
    catch (PermissionException e)
    {
      fail("Able to remove a child relationship with the DELETE permission");
    }
  }

  public void testTypeDeleteChildPermission_missingDeleteChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteChildPermission1(sessionId, parent, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteChildPermission1(String sessionId, Business parent, Relationship relationship1)
  {
    try
    {
      parent.removeChild(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Unable to remove a child relationship without the required DELETE permission");
    }
  }

  public void testTypeDeleteAllParentPermissions()
  {
    newUser1.grantPermission(Operation.DELETE_PARENT, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();
    Relationship relationship2 = child.addParent(parent, mdRelationship.definesType());
    relationship2.apply();
    Relationship relationship3 = child.addParent(parent, mdRelationship.definesType());
    relationship3.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteAllParentPermissions(sessionId, parent, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteAllParentPermissions(String sessionId, Business parent, Business child)
  {
    try
    {
      child.removeAllParents(parent, mdRelationship.definesType());
    }
    catch (PermissionException e)
    {
      fail("Unable to remove children with the DELETE_PARENT permission. \n" + e.getMessage());
    }
  }

  public void testTypeDeleteParentPermission()
  {
    newUser1.grantPermission(Operation.DELETE_PARENT, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject2.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteParentPermission(sessionId, child, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  public static void typeDeleteParentPermission(String sessionId, Business child, Relationship relationship1)
  {
    try
    {
      child.removeParent(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Unable to remove a given relationship with the DELETE_PARENT permission. \n" + e.getMessage());
    }
  }

  public void testTypeDeleteAllParentsPermissions_missingParentChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();
    Relationship relationship2 = child.addParent(parent, mdRelationship.definesType());
    relationship2.apply();
    Relationship relationship3 = child.addParent(parent, mdRelationship.definesType());
    relationship3.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteAllParentsPermissions1(sessionId, parent, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteAllParentsPermissions1(String sessionId, Business parent, Business child)
  {
    try
    {
      child.removeAllParents(parent, mdRelationship.definesType());
    }
    catch (PermissionException e)
    {
      fail("Unable to remove a parent relationship with DELETE permission");
    }
  }

  public void testTypeDeleteParentPermission_missingDeleteParent()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject1.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeleteParentPermission1(sessionId, child, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeleteParentPermission1(String sessionId, Business child, Relationship relationship1)
  {
    try
    {
      child.removeParent(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Unable to remove a child relationship with DELETE permission");
    }
  }

  public void testInvalidTypeDeleteParentPermission_missingDelete()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    // Create the relationship
    final Business parent = Business.get(businessDAO.getId());
    final Business child = Business.get(deleteObject2.getId());

    final Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidTypeDeleteParentPermission2(sessionId, child, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        child.removeParent(relationship1);
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  public static void invalidTypeDeleteParentPermission2(String sessionId, Business child, Relationship relationship1)
  {
    try
    {
      child.removeParent(relationship1);
      fail("Able to remove a parent relationship without the required DELETE permission");
    }
    catch (DeleteParentPermissionException e)
    {
      // This is expected
    }
  }

  public void testStateAddChildPermissions()
  {
    newUser1.grantPermission(Operation.ADD_CHILD, typeTuple2.getId());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    Business parent = Business.get(businessDAO3.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      stateAddChildPermissions(sessionId, parent);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void stateAddChildPermissions(String sessionId, Business parent)
  {

    Business child = Business.get(businessDAO.getId());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a child with type ADD_CHILD permissions");
    }

    rel.delete();
  }

  public void testOwnerAddChildPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    Business parent = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerAddChildPermissions(sessionId, parent);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerAddChildPermissions(String sessionId, Business parent)
  {
    Business child = Business.get(businessDAO2.getId());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a child with type ADD_CHILD permissions");
    }

    rel.delete();
  }

  public void testOwnerWriteChildPermissions()
  {
    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE, relMdAttribute.getId());
    role.grantPermission(Operation.DELETE, mdRelationship.getId());

    role.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = ownerWriteChildPermissions_CreateRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    role.revokePermission(Operation.ADD_CHILD, mdRelationship.getId());
    role.grantPermission(Operation.WRITE_CHILD, mdRelationship.getId());

    sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerWriteChildPermissions(sessionId, relationshipId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static String ownerWriteChildPermissions_CreateRelationship(String sessionId)
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    return relationship.getId();
  }

  @Request(RequestType.SESSION)
  public static void ownerWriteChildPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with owner WRITE permissions. \n" + e.getMessage());
    }
    finally
    {
      relationship.delete();
    }
  }

  /**
   * Make sure that the owner of the relationship is different that the owner on
   * the parent.
   */
  public void testOwnerWriteChildRelationshipPermissions()
  {
    newUser2.grantPermission(Operation.CREATE, mdRelationship.getId());

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE_CHILD, mdRelationship.getId());
    role.grantPermission(Operation.WRITE_CHILD, relMdAttribute.getId());
    String relationshipId = null;

    // create the relationship with one user
    String sessionId = Facade.login(username2, password2, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = typeWriteRelationshipPermissions_createRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    // Modify an attribute on the relationship using the permissions of the
    // owner of the parent.
    sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerWriteRelationshipPermissions(sessionId, relationshipId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    final String _relationshipId = relationshipId;

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        TestFixtureFactory.delete(Relationship.get(_relationshipId));
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  public static String typeWriteRelationshipPermissions_createRelationship(String sessionId)
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    return relationship.getId();
  }

  @Request(RequestType.SESSION)
  public static void ownerWriteRelationshipPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  public void testInvalidOwnerWriteChildPermissions()
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerInvalidWriteChildPermissions(sessionId, relationship.getId());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerInvalidWriteChildPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
      fail("Able to modify a child without owner WRITE_CHILD permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  public void testOwnerDeleteChildPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(deleteObject2.getId());

    Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerDeleteChildPermissions(sessionId, parent, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  public static void ownerDeleteChildPermissions(String sessionId, Business parent, Relationship relationship1)
  {
    try
    {
      parent.removeChild(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Owner is unable to delete child with required DELETE_CHILD permission");
    }
  }

  public void testInvalidOwnerDeleteChildPermissions()
  {

    // Create the relationship
    final Business parent = Business.get(businessDAO.getId());
    final Business child = Business.get(deleteObject1.getId());

    final Relationship relationship1 = parent.addChild(child, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerInvalidDeleteChildPermissions(sessionId, parent, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        parent.removeChild(relationship1);
      }
    }.run();

  }

  @Request(RequestType.SESSION)
  public static void ownerInvalidDeleteChildPermissions(String sessionId, Business parent, Relationship relationship1)
  {
    try
    {
      parent.removeChild(relationship1);
      fail("Owner able to delete child without required DELETE_CHILD permission");
    }
    catch (DeleteChildPermissionException e)
    {
      // This is expected
    }
  }

  public void testAddChildPermissions2()
  {
    newUser1.grantPermission(Operation.CREATE, mdRelationship.getId());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      Business parent = Business.get(businessDAO.getId());
      addChildPermissions2(sessionId, parent);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void addChildPermissions2(String sessionId, Business parent)
  {
    Business child = Business.get(businessDAO2.getId());

    try
    {
      parent.addChild(child, mdRelationship.definesType()).apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a child with CREATE permissions on the MdRelationship, but no ADD_CHILD permissions on the MdBusiness");
    }
  }

  public void testStateAddParentPermissions()
  {
    newUser1.grantPermission(Operation.ADD_PARENT, typeTuple2.getId());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    Business child = Business.get(businessDAO3.getId());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      stateAddParentPermissions(sessionId, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void stateAddParentPermissions(String sessionId, Business child)
  {
    Business parent = Business.get(businessDAO.getId());

    Relationship rel = null;

    try
    {
      rel = child.addParent(parent, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a parent with type ADD_PARENT permissions");
    }

    rel.delete();
  }

  public void testOwnerAddParentPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getId());

    Business child = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerAddParentPermissions(sessionId, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerAddParentPermissions(String sessionId, Business child)
  {
    Business parent = Business.get(businessDAO2.getId());
    Relationship rel = null;

    try
    {
      rel = child.addParent(parent, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to add a parent with type ADD_PARENT permissions");
    }

    rel.delete();
  }

  public void testInvalidOwnerAddParentPermissions()
  {
    Business child = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidOwnerAddParentPermissions(sessionId, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidOwnerAddParentPermissions(String sessionId, Business child)
  {
    Business parent = Business.get(businessDAO2.getId());
    Relationship rel = null;

    try
    {
      rel = child.addParent(parent, mdRelationship.definesType());
      rel.apply();
      fail("Able to add a parent with type ADD_PARENT permissions");
    }
    catch (AddParentPermissionException e)
    {
      // This is expected
    }
  }

  public void testOwnerWriteAddPermissions()
  {
    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE, relMdAttribute.getId());
    role.grantPermission(Operation.DELETE, mdRelationship.getId());

    role.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = ownerWriteParentPermissions_CreateRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    role.revokePermission(Operation.ADD_PARENT, mdRelationship.getId());
    role.grantPermission(Operation.WRITE_PARENT, mdRelationship.getId());

    sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerWriteParentPermissions(sessionId, relationshipId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static String ownerWriteParentPermissions_CreateRelationship(String sessionId)
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    return relationship.getId();
  }

  @Request(RequestType.SESSION)
  public static void ownerWriteParentPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with owner WRITE permissions. \n" + e.getMessage());
    }
    finally
    {
      relationship.delete();
    }
  }

  /**
   * Make sure that the owner of the relationship is different that the owner on
   * the child.
   */
  public void testOwnerWriteParentRelationshipPermissions()
  {
    newUser2.grantPermission(Operation.CREATE, mdRelationship.getId());

    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE_PARENT, mdRelationship.getId());
    role.grantPermission(Operation.WRITE_PARENT, relMdAttribute.getId());

    // create the relationship with one user
    String sessionId = Facade.login(username2, password2, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = typeWriteParentRelationshipPermissions_createRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    // Modify an attribute on the relationship using the permissions of the
    // owner of the parent.
    sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerWriteParentRelationshipPermissions(sessionId, relationshipId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    final String _relationshipId = relationshipId;

    new SystemSession()
    {
      @Request(RequestType.SESSION)
      protected void doIt(String sessionId)
      {
        TestFixtureFactory.delete(Relationship.get(_relationshipId));
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  public static String typeWriteParentRelationshipPermissions_createRelationship(String sessionId)
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    return relationship.getId();
  }

  @Request(RequestType.SESSION)
  public static void ownerWriteParentRelationshipPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  public void testInvalidOwnerWriteParentPermissions()
  {
    Business parent = Business.get(businessDAO.getId());
    Business child = Business.get(businessDAO2.getId());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerInvalidWriteParentPermissions(sessionId, relationship.getId());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerInvalidWriteParentPermissions(String sessionId, String relationshipId)
  {
    Relationship relationship = Relationship.get(relationshipId);

    try
    {
      relationship.lock();
      relationship.setValue(relMdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      relationship.apply();
      fail("Able to modify a child without owner WRITE_PARENT permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  public void testOwnerDeleteParentPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.DELETE_PARENT, mdRelationship.getId());

    // Create the relationship
    Business parent = Business.get(businessDAO2.getId());
    Business child = Business.get(businessDAO.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerDeleteParentPermissions(sessionId, child, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  public static void ownerDeleteParentPermissions(String sessionId, Business child, Relationship relationship1)
  {
    try
    {
      child.removeParent(relationship1);
    }
    catch (PermissionException e)
    {
      fail("Owner is unable to delete parent with required DELETE_PARENT permission");
    }
  }

  public void testInvalidOwnerDeleteParentPermissions()
  {
    // Create the relationship
    Business parent = Business.get(businessDAO2.getId());
    Business child = Business.get(businessDAO.getId());

    Relationship relationship1 = child.addParent(parent, mdRelationship.definesType());
    relationship1.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidOwnerDeleteParentPermissions(sessionId, child, relationship1);
    }
    finally
    {
      Facade.logout(sessionId);
    }

  }

  @Request(RequestType.SESSION)
  public static void invalidOwnerDeleteParentPermissions(String sessionId, Business child, Relationship relationship1)
  {
    try
    {
      child.removeParent(relationship1);
      fail("Owner is able to delete parent with required DELETE_PARENT permission");
    }
    catch (DeleteParentPermissionException e)
    {
      // This is expected
    }
  }

  public void testAddParentPermissions2()
  {
    newUser1.grantPermission(Operation.CREATE, mdRelationship.getId());

    Business child = Business.get(businessDAO.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      addParentPermissions2(sessionId, child);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void addParentPermissions2(String sessionId, Business child)
  {
    Business parent = Business.get(businessDAO2.getId());

    try
    {
      child.addParent(parent, mdRelationship.definesType()).apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to ADD_PARENT with CREATE permissions on the MdRelationship");
    }
  }

  public void testInvalidDeletePermissions()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidDeletePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidDeletePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.delete();

      fail("Able to DELETE without permissions");
    }
    catch (DeletePermissionException e)
    {
      // This is expected
    }
  }

  public static void testTypeDeletePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.DELETE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeDeletePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void typeDeletePermissions(String sessionId)
  {
    Business test = Business.get(deleteObject1.getId());

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      fail("Unable to delet with Type Permissions");
    }
  }

  public static void testOwnerDeletePermissions()
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    owner.grantPermission(Operation.WRITE, mdBusiness.getId());
    owner.grantPermission(Operation.DELETE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerDeletePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerDeletePermissions(String sessionId)
  {
    Business test = Business.get(deleteObject3.getId());

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      fail("Unable to delete with owner Permissions");
    }
  }

  public static void testStateDeletePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.DELETE, state1.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      stateDeletePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void stateDeletePermissions(String sessionId)
  {
    Business test = Business.get(deleteObject4.getId());

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      fail("Unable to delete with state Permissions");
    }
  }

  public static void testPromotePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.PROMOTE, state2.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      promotePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void promotePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO5.getId());

    try
    {
      test.lock();
      test.promote("transition1");
      assertEquals(state2, businessDAO5.currentState());
    }
    catch (PermissionException e)
    {
      fail("Unable to promote with state type PROMOTE permissions");
    }
    finally
    {
      test.unlock();
    }
  }

  public static void testOwnerPromotePermissions()
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    owner.grantPermission(Operation.WRITE, mdBusiness.getId());
    owner.grantPermission(Operation.PROMOTE, state2.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerPromotePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void ownerPromotePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.promote("transition1");
    }
    catch (PermissionException e)
    {
      fail("Unable to promote with state-businessDAO PROMOTE permissions");
    }
    finally
    {
      test.unlock();
    }
  }

  public static void testInvalidPromotePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidPromotePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidPromotePermissions(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());

    try
    {
      test.lock();
      test.promote("transition3");

      fail("Able to PROMOTE with no permissions");
    }
    catch (PromotePermissionException e)
    {
      // This is expected
    }
    finally
    {
      test.unlock();
    }
  }

  public static void testInvalidPromotePermissions2()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.PROMOTE, state1.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidPromotePermissions2(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidPromotePermissions2(String sessionId)
  {
    Business test = Business.get(businessDAO.getId());
    try
    {
      test.lock();

      test.promote("transition1");

      fail("Able to PROMOTE with permissions on the current state, but not on the destination state");
    }
    catch (PromotePermissionException e)
    {
      // This is expected
    }
    finally
    {
      test.unlock();
    }
  }

  public static void testRelationshipPermission()
  {
    newUser1.grantPermission(Operation.WRITE, mdRelationship.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void relationshipPermissions(String sessionId)
  {
    Relationship test = Relationship.get(relationshipDAO.getId());

    try
    {
      test.lock();
      test.apply();
    }
    catch (PermissionException e)
    {
      fail("Unable to update a relationship with WRITE permissions on the MdTree");
    }
  }

  public static void testInvalidRelationshipPermission()
  {
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidRelationshipPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidRelationshipPermissions(String sessionId)
  {
    Relationship test = Relationship.get(relationshipDAO.getId());

    try
    {
      test.lock();
      test.apply();
      test.unlock();

      fail("Able to write on a relationship without permissions");

    }
    catch (WritePermissionException e)
    {
      // This is expected
    }
  }

  public void testAttributeDimensionPermissions()
  {
    MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);
    
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttributeDimension.getId());
    newUser1.grantPermission(Operation.READ, mdAttributeDimension.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionFacade.setDimension(mdDimension.getKey(), sessionId);

    try
    {
      attributeDimensionPermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void attributeDimensionPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO4.getId());

    try
    {
      boolean value = Boolean.parseBoolean(test.getValue(mdAttribute.definesAttribute()));

      test.lock();
      test.setValue(mdAttribute.definesAttribute(), new Boolean(!value).toString());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      fail("Unable to write a businessDAO with Type permissions");
    }
  }

  public void testClassDimensionPermissions()
  {
    MdClassDimensionDAOIF mdClassDimension = mdDimension.getMdClassDimension(mdBusiness);

    newUser1.grantPermission(Operation.WRITE, mdClassDimension.getId());
    newUser1.grantPermission(Operation.READ, mdClassDimension.getId());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getId());
    newUser1.grantPermission(Operation.READ, mdAttribute.getId());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionFacade.setDimension(mdDimension.getKey(), sessionId);

    try
    {
      classDimensionPermissions(sessionId);
    }
    catch (Exception e)
    {
      fail(e.getLocalizedMessage());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void classDimensionPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO4.getId());

    boolean value = Boolean.parseBoolean(test.getValue(mdAttribute.definesAttribute()));

    test.lock();
    test.setValue(mdAttribute.definesAttribute(), new Boolean(!value).toString());
    test.apply();
  }

  public void testDenyCreatePermissions()
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
        subRole.assignMember(newUser1);

        superRole.grantPermission(Operation.CREATE, mdBusiness.getId());
        subRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getId());

        String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
        SessionFacade.setDimension(mdDimension.getKey(), sessionId);

        try
        {
          noCreatePermissions(sessionId);
        }
        finally
        {
          Facade.logout(sessionId);
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

  public void testOverwriteDenyCreatePermissions()
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
        subRole.assignMember(newUser1);

        superRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getId());
        subRole.grantPermission(Operation.CREATE, mdBusiness.getId());

        String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
        SessionFacade.setDimension(mdDimension.getKey(), sessionId);

        try
        {
          createMdBusiness(sessionId);
        }
        finally
        {
          Facade.logout(sessionId);
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
