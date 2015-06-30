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
package com.runwaysdk.facade;

import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.openqa.selenium.server.SeleniumServer;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.Util;
import com.runwaysdk.business.View;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdUtilInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestProperties;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.session.InvalidLoginException;
import com.thoughtworks.selenium.DefaultSelenium;

public class SeleniumTest extends TestCase
{

  private ClientSession systemSession;

  /**
   * The clientRequest required to set up the test.
   */
  private ClientRequestIF clientRequest;

  // server instance
  private DefaultSelenium selenium = null;

  private SeleniumServer server      = null;

  // selenium settings
  private int            port        = TestProperties.getSeleniumPort();

  private String         localServer = TestProperties.getSeleniumServer();

  private String         address     = TestProperties.getWebtestAddress();

  private String         webapp      = TestProperties.getWebtestWebapp();

  protected String       browser = SeleniumTestSuite.FIREFOX; // default browser

  // metadata
  private static final String PACKAGE = "com.runway.webtest";

  private BusinessDTO noPermissions;
  private BusinessDTO testClassMd;
  private BusinessDTO subClassMd;
  private BusinessDTO refClassMd;
  private BusinessDTO befriendsMd;
  private BusinessDTO mdFacade;
  private BusinessDTO stateEnumMdBusiness;
  private BusinessDTO structClassMd;
  private BusinessDTO mdView;
  private BusinessDTO mdUtil;
  private BusinessDTO mdException;
  private BusinessDTO mdProblem;

  /**
   * The time to wait for each test to run. This is needed
   * to allow any AJAX operations to respond.
   */
  private static final int WAIT_IN_SECONDS = 5;

  /**
   * Sets up the test.
   */
  public void setUp() throws Exception
  {
    systemSession =
      ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[]{CommonProperties.getDefaultLocale()});
    clientRequest = systemSession.getRequest();

    port        = TestProperties.getSeleniumPort();
    localServer = TestProperties.getSeleniumServer();
    address = TestProperties.getWebtestAddress();
    webapp = TestProperties.getWebtestWebapp();

    buildMetadata();

    finalizeSetup();

    systemSession.logout();

    // start the server
    // IMPORTANT: These lines were commented out because the API changed and we updated the jars when
    //              moving to maven.
    //server = new SeleniumServer(port);
    //server.start();

