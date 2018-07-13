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

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.DomainErrorException;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.Relationship;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAO;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDimensionDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeConcreteDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdDimensionDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAO;
import com.runwaysdk.dataaccess.metadata.TypeTupleDAOIF;
import com.runwaysdk.facade.Facade;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.BusinessDAOQuery;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.query.QueryFactory;

public class IntegratedMethodTest
{
  /**
   * The test user object, used to set and remove permissions for the user
   */
  private static UserDAO                   newUser;

  /**
   * The test methodActor which corresponds to the createMdMethod
   */
  private static MethodActorDAO            createMethod;

  /**
   * The test methodActor which corresponds to the deleteMdMethod
   */
  private static MethodActorDAO            deleteMethod;

  /**
   * The test methodActor which corresponds to the writeMdMethod
   */
  private static MethodActorDAO            writeMethod;

  /**
   * The test methodActor which corresponds to the promoteMdMethod
   */
  private static MethodActorDAO            promoteMethod;

  /**
   * The test methodActor which corresponds to the addChildMdMethod
   */
  private static MethodActorDAO            addChildMethod;

  /**
   * The test methodActor which corresponds to the addParentMdMethod
   */
  private static MethodActorDAO            addParentMethod;

  /**
   * The test methodActor which corresponds to the deleteChildMdMethod
   */
  private static MethodActorDAO            deleteChildMethod;

  /**
   * The test methodActor which corresponds to the deleteParentMdMehtod
   */
  private static MethodActorDAO            deleteParentMethod;

  /**
   * A test static MdMethod which creates a new MdBusiness
   */
  private static MdMethodDAO               createMdMethod;

  /**
   * A test static MdMethod which deletes a existing MdBusiness
   */
  private static MdMethodDAO               deleteMdMethod;

  /**
   * A test static method which writes to a existing MdBusiness
   */
  private static MdMethodDAO               writeMdMethod;

  /**
   * A test static method which promotes to a existing MdBusiness
   */
  private static MdMethodDAO               promoteMdMethod;

  /**
   * A test method which adds a child to an existing MdBusiness
   */
  private static MdMethodDAO               addChildMdMethod;

  /**
   * A test method which adds a parent to an existing MdBusiness
   */
  private static MdMethodDAO               addParentMdMethod;

  /**
   * A test method which deletes a child from an existing MdBusiness
   */
  private static MdMethodDAO               deleteChildMdMethod;

  /**
   * A test method which deletes a parent from an existing MdBusiness
   */
  private static MdMethodDAO               deleteParentMdMethod;

  /**
   * A test method which modifies a relationship instance.
   */
  private static MdMethodDAO               modifyRelationshipMdMethod;

  /**
   * The test methodActor which corresponds to the modifyRelationshipMdMethod
   */
  private static MethodActorDAO            modifyRelationshipMethodActor;

  /**
   * The test MdBusiness data object
   */
  private static MdBusinessDAO             mdBusiness;

  /**
   * The Business object.
   */
  private static Business                  business1;

  /**
   * The Business object.
   */
  private static Business                  business2;

  /**
   * The test MdBusiness that extends enumeration master.
   */
  private static MdBusinessDAO             enumMasterMdBusiness;

  /**
   * The test MdEnumeration data object
   */
  private static MdEnumerationDAO          structMdEnumeration;

  /**
   * The test mdStruct for structs
   */
  private static MdStructDAO               mdStruct;

  /**
   * The test mdRelationshiop
   */
  private static MdRelationshipDAO         mdRelationship;

  /**
   * The test MdAttribute of the MdBusiness
   */
  private static MdAttributeConcreteDAO    mdAttribute1;

  /**
   * The test MdAttribute of the MdBusiness
   */
  private static MdAttributeConcreteDAO    mdAttribute2;

  /**
   * The test MdAttribute Struct of MdBusiness
   */
  private static MdAttributeConcreteDAO    mdAttributeStruct;

  /**
   * Enumeration attribute on the struct.
   */
  private static MdAttributeEnumerationDAO mdAttributeEnumeration;

  /**
   * The test mdAttribute of mdStruct
   */
  private static MdAttributeConcreteDAO    mdAttributeCharacter;

  /**
   * The test mdAttributeBlob of mdStruct
   */
  private static MdAttributeBlobDAO        mdAttributeBlob;

  /**
   * The test state-mdAttribute tuple
   */
  private static TypeTupleDAO              typeTuple1;

