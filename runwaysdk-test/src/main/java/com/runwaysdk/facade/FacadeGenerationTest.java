/*******************************************************************************
 * Copyright (c) 2013 TerraFrame, Inc. All rights reserved.
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
 ******************************************************************************/
package com.runwaysdk.facade;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.runwaysdk.ClientSession;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.EnumerationDTOIF;
import com.runwaysdk.business.InformationDTO;
import com.runwaysdk.business.MessageDTO;
import com.runwaysdk.business.ProblemDTO;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.WarningDTO;
import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.rbac.Authenticate;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.LocalProperties;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdExceptionInfo;
import com.runwaysdk.constants.MdFacadeInfo;
import com.runwaysdk.constants.MdInformationInfo;
import com.runwaysdk.constants.MdLocalizableInfo;
import com.runwaysdk.constants.MdMethodInfo;
import com.runwaysdk.constants.MdParameterInfo;
import com.runwaysdk.constants.MdProblemInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdWarningInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.MethodActorInfo;
import com.runwaysdk.constants.RelationshipTypes;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SupportedLocaleInfo;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.io.XMLImporter;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.SupportedLocaleDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.GrantMethodPermissionExceptionDTO;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RevokeMethodPermissionExceptionDTO;
import com.runwaysdk.util.FileIO;

public class FacadeGenerationTest extends TestCase
{
  protected static BusinessDTO              tommyUser;

  protected static BusinessDTO              littleBillyTables;

  public static volatile Class<?>           facadeProxyClass    = null;

  public static volatile Object             generatedProxy      = null;

  public static volatile String             label               = null;

  protected static String                   pack                = "test.generated";

  private static String                     collectionName      = "CollectionF";

  private static String                     collectionType      = pack + "." + collectionName;

  private static String                     collectionQueryType = collectionType + EntityQueryAPIGenerator.QUERY_API_SUFFIX;

  protected static String                   facadeName          = "BookFacade";

  private static String                     bagName             = "BagF";

  private static String                     bagType             = pack + "." + bagName;

  private static String                     referenceName       = "ReferenceF";

  private static String                     referenceType       = pack + "." + referenceName;

  private static String                     stateName           = "AllStatesF";

  private static String                     stateType           = pack + "." + stateName;

  private static String                     coloradoName        = "colorado";

  private static String                     californiaName      = "california";

  private static String                     connecticutName     = "connecticut";

  private static String                     collectionDTO       = collectionType + ComponentDTOGenerator.DTO_SUFFIX;         ;

  private static String                     bagDTO              = bagType + ComponentDTOGenerator.DTO_SUFFIX;

  private static BusinessDTO                collection;

  private static BusinessDTO                mdAttributeCharacter;

  private static BusinessDTO                mdAttributeLockedBy = null;

  private static BusinessDTO                bag;

  private static BusinessDTO                reference;

  private static BusinessDTO                states;

  private static BusinessDTO                stateEnum;

  private static BusinessDTO                california;

  private static BusinessDTO                colorado;

  private static BusinessDTO                connecticut;

  private static BusinessDTO                mdFacade;

  private static BusinessDTO                sortNumbersMdMethod;

  private static BusinessDTO                sortNumbersMethodActor;

  private static BusinessDTO                mdMethod2;

  private static BusinessDTO                sortCollectionsMdMethod;

  private static BusinessDTO                sortCollectionsSingleActorMdMethod;

  private static BusinessDTO                testMdMethod;

  private static BusinessDTO                testAssignablMdMethod;

  private static BusinessDTO                mdMethod5;

  private static BusinessDTO                mdMethod6;

  private static BusinessDTO                mdMethod7;

  private static BusinessDTO                mdMethod8;

  private static BusinessDTO                exception;

  private static BusinessDTO                exceptionMethod;

  private static BusinessDTO                warning1;

  private static BusinessDTO                warningMdMethod1;

  private static BusinessDTO                warningMdMethod1SingleActorMdMethod;

  private static BusinessDTO                warningMdMethodReturnArrayObjects;

  private static BusinessDTO                warningMdMethodReturnQueryObject;

  private static BusinessDTO                warningMdMethodReturnInt;

  private static BusinessDTO                warningMdMethodReturnIntArray;

  private static BusinessDTO                information1;

  private static BusinessDTO                informationMdMethod;

  private static BusinessDTO                multipleMessagesMdMethod;

  private static BusinessDTO                problem1;

  private static BusinessDTO                problem2;

  private static BusinessDTO                problemMdMethod;

  protected static volatile ClientSession   systemSession;

