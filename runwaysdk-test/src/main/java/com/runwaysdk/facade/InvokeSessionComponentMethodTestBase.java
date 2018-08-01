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
package com.runwaysdk.facade;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdSessionInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TypeGeneratorInfo;
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
import com.runwaysdk.dataaccess.metadata.MdSessionDAO;
import com.runwaysdk.dataaccess.metadata.MdUtilDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;

public class InvokeSessionComponentMethodTestBase
{
  protected static ClientSession    systemSession;

  protected static ClientRequestIF  clientRequest     = null;

  protected static final String     pack              = "test.generated";

  protected static String           sessionTypeName   = "Session";

  protected static String           bagTypeName       = "Bag";

  protected static String           stateTypeName     = "AllStates";

  protected static String           utilParamTypeName = "UtilParam";

  protected static String           viewParamTypeName = "ViewParam";

  protected static String           sessionType       = pack + "." + sessionTypeName;

  protected static String           bagType           = pack + "." + bagTypeName;

  protected static String           bagDTO            = bagType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static String           stateType         = pack + "." + stateTypeName;

  protected static String           sessionDTOtype    = sessionType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdSessionDAO     mdSessionDTO;

  protected static MdSessionDAO     bag;

  protected static MdBusinessDAO    states;

  protected static MdEnumerationDAO stateEnum;

  protected static BusinessDAO      colorado;

  protected static String           utilParamType     = pack + "." + utilParamTypeName;

  protected static String           utilParamDTO      = utilParamType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdUtilDAO        mdUtilParam;

  protected static String           viewParamType     = pack + "." + viewParamTypeName;

  protected static String           viewParamDTO      = viewParamType + ComponentDTOGenerator.DTO_SUFFIX;

  protected static MdViewDAO        mdViewParam;

  protected static MdMethodDAO      mdMethod1;

  protected static MdMethodDAO      mdMethod2;

  protected static MdMethodDAO      mdMethod3;

  protected static MdMethodDAO      mdMethod4;

  protected static MdMethodDAO      mdMethod5;

  protected static MdMethodDAO      mdMethod7;

  protected static MdMethodDAO      mdMethod8;

  protected static MdMethodDAO      mdMethod9;

  protected static MdMethodDAO      mdMethod10;

  protected static MdMethodDAO      mdMethod11;

  protected static MdMethodDAO      mdMethod12;

  protected static MdMethodDAO      mdMethod13;

  protected static String           superClassField;

