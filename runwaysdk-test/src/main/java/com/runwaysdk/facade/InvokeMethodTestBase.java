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
package com.runwaysdk.facade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.AfterClass;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.rbac.MethodActorDAO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEntityInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdMethodDAO;
import com.runwaysdk.dataaccess.metadata.MdParameterDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.query.GeneratedComponentQuery;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.Request;

public class InvokeMethodTestBase
{
  protected static ClientSession           systemSession        = null;

  protected static ClientRequestIF         clientRequest        = null;

  protected static final String            pack                 = "test.generated";

  protected static String                  collectionType       = pack + ".Collection";

  protected static String                  collectionQueryClass = collectionType + EntityQueryAPIGenerator.QUERY_API_SUFFIX;

  protected static MdAttributeCharacterDAO collectionMdAttributeCharacterDTO;

  protected static String                  bagType              = pack + ".Bag";

  protected static String                  referenceType        = pack + ".Reference";

  protected static String                  stateType            = pack + ".AllStates";

  protected static String                  collectionDTO        = collectionType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static String                  bagDTO               = bagType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdBusinessDAO           collection;

  protected static MdBusinessDAO           bag;

  protected static MdRelationshipDAO       reference;

  protected static MdBusinessDAO           states;

  protected static MdEnumerationDAO        stateEnum;

  protected static BusinessDAO             california;

  protected static BusinessDAO             colorado;

  protected static BusinessDAO             connecticut;

  protected static String                  utilType             = pack + ".Util";

  protected static String                  utilDTO              = utilType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdUtilDAO               mdUtil;

  protected static MdMethodDAO             mdMethod0;

  protected static MdMethodDAO             mdMethod1;

  protected static MdMethodDAO             mdMethod2;

  protected static MdMethodDAO             mdMethod3;

  protected static MdMethodDAO             mdMethod4;

  protected static MdMethodDAO             mdMethod5;

  protected static MdMethodDAO             mdMethod6;

  protected static MdMethodDAO             mdMethod7;

  protected static MdMethodDAO             mdMethod8;

  protected static MdMethodDAO             mdMethod9;

  protected static MdMethodDAO             mdMethod10;

  protected static MdMethodDAO             mdMethod11;

  protected static MdMethodDAO             mdMethod12;

  protected static MdMethodDAO             mdMethod13;

  protected static MdMethodDAO             mdMethod14;

  protected static MdMethodDAO             mdMethod15;

  protected static MdMethodDAO             mdMethod16;

  protected static MdMethodDAO             mdMethod17;

  protected static MdMethodDAO             mdMethod18;

  protected static MdMethodDAO             mdMethod19;

  protected static MdMethodDAO             mdMethod20;

  protected static MethodActorDAO          methodActor;

  protected static String                  viewType             = pack + ".TestView";

  protected static String                  viewQueryClass       = ViewQueryStubAPIGenerator.getQueryStubClass(viewType);

  protected static String                  viewDTO              = viewType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdViewDAO               mdView;

  protected static UserDAO                 newUser;

  protected static ClientSession           noPermissionSession;

  protected static ClientRequestIF         noPermissionRequest;

  protected static MdMethodDAO             mdMethodReturnByte;

  protected static MdMethodDAO             mdMethodReturnStream;

  protected static MdAttributeLongDAO      mdAttributeLong;

  @Request
  public static void classSetUpRequest()
  {
    collection = MdBusinessDAO.newInstance();
    collection.setValue(MdBusinessInfo.NAME, "Collection");
    collection.setValue(MdBusinessInfo.PACKAGE, pack);
    collection.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setValue(MdBusinessInfo.DISPLAY_LABEL, "All Attributes");
    collection.apply();

    mdAttributeLong = MdAttributeLongDAO.newInstance();
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME, "aLong2");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long 2");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getOid());
    mdAttributeLong.apply();

