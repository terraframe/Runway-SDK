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
package com.runwaysdk.facade;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import junit.framework.TestCase;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQuery;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.ViewQueryStubAPIGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.rbac.Operation;
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
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.query.ValueQuery;
import com.runwaysdk.query.ValueQueryExcelExporter;
import com.runwaysdk.util.FileIO;

public class InvokeMethodTestBase extends TestCase
{
  protected static ClientSession   systemSession        = null;

  protected static ClientRequestIF clientRequest        = null;

  protected static final String    pack                 = "test.generated";

  protected static String          collectionType       = pack + ".Collection";

  protected static String          collectionQueryClass = collectionType + EntityQueryAPIGenerator.QUERY_API_SUFFIX;

  protected static BusinessDTO     collectionMdAttributeCharacterDTO;

  protected static String          bagType              = pack + ".Bag";

  protected static String          referenceType        = pack + ".Reference";

  protected static String          stateType            = pack + ".AllStates";

  protected static String          collectionDTO        = collectionType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static String          bagDTO               = bagType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static BusinessDTO     collection;

  protected static BusinessDTO     bag;

  protected static BusinessDTO     reference;

  protected static BusinessDTO     states;

  protected static BusinessDTO     stateEnum;

  protected static BusinessDTO     california;

  protected static BusinessDTO     colorado;

  protected static BusinessDTO     connecticut;

  protected static String          utilType             = pack + ".Util";

  protected static String          utilDTO              = utilType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static BusinessDTO     mdUtil;

  protected static BusinessDTO     mdMethod0;

  protected static BusinessDTO     mdMethod1;

  protected static BusinessDTO     mdMethod2;

  protected static BusinessDTO     mdMethod3;

  protected static BusinessDTO     mdMethod4;

  protected static BusinessDTO     mdMethod5;

  protected static BusinessDTO     mdMethod6;

  protected static BusinessDTO     mdMethod7;

  protected static BusinessDTO     mdMethod8;

  protected static BusinessDTO     mdMethod9;

  protected static BusinessDTO     mdMethod10;

  protected static BusinessDTO     mdMethod11;

  protected static BusinessDTO     mdMethod12;

  protected static BusinessDTO     mdMethod13;

  protected static BusinessDTO     mdMethod14;

  protected static BusinessDTO     mdMethod15;

  protected static BusinessDTO     mdMethod16;

  protected static BusinessDTO     mdMethod17;

  protected static BusinessDTO     mdMethod18;

  protected static BusinessDTO     mdMethod19;

  protected static BusinessDTO     methodActor;

  protected static String          viewType             = pack + ".TestView";

  protected static String          viewQueryClass       = ViewQueryStubAPIGenerator.getQueryStubClass(viewType);

  protected static String          viewDTO              = viewType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static BusinessDTO     mdView;

  protected static BusinessDTO     newUser;

  protected static ClientSession   noPermissionSession;

  protected static ClientRequestIF noPermissionRequest;

  protected static BusinessDTO     mdMethodReturnByte;

  protected static BusinessDTO     mdMethodReturnStream;

  protected static BusinessDTO     mdAttributeLong;

  public static void classSetUp()
  {
    collection = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    collection.setValue(MdBusinessInfo.NAME, "Collection");
    collection.setValue(MdBusinessInfo.PACKAGE, pack);
    collection.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setValue(MdBusinessInfo.DISPLAY_LABEL, "All Attributes");
    clientRequest.createBusiness(collection);

    mdAttributeLong = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME, "aLong2");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long 2");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getId());
    clientRequest.createBusiness(mdAttributeLong);

    // Create a new user who has no permissions
    newUser = clientRequest.newBusiness(UserInfo.CLASS);
    newUser.setValue(UserInfo.USERNAME, "smethie");
    newUser.setValue(UserInfo.PASSWORD, "aaa");
    clientRequest.createBusiness(newUser);

    // Grant read and write permissions to the new user
    clientRequest.grantTypePermission(newUser.getId(), collection.getId(), Operation.READ.name());
    clientRequest.grantTypePermission(newUser.getId(), collection.getId(), Operation.WRITE.name());

    addAttributes(collection.getId());

