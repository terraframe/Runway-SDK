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
package com.runwaysdk.session;

import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
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
import com.runwaysdk.business.rbac.SingleActorDAO;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.ElementInfo;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SingleActorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.MdClassDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.io.SharedTestDataManager;
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
import com.runwaysdk.facade.Facade;

@RunWith(ClasspathTestRunner.class)
public class IntegratedSessionTest
{
  public static final String TEST_DATA_PREFIX = "IntegratedSessionTest";
  
  /**
   * The test user object
   */
  private static UserDAO                   newUser1;

  /**
   * The test user object
   */
  private static UserDAO                   newUser2;

  /**
   * Test metadata of SingleActor subtype
   */
  private static MdBusinessDAO             mdActor;

  /**
   * Subtype of SingleActorDAO test object
   **/
  private static SingleActorDAO            newActor1;

  /**
   * Test dimension
   */
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
   * The username for the user .
   */
  private final static String              username1    = (TEST_DATA_PREFIX + "smethie").toLowerCase();

  /**
   * The password for the user.
   */
  private final static String              password1    = "1234";

  /**
   * The username for the user .
   */
  private final static String              username2    = (TEST_DATA_PREFIX + "jdoe").toLowerCase();

  /**
   * The password for the user.
   */
  private final static String              password2    = "9876";
  
  /**
   * The maximum number of sessions for the user.
   */
  private final static int                 sessionLimit = 2;

  /**
   * The OID of an enumeration Item.
   */
  private static String                    enumItemId1;

  /**
   * The OID of an enumeration Item.
   */
  private static String                    enumItemId2;

  /**
   * The OID of an enumeration Item.
   */
  private static String                    enumItemId3;

  /**
   * The setup done before the test suite is run
   */
  @BeforeClass
  public static void classSetUp()
  {
    //boolean skipGen = LocalProperties.isSkipCodeGenAndCompile();
    //LocalProperties.setSkipCodeGenAndCompile(true);
    
    classSetupInRequest();
    
    //LocalProperties.setSkipCodeGenAndCompile(skipGen);
  }

  @Request
  private static void classSetupInRequest()
  {
    newUser1 = SharedTestDataManager.getOrCreateUserDAO(username1, password1, sessionLimit, false);
    newUser2 = SharedTestDataManager.getOrCreateUserDAO(username2, password2, sessionLimit, false);

    mdActor = SharedTestDataManager.getOrCreateMdBusiness(TEST_DATA_PREFIX + "TestUser", mdBiz -> {
      mdBiz.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, MdBusinessDAO.getMdBusinessDAO(SingleActorInfo.CLASS).getOid());
    });

    newActor1 = (SingleActorDAO) BusinessDAO.newInstance(mdActor.definesType());
    newActor1.apply();

    mdDimension = SharedTestDataManager.getOrCreateMdDimension(SessionTestSuite.MD_DIMENSION_NAME);

    MdAttributeCharacterDAO mdAttributeCharacter1;
    enumMasterMdBusiness = (MdBusinessDAO) SharedTestDataManager.getMdTypeIfExist(TestFixConst.TEST_PACKAGE, TEST_DATA_PREFIX + "Enum");
    if (enumMasterMdBusiness == null)
    {
      enumMasterMdBusiness = SharedTestDataManager.getOrCreateMdBusiness(TEST_DATA_PREFIX + "Enum", mdBiz -> {
        mdBiz.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration mdBusiness Test");
        mdBiz.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
        mdBiz.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
      });
      
      mdAttributeCharacter1 = TestFixtureFactory.addCharacterAttribute(enumMasterMdBusiness);
      mdAttributeCharacter1.apply();
    }
    else
    {
      mdAttributeCharacter1 = (MdAttributeCharacterDAO) enumMasterMdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
    }
    
    structMdEnumeration = SharedTestDataManager.getOrCreateMdEnumeration(TEST_DATA_PREFIX + "MdEnumStruct", enumMasterMdBusiness.getOid());

    mdStruct = SharedTestDataManager.getMdStructIfExist(TEST_DATA_PREFIX + "MdStruct");
    if (mdStruct == null)
    {
      mdStruct = SharedTestDataManager.getOrCreateMdStruct(TEST_DATA_PREFIX + "MdStruct");

      mdAttributeCharacter = TestFixtureFactory.addCharacterAttribute(mdStruct);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter.apply();

      mdAttributeBlob = TestFixtureFactory.addBlobAttribute(mdStruct);
      mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeBlob.apply();
    }
    else
    {
      mdAttributeCharacter = (MdAttributeConcreteDAO) mdStruct.definesAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
      mdAttributeBlob = (MdAttributeBlobDAO) mdStruct.definesAttribute(TestFixConst.ATTRIBUTE_BLOB);
    }