    // Create a new user who has no permissions
    newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, "smethie");
    newUser.setValue(UserInfo.PASSWORD, "aaa");
    newUser.apply();

    // Grant read and write permissions to the new user
    clientRequest.grantTypePermission(newUser.getOid(), collection.getOid(), Operation.READ.name());
    clientRequest.grantTypePermission(newUser.getOid(), collection.getOid(), Operation.WRITE.name());

    addAttributes(collection.getOid());

    mdUtil = MdUtilDAO.newInstance();
    mdUtil.setValue(MdUtilInfo.NAME, "Util");
    mdUtil.setValue(MdUtilInfo.PACKAGE, pack);
    mdUtil.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util class");
    mdUtil.apply();

    MdAttributeCharacterDAO utilCharacter = MdAttributeCharacterDAO.newInstance();
    utilCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    utilCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    utilCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    utilCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdUtil.getOid());
    utilCharacter.apply();

    mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.NAME, "TestView");
    mdView.setValue(MdViewInfo.PACKAGE, pack);
    mdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test view");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    mdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    addAttributes(mdView.getOid());

    mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, getViewQueryStub());
    mdView.apply();

    bag = MdBusinessDAO.newInstance();
    bag.setValue(MdBusinessInfo.NAME, "Bag");
    bag.setValue(MdBusinessInfo.PACKAGE, pack);
    bag.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    bag.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Bag");
    bag.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, collection.getOid());
    bag.apply();

    reference = MdRelationshipDAO.newInstance();
    reference.setValue(MdRelationshipInfo.NAME, "Reference");
    reference.setValue(MdRelationshipInfo.PACKAGE, pack);
    reference.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    reference.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference");
    reference.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    reference.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection1");
    reference.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, collection.getOid());
    reference.setValue(MdRelationshipInfo.PARENT_METHOD, "collection1");
    reference.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    reference.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection2");
    reference.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, collection.getOid());
    reference.setValue(MdRelationshipInfo.CHILD_METHOD, "collection2");
    reference.apply();

    MdBusinessDAOIF enumMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    states = MdBusinessDAO.newInstance();
    states.setValue(MdBusinessInfo.NAME, "States");
    states.setValue(MdBusinessInfo.PACKAGE, pack);
    states.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    states.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    states.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMaster.getOid());
    states.apply();

    stateEnum = MdEnumerationDAO.newInstance();
    stateEnum.setValue(MdEnumerationInfo.NAME, "AllStates");
    stateEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    stateEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, states.getOid());
    stateEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States");
    stateEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    stateEnum.apply();

    mdMethod0 = MdMethodDAO.newInstance();
    mdMethod0.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod0.setValue(MdMethodInfo.NAME, "modifyNoPersist");
    mdMethod0.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Modify No Persist");
    mdMethod0.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod0.apply();

    MdParameterDAO noPersistParam = MdParameterDAO.newInstance();
    noPersistParam.setValue(MdParameterInfo.TYPE, collectionType);
    noPersistParam.setValue(MdParameterInfo.NAME, "collection");
    noPersistParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    noPersistParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod0.getOid());
    noPersistParam.setValue(MdParameterInfo.ORDER, "0");
    noPersistParam.apply();

    mdMethod1 = MdMethodDAO.newInstance();
    mdMethod1.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod1.setValue(MdMethodInfo.NAME, "sortNumbers");
    mdMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Number");
    mdMethod1.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod1.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Boolean");
    mdParameter.setValue(MdParameterInfo.NAME, "ascending");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ascending Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getOid());
    mdParameter.setValue(MdParameterInfo.ORDER, "1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Long" + "[]");
    mdParameter2.setValue(MdParameterInfo.NAME, "numbers");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Numbers Parameter");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getOid());
    mdParameter2.setValue(MdParameterInfo.ORDER, "0");
    mdParameter2.apply();

    mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setValue(MdMethodInfo.NAME, "poopNothing");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Poop Nothing");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod2.apply();

    mdMethod3 = MdMethodDAO.newInstance();
    mdMethod3.setValue(MdMethodInfo.RETURN_TYPE, collectionType + "[]");
    mdMethod3.setValue(MdMethodInfo.NAME, "sortCollections");
    mdMethod3.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Collections");
    mdMethod3.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod3.apply();

    MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.TYPE, collectionType + "[]");
    mdParameter3.setValue(MdParameterInfo.NAME, "collections");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collections Parameter");
    mdParameter3.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getOid());
    mdParameter3.setValue(MdParameterInfo.ORDER, "0");
    mdParameter3.apply();

    MdParameterDAO mdParameter5 = MdParameterDAO.newInstance();
    mdParameter5.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter5.setValue(MdParameterInfo.NAME, "collectionName");
    mdParameter5.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Name Parameter");
    mdParameter5.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getOid());
    mdParameter5.setValue(MdParameterInfo.ORDER, "1");
    mdParameter5.apply();

    mdMethod4 = MdMethodDAO.newInstance();
    mdMethod4.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod4.setValue(MdMethodInfo.NAME, "testMethod");
    mdMethod4.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod4.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod4.apply();

    MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.TYPE, collectionType);
    mdParameter4.setValue(MdParameterInfo.NAME, "collection");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    mdParameter4.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod4.getOid());
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.apply();

    mdMethod5 = MdMethodDAO.newInstance();
    mdMethod5.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.String[][]");
    mdMethod5.setValue(MdMethodInfo.NAME, "testMultiArray");
    mdMethod5.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod5.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod5.apply();

    MdParameterDAO mdParameter6 = MdParameterDAO.newInstance();
    mdParameter6.setValue(MdParameterInfo.TYPE, collectionType + "[][][][]");
    mdParameter6.setValue(MdParameterInfo.NAME, "collection4");
    mdParameter6.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    mdParameter6.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod5.getOid());
    mdParameter6.setValue(MdParameterInfo.ORDER, "0");
    mdParameter6.apply();

    mdMethod6 = MdMethodDAO.newInstance();
    mdMethod6.setValue(MdMethodInfo.RETURN_TYPE, referenceType + "[]");
    mdMethod6.setValue(MdMethodInfo.NAME, "getReferences");
    mdMethod6.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reference");
    mdMethod6.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod6.apply();

    MdParameterDAO mdParameter7 = MdParameterDAO.newInstance();
    mdParameter7.setValue(MdParameterInfo.TYPE, referenceType);
    mdParameter7.setValue(MdParameterInfo.NAME, "reference");
    mdParameter7.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Parameter");
    mdParameter7.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod6.getOid());
    mdParameter7.setValue(MdParameterInfo.ORDER, "0");
    mdParameter7.apply();

    mdMethod7 = MdMethodDAO.newInstance();
    mdMethod7.setValue(MdMethodInfo.RETURN_TYPE, stateType + "[]");
    mdMethod7.setValue(MdMethodInfo.NAME, "getStates");
    mdMethod7.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test States");
    mdMethod7.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod7.apply();

    // method that takes in an array of AllStates enums and returns them.
    // this is to test enum arrays as parameters.
    MdMethodDAO returnStates = MdMethodDAO.newInstance();
    returnStates.setValue(MdMethodInfo.RETURN_TYPE, pack + ".AllStates[]");
    returnStates.setValue(MdMethodInfo.NAME, "returnStates");
    returnStates.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Return states");
    returnStates.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    returnStates.apply();

    MdParameterDAO param = MdParameterDAO.newInstance();
    param.setValue(MdParameterInfo.TYPE, pack + ".AllStates[]");
    param.setValue(MdParameterInfo.NAME, "states");
    param.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "States Parameter");
    param.setValue(MdParameterInfo.ENCLOSING_METADATA, returnStates.getOid());
    param.setValue(MdParameterInfo.ORDER, "0");
    param.apply();

    MdParameterDAO mdParameter9 = MdParameterDAO.newInstance();
    mdParameter9.setValue(MdParameterInfo.TYPE, stateType);
    mdParameter9.setValue(MdParameterInfo.NAME, "state");
    mdParameter9.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Parameter");
    mdParameter9.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod7.getOid());
    mdParameter9.setValue(MdParameterInfo.ORDER, "0");
    mdParameter9.apply();

    mdMethod8 = MdMethodDAO.newInstance();
    mdMethod8.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer[]");
    mdMethod8.setValue(MdMethodInfo.NAME, "sortIntegers");
    mdMethod8.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Integers");
    mdMethod8.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod8.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod8.apply();

    MdParameterDAO mdParameter10 = MdParameterDAO.newInstance();
    mdParameter10.setValue(MdParameterInfo.TYPE, "java.lang.Integer[]");
    mdParameter10.setValue(MdParameterInfo.NAME, "integers");
    mdParameter10.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integers Parameter");
    mdParameter10.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod8.getOid());
    mdParameter10.setValue(MdParameterInfo.ORDER, "0");
    mdParameter10.apply();

    mdMethod9 = MdMethodDAO.newInstance();
    mdMethod9.setValue(MdMethodInfo.RETURN_TYPE, "java.util.Date[]");
    mdMethod9.setValue(MdMethodInfo.NAME, "getDates");
    mdMethod9.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Dates");
    mdMethod9.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod9.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod9.apply();

    MdParameterDAO mdParameter11 = MdParameterDAO.newInstance();
    mdParameter11.setValue(MdParameterInfo.TYPE, "java.util.Date");
    mdParameter11.setValue(MdParameterInfo.NAME, "date");
    mdParameter11.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Parameter");
    mdParameter11.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod9.getOid());
    mdParameter11.setValue(MdParameterInfo.ORDER, "0");
    mdParameter11.apply();

    mdMethod10 = MdMethodDAO.newInstance();
    mdMethod10.setValue(MdMethodInfo.RETURN_TYPE, utilType);
    mdMethod10.setValue(MdMethodInfo.NAME, "getUtil");
    mdMethod10.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object");
    mdMethod10.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod10.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod10.apply();

    MdParameterDAO mdParameter12 = MdParameterDAO.newInstance();
    mdParameter12.setValue(MdParameterInfo.TYPE, utilType);
    mdParameter12.setValue(MdParameterInfo.NAME, "util");
    mdParameter12.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object");
    mdParameter12.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod10.getOid());
    mdParameter12.setValue(MdParameterInfo.ORDER, "0");
    mdParameter12.apply();

    mdMethod11 = MdMethodDAO.newInstance();
    mdMethod11.setValue(MdMethodInfo.RETURN_TYPE, viewType);
    mdMethod11.setValue(MdMethodInfo.NAME, "getView");
    mdMethod11.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object");
    mdMethod11.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod11.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod11.apply();

    MdParameterDAO mdParameter13 = MdParameterDAO.newInstance();
    mdParameter13.setValue(MdParameterInfo.TYPE, viewType);
    mdParameter13.setValue(MdParameterInfo.NAME, "view");
    mdParameter13.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object");
    mdParameter13.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod11.getOid());
    mdParameter13.setValue(MdParameterInfo.ORDER, "0");
    mdParameter13.apply();

    mdMethod12 = MdMethodDAO.newInstance();
    mdMethod12.setValue(MdMethodInfo.RETURN_TYPE, utilType + "[]");
    mdMethod12.setValue(MdMethodInfo.NAME, "utilArray");
    mdMethod12.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object Array");
    mdMethod12.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod12.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod12.apply();

    MdParameterDAO mdParameter14 = MdParameterDAO.newInstance();
    mdParameter14.setValue(MdParameterInfo.TYPE, utilType + "[]");
    mdParameter14.setValue(MdParameterInfo.NAME, "utilArray");
    mdParameter14.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object Array");
    mdParameter14.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod12.getOid());
    mdParameter14.setValue(MdParameterInfo.ORDER, "0");
    mdParameter14.apply();

    mdMethod13 = MdMethodDAO.newInstance();
    mdMethod13.setValue(MdMethodInfo.RETURN_TYPE, viewType + "[]");
    mdMethod13.setValue(MdMethodInfo.NAME, "viewArray");
    mdMethod13.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object Array");
    mdMethod13.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod13.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod13.apply();

    MdParameterDAO mdParameter15 = MdParameterDAO.newInstance();
    mdParameter15.setValue(MdParameterInfo.TYPE, viewType + "[]");
    mdParameter15.setValue(MdParameterInfo.NAME, "viewArray");
    mdParameter15.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object Array");
    mdParameter15.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod13.getOid());
    mdParameter15.setValue(MdParameterInfo.ORDER, "0");
    mdParameter15.apply();

    mdMethod14 = MdMethodDAO.newInstance();
    mdMethod14.setValue(MdMethodInfo.RETURN_TYPE, collectionQueryClass);
    mdMethod14.setValue(MdMethodInfo.NAME, "getCollectionQuery");
    mdMethod14.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a collection query");
    mdMethod14.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod14.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod14.apply();

    mdMethod15 = MdMethodDAO.newInstance();
    mdMethod15.setValue(MdMethodInfo.RETURN_TYPE, collectionQueryClass);
    mdMethod15.setValue(MdMethodInfo.NAME, "getCollectionQueryRestrictRows");
    mdMethod15.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a collection query");
    mdMethod15.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod15.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod15.apply();

    mdMethod16 = MdMethodDAO.newInstance();
    mdMethod16.setValue(MdMethodInfo.RETURN_TYPE, viewQueryClass);
    mdMethod16.setValue(MdMethodInfo.NAME, "getViewQuery");
    mdMethod16.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a view query");
    mdMethod16.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod16.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod16.apply();

    mdMethod17 = MdMethodDAO.newInstance();
    mdMethod17.setValue(MdMethodInfo.RETURN_TYPE, viewQueryClass);
    mdMethod17.setValue(MdMethodInfo.NAME, "getViewQueryRestrictRows");
    mdMethod17.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a view query");
    mdMethod17.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod17.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod17.apply();

    mdMethod18 = MdMethodDAO.newInstance();
    mdMethod18.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName());
    mdMethod18.setValue(MdMethodInfo.NAME, "getCollectionObjectCount");
    mdMethod18.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns the number of collections");
    mdMethod18.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod18.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod18.apply();

    mdMethod19 = MdMethodDAO.newInstance();
    mdMethod19.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod19.setValue(MdMethodInfo.NAME, "methodActorRead");
    mdMethod19.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tests the read permissions on a method actor.");
    mdMethod19.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod19.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod19.apply();

    mdMethod20 = MdMethodDAO.newInstance();
    mdMethod20.setValue(MdMethodInfo.RETURN_TYPE, GeneratedComponentQuery.class.getName());
    mdMethod20.setValue(MdMethodInfo.NAME, "getBusinessQuery");
    mdMethod20.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a generic business query");
    mdMethod20.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethod20.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod20.apply();

    clientRequest.grantMethodPermission(newUser.getOid(), mdMethod19.getOid(), Operation.EXECUTE.name());

    methodActor = MethodActorDAO.newInstance();
    methodActor.setValue(MethodActorInfo.MD_METHOD, mdMethod19.getOid());
    methodActor.apply();

    mdMethodReturnByte = MdMethodDAO.newInstance();
    mdMethodReturnByte.setValue(MdMethodInfo.RETURN_TYPE, Byte.class.getName() + "[]");
    mdMethodReturnByte.setValue(MdMethodInfo.NAME, "getExcelFile");
    mdMethodReturnByte.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns a byte array that contains an Excel file.");
    mdMethodReturnByte.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethodReturnByte.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethodReturnByte.apply();

    mdMethodReturnStream = MdMethodDAO.newInstance();
    mdMethodReturnStream.setValue(MdMethodInfo.RETURN_TYPE, InputStream.class.getName());
    mdMethodReturnStream.setValue(MdMethodInfo.NAME, "getFileStream");
    mdMethodReturnStream.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns an input stream contains an Excel file.");
    mdMethodReturnStream.setValue(MdMethodInfo.REF_MD_TYPE, collection.getOid());
    mdMethodReturnStream.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethodReturnStream.apply();
  }

  private static void addAttributes(String mdClassId)
  {
    MdAttributeBooleanDAO aBoolean = MdAttributeBooleanDAO.newInstance();
    aBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    aBoolean.setValue(MdAttributeBooleanInfo.DISPLAY_LABEL, "A Boolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    aBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    aBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClassId);
    aBoolean.apply();

    MdAttributeCharacterDAO aCharacter = MdAttributeCharacterDAO.newInstance();
    aCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    aCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    aCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    aCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClassId);
    aCharacter.apply();

    if (collection.getOid().equals(mdClassId))
    {
      collectionMdAttributeCharacterDTO = aCharacter;
    }

    MdAttributeDoubleDAO aDouble = MdAttributeDoubleDAO.newInstance();
    aDouble.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    aDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    aDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdClassId);
    aDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    aDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    aDouble.apply();

    MdAttributeLongDAO businessDTO = MdAttributeLongDAO.newInstance();
    businessDTO.setValue(MdAttributeLongInfo.NAME, "aLong");
    businessDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    businessDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClassId);
    businessDTO.apply();
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    try
    {
      // Clear the stub source
      collection.setValue(MdEntityInfo.STUB_SOURCE, getBlankMethodStub());
      collection.apply();

      TestFixtureFactory.delete(mdMethod6);

      TestFixtureFactory.delete(reference);

      TestFixtureFactory.delete(bag);

      // Clear any dependency between mdView and collection before deleting
      // collection.
      mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, getBlankViewQueryStub());
      mdView.apply();

      TestFixtureFactory.delete(collection);

      TestFixtureFactory.delete(mdUtil);

      TestFixtureFactory.delete(stateEnum);

      TestFixtureFactory.delete(states);

      TestFixtureFactory.delete(mdView);


      TestFixtureFactory.delete(newUser);
    }
    finally
    {
      try
      {
        noPermissionSession.logout();
        systemSession.logout();
      }
      catch(Exception e)
      {
        // Do nothing
      }
    }
  }

  @Request
  public static void finalizeSetup()
  {
    // we set the stub source here since it references other objects that
    // were created during setup.
    String collectionStubSource = getMethodStub();
    collection.setValue(MdClassInfo.STUB_SOURCE, collectionStubSource);
    collection.apply();

    california = BusinessDAO.newInstance(pack + ".States");
    california.setValue(EnumerationMasterInfo.NAME, "CA");
    california.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    california.apply();

    colorado = BusinessDAO.newInstance(pack + ".States");
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.apply();

    connecticut = BusinessDAO.newInstance(pack + ".States");
    connecticut.setValue(EnumerationMasterInfo.NAME, "CT");
    connecticut.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Connecticut");
    connecticut.apply();
  }

  private static String getMethodStub()
  {
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(InvokeMethodTestBase.class.getResourceAsStream("/InvokeMethodSource")));
      StringBuffer buffer = new StringBuffer();

      while (reader.ready())
      {
        buffer.append(reader.readLine());
      }

      return buffer.toString();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  private static String getBlankMethodStub()
  {
    String[] collectionStubSource = { "package " + pack + ";", "public class Collection extends CollectionBase", "{", "  public Collection()", "  {", "    super();", "  }", "}" };

    String source = "";
    for (String s : collectionStubSource)
    {
      source += s + "\n";
    }

    return source;
  }

  private static String getViewQueryStub()
  {
    String aBooleanConst = TypeGenerator.buildAttributeConstant(collectionType, "aBoolean");
    String aCharacterConst = TypeGenerator.buildAttributeConstant(collectionType, "aCharacter");
    String aDoubleConst = TypeGenerator.buildAttributeConstant(collectionType, "aDouble");
    String aLongConst = TypeGenerator.buildAttributeConstant(collectionType, "aLong");

    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase \n" + "{\n" + "\n" + "  private " + collectionQueryClass + " collectionQuery;\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "     \n" + "     collectionQuery = new " + collectionQueryClass + "(componentQueryFactory);\n" + "\n" + "     this.map(" + aBooleanConst + ", collectionQuery.getABoolean());\n" + "     this.map(" + aCharacterConst + ", collectionQuery.getACharacter());\n" + "     this.map(" + aDoubleConst + ", collectionQuery.getADouble());\n" + "     this.map(" + aLongConst + ", collectionQuery.getALong());\n" + "\n"
        + "     this.buildSelectClause();\n" + "  }\n" + "}\n";

    return queryStubSource;
  }

  private static String getBlankViewQueryStub()
  {
    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase \n" + "{\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "  }\n" + "}\n";

    return queryStubSource;
  }
}