  protected static String           getterMethodImplementation;

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return clientSession.getRequest();
  }

  @Transaction
  public static void modelSetup()
  {
    mdSessionDTO.setValue(MdSessionInfo.NAME, sessionTypeName);
    mdSessionDTO.setValue(MdSessionInfo.PACKAGE, pack);
    mdSessionDTO.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdSessionDTO.setValue(MdSessionInfo.DISPLAY_LABEL, "All Attributes");
    mdSessionDTO.apply();

    MdAttributeBooleanDAO aBoolean = MdAttributeBooleanDAO.newInstance();
    aBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    aBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    aBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdSessionDTO.getId());
    aBoolean.apply();

    MdAttributeCharacterDAO aCharacter = MdAttributeCharacterDAO.newInstance();
    aCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    aCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    aCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    aCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdSessionDTO.getId());
    aCharacter.apply();

    MdAttributeDoubleDAO aDouble = MdAttributeDoubleDAO.newInstance();
    aDouble.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    aDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    aDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdSessionDTO.getId());
    aDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    aDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    aDouble.apply();

    MdAttributeLongDAO aLong = MdAttributeLongDAO.newInstance();
    aLong.setValue(MdAttributeLongInfo.NAME, "aLong");
    aLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    aLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdSessionDTO.getId());
    aLong.apply();

    mdUtilParam = MdUtilDAO.newInstance();
    mdUtilParam.setValue(MdUtilInfo.NAME, utilParamTypeName);
    mdUtilParam.setValue(MdUtilInfo.PACKAGE, pack);
    mdUtilParam.setValue(MdUtilInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdUtilParam.setStructValue(MdUtilInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util class");
    mdUtilParam.apply();

    MdAttributeCharacterDAO utilCharacter = MdAttributeCharacterDAO.newInstance();
    utilCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    utilCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    utilCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    utilCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdUtilParam.getId());
    utilCharacter.apply();

    mdViewParam = MdViewDAO.newInstance();
    mdViewParam.setValue(MdViewInfo.NAME, viewParamTypeName);
    mdViewParam.setValue(MdViewInfo.PACKAGE, pack);
    mdViewParam.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdViewParam.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View class");
    mdViewParam.apply();

    MdAttributeCharacterDAO viewCharacter = MdAttributeCharacterDAO.newInstance();
    viewCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    viewCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    viewCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    viewCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdViewParam.getId());
    viewCharacter.apply();

    bag.setValue(MdSessionInfo.NAME, bagTypeName);
    bag.setValue(MdSessionInfo.PACKAGE, pack);
    bag.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    bag.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Bag");
    bag.setValue(superClassField, mdSessionDTO.getId());
    bag.apply();

    MdBusinessDAOIF enumMaster = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    states = MdBusinessDAO.newInstance();
    states.setValue(MdBusinessInfo.NAME, "States");
    states.setValue(MdBusinessInfo.PACKAGE, pack);
    states.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    states.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    states.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMaster.getId());
    states.apply();

    stateEnum = MdEnumerationDAO.newInstance();
    stateEnum.setValue(MdEnumerationInfo.NAME, stateTypeName);
    stateEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    stateEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, states.getId());
    stateEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States");
    stateEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    stateEnum.apply();

    mdMethod1 = MdMethodDAO.newInstance();
    mdMethod1.setValue(MdMethodInfo.RETURN_TYPE, sessionType);
    mdMethod1.setValue(MdMethodInfo.NAME, "sortNumbers");
    mdMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Number");
    mdMethod1.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod1.apply();

    MdParameterDAO mdParameter = MdParameterDAO.newInstance();
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Boolean");
    mdParameter.setValue(MdParameterInfo.NAME, "ascending");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ascending Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "1");
    mdParameter.apply();

    MdParameterDAO mdParameter2 = MdParameterDAO.newInstance();
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Long" + "[]");
    mdParameter2.setValue(MdParameterInfo.NAME, "numbers");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Numbers Parameter");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod1.getId());
    mdParameter2.setValue(MdParameterInfo.ORDER, "0");
    mdParameter2.apply();

    mdMethod2 = MdMethodDAO.newInstance();
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setValue(MdMethodInfo.NAME, "poopNothing");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Poop Nothing");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod2.apply();

    mdMethod3 = MdMethodDAO.newInstance();
    mdMethod3.setValue(MdMethodInfo.RETURN_TYPE, sessionType + "[]");
    mdMethod3.setValue(MdMethodInfo.NAME, "sortSessions");
    mdMethod3.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Sessions");
    mdMethod3.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod3.apply();

    MdParameterDAO mdParameter3 = MdParameterDAO.newInstance();
    mdParameter3.setValue(MdParameterInfo.TYPE, sessionType + "[]");
    mdParameter3.setValue(MdParameterInfo.NAME, "sessions");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sessions Parameter");
    mdParameter3.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getId());
    mdParameter3.setValue(MdParameterInfo.ORDER, "0");
    mdParameter3.apply();

    MdParameterDAO mdParameter5 = MdParameterDAO.newInstance();
    mdParameter5.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter5.setValue(MdParameterInfo.NAME, "sessionName");
    mdParameter5.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Session Name Parameter");
    mdParameter5.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod3.getId());
    mdParameter5.setValue(MdParameterInfo.ORDER, "1");
    mdParameter5.apply();

    mdMethod4 = MdMethodDAO.newInstance();
    mdMethod4.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod4.setValue(MdMethodInfo.NAME, "testMethod");
    mdMethod4.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod4.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod4.apply();

    MdParameterDAO mdParameter4 = MdParameterDAO.newInstance();
    mdParameter4.setValue(MdParameterInfo.TYPE, sessionType);
    mdParameter4.setValue(MdParameterInfo.NAME, "mdSessionDTO");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Session Parameter");
    mdParameter4.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod4.getId());
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    mdParameter4.apply();

    mdMethod5 = MdMethodDAO.newInstance();
    mdMethod5.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.String[][]");
    mdMethod5.setValue(MdMethodInfo.NAME, "testMultiArray");
    mdMethod5.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod5.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod5.apply();

    MdParameterDAO mdParameter6 = MdParameterDAO.newInstance();
    mdParameter6.setValue(MdParameterInfo.TYPE, sessionType + "[][][][]");
    mdParameter6.setValue(MdParameterInfo.NAME, "session4");
    mdParameter6.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Session Parameter");
    mdParameter6.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod5.getId());
    mdParameter6.setValue(MdParameterInfo.ORDER, "0");
    mdParameter6.apply();

    mdMethod7 = MdMethodDAO.newInstance();
    mdMethod7.setValue(MdMethodInfo.RETURN_TYPE, stateType + "[]");
    mdMethod7.setValue(MdMethodInfo.NAME, "getStates");
    mdMethod7.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test States");
    mdMethod7.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod7.apply();

    MdParameterDAO mdParameter9 = MdParameterDAO.newInstance();
    mdParameter9.setValue(MdParameterInfo.TYPE, stateType);
    mdParameter9.setValue(MdParameterInfo.NAME, "state");
    mdParameter9.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Parameter");
    mdParameter9.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod7.getId());
    mdParameter9.setValue(MdParameterInfo.ORDER, "0");
    mdParameter9.apply();

    mdMethod8 = MdMethodDAO.newInstance();
    mdMethod8.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer[]");
    mdMethod8.setValue(MdMethodInfo.NAME, "sortIntegers");
    mdMethod8.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Integers");
    mdMethod8.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod8.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod8.apply();

    MdParameterDAO mdParameter10 = MdParameterDAO.newInstance();
    mdParameter10.setValue(MdParameterInfo.TYPE, "java.lang.Integer[]");
    mdParameter10.setValue(MdParameterInfo.NAME, "integers");
    mdParameter10.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integers Parameter");
    mdParameter10.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod8.getId());
    mdParameter10.setValue(MdParameterInfo.ORDER, "0");
    mdParameter10.apply();

    mdMethod9 = MdMethodDAO.newInstance();
    mdMethod9.setValue(MdMethodInfo.RETURN_TYPE, "java.util.Date[]");
    mdMethod9.setValue(MdMethodInfo.NAME, "getDates");
    mdMethod9.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Dates");
    mdMethod9.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod9.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    mdMethod9.apply();

    MdParameterDAO mdParameter11 = MdParameterDAO.newInstance();
    mdParameter11.setValue(MdParameterInfo.TYPE, "java.util.Date");
    mdParameter11.setValue(MdParameterInfo.NAME, "date");
    mdParameter11.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date Parameter");
    mdParameter11.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod9.getId());
    mdParameter11.setValue(MdParameterInfo.ORDER, "0");
    mdParameter11.apply();

    mdMethod10 = MdMethodDAO.newInstance();
    mdMethod10.setValue(MdMethodInfo.RETURN_TYPE, utilParamType);
    mdMethod10.setValue(MdMethodInfo.NAME, "getUtil");
    mdMethod10.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object");
    mdMethod10.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod10.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod10.apply();

    MdParameterDAO mdParameter12 = MdParameterDAO.newInstance();
    mdParameter12.setValue(MdParameterInfo.TYPE, utilParamType);
    mdParameter12.setValue(MdParameterInfo.NAME, "util");
    mdParameter12.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object");
    mdParameter12.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod10.getId());
    mdParameter12.setValue(MdParameterInfo.ORDER, "0");
    mdParameter12.apply();

    mdMethod11 = MdMethodDAO.newInstance();
    mdMethod11.setValue(MdMethodInfo.RETURN_TYPE, viewParamType);
    mdMethod11.setValue(MdMethodInfo.NAME, "getView");
    mdMethod11.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object");
    mdMethod11.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod11.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod11.apply();

    MdParameterDAO mdParameter13 = MdParameterDAO.newInstance();
    mdParameter13.setValue(MdParameterInfo.TYPE, viewParamType);
    mdParameter13.setValue(MdParameterInfo.NAME, "view");
    mdParameter13.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object");
    mdParameter13.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod11.getId());
    mdParameter13.setValue(MdParameterInfo.ORDER, "0");
    mdParameter13.apply();

    mdMethod12 = MdMethodDAO.newInstance();
    mdMethod12.setValue(MdMethodInfo.RETURN_TYPE, utilParamType + "[]");
    mdMethod12.setValue(MdMethodInfo.NAME, "utilArray");
    mdMethod12.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get Util Object Array");
    mdMethod12.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod12.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod12.apply();

    MdParameterDAO mdParameter14 = MdParameterDAO.newInstance();
    mdParameter14.setValue(MdParameterInfo.TYPE, utilParamType + "[]");
    mdParameter14.setValue(MdParameterInfo.NAME, "utilArray");
    mdParameter14.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Util Object Array");
    mdParameter14.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod12.getId());
    mdParameter14.setValue(MdParameterInfo.ORDER, "0");
    mdParameter14.apply();

    mdMethod13 = MdMethodDAO.newInstance();
    mdMethod13.setValue(MdMethodInfo.RETURN_TYPE, viewParamType + "[]");
    mdMethod13.setValue(MdMethodInfo.NAME, "viewArray");
    mdMethod13.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Get View Object Array");
    mdMethod13.setValue(MdMethodInfo.REF_MD_TYPE, mdSessionDTO.getId());
    mdMethod13.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    mdMethod13.apply();

    MdParameterDAO mdParameter15 = MdParameterDAO.newInstance();
    mdParameter15.setValue(MdParameterInfo.TYPE, viewParamType + "[]");
    mdParameter15.setValue(MdParameterInfo.NAME, "viewArray");
    mdParameter15.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View Object Array");
    mdParameter15.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod13.getId());
    mdParameter15.setValue(MdParameterInfo.ORDER, "0");
    mdParameter15.apply();

    // we set the stub source here since it references other objects that
    // were created during setup.
    String sessionStubSource = getMethodStub();
    mdSessionDTO.setValue(MdClassInfo.STUB_SOURCE, sessionStubSource);
    mdSessionDTO.apply();

    colorado = BusinessDAO.newInstance(pack + ".States");
    colorado.setValue(EnumerationMasterInfo.NAME, "CO");
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    colorado.apply();
  }

  @Transaction
  public static void modelTearDown()
  {
    // Clear the stub source
    mdSessionDTO.setValue(MdClassInfo.STUB_SOURCE, getBlankMethodStub());
    mdSessionDTO.apply();

    TestFixtureFactory.delete(bag);

    TestFixtureFactory.delete(mdSessionDTO);

    TestFixtureFactory.delete(mdUtilParam);

    TestFixtureFactory.delete(mdViewParam);

    TestFixtureFactory.delete(stateEnum);

    TestFixtureFactory.delete(states);
  }

  private static String getMethodStub()
  {
    String[] sessionStubSource = { "package " + pack + ";", "public class " + sessionTypeName + " extends " + sessionTypeName + TypeGeneratorInfo.BASE_SUFFIX, "{", "  public " + sessionTypeName + "()", "  {", "    super();", "  }", "  public static " + sessionTypeName + " get(String id)", "  {", getterMethodImplementation, "  }", "  public void testMethod(" + sessionType + " session)", "  {", "     if(session instanceof " + bagType + ")", "     {", "       this.setALong(session.getALong() + 10L);", "     }", "     else", "     {", "       this.setALong(session.getALong());", "     }", "     this.apply();", "  }", "  public " + sessionType + " sortNumbers(Long[] numbers, Boolean ascending)", "  {", "     " + sessionType + " session = new " + sessionType + "();",
        "     if(numbers.length > 0)", "     {", "       session.setALong(numbers[0]);", "     }", "     session.setABoolean(ascending);", "     return session;", "  }", "  public " + sessionType + "[] sortSessions(" + sessionType + "[] sessions, String sessionName)", "  {", "     " + sessionType + "[] out = new " + sessionType + "[sessions.length];", "     for(int i = 0; i < sessions.length; i++)", "     {", "       out[i] = new " + sessionType + "();", "       out[i].setACharacter(sessionName);", "       out[i].setALong(sessions[i].getALong());", "     }", "     return out;", "  }", "  public java.lang.String[][] testMultiArray(" + sessionType + "[][][][] session4)", "  {", "    String[][] output = new String[2][];",
        "    output[0] = new String[]{\"Yo my nizzle\", \"Leroy Im witha or against ya.\"};", "    output[1] = new String[]{session4.getClass().getName(), session4.getClass().getSimpleName()};",

        "    return output;", "  }", "  public void poopNothing()", "  {", "  }", "  public " + stateType + "[] getStates(" + stateType + " state)", "  {", "    " + stateType + "[] array = new " + stateType + "[5];", "    for(int i = 0; i < 5; i++)", "      array[i] = state;", "    return array;", "  }", "  public static java.lang.Integer[] sortIntegers(java.lang.Integer[] integers)", "  {", "    java.lang.Integer[] array = new Integer[integers.length];", "    java.util.List<Integer> list = java.util.Arrays.asList(integers);", "    java.util.Collections.sort(list);", "    return list.toArray(array);", "  }", "  public static java.util.Date[] getDates(java.util.Date date)", "  {", "    java.util.Date[] array = new java.util.Date[4];",
        "    array[0] = new java.util.Date(date.getTime() + 0L);", "    array[1] = new java.util.Date(date.getTime() + 10L);", "    array[2] = new java.util.Date(date.getTime() + 20L);", "    array[3] = new java.util.Date(date.getTime() + 30L);", "    return array;", "  }", "  public " + utilParamType + " getUtil(" + utilParamType + " util)", "  {", "    return util;", "  }", "  public " + viewParamType + " getView(" + viewParamType + " view)", "  {", "    return view;", "  }", "  public " + utilParamType + "[] utilArray(" + utilParamType + "[] utilArray)", "  {", "     " + utilParamType + "[] out = new " + utilParamType + "[utilArray.length];", "     for(int i = 0; i< utilArray.length; i++)", "     {", "       out[i] = new " + utilParamType + "();",
        "       out[i].setACharacter(utilArray[i].getACharacter());", "     }", "     return out;", "  }", "  public " + viewParamType + "[] viewArray(" + viewParamType + "[] viewArray)", "  {", "     " + viewParamType + "[] out = new " + viewParamType + "[viewArray.length];", "     for(int i = 0; i< viewArray.length; i++)", "     {", "       out[i] = new " + viewParamType + "();", "       out[i].setACharacter(viewArray[i].getACharacter());", "     }", "     return out;", "  }", "}" };

    String source = "";
    for (String s : sessionStubSource)
    {
      source += s + "\n";
    }

    return source;
  }

  private static String getBlankMethodStub()
  {
    String[] sessionStubSource = { "package " + pack + ";", "public class " + sessionTypeName + " extends " + sessionTypeName + TypeGeneratorInfo.BASE_SUFFIX, "{", "  public " + sessionTypeName + "()", "  {", "    super();", "  }", "}" };

    String source = "";
    for (String s : sessionStubSource)
    {
      source += s + "\n";
    }

    return source;
  }
}