    mdUtil = clientRequest.newBusiness(MdUtilInfo.CLASS);
    mdUtil.setValue(MdUtilInfo.NAME, "Util");
    mdUtil.setValue(MdUtilInfo.PACKAGE, pack);
    mdUtil.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtil.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util class");
    clientRequest.createBusiness(mdUtil);

    BusinessDTO utilCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    utilCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    utilCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    utilCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    utilCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdUtil.getId());
    clientRequest.createBusiness(utilCharacter);

    mdView = clientRequest.newBusiness(MdViewInfo.CLASS);
    mdView.setValue(MdViewInfo.NAME, "TestView");
    mdView.setValue(MdViewInfo.PACKAGE, pack);
    mdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test view");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    mdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdView);

    addAttributes(mdView.getId());

    clientRequest.lock(mdView);
    mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, getViewQueryStub());
    clientRequest.update(mdView);

    bag = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    bag.setValue(MdBusinessInfo.NAME, "Bag");
    bag.setValue(MdBusinessInfo.PACKAGE, pack);
    bag.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    bag.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Bag");
    bag.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, collection.getId());
    clientRequest.createBusiness(bag);

    reference = clientRequest.newBusiness(MdRelationshipInfo.CLASS);
    reference.setValue(MdRelationshipInfo.NAME, "Reference");
    reference.setValue(MdRelationshipInfo.PACKAGE, pack);
    reference.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    reference.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference");
    reference.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    reference.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection1");
    reference.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, collection.getId());
    reference.setValue(MdRelationshipInfo.PARENT_METHOD, "collection1");
    reference.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    reference.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection2");
    reference.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, collection.getId());
    reference.setValue(MdRelationshipInfo.CHILD_METHOD, "collection2");
    clientRequest.createBusiness(reference);

    MdBusinessDAOIF enumMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    states = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    states.setValue(MdBusinessInfo.NAME, "States");
    states.setValue(MdBusinessInfo.PACKAGE, pack);
    states.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    states.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    states.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMaster.getId());
    clientRequest.createBusiness(states);

    stateEnum = clientRequest.newBusiness(MdEnumerationInfo.CLASS);
    stateEnum.setValue(MdEnumerationInfo.NAME, "AllStates");
    stateEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    stateEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, states.getId());
    stateEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States");
    stateEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(stateEnum);

    mdMethod0 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod0.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod0.setValue(MdMethodInfo.NAME, "modifyNoPersist");
    mdMethod0.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Modify No Persist");
    mdMethod0.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod0);

    BusinessDTO noPersistParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    noPersistParam.setValue(MdParameterInfo.TYPE, collectionType);
    noPersistParam.setValue(MdParameterInfo.NAME, "collection");
    noPersistParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    noPersistParam.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod0.getId());
    noPersistParam.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(noPersistParam);

    mdMethod1 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod1.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod1.setValue(MdMethodInfo.NAME, "sortNumbers");
    mdMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Number");
    mdMethod1.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod1);

    BusinessDTO mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Boolean");
    mdParameter.setValue(MdParameterInfo.NAME, "ascending");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ascending Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "1");
    clientRequest.createBusiness(mdParameter);

    BusinessDTO mdParameter2 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Long" + "[]");
    mdParameter2.setValue(MdParameterInfo.NAME, "numbers");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Numbers Parameter");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getId());
    mdParameter2.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter2);

    mdMethod2 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setValue(MdMethodInfo.NAME, "poopNothing");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Poop Nothing");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod2);

    mdMethod3 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod3.setValue(MdMethodInfo.RETURN_TYPE, collectionType + "[]");
    mdMethod3.setValue(MdMethodInfo.NAME, "sortCollections");
    mdMethod3.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Collections");
    mdMethod3.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod3);

    BusinessDTO mdParameter3 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter3.setValue(MdParameterInfo.TYPE, collectionType + "[]");
    mdParameter3.setValue(MdParameterInfo.NAME, "collections");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collections Parameter");
    mdParameter3.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getId());
    mdParameter3.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter3);

    BusinessDTO mdParameter5 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter5.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter5.setValue(MdParameterInfo.NAME, "collectionName");
    mdParameter5.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Name Parameter");
    mdParameter5.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getId());
    mdParameter5.setValue(MdParameterInfo.ORDER, "1");
    clientRequest.createBusiness(mdParameter5);

    mdMethod4 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod4.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod4.setValue(MdMethodInfo.NAME, "testMethod");
    mdMethod4.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod4.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod4);

    BusinessDTO mdParameter4 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter4.setValue(MdParameterInfo.TYPE, collectionType);
    mdParameter4.setValue(MdParameterInfo.NAME, "collection");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    mdParameter4.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod4.getId());
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter4);

    mdMethod5 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod5.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.String[][]");
    mdMethod5.setValue(MdMethodInfo.NAME, "testMultiArray");
    mdMethod5.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod5.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod5);

    BusinessDTO mdParameter6 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter6.setValue(MdParameterInfo.TYPE, collectionType + "[][][][]");
    mdParameter6.setValue(MdParameterInfo.NAME, "collection4");
    mdParameter6.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    mdParameter6.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod5.getId());
    mdParameter6.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter6);

    mdMethod6 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod6.setValue(MdMethodInfo.RETURN_TYPE, referenceType + "[]");
    mdMethod6.setValue(MdMethodInfo.NAME, "getReferences");
    mdMethod6.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Reference");
    mdMethod6.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod6);

    BusinessDTO mdParameter7 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter7.setValue(MdParameterInfo.TYPE, referenceType);
    mdParameter7.setValue(MdParameterInfo.NAME, "reference");
    mdParameter7.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Parameter");
    mdParameter7.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod6.getId());
    mdParameter7.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter7);

    mdMethod7 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod7.setValue(MdMethodInfo.RETURN_TYPE, stateType + "[]");
    mdMethod7.setValue(MdMethodInfo.NAME, "getStates");
    mdMethod7.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test States");
    mdMethod7.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(mdMethod7);

    // method that takes in an array of AllStates enums and returns them.
    // this is to test enum arrays as parameters.
    BusinessDTO returnStates = clientRequest.newBusiness(MdMethodInfo.CLASS);
    returnStates.setValue(MdMethodInfo.RETURN_TYPE, pack + ".AllStates[]");
    returnStates.setValue(MdMethodInfo.NAME, "returnStates");
    returnStates.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Return states");
    returnStates.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    clientRequest.createBusiness(returnStates);

    BusinessDTO param = clientRequest.newBusiness(MdParameterInfo.CLASS);
    param.setValue(MdParameterInfo.TYPE, pack + ".AllStates[]");
    param.setValue(MdParameterInfo.NAME, "states");
    param.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "States Parameter");
    param.setValue(MdParameterInfo.ENCLOSING_METADATA, returnStates.getId());
    param.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(param);

    BusinessDTO mdParameter9 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter9.setValue(MdParameterInfo.TYPE, stateType);
    mdParameter9.setValue(MdParameterInfo.NAME, "state");
    mdParameter9.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Parameter");
    mdParameter9.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod7.getId());
    mdParameter9.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter9);

    mdMethod8 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod8.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer[]");
    mdMethod8.setValue(MdMethodInfo.NAME, "sortIntegers");
    mdMethod8.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Integers");
    mdMethod8.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod8.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod8);

    BusinessDTO mdParameter10 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter10.setValue(MdParameterInfo.TYPE, "java.lang.Integer[]");
    mdParameter10.setValue(MdParameterInfo.NAME, "integers");
    mdParameter10.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integers Parameter");
    mdParameter10.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod8.getId());
    mdParameter10.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter10);

    mdMethod9 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod9.setValue(MdMethodInfo.RETURN_TYPE, "java.util.Date[]");
    mdMethod9.setValue(MdMethodInfo.NAME, "getDates");
    mdMethod9.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Dates");
    mdMethod9.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod9.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod9);

    BusinessDTO mdParameter11 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter11.setValue(MdParameterInfo.TYPE, "java.util.Date");
    mdParameter11.setValue(MdParameterInfo.NAME, "date");
    mdParameter11.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Parameter");
    mdParameter11.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod9.getId());
    mdParameter11.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter11);

    mdMethod10 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod10.setValue(MdMethodInfo.RETURN_TYPE, utilType);
    mdMethod10.setValue(MdMethodInfo.NAME, "getUtil");
    mdMethod10.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object");
    mdMethod10.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod10.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdMethod10);

    BusinessDTO mdParameter12 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter12.setValue(MdParameterInfo.TYPE, utilType);
    mdParameter12.setValue(MdParameterInfo.NAME, "util");
    mdParameter12.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object");
    mdParameter12.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod10.getId());
    mdParameter12.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter12);

    mdMethod11 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod11.setValue(MdMethodInfo.RETURN_TYPE, viewType);
    mdMethod11.setValue(MdMethodInfo.NAME, "getView");
    mdMethod11.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object");
    mdMethod11.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod11.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdMethod11);

    BusinessDTO mdParameter13 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter13.setValue(MdParameterInfo.TYPE, viewType);
    mdParameter13.setValue(MdParameterInfo.NAME, "view");
    mdParameter13.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object");
    mdParameter13.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod11.getId());
    mdParameter13.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter13);

    mdMethod12 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod12.setValue(MdMethodInfo.RETURN_TYPE, utilType + "[]");
    mdMethod12.setValue(MdMethodInfo.NAME, "utilArray");
    mdMethod12.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object Array");
    mdMethod12.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod12.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdMethod12);

    BusinessDTO mdParameter14 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter14.setValue(MdParameterInfo.TYPE, utilType + "[]");
    mdParameter14.setValue(MdParameterInfo.NAME, "utilArray");
    mdParameter14.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object Array");
    mdParameter14.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod12.getId());
    mdParameter14.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter14);

    mdMethod13 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod13.setValue(MdMethodInfo.RETURN_TYPE, viewType + "[]");
    mdMethod13.setValue(MdMethodInfo.NAME, "viewArray");
    mdMethod13.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object Array");
    mdMethod13.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod13.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdMethod13);

    BusinessDTO mdParameter15 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter15.setValue(MdParameterInfo.TYPE, viewType + "[]");
    mdParameter15.setValue(MdParameterInfo.NAME, "viewArray");
    mdParameter15.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object Array");
    mdParameter15.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod13.getId());
    mdParameter15.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter15);

    mdMethod14 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod14.setValue(MdMethodInfo.RETURN_TYPE, collectionQueryClass);
    mdMethod14.setValue(MdMethodInfo.NAME, "getCollectionQuery");
    mdMethod14.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a collection query");
    mdMethod14.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod14.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod14);

    mdMethod15 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod15.setValue(MdMethodInfo.RETURN_TYPE, collectionQueryClass);
    mdMethod15.setValue(MdMethodInfo.NAME, "getCollectionQueryRestrictRows");
    mdMethod15.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a collection query");
    mdMethod15.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod15.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod15);

    mdMethod16 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod16.setValue(MdMethodInfo.RETURN_TYPE, viewQueryClass);
    mdMethod16.setValue(MdMethodInfo.NAME, "getViewQuery");
    mdMethod16.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a view query");
    mdMethod16.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod16.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod16);

    mdMethod17 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod17.setValue(MdMethodInfo.RETURN_TYPE, viewQueryClass);
    mdMethod17.setValue(MdMethodInfo.NAME, "getViewQueryRestrictRows");
    mdMethod17.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get a view query");
    mdMethod17.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod17.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod17);

    mdMethod18 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod18.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName());
    mdMethod18.setValue(MdMethodInfo.NAME, "getCollectionObjectCount");
    mdMethod18.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns the number of collections");
    mdMethod18.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod18.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod18);

    mdMethod19 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod19.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    mdMethod19.setValue(MdMethodInfo.NAME, "methodActorRead");
    mdMethod19.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tests the read permissions on a method actor.");
    mdMethod19.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethod19.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethod19);

    clientRequest.grantMethodPermission(newUser.getId(), mdMethod19.getId(), Operation.EXECUTE.name());

    methodActor = clientRequest.newBusiness(MethodActorInfo.CLASS);
    methodActor.setValue(MethodActorInfo.MD_METHOD, mdMethod19.getId());
    clientRequest.createBusiness(methodActor);

    mdMethodReturnByte = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethodReturnByte.setValue(MdMethodInfo.RETURN_TYPE, Byte.class.getName() + "[]");
    mdMethodReturnByte.setValue(MdMethodInfo.NAME, "getExcelFile");
    mdMethodReturnByte.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns a byte array that contains an Excel file.");
    mdMethodReturnByte.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethodReturnByte.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethodReturnByte);

    mdMethodReturnStream = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethodReturnStream.setValue(MdMethodInfo.RETURN_TYPE, InputStream.class.getName());
    mdMethodReturnStream.setValue(MdMethodInfo.NAME, "getFileStream");
    mdMethodReturnStream.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns an input stream contains an Excel file.");
    mdMethodReturnStream.setValue(MdMethodInfo.REF_MD_TYPE, collection.getId());
    mdMethodReturnStream.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdMethodReturnStream);
  }

  private static void addAttributes(String mdClassId)
  {
    BusinessDTO aBoolean = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    aBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    aBoolean.setValue(MdAttributeBooleanInfo.DISPLAY_LABEL, "A Boolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    aBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    aBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(aBoolean);

    BusinessDTO aCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    aCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    aCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    aCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    aCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(aCharacter);

    if (collection.getId().equals(mdClassId))
    {
      collectionMdAttributeCharacterDTO = aCharacter;
    }

    BusinessDTO aDouble = clientRequest.newBusiness(MdAttributeDoubleInfo.CLASS);
    aDouble.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    aDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    aDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdClassId);
    aDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    aDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    clientRequest.createBusiness(aDouble);

    BusinessDTO businessDTO = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    businessDTO.setValue(MdAttributeLongInfo.NAME, "aLong");
    businessDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    businessDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(businessDTO);
  }

  public static void classTearDown()
  {
    try
    {
      // Clear the stub source
      clientRequest.lock(collection);
      collection.setValue(MdEntityInfo.STUB_SOURCE, getBlankMethodStub());
      clientRequest.update(collection);

      clientRequest.delete(mdMethod6.getId());

      clientRequest.delete(reference.getId());

      clientRequest.delete(bag.getId());

      // Clear any dependency between mdView and collection before deleting
      // collection.
      clientRequest.lock(mdView);
      mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, getBlankViewQueryStub());
      clientRequest.update(mdView);

      clientRequest.delete(collection.getId());

      clientRequest.delete(mdUtil.getId());

      clientRequest.delete(stateEnum.getId());

      clientRequest.delete(states.getId());

      clientRequest.delete(mdView.getId());

      noPermissionSession.logout();

      clientRequest.delete(newUser.getId());
    }
    finally
    {
      systemSession.logout();
    }
  }

  public static void finalizeSetup()
  {
    // we set the stub source here since it references other objects that
    // were created during setup.
    clientRequest.lock(collection);
    String collectionStubSource = getMethodStub();
    collection.setValue(MdClassInfo.STUB_SOURCE, collectionStubSource);
    clientRequest.update(collection);

    california = clientRequest.newBusiness(pack + ".States");
    california.setValue(EnumerationMasterInfo.NAME, "CA");
    california.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    clientRequest.createBusiness(california);

    colorado = clientRequest.newBusiness(pack + ".States");
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    clientRequest.createBusiness(colorado);

    connecticut = clientRequest.newBusiness(pack + ".States");
    connecticut.setValue(EnumerationMasterInfo.NAME, "CT");
    connecticut.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Connecticut");
    clientRequest.createBusiness(connecticut);
  }

  private static String getMethodStub()
  {
    String[] collectionStubSource = { "package " + pack + ";", "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS, "{", "  public Collection()", "  {", "    super();", "  }", "  public static Collection get(String id)", "  {", "    return (Collection) " + Business.class.getName() + ".get(id);", "  }", "  public void testMethod(" + pack + ".Collection collection)", "  {", "     if(collection instanceof " + pack + ".Bag)", "     {",
        "       this.setALong(collection.getALong() + 10L);", "     }", "     else", "     {", "       this.setALong(collection.getALong());", "     }", "     this.apply();", "  }", "  public " + pack + ".Collection modifyNoPersist(" + pack + ".Collection collection)", "  {", "    this.setALong(77L);", "    collection.setALong(77L);", "    return collection;", "  }", "  @" + com.runwaysdk.business.rbac.Authenticate.class.getName(),
        "  public " + pack + ".Collection sortNumbers(Long[] numbers, Boolean ascending)", "  {", "     " + pack + ".Collection collection = new " + pack + ".Collection();", "     if(numbers == null)", "     {", "       collection.setALong(13L);", "     }", "     else if(numbers.length > 0)", "     {", "       collection.setALong(numbers[0]);", "     }", "     collection.setABoolean(ascending);", "     return collection;", "  }",
        "  public " + pack + ".Collection[] sortCollections(" + pack + ".Collection[] collections, String collectionName)", "  {", "     " + pack + ".Collection[] out = new " + pack + ".Collection[collections.length];", "     for(int i = 0; i < collections.length; i++)", "     {", "       out[i] = new Collection();", "       out[i].setACharacter(collectionName);", "       out[i].setALong(collections[i].getALong());", "     }", "     return out;", "  }",
        "  public java.lang.String[][] testMultiArray(" + pack + ".Collection[][][][] collection4)", "  {", "    String[][] output = new String[2][];", "    output[0] = new String[]{\"Yo my nizzle\", \"Leroy Im witha or against ya.\"};", "    output[1] = new String[]{collection4.getClass().getName(), collection4.getClass().getSimpleName()};", "    return output;", "  }", "  public void poopNothing()", "  {", "  }",
        "  public " + pack + ".Reference[] getReferences(" + pack + ".Reference reference)", "  {", "    " + pack + ".Reference[] array = new " + pack + ".Reference[5];", "    for(int i = 0; i < 5; i++)", "      array[i] = reference;", "    return array;", "  }", "  public " + pack + ".AllStates[] getStates(" + pack + ".AllStates state)", "  {", "    " + pack + ".AllStates[] array = new " + pack + ".AllStates[5];", "    for(int i = 0; i < 5; i++)", "      array[i] = state;",
        "    return array;", "  }", "  @" + com.runwaysdk.business.rbac.Authenticate.class.getName(), "  public static java.lang.Integer[] sortIntegers(java.lang.Integer[] integers)", "  {", "    if(integers == null) return null;", "    java.lang.Integer[] array = new Integer[integers.length];", "    java.util.List<Integer> list = java.util.Arrays.asList(integers);", "    java.util.Collections.sort(list);", "    return list.toArray(array);", "  }",
        "  public static java.util.Date[] getDates(java.util.Date date)", "  {", "    java.util.Date[] array = new java.util.Date[4];", "    array[0] = new java.util.Date(date.getTime() + 0L);", "    array[1] = new java.util.Date(date.getTime() + 10L);", "    array[2] = new java.util.Date(date.getTime() + 20L);", "    array[3] = new java.util.Date(date.getTime() + 30L);", "    return array;", "  }", "  public " + utilType + " getUtil(" + utilType + " util)", "  {", "    return util;", "  }",
        "  public " + viewType + " getView(" + viewType + " view)", "  {", "    return view;", "  }", "  public " + utilType + "[] utilArray(" + utilType + "[] utilArray)", "  {", "     " + utilType + "[] out = new " + utilType + "[utilArray.length];", "     for(int i = 0; i< utilArray.length; i++)", "     {", "       out[i] = new " + utilType + "();", "       out[i].setACharacter(utilArray[i].getACharacter());", "     }", "     return out;", "  }",
        "  public " + viewType + "[] viewArray(" + viewType + "[] viewArray)", "  {", "     " + viewType + "[] out = new " + viewType + "[viewArray.length];", "     for(int i = 0; i< viewArray.length; i++)", "     {", "       out[i] = new " + viewType + "();", "       out[i].setACharacter(viewArray[i].getACharacter());", "     }", "     return out;", "  }", "  public static " + collectionQueryClass + " getCollectionQuery()", "  {",
        "     " + QueryFactory.class.getName() + " f = new " + QueryFactory.class.getName() + "();", "     " + collectionQueryClass + " query = new " + collectionQueryClass + "(f);", "     ", "     return query;", "  }", "  public static " + viewQueryClass + " getViewQuery()", "  {", "     " + QueryFactory.class.getName() + " f = new " + QueryFactory.class.getName() + "();", "     " + viewQueryClass + " query = new " + viewQueryClass + "(f);", "     ", "     return query;", "  }",
        "  public static " + collectionQueryClass + " getCollectionQueryRestrictRows()", "  {", "     " + QueryFactory.class.getName() + " f = new " + QueryFactory.class.getName() + "();", "     " + collectionQueryClass + " query = new " + collectionQueryClass + "(f);", "     ", "     query.restrictRows(2, 1);", "     return query;", "  }", "  public static " + viewQueryClass + " getViewQueryRestrictRows()", "  {",
        "     " + QueryFactory.class.getName() + " f = new " + QueryFactory.class.getName() + "();", "     " + viewQueryClass + " query = new " + viewQueryClass + "(f);", "     ", "     query.restrictRows(2, 1);", "     return query;", "  }", "  public " + pack + ".AllStates[] returnStates(" + pack + ".AllStates[] states)", "  {", "    return states;", "  }", "  public static " + Integer.class.getName() + " getCollectionObjectCount()", "  {",
        "    " + QueryFactory.class.getName() + " queryFactory = new " + QueryFactory.class.getName() + "();", "    " + BusinessQuery.class.getName() + " bq = queryFactory.businessQuery(\"" + collectionType + "\");", "", "    long recordCount = bq.getCount();", "    return (int)recordCount;", "  }", "  public static " + Byte.class.getName() + "[] getExcelFile()", "  {", "    " + QueryFactory.class.getName() + " queryFactory = new " + QueryFactory.class.getName() + "();",
        "    " + BusinessQuery.class.getName() + " bq = queryFactory.businessQuery(\"" + collectionType + "\");", "", "    " + ValueQuery.class.getName() + " valueQuery = new " + ValueQuery.class.getName() + "(queryFactory);", "", "    valueQuery.SELECT(bq.aCharacter(\"aCharacter\"));", "", "    " + ValueQueryExcelExporter.class.getName() + " excelExporter = new " + ValueQueryExcelExporter.class.getName() + "(valueQuery, \"Test Sheet\");", "    return excelExporter.export();", "", "  }",
        "  public static " + InputStream.class.getName() + " getFileStream()", "  {", "    " + QueryFactory.class.getName() + " queryFactory = new " + QueryFactory.class.getName() + "();", "    " + BusinessQuery.class.getName() + " bq = queryFactory.businessQuery(\"" + collectionType + "\");", "", "    " + ValueQuery.class.getName() + " valueQuery = new " + ValueQuery.class.getName() + "(queryFactory);", "", "    valueQuery.SELECT(bq.aCharacter(\"aCharacter\"));", "",
        "    " + ValueQueryExcelExporter.class.getName() + " excelExporter = new " + ValueQueryExcelExporter.class.getName() + "(valueQuery, \"Test Sheet\");", "", "    try", "    {", "      return new " + ByteArrayInputStream.class.getName() + " ( " + FileIO.class.getName() + ".convertFromBytes(excelExporter.export()));", "    }", "    catch(" + Exception.class.getName() + " e)", "    { ", "      throw new " + RuntimeException.class.getName() + "(e);", "    }", "", "  }",
        "  @" + com.runwaysdk.business.rbac.Authenticate.class.getName(), "  public static " + pack + ".Collection methodActorRead()", "  {", "     " + pack + ".Collection collection = new " + pack + ".Collection();", "     collection.setALong2(13L);", "     collection.apply();", "     return collection;", "  }", "}" };

    String source = "";
    for (String s : collectionStubSource)
    {
      source += s + "\n";
    }

    return source;
  }

  private static String getBlankMethodStub()
  {
    String[] collectionStubSource = { "package " + pack + ";", "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS, "{", "  public Collection()", "  {", "    super();", "  }", "}" };

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

    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "\n" + "  private " + collectionQueryClass + " collectionQuery;\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "     \n" + "     collectionQuery = new " + collectionQueryClass
        + "(componentQueryFactory);\n" + "\n" + "     this.map(" + aBooleanConst + ", collectionQuery.getABoolean());\n" + "     this.map(" + aCharacterConst + ", collectionQuery.getACharacter());\n" + "     this.map(" + aDoubleConst + ", collectionQuery.getADouble());\n" + "     this.map(" + aLongConst + ", collectionQuery.getALong());\n" + "\n" + "     this.buildSelectClause();\n" + "  }\n" + "}\n";

    return queryStubSource;
  }

  private static String getBlankViewQueryStub()
  {
    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "  }\n" + "}\n";

    return queryStubSource;
  }
}