    mdBusiness = (MdBusinessDAO) SharedTestDataManager.getMdTypeIfExist(TestFixConst.TEST_PACKAGE, TEST_DATA_PREFIX + "MdBusiness");
    if (mdBusiness == null)
    {
      mdBusiness = SharedTestDataManager.getOrCreateMdBusiness(TEST_DATA_PREFIX + "MdBusiness");
      
      mdAttribute = TestFixtureFactory.addBooleanAttribute(mdBusiness);
      mdAttribute.apply();
      
      mdAttributeStruct = TestFixtureFactory.addStructAttribute(mdBusiness, mdStruct);
      mdAttributeStruct.apply();

      mdAttributeEnumeration = TestFixtureFactory.addEnumerationAttribute(mdStruct, structMdEnumeration);
      mdAttributeEnumeration.apply();
    }
    else
    {
      mdAttribute = (MdAttributeConcreteDAO) mdBusiness.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);
      mdAttributeStruct = (MdAttributeConcreteDAO) mdBusiness.definesAttribute("testStruct");
      mdAttributeEnumeration =  (MdAttributeEnumerationDAO) mdStruct.definesAttribute("testEnumeration");
    }

    mdBusinessChild = SharedTestDataManager.getOrCreateMdBusiness(TEST_DATA_PREFIX + "MdBusinessChild", mdBiz -> {
      mdBiz.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusiness.getOid());
    });

    mdRelationship = (MdRelationshipDAO) SharedTestDataManager.getMdTypeIfExist(TestFixConst.TEST_PACKAGE, TEST_DATA_PREFIX + "MdRelationship");
    if (mdRelationship == null)
    {
      mdRelationship = SharedTestDataManager.getOrCreateMdRelationship(TEST_DATA_PREFIX + "MdRelationship", mdBusiness, mdBusiness);
      
      relMdAttribute = TestFixtureFactory.addBooleanAttribute(mdRelationship);
      relMdAttribute.apply();
    }
    else
    {
      relMdAttribute = (MdAttributeConcreteDAO) mdRelationship.definesAttribute(TestFixConst.ATTRIBUTE_BOOLEAN);
    }

    structDAO = (StructDAO) StructDAO.newInstance(mdStruct.definesType());
    structDAO.apply();

    // Create a businessDAO of MdBusiness
    businessDAO = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    businessDAO.apply();

    businessDAO2 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO2.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO2.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    businessDAO2.apply();

    businessDAO3 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO3.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO3.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    businessDAO3.apply();

    businessDAO4 = BusinessDAO.newInstance(mdBusinessChild.definesType());
    businessDAO4.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO4.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    businessDAO4.apply();

    businessDAO5 = BusinessDAO.newInstance(mdBusiness.definesType());
    businessDAO5.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    businessDAO5.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    businessDAO5.apply();

    deleteObject1 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject1.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject1.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    deleteObject1.apply();

    deleteObject2 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject2.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject2.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    deleteObject2.apply();

    deleteObject3 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject3.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject3.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
    deleteObject3.apply();

    deleteObject4 = BusinessDAO.newInstance(mdBusiness.definesType());
    deleteObject4.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
    deleteObject4.getAttribute(ElementInfo.OWNER).setValue(newUser1.getOid());
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

    relationshipDAO = RelationshipDAO.newInstance(businessDAO.getOid(), businessDAO2.getOid(), mdRelationship.definesType());
    relationshipDAO.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    SharedTestDataManager.deleteAllRelationshipObjects(TestFixConst.TEST_PACKAGE, mdRelationship.getTypeName());
    SharedTestDataManager.deleteAllStructObjects(TestFixConst.TEST_PACKAGE, TEST_DATA_PREFIX + "MdStruct");
    SharedTestDataManager.deleteAllBusinessObjects(TestFixConst.TEST_PACKAGE, mdBusiness.getTypeName());
    SharedTestDataManager.deleteAllBusinessObjects(TestFixConst.TEST_PACKAGE, mdBusinessChild.getTypeName());
    SharedTestDataManager.deleteAllBusinessObjects(TestFixConst.TEST_PACKAGE, enumMasterMdBusiness.getTypeName());
    