  /**
   * The test state-mdRelationship tuple
   */
  private static TypeTupleDAO              typeTuple2;

  /**
   * The test state1 of the MdBusiness StateMachine
   */
  private static StateMasterDAO            state1;

  /**
   * The test state2 of the MdBusiness StateMachine
   */
  private static StateMasterDAO            state2;

  /**
   * The test state3 of the MdBusiness StateMachine
   */
  private static StateMasterDAO            state3;

  private static MdDimensionDAO            mdDimension;

  /**
   * The username for the user
   */
  private final static String              username     = "smethie";

  /**
   * The password for the user
   */
  private final static String              password     = "1234";

  /**
   * The maximum number of sessions for the user
   */
  private final static int                 sessionLimit = 2;

  private static boolean                   setup;

  /**
   * The setup done before the test suite is run
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    setup = false;

    // Create a new user
    newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, username);
    newUser.setValue(UserInfo.PASSWORD, password);
    newUser.setValue(UserInfo.SESSION_LIMIT, Integer.toString(sessionLimit));
    newUser.apply();

    mdDimension = TestFixtureFactory.createMdDimension();
    mdDimension.apply();

    // Create an enumeration master class
    enumMasterMdBusiness = MdBusinessDAO.newInstance();
    enumMasterMdBusiness.setValue(MdBusinessInfo.NAME, "EnumMasterTest1");
    enumMasterMdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.session");
    enumMasterMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Master");
    enumMasterMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Master");
    enumMasterMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    enumMasterMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    enumMasterMdBusiness.getAttribute(MdBusinessInfo.OWNER).setValue(newUser.getId());
    enumMasterMdBusiness.apply();

    MdAttributeCharacterDAO mdAttributeCharacter1 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.NAME, "testEnumCharacter");
    mdAttributeCharacter1.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testEnumCharacter");
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, enumMasterMdBusiness.getId());
    mdAttributeCharacter1.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter1.apply();

    structMdEnumeration = MdEnumerationDAO.newInstance();
    structMdEnumeration.setValue(MdEnumerationInfo.NAME, "StructMdEnumeration");
    structMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, "test.session");
    structMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StructMdEnumeration");
    structMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    structMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, enumMasterMdBusiness.getId());
    structMdEnumeration.apply();

    // Create a new MdStruct
    mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.NAME, "Basic1");
    mdStruct.setValue(MdStructInfo.PACKAGE, "test.session");
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdStruct.getAttribute(MdStructInfo.OWNER).setValue(newUser.getId());
    mdStruct.apply();

    // Crate a new MdAttribute for the MdStruct
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdStruct.getId());
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdAttributeCharacter.apply();

    mdAttributeBlob = MdAttributeBlobDAO.newInstance();
    mdAttributeBlob.setValue(MdAttributeBlobInfo.NAME, "testBasicBlob");
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enum Blob");
    mdAttributeBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, mdStruct.getId());
    mdAttributeBlob.apply();

    // Create a new MdBusiness
    mdBusiness = MdBusinessDAO.newInstance();
    mdBusiness.setValue(MdBusinessInfo.NAME, "Class1");
    mdBusiness.setValue(MdBusinessInfo.PACKAGE, "test.session");
    mdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Class");
    mdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Class");
    mdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdBusiness.setValue(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
    mdBusiness.getAttribute(MdBusinessInfo.OWNER).setValue(newUser.getId());
    mdBusiness.apply();

    // Create an MdAttribute on the MdBusiness
    mdAttribute1 = MdAttributeBooleanDAO.newInstance();
    mdAttribute1.setValue(MdAttributeBooleanInfo.NAME, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute1.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttribute1.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute1.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttribute1.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttribute1.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttribute1.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttribute1.apply();

    mdAttributeStruct = MdAttributeStructDAO.newInstance();
    mdAttributeStruct.setValue(MdAttributeStructInfo.NAME, "testStruct");
    mdAttributeStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "testStruct");
    mdAttributeStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, mdBusiness.getId());
    mdAttributeStruct.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getId());
    mdAttributeStruct.apply();

    mdAttributeEnumeration = MdAttributeEnumerationDAO.newInstance();
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.NAME, "structEnumeration");
    mdAttributeEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Enumeration");
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdStruct.getId());
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, structMdEnumeration.getId());
    mdAttributeEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnumeration.apply();

    business1 = new Business(mdBusiness.definesType());
    business1.apply();

    business2 = new Business(mdBusiness.definesType());
    business2.apply();

    createMdMethod = MdMethodDAO.newInstance();
    createMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    createMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    createMdMethod.setValue(MdMethodInfo.NAME, "testCreate");
    createMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    createMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    createMdMethod.apply();

    deleteMdMethod = MdMethodDAO.newInstance();
    deleteMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    deleteMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    deleteMdMethod.setValue(MdMethodInfo.NAME, "testDelete");
    deleteMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    deleteMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    deleteMdMethod.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, deleteMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    writeMdMethod = MdMethodDAO.newInstance();
    writeMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    writeMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    writeMdMethod.setValue(MdMethodInfo.NAME, "testWrite");
    writeMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    writeMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    writeMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, writeMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    promoteMdMethod = MdMethodDAO.newInstance();
    promoteMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    promoteMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    promoteMdMethod.setValue(MdMethodInfo.NAME, "testPromote");
    promoteMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    promoteMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Promote");
    promoteMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, promoteMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    addChildMdMethod = MdMethodDAO.newInstance();
    addChildMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    addChildMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    addChildMdMethod.setValue(MdMethodInfo.NAME, "testAddChild");
    addChildMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    addChildMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test AddChild");
    addChildMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, addChildMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    addParentMdMethod = MdMethodDAO.newInstance();
    addParentMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    addParentMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    addParentMdMethod.setValue(MdMethodInfo.NAME, "testAddParent");
    addParentMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    addParentMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Add Parent");
    addParentMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, addParentMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    deleteChildMdMethod = MdMethodDAO.newInstance();
    deleteChildMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    deleteChildMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    deleteChildMdMethod.setValue(MdMethodInfo.NAME, "testDeleteChild");
    deleteChildMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    deleteChildMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test AddChild");
    deleteChildMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, deleteChildMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    deleteParentMdMethod = MdMethodDAO.newInstance();
    deleteParentMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    deleteParentMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    deleteParentMdMethod.setValue(MdMethodInfo.NAME, "testDeleteParent");
    deleteParentMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    deleteParentMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Delete Parent");
    deleteParentMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdBusiness.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "class1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Class1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, deleteParentMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    // Create a StateMachine for the MdBusiness
    MdStateMachineDAO mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.NAME, "StateMachine1");
    mdState.setValue(MdStateMachineInfo.PACKAGE, "test.session");
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "StateMachine1");
    mdState.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, mdBusiness.getId());
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
    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, "Relationship1");
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, "test.session");
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);

    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TEST class");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "TestParent");

    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, mdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test child class");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "TestChild");
    mdRelationship.apply();

    modifyRelationshipMdMethod = MdMethodDAO.newInstance();
    modifyRelationshipMdMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    modifyRelationshipMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdBusiness.getId());
    modifyRelationshipMdMethod.setValue(MdMethodInfo.NAME, "testWriteRelationship");
    modifyRelationshipMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    modifyRelationshipMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Modify Relationship");
    modifyRelationshipMdMethod.apply();

    mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, mdRelationship.definesType());
    mdParameter.setValue(MdParameterInfo.NAME, "relationship1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship1 Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, modifyRelationshipMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.apply();

    // Create an MdAttribute on the MdBusiness
    mdAttribute2 = MdAttributeBooleanDAO.newInstance();
    mdAttribute2.setValue(MdAttributeBooleanInfo.NAME, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute2.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.FALSE);
    mdAttribute2.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttribute2.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttribute2.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttribute2.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttribute2.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    mdAttribute2.apply();

    typeTuple1 = TypeTupleDAO.newInstance();
    typeTuple1.setStateMaster(state1.getId());
    typeTuple1.setMetaData(mdAttribute1.getId());
    typeTuple1.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, state1.getName() + mdAttribute1.getDisplayLabel(CommonProperties.getDefaultLocale()));
    typeTuple1.apply();

    typeTuple2 = TypeTupleDAO.newInstance();
    typeTuple2.setStateMaster(state1.getId());
    typeTuple2.setMetaData(mdRelationship.getId());
    typeTuple2.setStructValue(TypeTupleDAOIF.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, state1.getName() + mdRelationship.getDisplayLabel(CommonProperties.getDefaultLocale()));
    typeTuple2.apply();

    createMethod = MethodActorDAO.newInstance();
    createMethod.setValue(MethodActorInfo.MD_METHOD, createMdMethod.getId());
    createMethod.apply();

    deleteMethod = MethodActorDAO.newInstance();
    deleteMethod.setValue(MethodActorInfo.MD_METHOD, deleteMdMethod.getId());
    deleteMethod.apply();

    writeMethod = MethodActorDAO.newInstance();
    writeMethod.setValue(MethodActorInfo.MD_METHOD, writeMdMethod.getId());
    writeMethod.apply();

    promoteMethod = MethodActorDAO.newInstance();
    promoteMethod.setValue(MethodActorInfo.MD_METHOD, promoteMdMethod.getId());
    promoteMethod.apply();

    addChildMethod = MethodActorDAO.newInstance();
    addChildMethod.setValue(MethodActorInfo.MD_METHOD, addChildMdMethod.getId());
    addChildMethod.apply();

    addParentMethod = MethodActorDAO.newInstance();
    addParentMethod.setValue(MethodActorInfo.MD_METHOD, addParentMdMethod.getId());
    addParentMethod.apply();

    deleteChildMethod = MethodActorDAO.newInstance();
    deleteChildMethod.setValue(MethodActorInfo.MD_METHOD, deleteChildMdMethod.getId());
    deleteChildMethod.apply();

    deleteParentMethod = MethodActorDAO.newInstance();
    deleteParentMethod.setValue(MethodActorInfo.MD_METHOD, deleteParentMdMethod.getId());
    deleteParentMethod.apply();

    modifyRelationshipMethodActor = MethodActorDAO.newInstance();
    modifyRelationshipMethodActor.setValue(MethodActorInfo.MD_METHOD, modifyRelationshipMdMethod.getId());
    modifyRelationshipMethodActor.apply();
  }

  /**
   * The tear down done after all the test in the test suite have run
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    // Write the new stub, and compile to make sure it's valid
    String stubSource = getEmptyStub();

    MdBusinessDAO mdBusinessUpdate = MdBusinessDAO.get(mdBusiness.getId()).getBusinessDAO();
    mdBusinessUpdate.setValue(MdBusinessInfo.STUB_SOURCE, stubSource);
    mdBusinessUpdate.apply();

    QueryFactory queryFactory = new QueryFactory();

    BusinessDAOQuery q = queryFactory.businessDAOQuery(mdBusiness.definesType());
    OIterator<BusinessDAOIF> i = q.getIterator();
    for (BusinessDAOIF b : i)
    {
      b.getBusinessDAO().delete();
    }

    TestFixtureFactory.delete(createMethod);
    TestFixtureFactory.delete(deleteMethod);
    TestFixtureFactory.delete(writeMethod);
    TestFixtureFactory.delete(promoteMethod);
    TestFixtureFactory.delete(addChildMethod);
    TestFixtureFactory.delete(addParentMethod);
    TestFixtureFactory.delete(deleteChildMethod);
    TestFixtureFactory.delete(deleteParentMethod);

    TestFixtureFactory.delete(createMdMethod);
    TestFixtureFactory.delete(deleteMdMethod);
    TestFixtureFactory.delete(writeMdMethod);
    TestFixtureFactory.delete(promoteMdMethod);
    TestFixtureFactory.delete(addChildMdMethod);
    TestFixtureFactory.delete(addParentMdMethod);
    TestFixtureFactory.delete(deleteChildMdMethod);
    TestFixtureFactory.delete(deleteParentMdMethod);
    TestFixtureFactory.delete(modifyRelationshipMdMethod);

    TestFixtureFactory.delete(typeTuple1);
    TestFixtureFactory.delete(typeTuple2);
    TestFixtureFactory.delete(enumMasterMdBusiness);
    TestFixtureFactory.delete(mdRelationship);
    TestFixtureFactory.delete(mdBusiness);
    TestFixtureFactory.delete(mdStruct);
    TestFixtureFactory.delete(newUser);
    TestFixtureFactory.delete(mdDimension);
  }

  /**
   * No setup needed non-Javadoc)
   * 
   * @see junit.framework.TestCase#setUp()
   */
  @Request
  @Before
  protected void setUp() throws Exception
  {
    if (!setup)
    {
      String stubSource = getMethodStub();

      // Write the new stub, and compile to make sure it's valid
      MdBusinessDAO mdBusinessUpdate = MdBusinessDAO.get(mdBusiness.getId()).getBusinessDAO();
      mdBusinessUpdate.setValue(MdBusinessInfo.STUB_SOURCE, stubSource);
      mdBusinessUpdate.apply();

      setup = true;
    }
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
    for (RelationshipDAOIF reference : newUser.getAllPermissions())
    {
      // Revoke any type permissions given to newUser
      newUser.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : createMethod.getAllPermissions())
    {
      // Revoke any type permissions given to newUser
      createMethod.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : deleteMethod.getAllPermissions())
    {
      // Revoke any type permissions given to newUser
      deleteMethod.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : promoteMethod.getAllPermissions())
    {
      promoteMethod.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : addParentMethod.getAllPermissions())
    {
      addParentMethod.revokeAllPermissions(reference.getChildId());

    }

    for (RelationshipDAOIF reference : addChildMethod.getAllPermissions())
    {
      addChildMethod.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : deleteChildMethod.getAllPermissions())
    {
      deleteChildMethod.revokeAllPermissions(reference.getChildId());
    }

    for (RelationshipDAOIF reference : deleteParentMethod.getAllPermissions())
    {
      deleteParentMethod.revokeAllPermissions(reference.getChildId());
    }

    RoleDAO role = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();

    for (RelationshipDAOIF reference : role.getAllPermissions())
    {
      // Revoke any businessDAO permissions given to newUser
      role.revokeAllPermissions(reference.getChildId());
    }

    // Clear any lingering sessions
    SessionFacade.clearSessions();
  }