  protected static volatile ClientRequestIF clientRequest;

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });

    junit.textui.TestRunner.run(FacadeGenerationTest.suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTestSuite(FacadeGenerationTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp() throws Exception
      {
        /*
         * Delete the java generated directory because it has lingering classes
         * which conflict with the java definitions used in this test suite
         */
        FileIO.deleteDirectory(new File(LocalProperties.getClientGenSrc()));
        FileIO.deleteDirectory(new File(LocalProperties.getCommonGenSrc()));
        FileIO.deleteDirectory(new File(LocalProperties.getServerGenSrc()));
        FileIO.deleteDirectory(new File(LocalProperties.getClientGenBin()));
        FileIO.deleteDirectory(new File(LocalProperties.getCommonGenBin()));
        FileIO.deleteDirectory(new File(LocalProperties.getServerGenBin()));

        label = "default";
        systemSession = ClientSession.createUserSession(label, ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });

        try
        {
          clientRequest = systemSession.getRequest();
          clientRequest.setKeepMessages(false);

          classSetUp();
          finalizeSetup();

          String proxyName = pack + ".Java" + facadeName + "Proxy";

          facadeProxyClass = LoaderDecorator.load(proxyName);
          generatedProxy = facadeProxyClass.getConstructor(String.class, String.class).newInstance("default", "");
        }
        catch (Exception e)
        {
          systemSession.logout();

          throw new RuntimeException(e);
        }
      }

      protected void tearDown()
      {
        classTearDown();
      }
    };

    return wrapper;
  }

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession(label, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession(label, userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password, Locale[] locales)
  {
    return ClientSession.createUserSession(label, userName, password, locales);
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    ClientRequestIF clientRequestIF = clientSession.getRequest();
    clientRequestIF.setKeepMessages(false);

    return clientRequestIF;
  }

  public static void classSetUp()
  {
    addLocale(Locale.GERMAN);

    tommyUser = clientRequest.newBusiness(UserInfo.CLASS);
    tommyUser.setValue(UserInfo.USERNAME, "Tommy");
    tommyUser.setValue(UserInfo.PASSWORD, "music");
    clientRequest.createBusiness(tommyUser);

    littleBillyTables = clientRequest.newBusiness(UserInfo.CLASS);
    littleBillyTables.setValue(UserInfo.USERNAME, "Billy");
    littleBillyTables.setValue(UserInfo.PASSWORD, "Tables");
    clientRequest.createBusiness(littleBillyTables);

    collection = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    collection.setValue(MdBusinessInfo.NAME, collectionName);
    collection.setValue(MdBusinessInfo.PACKAGE, pack);
    collection.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    clientRequest.createBusiness(collection);

    BusinessDTO aBoolean = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    aBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    aBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    aBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    aBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, collection.getId());
    clientRequest.createBusiness(aBoolean);

    mdAttributeCharacter = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, collection.getId());
    clientRequest.createBusiness(mdAttributeCharacter);

    BusinessDTO aDouble = clientRequest.newBusiness(MdAttributeDoubleInfo.CLASS);
    aDouble.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    aDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    aDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, collection.getId());
    aDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    aDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    clientRequest.createBusiness(aDouble);

    BusinessDTO aLong = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    aLong.setValue(MdAttributeLongInfo.NAME, "aLong");
    aLong.setValue(MdAttributeLongInfo.DISPLAY_LABEL, "A Long");
    aLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getId());
    clientRequest.createBusiness(aLong);

    bag = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    bag.setValue(MdBusinessInfo.NAME, bagName);
    bag.setValue(MdBusinessInfo.PACKAGE, pack);
    bag.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    bag.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, bagName);
    bag.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, collection.getId());
    clientRequest.createBusiness(bag);

    reference = clientRequest.newBusiness(MdRelationshipInfo.CLASS);
    reference.setValue(MdRelationshipInfo.NAME, referenceName);
    reference.setValue(MdRelationshipInfo.PACKAGE, pack);
    reference.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    reference.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, referenceName);
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
    stateEnum.setValue(MdEnumerationInfo.NAME, stateName);
    stateEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    stateEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, states.getId());
    stateEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States");
    stateEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    clientRequest.createBusiness(stateEnum);

    // the custom source with the methods implemented
    mdFacade = clientRequest.newBusiness(MdFacadeInfo.CLASS);
    mdFacade.setValue(MdFacadeInfo.NAME, "BookFacade");
    mdFacade.setValue(MdFacadeInfo.PACKAGE, pack);
    mdFacade.setStructValue(MdFacadeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Facade");
    clientRequest.createBusiness(mdFacade);

    sortNumbersMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    sortNumbersMdMethod.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    sortNumbersMdMethod.setValue(MdMethodInfo.NAME, "sortNumbers");
    sortNumbersMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Number");
    sortNumbersMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(sortNumbersMdMethod);

    sortNumbersMethodActor = clientRequest.newBusiness(MethodActorInfo.CLASS);
    sortNumbersMethodActor.setValue(MethodActorInfo.MD_METHOD, sortNumbersMdMethod.getId());
    clientRequest.createBusiness(sortNumbersMethodActor);

    clientRequest.grantTypePermission(sortNumbersMethodActor.getId(), collection.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(sortNumbersMethodActor.getId(), aBoolean.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(sortNumbersMethodActor.getId(), aLong.getId(), Operation.WRITE.name());

    BusinessDTO mdParameter = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter.setValue(MdParameterInfo.TYPE, "java.lang.Boolean");
    mdParameter.setValue(MdParameterInfo.NAME, "ascending");
    mdParameter.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Ascending Parameter");
    mdParameter.setValue(MdParameterInfo.ENCLOSING_METADATA, sortNumbersMdMethod.getId());
    mdParameter.setValue(MdParameterInfo.ORDER, "1");
    clientRequest.createBusiness(mdParameter);

    BusinessDTO mdParameter2 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter2.setValue(MdParameterInfo.TYPE, "java.lang.Long" + "[]");
    mdParameter2.setValue(MdParameterInfo.NAME, "numbers");
    mdParameter2.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Numbers Parameter");
    mdParameter2.setValue(MdParameterInfo.ENCLOSING_METADATA, sortNumbersMdMethod.getId());
    mdParameter2.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter2);

    mdMethod2 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod2.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod2.setValue(MdMethodInfo.NAME, "poopNothing");
    mdMethod2.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Poop Nothing");
    mdMethod2.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(mdMethod2);

    sortCollectionsMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    sortCollectionsMdMethod.setValue(MdMethodInfo.RETURN_TYPE, collectionType + "[]");
    sortCollectionsMdMethod.setValue(MdMethodInfo.NAME, "sortCollections");
    sortCollectionsMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Sort Collections");
    sortCollectionsMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(sortCollectionsMdMethod);

    sortCollectionsSingleActorMdMethod = clientRequest.newBusiness(MethodActorInfo.CLASS);
    sortCollectionsSingleActorMdMethod.setValue(MethodActorInfo.MD_METHOD, sortCollectionsMdMethod.getId());
    clientRequest.createBusiness(sortCollectionsSingleActorMdMethod);

    clientRequest.grantTypePermission(sortCollectionsSingleActorMdMethod.getId(), collection.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(sortCollectionsSingleActorMdMethod.getId(), collection.getId(), Operation.CREATE.name());
    clientRequest.grantAttributePermission(sortCollectionsSingleActorMdMethod.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(sortCollectionsSingleActorMdMethod.getId(), aLong.getId(), Operation.WRITE.name());

    BusinessDTO mdParameter3 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter3.setValue(MdParameterInfo.TYPE, collectionType + "[]");
    mdParameter3.setValue(MdParameterInfo.NAME, "collections");
    mdParameter3.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collections Parameter");
    mdParameter3.setValue(MdParameterInfo.ENCLOSING_METADATA, sortCollectionsMdMethod.getId());
    mdParameter3.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter3);

    BusinessDTO mdParameter5 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter5.setValue(MdParameterInfo.TYPE, "java.lang.String");
    mdParameter5.setValue(MdParameterInfo.NAME, "collectionName");
    mdParameter5.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Name Parameter");
    mdParameter5.setValue(MdParameterInfo.ENCLOSING_METADATA, sortCollectionsMdMethod.getId());
    mdParameter5.setValue(MdParameterInfo.ORDER, "1");
    clientRequest.createBusiness(mdParameter5);

    testMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    testMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    testMdMethod.setValue(MdMethodInfo.NAME, "testMethod");
    testMdMethod.setValue(MdMethodInfo.DISPLAY_LABEL, "Test Method");
    testMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(testMdMethod);

    testAssignablMdMethod = clientRequest.newBusiness(MethodActorInfo.CLASS);
    testAssignablMdMethod.setValue(MethodActorInfo.MD_METHOD, testMdMethod.getId());
    clientRequest.createBusiness(testAssignablMdMethod);

    clientRequest.grantTypePermission(testAssignablMdMethod.getId(), collection.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(testAssignablMdMethod.getId(), collection.getId(), Operation.CREATE.name());
    clientRequest.grantAttributePermission(testAssignablMdMethod.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());

    BusinessDTO mdParameter4 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter4.setValue(MdParameterInfo.TYPE, collectionType);
    mdParameter4.setValue(MdParameterInfo.NAME, "collection");
    mdParameter4.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Parameter");
    mdParameter4.setValue(MdParameterInfo.ENCLOSING_METADATA, testMdMethod.getId());
    mdParameter4.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter4);

    mdMethod5 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod5.setValue(MdMethodInfo.RETURN_TYPE, "java.lang.String[][]");
    mdMethod5.setValue(MdMethodInfo.NAME, "testMultiArray");
    mdMethod5.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Method");
    mdMethod5.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
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
    mdMethod6.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
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
    mdMethod7.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(mdMethod7);

    mdMethod8 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    mdMethod8.setValue(MdMethodInfo.RETURN_TYPE, "void");
    mdMethod8.setValue(MdMethodInfo.NAME, "deleteCollections");
    mdMethod8.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Delete Collections");
    mdMethod8.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(mdMethod8);

    exception = clientRequest.newBusiness(MdExceptionInfo.CLASS);
    exception.setValue(MdExceptionInfo.NAME, "AlreadyCheckedOutException");
    exception.setValue(MdExceptionInfo.PACKAGE, pack);
    exception.setStructValue(MdExceptionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Exception thrown when there are no copies available for checkout of the requested book");
    exception.setStructValue(MdExceptionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Already Checked Out Exception");
    clientRequest.createBusiness(exception);

    BusinessDTO title = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    title.setValue(MdAttributeCharacterInfo.NAME, "bookTitle");
    title.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Title");
    title.setValue(MdAttributeCharacterInfo.SIZE, "64");
    title.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, exception.getId());
    clientRequest.createBusiness(title);

    exceptionMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    exceptionMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    exceptionMethod.setValue(MdMethodInfo.NAME, "exceptionMethod");
    exceptionMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Exception Method");
    exceptionMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(exceptionMethod);

    warning1 = clientRequest.newBusiness(MdWarningInfo.CLASS);
    warning1.setValue(MdWarningInfo.NAME, "CheckoutLimitReached");
    warning1.setValue(MdWarningInfo.PACKAGE, pack);
    warning1.setStructValue(MdWarningInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning that lets the user know that they have checked out the maximum number of books.");
    warning1.setStructValue(MdWarningInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Checkout Limit Reached");
    clientRequest.createBusiness(warning1);

    BusinessDTO maxAllowedBooks = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.NAME, "maxAllowedBooks");
    maxAllowedBooks.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Maximum Allowed Books");
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    maxAllowedBooks.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, warning1.getId());
    clientRequest.createBusiness(maxAllowedBooks);

    information1 = clientRequest.newBusiness(MdInformationInfo.CLASS);
    information1.setValue(MdInformationInfo.NAME, "RecommendedBook");
    information1.setValue(MdInformationInfo.PACKAGE, pack);
    information1.setStructValue(MdInformationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Message that recommends another book to the user.");
    information1.setStructValue(MdInformationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Book Recommendation");
    clientRequest.createBusiness(information1);

    BusinessDTO recommendationTitle = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    recommendationTitle.setValue(MdAttributeCharacterInfo.NAME, "title");
    recommendationTitle.setValue(MdAttributeCharacterInfo.DISPLAY_LABEL, "Book Title");
    recommendationTitle.setValue(MdAttributeCharacterInfo.SIZE, "64");
    recommendationTitle.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, information1.getId());
    clientRequest.createBusiness(recommendationTitle);

    warningMdMethod1 = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethod1.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    warningMdMethod1.setValue(MdMethodInfo.NAME, "warningMethod");
    warningMdMethod1.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method");
    warningMdMethod1.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(warningMdMethod1);

    warningMdMethod1SingleActorMdMethod = clientRequest.newBusiness(MethodActorInfo.CLASS);
    warningMdMethod1SingleActorMdMethod.setValue(MethodActorInfo.MD_METHOD, warningMdMethod1.getId());
    clientRequest.createBusiness(warningMdMethod1SingleActorMdMethod);

    problem1 = clientRequest.newBusiness(MdProblemInfo.CLASS);
    problem1.setValue(MdProblemInfo.NAME, "TooManyCheckedOutBooksProblem");
    problem1.setValue(MdProblemInfo.PACKAGE, pack);
    problem1.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem thrown when the library user already has too many books checked out.");
    problem1.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Too Many Checked Out Books Problem");
    clientRequest.createBusiness(problem1);

    // Attributes on localized message classex
    BusinessDTO checkedOutBooks = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    checkedOutBooks.setValue(MdAttributeIntegerInfo.NAME, "checkedOutBooks");
    checkedOutBooks.setValue(MdAttributeIntegerInfo.DISPLAY_LABEL, "Number of Checked Out Books");
    checkedOutBooks.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, problem1.getId());
    clientRequest.createBusiness(checkedOutBooks);

    problem2 = clientRequest.newBusiness(MdProblemInfo.CLASS);
    problem2.setValue(MdProblemInfo.NAME, "OverdueLibraryFeesProblem");
    problem2.setValue(MdProblemInfo.PACKAGE, pack);
    problem2.setStructValue(MdProblemInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem thrown when the library user has overdue library fees.");
    problem2.setStructValue(MdProblemInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Overdue Library Fees Problem");
    clientRequest.createBusiness(problem2);

    BusinessDTO outstandingFees = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    outstandingFees.setValue(MdAttributeIntegerInfo.NAME, "totalOutstandingFees");
    outstandingFees.setValue(MdAttributeIntegerInfo.DISPLAY_LABEL, "Total Outstanding Fees");
    outstandingFees.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, problem2.getId());
    clientRequest.createBusiness(outstandingFees);

    problemMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    problemMdMethod.setValue(MdMethodInfo.RETURN_TYPE, "void");
    problemMdMethod.setValue(MdMethodInfo.NAME, "problemMethod");
    problemMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Problem Method");
    problemMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(problemMdMethod);

    BusinessDTO mdParameter9 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter9.setValue(MdParameterInfo.TYPE, collectionType);
    mdParameter9.setValue(MdParameterInfo.NAME, "collection");
    mdParameter9.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection");
    mdParameter9.setValue(MdParameterInfo.ENCLOSING_METADATA, problemMdMethod.getId());
    mdParameter9.setValue(MdParameterInfo.ORDER, "1");
    clientRequest.createBusiness(mdParameter9);

    clientRequest.grantTypePermission(warningMdMethod1SingleActorMdMethod.getId(), collection.getId(), Operation.WRITE.name());
    clientRequest.grantTypePermission(warningMdMethod1SingleActorMdMethod.getId(), collection.getId(), Operation.CREATE.name());
    clientRequest.grantAttributePermission(warningMdMethod1SingleActorMdMethod.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
    clientRequest.grantAttributePermission(warningMdMethod1SingleActorMdMethod.getId(), aLong.getId(), Operation.WRITE.name());

    warningMdMethodReturnArrayObjects = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.RETURN_TYPE, collectionType + "[]");
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.NAME, "warningMethodReturnArrayObjects");
    warningMdMethodReturnArrayObjects.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Array of Return Objects");
    warningMdMethodReturnArrayObjects.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(warningMdMethodReturnArrayObjects);

    warningMdMethodReturnQueryObject = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.RETURN_TYPE, collectionQueryType);
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.NAME, "warningMdMethodReturnQueryObject");
    warningMdMethodReturnQueryObject.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Query Object");
    warningMdMethodReturnQueryObject.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(warningMdMethodReturnQueryObject);

    warningMdMethodReturnInt = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnInt.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName());
    warningMdMethodReturnInt.setValue(MdMethodInfo.NAME, "warningMdMethodReturnInt");
    warningMdMethodReturnInt.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Integer");
    warningMdMethodReturnInt.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(warningMdMethodReturnInt);

    warningMdMethodReturnIntArray = clientRequest.newBusiness(MdMethodInfo.CLASS);
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.RETURN_TYPE, Integer.class.getName() + "[]");
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.NAME, "warningMdMethodReturnIntArray");
    warningMdMethodReturnIntArray.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method With Return Integer Array");
    warningMdMethodReturnIntArray.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(warningMdMethodReturnIntArray);

    informationMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    informationMdMethod.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    informationMdMethod.setValue(MdMethodInfo.NAME, "informationMethod");
    informationMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Information Method");
    informationMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(informationMdMethod);

    multipleMessagesMdMethod = clientRequest.newBusiness(MdMethodInfo.CLASS);
    multipleMessagesMdMethod.setValue(MdMethodInfo.RETURN_TYPE, collectionType);
    multipleMessagesMdMethod.setValue(MdMethodInfo.NAME, "multipleMessagesMethod");
    multipleMessagesMdMethod.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Warning Method");
    multipleMessagesMdMethod.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(multipleMessagesMdMethod);

    BusinessDTO mdParameter8 = clientRequest.newBusiness(MdParameterInfo.CLASS);
    mdParameter8.setValue(MdParameterInfo.TYPE, stateType);
    mdParameter8.setValue(MdParameterInfo.NAME, "state");
    mdParameter8.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Parameter");
    mdParameter8.setValue(MdParameterInfo.ENCLOSING_METADATA, mdMethod7.getId());
    mdParameter8.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(mdParameter8);

    // method that takes in an array of AllStates enums and returns them.
    // this is to test enum arrays as parameters.
    BusinessDTO returnStates = clientRequest.newBusiness(MdMethodInfo.CLASS);
    returnStates.setValue(MdMethodInfo.RETURN_TYPE, stateType + "[]");
    returnStates.setValue(MdMethodInfo.NAME, "returnStates");
    returnStates.setStructValue(MdMethodInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Return states");
    returnStates.setValue(MdMethodInfo.REF_MD_TYPE, mdFacade.getId());
    clientRequest.createBusiness(returnStates);

    BusinessDTO param = clientRequest.newBusiness(MdParameterInfo.CLASS);
    param.setValue(MdParameterInfo.TYPE, stateType + "[]");
    param.setValue(MdParameterInfo.NAME, "states");
    param.setStructValue(MdParameterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "States Parameter");
    param.setValue(MdParameterInfo.ENCLOSING_METADATA, returnStates.getId());
    param.setValue(MdParameterInfo.ORDER, "0");
    clientRequest.createBusiness(param);

    List<? extends BusinessDTO> mdAttributeDTOList = clientRequest.getChildren(collection.getId(), RelationshipTypes.CLASS_ATTRIBUTE_CONCRETE.getType());

    for (BusinessDTO mdAtrributeDTO : mdAttributeDTOList)
    {
      if (mdAtrributeDTO.getValue(MdAttributeConcreteInfo.NAME).equals(MetadataInfo.LOCKED_BY))
      {
        mdAttributeLockedBy = mdAtrributeDTO;
      }
    }
  }

  public static void finalizeSetup()
  {
    clientRequest.lock(exception);
    exception.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "There are no copies of {bookTitle} available for checkout.");
    exception.setStructValue(MdLocalizableInfo.MESSAGE, Locale.FRENCH.toString(), "Il n'existe pas de copies disponibles pour {bookTitle} checkout.");
    clientRequest.update(exception);

    clientRequest.lock(problem1);
    problem1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You already have {checkedOutBooks} books checked out.");
    problem1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Sie haben schon {checkedOutBooks} ausgeliehen.");
    clientRequest.update(problem1);

    clientRequest.lock(problem2);
    problem2.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You have ${totalOutstandingFees}.00 in outstanding fees.");
    problem2.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Sie haben unbezahlte Gebuehr in Summe ${totalOutstandingFees}.00.");
    clientRequest.update(problem2);

    clientRequest.lock(warning1);
    warning1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "{maxAllowedBooks} is the maximum number of books you can check out at a given time.");
    warning1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "{maxAllowedBooks} ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.");
    clientRequest.update(warning1);

    clientRequest.lock(information1);
    information1.setStructValue(MdLocalizableInfo.MESSAGE, MdAttributeLocalInfo.DEFAULT_LOCALE, "You may also enjoy {title}.");
    information1.setStructValue(MdLocalizableInfo.MESSAGE, Locale.GERMAN.toString(), "Das Buch {title} werden Sie gefallen.");
    clientRequest.update(information1);

    // the custom source with the methods implemented
    clientRequest.lock(mdFacade);
    String facadeStubSource = getFacadeSource();
    mdFacade.setValue(MdFacadeInfo.STUB_SOURCE, facadeStubSource);
    clientRequest.update(mdFacade);

    california = clientRequest.newBusiness(pack + ".States");
    california.setValue(EnumerationMasterInfo.NAME, californiaName);
    california.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, californiaName);
    clientRequest.createBusiness(california);

    colorado = clientRequest.newBusiness(pack + ".States");
    colorado.setValue(EnumerationMasterInfo.NAME, coloradoName);
    colorado.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, coloradoName);
    clientRequest.createBusiness(colorado);

    connecticut = clientRequest.newBusiness(pack + ".States");
    connecticut.setValue(EnumerationMasterInfo.NAME, connecticutName);
    connecticut.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, connecticutName);
    clientRequest.createBusiness(connecticut);
  }

  public static void classTearDown()
  {
    try
    {
      clientRequest.delete(mdFacade.getId());

      clientRequest.delete(stateEnum.getId());

      clientRequest.delete(states.getId());

      clientRequest.delete(reference.getId());

      clientRequest.delete(bag.getId());

      clientRequest.delete(collection.getId());

      clientRequest.delete(exception.getId());

      clientRequest.delete(warning1.getId());

      clientRequest.delete(information1.getId());

      clientRequest.delete(problem1.getId());

      clientRequest.delete(problem2.getId());

      clientRequest.delete(tommyUser.getId());

      clientRequest.delete(littleBillyTables.getId());

      deleteLocale(Locale.GERMAN);
    }
    finally
    {
      systemSession.logout();
    }
  }

  protected void tearDown()
  {
    try
    {
      Method method = facadeProxyClass.getMethod("deleteCollections", ClientRequestIF.class);
      method.invoke(generatedProxy, clientRequest);
    }
    catch (RuntimeException e)
    {
      throw e;
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void testEmptyMethod() throws Exception
  {
    Method poopNothing = facadeProxyClass.getMethod("poopNothing", ClientRequestIF.class);
    poopNothing.invoke(generatedProxy, clientRequest);
  }

  public void testArray() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String booleanInput = Boolean.toString(true);

    Boolean bool = Boolean.TRUE;
    Long[] array = new Long[] { 3L, 4L, 8L, 9L, 10923012910L };

    BusinessDTO output = (BusinessDTO) facadeProxyClass.getMethod("sortNumbers", ClientRequestIF.class, array.getClass(), Boolean.class).invoke(generatedProxy, clientRequest, array, bool);

    assertTrue(collectionClass.isInstance(output));
    assertEquals(Boolean.parseBoolean(booleanInput), collectionClass.getMethod("getABoolean").invoke(output));
    assertEquals(new Long(3), collectionClass.getMethod("getALong").invoke(output));
  }

  public void testBusinessDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "164";

    BusinessDTO businessDTO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDTO.setValue("aLong", input);
    collectionClass.getMethod("apply").invoke(businessDTO);
    String id = businessDTO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    collectionClass.getMethod("lock").invoke(object);
    facadeProxyClass.getMethod("testMethod", ClientRequestIF.class, collectionClass).invoke(generatedProxy, clientRequest, object);

    object = (BusinessDTO) get.invoke(null, clientRequest, id);
    assertEquals(clientRequest.getSessionId(), collectionClass.getMethod("getACharacter").invoke(object));
  }

  public void testArrayReturn() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "Har har bar bar";
    String longInput = "1";

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, object);

    collectionClass.getMethod("lock").invoke(object);
    BusinessDTO[] output = (BusinessDTO[]) facadeProxyClass.getMethod("sortCollections", ClientRequestIF.class, array.getClass(), String.class).invoke(generatedProxy, clientRequest, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertTrue(collectionClass.isInstance(dto));
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testMultiArray() throws Exception
  {
    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", Long.toString(34350));
    businessDAO.setValue("aCharacter", "Test Character");
    businessDAO.setValue("aDouble", Double.toString(3903.1213));
    businessDAO.setValue("aBoolean", Boolean.toString(true));
    collectionClass.getMethod("apply").invoke(businessDAO);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, businessDAO);

    Object array2 = Array.newInstance(array.getClass(), 1);
    Array.set(array2, 0, array);

    Object array3 = Array.newInstance(array2.getClass(), 1);
    Array.set(array3, 0, array2);

    Object array4 = Array.newInstance(array3.getClass(), 1);
    Array.set(array4, 0, array3);

    collectionClass.getMethod("lock").invoke(businessDAO);

    Method method = facadeProxyClass.getMethod("testMultiArray", ClientRequestIF.class, array4.getClass());
    String[][] output = (String[][]) method.invoke(generatedProxy, clientRequest, array4);

    assertNotNull(output);
    assertEquals(2, output.length);
    assertEquals(2, output[0].length);
    assertEquals(businessDAO.getValue("aLong"), output[0][0]);
    assertEquals(businessDAO.getValue("aCharacter"), output[0][1]);
    assertEquals(2, output[1].length);
    assertEquals(businessDAO.getValue("aDouble"), output[1][0]);
    assertEquals(businessDAO.getValue("aBoolean"), output[1][1]);
  }

  public void testEmptyArray() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "Har har de dar dar";
    String longInput = "152";

    Object array = Array.newInstance(collectionClass, 0);
    BusinessDTO[] output = (BusinessDTO[]) facadeProxyClass.getMethod("sortCollections", ClientRequestIF.class, array.getClass(), String.class).invoke(generatedProxy, clientRequest, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testSubclass() throws Exception
  {
    Class<?> bagClass = LoaderDecorator.load(bagDTO);
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String longInput = "278";

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput + "0");
    bagClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    bagClass.getMethod("lock").invoke(object);
    facadeProxyClass.getMethod("testMethod", ClientRequestIF.class, collectionClass).invoke(generatedProxy, clientRequest, object);

    object = (BusinessDTO) get.invoke(null, clientRequest, id);
    assertEquals(clientRequest.getSessionId(), collectionClass.getMethod("getACharacter").invoke(object));
  }

  public void testSubArray() throws Exception
  {
    Class<?> bagClass = LoaderDecorator.load(bagDTO);
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String longInput = "142";
    String input = "H to this izzo, E to the izza";

    BusinessDTO businessDAO = (BusinessDTO) bagClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    bagClass.getMethod("apply").invoke(businessDAO);
    String id = businessDAO.getId();

    Method get = bagClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequest, id);

    Class<?> arrayClass = Array.newInstance(collectionClass, 0).getClass();
    Object array = Array.newInstance(bagClass, 1);
    Array.set(array, 0, object);

    bagClass.getMethod("lock").invoke(object);
    BusinessDTO[] output = (BusinessDTO[]) facadeProxyClass.getMethod("sortCollections", ClientRequestIF.class, arrayClass, String.class).invoke(generatedProxy, clientRequest, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertTrue(collectionClass.isInstance(dto));
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testRelationship() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);
    String longInput = "152";

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);
    referenceClass.getMethod("apply").invoke(relationship);

    RelationshipDTO[] output = (RelationshipDTO[]) facadeProxyClass.getMethod("getReferences", ClientRequestIF.class, referenceClass).invoke(generatedProxy, clientRequest, relationship);

    assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      assertEquals(businessDAO.getId(), dto.getParentId());
      assertEquals(businessDAO2.getId(), dto.getChildId());
    }
  }

  public void testNewBusinessDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "164";

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", input);

    facadeProxyClass.getMethod("testMethod", ClientRequestIF.class, collectionClass).invoke(generatedProxy, clientRequest, businessDAO);
  }

  public void testNewBusinessDTOInArray() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    String input = "Har har bar bar";
    String longInput = "1";

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);

    Object array = Array.newInstance(collectionClass, 1);
    Array.set(array, 0, businessDAO);

    BusinessDTO[] output = (BusinessDTO[]) facadeProxyClass.getMethod("sortCollections", ClientRequestIF.class, array.getClass(), String.class).invoke(generatedProxy, clientRequest, array, input);

    assertEquals(Array.getLength(array), output.length);

    for (BusinessDTO dto : output)
    {
      assertTrue(collectionClass.isInstance(dto));
      assertEquals(input, dto.getValue("aCharacter"));
      assertEquals(longInput, dto.getValue("aLong"));
    }
  }

  public void testNewRelationship() throws Exception
  {
    String longInput = "152";

    // Create the existing BusinessDAO
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    BusinessDTO businessDAO2 = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO2.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO2);

    RelationshipDTO relationship = (RelationshipDTO) collectionClass.getMethod("addCollection2", collectionClass).invoke(businessDAO, businessDAO2);

    RelationshipDTO[] output = (RelationshipDTO[]) facadeProxyClass.getMethod("getReferences", ClientRequestIF.class, referenceClass).invoke(generatedProxy, clientRequest, relationship);

    assertEquals(5, output.length);

    for (RelationshipDTO dto : output)
    {
      assertEquals(businessDAO.getId(), dto.getParentId());
      assertEquals(businessDAO2.getId(), dto.getChildId());
    }
  }

  public void testEnumeration() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> stateClass = LoaderDecorator.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);
    String longInput = "152";

    // Create the existing BusinessDAO
    BusinessDTO businessDAO = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequest);
    businessDAO.setValue("aLong", longInput);
    collectionClass.getMethod("apply").invoke(businessDAO);

    EnumerationDTOIF enu = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, coloradoName);

    Method method = facadeProxyClass.getDeclaredMethod("getStates", ClientRequestIF.class, stateClass);

    EnumerationDTOIF[] output = (EnumerationDTOIF[]) method.invoke(generatedProxy, clientRequest, enu);

    assertEquals(5, output.length);

    for (EnumerationDTOIF dto : output)
    {
      assertEquals(enu.name(), dto.name());
    }
  }

  public void testReturnStates() throws Exception
  {
    Class<?> stateClass = LoaderDecorator.load(stateType + TypeGeneratorInfo.DTO_SUFFIX);

    EnumerationDTOIF[] enums = new EnumerationDTOIF[3];

    enums[0] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, californiaName);
    enums[1] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, coloradoName);
    enums[2] = (EnumerationDTOIF) stateClass.getMethod("valueOf", String.class).invoke(null, connecticutName);

    Object arr = Array.newInstance(stateClass, 3);
    Array.set(arr, 0, enums[0]);
    Array.set(arr, 1, enums[1]);
    Array.set(arr, 2, enums[2]);

    Method method = facadeProxyClass.getDeclaredMethod("returnStates", ClientRequestIF.class, arr.getClass());

    EnumerationDTOIF[] output = (EnumerationDTOIF[]) method.invoke(generatedProxy, clientRequest, arr);

    assertEquals(3, output.length);
    for (int i = 0; i < 3; i++)
    {
      assertEquals(enums[i].name(), output[i].name());
    }
  }

  /**
   * Test that the value of the .CLASS constant is equal to the name of the
   * clientRequest.
   * 
   * @throws Exception
   */
  public void testClassConstant() throws Exception
  {
    String classConstant = null;
    classConstant = (String) facadeProxyClass.getField("CLASS").get(classConstant);

    assertEquals(pack + "." + facadeName, classConstant);
  }

  /**
   * Test that the getProxy(label) method on the AbstractProxy returns the
   * correct clientRequest according to the label passed in.
   * 
   * @throws Exception
   */
  public void testGetClientRequest() throws Exception
  {
    String abstractName = pack + "." + facadeName + "Proxy";
    Class<?> abstractProxyClass = LoaderDecorator.load(abstractName);

    Object p = abstractProxyClass.getMethod("getClientRequest", String.class).invoke(null, label);

    assertTrue(facadeProxyClass.isInstance(p));
  }

  public void testThrow()
  {
    Class<?> exceptionClass = LoaderDecorator.load(pack + ".AlreadyCheckedOutException" + TypeGeneratorInfo.DTO_SUFFIX);
    try
    {
      facadeProxyClass.getDeclaredMethod("exceptionMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);
    }
    catch (Throwable e)
    {
      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && exceptionClass.isInstance(te))
        {
          String localizedMessage = "";

          try
          {
            localizedMessage = (String) exceptionClass.getMethod("getMessage").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          assertEquals("There are no copies of Atlas Shrugged available for checkout.", localizedMessage);
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }

  }

  public void testWarning()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO instance = null;
    try
    {
      instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("warningMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertTrue("Return object is of the wrong type.", collectionClass.isInstance(instance));

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("aCharacter"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

      systemSession.logout();
      try
      {

        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMAN });
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("warningMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);
        messageList = clientRequest.getMessages();
        messageDTO = messageList.get(0);
        localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
        assertEquals("10 ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.", localizedMessage);
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testWarningMethodReturnArrayObjects()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO[] collectionArray = null;
    try
    {
      collectionArray = (BusinessDTO[]) facadeProxyClass.getDeclaredMethod("warningMethodReturnArrayObjects", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertEquals("Returned the wrong number of elements in the array", 2, collectionArray.length);

      assertEquals("The Illiad", collectionArray[0].getValue("aCharacter"));
      assertEquals("The Odyssey", collectionArray[1].getValue("aCharacter"));

      assertTrue("Return object is of the wrong type.", collectionClass.isInstance(collectionArray[0]));
      assertTrue("Return object is of the wrong type.", collectionClass.isInstance(collectionArray[1]));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (collectionArray != null)
      {
        for (BusinessDTO bookDTO : collectionArray)
        {
          clientRequest.delete(bookDTO.getId());
        }
      }
    }
  }

  public void testWarningMdMethodReturnInt()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);

    try
    {
      Integer integer = (Integer) facadeProxyClass.getDeclaredMethod("warningMdMethodReturnInt", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertEquals("Returned the wrong Integer Value.", 5, integer.intValue());

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  public void testWarningMdMethodReturnIntArray()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);

    try
    {
      Integer[] integerArray = (Integer[]) facadeProxyClass.getDeclaredMethod("warningMdMethodReturnIntArray", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertEquals("Returned Integer Array is of the wrong size.", 2, integerArray.length);

      assertEquals("Returned the wrong Integer Value.", 6, integerArray[0].intValue());

      assertEquals("Returned the wrong Integer Value.", 7, integerArray[1].intValue());

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  public void testInformation()
  {
    Class<?> informationClass = LoaderDecorator.load(pack + ".RecommendedBook" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO instance = null;
    try
    {
      instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("informationMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertTrue("Return object is of the wrong type.", collectionClass.isInstance(instance));

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("aCharacter"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 0, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 1, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", informationClass.isInstance(messageDTO));

      String recommendedTitle = (String) informationClass.getMethod("getTitle").invoke(messageDTO);
      assertEquals("Attribute on information class has the wrong value.", "Tom Sawyer", recommendedTitle);

      String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("You may also enjoy Tom Sawyer.", localizedMessage);

      systemSession.logout();
      try
      {
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMAN });
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("informationMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);
        messageList = clientRequest.getMessages();
        messageDTO = messageList.get(0);
        localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
        assertEquals("Das Buch Tom Sawyer werden Sie gefallen.", localizedMessage);
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testMultipleMessages()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> informationClass = LoaderDecorator.load(pack + ".RecommendedBook" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO instance = null;
    try
    {
      instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("multipleMessagesMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertTrue("Return object is of the wrong type.", collectionClass.isInstance(instance));

      // Make sure the original object is returned.
      assertEquals("Moby Dick", instance.getValue("aCharacter"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 2, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 1, informationList.size());

      boolean warningClassFound = false;
      boolean informationClassFound = false;
      for (MessageDTO messageDTO : messageList)
      {
        if (warningClass.isInstance(messageDTO))
        {
          warningClassFound = true;

          Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
          assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

          String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
          assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);
        }
        else if (informationClass.isInstance(messageDTO))
        {
          informationClassFound = true;

          String recommendedTitle = (String) informationClass.getMethod("getTitle").invoke(messageDTO);
          assertEquals("Attribute on information class has the wrong value.", "Tom Sawyer", recommendedTitle);

          String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
          assertEquals("You may also enjoy Tom Sawyer.", localizedMessage);
        }
        else
        {
          fail("Returned message was of the wrong type.");
        }
      }

      assertTrue("Both message types were not returned from the method.", warningClassFound && informationClassFound);

      systemSession.logout();
      try
      {
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { Locale.GERMAN });
        clientRequest = getRequest(systemSession);
        instance = (BusinessDTO) facadeProxyClass.getDeclaredMethod("multipleMessagesMethod", ClientRequestIF.class).invoke(generatedProxy, clientRequest);
        messageList = clientRequest.getMessages();

        for (MessageDTO messageDTO : messageList)
        {
          if (warningClass.isInstance(messageDTO))
          {
            String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
            assertEquals("10 ist die Hoechstzahl der Buecher, die Sie zur gegebener Zeit ausleihen koennen.", localizedMessage);
          }
          else if (informationClass.isInstance(messageDTO))
          {
            String localizedMessage = (String) informationClass.getMethod("getMessage").invoke(messageDTO);
            assertEquals("Das Buch Tom Sawyer werden Sie gefallen.", localizedMessage);
          }
        }
      }
      finally
      {
        systemSession.logout();
        systemSession = this.createSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD);
        clientRequest = getRequest(systemSession);
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void testProblem_DeveloperMessage()
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> tooManyCheckedOutDTOclass = LoaderDecorator.load(pack + ".TooManyCheckedOutBooksProblem" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> overdueFeesDTOclass = LoaderDecorator.load(pack + ".OverdueLibraryFeesProblem" + TypeGeneratorInfo.DTO_SUFFIX);

    List<ProblemDTO> problemDTOList = new LinkedList<ProblemDTO>();

    boolean foundTooManyCheckedOut = false;
    boolean foundOverdueFee = false;
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    BusinessDTO instance = null;
    try
    {
      clientRequest.assignMember(tommyUser.getId(), RoleDAOIF.DEVELOPER_ROLE);
      clientRequest.grantTypePermission(tommyUser.getId(), collection.getId(), Operation.CREATE.name());
      clientRequest.grantTypePermission(tommyUser.getId(), collection.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
      clientRequest.grantMethodPermission(tommyUser.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      instance = tommyRequest.newBusiness(collectionType);
      instance.setValue("aCharacter", "Original Value");
      tommyRequest.createBusiness(instance);

      facadeProxyClass.getDeclaredMethod("problemMethod", ClientRequestIF.class, collectionClass).invoke(generatedProxy, tommyRequest, instance);

    }
    catch (Throwable e)
    {
      if (instance != null)
      {
        if (instance.getValue("title").equals("Changed Value"))
        {
          fail("Problems did not abort the transaction.  A value on an attribute was set.");
        }
        if (!instance.getValue("lockedBy").equals(""))
        {
          fail("Problems did not abort the transaction.  User lock was not rolled back.");
        }
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && te instanceof ProblemExceptionDTO)
        {
          Class<?> problemExceptionDTOclass = LoaderDecorator.load(ProblemExceptionDTO.class.getName());

          try
          {
            problemDTOList = (List<ProblemDTO>) problemExceptionDTOclass.getMethod("getProblems").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          for (ProblemDTO problemDTO : problemDTOList)
          {
            if (tooManyCheckedOutDTOclass.isInstance(problemDTO))
            {
              foundTooManyCheckedOut = true;

              int numberOfBooks = 10;
              try
              {
                numberOfBooks = (Integer) tooManyCheckedOutDTOclass.getMethod("getCheckedOutBooks").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("Problem1 Developer Message", problemDTO.getDeveloperMessage());
              assertEquals("You already have 10 books checked out.", problemDTO.getMessage());
              assertEquals(10, numberOfBooks);
            }
            else if (overdueFeesDTOclass.isInstance(problemDTO))
            {
              foundOverdueFee = true;

              int overdueFees = 1000;
              try
              {
                overdueFees = (Integer) overdueFeesDTOclass.getMethod("getTotalOutstandingFees").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("Problem2 Developer Message", problemDTO.getDeveloperMessage());
              assertEquals("You have $1000.00 in outstanding fees.", problemDTO.getMessage());
              assertEquals(overdueFees, 1000);
            }
          }
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }

      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }

      clientRequest.revokeTypePermission(tommyUser.getId(), collection.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), collection.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
      clientRequest.removeMember(tommyUser.getId(), RoleDAOIF.DEVELOPER_ROLE);
      clientRequest.revokeMethodPermission(tommyUser.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());
    }

    assertEquals("Wrong number of problems was generated.", 2, problemDTOList.size());
    assertTrue("The [" + tooManyCheckedOutDTOclass + "] problem was not returned.", foundTooManyCheckedOut);
    assertTrue("The [" + overdueFeesDTOclass + "] problem was not returned.", foundOverdueFee);
  }

  @SuppressWarnings("unchecked")
  public void testProblem_NoDeveloperMessage()
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> tooManyCheckedOutDTOclass = LoaderDecorator.load(pack + ".TooManyCheckedOutBooksProblem" + TypeGeneratorInfo.DTO_SUFFIX);
    Class<?> overdueFeesDTOclass = LoaderDecorator.load(pack + ".OverdueLibraryFeesProblem" + TypeGeneratorInfo.DTO_SUFFIX);

    List<ProblemDTO> problemDTOList = new LinkedList<ProblemDTO>();

    boolean foundTooManyCheckedOut = false;
    boolean foundOverdueFee = false;
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    BusinessDTO instance = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), collection.getId(), Operation.CREATE.name());
      clientRequest.grantTypePermission(tommyUser.getId(), collection.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
      clientRequest.grantMethodPermission(tommyUser.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      instance = tommyRequest.newBusiness(collectionType);
      instance.setValue("aCharacter", "Original Value");
      tommyRequest.createBusiness(instance);

      facadeProxyClass.getDeclaredMethod("problemMethod", ClientRequestIF.class, collectionClass).invoke(generatedProxy, tommyRequest, instance);

    }
    catch (Throwable e)
    {
      if (instance != null)
      {
        if (instance.getValue("title").equals("Changed Value"))
        {
          fail("Problems did not abort the transaction.  A value on an attribute was set.");
        }
        if (!instance.getValue("lockedBy").equals(""))
        {
          fail("Problems did not abort the transaction.  User lock was not rolled back.");
        }
      }

      if (e instanceof InvocationTargetException)
      {
        InvocationTargetException invocationTargetException = (InvocationTargetException) e;

        Throwable te = invocationTargetException.getTargetException();

        if (te != null && te instanceof ProblemExceptionDTO)
        {
          Class<?> problemExceptionDTOclass = LoaderDecorator.load(ProblemExceptionDTO.class.getName());

          try
          {
            problemDTOList = (List<ProblemDTO>) problemExceptionDTOclass.getMethod("getProblems").invoke(te);
          }
          catch (Exception exception)
          {
            fail(exception.getMessage());
          }

          for (ProblemDTO problemDTO : problemDTOList)
          {
            if (tooManyCheckedOutDTOclass.isInstance(problemDTO))
            {
              foundTooManyCheckedOut = true;

              int numberOfBooks = 10;
              try
              {
                numberOfBooks = (Integer) tooManyCheckedOutDTOclass.getMethod("getCheckedOutBooks").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("", problemDTO.getDeveloperMessage());
              assertEquals("You already have 10 books checked out.", problemDTO.getMessage());
              assertEquals(10, numberOfBooks);
            }
            else if (overdueFeesDTOclass.isInstance(problemDTO))
            {
              foundOverdueFee = true;

              int overdueFees = 1000;
              try
              {
                overdueFees = (Integer) overdueFeesDTOclass.getMethod("getTotalOutstandingFees").invoke(problemDTO);
              }
              catch (Exception exception)
              {
                fail(exception.getMessage());
              }

              assertEquals("", problemDTO.getDeveloperMessage());
              assertEquals("You have $1000.00 in outstanding fees.", problemDTO.getMessage());
              assertEquals(overdueFees, 1000);
            }
          }
        }
        else
        {
          fail(e.getMessage());
        }
      }
      else
      {
        fail(e.getMessage());
      }
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }

      if (instance != null)
      {
        clientRequest.delete(instance.getId());
      }

      clientRequest.revokeTypePermission(tommyUser.getId(), collection.getId(), Operation.CREATE.name());
      clientRequest.revokeTypePermission(tommyUser.getId(), collection.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacter.getId(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeLockedBy.getId(), Operation.READ.name());
      clientRequest.revokeMethodPermission(tommyUser.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());
    }

    assertEquals("Wrong number of problems was generated.", 2, problemDTOList.size());
    assertTrue("The [" + tooManyCheckedOutDTOclass + "] problem was not returned.", foundTooManyCheckedOut);
    assertTrue("The [" + overdueFeesDTOclass + "] problem was not returned.", foundOverdueFee);
  }

  public void testWarningMdMethodReturnQueryObject()
  {
    Class<?> warningClass = LoaderDecorator.load(pack + ".CheckoutLimitReached" + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDTO instance1 = null;
    BusinessDTO instance2 = null;
    BusinessQueryDTO bookQueryDTO = null;
    try
    {
      instance1 = clientRequest.newBusiness(collectionType);
      instance1.setValue("aCharacter", "Moby Dick");
      clientRequest.createBusiness(instance1);

      instance2 = clientRequest.newBusiness(collectionType);
      instance2.setValue("aCharacter", "Hunt for Red October");
      clientRequest.createBusiness(instance2);

      bookQueryDTO = (BusinessQueryDTO) facadeProxyClass.getDeclaredMethod("warningMdMethodReturnQueryObject", ClientRequestIF.class).invoke(generatedProxy, clientRequest);

      assertEquals("Returned the wrong number of elements in the array", 2, bookQueryDTO.getCount());

      List<? extends BusinessDTO> businessDTOList = bookQueryDTO.getResultSet();

      assertEquals("Hunt for Red October", businessDTOList.get(0).getValue("aCharacter"));
      assertEquals("Moby Dick", businessDTOList.get(1).getValue("aCharacter"));

      List<MessageDTO> messageList = clientRequest.getMessages();
      assertEquals("Only one message should have been returned.", 1, messageList.size());

      List<WarningDTO> warningList = clientRequest.getWarnings();
      assertEquals("Only one warning should have been returned.", 1, warningList.size());

      List<InformationDTO> informationList = clientRequest.getInformation();
      assertEquals("Only one information should have been returned.", 0, informationList.size());

      MessageDTO messageDTO = messageList.get(0);
      assertTrue("Message is of the wrong type.", warningClass.isInstance(messageDTO));

      Integer maxAllowedBooks = (Integer) warningClass.getMethod("getMaxAllowedBooks").invoke(messageDTO);
      assertEquals("Attribute on warning class has the wrong value.", 10, maxAllowedBooks + 0);

      String localizedMessage = (String) warningClass.getMethod("getMessage").invoke(messageDTO);
      assertEquals("10 is the maximum number of books you can check out at a given time.", localizedMessage);

    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance1 != null)
      {
        clientRequest.delete(instance1.getId());
      }

      if (instance2 != null)
      {
        clientRequest.delete(instance2.getId());
      }
    }
  }

  public void testGrantGrantMethodPermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;

    try
    {
      clientRequest.grantMethodPermission(tommyUser.getId(), mdMethod2.getId(), Operation.GRANT.name());

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantMethodPermission(littleBillyTables.getId(), mdMethod2.getId(), Operation.EXECUTE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      Method poopNothing = facadeProxyClass.getMethod("poopNothing", ClientRequestIF.class);
      poopNothing.invoke(generatedProxy, billyRequest);

      tommyRequest.revokeMethodPermission(littleBillyTables.getId(), mdMethod2.getId(), Operation.EXECUTE.name());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {

      if (tommySession != null)
      {
        tommySession.logout();
      }

      if (billySession != null)
      {
        billySession.logout();
      }

      clientRequest.revokeMethodPermission(tommyUser.getId(), mdMethod2.getId(), Operation.GRANT.name());
    }
  }

  public void testInvalidGrantGrantPermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantMethodPermission(littleBillyTables.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());
    }
    catch (GrantMethodPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testInvalidGrantRevokePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeMethodPermission(littleBillyTables.getId(), problemMdMethod.getId(), Operation.EXECUTE.name());
    }
    catch (RevokeMethodPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  protected static String getFacadeSource()
  {
    String[] bookFacadeStubSource = {
        "package " + pack + ";",
        "public class BookFacade extends BookFacade" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS,
        "{",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static void testMethod(java.lang.String sessionId, " + collectionType + " collection)",
        "  {",
        "    collection.setACharacter(sessionId);",
        "    collection.apply();",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + collectionType + " sortNumbers(java.lang.String sessionId, Long[] numbers, Boolean ascending)",
        "  {",
        "     " + collectionType + " collection = new " + collectionType + "();",
        "     collection.setABoolean(ascending);",
        "     if(numbers != null && numbers.length > 0)",
        "       collection.setALong(numbers[0]);",
        "     ",
        "     return collection;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + collectionType + "[] sortCollections(java.lang.String sessionId, " + collectionType + "[] collections, String collectionName)",
        "  {",
        "     " + collectionType + "[] output = new " + collectionType + "[collections.length];",
        "     for(int i = 0; i < collections.length; i++)",
        "     {",
        "       output[i] = new " + collectionType + "();",
        "       output[i].setALong(collections[i].getALong());",
        "       output[i].setACharacter(collectionName);",
        "       output[i].apply();",
        "     }",
        "     return output;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static java.lang.String[][] testMultiArray(java.lang.String sessionId, " + collectionType + "[][][][] collection4)",
        "  {",
        "     " + collectionType + " collection = collection4[0][0][0][0];",
        "     String[][] output = new String[2][2];",
        "     output[0][0] = collection.getALong().toString();",
        "     output[0][1] = collection.getACharacter().toString();",
        "     output[1][0] = collection.getADouble().toString();",
        "     output[1][1] = collection.getABoolean().toString();",
        "     return output;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static void poopNothing(java.lang.String sessionId)",
        "  {",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + referenceType + "[] getReferences(java.lang.String sessionId, " + referenceType + " reference)",
        "  {",
        "    " + referenceType + "[] array = new " + referenceType + "[5];",
        "    for(int i = 0; i < 5; i++)",
        "      array[i] = reference;",
        "    return array;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + stateType + "[] getStates(java.lang.String sessionId, " + stateType + " state)",
        "  {",
        "    " + stateType + "[] array = new " + stateType + "[5];",
        "    for(int i = 0; i < 5; i++)",
        "      array[i] = state;",
        "    return array;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + stateType + "[] returnStates(java.lang.String sessionId, " + stateType + "[] states)",
        "  {",
        "    return states;",
        "  }",
        " " + "@" + Authenticate.class.getName() + "\n" + "  public static void exceptionMethod(java.lang.String sessionId)\n" + "  {\n" + "    AlreadyCheckedOutException acoe = new AlreadyCheckedOutException(\"Sup, developer\");\n" + "    acoe.setBookTitle(\"Atlas Shrugged\");\n" + "    throw acoe;\n" + "  }\n" + " " + "@" + Authenticate.class.getName() + "\n" + "  public static " + collectionType + " warningMethod(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    " + collectionType + " collection = new " + collectionType + "();",
        "    collection.setACharacter(\"Moby Dick\");",
        "    collection.apply();",
        "    \n" + "    return collection;\n" + "  }\n" + "  public static " + collectionType + "[] warningMethodReturnArrayObjects(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    " + collectionType + " collection = new " + collectionType + "();",
        "    \n" + "    " + collectionType + "[] collectionArray = new " + collectionType + "[2]; \n" + "    \n" + "    collection.setACharacter(\"The Illiad\");",
        "    collection.apply();",
        "    collectionArray[0] = collection;\n" + "    \n" + "    collection = new " + collectionType + "();",
        "    collection.setACharacter(\"The Odyssey\");",
        "    collection.apply();",
        "    collectionArray[1] = collection;\n" + "    \n" + "    return collectionArray;\n" + "  }\n" + "  public static " + Integer.class.getName() + " warningMdMethodReturnInt(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    return 5;\n" + "  }\n" + "  public static " + Integer.class.getName() + "[] warningMdMethodReturnIntArray(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    Integer[] intArray = new Integer[2];\n"
            + "    intArray[0] = 6;\n" + "    intArray[1] = 7;\n" + "    return intArray;\n" + "  }\n" + "  public static " + collectionType + " informationMethod(java.lang.String sessionId)\n" + "  {\n" + "    RecommendedBook information1 = new RecommendedBook();\n" + "    information1.setTitle(\"Tom Sawyer\");\n" + "    information1.apply();\n" + "    information1.throwIt();\n" + "    \n" + "    " + collectionType + " collection = new " + collectionType + "();",
        "    collection.setACharacter(\"Moby Dick\");",
        "    collection.apply();",
        "    \n" + "    return collection;\n" + "  }\n" + "  public static " + collectionType + " multipleMessagesMethod(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "\n" + "    RecommendedBook information1 = new RecommendedBook();\n" + "    information1.setTitle(\"Tom Sawyer\");\n" + "    information1.apply();\n" + "    information1.throwIt();\n" + "    \n" + "    " + collectionType + " collection = new " + collectionType + "();",
        "    collection.setACharacter(\"Moby Dick\");",
        "    collection.apply();",
        "    \n" + "    return collection;\n" + "  }\n" + "  @" + Transaction.class.getName() + "\n" + "  public static void problemMethod(java.lang.String sessionId, " + collectionType + " collection)\n" + "  {\n" + "    TooManyCheckedOutBooksProblem problem1 = new TooManyCheckedOutBooksProblem(\"Problem1 Developer Message\");\n" + "    problem1.setCheckedOutBooks(10);\n" + "    problem1.apply();\n" + "    problem1.throwIt();\n" + "    \n" + "    OverdueLibraryFeesProblem problem2 = new OverdueLibraryFeesProblem(\"Problem2 Developer Message\");\n" + "    problem2.setTotalOutstandingFees(1000);\n" + "    problem2.apply();\n" + "    problem2.throwIt();\n" + "    \n" + "    collection.lock();\n" + "    collection.setACharacter(\"Changed Value\");\n" + "    collection.apply();\n" + "  }\n"
            + "  public static " + collectionQueryType + " warningMdMethodReturnQueryObject(java.lang.String sessionId)\n" + "  {\n" + "    CheckoutLimitReached warning1 = new CheckoutLimitReached();\n" + "    warning1.setMaxAllowedBooks(10);\n" + "    warning1.apply();\n" + "    warning1.throwIt();\n" + "    \n" + "    " + QueryFactory.class.getName() + " q = new " + QueryFactory.class.getName() + "();\n" + "    " + collectionQueryType + " collectionQuery = new " + collectionQueryType + "(q);\n" + "    collectionQuery.ORDER_BY_ASC(collectionQuery.getACharacter());\n" + "    \n" + "    return collectionQuery;\n" + "  }\n" + "  public static void deleteCollections(java.lang.String sessionId)\n" + "  {\n" + "    " + QueryFactory.class.getName() + " q = new " + QueryFactory.class.getName()
            + "();\n" + "    " + collectionQueryType + " collectionQuery = new " + collectionQueryType + "(q);\n" + "    \n" + "    for (" + collectionType + " collection: collectionQuery.getIterator())\n" + "    {\n" + "      collection.delete();\n" + "    }\n" + "  }\n" + "}" };

    String source = "";
    for (String s : bookFacadeStubSource)
    {
      source += s + "\n";
    }
    return source;
  }

  @Request
  private static void addLocale(Locale locale)
  {
    SupportedLocaleDAO supportedLocaleDAO = SupportedLocaleDAO.newInstance();
    supportedLocaleDAO.setValue(SupportedLocaleInfo.NAME, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.DISPLAY_LABEL, locale.toString());
    supportedLocaleDAO.setValue(SupportedLocaleInfo.LOCALE_LABEL, locale.toString());
    supportedLocaleDAO.apply();
  }

  @Request
  private static void deleteLocale(Locale locale)
  {
    SupportedLocaleDAO supportedLocaleDAO = (SupportedLocaleDAO) SupportedLocaleDAO.getEnumeration(SupportedLocaleInfo.CLASS, locale.toString());
    supportedLocaleDAO.delete();
  }
}
