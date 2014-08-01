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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.runwaysdk.ClientSession;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.ServerSideException;
import com.runwaysdk.TestSuiteTF;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsChild;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsParent;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.ElementDTO;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.InvalidEnumerationNameDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ValueObjectDTO;
import com.runwaysdk.business.ValueQueryDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.dto.EntityDTOBaseGenerator;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.AdminConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexAttributeInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeHashInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeMultiReferenceInfo;
import com.runwaysdk.constants.MdAttributeMultiTermInfo;
import com.runwaysdk.constants.MdAttributeNumberInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdAttributeVirtualInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTermInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.PhoneNumberInfo;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.DuplicateDataExceptionDTO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.dataaccess.UnexpectedTypeExceptionDTO;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblemDTO;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblemDTO;
import com.runwaysdk.dataaccess.cache.DataNotFoundExceptionDTO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.ConditionOperator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.AttributeWritePermissionExceptionDTO;
import com.runwaysdk.session.DeleteChildPermissionExceptionDTO;
import com.runwaysdk.session.DeleteParentPermissionExceptionDTO;
import com.runwaysdk.session.DeletePermissionExceptionDTO;
import com.runwaysdk.session.GrantAttributePermissionExceptionDTO;
import com.runwaysdk.session.GrantAttributeStatePermissionExceptionDTO;
import com.runwaysdk.session.GrantStatePermissionExceptionDTO;
import com.runwaysdk.session.GrantTypePermissionExceptionDTO;
import com.runwaysdk.session.ImportDomainExecuteExceptionDTO;
import com.runwaysdk.session.InvalidLoginExceptionDTO;
import com.runwaysdk.session.ReadChildPermissionExceptionDTO;
import com.runwaysdk.session.ReadParentPermissionExceptionDTO;
import com.runwaysdk.session.ReadPermissionExceptionDTO;
import com.runwaysdk.session.ReadTypePermissionExceptionDTO;
import com.runwaysdk.session.RevokeAttributePermissionExceptionDTO;
import com.runwaysdk.session.RevokeAttributeStatePermissionExceptionDTO;
import com.runwaysdk.session.RevokeStatePermissionExceptionDTO;
import com.runwaysdk.session.RevokeTypePermissionExceptionDTO;
import com.runwaysdk.transport.attributes.AttributeBlobDTO;
import com.runwaysdk.transport.attributes.AttributeBooleanDTO;
import com.runwaysdk.transport.attributes.AttributeCharacterDTO;
import com.runwaysdk.transport.attributes.AttributeClobDTO;
import com.runwaysdk.transport.attributes.AttributeDTO;
import com.runwaysdk.transport.attributes.AttributeDateDTO;
import com.runwaysdk.transport.attributes.AttributeDateTimeDTO;
import com.runwaysdk.transport.attributes.AttributeDecimalDTO;
import com.runwaysdk.transport.attributes.AttributeDoubleDTO;
import com.runwaysdk.transport.attributes.AttributeEnumerationDTO;
import com.runwaysdk.transport.attributes.AttributeFloatDTO;
import com.runwaysdk.transport.attributes.AttributeIntegerDTO;
import com.runwaysdk.transport.attributes.AttributeLongDTO;
import com.runwaysdk.transport.attributes.AttributeReferenceDTO;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.attributes.AttributeTermDTO;
import com.runwaysdk.transport.attributes.AttributeTextDTO;
import com.runwaysdk.transport.attributes.AttributeTimeDTO;
import com.runwaysdk.transport.metadata.AttributeBooleanMdDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeMultiTermMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.AttributeTermMdDTO;
import com.runwaysdk.util.FileIO;
import com.runwaysdk.util.IDGenerator;
import com.runwaysdk.web.AdminScreenAccessExceptionDTO;

public class AdapterTest extends TestCase
{
  protected static String            pack                           = "com.test.controller";

  protected static BusinessDTO       tommyUser                      = null;

  protected static BusinessDTO       littleBillyTables              = null;

  protected static ClientSession     systemSession                  = null;

  protected static ClientRequestIF   clientRequest                  = null;

  protected static BusinessDTO       childMdBusiness                = null;

  protected static String            childMdBusinessType            = null;

  protected static BusinessDTO       parentMdBusiness               = null;

  protected static String            parentMdBusinessType           = null;

  protected static BusinessDTO       mdView                         = null;

  protected static String            mdViewType                     = null;

  protected static BusinessDTO       childInstance                  = null;

  protected static BusinessDTO       parentInstance                 = null;

  private static BusinessDTO         mdRelationship                 = null;

  protected static String            mdRelationshipType             = null;

  protected static RelationshipDTO   relInstance                    = null;

  protected static BusinessDTO       mdAttributeCharacterDTO_2      = null;

  private static BusinessDTO         stateMachine                   = null;

  protected static String            stateMachineType               = null;

  protected static BusinessDTO       state1                         = null;

  protected static BusinessDTO       state2                         = null;

  protected static RelationshipDTO   transition                     = null;

  protected static RelationshipDTO   transition2                    = null;

  /* Md references for attribute metadata tests */
  protected static BusinessDTO       mdAttributeBlobDTO             = null;

  protected static BusinessDTO       testUserMd                     = null;

  protected static String            testUserType                   = null;

  protected static BusinessDTO       mdAttributeBooleanDTO          = null;

  protected static BusinessDTO       mdAttributeConversionDTO       = null;

  protected static BusinessDTO       mdAttributeIntegerDTO          = null;

  protected static BusinessDTO       mdAttributeLongDTO             = null;

  protected static BusinessDTO       mdAttributeDecimalDTO          = null;

  protected static BusinessDTO       mdAttributeDoubleDTO           = null;

  protected static BusinessDTO       mdAttributeFloatDTO            = null;

  protected static BusinessDTO       mdAttributeDateTimeDTO         = null;

  protected static BusinessDTO       mdAttributeFileDTO             = null;

  protected static BusinessDTO       mdAttributeDateDTO             = null;

  protected static BusinessDTO       mdAttributeTimeDTO             = null;

  protected static BusinessDTO       suitMaster                     = null;

  protected static String            suitMasterType                 = null;

  protected static BusinessDTO       structMdBusiness               = null;

  protected static String            structType                     = null;

  protected static BusinessDTO       suitMdEnumeration              = null;

  protected static String            suitMdEnumerationType          = null;

  protected static List<String>      suitEnumNames;

  protected static List<String>      suitEnumDisplayLabels;

  protected static BusinessDTO       enumMdAttribute                = null;

  protected static BusinessDTO       mdAttributeEnumerationDTO      = null;

  protected static BusinessDTO       mdAttributeMultiReferenceDTO   = null;

  protected static BusinessDTO       mdAttributeMultiTermDTO        = null;

  protected static BusinessDTO       mdAttributeStructDTO           = null;

  protected static BusinessDTO       refClass                       = null;

  protected static BusinessDTO       termClass                      = null;

  protected static String            refType                        = null;

  protected static String            termType                       = null;

  protected static BusinessDTO       mdAttributeReferenceDTO        = null;

  protected static BusinessDTO       mdAttributeTermDTO             = null;

  protected static BusinessDTO       mdAttributeHashDTO             = null;

  protected static BusinessDTO       mdAttributeSymmetricDTO        = null;

  protected static BusinessDTO       mdAttributeCharacterDTO        = null;

  protected static BusinessDTO       mdAttributeVirtualCharacterDTO = null;

  protected static BusinessDTO       mdAttributeTextDTO             = null;

  protected static BusinessDTO       mdAttributeClobDTO             = null;

  protected static BusinessDTO       vault                          = null;

  protected static BusinessDTO       relMdAttributeLongDTO          = null;

  protected static BusinessDTO       structMdAttributeCharDTO       = null;

  protected static BusinessDTO       hearts                         = null;

  protected static BusinessDTO       clubs                          = null;

  protected static BusinessDTO       spades                         = null;

  protected static BusinessDTO       diamonds                       = null;

  protected static List<BusinessDTO> suits                          = new LinkedList<BusinessDTO>();

  private static final String        toStringPrepend                = "Test toString: ";

  protected static final byte[]      bytes                          = { 1, 2, 3, 4 };

  protected static final byte[]      bytes2                         = { 2, 3, 4, 5, 6 };

  public static Test suite()
  {
    TestSuiteTF suite = new TestSuiteTF();
    suite.addTestSuite(AdapterTest.class);

    TestSetup wrapper = new TestSetup(suite)
    {
      protected void setUp()
      {
        systemSession = ClientSession.createUserSession("default", ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
        clientRequest = systemSession.getRequest();
        classSetUp();
        finalizeSetup();
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
    return ClientSession.createAnonymousSession("default", new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession("default", userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return clientSession.getRequest();
  }

  public static void classSetUp()
  {
    // create a new TestUser type with a phone number struct
    testUserMd = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    testUserMd.setValue(MdBusinessInfo.NAME, "TestUser");
    testUserMd.setValue(MdBusinessInfo.PACKAGE, pack);
    testUserMd.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testUserMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "User Subclass");
    testUserMd.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, UserInfo.ID_VALUE);
    clientRequest.createBusiness(testUserMd);

    testUserType = definesType(testUserMd);

    tommyUser = clientRequest.newBusiness(testUserType);
    tommyUser.setValue(UserInfo.USERNAME, "Tommy");
    tommyUser.setValue(UserInfo.PASSWORD, "music");
    clientRequest.createBusiness(tommyUser);

    littleBillyTables = clientRequest.newBusiness(testUserType);
    littleBillyTables.setValue(UserInfo.USERNAME, "Billy");
    littleBillyTables.setValue(UserInfo.PASSWORD, "Tables");
    clientRequest.createBusiness(littleBillyTables);

    suitMaster = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    suitMaster.setValue(MdBusinessInfo.NAME, "SuitMaster");
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    clientRequest.createBusiness(suitMaster);

    suitMdEnumeration = clientRequest.newBusiness(MdEnumerationInfo.CLASS);
    suitMdEnumeration.setValue(MdEnumerationInfo.NAME, "SuitEnum");
    suitMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, pack);
    suitMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration");
    suitMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    suitMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getId());
    clientRequest.createBusiness(suitMdEnumeration);

    suitMdEnumerationType = pack + ".SuitEnum";

    enumMdAttribute = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    enumMdAttribute.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "32");
    enumMdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    enumMdAttribute.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    enumMdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, suitMaster.getId());
    clientRequest.createBusiness(enumMdAttribute);