  /**
   * Ensure that an TypePermissionException is thrown when a User attempts to
   * execute a method without EXECUTE permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoExecutePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noExecutePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noExecutePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business business = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(business);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testWrite", c).invoke(business, input);
      Assert.fail("Failed to thrown an exception when executing a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a TypePermissionException is being thrown as the root
      // exception
      Assert.assertEquals(ExecuteInstancePermissionException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * create a new object without any CREATE permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoCreatePermissions() throws Exception
  {
    newUser.grantPermission(Operation.EXECUTE, createMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noCreatePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noCreatePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());

    try
    {
      c.getMethod("testCreate").invoke(null);
      Assert.fail("Failed to throw an exception when creating a object in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is the root execption
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test create a new object in a MdMethod where the MdMethod has CREATE
   * permissions on the MdBusiness of the object.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testCreatePermissions() throws Exception
  {
    newUser.grantPermission(Operation.EXECUTE, createMdMethod.getId());
    createMethod.grantPermission(Operation.CREATE, mdBusiness.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    createPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void createPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());

    try
    {
      c.getMethod("testCreate").invoke(null);
    }
    catch (InvocationTargetException e)
    {
      Assert.fail("Unable to create an object with CREATE permissions on the MdBusiness");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * delete a object without any DELETE permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoDeletePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noDeletePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noDeletePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testDelete", c).invoke(null, input);
      Assert.fail("Failed to throw an exception when deleting a object in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is the root execption
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test delete a object in a MdMethod where the MdMethod has DELETE
   * permissions on the MdBusiness of the object.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testDeletePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteMdMethod.getId());
    deleteMethod.grantPermission(Operation.DELETE, mdBusiness.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    deletePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void deletePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testDelete", c).invoke(null, input);
    }
    catch (InvocationTargetException e)
    {
      Assert.fail("Unable to delete an object with DELETE permissions on the MdBusiness");
    }
  }

  /**
   * Test delete a new object in a MdMethod where the MdMethod has DELETE
   * permissions on the MdBusiness of the object.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateDeletePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteMdMethod.getId());
    deleteMethod.grantPermission(Operation.DELETE, state1.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateDeletePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateDeletePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testDelete", c).invoke(null, input);
    }
    catch (InvocationTargetException e)
    {
      Assert.fail("Unable to delete an object with DELETE permissions on StateIF");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * write an object without any WRITE permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoWritePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, writeMdMethod.getId());

    writeMethod.revokeAllPermissions(state1.getId());
    writeMethod.revokeAllPermissions(mdBusiness.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noWritePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noWritePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business business = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(business);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testWrite", c).invoke(business, input);

      Assert.fail("Failed to throw an exception when writing a object in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test writing an object in a MdMethod where the MdMethod has write
   * permissions on the MdBusiness of the object. Additionally ensure that
   * EXECUTE permissions on the OWNER role work.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testWritePermissions() throws Exception
  {
    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    owner.grantPermission(Operation.EXECUTE, writeMdMethod.getId());

    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    writeMethod.grantPermission(Operation.WRITE, mdBusiness.getId());
    writeMethod.grantPermission(Operation.WRITE, mdAttribute1.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    writePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void writePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business business = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(business);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testWrite", c).invoke(business, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to write an object with WRITE permissions on the MdBusiness");
    }
  }

  /**
   * Test writing an object in a MdMethod where the MdMethod only has write
   * permissions on the StateIF of the object.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateWritePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, writeMdMethod.getId());
    writeMethod.grantPermission(Operation.WRITE, state1.getId());
    writeMethod.grantPermission(Operation.WRITE, typeTuple1.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateWritePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateWritePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business business = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(business);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testWrite", c).invoke(business, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to write an object with WRITE permissions on StateIF");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * promote an object without any WRITE permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoPromotePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, promoteMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noPromotePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noPromotePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testPromote", c).invoke(null, input);

      Assert.fail("Failed to throw an exception when promoting an object in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test promoting an object in a MdMethod where the MdMethod only has promote
   * permissions on the StateIF of the object.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testPromotePermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, promoteMdMethod.getId());
    promoteMethod.grantPermission(Operation.PROMOTE, state2.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    promotePermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void promotePermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testPromote", c).invoke(null, input);
    }
    catch (InvocationTargetException e)
    {
      Assert.fail("Unable to promote an object with PROMOTE permissions on StateIF");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * add a child to an object without any ADD_CHILD permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoAddChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addChildMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noAddChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noAddChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddChild", c).invoke(object, input);

      Assert.fail("Failed to throw an exception when adding a child in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test adding a child to an object in a MdMethod where the MdMethod only has
   * ADD_CHILD permissions on the MdBusiness.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testAddChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addChildMdMethod.getId());
    addChildMethod.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    addChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void addChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddChild", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      Assert.fail("Unable to add a child with ADD_CHILD permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test modifying to a relationship instance in a MdMethod where the MdMethod
   * only has WRITE_CHILD permissions on the MdRelationship.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testWriteChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, modifyRelationshipMdMethod.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE_CHILD, mdRelationship.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE_CHILD, mdAttribute2.getId());

    Business parent = business1;
    Business child = business2;

    final Relationship relationship = parent.addChild(child, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      addWriteChildPermissions(sessionId, relationship);
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
      };
    }.run();
  }

  @Request(RequestType.SESSION)
  public static void addWriteChildPermissions(String sessionId, Relationship relationship) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Class<?> relClass = LoaderDecorator.load(mdRelationship.definesType());

    try
    {
      c.getMethod("testWriteRelationship", relClass).invoke(object, relationship);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to modify a relationship with WRITE_CHILD permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test modifying to a relationship instance in a MdMethod where the MdMethod
   * only has WRITE_PARENT permissions on the MdRelationship.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testWriteParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, modifyRelationshipMdMethod.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE_PARENT, mdRelationship.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE_PARENT, mdAttribute2.getId());

    Business parent = business1;
    Business child = business2;

    final Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      addWriteParentPermissions(sessionId, relationship);
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
      };
    }.run();
  }

  @Request(RequestType.SESSION)
  public static void addWriteParentPermissions(String sessionId, Relationship relationship) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Class<?> relClass = LoaderDecorator.load(mdRelationship.definesType());

    try
    {
      c.getMethod("testWriteRelationship", relClass).invoke(object, relationship);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to modify a relationship with WRITE_CHILD permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test modifying to a relationship instance in a MdMethod where the MdMethod
   * only has WRITE permissions on the MdRelationship.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testWriteRelationshipPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, modifyRelationshipMdMethod.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE, mdRelationship.getId());
    modifyRelationshipMethodActor.grantPermission(Operation.WRITE, mdAttribute2.getId());

    Business parent = business1;
    Business child = business2;

    final Relationship relationship = child.addParent(parent, mdRelationship.definesType());
    relationship.apply();

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    try
    {
      addWriteRelationshipPermissions(sessionId, relationship);
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
      };
    }.run();
  }

  @Request(RequestType.SESSION)
  public static void addWriteRelationshipPermissions(String sessionId, Relationship relationship) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Class<?> relClass = LoaderDecorator.load(mdRelationship.definesType());

    try
    {
      c.getMethod("testWriteRelationship", relClass).invoke(object, relationship);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to modify a relationship with WRITE_CHILD permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test adding a child to an object in a MdMethod where the MdMethod only has
   * ADD_CHILD permissions on StateIF.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateAddChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addChildMdMethod.getId());
    addChildMethod.grantPermission(Operation.ADD_CHILD, typeTuple2.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateAddChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateAddChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddChild", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to add a child with ADD_CHILD permissions on StateIF and MdRelationship");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * add a child to an object without any ADD_CHILD permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoAddParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addParentMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noAddParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noAddParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddParent", c).invoke(object, input);

      Assert.fail("Failed to throw an exception when adding a child in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test adding a child to an object in a MdMethod where the MdMethod only has
   * ADD_PARENT permissions on the MdBusiness.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testAddParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addParentMdMethod.getId());
    addParentMethod.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    addParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void addParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddParent", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to add a child with ADD_PARENT permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test adding a child to an object in a MdMethod where the MdMethod only has
   * ADD_PARENT permissions on StateIF.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateAddParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.EXECUTE, addParentMdMethod.getId());
    addParentMethod.grantPermission(Operation.ADD_PARENT, typeTuple2.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateAddParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateAddParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    try
    {
      c.getMethod("testAddParent", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to add a child with ADD_PARENT permissions on StateIF and MdRelationship");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * deletes a child from an object without any DELTE_CHILD permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoDeleteChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteChildMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noDeleteChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noDeleteChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestChild", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteChild", c).invoke(object, input);

      Assert.fail("Failed to throw an exception when deleting a child in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test deleting a child from an object in a MdMethod where the MdMethod only
   * has DELETE_CHILD permissions on the MdBusiness.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testDeleteChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteChildMdMethod.getId());
    deleteChildMethod.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    deleteChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void deleteChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestChild", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteChild", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to delete a child with DELETE_CHILD permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test deleting a child to an object in a MdMethod where the MdMethod only
   * has DELETE_CHILD permissions on StateIF.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateDeleteChildPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_CHILD, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteChildMdMethod.getId());
    deleteChildMethod.grantPermission(Operation.DELETE_CHILD, mdRelationship.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateDeleteChildPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateDeleteChildPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestChild", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteChild", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to delete a child with DELETE_CHILD permissions on StateIF and MdRelationship");
    }
  }

  /**
   * Ensure that an DomainErrorException is thrown when a MdMethod attempts to
   * deletes a child from an object without any DELTE_CHILD permissions.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoDeleteParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteParentMdMethod.getId());
    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    noDeleteParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void noDeleteParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestParent", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteParent", c).invoke(object, input);

      Assert.fail("Failed to throw an exception when deleting a child in a method without permission");
    }
    catch (InvocationTargetException e)
    {
      Throwable root = getRootCause(e);

      // Ensure that a DomainErrorException is being thrown
      Assert.assertEquals(DomainErrorException.class.getName(), root.getClass().getName());
    }
  }

  /**
   * Test deleting a child from an object in a MdMethod where the MdMethod only
   * has DELETE_CHILD permissions on the MdBusiness.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testDeleteParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteParentMdMethod.getId());
    deleteParentMethod.grantPermission(Operation.DELETE_PARENT, mdRelationship.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    deleteParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void deleteParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestParent", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteParent", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to delete a parent with DELETE_PARENT permissions on MdBusiness and MdRelationship");
    }
  }

  /**
   * Test deleting a child to an object in a MdMethod where the MdMethod only
   * has DELETE_CHILD permissions on StateIF.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testStateDeleteParentPermissions() throws Exception
  {
    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    newUser.grantPermission(Operation.ADD_PARENT, mdRelationship.getId());
    newUser.grantPermission(Operation.EXECUTE, deleteParentMdMethod.getId());
    deleteParentMethod.grantPermission(Operation.DELETE_PARENT, typeTuple2.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });

    stateDeleteParentPermissions(sessionId);

    Facade.logout(sessionId);
  }

  @Request(RequestType.SESSION)
  public static void stateDeleteParentPermissions(String sessionId) throws Exception
  {
    Class<?> c = LoaderDecorator.load(mdBusiness.definesType());
    Business object = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(object);

    Business input = (Business) c.getConstructor().newInstance();
    c.getMethod("apply").invoke(input);

    Relationship re = (Relationship) c.getMethod("addTestParent", c).invoke(object, input);
    re.apply();

    try
    {
      c.getMethod("testDeleteParent", c).invoke(object, input);
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
      Assert.fail("Unable to delete a parent with DELETE_PARENT permissions on StateIF and MdRelationship");
    }
  }

  /**
   * Test writing an object in a MdMethod where the MdMethod has write
   * permissions on the MdBusiness of the object. Additionally ensure that
   * EXECUTE permissions on the OWNER role work.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testAttributeDimensionPermissions() throws Exception
  {
    MdAttributeDimensionDAOIF mdAttributeDimension = mdAttribute1.getMdAttributeDimension(mdDimension);

    RoleDAO owner = RoleDAO.findRole(RoleDAOIF.OWNER_ROLE).getBusinessDAO();
    owner.grantPermission(Operation.EXECUTE, writeMdMethod.getId());

    newUser.grantPermission(Operation.CREATE, mdBusiness.getId());
    writeMethod.grantPermission(Operation.WRITE, mdBusiness.getId());
    writeMethod.grantPermission(Operation.WRITE, mdAttributeDimension.getId());

    String sessionId = Facade.login(username, password, new Locale[] { CommonProperties.getDefaultLocale() });
    SessionFacade.setDimension(mdDimension.getKey(), sessionId);

    try
    {
      writePermissions(sessionId);
    }
    finally
    {
      Facade.logout(sessionId);
    }
  }

  /**
   * Returns the root cause of an Exception
   * 
   * @param t
   *          A Throwable exception
   * @return
   */
  private static Throwable getRootCause(Throwable t)
  {
    while (t.getCause() != null)
    {
      t = t.getCause();
    }

    return t;
  }