//    TestFixtureFactory.delete(enumMasterMdBusiness);
//    TestFixtureFactory.delete(mdRelationship);
//    TestFixtureFactory.delete(mdBusinessChild);
//    TestFixtureFactory.delete(mdBusiness);
//    TestFixtureFactory.delete(mdStruct);
//    TestFixtureFactory.delete(mdDimension);
//    TestFixtureFactory.delete(newUser1);
//    TestFixtureFactory.delete(newUser2);

    TestFixtureFactory.delete(newActor1);
    TestFixtureFactory.delete(mdActor);
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
    Set<RelationshipDAOIF> set = newUser1.getAllPermissions();
    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser1
      newUser1.revokeAllPermissions(reference.getChildOid());
    }

    set = newUser2.getAllPermissions();
    for (RelationshipDAOIF reference : set)
    {
      // Revoke any type permissions given to newUser2
      newUser2.revokeAllPermissions(reference.getChildOid());
    }

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    set = role.getAllPermissions();

    for (RelationshipDAOIF reference : set)
    {
      // Revoke any businessDAO permissions given to newUser
      role.revokeAllPermissions(reference.getChildOid());
    }

    // Clear any lingering sessions
    SessionFacade.clearSessions();
  }

  @Request
  @Test
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
      Assert.fail("Failed to thrown an exception on creation of invalid permisions");
    }
    catch (CreatePermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
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
      Assert.fail("Failed to set readable on DTO when reading an object without read permission.  Object was instantiated.");
    }
    catch (ReadPermissionException e)
    {
      // we want to land here.
    }
  }

  @Request
  @Test
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

      if (!mutableDTO.getOid().equals(ElementInfo.ID_VALUE))
      {
        Assert.fail("Failed to instantiate an object with read permission.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
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
        Assert.fail("Created a DTO without permission.");
      }
    }
    catch (CreatePermissionException e)
    {
      // expected
    }
  }

  @Request
  @Test
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
        Assert.fail("Failed to instantiate a business DTO although adequate permissions were granted. Object was instantiated.");
      }
    }
    catch (PermissionException e)
    {
      Assert.fail("Failed to instantiate a business DTO although adequate permissions were granted. Object was not instantiated.");
    }
  }

  @Request
  @Test
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

      RelationshipDTO relationshipDTO = Facade.addChild(sessionId, parent.getOid(), child.getOid(), mdRelationship.definesType());

      if (!relationshipDTO.isReadable() || relationshipDTO.isWritable())
      {
        Assert.fail("Created a DTO without permission.");
      }
    }
    catch (CreatePermissionException e)
    {
      // expected
    }
  }

  @Request
  @Test
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

      RelationshipDTO relationshipDTO = Facade.addChild(sessionId, parent.getOid(), child.getOid(), mdRelationship.definesType());

      if (!relationshipDTO.getType().equals(mdRelationship.definesType()))
      {
        Assert.fail("Failed to instantiate a relationship DTO although adequate permissions were granted. Relationship was instantiated.");
      }
    }
    catch (PermissionException e)
    {
      Assert.fail("Failed to instantiate a relationship DTO although adequate permissions were granted. Relationship was not instantiated.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Request
  @Test
  public void testTypeCreatePermissions()
  {
    newUser1.grantPermission(Operation.CREATE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.DELETE, mdBusiness.getOid());

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
      Assert.fail("Unable to create a businessDAO with Create Type permissions");
    }

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to delete a businessDAO with Delete Type permissions");
    }
  }

  @Request(RequestType.SESSION)
  public static void businessDAOCreatePermissions(String sessionId)
  {
    Business test = BusinessFacade.newBusiness(mdBusiness.definesType());

    try
    {
      test.apply();
      Assert.fail("Able to create a businessDAO without Create BusinessDAO permissions on the MetaData");
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
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Request
  @Test
  public void testOwnerCreatePermissions()
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    owner.grantPermission(Operation.CREATE, mdBusiness.getOid());
    owner.grantPermission(Operation.WRITE, mdBusiness.getOid());
    owner.grantPermission(Operation.DELETE, mdBusiness.getOid());

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
      Assert.fail("Unable to create a businessDAO with Owner Create Type permissions");
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
  @Request
  @Test
  public void testAnonymousLogin()
  {
    String sessionId = null;
    try
    {
      sessionId = Facade.loginAnonymous(new Locale[] { CommonProperties.getDefaultLocale() });
    }
    catch (Throwable ex)
    {
      Assert.fail(ex.getMessage());
    }

    Facade.logout(sessionId);
  }

  /**
   * Tests logging in with the anonymous user.
   */
  @Request
  @Test
  public void testValidChangLogin()
  {
    String sessionId = Facade.loginAnonymous(new Locale[] { CommonProperties.getDefaultLocale() });
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());

    try
    {
      Facade.changeLogin(sessionId, username1, password1);
      writeTypePermissions(sessionId);
    }
    catch (Throwable ex)
    {
      Assert.fail(ex.getMessage());
    }

    Facade.logout(sessionId);
  }

  /**
   * Tests logging in with the anonymous user.
   */
  @Request
  @Test
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

      Assert.fail("Able to change the user on a give session without providing the proper password.");
    }
    catch (Exception ex)
    {
      // We want to land here.
    }
  }

  /**
   * Tests passing in an invalid session oid.
   */
  @Request
  @Test
  public void testInvalidSessionId()
  {
    String sessionId = "Blah Blah Blah";
    try
    {
      Facade.changeLogin(sessionId, username1, password1);
      Assert.fail("An invalid session oid was accepted.");
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
        Assert.fail(e.getMessage());
      }
    }
  }

  @Request
  @Test
  public void testLock()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());

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
    Business test = Business.get(businessDAO.getOid());

    if (test.getLockedBy() != null)
    {
      test.unlock();
    }

    try
    {
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();

      Assert.fail("Able to update a Business without acquiring the lock");
    }
    catch (LockException e)
    {
      // This is expected
    }
  }

  @Request(RequestType.SESSION)
  public static void invalidUnlockTest(String sessionId)
  {
    Business test = Business.get(businessDAO.getOid());
    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.unlock();
      test.apply();

      Assert.fail("Able to update a Business without acquiring the lock");
    }
    catch (LockException e)
    {
      // This is Expected
    }
  }

  @Request(RequestType.SESSION)
  public static void validReleaseUserLockTest(String sessionId)
  {
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.releaseUserLock(newUser1.getOid());
    }
    catch (ClientException e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testWriteTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());

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
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write a businessDAO with Type permissions");
    }
  }

  public static void testWriteInheritancePermissions()
  {
    // Give permissions to the super class
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());

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
    Business test = Business.get(businessDAO4.getOid());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write a businessDAO with Type permissions");
    }
  }

  @Request
  @Test
  public void testWriteStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.lock();
      test.setStructValue("testStruct", TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write a with a struct type permissions");
    }
  }

  /**
   * Test to ensure that you can set a value on a standalone struct with
   * adequate permissions.Facade
   * 
   */
  @Request
  @Test
  public void testValidStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeCharacter.getOid());

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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Yahooo");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write a directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot set a value on a standalone struct without
   * adequate permissions.
   * 
   */
  @Request
  @Test
  public void testInvalidStructTypePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "Yahooo");
      test.apply();

      Assert.fail("Able to write a directly to a Struct without adequate permissions.");
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
  @Request
  @Test
  public void testValidStructSetBlobPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeBlob.getOid());

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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable set a blob attribute directly on a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot set a blob on a standalone struct without
   * adequate permissions.
   * 
   */
  @Request
  @Test
  public void testInvalidStructSetBlobPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
      Assert.fail("Able set a blob attribute directly on a Struct without adequate permissions.");
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
  @Request
  @Test
  public void testValidStructAddEnumPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getOid());

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
        Struct test = Struct.get(structDAO.getOid());
        test.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  private static void validStructAddEnumPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to add an enumeration item directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot add an enumeration on a standalone struct
   * without adequate permissions.
   * 
   */
  @Request
  @Test
  public void testInvalidStructAddEnumPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
        Struct test = Struct.get(structDAO.getOid());
        test.clearEnum(mdAttributeEnumeration.definesAttribute());
      }
    }.run();
  }

  @Request(RequestType.SESSION)
  private static void invalidStructAddEnumPermissions(String sessionId)
  {
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      Assert.fail("Able to add an enumeration item directly to a Struct without adequate permissions.");
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
  @Request
  @Test
  public void testValidStructClearAllPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getOid());

    Struct test = Struct.get(structDAO.getOid());
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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to clear all enumerations item directly to a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot clear all enumerations on a standalone
   * struct without adequate permissions.
   * 
   */
  @Request
  @Test
  public void testInvalidStructClearAllPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

    Struct test = Struct.get(structDAO.getOid());
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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
      Assert.fail("Able to clear all enumerations item directly to a Struct without adequate permissions.");
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
  @Request
  @Test
  public void testValidStructRemoveItemPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdStruct.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeEnumeration.getOid());

    Struct test = Struct.get(structDAO.getOid());
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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to remove an enumeration item directly from a Struct with adequate permissions.");
    }
  }

  /**
   * Test to ensure that you cannot remove an enumeration from a standalone
   * struct without adequate permissions.
   * 
   */
  @Request
  @Test
  public void testInvalidStructRemoveItemPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

    Struct test = Struct.get(structDAO.getOid());
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
    Struct test = Struct.get(structDAO.getOid());

    try
    {
      test.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      Assert.fail("Able to remove an enumeration item directly from a Struct without adequate permissions.");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here
    }
  }

  @Request
  @Test
  public void testWriteStructOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you to set a value on a struct that was
   * obtained from an entity.
   */
  @Request
  @Test
  public void testWriteStructOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "HelloASD");
      test.apply();
      Assert.fail("Able to write on a generic struct retrieved from a business entity with inadequate permissions");
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
  @Request
  @Test
  public void testStructSetBlobOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to set a blob on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from setting a blob value on a Struct
   * obtained from a Entity.
   */
  @Request
  @Test
  public void testStructSetBlobOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.setBlob(mdAttributeBlob.definesAttribute(), new byte[0]);
      test.apply();
      Assert.fail("Able to set a blob on a generic struct retrieved from a business entity without adequate permissions");
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
  @Request
  @Test
  public void testStructAddItemOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to an enumeration item to an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if inadequate permissions permit you to add an enumeration item on a
   * Struct obtained from a Entity.
   */
  @Request
  @Test
  public void testStructAddItemOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.addEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      Assert.fail("Able to an enumeration item to an attribute on a generic struct retrieved from a business entity with inadequate permissions");
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
  @Request
  @Test
  public void testStructClearItemsOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

    Business test = Business.get(businessDAO.getOid());
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
        Business test = Business.get(businessDAO.getOid());
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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to clear enumeration items from an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from removing all enumeration items from a
   * Struct obtained from a Entity.
   */
  @Request
  @Test
  public void testStructClearItemsOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

    Business test = Business.get(businessDAO.getOid());
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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.clearEnum(mdAttributeEnumeration.definesAttribute());
      test.apply();
      Assert.fail("Able to clear enumeration items from an attribute on a generic struct retrieved from a business entityout with adequate permissions");
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
  @Request
  @Test
  public void testStructRemoveItemOnStructPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeStruct.getOid());

    Business test = Business.get(businessDAO.getOid());
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
        Business test = Business.get(businessDAO.getOid());
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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to remove an enumeration item from an attribute on a generic struct retrieved from a business entity with adequate permissions");
    }
  }

  /**
   * Tests if permissions prevent you from removing an enumeration item from a
   * Struct obtained from a Entity.
   */
  @Request
  @Test
  public void testStructRemoveItemOnStructPermissionsFail()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

    Business test = Business.get(businessDAO.getOid());
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
    Business test = Business.get(businessDAO.getOid());
    Struct struct = test.getGenericStruct("testStruct");

    try
    {
      test.lock();
      struct.removeEnumItem(mdAttributeEnumeration.definesAttribute(), enumItemId1);
      test.apply();
      Assert.fail("Able to remove an enumeration item from an attribute on a generic struct retrieved from a business entity without adequate permissions");
    }
    catch (AttributeWritePermissionException e)
    {
      // We want to land here.
    }
  }

  @Request
  @Test
  public void testInvalidWritePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());

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
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
      test.unlock();

      Assert.fail("Able to write a with an attribute with WRITE permissions only on the defining MdBusiness");
    }
    catch (AttributeWritePermissionException e)
    {
      // This is expected
    }
    finally
    {
      if (test.getValue(ElementInfo.LOCKED_BY).equals(newUser1.getOid()))
      {
        test.releaseUserLock(newUser1.getOid());
      }
    }
  }

  @Request
  @Test
  public void testInvalidWritePermissions2()
  {
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());

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
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.lock();
      test.setValue(mdAttribute.definesAttribute(), MdAttributeBooleanInfo.TRUE);
      test.apply();
      test.unlock();

      Assert.fail("Able to write a with a WRITE permissions on MdAttribute, but not the defining MdBusiness");
    }
    catch (WritePermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testTypeAddChildPermissions()
  {
    newUser1.grantPermission(Operation.ADD_CHILD, mdRelationship.getOid());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    Business parent = Business.get(businessDAO.getOid());

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
    Business child = Business.get(businessDAO2.getOid());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to add a child with type ADD_CHILD permissions. \n" + e.getMessage());
    }

    rel.delete();
  }

  @Request
  @Test
  public void testInvalidTypeAddChildPermissions()
  {
    Business parent = Business.get(businessDAO.getOid());

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
    Business child = Business.get(businessDAO2.getOid());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
      Assert.fail("Able to add a child without type ADD_CHILD permissions. \n");
    }
    catch (Throwable e)
    {
      if (!e.getMessage().contains(AddChildPermissionException.class.getName()))
      {
        // This is expected
      }
    }
  }

  @Request
  @Test
  public void testTypeWriteChildPermissions()
  {
    newUser1.grantPermission(Operation.WRITE_CHILD, mdRelationship.getOid());
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getOid());

    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeWriteChildPermissions(sessionId, relationship.getOid());
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
      Assert.fail("Unable to modify a child with type WRITE_CHILD permissions. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidWriteRelationshipPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getOid());

    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      invalidWriteRelationsPermissions(sessionId, relationship.getOid());
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
      Assert.fail("Able to modify a child without type WRITE_CHILD permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  @Request
  @Test
  public void testTypeWriteRelationshipPermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdRelationship.getOid());
    newUser1.grantPermission(Operation.WRITE, relMdAttribute.getOid());

    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      typeWriteRelationshipPermissions(sessionId, relationship.getOid());
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
      Assert.fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testTypeDeleteAllChildrenPermissions()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove children with the DELETE_CHILD permission. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testTypeDeleteChildPermission()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove a given relationship with the DELETE_CHILD permission. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testTypeDeleteAllChildrenPermissions_missingDeleteChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Able to remove a child relationship with the DELETE permission");
    }
  }

  @Request
  @Test
  public void testTypeDeleteChildPermission_missingDeleteChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove a child relationship without the required DELETE permission");
    }
  }

  @Request
  @Test
  public void testTypeDeleteAllParentPermissions()
  {
    newUser1.grantPermission(Operation.DELETE_PARENT, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove children with the DELETE_PARENT permission. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testTypeDeleteParentPermission()
  {
    newUser1.grantPermission(Operation.DELETE_PARENT, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject2.getOid());

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
      Assert.fail("Unable to remove a given relationship with the DELETE_PARENT permission. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testTypeDeleteAllParentsPermissions_missingParentChild()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove a parent relationship with DELETE permission");
    }
  }

  @Request
  @Test
  public void testTypeDeleteParentPermission_missingDeleteParent()
  {
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Unable to remove a child relationship with DELETE permission");
    }
  }

  @Request
  @Test
  public void testInvalidTypeDeleteParentPermission_missingDelete()
  {
    newUser1.grantPermission(Operation.DELETE_CHILD, mdRelationship.getOid());

    // Create the relationship
    final Business parent = Business.get(businessDAO.getOid());
    final Business child = Business.get(deleteObject2.getOid());

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
      Assert.fail("Able to remove a parent relationship without the required DELETE permission");
    }
    catch (DeleteParentPermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testOwnerAddChildPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.ADD_CHILD, mdRelationship.getOid());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    Business parent = Business.get(businessDAO.getOid());

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
    Business child = Business.get(businessDAO2.getOid());

    Relationship rel = null;

    try
    {
      rel = parent.addChild(child, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to add a child with type ADD_CHILD permissions");
    }

    rel.delete();
  }

  @Request
  @Test
  public void testOwnerWriteChildPermissions()
  {
    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE, relMdAttribute.getOid());
    role.grantPermission(Operation.DELETE, mdRelationship.getOid());

    role.grantPermission(Operation.ADD_CHILD, mdRelationship.getOid());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = ownerWriteChildPermissions_CreateRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    role.revokePermission(Operation.ADD_CHILD, mdRelationship.getOid());
    role.grantPermission(Operation.WRITE_CHILD, mdRelationship.getOid());

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
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    return relationship.getOid();
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
      Assert.fail("Unable to modify a child with owner WRITE permissions. \n" + e.getMessage());
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
  @Request
  @Test
  public void testOwnerWriteChildRelationshipPermissions()
  {
    newUser2.grantPermission(Operation.CREATE, mdRelationship.getOid());

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE_CHILD, mdRelationship.getOid());
    role.grantPermission(Operation.WRITE_CHILD, relMdAttribute.getOid());
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
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    return relationship.getOid();
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
      Assert.fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerWriteChildPermissions()
  {
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerInvalidWriteChildPermissions(sessionId, relationship.getOid());
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
      Assert.fail("Able to modify a child without owner WRITE_CHILD permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  @Request
  @Test
  public void testOwnerDeleteChildPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.DELETE_CHILD, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(deleteObject2.getOid());

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
      Assert.fail("Owner is unable to delete child with required DELETE_CHILD permission");
    }
  }

  @Request
  @Test
  public void testInvalidOwnerDeleteChildPermissions()
  {

    // Create the relationship
    final Business parent = Business.get(businessDAO.getOid());
    final Business child = Business.get(deleteObject1.getOid());

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
      Assert.fail("Owner able to delete child without required DELETE_CHILD permission");
    }
    catch (DeleteChildPermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testAddChildPermissions2()
  {
    newUser1.grantPermission(Operation.CREATE, mdRelationship.getOid());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      Business parent = Business.get(businessDAO.getOid());
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
    Business child = Business.get(businessDAO2.getOid());

    try
    {
      parent.addChild(child, mdRelationship.definesType()).apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to add a child with CREATE permissions on the MdRelationship, but no ADD_CHILD permissions on the MdBusiness");
    }
  }

  @Request
  @Test
  public void testOwnerAddParentPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.ADD_PARENT, mdRelationship.getOid());
    newUser1.grantPermission(Operation.DELETE, mdRelationship.getOid());

    Business child = Business.get(businessDAO.getOid());

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
    Business parent = Business.get(businessDAO2.getOid());
    Relationship rel = null;

    try
    {
      rel = child.addParent(parent, mdRelationship.definesType());
      rel.apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to add a parent with type ADD_PARENT permissions");
    }

    rel.delete();
  }

  @Request
  @Test
  public void testInvalidOwnerAddParentPermissions()
  {
    Business child = Business.get(businessDAO.getOid());

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
    Business parent = Business.get(businessDAO2.getOid());
    Relationship rel = null;

    try
    {
      rel = child.addParent(parent, mdRelationship.definesType());
      rel.apply();
      Assert.fail("Able to add a parent with type ADD_PARENT permissions");
    }
    catch (AddParentPermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testOwnerWriteAddPermissions()
  {
    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE, relMdAttribute.getOid());
    role.grantPermission(Operation.DELETE, mdRelationship.getOid());

    role.grantPermission(Operation.ADD_PARENT, mdRelationship.getOid());
    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      relationshipId = ownerWriteParentPermissions_CreateRelationship(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }

    role.revokePermission(Operation.ADD_PARENT, mdRelationship.getOid());
    role.grantPermission(Operation.WRITE_PARENT, mdRelationship.getOid());

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
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    return relationship.getOid();
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
      Assert.fail("Unable to modify a child with owner WRITE permissions. \n" + e.getMessage());
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
  @Request
  @Test
  public void testOwnerWriteParentRelationshipPermissions()
  {
    newUser2.grantPermission(Operation.CREATE, mdRelationship.getOid());

    String relationshipId = null;
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    role.grantPermission(Operation.WRITE_PARENT, mdRelationship.getOid());
    role.grantPermission(Operation.WRITE_PARENT, relMdAttribute.getOid());

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
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    return relationship.getOid();
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
      Assert.fail("Unable to modify a child with type WRITE permissions. \n" + e.getMessage());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerWriteParentPermissions()
  {
    Business parent = Business.get(businessDAO.getOid());
    Business child = Business.get(businessDAO2.getOid());

    Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      ownerInvalidWriteParentPermissions(sessionId, relationship.getOid());
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
      Assert.fail("Able to modify a child without owner WRITE_PARENT permissions.");
    }
    catch (WritePermissionException e)
    {
      // this is expected
    }
  }

  @Request
  @Test
  public void testOwnerDeleteParentPermissions()
  {
    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    role.grantPermission(Operation.DELETE_PARENT, mdRelationship.getOid());

    // Create the relationship
    Business parent = Business.get(businessDAO2.getOid());
    Business child = Business.get(businessDAO.getOid());

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
      Assert.fail("Owner is unable to delete parent with required DELETE_PARENT permission");
    }
  }

  @Request
  @Test
  public void testInvalidOwnerDeleteParentPermissions()
  {
    // Create the relationship
    Business parent = Business.get(businessDAO2.getOid());
    Business child = Business.get(businessDAO.getOid());

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
      Assert.fail("Owner is able to delete parent with required DELETE_PARENT permission");
    }
    catch (DeleteParentPermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testAddParentPermissions2()
  {
    newUser1.grantPermission(Operation.CREATE, mdRelationship.getOid());

    Business child = Business.get(businessDAO.getOid());

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
    Business parent = Business.get(businessDAO2.getOid());

    try
    {
      child.addParent(parent, mdRelationship.definesType()).apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to ADD_PARENT with CREATE permissions on the MdRelationship");
    }
  }

  @Request
  @Test
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
    Business test = Business.get(businessDAO.getOid());

    try
    {
      test.delete();

      Assert.fail("Able to DELETE without permissions");
    }
    catch (DeletePermissionException e)
    {
      // This is expected
    }
  }

  public static void testTypeDeletePermissions()
  {
    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.DELETE, mdBusiness.getOid());

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
    Business test = Business.get(deleteObject1.getOid());

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to delet with Type Permissions");
    }
  }

  public static void testOwnerDeletePermissions()
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    owner.grantPermission(Operation.WRITE, mdBusiness.getOid());
    owner.grantPermission(Operation.DELETE, mdBusiness.getOid());

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
    Business test = Business.get(deleteObject3.getOid());

    try
    {
      test.lock();
      test.delete();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to delete with owner Permissions");
    }
  }

  public static void testRelationshipPermission()
  {
    newUser1.grantPermission(Operation.WRITE, mdRelationship.getOid());

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
    Relationship test = Relationship.get(relationshipDAO.getOid());

    try
    {
      test.lock();
      test.apply();
    }
    catch (PermissionException e)
    {
      Assert.fail("Unable to update a relationship with WRITE permissions on the MdTree");
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
    Relationship test = Relationship.get(relationshipDAO.getOid());

    try
    {
      test.lock();
      test.apply();
      test.unlock();

      Assert.fail("Able to write on a relationship without permissions");

    }
    catch (WritePermissionException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testAttributeDimensionPermissions()
  {
    MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute.getMdAttributeDimension(mdDimension);

    newUser1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttributeDimension.getOid());
    newUser1.grantPermission(Operation.READ, mdAttributeDimension.getOid());

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
    Business test = Business.get(businessDAO4.getOid());

    try
    {
      boolean value = Boolean.parseBoolean(test.getValue(mdAttribute.definesAttribute()));

      test.lock();
      test.setValue(mdAttribute.definesAttribute(), new Boolean(!value).toString());
      test.apply();
    }
    catch (AttributeWritePermissionException e)
    {
      Assert.fail("Unable to write a businessDAO with Type permissions");
    }
  }

  @Request
  @Test
  public void testClassDimensionPermissions()
  {
    MdClassDimensionDAOIF mdClassDimension = mdDimension.getMdClassDimension(mdBusiness);

    newUser1.grantPermission(Operation.WRITE, mdClassDimension.getOid());
    newUser1.grantPermission(Operation.READ, mdClassDimension.getOid());
    newUser1.grantPermission(Operation.WRITE, mdAttribute.getOid());
    newUser1.grantPermission(Operation.READ, mdAttribute.getOid());

    String sessionId = Facade.login(username1, password1, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionFacade.setDimension(mdDimension.getKey(), sessionId);

    try
    {
      classDimensionPermissions(sessionId);
    }
    catch (Exception e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  @Request(RequestType.SESSION)
  public static void classDimensionPermissions(String sessionId)
  {
    Business test = Business.get(businessDAO4.getOid());

    boolean value = Boolean.parseBoolean(test.getValue(mdAttribute.definesAttribute()));

    test.lock();
    test.setValue(mdAttribute.definesAttribute(), new Boolean(!value).toString());
    test.apply();
  }

  @Request
  @Test
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

        superRole.grantPermission(Operation.CREATE, mdBusiness.getOid());
        subRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());

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

  @Request
  @Test
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

        superRole.grantPermission(Operation.DENY_CREATE, mdBusiness.getOid());
        subRole.grantPermission(Operation.CREATE, mdBusiness.getOid());

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

  @Request
  @Test
  public void testSingleActorPermissions()
  {
    newActor1.grantPermission(Operation.WRITE, mdBusiness.getOid());
    newActor1.grantPermission(Operation.WRITE, mdAttribute.getOid());

    String sessionId = SessionFacade.logIn(newActor1, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      writeTypePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }
}