    structMdBusiness = clientRequest.newBusiness(MdStructInfo.CLASS);
    structMdBusiness.setValue(MdStructInfo.NAME, "StructTest");
    structMdBusiness.setValue(MdStructInfo.PACKAGE, pack);
    structMdBusiness.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A strct");
    structMdBusiness.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "junit struct");
    clientRequest.createBusiness(structMdBusiness);

    structMdAttributeCharDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.NAME, "structChar");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    structMdAttributeCharDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "struct string");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, structMdBusiness.getId());
    clientRequest.createBusiness(structMdAttributeCharDTO);

    childMdBusiness = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    childMdBusiness.setValue(MdBusinessInfo.NAME, "ChildTest");
    childMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    childMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test child");
    childMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    childMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(childMdBusiness);

    BusinessDTO phoneNumber = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    phoneNumber.setValue(MdAttributeStructInfo.NAME, "phoneNumber");
    phoneNumber.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Phone Number");
    phoneNumber.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testUserMd.getId());
    phoneNumber.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    phoneNumber.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    phoneNumber.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(phoneNumber);

    mdAttributeCharacterDTO_2 = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacterDTO_2.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, childMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeCharacterDTO_2);

    stateMachine = clientRequest.newBusiness(MdStateMachineInfo.CLASS);
    stateMachine.setValue(MdStateMachineInfo.NAME, "ChildStateMachine");
    stateMachine.setValue(MdStateMachineInfo.PACKAGE, pack);
    stateMachine.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Child State Machine");
    stateMachine.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, childMdBusiness.getId());
    stateMachine.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    clientRequest.createBusiness(stateMachine);

    parentMdBusiness = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    parentMdBusiness.setValue(MdBusinessInfo.NAME, "ParentTest");
    parentMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    parentMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test parent");
    parentMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    parentMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.addEnumItem(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.toString());

    String source = "package com.test.controller;\n" + "public class ParentTest extends ParentTestBase implements " + Reloadable.class.getName() + "\n" + "{" + "public ParentTest()" + "{" + "   super();" + "}" + "public static ParentTest get(String id)" + "{" + "  return (ParentTest) " + Business.class.getName() + ".get(id);" + "}" + "public String toString()" + "{" + "  return \"" + toStringPrepend + "\" + getId();" + "}" + "}";

    parentMdBusiness.setValue(MdElementInfo.STUB_SOURCE, source);
    clientRequest.createBusiness(parentMdBusiness);

    mdAttributeBooleanDTO = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeBooleanDTO);

    BusinessDTO mdAttributeBooleanImmutableDTO = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.NAME, "anImmutableBoolean");
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Immutable Boolean");
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeBooleanImmutableDTO);

    mdAttributeCharacterDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character desc");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.toString());
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeCharacterDTO);

    mdAttributeConversionDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.NAME, "aHiddenCharacter");
    mdAttributeConversionDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hidden Character");
    mdAttributeConversionDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hidden Character desc");
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.toString());
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.FALSE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeConversionDTO);

    BusinessDTO mdAttributeCharGroupIndex1_DTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacterGroupIndex1");
    mdAttributeCharGroupIndex1_DTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character Group Index 1");
    mdAttributeCharGroupIndex1_DTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character for a group index");
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeCharGroupIndex1_DTO);

    BusinessDTO mdAttributeCharGroupIndex2_DTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacterGroupIndex2");
    mdAttributeCharGroupIndex2_DTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character Group Index 2");
    mdAttributeCharGroupIndex2_DTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character for a group index");
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeCharGroupIndex2_DTO);

    BusinessDTO mdIndexDTO = clientRequest.newBusiness(MdIndexInfo.CLASS);
    mdIndexDTO.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndexDTO.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Group Index");
    mdIndexDTO.setValue(MdIndexInfo.MD_ENTITY, parentMdBusiness.getId());
    clientRequest.createBusiness(mdIndexDTO);

    RelationshipDTO childDTO = clientRequest.addChild(mdIndexDTO.getId(), mdAttributeCharGroupIndex1_DTO.getId(), IndexAttributeInfo.CLASS);
    childDTO.setValue(IndexAttributeInfo.INDEX_ORDER, "0");
    clientRequest.createRelationship(childDTO);
    childDTO = clientRequest.addChild(mdIndexDTO.getId(), mdAttributeCharGroupIndex2_DTO.getId(), IndexAttributeInfo.CLASS);
    childDTO.setValue(IndexAttributeInfo.INDEX_ORDER, "1");
    clientRequest.createRelationship(childDTO);

    clientRequest.lock(mdIndexDTO);
    mdIndexDTO.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    clientRequest.update(mdIndexDTO);

    mdAttributeDecimalDTO = clientRequest.newBusiness(MdAttributeDecimalInfo.CLASS);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.NAME, "aDecimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal desc");
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.LENGTH, "16");
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.DECIMAL, "4");
    clientRequest.createBusiness(mdAttributeDecimalDTO);

    mdAttributeDoubleDTO = clientRequest.newBusiness(MdAttributeDoubleInfo.CLASS);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    mdAttributeDoubleDTO.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    mdAttributeDoubleDTO.setStructValue(MdAttributeDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double desc");
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    clientRequest.createBusiness(mdAttributeDoubleDTO);

    mdAttributeFloatDTO = clientRequest.newBusiness(MdAttributeFloatInfo.CLASS);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.NAME, "aFloat");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float Desc");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.LENGTH, "16");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DECIMAL, "4");
    clientRequest.createBusiness(mdAttributeFloatDTO);

    mdAttributeIntegerDTO = clientRequest.newBusiness(MdAttributeIntegerInfo.CLASS);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.NAME, "anInteger");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer Desc");
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeIntegerDTO);

    mdAttributeLongDTO = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.NAME, "aLong");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long Desc");
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttributeLongDTO);

    mdAttributeDateDTO = clientRequest.newBusiness(MdAttributeDateInfo.CLASS);
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.NAME, "aDate");
    mdAttributeDateDTO.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date");
    mdAttributeDateDTO.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date Desc");
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeDateDTO);

    mdAttributeDateTimeDTO = clientRequest.newBusiness(MdAttributeDateTimeInfo.CLASS);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.NAME, "aDateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime Desc");
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeDateTimeDTO);

    mdAttributeTimeDTO = clientRequest.newBusiness(MdAttributeTimeInfo.CLASS);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.NAME, "aTime");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time Desc");
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeTimeDTO);

    // add an MdAttributeBlob to the new type
    mdAttributeBlobDTO = clientRequest.newBusiness(MdAttributeBlobInfo.CLASS);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.NAME, "aBlob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "some blob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob desc");
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeBlobDTO);

    mdAttributeEnumerationDTO = clientRequest.newBusiness(MdAttributeEnumerationInfo.CLASS);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.NAME, "anEnum");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated Attribute");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated desc");
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitMdEnumeration.getId());
    clientRequest.createBusiness(mdAttributeEnumerationDTO);

    mdAttributeHashDTO = clientRequest.newBusiness(MdAttributeHashInfo.CLASS);
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.NAME, "aHash");
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hashed Attributed");
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hash Desc");
    mdAttributeHashDTO.addEnumItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.toString());
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeHashDTO);

    mdAttributeFileDTO = clientRequest.newBusiness(MdAttributeFileInfo.CLASS);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.NAME, "aFile");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File Desc");
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeFileDTO);

    // class for ref object attribute
    refClass = clientRequest.newBusiness(MdBusinessInfo.CLASS);
    refClass.setValue(MdBusinessInfo.NAME, "RefClass");
    refClass.setValue(MdBusinessInfo.PACKAGE, pack);
    refClass.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    refClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A ref class");
    refClass.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit ref object");
    refClass.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    refClass.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(refClass);

    mdAttributeReferenceDTO = clientRequest.newBusiness(MdAttributeReferenceInfo.CLASS);
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.NAME, "aReference");
    mdAttributeReferenceDTO.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference");
    mdAttributeReferenceDTO.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference Desc");
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, refClass.getId());
    clientRequest.createBusiness(mdAttributeReferenceDTO);

    // class for term object attribute
    termClass = clientRequest.newBusiness(MdTermInfo.CLASS);
    termClass.setValue(MdBusinessInfo.NAME, "TermClass");
    termClass.setValue(MdBusinessInfo.PACKAGE, pack);
    termClass.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    termClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A term class");
    termClass.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit term object");
    termClass.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    termClass.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(termClass);

    mdAttributeTermDTO = clientRequest.newBusiness(MdAttributeTermInfo.CLASS);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.NAME, "aTerm");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term Desc");
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeTermDTO);

    mdAttributeMultiReferenceDTO = clientRequest.newBusiness(MdAttributeMultiReferenceInfo.CLASS);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.NAME, "aMultiReference");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeMultiReferenceDTO);

    mdAttributeMultiTermDTO = clientRequest.newBusiness(MdAttributeMultiTermInfo.CLASS);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.NAME, "aMultiTerm");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, termClass.getId());
    clientRequest.createBusiness(mdAttributeMultiTermDTO);

    mdAttributeStructDTO = clientRequest.newBusiness(MdAttributeStructInfo.CLASS);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.NAME, "aStruct");
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Desc");
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getId());
    clientRequest.createBusiness(mdAttributeStructDTO);

    mdAttributeSymmetricDTO = clientRequest.newBusiness(MdAttributeSymmetricInfo.CLASS);
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.NAME, "aSym");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Attribute");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Desc");
    mdAttributeSymmetricDTO.addEnumItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.toString());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    clientRequest.createBusiness(mdAttributeSymmetricDTO);

    mdAttributeTextDTO = clientRequest.newBusiness(MdAttributeTextInfo.CLASS);
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.NAME, "aText");
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text Desc");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeTextDTO);

    mdAttributeClobDTO = clientRequest.newBusiness(MdAttributeClobInfo.CLASS);
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.NAME, "aClob");
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob Desc");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdBusiness.getId());
    clientRequest.createBusiness(mdAttributeClobDTO);

    mdRelationship = clientRequest.newBusiness(MdRelationshipInfo.CLASS);
    mdRelationship.setValue(MdRelationshipInfo.NAME, "DtoRel");
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, parentMdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent dto");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, childMdBusiness.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child dto");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "testParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "testChild");
    clientRequest.createBusiness(mdRelationship);

    addAttributesToQuery(parentMdBusiness.getId());

    // Add attributes to the relationship to query
    BusinessDTO businessDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "relChar");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    clientRequest.createBusiness(businessDTO);

    relMdAttributeLongDTO = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.NAME, "relLong");
    relMdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A long to query.");
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "123321");
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    clientRequest.createBusiness(relMdAttributeLongDTO);

    businessDTO = clientRequest.newBusiness(MdAttributeTimeInfo.CLASS);
    businessDTO.setValue(MdAttributeTimeInfo.NAME, "relTime");
    businessDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A time to query.");
    businessDTO.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    clientRequest.createBusiness(businessDTO);

    businessDTO = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    businessDTO.setValue(MdAttributeBooleanInfo.NAME, "relBoolean");
    businessDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A boolean to query.");
    businessDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdRelationship.getId());
    clientRequest.createBusiness(businessDTO);

    mdView = clientRequest.newBusiness(MdViewInfo.CLASS);
    mdView.setValue(MdViewInfo.NAME, "TestView");
    mdView.setValue(MdViewInfo.PACKAGE, pack);
    mdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test view");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    mdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);

    clientRequest.createBusiness(mdView);

    mdViewType = definesType(mdView);

    mdAttributeVirtualCharacterDTO = clientRequest.newBusiness(MdAttributeVirtualInfo.CLASS);
    mdAttributeVirtualCharacterDTO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeCharacterDTO.getId());
    mdAttributeVirtualCharacterDTO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, mdView.getId());
    clientRequest.createBusiness(mdAttributeVirtualCharacterDTO);

    String objectIdConst = TypeGenerator.buildAttributeConstant(mdViewType, "objectId");
    String queryBooleanConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryBoolean");
    String queryCharConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryChar");
    String aCharConst = TypeGenerator.buildAttributeConstant(mdViewType, "aCharacter");
    String queryLongConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryLong");
    String queryTimeConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryTime");

    businessDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "objectId");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Original Object Id");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdView.getId());
    clientRequest.createBusiness(businessDTO);

    addAttributesToQuery(mdView.getId());

    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase implements " + Reloadable.class.getName() + "\n" + "{\n" + "\n" + "  private ParentTestQuery parentTest;\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "     \n" + "     parentTest = new ParentTestQuery(componentQueryFactory);\n" + "\n" + "     this.map(" + objectIdConst + ", parentTest.getId());\n" + "     this.map(" + queryBooleanConst + ", parentTest.getQueryBoolean());\n" + "     this.map(" + queryCharConst + ", parentTest.getQueryChar());\n" + "     this.map(" + aCharConst + ", parentTest.getACharacter());\n" + "     this.map(" + queryLongConst
        + ", parentTest.getQueryLong());\n" + "     this.map(" + queryTimeConst + ", parentTest.getQueryTime());\n" + "\n" + "     this.buildSelectClause();\n" + "  }\n" + "}\n";

    clientRequest.lock(mdView);
    mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, queryStubSource);
    clientRequest.update(mdView);

    vault = clientRequest.newBusiness(VaultInfo.CLASS);
    vault.setValue(VaultInfo.VAULT_NAME, "vault1");
    clientRequest.createBusiness(vault);

    childMdBusinessType = definesType(childMdBusiness);
    parentMdBusinessType = definesType(parentMdBusiness);
    stateMachineType = definesType(stateMachine);
    mdRelationshipType = definesType(mdRelationship);
    refType = definesType(refClass);
    termType = definesType(termClass);
    structType = definesType(structMdBusiness);

    suitMasterType = suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME);

  }

  private static String definesType(BusinessDTO mdBusinessDTO)
  {
    return pack + "." + mdBusinessDTO.getValue(MdTypeInfo.NAME);
  }

  private static void addAttributesToQuery(String mdClassId)
  {
    // START attributes to the parent mdentity to query
    BusinessDTO businessDTO = clientRequest.newBusiness(MdAttributeCharacterInfo.CLASS);
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "queryChar");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(businessDTO);

    businessDTO = clientRequest.newBusiness(MdAttributeLongInfo.CLASS);
    businessDTO.setValue(MdAttributeLongInfo.NAME, "queryLong");
    businessDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A long to query.");
    businessDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(businessDTO);

    businessDTO = clientRequest.newBusiness(MdAttributeTimeInfo.CLASS);
    businessDTO.setValue(MdAttributeTimeInfo.NAME, "queryTime");
    businessDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A time to query.");
    businessDTO.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(businessDTO);

    businessDTO = clientRequest.newBusiness(MdAttributeBooleanInfo.CLASS);
    businessDTO.setValue(MdAttributeBooleanInfo.NAME, "queryBoolean");
    businessDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A boolean to query.");
    businessDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClassId);
    clientRequest.createBusiness(businessDTO);
    // END add attributes to query
  }

  public static void finalizeSetup()
  {
    String mdStateType = stateMachineType;

    state1 = clientRequest.newBusiness(mdStateType);
    state1.setValue(StateMasterDAOIF.STATE_NAME, "FirstState");
    state1.clearEnum(StateMasterDAOIF.ENTRY_STATE);
    state1.addEnumItem(StateMasterDAOIF.ENTRY_STATE, StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.toString());
    clientRequest.createBusiness(state1);

    // state2 = clientRequest.newBusiness(mdStateType);
    // state2.setValue(StateMasterIF.STATE_NAME, "SecondState");
    // clientRequest.createBusiness(state2);

    // String transitionRelationship =
    // "state."+mdStateType+".ChildStateMachine_T";
    // transition = clientRequest.addChild(state1.getId(), state2.getId(),
    // transitionRelationship);
    // transition.setValue(StateMasterIF.TRANSITION_NAME, "Jump1");
    // clientRequest.createRelationship(transition);
    //
    // transition2 = clientRequest.addChild(state1.getId(), state2.getId(),
    // transitionRelationship);
    // transition2.setValue(StateMasterIF.TRANSITION_NAME, "Jump2");
    // clientRequest.createRelationship(transition2);

    suitEnumNames = new LinkedList<String>();
    suitEnumDisplayLabels = new LinkedList<String>();

    hearts = clientRequest.newBusiness(suitMasterType);
    hearts.setValue("refChar", "Some other string");
    hearts.setValue(EnumerationMasterInfo.NAME, "HEARTS");
    hearts.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    clientRequest.createBusiness(hearts);
    suitEnumNames.add(hearts.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(hearts.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    clubs = clientRequest.newBusiness(suitMasterType);
    clubs.setValue("refChar", "Some other string: Clubs");
    clubs.setValue(EnumerationMasterInfo.NAME, "CLUBS");
    clubs.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    clientRequest.createBusiness(clubs);
    suitEnumNames.add(clubs.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(clubs.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    spades = clientRequest.newBusiness(suitMasterType);
    spades.setValue("refChar", "Some other string: Spades");
    spades.setValue(EnumerationMasterInfo.NAME, "SPADES");
    spades.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Spades");
    clientRequest.createBusiness(spades);
    suitEnumNames.add(spades.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(spades.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    diamonds = clientRequest.newBusiness(suitMasterType);
    diamonds.setValue("refChar", "Some other string: Diamonds");
    diamonds.setValue(EnumerationMasterInfo.NAME, "DIAMONDS");
    diamonds.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    clientRequest.createBusiness(diamonds);
    suitEnumNames.add(diamonds.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(diamonds.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    suits.add(hearts);
    suits.add(clubs);
    suits.add(spades);
    suits.add(diamonds);

    MutableDTO _mdAttributeEnumerationDTO = clientRequest.get(mdAttributeEnumerationDTO.getId());
    clientRequest.lock((ElementDTO) _mdAttributeEnumerationDTO);
    _mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, diamonds.getId());
    clientRequest.update(_mdAttributeEnumerationDTO);
  }

  public static void classTearDown()
  {
    clientRequest.delete(mdView.getId());

    clientRequest.delete(mdRelationship.getId());

    clientRequest.delete(childMdBusiness.getId());

    clientRequest.delete(parentMdBusiness.getId());

    clientRequest.delete(structMdBusiness.getId());

    clientRequest.delete(refClass.getId());

    clientRequest.delete(termClass.getId());

    clientRequest.delete(suitMaster.getId());

    clientRequest.delete(vault.getId());

    state1 = null;

    suits.clear();

    clientRequest.delete(littleBillyTables.getId());

    clientRequest.delete(tommyUser.getId());

    clientRequest.delete(testUserMd.getId());

    systemSession.logout();
  }

  public void createQueryInstances()
  {
    // parent instance
    parentInstance = this.initParentInstance();
    parentInstance.setValue("queryChar", "controller query");
    parentInstance.setValue("queryLong", "100");
    parentInstance.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    parentInstance.setValue("queryTime", "12:00:00");
    clientRequest.createBusiness(parentInstance);

    // child instance
    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    // relationship instance
    relInstance = clientRequest.addParent(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    relInstance.setValue("relChar", "controller query");
    relInstance.setValue("relLong", "100");
    relInstance.setValue("relBoolean", MdAttributeBooleanInfo.TRUE);
    relInstance.setValue("relTime", "12:00:00");

    clientRequest.createRelationship(relInstance);
  }

  public void destroyQueryInstances()
  {
    if (parentInstance != null)
    {
      clientRequest.lock(parentInstance);
      clientRequest.delete(parentInstance.getId());
    }

    if (childInstance != null)
    {
      clientRequest.lock(childInstance);
    }

    parentInstance = null;
    childInstance = null;
  }

  private BusinessDTO createParentInstance(ClientRequestIF _clientRequestIF)
  {
    BusinessDTO _parentInstance = this.initParentInstance();
    _clientRequestIF.createBusiness(_parentInstance);

    return _parentInstance;
  }

  private BusinessDTO initParentInstance()
  {
    BusinessDTO _parentInstance = clientRequest.newBusiness(parentMdBusinessType);
    _parentInstance.setValue("aCharacter", IDGenerator.nextID());
    _parentInstance.setValue("aHiddenCharacter", IDGenerator.nextID());
    _parentInstance.setValue("aCharacterGroupIndex1", IDGenerator.nextID());
    _parentInstance.setValue("aCharacterGroupIndex2", IDGenerator.nextID());

    return _parentInstance;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(new EntityMasterTestSetup(AdapterTest.suite()));
  }

  public void testUserRoleMap()
  {
    clientRequest.assignMember(tommyUser.getId(), RoleDAOIF.ROLE_ADMIN_ROLE, RoleDAOIF.DEVELOPER_ROLE);

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    Map<String, String> roleMap = tommyRequest.getSessionUserRoles();

    assertEquals(roleMap.size(), 2);
    assertTrue(roleMap.containsKey(RoleDAOIF.ROLE_ADMIN_ROLE));
    assertTrue(roleMap.containsKey(RoleDAOIF.DEVELOPER_ROLE));

    tommySession.logout();

    clientRequest.removeMember(tommyUser.getId(), RoleDAOIF.ROLE_ADMIN_ROLE, RoleDAOIF.DEVELOPER_ROLE);
  }

  public void testGetAllBusinessDTOsForEnumItems()
  {
    Class<?> masterEnumClass = LoaderDecorator.load(suitMasterType + EntityDTOBaseGenerator.DTO_SUFFIX);

    List<? extends BusinessDTO> enumItems = clientRequest.getAllEnumerations(suitMdEnumerationType);

    assertEquals("Did not return the correct number of enumeration items.", suitEnumNames.size(), enumItems.size());

    for (BusinessDTO enumItem : enumItems)
    {
      if (!masterEnumClass.isInstance(enumItem))
      {
        fail(BusinessDTO.class.getName() + "s representing enumeration items were returned of the wrong type.");
      }
      else
      {
        if (!suitEnumNames.contains(enumItem.getValue(EnumerationMasterInfo.NAME)))
        {
          fail("Did not return all of the enumerations for the given [" + MdEnumerationInfo.CLASS + "]");
        }
        else
        {
          int index = suitEnumNames.indexOf(enumItem.getValue(EnumerationMasterInfo.NAME));
          assertEquals("Returned the wrong enumeration name display label.", suitEnumDisplayLabels.get(index), enumItem.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
        }
      }
    }

  }

  public void testGetBusinessDTOsForEnumItems()
  {
    Class<?> masterEnumClass = LoaderDecorator.load(suitMasterType + EntityDTOBaseGenerator.DTO_SUFFIX);

    List<String> requestedNamesList = new ArrayList<String>();

    // Get all but one of the enum items
    String[] requestedNames = new String[suitEnumNames.size() - 1];
    for (int i = 0; i < requestedNames.length; i++)
    {
      requestedNames[i] = suitEnumNames.get(i);
      requestedNamesList.add(suitEnumNames.get(i));
    }

    List<? extends BusinessDTO> enumItems = clientRequest.getEnumerations(suitMdEnumerationType, requestedNames);

    assertEquals("Did not return the correct number of enumeration items.", requestedNames.length, enumItems.size());

    for (BusinessDTO enumItem : enumItems)
    {
      if (!masterEnumClass.isInstance(enumItem))
      {
        fail(BusinessDTO.class.getName() + "s representing enumeration items were returned of the wrong type.");
      }
      else
      {
        if (!requestedNamesList.contains(enumItem.getValue(EnumerationMasterInfo.NAME)))
        {
          fail("Did not return all of the enumerations for the given [" + MdEnumerationInfo.CLASS + "]");
        }
      }
    }

  }

  public void testGetInvalidEnumeration()
  {
    List<String> requestedNamesList = new ArrayList<String>();

    // Get all but one of the enum items
    String[] requestedNames = new String[suitEnumNames.size()];
    for (int i = 0; i < requestedNames.length; i++)
    {
      requestedNames[i] = suitEnumNames.get(i);
      requestedNamesList.add(suitEnumNames.get(i));
    }

    String invalidEnumName = "INVALID_ENUM";

    requestedNames[requestedNames.length - 1] = invalidEnumName;

    try
    {
      clientRequest.getEnumerations(suitMdEnumerationType, requestedNames);
    }
    catch (InvalidEnumerationNameDTO e)
    {
      // this is expected
      String expectedMessage = "The enummeration name [" + invalidEnumName + "] is not valid for enumeration [" + suitMdEnumeration.getStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE) + "].";
      assertEquals("Wrong localized message.", expectedMessage, e.getMessage());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testEmptyValueProblem()
  {
    BusinessDTO parent = clientRequest.newBusiness(parentMdBusinessType);

    try
    {
      clientRequest.update(parent);
      fail("Able to apply an object without supplying values to required attributes");
    }
    catch (ProblemExceptionDTO e)
    {
      boolean aCharacterProblem = false;
      boolean aHiddenCharacterProblem = false;
      boolean groupIndex1Problem = false;
      boolean groupIndex2Problem = false;

      ProblemExceptionDTO problemExceptionDTO = (ProblemExceptionDTO) e;

      List<? extends ProblemDTOIF> problemList = problemExceptionDTO.getProblems();
      // "A simple test parent"
      // parentMdBusinessType
      for (ProblemDTOIF problem : problemList)
      {
        if (problem instanceof EmptyValueProblemDTO)
        {
          EmptyValueProblemDTO emptyValueProblemDTO = (EmptyValueProblemDTO) problem;
          if (emptyValueProblemDTO.getAttributeName().equals("aCharacter"))
          {
            aCharacterProblem = true;
            assertEquals("A Character", emptyValueProblemDTO.getAttributeDisplayLabel());
            assertEquals(parentMdBusinessType, emptyValueProblemDTO.getDefiningType());
            assertEquals("A simple test parent", emptyValueProblemDTO.getDefiningTypeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aHiddenCharacter"))
          {
            aHiddenCharacterProblem = true;
            assertEquals("A Hidden Character", emptyValueProblemDTO.getAttributeDisplayLabel());
            assertEquals(parentMdBusinessType, emptyValueProblemDTO.getDefiningType());
            assertEquals("A simple test parent", emptyValueProblemDTO.getDefiningTypeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aCharacterGroupIndex1"))
          {
            groupIndex1Problem = true;
            assertEquals("A Character Group Index 1", emptyValueProblemDTO.getAttributeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aCharacterGroupIndex2"))
          {
            groupIndex2Problem = true;
            assertEquals("A Character Group Index 2", emptyValueProblemDTO.getAttributeDisplayLabel());
          }
        }
      }

      if (problemList.size() == 4 && aCharacterProblem && aHiddenCharacterProblem && groupIndex1Problem && groupIndex2Problem)
      {
        // we want to land here
      }
      else
      {
        fail("The wrong number and/or types of problems were thrown.");
      }

      // Make sure the metadata is able to identify the attribute that violates
      // the constraint
    }
    finally
    {
      if (!parent.isNewInstance())
      {
        clientRequest.delete(parent.getId());
      }
    }
  }

  public void testDuplicateColumnDataInDatabase()
  {
    BusinessDTO parent1 = createParentInstance(clientRequest);
    BusinessDTO parent2 = initParentInstance();
    // copy the value from the other object. This should create a uniquness
    // violation
    parent2.setValue("aCharacter", parent1.getValue("aCharacter"));

    try
    {
      clientRequest.update(parent2);
      fail("Able to violate a uniqueness constraint defined on an attribute.");
    }
    catch (DuplicateDataExceptionDTO e)
    {
      // we want to land here
      // Make sure the metadata is able to identify the attribute that violates
      // the constraint
      // if (!e.getDeveloperMessage().contains("[aCharacter]")) {
      // fail("Expected to find the string \"[aCharacter]\" within the exception's developer message: ["
      // + e.getDeveloperMessage() + "].");
      // }
      String check = "[A Character]";
      if (!e.getMessage().contains(check))
      {
        fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
    }
    finally
    {
      clientRequest.delete(parent1.getId());
      if (!parent2.isNewInstance())
      {
        clientRequest.delete(parent2.getId());
      }
    }
  }

  public void testDuplicateGroupColumnIndexInDatabase()
  {
    BusinessDTO parent1 = createParentInstance(clientRequest);
    BusinessDTO parent2 = initParentInstance();
    // copy the value from the other object. This should create a uniquness
    // violation
    parent2.setValue("aCharacterGroupIndex1", parent1.getValue("aCharacterGroupIndex1"));
    parent2.setValue("aCharacterGroupIndex2", parent1.getValue("aCharacterGroupIndex2"));

    try
    {
      clientRequest.update(parent2);
      fail("Able to violate a uniqueness constraint defined on an attribute.");
    }
    catch (DuplicateDataExceptionDTO e)
    {
      // we want to land here
      // Make sure the metadata is able to identify the attribute that violates
      // the constraint
      // assert ( e.getDeveloperMessage().contains("[aCharacterGroupIndex1]") );
      // assert ( e.getDeveloperMessage().contains("[aCharacterGroupIndex2]") );

      String check = "[A Character Group Index 1]";
      if (!e.getMessage().contains(check))
      {
        fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
      check = "[A Character Group Index 2]";
      if (!e.getMessage().contains(check))
      {
        fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
    }
    finally
    {
      clientRequest.delete(parent1.getId());
      if (!parent2.isNewInstance())
      {
        clientRequest.delete(parent2.getId());
      }
    }

  }

  public void testGroovyObjectQuery()
  {
    createQueryInstances();
    try
    {
      String groovyString = "def relTest = new " + mdRelationshipType + "Query(f);" + "q.WHERE q.testChild(relTest);";

      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      queryDTO.setGroovyQuery(groovyString);

      queryDTO = (BusinessQueryDTO) clientRequest.groovyObjectQuery(queryDTO);

      for (BusinessDTO businessDTO : queryDTO.getResultSet())
      {
        assertEquals("controller query", businessDTO.getValue("queryChar"));
      }

    }
    finally
    {
      destroyQueryInstances();
    }
  }

  public void testJSONObjectQuery() throws JSONException
  {
    createQueryInstances();

    try
    {
      String charVal = "Jason writes JSON";

      clientRequest.lock(parentInstance);
      parentInstance.setValue("aCharacter", charVal);
      clientRequest.update(parentInstance);

      JSONArray arr = new JSONArray();
      JSONObject obj = new JSONObject();

      obj.put(AdminConstants.JSON_QUERY_OPERATOR, ConditionOperator.AND.name());
      obj.put(AdminConstants.JSON_QUERY_ATTRIBUTE, "aCharacter");
      obj.put(AdminConstants.JSON_QUERY_CONDITION, QueryConditions.EQUALS);
      obj.put(AdminConstants.JSON_QUERY_VALUE, charVal);

      arr.put(obj);

      String json = arr.toString();

      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);
      queryDTO.setJsonQuery(json);

      BusinessQueryDTO returnQuery = clientRequest.queryBusinesses(queryDTO);

      List<? extends BusinessDTO> results = returnQuery.getResultSet();
      assertEquals(results.size(), 1);

      BusinessDTO result = results.get(0);
      assertEquals(charVal, result.getValue("aCharacter"));
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  public void testGroovyValueQuery()
  {
    createQueryInstances();
    try
    {
      String groovyString = "def parentTest = new " + pack + ".ParentTestQuery(f);" + "def relTest = new " + mdRelationshipType + "Query(f);" + "q.SELECT parentTest.getQueryChar(\"queryChar\"), relTest.getRelLong(\"relLong\");" + "q.WHERE parentTest.testChild(relTest);";

      ValueQueryDTO valueQueryDTO = new ValueQueryDTO(groovyString);

      valueQueryDTO = clientRequest.groovyValueQuery(valueQueryDTO);

      for (ValueObjectDTO valueObjectDTO : valueQueryDTO.getResultSet())
      {
        assertEquals("controller query", valueObjectDTO.getValue("queryChar"));
        assertEquals("100", valueObjectDTO.getValue("relLong"));
      }

    }
    finally
    {
      destroyQueryInstances();
    }
  }

  public void testGetChildren()
  {
    BusinessDTO child1 = null;
    BusinessDTO child2 = null;
    BusinessDTO child3 = null;
    BusinessDTO parent = null;

    try
    {
      // child 1
      child1 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child1);

      // child 2
      child2 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child2);

      // child 3
      child3 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child3);

      // parent
      parent = this.createParentInstance(clientRequest);

      // add the children to the parent
      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getId(), child1.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getId(), child2.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getId(), child3.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<BusinessDTO> definedObjects = new LinkedList<BusinessDTO>();
      definedObjects.add(child1);
      definedObjects.add(child2);
      definedObjects.add(child3);

      // retrieve the children and check for id matches
      List<? extends BusinessDTO> returnedObjects = (List<? extends BusinessDTO>) clientRequest.getChildren(parent.getId(), mdRelationshipType);
      for (BusinessDTO defRel : definedObjects)
      {
        boolean foundMatch = false;
        for (BusinessDTO returnRel : returnedObjects)
        {
          // look for a relationship match
          if (defRel.getId().equals(returnRel.getId()))
          {
            foundMatch = true;
          }
        }
        if (!foundMatch)
        {
          fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getId());

      if (child1 != null)
        clientRequest.delete(child1.getId());

      if (child2 != null)
        clientRequest.delete(child2.getId());

      if (child3 != null)
        clientRequest.delete(child3.getId());
    }
  }

  public void testGetParents()
  {
    BusinessDTO parent1 = null;
    BusinessDTO parent2 = null;
    BusinessDTO parent3 = null;
    BusinessDTO child = null;

    try
    {
      // parent 1
      parent1 = this.createParentInstance(clientRequest);

      // parent 2
      parent2 = this.createParentInstance(clientRequest);

      // parent 3
      parent3 = this.createParentInstance(clientRequest);

      // child
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      // add the children to the parent
      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent3.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<BusinessDTO> definedObjects = new LinkedList<BusinessDTO>();
      definedObjects.add(parent1);
      definedObjects.add(parent2);
      definedObjects.add(parent3);

      // retrieve the children and check for id matches
      List<? extends BusinessDTO> returnedObjects = (List<? extends BusinessDTO>) clientRequest.getParents(child.getId(), mdRelationshipType);
      for (BusinessDTO defRel : definedObjects)
      {
        boolean foundMatch = false;
        for (BusinessDTO returnRel : returnedObjects)
        {
          // look for a relationship match
          if (defRel.getId().equals(returnRel.getId()))
          {
            foundMatch = true;
          }
        }
        if (!foundMatch)
        {
          fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getId());

      if (parent2 != null)
        clientRequest.delete(parent2.getId());

      if (parent3 != null)
        clientRequest.delete(parent3.getId());

      if (child != null)
        clientRequest.delete(child.getId());
    }
  }

  /**
   * Creates a few relationships and then gets the children.
   */
  @SuppressWarnings("unchecked")
  public void testGetChildRelationships()
  {
    BusinessDTO child1 = null;
    BusinessDTO child2 = null;
    BusinessDTO child3 = null;
    BusinessDTO parent = null;

    try
    {
      // child 1
      child1 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child1);

      // child 2
      child2 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child2);

      // child 3
      child3 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child3);

      // parent
      parent = this.createParentInstance(clientRequest);

      // add the children to the parent
      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getId(), child1.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getId(), child2.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getId(), child3.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<RelationshipDTO> definedRels = new LinkedList<RelationshipDTO>();
      definedRels.add(relDTO1);
      definedRels.add(relDTO2);
      definedRels.add(relDTO3);

      // retrieve the children and check for id matches
      List<RelationshipDTO> returnedRels = (List<RelationshipDTO>) clientRequest.getChildRelationships(parent.getId(), mdRelationshipType);
      for (RelationshipDTO defRel : definedRels)
      {
        boolean foundMatch = false;
        for (RelationshipDTO returnRel : returnedRels)
        {
          // look for a relationship match
          if (defRel.getId().equals(returnRel.getId()))
          {
            if (defRel.getChildId().equals(returnRel.getChildId()) && defRel.getParentId().equals(returnRel.getParentId()))
            {
              foundMatch = true;
            }
          }
        }
        if (!foundMatch)
        {
          fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getId());

      if (child1 != null)
        clientRequest.delete(child1.getId());

      if (child2 != null)
        clientRequest.delete(child2.getId());

      if (child3 != null)
        clientRequest.delete(child3.getId());
    }
  }

  /**
   * Creates a few relationships and then gets the parents.
   */
  @SuppressWarnings("unchecked")
  public void testGetParentRelationships()
  {
    BusinessDTO parent1 = null;
    BusinessDTO parent2 = null;
    BusinessDTO parent3 = null;
    BusinessDTO child = null;

    try
    {
      // parent 1
      parent1 = this.createParentInstance(clientRequest);

      // parent 2
      parent2 = this.createParentInstance(clientRequest);

      // parent 3
      parent3 = this.createParentInstance(clientRequest);

      // parent
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      // create the relationships

      // add the children to the parent
      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent3.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<RelationshipDTO> definedRels = new LinkedList<RelationshipDTO>();
      definedRels.add(relDTO1);
      definedRels.add(relDTO2);
      definedRels.add(relDTO3);

      // retrieve the children and check for id matches
      List<RelationshipDTO> returnedRels = (List<RelationshipDTO>) clientRequest.getParentRelationships(child.getId(), mdRelationshipType);
      for (RelationshipDTO defRel : definedRels)
      {
        boolean foundMatch = false;
        for (RelationshipDTO returnRel : returnedRels)
        {
          // look for a relationship match
          if (defRel.getId().equals(returnRel.getId()))
          {
            if (defRel.getChildId().equals(returnRel.getChildId()) && defRel.getParentId().equals(returnRel.getParentId()))
            {
              foundMatch = true;
            }
          }
        }
        if (!foundMatch)
        {
          fail("The returned list of RelationshipDTO objects from the facade's getParentRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getId());

      if (parent2 != null)
        clientRequest.delete(parent2.getId());

      if (parent3 != null)
        clientRequest.delete(parent3.getId());

      if (child != null)
        clientRequest.delete(child.getId());
    }
  }

  /**
   * Adds a child to a relationship.
   */
  public void testAddChild()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;
    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getId());

      if (parent != null)
        clientRequest.delete(parent.getId());
    }
  }

  /**
   * Deletes a child in a relationship.
   */
  public void testDeleteChild()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      // delete the child
      clientRequest.lock(relDTO);
      clientRequest.deleteChild(relDTO.getId());
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getId());

      if (parent != null)
        clientRequest.delete(parent.getId());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDeleteChildren()
  {
    BusinessDTO child1 = null;
    BusinessDTO child2 = null;
    BusinessDTO child3 = null;
    BusinessDTO parent = null;

    try
    {
      parent = this.createParentInstance(clientRequest);

      // create several children
      child1 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child1);

      child2 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child2);

      child3 = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child3);

      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getId(), child1.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getId(), child2.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getId(), child3.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      // delete the children
      clientRequest.deleteChildren(parent.getId(), mdRelationshipType);

      // make sure the children were deleted
      List<RelationshipDTO> retrievedChildren = (List<RelationshipDTO>) clientRequest.getChildRelationships(parent.getId(), mdRelationshipType);

      // there should not be any children
      if (retrievedChildren.size() != 0)
      {
        fail("A facade call to deleteChildren() failed to delete all children.");
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getId());

      if (child1 != null)
        clientRequest.delete(child1.getId());

      if (child2 != null)
        clientRequest.delete(child2.getId());

      if (child3 != null)
        clientRequest.delete(child3.getId());
    }
  }

  @SuppressWarnings("unchecked")
  public void testDeleteParents()
  {
    BusinessDTO parent1 = null;
    BusinessDTO parent2 = null;
    BusinessDTO parent3 = null;
    BusinessDTO child = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      // create several parents
      parent1 = this.createParentInstance(clientRequest);

      parent2 = this.createParentInstance(clientRequest);

      parent3 = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent2.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      // delete the parents
      clientRequest.deleteParents(child.getId(), mdRelationshipType);

      // make sure the parents were deleted
      List<RelationshipDTO> retrievedParents = (List<RelationshipDTO>) clientRequest.getParentRelationships(child.getId(), mdRelationshipType);

      // there should not be any parents
      if (retrievedParents.size() != 0)
      {
        fail("A facade call to deleteParents() failed to delete all parents.");
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getId());

      if (parent2 != null)
        clientRequest.delete(parent2.getId());

      if (parent3 != null)
        clientRequest.delete(parent3.getId());

      if (child != null)
        clientRequest.delete(child.getId());
    }
  }

  public void testDeleteParent()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      // delete the parent
      clientRequest.lock(relDTO);
      clientRequest.deleteParent(relDTO.getId());
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getId());

      if (parent != null)
        clientRequest.delete(parent.getId());
    }
  }

  /**
   * Adds an invalid child to a relationship.
   */
  public void testAddChildInvalid()
  {
    BusinessDTO child = null;
    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relDTO = clientRequest.addChild(child.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      fail("An invalid relationship occured through Facade.addChild()");
    }
    catch (UnexpectedTypeExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (child != null)
      {
        clientRequest.delete(child.getId());
      }
    }
  }

  /**
   * Adds a valid parent to a relationship.
   */
  public void testAddParent()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getId());

      if (parent != null)
        clientRequest.delete(parent.getId());
    }
  }

  /**
   * Adds an invalid parent to a relationship.
   */
  public void testAddParentInvalid()
  {
    BusinessDTO parent = null;
    try
    {
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getId(), parent.getId(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      fail("An invalid relationship occured through Facade.addParent()");
    }
    catch (UnexpectedTypeExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getId());
    }
  }

  /**
   * Updates an entity through the controller.
   */
  public void testUpdateEntity()
  {
    // attributes for the new business object
    BusinessDTO testDTO = null;

    try
    {
      // create the entity
      testDTO = this.createParentInstance(clientRequest);

      clientRequest.lock(testDTO);

      // change a value and update the entity
      testDTO.setValue("aCharacter", "hello");
      testDTO.setValue("aText", "purple monkey dishwasher");
      testDTO.setValue("aClob", "a very large purple monkey dishwasher");
      testDTO.setValue("aFloat", "12.34");
      testDTO.setValue("aBoolean", MdAttributeBooleanInfo.TRUE);
      testDTO.setValue("anInteger", "976");

      clientRequest.update(testDTO);

      assertEquals(testDTO.getValue("aCharacter"), "hello");
      assertEquals(testDTO.getValue("aText"), "purple monkey dishwasher");
      assertEquals(testDTO.getValue("aClob"), "a very large purple monkey dishwasher");
      assertEquals(testDTO.getValue("aFloat"), "12.34");
      assertEquals(testDTO.getValue("aBoolean"), MdAttributeBooleanInfo.TRUE);
      assertEquals(testDTO.getValue("anInteger"), "976");
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getId());
      }
    }
  }

  /**
   * Updates an entity through the controller.
   */
  public void testImmutableAttributeEntity()
  {
    // attributes for the new business object
    BusinessDTO testDTO = null;

    try
    {
      // create the entity
      testDTO = this.createParentInstance(clientRequest);

      clientRequest.lock(testDTO);

      // change a value and update the entity
      testDTO.setValue("anImmutableBoolean", MdAttributeBooleanInfo.FALSE);

      clientRequest.update(testDTO);
    }
    catch (ProblemExceptionDTO e)
    {
      List<? extends ProblemDTOIF> problemList = e.getProblems();

      if (problemList.size() != 1)
      {
        fail("Wrong number of problems returned");
      }
      else
      {
        ProblemDTOIF problemDTOIF = problemList.get(0);

        if (! ( problemDTOIF instanceof ImmutableAttributeProblemDTO ))
        {
          fail("Wrong problem class returned for an immutable attribute violation.");
        }
      }
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getId());
      }
    }
  }

  /**
   * Updates an entity in an invalid fashion by setting an attribute incorrectly
   */
  public void testUpateEntityInvalid()
  {
    BusinessDTO testDTO = null;

    try
    {
      testDTO = this.createParentInstance(clientRequest);

      // change a value and update the entity
      clientRequest.lock(testDTO);

      // anInteger should reject negative numbers
      testDTO.setValue("anInteger", "-100");
      clientRequest.update(testDTO);

      fail("The controller allowed for an erroneous attribute to be set.");
    }
    catch (ProblemExceptionDTO e)
    {
      for (ProblemDTOIF p : e.getProblems())
      {
        if (p instanceof AttributeProblemDTO)
        {
          return;
        }
      }
      fail("Did not find [" + AttributeProblemDTO.CLASS + "] in the problem list.");
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getId());
      }
    }
  }

  /**
   * Deletes an entity.
   */
  public void testDeleteEntity()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      clientRequest.lock(businessDTO);
      clientRequest.delete(businessDTO.getId());

      // Now try to get the object that was created. If it was deleted, it
      // should error out.
      clientRequest.get(businessDTO.getId());
      fail("An entity was not correctly deleted");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tries to double-delete an entity, which should fail.
   */
  public void testDeleteEntityInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.delete("999999999999999999999999999999999-this.class.should.not.exist.Blah");
      fail("An entity was able to deleted that doesn't exist.");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Creates a valid instance of a type.
   */
  public void testNewInstance()
  {
    BusinessDTO businessDTO = null;

    try
    {
      businessDTO = clientRequest.newBusiness(parentMdBusinessType);

      if (!businessDTO.getType().equals(parentMdBusinessType))
      {
        fail("A new instance type did not match its defining entity.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Attempts to create an invalid instance of a fake type.
   */
  public void testNewInstanceInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.newBusiness("this.class.should.not.exist.Blah");
      fail("A new instance was created that doesn't exist.");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstances()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertTrue("The [getAllInstances] method should return one or more results", resultSet.size() >= 1);
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }
    }
  }

  /**
   * Ensures that the id changes between a new <code>BusinessDTO</code> and when
   * the DTO is applied
   */
  public void testSameId()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.initParentInstance();
      String id = businessDTO.getId();

      clientRequest.createBusiness(businessDTO);

      assertFalse("ID of a new " + BusinessDTO.class.getName() + " did not change when it was applied.", id.equals(businessDTO.getId()));
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstancesEmpty()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertEquals("The [getAllInstances] method should have no results", 0, resultSet.size());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstancesNullSortAttribute()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, null, false, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertEquals("The [getAllInstances] method should have no results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstancesNegativePageSize()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", false, -1, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertEquals("The [getAllInstances] method should have no results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstancesNegativePageNumber()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", false, 0, -1);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertEquals("The [getAllInstances] method should have all of the results results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  public void testGetAllInstancesInvalidPageNumber()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 2, 10);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      assertEquals("The [getAllInstances] method should have no results for an out of range page", 0, resultSet.size());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }
    }
  }

  /**
   * Gets a valid instance.
   */
  public void testInstance()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      BusinessDTO retrieved = (BusinessDTO) clientRequest.get(businessDTO.getId());

      assertEquals(businessDTO.getId(), retrieved.getId());

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getId());
      }
    }
  }

  /**
   * Attempts to grab an invalid instance.
   */
  public void testInstanceInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.get("");
      fail("An instance was retrieved that doesn't exist.");
    }
    catch (ProgrammingErrorExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  /**
   * Tests a valid login through the controller.
   */
  public void testLogin()
  {
    BusinessDTO user = null;
    try
    {
      user = clientRequest.newBusiness(testUserType);
      user.setValue(UserInfo.USERNAME, "someUser");
      user.setValue(UserInfo.PASSWORD, "somePass");
      clientRequest.createBusiness(user);

      ClientSession someUserSession = this.createSession("someUser", "somePass");
      someUserSession.logout();
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getId());
      }
    }
  }

  /**
   * Tests a valid login through the controller.
   */
  public void testLoginAnonymous()
  {
    ClientSession anonymousSession = null;

    try
    {
      anonymousSession = this.createAnonymousSession();
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (anonymousSession != null)
      {
        anonymousSession.logout();
      }
    }
  }

  /**
   * Tests a valid login through the controller.
   */
  public void testChangeLogin()
  {
    ClientSession anonymousSession = this.createAnonymousSession();
    BusinessDTO user = null;
    try
    {
      user = clientRequest.newBusiness(testUserType);
      user.setValue(UserInfo.USERNAME, "someUser");
      user.setValue(UserInfo.PASSWORD, "somePass");
      clientRequest.createBusiness(user);

      anonymousSession.changeLogin("someUser", "somePass");
      anonymousSession.logout();
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getId());
      }
    }
  }

  public void testGetSessionUser()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(UserInfo.CLASS);
    MdAttributeDAOIF mdAttributeIF = mdBusinessIF.definesAttribute(UserInfo.USERNAME);

    clientRequest.grantTypePermission(tommyUser.getId(), mdBusinessIF.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeIF.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    try
    {
      BusinessDTO userDTO = tommyProxy.getSessionUser();

      assertEquals("Tommy", userDTO.getValue(UserInfo.USERNAME));
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getId(), mdBusinessIF.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeIF.getId(), Operation.READ.name());
    }

  }

  /**
   * Log in an invalid user.
   */
  public void testLoginInvalid()
  {
    try
    {
      ClientSession.createUserSession("not_a_user", "not_a_password", new Locale[] { CommonProperties.getDefaultLocale() });
      fail("Server did not throw an exception for invalid username and password.");
    }
    catch (InvalidLoginExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail("Wrong exception for invalid login: " + e.getMessage());
    }
  }

  public void testEnumerationDTO()
  {
    try
    {
      BusinessDTO user = clientRequest.newBusiness(parentMdBusinessType);
      List<? extends BusinessDTO> suit = user.getEnumValues("anEnum");

      // make sure there is only one Locale
      assertEquals(suit.size(), 1);

      BusinessDTO locale = suit.get(0);

      // test the values on the locale. English is the default.
      assertEquals(locale.getId(), diamonds.getId());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  public void testNoAccessorConversion()
  {
    BusinessDTO parent = null;

    try
    {
      parent = this.createParentInstance(clientRequest);
      String value = parent.getValue("aHiddenCharacter");

      assertTrue(value != null && !value.equals(""));
    }
    finally
    {
      if (parent != null)
      {
        clientRequest.delete(parent.getId());
      }
    }
  }

  public void testBlob()
  {
    BusinessDTO instance = null;

    try
    {
      // create a new instance of the type
      instance = this.initParentInstance();
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      instance.setBlob("aBlob", value);

      // make sure the intial bytes are stored correctly.
      byte[] retVal = instance.getBlob("aBlob");
      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != retVal[i])
        {
          fail("A new instance did not store blob bytes correctly.");
        }
      }

      // create the instance and check the bytes
      clientRequest.createBusiness(instance);
      retVal = instance.getBlob("aBlob");
      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != retVal[i])
        {
          fail("An updated instance did not store the blob bytes correctly.");
        }
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (instance != null && !instance.isNewInstance())
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getId());
      }
    }
  }

  public void testAttributeMultiReference()
  {
    String attributeName = "aMultiReference";

    BusinessDTO term = clientRequest.newBusiness(termType);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = this.initParentInstance();
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getId());

        List<String> results = test.getMultiItems(attributeName);

        assertEquals(1, results.size());
        assertEquals(term.getId(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  @SuppressWarnings("unchecked")
  public void testAttributeMultiReferenceGeneration() throws Exception
  {
    BusinessDTO term = clientRequest.newBusiness(termType);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    clientRequest.createBusiness(term);

    try
    {
      BusinessDTO instance = this.initParentInstance();
      instance.getClass().getMethod("clearAMultiReference").invoke(instance);
      instance.getClass().getMethod("addAMultiReference", term.getClass()).invoke(instance, term);
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getId());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiReference").invoke(test);

        assertEquals(1, results.size());
        assertEquals(term.getId(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testAttributeMultiTerm()
  {
    String attributeName = "aMultiTerm";

    BusinessDTO term = clientRequest.newBusiness(termType);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    clientRequest.createBusiness(term);

    try
    {

      BusinessDTO instance = this.initParentInstance();
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getId());
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getId());

        List<String> results = test.getMultiItems(attributeName);

        assertEquals(1, results.size());
        assertEquals(term.getId(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  @SuppressWarnings("unchecked")
  public void testAttributeMultiTermGeneration() throws Exception
  {
    BusinessDTO term = clientRequest.newBusiness(termType);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    clientRequest.createBusiness(term);

    try
    {
      BusinessDTO instance = this.initParentInstance();
      instance.getClass().getMethod("clearAMultiTerm").invoke(instance);
      instance.getClass().getMethod("addAMultiTerm", term.getClass()).invoke(instance, term);
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getId());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiTerm").invoke(test);

        assertEquals(1, results.size());
        assertEquals(term.getId(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getId());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getId());
    }
  }

  public void testStructDTO()
  {
    BusinessDTO user = null;

    try
    {
      user = (BusinessDTO) clientRequest.newBusiness(testUserType);
      user.setValue(UserInfo.USERNAME, "Justin");
      user.setValue(UserInfo.PASSWORD, "Butters");

      AttributeStructDTO phone = ComponentDTOFacade.getAttributeStructDTO(user, "phoneNumber");
      phone.setValue(PhoneNumberInfo.AREACODE, "303");
      phone.setValue(PhoneNumberInfo.PREFIX, "979");
      phone.setValue(PhoneNumberInfo.SUFFIX, "8874");

      // create the user
      clientRequest.createBusiness(user);
      AttributeStructDTO createdPhone = ComponentDTOFacade.getAttributeStructDTO(user, "phoneNumber");
      if (!phone.getValue(PhoneNumberInfo.AREACODE).equals(createdPhone.getValue(PhoneNumberInfo.AREACODE)) || !phone.getValue(PhoneNumberInfo.PREFIX).equals(createdPhone.getValue(PhoneNumberInfo.PREFIX)) || !phone.getValue(PhoneNumberInfo.SUFFIX).equals(createdPhone.getValue(PhoneNumberInfo.SUFFIX)))
      {
        fail("The values for a created struct do not match the set values.");
      }

      // update the user
      clientRequest.lock(user);
      createdPhone = ComponentDTOFacade.getAttributeStructDTO(user, "phoneNumber");
      createdPhone.setValue(PhoneNumberInfo.AREACODE, "720");
      createdPhone.setValue(PhoneNumberInfo.PREFIX, "363");
      createdPhone.setValue(PhoneNumberInfo.SUFFIX, "7744");

      clientRequest.update(user);
      AttributeStructDTO updatedPhone = ComponentDTOFacade.getAttributeStructDTO(user, "phoneNumber");
      if (!createdPhone.getValue(PhoneNumberInfo.AREACODE).equals(updatedPhone.getValue(PhoneNumberInfo.AREACODE)) || !createdPhone.getValue(PhoneNumberInfo.PREFIX).equals(updatedPhone.getValue(PhoneNumberInfo.PREFIX)) || !createdPhone.getValue(PhoneNumberInfo.SUFFIX).equals(updatedPhone.getValue(PhoneNumberInfo.SUFFIX)))
      {
        fail("The values for an updated struct do not match the set values.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getId());
      }
    }
  }

  public void testTypeReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getId(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_CHILD.name());
    }
  }

  public void testTypeReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getId(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testInvalidTypeReadChildRelationshipsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getId(), mdRelationshipType);
      fail("Able to read child relationships without READ_CHILD permissions.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());
    }
  }

  public void testTypeReadChildrenPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getId(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_CHILD.name());
    }
  }

  public void testTypeReadChildrenPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getId(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testInvalidTypeReadChildrenPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getId(), mdRelationshipType);
      fail("Able to read child relationships without READ_CHILD permissions.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());
    }
  }

  public void testTypeReadParentRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testTypeReadParentRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testInvalidTypeReadParentRelationshipsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(parentInstance.getId(), mdRelationshipType);
      fail("Able to read parent relationships without READ_PARENT permissions.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());
    }
  }

  public void testTypeReadParentsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testTypeReadParentsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testInvalidTypeReadParentsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(parentInstance.getId(), mdRelationshipType);
      fail("Able to read parent relationships without READ_PARENT permissions.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());
    }
  }

  public void testOwnerReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getId(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_CHILD.name());
    }
  }

  public void testInvalidOwnerReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getId(), mdRelationshipType);
      fail("Able to read child relationships without READ_CHILD permissions on owner.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_CHILD.name());
    }
  }

  public void testOwnerReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.ADD_CHILD.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = tommyProxy.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    tommyProxy.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.ADD_CHILD.name());

    try
    {
      tommyProxy.get(relationshipDTO.getId());
    }
    catch (ReadPermissionExceptionDTO e)
    {
      fail("Unable to read child relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testInvalidOwnerReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.ADD_CHILD.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    // Tommy is not the owner of the relationship instance
    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.ADD_CHILD.name());

    try
    {
      tommyProxy.get(relationshipDTO.getId());
      fail("Able to read child relationships with READ permissions on owner, but user is not the owner.");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ.name());
    }
  }

  public void testOwnerReadParentRelationshipsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParentRelationships(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testInvalidOwnerReadParentRelationshipsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParentRelationships(childInstance.getId(), mdRelationshipType);
      fail("Able to read parent relationships without READ_PARENT permissions on owner.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testOwnerReadParentsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParents(childInstance.getId(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testInvalidOwnerReadParentsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getId(), childInstance.getId(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParents(childInstance.getId(), mdRelationshipType);
      fail("Able to read parent relationships without READ_PARENT permissions on owner.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getId());
      clientRequest.delete(parentInstance.getId());
      clientRequest.delete(childInstance.getId());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getId(), Operation.READ_PARENT.name());
    }
  }

  public void testGrantGrantTypePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.GRANT.name());

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.WRITE.name());
      tommyRequest.grantTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getId());

      tommyRequest.revokeTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.WRITE.name());
      tommyRequest.revokeTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.GRANT.name());
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

      if (billySession != null)
      {
        billySession.logout();
      }
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

      tommyRequest.grantTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.WRITE.name());
    }
    catch (GrantTypePermissionExceptionDTO e)
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

      tommyRequest.revokeTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.WRITE.name());
    }
    catch (RevokeTypePermissionExceptionDTO e)
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

  public void testGrantGrantStateAttributePermission()
  {
    BusinessDTO testObject = null;

    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;

    try
    {
      clientRequest.grantTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.grantAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.GRANT.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributeStatePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      billyRequest.lock(testObject);
      testObject.setValue("refChar", "I am all that is man!");
      billyRequest.update(testObject);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getId());

      tommyRequest.revokeAttributeStatePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());
      clientRequest.revokeAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.GRANT.name());

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

      if (billySession != null)
      {
        billySession.logout();
      }
    }
  }

  public void testInvalidGrantGrantStateAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributeStatePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());
    }
    catch (GrantAttributeStatePermissionExceptionDTO e)
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

  public void testInvalidGrantRevokeStateAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeAttributeStatePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());
    }
    catch (RevokeAttributeStatePermissionExceptionDTO e)
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

  public void testGrantGrantAttributePermission()
  {
    BusinessDTO testObject = null;

    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;

    try
    {
      clientRequest.grantTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.GRANT.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      billyRequest.lock(testObject);
      testObject.setValue("refChar", "I am all that is man!");
      billyRequest.update(testObject);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getId());

      tommyRequest.revokeAttributePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());

      clientRequest.revokeTypePermission(littleBillyTables.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.GRANT.name());
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

      if (billySession != null)
      {
        billySession.logout();
      }
    }
  }

  public void testInvalidGrantGrantAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());
    }
    catch (GrantAttributePermissionExceptionDTO e)
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

  public void testInvalidGrantRevokeAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeAttributePermission(littleBillyTables.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());
    }
    catch (RevokeAttributePermissionExceptionDTO e)
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

  public void testGrantGrantStatePermission()
  {
    BusinessDTO testObject = null;

    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;

    try
    {
      clientRequest.grantStatePermission(tommyUser.getId(), state1.getId(), Operation.GRANT.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantStatePermission(littleBillyTables.getId(), state1.getId(), Operation.WRITE.name(), Operation.DELETE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getId());

      tommyRequest.revokeStatePermission(littleBillyTables.getId(), state1.getId(), Operation.WRITE.name(), Operation.DELETE.name());

      clientRequest.revokeStatePermission(tommyUser.getId(), state1.getId(), Operation.GRANT.name());
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

      if (billySession != null)
      {
        billySession.logout();
      }
    }
  }

  public void testInvalidGrantGrantStatePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantStatePermission(littleBillyTables.getId(), state1.getId(), Operation.WRITE.name(), Operation.DELETE.name());
    }
    catch (GrantStatePermissionExceptionDTO e)
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

  public void testInvalidGrantRevokeStatePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeStatePermission(littleBillyTables.getId(), state1.getId(), Operation.WRITE.name(), Operation.DELETE.name());
    }
    catch (RevokeStatePermissionExceptionDTO e)
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

  public void testGrantTypeReadWritePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name());
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.WRITE.name());
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      BusinessDTO businessDTO = (BusinessDTO) tommyProxy.get(testObject.getId());

      // make sure the readable and writable flag is set to true
      if (!businessDTO.isReadable() || !businessDTO.isWritable())
      {
        fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.lock(testObject);
        tommyProxy.delete(testObject.getId());
      }

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypeNoWritePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      BusinessDTO businessDTO = (BusinessDTO) tommyProxy.get(testObject.getId());

      // make sure the readable and writable flag is set to true
      if (businessDTO.isWritable())
      {
        fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getId());
      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypeNoReadPermission_DeveloperMessage()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.assignMember(tommyUser.getId(), RoleDAOIF.DEVELOPER_ROLE);
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.get(testObject.getId());

      fail("Read or Write permission on a type were not properly set in the DTO");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here
      // Make sure there is a developer message
      assertTrue(!e.getDeveloperMessage().equals(""));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getId());
      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());
      clientRequest.removeMember(tommyUser.getId(), RoleDAOIF.DEVELOPER_ROLE);

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypeNoReadPermission_NoDeveloperMessage()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.get(testObject.getId());

      fail("Read or Write permission on a type were not properly set in the DTO");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here
      // Make sure there is no developer message
      assertTrue(e.getDeveloperMessage().equals(""));
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getId());
      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.WRITE.name());
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.DELETE.name());
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

  public void testGrantTypeReadEnumWritePermission()
  {
    BusinessDTO enumItem = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), suitMaster.getId(), Operation.READ.name());

      enumItem = clientRequest.newBusiness(suitMasterType);
      enumItem.setValue("refChar", "some string");
      enumItem.setValue(EnumerationMasterInfo.NAME, "ENUM1");
      enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enum1");
      clientRequest.createBusiness(enumItem);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      tommyProxy.getEnumeration(suitMdEnumerationType, "ENUM1");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      fail("Unable to read an enumeration with adequate read permissions.");
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(enumItem);
      clientRequest.delete(enumItem.getId());
      clientRequest.revokeTypePermission(tommyUser.getId(), suitMaster.getId(), Operation.READ.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypeNoReadEnumWritePermission()
  {
    BusinessDTO enumItem = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      enumItem = clientRequest.newBusiness(suitMasterType);
      enumItem.setValue("refChar", "some string");
      enumItem.setValue(EnumerationMasterInfo.NAME, "ENUM1");
      enumItem.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enum1");
      clientRequest.createBusiness(enumItem);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      tommyProxy.getEnumeration(suitMdEnumerationType, "ENUM1");
      fail("Able to read an enumeration with inadequate read permissions.");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(enumItem);
      clientRequest.delete(enumItem.getId());
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantStatePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantStatePermission(tommyUser.getId(), state1.getId(), Operation.WRITE.name(), Operation.DELETE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      clientRequest.revokeStatePermission(tommyUser.getId(), state1.getId(), Operation.DELETE.name());
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

  public void testGrantAttributePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.WRITE.name());
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

  public void testGrantStateAttributePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.grantAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());
      clientRequest.revokeAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.WRITE.name());
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

  public void testGrantTypePermissionInvalid()
  {
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), "not_a_proper_permission");

      fail("An invalid type of permission was added to a user.");
    }
    catch (InvalidEnumerationNameDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  public void testGrantTypePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      // delete the object
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());
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

  public void testGrantStatePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantStatePermission(tommyUser.getId(), state1.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.revokeStatePermission(tommyUser.getId(), state1.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantAttributePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.READ.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantAttributePermissionsInvalid()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      fail("User was able to update an attribute value without permission.");
    }
    catch (AttributeWritePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Throwable e)
    {
      fail(e.getLocalizedMessage());
    }
    finally
    {
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantStateAttributePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.grantAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.READ.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      clientRequest.revokeTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributeStatePermission(tommyUser.getId(), mdAttributeCharacterDTO_2.getId(), state1.getId(), Operation.READ.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testGrantTypePermissionsInvalid()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.READ.name(), Operation.WRITE.name());

      testObject = this.createParentInstance(clientRequest);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      // delete the object
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getId());

      fail("A user was able to delete an object without permission.");
    }
    catch (DeletePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Throwable e)
    {
      fail(e.getLocalizedMessage());
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.unlock(testObject);
      }

      if (tommySession != null)
      {
        tommySession.logout();
      }

      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getId());
    }
  }

  @SuppressWarnings("unchecked")
  public void testQueryUserExactMatch()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy");

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getId().equals(tommyUser.getId()))
    {
      fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  public void testQueryUserExactMatchComplicated()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy");

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getId().equals(tommyUser.getId()))
    {
      fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  public void testQueryUserWildCard()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.LIKE, "Tom" + QueryConditions.WILDCARD);

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getId().equals(tommyUser.getId()))
    {
      fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesObjectQueryNumber()
  {
    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryLong", QueryConditions.EQUALS, "100");
      List<BusinessDTO> parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("queryLong", QueryConditions.LT, "101");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("queryLong", QueryConditions.GT_EQ, "100");
      queryDTO.addCondition("queryLong", QueryConditions.GT_EQ, "99");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesObjectQueryChar()
  {
    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.EQUALS, "controller query");
      List<BusinessDTO> parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.NOT_EQUALS, "facade query");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testViewObjectQueryChar()
  {
    createQueryInstances();
    try
    {
      ViewQueryDTO queryDTO = (ViewQueryDTO) clientRequest.getQuery(mdViewType);

      // test EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.EQUALS, "controller query");
      List<ViewDTO> parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      ViewDTO instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.NOT_EQUALS, "facade query");
      parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesObjectQueryBoolean()
  {
    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);
      List<BusinessDTO> parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getId().equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testViewObjectQueryBoolean()
  {
    createQueryInstances();
    try
    {
      ViewQueryDTO queryDTO = (ViewQueryDTO) clientRequest.getQuery(mdViewType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);
      List<ViewDTO> parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      ViewDTO instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinessRelationshipQueryMoment()
  {
    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

      // test EQUALS
      queryDTO.addCondition("relTime", QueryConditions.EQUALS, "12:00:00");
      List<RelationshipDTO> rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.LT, "13:00:00");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "12:00:00");
      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "11:59:59");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesRelationshipQueryNumber()
  {
    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

      // test EQUALS
      queryDTO.addCondition("relLong", QueryConditions.EQUALS, "100");
      List<RelationshipDTO> rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relLong", QueryConditions.LT, "101");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relLong", QueryConditions.GT_EQ, "100");
      queryDTO.addCondition("relLong", QueryConditions.NOT_EQUALS, "123321");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesRelationshipQueryChar()
  {
    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

      // test EQUALS
      queryDTO.addCondition("relChar", QueryConditions.EQUALS, "controller query");
      List<RelationshipDTO> rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("relChar", QueryConditions.NOT_EQUALS, "facade query");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesRelationshipQueryBoolean()
  {
    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

      // test EQUALS
      queryDTO.addCondition("relBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);
      List<RelationshipDTO> rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("relBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  public void testBusinesObjectQueryMoment()
  {
    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

      // test EQUALS
      queryDTO.addCondition("relTime", QueryConditions.EQUALS, "12:00:00");
      List<RelationshipDTO> rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.LT, "13:00:00");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "12:00:00");
      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "11:59:59");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getId().equals(relInstance.getId()))
      {
        fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  public void testBusinesObjectQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);

      tommyProxy.queryBusinesses(queryDTO);
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  public void testViewObjectQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewQueryDTO queryDTO = (ViewQueryDTO) clientRequest.getQuery(mdViewType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);

      tommyProxy.queryViews(queryDTO);
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  public void testViewVirtualAttributeReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewDTO testViewDTO = (ViewDTO) tommyProxy.newMutable(mdViewType);
      testViewDTO.setValue("aCharacter", "some value");
      tommyProxy.createSessionComponent(testViewDTO);

      ViewDTO viewDTO = (ViewDTO) tommyProxy.get(testViewDTO.getId());

      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(viewDTO, "aCharacter");

      // make sure the readable and writable flag is set to true
      if (!attributeDTO.isReadable())
      {
        fail("Read permission on a virtual attribute failed.  Permission was set on the concrete attribute.");
      }
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (ProblemExceptionDTO pe)
    {
      List<? extends ProblemDTOIF> problemList = pe.getProblems();

      for (ProblemDTOIF problemDTOIF : problemList)
      {
        fail(problemDTOIF.getMessage());
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getId(), mdAttributeCharacterDTO.getId(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  public void testViewVirtualAttributeN0ReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewDTO testViewDTO = (ViewDTO) tommyProxy.newMutable(mdViewType);
      testViewDTO.setValue("aCharacter", "some value");
      tommyProxy.createSessionComponent(testViewDTO);

      ViewDTO viewDTO = (ViewDTO) tommyProxy.get(testViewDTO.getId());

      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(viewDTO, "aCharacter");

      // make sure the readable and writable flag is set to true
      if (attributeDTO.isReadable())
      {
        fail("Read permission on a virtual attribute failed.  Permission was not set on the concrete attribute but the virtual attribute was readable.");
      }
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (ProblemExceptionDTO pe)
    {
      List<? extends ProblemDTOIF> problemList = pe.getProblems();

      for (ProblemDTOIF problemDTOIF : problemList)
      {
        fail(problemDTOIF.getMessage());
      }
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getId(), mdView.getId(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  public void testBusinesObjectNoQueryReadPermission()
  {
    clientRequest.revokeTypePermission(tommyUser.getId(), parentMdBusiness.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);

      tommyProxy.queryBusinesses(queryDTO);

      fail("Able to perform a query on a type without adequate read permissions.");
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      // This is expected
    }
    finally
    {
      tommySession.logout();
      destroyQueryInstances();
    }
  }

  public void testViewObjectNoQueryReadPermission()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewQueryDTO queryDTO = (ViewQueryDTO) clientRequest.getQuery(mdViewType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);

      tommyProxy.queryViews(queryDTO);

      fail("Able to perform a query on a type without adequate read permissions.");
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      destroyQueryInstances();
    }
  }

  public void testRelationshipQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);
      queryDTO.addCondition("relBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);
      tommyProxy.queryRelationships(queryDTO);
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  public void testRelationshipQueryNoReadPermission()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);
      queryDTO.addCondition("relBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);
      tommyProxy.queryRelationships(queryDTO);
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      destroyQueryInstances();
    }
  }

  // TODO revisit this test method once SSL and other security concerns are
  // worked out.
  // This test method is currently obsolete, in that it expects the server to
  // return the hash value.
  // Due to security concerns, we return an empty string for hash values, so
  // this test will always fail.
  // public void testHashDTO()
  // {
  // try
  // {
  // AttributeHashDTO hashDTO =
  // tommyUser.getAttributeHashDTO(UserInfo.PASSWORD);
  //
  // // manually hash the value here for comparison
  // String manualHash = null;
  // try
  // {
  // MessageDigest digest =
  // MessageDigest.getInstance(hashDTO.getAttributeMdDTO().getEncryptionMethod(),
  // new Sun());
  // digest.update(new String("music").getBytes());
  // manualHash = Base64.encodeToString(digest.digest(), false);
  // }
  // catch (NoSuchAlgorithmException e)
  // {
  // fail(e.getMessage());
  // }
  //
  // // compare the hashes
  // if (hashDTO.encryptionEquals(manualHash, true) != true)
  // {
  // fail("HashDTO did not propery represent a hashed attribute (manual
  // hash).");
  // }
  //
  // if (hashDTO.encryptionEquals("music", false) != true)
  // {
  // fail("HashDTO did not propery represent a hashed attribute (auto
  // hash).");
  // }
  // }
  // catch (Exception e)
  // {
  // fail(e.getMessage());
  // }
  // }

  /**
   * Checks if a DTO can correctly lock an object and have the value of
   * EntityDTO.isLockedByCurrentUser() return true if the object is locked by
   * the current user and false otherwise.
   */
  public void testCurrentUserLockValue()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), childMdBusiness.getId(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.lock(testObject);

      // make sure Tommy has the lock value set to true
      if (!testObject.isLockedByCurrentUser())
      {
        fail("The Tommy user is not marked as locking an object which it did.");
      }

      // now make sure the SYSTEM user has the lock value set to false
      // (since it
      // didn't lock it, Tommy did)
      BusinessDTO temp = (BusinessDTO) clientRequest.get(testObject.getId());
      if (temp.isLockedByCurrentUser())
      {
        fail("The SYSTEM user is marked as locking an object which it did not.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getId());
      }

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);

    assertEquals(parentMdBusiness.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDisplayLabel());
    assertEquals(parentMdBusiness.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDescription());
    assertEquals(parentMdBusiness.getId(), instance.getMd().getId());
  }

  public void testRelationshipMetadata()
  {
    RelationshipDTO instance = (RelationshipDTO) clientRequest.newMutable(mdRelationshipType);

    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDisplayLabel());
    assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDescription());
    assertEquals(mdRelationship.getId(), instance.getMd().getId());

    assertEquals(parentMdBusinessType, instance.getMd().getParentMdBusiness());
    assertEquals(childMdBusinessType, instance.getMd().getChildMdBusiness());
  }

  public void testBooleanMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeBooleanMdDTO md = (AttributeBooleanMdDTO) ComponentDTOFacade.getAttributeDTO(instance, "aBoolean").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBooleanDTO, md);
    assertEquals(mdAttributeBooleanDTO.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getPositiveDisplayLabel());
    assertEquals(mdAttributeBooleanDTO.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getNegativeDisplayLabel());
  }

  public void testBlobMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aBlob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBlobDTO, md);
  }

  public void testReferenceMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeReferenceMdDTO md = ComponentDTOFacade.getAttributeReferenceDTO(instance, "aReference").getAttributeMdDTO();

    checkAttributeMd(mdAttributeReferenceDTO, md);

    assertEquals(md.getReferencedMdBusiness(), refType);
  }

  public void testTermMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeTermMdDTO md = ComponentDTOFacade.getAttributeTermDTO(instance, "aTerm").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTermDTO, md);

    assertEquals(md.getReferencedMdBusiness(), termType);
  }

  public void testMultiReferenceMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMultiReferenceMdDTO md = ComponentDTOFacade.getAttributeMultiReferenceDTO(instance, "aMultiReference").getAttributeMdDTO();

    checkAttributeMd(mdAttributeMultiReferenceDTO, md);

    assertEquals(md.getReferencedMdBusiness(), termType);
  }

  public void testMultiTermMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMultiTermMdDTO md = ComponentDTOFacade.getAttributeMultiTermDTO(instance, "aMultiTerm").getAttributeMdDTO();

    checkAttributeMd(mdAttributeMultiTermDTO, md);

    assertEquals(md.getReferencedMdBusiness(), termType);
  }

  public void testDateTimeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDateTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateTimeDTO, md);
  }

  public void testDateMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDate").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateDTO, md);
  }

  public void testTimeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTimeDTO, md);
  }

  public void testIdMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = instance.getIdMd();

    assertEquals(md.getName(), EntityInfo.ID);
    assertEquals(md.isRequired(), true);
    assertEquals(md.isSystem(), true);
  }

  public void testTypeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = instance.getTypeMd();

    assertEquals(md.getName(), EntityInfo.TYPE);
    assertEquals(md.isRequired(), true);
    assertEquals(md.isSystem(), true);
  }

  public void testIntegerMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "anInteger").getAttributeMdDTO();

    checkAttributeMd(mdAttributeIntegerDTO, md);
  }

  public void testLongMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "aLong").getAttributeMdDTO();

    checkAttributeMd(mdAttributeLongDTO, md);
  }

  public void testDecimalMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDecimal").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDecimalDTO, md);
  }

  public void testDoubleMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDouble").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDoubleDTO, md);
  }

  public void testFloatMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aFloat").getAttributeMdDTO();

    checkAttributeMd(mdAttributeFloatDTO, md);
  }

  public void testTextMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aText").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTextDTO, md);
  }

  public void testClobMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aClob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeClobDTO, md);
  }

  public void testCharacterMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = ComponentDTOFacade.getAttributeCharacterDTO(instance, "aCharacter").getAttributeMdDTO();

    checkAttributeMd(mdAttributeCharacterDTO, md);
    assertEquals(Integer.parseInt(mdAttributeCharacterDTO.getValue(MdAttributeCharacterInfo.SIZE)), md.getSize());
  }

  public void testStructMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeStructMdDTO md = ComponentDTOFacade.getAttributeStructDTO(instance, "aStruct").getAttributeMdDTO();

    checkAttributeMd(mdAttributeStructDTO, md);
    assertEquals(EntityTypes.PHONE_NUMBER.getType(), md.getDefiningMdStruct());
  }

  public void testEnumerationMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEnumerationMdDTO md = ComponentDTOFacade.getAttributeEnumerationDTO(instance, "anEnum").getAttributeMdDTO();

    checkAttributeMd(mdAttributeEnumerationDTO, md);
    assertEquals(Boolean.parseBoolean(mdAttributeEnumerationDTO.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE)), md.selectMultiple());
    assertEquals("anEnum", md.getName());
    assertEquals(suitMdEnumerationType, md.getReferencedMdEnumeration());
    assertEquals(suitMdEnumerationType + TypeGeneratorInfo.DTO_SUFFIX, md.getJavaType().getName());

    // check all enum values
    Map<String, String> enumNameMap = md.getEnumItems();
    for (BusinessDTO suit : suits)
    {
      String expectedName = suit.getValue(EnumerationMasterInfo.NAME);
      if (!enumNameMap.containsKey(expectedName))
      {
        fail("The enumeration values on the enumeration metadata was incorrect.");
      }
    }
  }

  public void testHashMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeHashDTO(instance, "aHash").getAttributeMdDTO();

    checkAttributeMd(mdAttributeHashDTO, md);
    assertEquals(HashMethods.MD5.getMessageDigest(), md.getEncryptionMethod());
  }

  public void testSymmetricMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeSymmetricDTO(instance, "aSym").getAttributeMdDTO();

    checkAttributeMd(mdAttributeSymmetricDTO, md);
    assertEquals(SymmetricMethods.DES.getTransformation(), md.getEncryptionMethod());
  }

  public void testNewVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    InputStream stream = clientRequest.getSecureFile(fileDTO.getId());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

    // Heads up: delete
    // int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        assertEquals(bytes1.read(), bytes2.read());
        // Heads up: delete
        // i++;
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      fail(e.getLocalizedMessage());
    }
  }

  public void testGetVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getId());
    clientRequest.createBusiness(businessDTO);

    try
    {
      // Load file into the client file cache
      clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getId()).close();
    }
    catch (IOException e1)
    {
      fail(e1.getMessage());
    }

    InputStream stream = clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getId());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        assertEquals(bytes1.read(), bytes2.read());
        i++;
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      fail(e.getLocalizedMessage());
    }

    assertEquals(i, bytes.length);
  }

  public void testGetVaultFileNotCached()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getId());
    clientRequest.createBusiness(businessDTO);

    InputStream stream = clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getId());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        assertEquals(bytes1.read(), bytes2.read());
        i++;
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      fail(e.getLocalizedMessage());
    }

    assertEquals(i, bytes.length);

  }

  public void testNoPermissionGetVaultFile()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getId());
    clientRequest.createBusiness(businessDTO);

    try
    {
      tommyProxy.getSecureFile("aFile", businessDTO.getType(), businessDTO.getId());

      fail("Able to retrieve a cached secure file without permissions");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
    }
  }

  public void testDeleteVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));
    String fileId = fileDTO.getId();

    clientRequest.delete(fileId);

    try
    {
      clientRequest.getSecureFile(fileId);
      fail("Able to retrieve a file that should have been deleted");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      fail(e.getMessage());
    }
  }

  public void testNewFile()
  {
    // FIXME fix for web services
    // if (! ( clientRequest instanceof WebServiceClientRequest ))
    {
      BusinessDTO fileDTO = clientRequest.newFile("test/", "file", "txt", new ByteArrayInputStream(bytes));

      try
      {
        InputStream stream = clientRequest.getFile(fileDTO.getId());

        // Ensure that the file is the same
        BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
        BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

        try
        {
          while (bytes1.ready() || bytes2.ready())
          {
            assertEquals(bytes1.read(), bytes2.read());
          }

          bytes1.close();
          bytes2.close();
        }
        catch (IOException e)
        {
          fail(e.getLocalizedMessage());
        }
      }
      finally
      {
        clientRequest.delete(fileDTO.getId());
      }
    }
  }

  public void testDeleteFile()
  {
    // FIXME fix for web services
    // if (! ( clientRequest instanceof WebServiceClientRequest ))
    {
      BusinessDTO fileDTO = clientRequest.newFile("test/", "file", "txt", new ByteArrayInputStream(bytes));
      String fileId = fileDTO.getId();

      clientRequest.delete(fileId);

      try
      {
        clientRequest.getSecureFile(fileId);
        fail("Able to retrieve a file that should have been deleted");
      }
      catch (DataNotFoundExceptionDTO e)
      {
        // we want to land here.
      }
      catch (Throwable e)
      {
        fail(e.getMessage());
      }
    }
  }

  /**
   * This method does all the checks for attribute metadata on DTOs. All checks,
   * except for attribute specific metadata is consolidated here (it's better
   * than copying/pasting the same checks into a dozen different tests). <<<<<<<
   * HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @param mdAttribute
   * @param mdDTO
   */
  protected static void checkAttributeMd(BusinessDTO mdAttribute, AttributeMdDTO md)
  {
    assertEquals(mdAttribute.getStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getDisplayLabel());
    assertEquals(mdAttribute.getStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getDescription());
    assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.REQUIRED)), md.isRequired());
    assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.IMMUTABLE)), md.isImmutable());
    assertEquals(mdAttribute.getId(), md.getId());
    assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.SYSTEM)), md.isSystem());
    assertEquals(mdAttribute.getValue(MdAttributeConcreteInfo.NAME), md.getName());

    if (md instanceof AttributeNumberMdDTO)
    {
      AttributeNumberMdDTO numberMdDTO = (AttributeNumberMdDTO) md;

      assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_ZERO)), numberMdDTO.rejectZero());
      assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_NEGATIVE)), numberMdDTO.rejectNegative());
      assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_POSITIVE)), numberMdDTO.rejectPositive());
    }

    if (md instanceof AttributeDecMdDTO)
    {
      AttributeDecMdDTO decMdDTO = (AttributeDecMdDTO) md;

      assertEquals(Integer.parseInt(mdAttribute.getValue(MdAttributeDecInfo.LENGTH)), decMdDTO.getTotalLength());
      assertEquals(Integer.parseInt(mdAttribute.getValue(MdAttributeDecInfo.DECIMAL)), decMdDTO.getDecimalLength());
    }

  }

  /**
   *
   */
  public void testDirectionalDeleteChild()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.DELETE_CHILD.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteChild(relationshipDTO.getId());
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getId());
      clientRequest.delete(child.getId());
      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.DELETE_CHILD.name());
    }
  }

  public void testDirectionalDeleteChildInvalid()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteChild(relationshipDTO.getId());

      fail("The user Tommy does not have permission to delete a child");
    }
    catch (DeleteChildPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getId());
      clientRequest.delete(child.getId());
    }
  }

  public void testDirectionalDeleteParent()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.DELETE_PARENT.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteParent(relationshipDTO.getId());
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      if (parent != null)
      {
        clientRequest.delete(parent.getId());
      }
      if (child != null)
      {
        clientRequest.delete(child.getId());
      }
      clientRequest.revokeTypePermission(tommyUser.getId(), mdRelationship.getId(), Operation.DELETE_PARENT.name());
    }
  }

  public void testDirectionalDeleteParentInvalid()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getId(), child.getId(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteParent(relationshipDTO.getId());

      fail("The user Tommy does not have permission to delete a child");
    }
    catch (DeleteParentPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getId());
      clientRequest.delete(child.getId());
    }
  }

  /**
   * Makes sure that a queryDTO for a given type returns the attributes that the
   * query type defines.
   */
  public void testQueryDTODefinedAttributes()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

    AttributeBooleanDTO aBoolean = (AttributeBooleanDTO) queryDTO.getAttributeDTO(mdAttributeBooleanDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeBooleanDTO, aBoolean.getAttributeMdDTO());

    AttributeBlobDTO aBlob = (AttributeBlobDTO) queryDTO.getAttributeDTO(mdAttributeBlobDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeBlobDTO, aBlob.getAttributeMdDTO());

    AttributeCharacterDTO aCharacter = (AttributeCharacterDTO) queryDTO.getAttributeDTO(mdAttributeCharacterDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeCharacterDTO, aCharacter.getAttributeMdDTO());

    AttributeTextDTO aText = (AttributeTextDTO) queryDTO.getAttributeDTO(mdAttributeTextDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeTextDTO, aText.getAttributeMdDTO());

    AttributeClobDTO aClob = (AttributeClobDTO) queryDTO.getAttributeDTO(mdAttributeClobDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeClobDTO, aClob.getAttributeMdDTO());

    AttributeIntegerDTO anInteger = (AttributeIntegerDTO) queryDTO.getAttributeDTO(mdAttributeIntegerDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeIntegerDTO, anInteger.getAttributeMdDTO());

    AttributeLongDTO aLong = (AttributeLongDTO) queryDTO.getAttributeDTO(mdAttributeLongDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeLongDTO, aLong.getAttributeMdDTO());

    AttributeDecimalDTO aDecimal = (AttributeDecimalDTO) queryDTO.getAttributeDTO(mdAttributeDecimalDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeDecimalDTO, aDecimal.getAttributeMdDTO());

    AttributeDoubleDTO aDouble = (AttributeDoubleDTO) queryDTO.getAttributeDTO(mdAttributeDoubleDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeDoubleDTO, aDouble.getAttributeMdDTO());

    AttributeFloatDTO aFloat = (AttributeFloatDTO) queryDTO.getAttributeDTO(mdAttributeFloatDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeFloatDTO, aFloat.getAttributeMdDTO());

    AttributeReferenceDTO aReference = (AttributeReferenceDTO) queryDTO.getAttributeDTO(mdAttributeReferenceDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeReferenceDTO, aReference.getAttributeMdDTO());

    AttributeTermDTO aTerm = (AttributeTermDTO) queryDTO.getAttributeDTO(mdAttributeTermDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeTermDTO, aTerm.getAttributeMdDTO());

    AttributeEnumerationDTO anEnumeration = (AttributeEnumerationDTO) queryDTO.getAttributeDTO(mdAttributeEnumerationDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeEnumerationDTO, anEnumeration.getAttributeMdDTO());

    AttributeStructDTO aStruct = (AttributeStructDTO) queryDTO.getAttributeDTO(mdAttributeStructDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeStructDTO, aStruct.getAttributeMdDTO());

    AttributeDateTimeDTO aDateTime = (AttributeDateTimeDTO) queryDTO.getAttributeDTO(mdAttributeDateTimeDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeDateTimeDTO, aDateTime.getAttributeMdDTO());

    AttributeTimeDTO aTime = (AttributeTimeDTO) queryDTO.getAttributeDTO(mdAttributeTimeDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeTimeDTO, aTime.getAttributeMdDTO());

    AttributeDateDTO aDate = (AttributeDateDTO) queryDTO.getAttributeDTO(mdAttributeDateDTO.getValue(MdAttributeConcreteInfo.NAME));
    checkAttributeMd(mdAttributeDateDTO, aDate.getAttributeMdDTO());
  }

  public void testToString()
  {
    BusinessDTO businessDTO = null;

    try
    {
      businessDTO = clientRequest.newBusiness(parentMdBusinessType);

      String expectedToString = toStringPrepend + businessDTO.getId();
      String toString = businessDTO.toString();

      if (!expectedToString.equals(toString))
      {
        fail("The toString() value on an EntityDTO is incorrect.");
      }
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
  }

  // TODO enable once the state machines have been fixed (bugzilla task for
  // naifeh)
  // public void test_StateInfoOnBusinessDTO()
  // {
  // BusinessDTO instance = clientRequest.newBusiness(childMdBusinessType);
  // clientRequest.createBusiness(instance);
  //
  // assertEquals(instance.getState(),
  // state1.getValue(StateMasterIF.STATE_NAME));
  //
  // List<String> transitions = instance.getTransitions();
  // assertEquals(transitions.size(), 2);
  //
  // String tranName1 = transition.getValue(StateMasterIF.TRANSITION_NAME);
  // String tranName2 = transition2.getValue(StateMasterIF.TRANSITION_NAME);
  //
  // String tran1 = transitions.get(0);
  // String tran2 = transitions.get(1);
  //
  // if (!tran1.equals(tranName1) && !tran1.equals(tranName2))
  // {
  // fail("Invalid transistion information was returned");
  // }
  //
  // if (!tran2.equals(tranName1) && !tran2.equals(tranName2))
  // {
  // fail("Invalid transistion information was returned");
  // }
  //
  // if (tran1.equals(tran2))
  // {
  // fail("Invalid transistion information was returned");
  // }
  // }

  public void testQueryDTObasics()
  {
    // queries for objects using the basic properties of the queryDTO
    for (int i = 0; i < 10; i++)
    {
      StructDTO structDTO = clientRequest.newStruct(structType);
      clientRequest.createStruct(structDTO);
    }

    StructQueryDTO queryDTO = (StructQueryDTO) clientRequest.getQuery(structType);
    queryDTO.setCountEnabled(true);
    queryDTO.setPageNumber(2);
    queryDTO.setPageSize(3);

    queryDTO = clientRequest.queryStructs(queryDTO);

    assertEquals(queryDTO.isCountEnabled(), true);
    assertEquals(queryDTO.getCount(), 10);
    assertEquals(queryDTO.getPageNumber(), 2);
    assertEquals(queryDTO.getPageSize(), 3);
    assertEquals(queryDTO.getResultSet().size(), 3);
  }

  public void testStructQueryDTO()
  {
    StructQueryDTO queryDTO = (StructQueryDTO) clientRequest.getQuery(structType);

    AttributeCharacterDTO charDTO = (AttributeCharacterDTO) queryDTO.getAttributeDTO(structMdAttributeCharDTO.getValue(MdAttributeCharacterInfo.NAME));
    assertNotNull(charDTO);

    assertEquals(queryDTO.getType(), structType);
  }

  public void testRelationshipQueryDTO()
  {
    RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

    AttributeLongDTO longDTO = (AttributeLongDTO) queryDTO.getAttributeDTO(relMdAttributeLongDTO.getValue(MdAttributeCharacterInfo.NAME));
    assertNotNull(longDTO);
    assertEquals(longDTO.getValue(), "123321"); // test the default value

    assertEquals(queryDTO.getParentMdBusiness(), parentMdBusinessType);
    assertEquals(queryDTO.getChildMdBusiness(), childMdBusinessType);
    assertEquals(queryDTO.getType(), mdRelationshipType);
  }

  public void testBusinessQueryDTO()
  {
    BusinessQueryDTO parentQueryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);
    List<TypeInMdRelationshipAsParent> asParents = parentQueryDTO.getTypeInMdRelationshipAsParentList();
    assertEquals(asParents.size(), 1);

    TypeInMdRelationshipAsParent asParent = asParents.get(0);
    assertEquals(asParent.getParentDisplayLabel(), mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(asParent.getRelationshipType(), mdRelationshipType);

    BusinessQueryDTO childQueryDTO = (BusinessQueryDTO) clientRequest.getQuery(childMdBusinessType);
    List<TypeInMdRelationshipAsChild> asChilds = childQueryDTO.getTypeInMdRelationshipAsChildList();
    assertEquals(asChilds.size(), 1);

    TypeInMdRelationshipAsChild asChild = asChilds.get(0);
    assertEquals(asChild.getChildDisplayLabel(), mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    assertEquals(asChild.getRelationshipType(), mdRelationshipType);
  }

  public void testImportDomainModel()
  {
    String location = TestConstants.Path.XMLFiles + "/standaloneSetTest.xml";
    String pack = "test.xmlclasses";
    String name = "Class1";

    String xml;
    try
    {
      xml = FileIO.readString(new File(location));
      clientRequest.importDomainModel(xml, XMLConstants.DATATYPE_XSD);

      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(MdStructInfo.CLASS);
      queryDTO.addCondition(MdBusinessInfo.NAME, QueryConditions.EQUALS, name);
      queryDTO.addCondition(MdBusinessInfo.PACKAGE, QueryConditions.EQUALS, pack);

      queryDTO = clientRequest.queryBusinesses(queryDTO);
      List<? extends BusinessDTO> resultSet = queryDTO.getResultSet();

      assertEquals(1, resultSet.size());
      clientRequest.delete(resultSet.get(0).getId());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void testNoPermissionImportDomainModel()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);
    String location = TestConstants.Path.XMLFiles + "/standaloneSetTest.xml";

    String xml;
    try
    {
      xml = FileIO.readString(new File(location));
      tommyProxy.importDomainModel(xml, XMLConstants.DATATYPE_XSD);
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }
    catch (ImportDomainExecuteExceptionDTO e)
    {
      // This is expected
    }
    catch (ServerSideException e)
    {
      fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
    }
  }

  public void testAccessAdminValid()
  {
    try
    {
      clientRequest.checkAdminScreenAccess();
    }
    catch (AdminScreenAccessExceptionDTO e)
    {
      String error = "The SYSTEM user failed to access the admin screen.";
      fail(error);
    }
  }

  public void testAccessAdminInvalid()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.checkAdminScreenAccess();
    }
    catch (AdminScreenAccessExceptionDTO e)
    {
      // we want to land here
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  public void testProxyLoggedIn()
  {
    ClientSession anonymousSession = this.createAnonymousSession();
    ClientRequestIF publicProxy = getRequest(anonymousSession);

    ClientSession userSession = null;
    ClientRequestIF userProxy = null;

    try
    {
      assertEquals(true, publicProxy.isLoggedIn());
      assertEquals(true, publicProxy.isPublicUser());

      anonymousSession.changeLogin("Tommy", "music");

      assertEquals(true, publicProxy.isLoggedIn());
      assertEquals(false, publicProxy.isPublicUser());

      anonymousSession.logout();

      userSession = this.createSession("Tommy", "music");
      userProxy = getRequest(userSession);

      assertEquals(false, publicProxy.isLoggedIn());
      assertEquals(false, publicProxy.isPublicUser());

      assertEquals(true, userProxy.isLoggedIn());
      assertEquals(false, userProxy.isPublicUser());

      userSession.logout();

      assertEquals(false, publicProxy.isLoggedIn());
      assertEquals(false, publicProxy.isPublicUser());
    }
    catch (Exception e)
    {
      fail(e.getMessage());
    }
    finally
    {
      anonymousSession.logout();

      if (userSession != null)
      {
        userSession.logout();
      }
    }
  }

}