  /**
   * Returns Class1 stub source with the default methods and all MdMethods
   * defined.
   * 
   * @return
   */
  private static String getMethodStub()
  {
    String[] stubSourceArray = { "package test.session;", "public class Class1 extends Class1" + TypeGeneratorInfo.BASE_SUFFIX, "{", "  public Class1()", "  {", "    super();", "  }", "  public static Class1 get(String id)", "  {", "    return (Class1) " + Business.class.getName() + ".get(id);", "  }", "  @" + Authenticate.class.getName(), "  public static void testCreate()", "  {", "    Class1 c = new Class1();", "    c.apply();", "  }", "  @" + Authenticate.class.getName(), "  public static void testDelete(test.session.Class1 class1)", "  {", "    class1.appLock();", "    class1.delete();", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testWrite(test.session.Class1 class1)", "  {", "    class1.appLock();", "    class1.setTestBoolean(true);",
        "    class1.apply();", "  }",

        "  @" + Authenticate.class.getName(), "  public static void testPromote(test.session.Class1 class1)", "  {", "    class1.appLock();", "    class1.transition1();", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testAddChild(test.session.Class1 class1)", "  {", "    this.addTestChild(class1).apply();", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testAddParent(test.session.Class1 class1)", "  {", "    this.addTestParent(class1).apply();", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testDeleteChild(test.session.Class1 class1)", "  {", "    this.removeTestChild(class1);", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testDeleteParent(test.session.Class1 class1)", "  {",
        "    this.removeTestParent(class1);", "  }", "  @" + Authenticate.class.getName(), "  @Request @Test public void testWriteRelationship(test.session.Relationship1 relationship1)", "  {", "    relationship1.appLock();", "    relationship1.setValue(\"testBoolean\", " + MdAttributeBooleanInfo.class.getName() + ".TRUE);", "    relationship1.apply();", "  }", "}" };

    String stubSourceString = new String();

    for (String loc : stubSourceArray)
    {
      stubSourceString += loc;
    }

    return stubSourceString;
  }

  /**
   * Returns Class1 stub source with only the default methods defined.
   * 
   * @return
   */
  private static String getEmptyStub()
  {
    String[] stubSourceArray = { "package test.session;", "public class Class1 extends Class1" + TypeGeneratorInfo.BASE_SUFFIX, "{", "  public Class1()", "  {", "    super();", "  }", "  public static Class1 get(String id)", "  {", "    return (Class1) " + Business.class.getName() + ".get(id);", "  }", "}" };

    String stubSourceString = new String();

    for (String loc : stubSourceArray)
    {
      stubSourceString += loc;
    }

    return stubSourceString;
  }

}