    // start the test session
    selenium = new DefaultSelenium(localServer, port, "*" + browser, address);
    selenium.start();
  }

  /**
   * Builds the metadata for the ajax test.
   */
  private void buildMetadata()
  {
    /* Define a problem and exception */
    mdException = clientRequest.newBusiness(MdExceptionInfo.CLASS);
    mdException.setValue(MdExceptionInfo.NAME, "TestException");
    mdException.setValue(MdExceptionInfo.PACKAGE, PACKAGE);
    mdException.setStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A test exception");
    mdException.setStructValue(MdExceptionInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "This is a message from TestException");
    clientRequest.createBusiness(mdException);

    mdProblem = clientRequest.newBusiness(MdProblemInfo.CLASS);
    mdProblem.setValue(MdProblemInfo.NAME, "TestProblem");
    mdProblem.setValue(MdProblemInfo.PACKAGE, PACKAGE);
    mdProblem.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A test problem");
    mdProblem.setStructValue(MdProblemInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "This is a message from TestProblem");
    clientRequest.createBusiness(mdProblem);

    // this user will be granted permissions in the AJAX layer
    noPermissions = clientRequest.newBusiness(UserInfo.CLASS);
    noPermissions.setValue(UserInfo.USERNAME, "NoPerm"); // FIXME user/pass is really stored in AjaxTestConstant.java in AjaxTest project
    noPermissions.setValue(UserInfo.PASSWORD, "Worthless");
    noPermissions.setValue(UserInfo.SESSION_LIMIT, "100000");
    clientRequest.createBusiness(noPermissions);

    testClassMd = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    testClassMd.setValue(MdBusinessInfo.NAME,                   "TestClass");
    testClassMd.setValue(MdBusinessInfo.PACKAGE,                PACKAGE);
    testClassMd.setValue(MdBusinessInfo.REMOVE,                 MdAttributeBooleanInfo.TRUE);
    testClassMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE, "A Class");
    testClassMd.setStructValue(MdBusinessInfo.DESCRIPTION,      MdAttributeLocalInfo.DEFAULT_LOCALE,      "Web Test Class");
    testClassMd.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.TRUE);
    testClassMd.setValue(MdBusinessInfo.ABSTRACT,               MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(testClassMd);
    String testClassDefinesType = testClassMd.getValue(MdTypeInfo.PACKAGE) + "." + testClassMd.getValue(MdBusinessInfo.NAME);

    // Create the MasterTestSetup.TEST_CLASS class.
    subClassMd = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    subClassMd.setValue(MdBusinessInfo.NAME,                   "SubClass");
    subClassMd.setValue(MdBusinessInfo.PACKAGE,                PACKAGE);
    subClassMd.setValue(MdBusinessInfo.REMOVE,                 MdAttributeBooleanInfo.TRUE);
    subClassMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE,      "A subclass");
    subClassMd.setStructValue(MdBusinessInfo.DESCRIPTION,      MdAttributeLocalInfo.DEFAULT_LOCALE,      "Web Test SubClass");
    subClassMd.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.TRUE);
    subClassMd.setValue(MdBusinessInfo.ABSTRACT,               MdAttributeBooleanInfo.FALSE);
    subClassMd.setValue(MdBusinessInfo.SUPER_MD_BUSINESS,      testClassMd.getId());
    clientRequest.createBusiness(subClassMd);

    // Add atributes to the subclass
    BusinessDTO mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "subCharacter");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "A character for the subclass");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "Sub char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, subClassMd.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    BusinessDTO instanceProblemMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceProblemMethod.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceProblemMethod.setValue(MdMethodInfo.NAME, "instanceForceProblem");
    instanceProblemMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    instanceProblemMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an MdProblem.");
    clientRequest.createBusiness(instanceProblemMethod);

    BusinessDTO instanceExMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceExMethod.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceExMethod.setValue(MdMethodInfo.NAME, "instanceForceException");
    instanceExMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    instanceExMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,  "Throws an MdException.");
    clientRequest.createBusiness(instanceExMethod);

    BusinessDTO instanceExMethod1 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceExMethod1.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceExMethod1.setValue(MdMethodInfo.NAME, "instanceForceException1");
    instanceExMethod1.setValue(MdMethodInfo.RETURN_TYPE, "void");
    instanceExMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws a hard-coded exception.");
    clientRequest.createBusiness(instanceExMethod1);

    BusinessDTO instanceExMethod2 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceExMethod2.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceExMethod2.setValue(MdMethodInfo.NAME, "instanceForceException2");
    instanceExMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    instanceExMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an exception.");
    clientRequest.createBusiness(instanceExMethod2);

    BusinessDTO instanceExMethod3 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceExMethod3.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceExMethod3.setValue(MdMethodInfo.NAME, "instanceForceException3");
    instanceExMethod3.setValue(MdMethodInfo.RETURN_TYPE, "void");
    instanceExMethod3.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an exception that is not in the javascript layer.");
    clientRequest.createBusiness(instanceExMethod3);

    BusinessDTO instanceMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceMethod.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceMethod.setValue(MdMethodInfo.NAME, "createInstances");
    instanceMethod.setValue(MdMethodInfo.RETURN_TYPE, testClassDefinesType + "[]");
    instanceMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Method");
    clientRequest.createBusiness(instanceMethod);

    BusinessDTO mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, instanceMethod.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Number of instances");
    clientRequest.createBusiness(mdParameter);

    BusinessDTO staticMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    staticMethod.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    staticMethod.setValue(MdMethodInfo.NAME, "doubleAnInteger");
    staticMethod.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    staticMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Doubles an Integer");
    staticMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    clientRequest.createBusiness(staticMethod);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, staticMethod.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The value to double");
    clientRequest.createBusiness(mdParameter);

    // create an MdMethod with 2 parameters (to test javascript's ability to loop through params)
    BusinessDTO instanceMethod2 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    instanceMethod2.setValue(MdMethodInfo.REF_MD_TYPE, testClassMd.getId());
    instanceMethod2.setValue(MdMethodInfo.NAME, "compareIntegers");
    instanceMethod2.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    instanceMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Compares two ints");
    instanceMethod2.setValue(MdMethodInfo.RETURN_TYPE, Boolean.class.getName());
    clientRequest.createBusiness(instanceMethod2);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, instanceMethod2.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num1");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int 1");
    clientRequest.createBusiness(mdParameter);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, instanceMethod2.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num2");
    mdParameter.setValue(MdParameterInfo.ORDER, "1");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "int 2");
    clientRequest.createBusiness(mdParameter);

    // Create an new class (MasterTestSetup.REFERENCE_CLASS) that we can reference with Reference Fields
    refClassMd = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    refClassMd.setValue(MdBusinessInfo.NAME,                   "RefClass");
    refClassMd.setValue(MdBusinessInfo.PACKAGE,                PACKAGE);
    refClassMd.setValue(MdBusinessInfo.REMOVE,                 MdAttributeBooleanInfo.TRUE);
    refClassMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL,    MdAttributeLocalInfo.DEFAULT_LOCALE, "A Ref Class");
    refClassMd.setStructValue(MdBusinessInfo.DESCRIPTION,       MdAttributeLocalInfo.DEFAULT_LOCALE,     "Web Test Reference Class");
    refClassMd.setValue(MdBusinessInfo.EXTENDABLE,             MdAttributeBooleanInfo.TRUE);
    refClassMd.setValue(MdBusinessInfo.ABSTRACT,               MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(refClassMd);

    // Add an attribute to the reference type
    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "refChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "A string");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "I wish I was a reference field!");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, refClassMd.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    befriendsMd = clientRequest.newBusiness(MdRelationshipInfo.CLASS);
    befriendsMd.setValue(MdRelationshipInfo.NAME,                 "Befriends");
    befriendsMd.setValue(MdRelationshipInfo.PACKAGE,              PACKAGE);
    befriendsMd.setValue(MdRelationshipInfo.COMPOSITION,          MdAttributeBooleanInfo.FALSE);
    befriendsMd.setStructValue(MdRelationshipInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "TestClass befriends RefClass relationship");
    befriendsMd.setStructValue(MdRelationshipInfo.DESCRIPTION,    MdAttributeLocalInfo.DEFAULT_LOCALE,      "They have to be friends before TestClass can reference RefClass, right?");
    befriendsMd.setValue(MdRelationshipInfo.REMOVE,               MdAttributeBooleanInfo.TRUE);
    befriendsMd.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS,   testClassMd.getId());
    befriendsMd.setValue(MdRelationshipInfo.PARENT_CARDINALITY,   "*");
    befriendsMd.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE, "testclass parent");
    befriendsMd.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS,    refClassMd.getId());
    befriendsMd.setValue(MdRelationshipInfo.CHILD_CARDINALITY,    "*");
    befriendsMd.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "refclass child");
    befriendsMd.setValue(MdRelationshipInfo.PARENT_METHOD,        "TestClass");
    befriendsMd.setValue(MdRelationshipInfo.CHILD_METHOD,         "RefClass");
    befriendsMd.setValue(MdRelationshipInfo.EXTENDABLE,           MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(befriendsMd);

    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "relChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,       "Relationship Character");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "I'm a rel char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, befriendsMd.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    structClassMd = clientRequest.newBusiness(MdStructInfo.CLASS);
    structClassMd.setValue(MdStructInfo.NAME,                 "TestStruct");
    structClassMd.setValue(MdStructInfo.PACKAGE,              PACKAGE);
    structClassMd.setStructValue(MdStructInfo.DESCRIPTION,    MdAttributeLocalInfo.DEFAULT_LOCALE,          "A test struct");
    structClassMd.setStructValue(MdStructInfo.DISPLAY_LABEL,      MdAttributeLocalInfo.DEFAULT_LOCALE,   "A test struct");
    clientRequest.createBusiness(structClassMd);

    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "structChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,     "Struct Character");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A struct character");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "I'm a struct char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, structClassMd.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    // home phone
    BusinessDTO mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,                "homePhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Struct");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,            MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT,  EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    // work phone
    mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,               "workPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "workPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testClassMd.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    // cell phone
    mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,               "cellPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "cellPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testClassMd.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    BusinessDTO mdAttributeText = clientRequest.newBusiness(MdAttributeTextInfo.CLASS);
    mdAttributeText.setValue(MdAttributeTextInfo.NAME,                "testText");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Text");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFAULT_VALUE,       "");
    mdAttributeText.setValue(MdAttributeTextInfo.REQUIRED,            MdAttributeBooleanInfo.FALSE);
    mdAttributeText.setValue(MdAttributeTextInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeText);

    // Add attributes to the test type
    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Character");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "Yo diggity");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, testClassMd.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    BusinessDTO mdAttributeBlob = clientRequest.newBusiness(MdAttributeBlobInfo.CLASS);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.NAME,              "testBlob");
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,       "Some blob");
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED,          MdAttributeBooleanInfo.FALSE);
    mdAttributeBlob.addEnumItem(MdAttributeBlobInfo.INDEX_TYPE,     IndexTypes.NO_INDEX.toString());
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REMOVE,               MdAttributeBooleanInfo.TRUE);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, testClassMd.getId());
    clientRequest.createBusiness(mdAttributeBlob);

    BusinessDTO mdAttributeInteger = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME,               "testInteger");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Integer");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, testClassMd.getId());
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttributeInteger);

    BusinessDTO mdAttributeLong = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME,                "testLong");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Long");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFAULT_VALUE,       "");
    mdAttributeLong.setValue(MdAttributeLongInfo.REQUIRED,            MdAttributeBooleanInfo.FALSE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    mdAttributeLong.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttributeLong);

    BusinessDTO mdAttributeFloat = clientRequest.newBusiness(MdAttributeFloatInfo.CLASS);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME,                "testFloat");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Float");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE,       "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED,            MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH,              "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL,             "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_ZERO,         MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE,     MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE,     MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS,   testClassMd.getId());
    clientRequest.createBusiness(mdAttributeFloat);

    BusinessDTO mdAttributeDecimal = clientRequest.newBusiness(MdAttributeDecimalInfo.CLASS);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.NAME,                "testDecimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Decimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE,       "");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED,            MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.LENGTH,              "13");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL,             "3");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_ZERO,         MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE,     MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REJECT_POSITIVE,     MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS,   testClassMd.getId());
    clientRequest.createBusiness(mdAttributeDecimal);

    BusinessDTO mdAttributeDouble = clientRequest.newBusiness(MdAttributeDoubleInfo.CLASS);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.NAME,               "testDouble");
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Double");
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A Double");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE,      "");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.LENGTH,             "16");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DECIMAL,            "4");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_ZERO,        MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE,    MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE,    MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeDouble);

    BusinessDTO mdAttributeTime = clientRequest.newBusiness(MdAttributeTimeInfo.CLASS);
    mdAttributeTime.setValue(MdAttributeTimeInfo.NAME,               "testTime");
    mdAttributeTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL,MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Time");
    mdAttributeTime.setStructValue(MdAttributeTimeInfo.DESCRIPTION,  MdAttributeLocalInfo.DEFAULT_LOCALE,      "A Time");
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFAULT_VALUE,      "");
    mdAttributeTime.setValue(MdAttributeTimeInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeTime);

    BusinessDTO mdAttributeDate = clientRequest.newBusiness(MdAttributeDateInfo.CLASS);
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME,               "testDate");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Date");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A Date");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFAULT_VALUE,      "");
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeDate.setValue(MdAttributeDateInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeDate);

    BusinessDTO mdAttributeDateTime = clientRequest.newBusiness(MdAttributeDateTimeInfo.CLASS);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.NAME,               "testDateTime");
    mdAttributeDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test DateTime");
    mdAttributeDateTime.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A DateTime");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFAULT_VALUE,      "");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeDateTime);

    BusinessDTO mdAttributeReference= clientRequest.newBusiness(MdAttributeReferenceInfo.CLASS);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME,               "testReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test ReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A ReferenceObject");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE,      "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,    refClassMd.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeReference);

    BusinessDTO mdAttributeBoolean = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME,               TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    clientRequest.createBusiness(mdAttributeBoolean);

    BusinessDTO mdAttributeHash = clientRequest.newBusiness(MdAttributeHashInfo.CLASS);
    mdAttributeHash.setValue(MdAttributeHashInfo.NAME, "testHash");
    mdAttributeHash.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeHash.setValue(MdAttributeHashInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeHash.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Hash");
    mdAttributeHash.setStructValue(MdAttributeHashInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hash");
    mdAttributeHash.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getId());
    mdAttributeHash.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, testClassMd.getId());
    clientRequest.createBusiness(mdAttributeHash);

    BusinessDTO mdAttributeSymmetric = clientRequest.newBusiness(MdAttributeSymmetricInfo.CLASS);
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.NAME, "testSymmetric");
    mdAttributeSymmetric.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Symmetric");
    mdAttributeSymmetric.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric");
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getId());
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, testClassMd.getId());
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetric.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttributeSymmetric);

    // New Type (STATE) extends Enumeration_Attribute
    stateEnumMdBusiness = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    stateEnumMdBusiness.setValue(MdBusinessInfo.NAME,              "StateEnum");
    stateEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE,           PACKAGE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.REMOVE,            MdAttributeBooleanInfo.TRUE);
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,   "State");
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION,    MdAttributeLocalInfo.DEFAULT_LOCALE,   "States of the Union");
    stateEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE,        MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT,          MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    clientRequest.createBusiness(stateEnumMdBusiness);

    // Instantiate an md_enumeration to define State
    BusinessDTO mdEnumeration = clientRequest.newBusiness(MdEnumerationInfo.CLASS);
    mdEnumeration.setValue(MdEnumerationInfo.NAME,                "States");
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE,             PACKAGE);
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "All States in the United States");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE,              MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL,         MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS,  stateEnumMdBusiness.getId());
    clientRequest.createBusiness(mdEnumeration);
    String mdEnumerationDefinesType = mdEnumeration.getValue(MdTypeInfo.PACKAGE) + "." + mdEnumeration.getValue(MdTypeInfo.NAME);

    // Define attributes on the enumeration
    BusinessDTO mdAttrChar = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME,              "stateCode");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE,     "2");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,      "State Postal Code");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,     "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED,          MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addEnumItem(MdAttributeCharacterInfo.INDEX_TYPE,     IndexTypes.UNIQUE_INDEX.toString());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE,               MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getId());
    clientRequest.createBusiness(mdAttrChar);

    // state phone (I know this doesn't make any sense. I just wanted to recycle code)
    mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,               "statePhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,    "A state phone...which makes no sense.");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,  stateEnumMdBusiness.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT,          EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    mdAttrChar = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME,              "stateName");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE,              "32");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,    "State Name");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,     "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED,          MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addEnumItem(MdAttributeCharacterInfo.INDEX_TYPE,     IndexTypes.UNIQUE_INDEX.toString());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE,            MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getId());
    clientRequest.createBusiness(mdAttrChar);

    mdAttributeInteger = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME,               "enumInt");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Enumeration Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE,      "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS,  stateEnumMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeInteger);
    String stateEnumDefinesType = stateEnumMdBusiness.getValue(MdTypeInfo.PACKAGE) + "." + stateEnumMdBusiness.getValue(MdTypeInfo.NAME);

    // Define the options for the enumeration
    BusinessDTO enumItem = clientRequest.newBusiness(stateEnumDefinesType);
    enumItem.setValue("enumInt", "1");
    enumItem.setValue("stateCode", "CA");
    enumItem.setValue("stateName", "California");
    enumItem.setValue(EnumerationMasterInfo.NAME, "CA");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    clientRequest.createBusiness(enumItem);

    enumItem = clientRequest.newBusiness(stateEnumDefinesType);
    enumItem.setValue("enumInt", "2");
    enumItem.setValue("stateCode", "CO");
    enumItem.setValue("stateName", "Colorado");
    enumItem.setValue(EnumerationMasterInfo.NAME, "CO");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    clientRequest.createBusiness(enumItem);
    String coloradoItemId = enumItem.getId();

    enumItem = clientRequest.newBusiness(stateEnumDefinesType);
    enumItem.setValue("enumInt", "3");
    enumItem.setValue("stateCode", "CT");
    enumItem.setValue("stateName", "Connecticut");
    enumItem.setValue(EnumerationMasterInfo.NAME, "CT");
    enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Connecticut");
    clientRequest.createBusiness(enumItem);

    // Add the enumeration as a multi-select Attribute on the TEST type
    BusinessDTO mdAttrEnumMultiple = clientRequest.newBusiness(MdAttributeEnumerationInfo.CLASS);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.NAME,               "multipleState");
    mdAttrEnumMultiple.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Multiple select state attribute");
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION,     mdEnumeration.getId());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE,    MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(mdAttrEnumMultiple);

    // Add the enumeration as a single-select Attribute on the TEST type
    BusinessDTO mdAttrEnumSingle = clientRequest.newBusiness(MdAttributeEnumerationInfo.CLASS);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME,               "singleState");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,    "Test Enumeration");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "An Enumeration");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE,      coloradoItemId);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED,           MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS,  testClassMd.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION,     mdEnumeration.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE,    MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttrEnumSingle);

    // Util
    mdUtil = clientRequest.newBusiness(MdUtilInfo.CLASS);
    mdUtil.setValue(MdViewInfo.NAME, "TestUtil");
    mdUtil.setValue(MdViewInfo.PACKAGE, PACKAGE);
    mdUtil.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A blandly named MdUtil.");
    clientRequest.createBusiness(mdUtil);
    String testUtilDefinesType = mdUtil.getValue(MdTypeInfo.PACKAGE) + "." + mdUtil.getValue(MdTypeInfo.NAME);

    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "utilCharacter");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL,MdAttributeLocalInfo.DEFAULT_LOCALE,      "A character for the util");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DEFAULT_VALUE,MdAttributeLocalInfo.DEFAULT_LOCALE,      "Util char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS,  mdUtil.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    mdAttrEnumSingle = clientRequest.newBusiness(MdAttributeEnumerationInfo.CLASS);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME,               "utilSingleState");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test Enumeration");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "An Enumeration for the util.");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE,      coloradoItemId);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED,           MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS,  mdUtil.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION,     mdEnumeration.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE,    MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttrEnumSingle);

    mdAttributeReference= clientRequest.newBusiness(MdAttributeReferenceInfo.CLASS);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME,               "utilReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test ReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A ReferenceObject");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE,      "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,    refClassMd.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS,  mdUtil.getId());
    clientRequest.createBusiness(mdAttributeReference);

    mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,               "utilPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Phone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,  mdUtil.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT,          EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    BusinessDTO viewStaticMd = clientRequest.newBusiness(MdMethodInfo.CLASS);
    viewStaticMd.setValue(MdMethodInfo.REF_MD_TYPE, mdUtil.getId());
    viewStaticMd.setValue(MdMethodInfo.NAME, "doubleAnInt");
    viewStaticMd.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    viewStaticMd.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Doubles an Integer");
    viewStaticMd.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    clientRequest.createBusiness(viewStaticMd);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, viewStaticMd.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The value to double");
    clientRequest.createBusiness(mdParameter);

    BusinessDTO viewInstanceId = clientRequest.newBusiness(MdMethodInfo.CLASS);
    viewInstanceId.setValue(MdMethodInfo.REF_MD_TYPE, mdUtil.getId());
    viewInstanceId.setValue(MdMethodInfo.NAME, "returnUtil");
    viewInstanceId.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    viewInstanceId.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns the input util");
    viewInstanceId.setValue(MdMethodInfo.RETURN_TYPE, testUtilDefinesType);
    clientRequest.createBusiness(viewInstanceId);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, viewInstanceId.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, testUtilDefinesType);
    mdParameter.setValue(MdParameterInfo.NAME, "input");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "input");
    clientRequest.createBusiness(mdParameter);

    // View
    mdView = clientRequest.newBusiness(MdViewInfo.CLASS);
    mdView.setValue(MdViewInfo.NAME, "TestView");
    mdView.setValue(MdViewInfo.PACKAGE, PACKAGE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A blandly named MdView.");
    clientRequest.createBusiness(mdView);
    String testViewDefinesType = mdView.getValue(MdTypeInfo.PACKAGE) + "." + mdView.getValue(MdTypeInfo.NAME);

    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME,               "viewCharacter");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE,               "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "A character for the view");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE,      "View char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS,  mdView.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    mdAttrEnumSingle = clientRequest.newBusiness(MdAttributeEnumerationInfo.CLASS);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME,               "viewSingleState");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL,MdAttributeLocalInfo.DEFAULT_LOCALE,      "Test Enumeration");
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION,  MdAttributeLocalInfo.DEFAULT_LOCALE,      "An Enumeration for the view.");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE,      coloradoItemId);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED,           MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS,  mdView.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION,     mdEnumeration.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE,    MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttrEnumSingle);

    mdAttributeReference= clientRequest.newBusiness(MdAttributeReferenceInfo.CLASS);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME,               "viewReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Test ReferenceObject");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION,   MdAttributeLocalInfo.DEFAULT_LOCALE,     "A ReferenceObject");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE,      "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY,    refClassMd.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS,  mdView.getId());
    clientRequest.createBusiness(mdAttributeReference);

    mdAttrStruct = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME,               "viewPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,     "Phone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED,           MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE,             MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS,  mdView.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT,          EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttrStruct);

    viewStaticMd = clientRequest.newBusiness(MdMethodInfo.CLASS);
    viewStaticMd.setValue(MdMethodInfo.REF_MD_TYPE, mdView.getId());
    viewStaticMd.setValue(MdMethodInfo.NAME, "doubleAnInt");
    viewStaticMd.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.TRUE);
    viewStaticMd.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Doubles an Integer");
    viewStaticMd.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    clientRequest.createBusiness(viewStaticMd);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, viewStaticMd.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Integer");
    mdParameter.setValue(MdParameterInfo.NAME, "num");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The value to double");
    clientRequest.createBusiness(mdParameter);

    viewInstanceId = clientRequest.newBusiness(MdMethodInfo.CLASS);
    viewInstanceId.setValue(MdMethodInfo.REF_MD_TYPE, mdView.getId());
    viewInstanceId.setValue(MdMethodInfo.NAME, "returnView");
    viewInstanceId.setValue(MdMethodInfo.IS_STATIC, MdAttributeBooleanInfo.FALSE);
    viewInstanceId.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Returns the input view");
    viewInstanceId.setValue(MdMethodInfo.RETURN_TYPE, testViewDefinesType);
    clientRequest.createBusiness(viewInstanceId);

    mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, viewInstanceId.getId());
    mdParameter.setValue(MdParameterInfo.TYPE, testViewDefinesType);
    mdParameter.setValue(MdParameterInfo.NAME, "input");
    mdParameter.setValue(MdParameterInfo.ORDER, "0");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "input");
    clientRequest.createBusiness(mdParameter);

    BusinessDTO exChar = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    exChar.setValue(MdAttributeCharacterInfo.NAME, "exChar");
    exChar.setValue(MdAttributeCharacterInfo.SIZE, "50");
    exChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An exception char");
    exChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdException.getId());
    clientRequest.createBusiness(exChar);

    BusinessDTO problemChar = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    problemChar.setValue(MdAttributeCharacterInfo.NAME, "problemChar");
    problemChar.setValue(MdAttributeCharacterInfo.SIZE, "50");
    problemChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A problem char");
    problemChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdProblem.getId());
    clientRequest.createBusiness(problemChar);

    /* Define a facade */

    mdFacade = clientRequest.newBusiness(MdFacadeInfo.CLASS);
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Summation");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, PACKAGE);
    mdFacade.setValue(MdFacadeInfo.NAME, "Summation");
    clientRequest.createBusiness(mdFacade);

    BusinessDTO facadeMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod.setValue(MdMethodInfo.NAME, "sumIntegerValues");
    facadeMethod.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    facadeMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sums the values of the attribute testInteger on TestClass");
    clientRequest.createBusiness(facadeMethod);

    BusinessDTO facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, testClassDefinesType + "[]");
    facadeParam.setValue(MdParameterInfo.NAME, "testClassArr");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "TestClass Array");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod1 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod1.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod1.setValue(MdMethodInfo.NAME, "facadeForceException");
    facadeMethod1.setValue(MdMethodInfo.RETURN_TYPE, "void");
    facadeMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Throws an exception.");
    clientRequest.createBusiness(facadeMethod1);

    BusinessDTO facadeMethod2 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod2.setValue(MdMethodInfo.NAME, "getNullInteger");
    facadeMethod2.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.Integer");
    facadeMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Tests null passing (param and return)");
    clientRequest.createBusiness(facadeMethod2);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod2.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, testClassDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "nullObj");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A null");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod3 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod3.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod3.setValue(MdMethodInfo.NAME, "getState");
    facadeMethod3.setValue(MdMethodInfo.RETURN_TYPE, mdEnumerationDefinesType);
    facadeMethod3.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Receives a State Enumeration and returns the same object");
    clientRequest.createBusiness(facadeMethod3);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod3.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, mdEnumerationDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "state");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A State");
    clientRequest.createBusiness(facadeParam);

    // create an MdMethod with 2 parameters (to test javascript's ability to loop through params)
    BusinessDTO facadeMethod4 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod4.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod4.setValue(MdMethodInfo.NAME, "compareStates");
    facadeMethod4.setValue(MdMethodInfo.RETURN_TYPE, Boolean.class.getName());
    facadeMethod4.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE,  "Receives two state parameters and compares them");
    clientRequest.createBusiness(facadeMethod4);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod4.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, mdEnumerationDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "state1");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State 1");
    clientRequest.createBusiness(facadeParam);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod4.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, mdEnumerationDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "state2");
    facadeParam.setValue(MdParameterInfo.ORDER, "1");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State 2");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod5 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod5.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod5.setValue(MdMethodInfo.NAME, "concatViewChar");
    facadeMethod5.setValue(MdMethodInfo.RETURN_TYPE, testViewDefinesType);
    facadeMethod5.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "concats the char value in a view.");
    clientRequest.createBusiness(facadeMethod5);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod5.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, testViewDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "view");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View input");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod6 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod6.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod6.setValue(MdMethodInfo.NAME, "concatUtilChar");
    facadeMethod6.setValue(MdMethodInfo.RETURN_TYPE, testUtilDefinesType);
    facadeMethod6.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "concats the char value in a util.");
    clientRequest.createBusiness(facadeMethod6);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod6.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, testUtilDefinesType);
    facadeParam.setValue(MdParameterInfo.NAME, "util");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "View input");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod7 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod7.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod7.setValue(MdMethodInfo.NAME, "doNothing");
    facadeMethod7.setValue(MdMethodInfo.RETURN_TYPE, "void");
    facadeMethod7.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "takes in nothing, returns nothing");
    clientRequest.createBusiness(facadeMethod7);

    BusinessDTO facadeMethod8 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod8.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod8.setValue(MdMethodInfo.NAME, "arrayInOut");
    facadeMethod8.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName()+"[][]");
    facadeMethod8.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "creates and returns a 2-dimensional int array");
    clientRequest.createBusiness(facadeMethod8);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod8.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, Integer.class.getName()+"[][]");
    facadeParam.setValue(MdParameterInfo.NAME, "array");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "array input");
    clientRequest.createBusiness(facadeParam);

    BusinessDTO facadeMethod9 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    facadeMethod9.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    facadeMethod9.setValue(MdMethodInfo.NAME, "dateInOut");
    facadeMethod9.setValue(MdMethodInfo.RETURN_TYPE, Date.class.getName());
    facadeMethod9.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Takes in a date and returns it.");
    clientRequest.createBusiness(facadeMethod9);

    facadeParam = clientRequest.newBusiness(MdParameterInfo.CLASS);
    facadeParam.setValue(MdParameterInfo.ENCLOSING_METADATA, facadeMethod9.getId());
    facadeParam.setValue(MdParameterInfo.TYPE, Date.class.getName());
    facadeParam.setValue(MdParameterInfo.NAME, "date");
    facadeParam.setValue(MdParameterInfo.ORDER, "0");
    facadeParam.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "date input");
    clientRequest.createBusiness(facadeParam);
  }

  private void finalizeSetup()
  {
    // change/apply the facade source
    String facadeSource = getFacadeSource();
    clientRequest.lock(mdFacade);
    mdFacade.setValue(MdFacadeInfo.STUB_SOURCE, facadeSource);
    clientRequest.update(mdFacade);

    // change/apply the class source
    String classSource = getClassSource();
    clientRequest.lock(testClassMd);
    testClassMd.setValue(MdClassInfo.STUB_SOURCE, classSource);
    clientRequest.update(testClassMd);

    String viewSource = getViewSource();
    clientRequest.lock(mdView);
    mdView.setValue(MdClassInfo.STUB_SOURCE, viewSource);
    clientRequest.update(mdView);

    String utilSource = getUtilSource();
    clientRequest.lock(mdUtil);
    mdUtil.setValue(MdClassInfo.STUB_SOURCE, utilSource);
    clientRequest.update(mdUtil);

  }

  /**
   * Tears down the test.
   */
  public void tearDown() throws Exception
  {
    server.stop();

    clientRequest.lock(noPermissions);
    clientRequest.delete(noPermissions.getId());

    clientRequest.lock(mdFacade);
    clientRequest.delete(mdFacade.getId());

    clientRequest.lock(mdException);
    clientRequest.delete(mdException.getId());

    clientRequest.lock(mdProblem);
    clientRequest.delete(mdProblem.getId());

    clientRequest.lock(befriendsMd);
    clientRequest.delete(befriendsMd.getId());

    clientRequest.lock(subClassMd);
    clientRequest.delete(subClassMd.getId());

    clientRequest.lock(testClassMd);
    clientRequest.delete(testClassMd.getId());

    clientRequest.lock(mdUtil);
    clientRequest.delete(mdUtil.getId());

    clientRequest.lock(mdView);
    clientRequest.delete(mdView.getId());

    clientRequest.lock(refClassMd);
    clientRequest.delete(refClassMd.getId());

    clientRequest.lock(stateEnumMdBusiness);
    clientRequest.delete(stateEnumMdBusiness.getId());

    clientRequest.lock(structClassMd);
    clientRequest.delete(structClassMd.getId());

    systemSession.logout();
  }

  private String getUtilSource()
  {
    String[] lines = {
        "package "+PACKAGE+";",
        "public class TestUtil extends TestUtilBase" + Reloadable.IMPLEMENTS,
        "{",
          "public TestUtil()",
          "{",
            "super();",
          "}",
          "public String toString()",
          "{",
          "  return \"TestUtil: \"+getId();",
          "}",
          "public static TestUtil get(String id)",
          "{",
            "return (TestUtil) "+Util.class.getName()+".get(id);",
          "}",
          "@"+Authenticate.class.getName(),
          "public "+PACKAGE+".TestUtil returnUtil("+PACKAGE+".TestUtil util)",
          "{",
          "  util.setUtilCharacter(\"Returned!\");",
          "  return util;",
          "}",
          "@"+Authenticate.class.getName(),
          "public static java.lang.Integer doubleAnInt(java.lang.Integer num)",
          "{",
            "return 2 * num;",
          "}",
        "}",
    };

    String source = "";
    for(String line : lines)
    {
      source += line + "\n";
    }

    return source;
  }

  private String getViewSource()
  {
    String[] lines = {
        "package "+PACKAGE+";",
        "public class TestView extends TestViewBase" + Reloadable.IMPLEMENTS,
        "{",
          "public TestView()",
          "{",
            "super();",
          "}",
          "public String toString()",
          "{",
          "  return \"TestView: \"+getId();",
          "}",
          "public static TestView get(String id)",
          "{",
            "return (TestView) "+View.class.getName()+".get(id);",
          "}",
          "@"+Authenticate.class.getName(),
          "public "+PACKAGE+".TestView returnView("+PACKAGE+".TestView view)",
          "{",
          "  view.setViewCharacter(\"Returned!\");",
          "  return view;",
          "}",
          "@"+Authenticate.class.getName(),
          "public static java.lang.Integer doubleAnInt(java.lang.Integer num)",
          "{",
            "return 2 * num;",
          "}",
        "}",
    };

    String source = "";
    for(String line : lines)
    {
      source += line + "\n";
    }

    return source;
  }

  private String getFacadeSource()
  {
    String[] lines = {
        "package "+PACKAGE+";",
        "public class Summation extends SummationBase" + Reloadable.IMPLEMENTS,
        "{",
          "protected static void facadeForceException(String sessionId)" +
          "{" +
          "  "+PACKAGE+".TestException ex = new "+PACKAGE+".TestException(\"Test Ex\");" +
          "  ex.setExChar(\"Test exChar\");" +
          "  throw ex;" +
          "}" +
          "protected static java.util.Date dateInOut(String sessionId, java.util.Date date)" +
          "{" +
          " return date;" +
          "}" +
          "protected static java.lang.Integer[][] arrayInOut(String sessionId, java.lang.Integer[][] arrayIn)" +
          "{" +
          "  java.lang.Integer[][] array = new java.lang.Integer[5][5];" +
          "  for(int i=0; i<5; i++)" +
          "  {" +
          "    for(int j=0; j<5; j++)" +
          "    {" +
          "      array[i][j] = arrayIn[i][j];" +
          "    }" +
          "  }" +
          "  return array;" +
          "}" +
          "protected static void doNothing(String sessionId)",
          "{",
          "}",
          "protected static java.lang.Integer sumIntegerValues(String sessionId, "+PACKAGE+".TestClass[] testClassArr)",
          "{",
            "int sum = 0;",
            "for(TestClass testClass : testClassArr)",
            "{",
              "sum += testClass.getTestInteger();",
            "}",
            "return sum;",
          "}",
          "protected static java.lang.Integer getNullInteger(String sessionId, "+PACKAGE+".TestClass nullObj)",
          "{",
            "if(nullObj == null)",
              "return null;",
            "else",
              "throw new RuntimeException();",
          "}",
          "protected static "+PACKAGE+".States getState(String sessionId, "+PACKAGE+".States state)",
          "{",
            "return state;",
          "}",
          "protected static "+PACKAGE+".TestView concatViewChar(String sessionId, "+PACKAGE+".TestView view)",
          "{",
          "  view.setViewCharacter(view.getViewCharacter() + view.getViewCharacter());",
          "  return view;",
          "}",
          "protected static "+PACKAGE+".TestUtil concatUtilChar(String sessionId, "+PACKAGE+".TestUtil util)",
          "{",
          "  util.setUtilCharacter(util.getUtilCharacter() + util.getUtilCharacter());",
          "  return util;",
          "}",
        "}",
    };

    String source = "";
    for(String line : lines)
    {
      source += line + "\n";
    }

    return source;
  }

  private String getClassSource()
  {
    String[] lines = {
        "package "+PACKAGE+";",
        "public class TestClass extends TestClassBase" + Reloadable.IMPLEMENTS,
        "{",
          "public TestClass()",
          "{",
            "super();",
          "}",
          "public String toString()",
          "{",
          "  return \"TestClass: \"+getId();",
          "}",
          "public static TestClass get(String id)",
          "{",
            "return (TestClass) "+Business.class.getName()+".get(id);",
          "}",
          "@"+Authenticate.class.getName(),
          "public "+PACKAGE+".TestClass[] createInstances(java.lang.Integer num)",
          "{",
            "if(num == null)",
            "{",
              "return null;",
            "}",
            "TestClass[] ret = new TestClass[num];",
            "for (int i = 0; i < num; i++)",
            "{",
              "ret[i] = new TestClass();",
            "}",
            "return ret;",
          "}",
          "@"+Authenticate.class.getName(),
          "public static java.lang.Integer doubleAnInteger(java.lang.Integer num)",
          "{",
            "return 2 * num;",
          "}",
          "@"+Authenticate.class.getName(),
          "public void instanceForceException()",
          "{",
          "  "+PACKAGE+".TestException ex = new "+PACKAGE+".TestException(\"Test Ex\");",
          "  ex.setExChar(\"Test exChar\");",
          "  throw ex;",
          "}",
          "@"+Authenticate.class.getName(),
          "public void instanceForceException1()",
          "{",
          "  throw new "+InvalidLoginException.class.getName()+"(\"Test InvalidLoginException\");",
          "}",
          "@"+Authenticate.class.getName(),
          "public void instanceForceException2()",
          "{",
          "  throw new java.lang.NullPointerException(\"Test NullPointerException\");",
          "}",
          "@"+Authenticate.class.getName(),
          "public void instanceForceException3()",
          "{",
          "  throw new java.lang.ClassCastException(\"Test ClassCastException\");",
          "}",
          "@"+Authenticate.class.getName(),
          "@"+Transaction.class.getName()+"\n"+
          "public void instanceForceProblem()",
          "{",
          "  "+PACKAGE+".TestProblem p = new "+PACKAGE+".TestProblem(\"Test Problem\");",
          "  p.setProblemChar(\"Test problemChar\");",
          "  p.apply();",
          "  p.throwIt();",
          "}",
        "}",
    };

    String source = "";
    for(String line : lines)
    {
      source += line + "\n";
    }

    return source;
  }

  /**
   * Runs the javascript test via Selenium RC.
   */
  public void testJavascript()
  {

    try
    {
      System.out.print("Testing Javascript");

      selenium.setTimeout(Integer.toString(Integer.MAX_VALUE)); // allow a large timeout for the core
                                    // to spool up
      selenium.open("/" + webapp + "/ajax.jsp");

      // (New/Get)Instance Test
      wrap("startInstanceTest", "(New/Get)Instance Test", "instanceResults");

      // Delete Test
      wrap("startDeleteTest", "Delete Test", "deleteResults");

      // Primitive Test
      wrap("startPrimitiveTest", "Primitive Test", "primitiveResults");

      // Reference Test
      wrap("startReferenceTest", "Reference Test", "referenceResults");

      // Enumeration Test
      wrap("startEnumerationTest", "Enumeration Test", "enumResults");

      // Struct Test
      wrap("startStructTest", "Struct Test", "structResults");

      // Method Test
      wrap("startMethodTest", "Method Test", "methodResults");

      // Metadata Test
      wrap("startMetadataTest", "Metadata Test", "metadataResults");

      // Facade Test
      wrap("startFacadeTest", "Facade Test", "facadeResults");

      // Relationship Test
      wrap("startRelationshipTest", "Relationship Test", "relationshipResults");

      // Alias Test
      wrap("startAliasTest", "Alias Test", "aliasResults");

      // Permission Test
      wrap("startPermissionTest", "Permission Test", "permissionResults");

      // MdEnumeration Test
      wrap("startMdEnumerationTest", "MdEnumeration Test", "mdEnumerationResults");

      // Default Test
      wrap("startDefaultTest", "Default Test", "defaultResults");

      // Moment Test
      wrap("startMomentTest", "Moment Test", "momentResults");

      // Subclass Test
      wrap("startSubclassTest", "Subclass Test", "subclassResults");

      // Subclass Attribute Test
      wrap("startSubclassPrimitiveTest", "Subclass Primitive Test", "subclassPrimitiveResults");

      // Subclass Method Test
      wrap("startSubclassMethodTest", "Subclass Method Test", "subclassMethodResults");

      // Subclass Default Test
      wrap("startSubclassDefaultTest", "Subclass Default Test", "subclassDefaultResults");

      // Subclass Instance Test
      wrap("startSubclassInstanceTest", "Subclass Instance Test", "subclassInstanceResults");

      // Subclass Delete Test
      wrap("startSubclassDeleteTest", "Subclass Delete Test", "subclassDeleteResults");

      // Subclass Struct Test
      wrap("startSubclassStructTest", "Subclass Struct Test", "subclassStructResults");

      // Subclass Enumeration Test
      wrap("startSubclassEnumerationTest", "Subclass Enumeration Test", "subclassEnumResults");

      // Subclass Metadata test
      wrap("startSubclassMetadataTest", "Subclass Metadata Test", "subclassMetadataResults");

      // Subclass Reference Test
      wrap("startSubclassReferenceTest", "Subclass Reference Test", "subclassReferenceResults");

      // Subclass type test
      wrap("startSubclassTypeTest", "Subclass Type Test", "subclassTypeResults");

      // BusinessQueryDTO test
      wrap("startBusinessQueryDTOTest", "BusinessQueryDTO Test", "businessQueryDTOResults");

      // RelationshipQueryDTO test
      wrap("startRelationshipQueryDTOTest", "RelationshipQueryDTO Test", "relationshipQueryDTOResults");

      // ValueQueryDTO test
      wrap("startValueQueryDTOTest", "ValueQueryDTO Test", "valueQueryDTOResults");

      // StructQueryDTO test
      wrap("startStructQueryDTOTest", "StructQueryDTO Test", "structQueryDTOResults");

      // MdView test
      wrap("startMdViewTest", "MdView Test", "mdViewResults");

      // MdUtil test
      wrap("startMdUtilTest", "MdUtil Test", "mdUtilResults");

      // MdView invoke method test
      wrap("startMdViewInvokeMethodTest", "MdView InvokeMethod Test", "mdViewInvokeMethodResults");

      // MdUtil invoke method test
      wrap("startMdUtilInvokeMethodTest", "MdUtil InvokeMethod Test", "mdUtilInvokeMethodResults");

      // Admin Screen Test
      wrap("startAdminScreenAccessTest", "Admin Screen Access Test", "adminScreenAccessResults");

      // TestExceptions
      wrap("startExceptionTest", "Exception Test", "exceptionResults");

      assertTrue("No Error!".equals(selenium.getText("status")));

      selenium.click("tearDown");
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Wraps a test with a threading timeout system. This is needed to wait for
   * the AJAX calls to respond.
   *
   * @param button
   * @param testName
   * @param resultsId
   */
  private void wrap(String button, String testName, String resultsId) throws Exception
  {
    selenium.click(button);
    for (int second = 0;; second++)
    {
      if (second >= WAIT_IN_SECONDS)
        fail(testName + " Failed");

      if (new String(testName + ": Passed").equals(selenium.getText(resultsId)))
        break;
      else
        Thread.sleep(1000);
    }
    Thread.sleep(2000);
  }
}
