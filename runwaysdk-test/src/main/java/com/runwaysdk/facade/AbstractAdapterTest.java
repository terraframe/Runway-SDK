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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.ServerSideException;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.Business;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.BusinessQueryDTO;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsChild;
import com.runwaysdk.business.BusinessQueryDTO.TypeInMdRelationshipAsParent;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.EntityDTO;
import com.runwaysdk.business.EntityQueryDTO;
import com.runwaysdk.business.InvalidEnumerationNameDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.RelationshipDTO;
import com.runwaysdk.business.RelationshipQueryDTO;
import com.runwaysdk.business.StructDTO;
import com.runwaysdk.business.StructQueryDTO;
import com.runwaysdk.business.ViewDTO;
import com.runwaysdk.business.ViewQueryDTO;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.business.generation.dto.EntityDTOBaseGenerator;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.constants.AdminConstants;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
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
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.PhoneNumberInfo;
import com.runwaysdk.constants.QueryConditions;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.constants.VaultInfo;
import com.runwaysdk.constants.XMLConstants;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.DuplicateDataExceptionDTO;
import com.runwaysdk.dataaccess.MdAttributeDAOIF;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.dataaccess.UnexpectedTypeExceptionDTO;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblemDTO;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblemDTO;
import com.runwaysdk.dataaccess.cache.DataNotFoundExceptionDTO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFileDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeHashDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeMultiTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeVirtualDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.query.ConditionOperator;
import com.runwaysdk.query.QueryFactory;
import com.runwaysdk.session.AttributeWritePermissionExceptionDTO;
import com.runwaysdk.session.CreatePermissionExceptionDTO;
import com.runwaysdk.session.DeleteChildPermissionExceptionDTO;
import com.runwaysdk.session.DeleteParentPermissionExceptionDTO;
import com.runwaysdk.session.DeletePermissionExceptionDTO;
import com.runwaysdk.session.GrantAttributePermissionExceptionDTO;
import com.runwaysdk.session.GrantTypePermissionExceptionDTO;
import com.runwaysdk.session.ImportDomainExecuteExceptionDTO;
import com.runwaysdk.session.InvalidLoginExceptionDTO;
import com.runwaysdk.session.ReadChildPermissionExceptionDTO;
import com.runwaysdk.session.ReadParentPermissionExceptionDTO;
import com.runwaysdk.session.ReadPermissionExceptionDTO;
import com.runwaysdk.session.ReadTypePermissionExceptionDTO;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RevokeAttributePermissionExceptionDTO;
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
import com.runwaysdk.vault.VaultDAO;
import com.runwaysdk.web.AdminScreenAccessExceptionDTO;

@RunWith(ClasspathTestRunner.class)
public abstract class AbstractAdapterTest
{
  protected static String                       pack                           = "com.test.controller";

  protected static BusinessDAO                  tommyUser                      = null;

  protected static BusinessDAO                  littleBillyTables              = null;

  protected static ClientSession                systemSession                  = null;

  protected static ClientRequestIF              clientRequest                  = null;

  protected static MdBusinessDAO                childMdBusiness                = null;

  protected static String                       childMdBusinessType            = null;

  protected static MdBusinessDAO                parentMdBusiness               = null;

  protected static String                       parentMdBusinessType           = null;

  protected static MdViewDAO                    mdView                         = null;

  protected static String                       mdViewType                     = null;

  protected static BusinessDTO                  childInstance                  = null;

  protected static BusinessDTO                  parentInstance                 = null;

  private static MdRelationshipDAO              mdRelationship                 = null;

  protected static String                       mdRelationshipType             = null;

  protected static RelationshipDTO              relInstance                    = null;

  protected static MdAttributeCharacterDAO      mdAttributeCharacterDTO_2      = null;

  /* Md references for attribute metadata tests */
  protected static MdAttributeBlobDAO           mdAttributeBlobDTO             = null;

  protected static MdBusinessDAO                testUserMd                     = null;

  protected static String                       testUserType                   = null;

  protected static MdAttributeBooleanDAO        mdAttributeBooleanDTO          = null;

  protected static MdAttributeCharacterDAO      mdAttributeConversionDTO       = null;

  protected static MdAttributeIntegerDAO        mdAttributeIntegerDTO          = null;

  protected static MdAttributeLongDAO           mdAttributeLongDTO             = null;

  protected static MdAttributeDecimalDAO        mdAttributeDecimalDTO          = null;

  protected static MdAttributeDoubleDAO         mdAttributeDoubleDTO           = null;

  protected static MdAttributeFloatDAO          mdAttributeFloatDTO            = null;

  protected static MdAttributeDateTimeDAO       mdAttributeDateTimeDTO         = null;

  protected static MdAttributeFileDAO           mdAttributeFileDTO             = null;

  protected static MdAttributeDateDAO           mdAttributeDateDTO             = null;

  protected static MdAttributeTimeDAO           mdAttributeTimeDTO             = null;

  protected static MdBusinessDAO                suitMaster                     = null;

  protected static String                       suitMasterType                 = null;

  protected static MdStructDAO                  structMdBusiness               = null;

  protected static String                       structType                     = null;

  protected static MdEnumerationDAO             suitMdEnumeration              = null;

  protected static String                       suitMdEnumerationType          = null;

  protected static List<String>                 suitEnumNames;

  protected static List<String>                 suitEnumDisplayLabels;

  protected static BusinessDAO                  enumMdAttribute                = null;

  protected static MdAttributeEnumerationDAO    mdAttributeEnumerationDTO      = null;

  protected static MdAttributeMultiReferenceDAO mdAttributeMultiReferenceDTO   = null;

  protected static MdAttributeMultiTermDAO      mdAttributeMultiTermDTO        = null;

  protected static MdAttributeStructDAO         mdAttributeStructDTO           = null;

  protected static MdBusinessDAO                refClass                       = null;

  protected static MdBusinessDAO                termClass                      = null;

  protected static String                       refType                        = null;

  protected static String                       termType                       = null;

  protected static MdAttributeReferenceDAO      mdAttributeReferenceDTO        = null;

  protected static MdAttributeTermDAO           mdAttributeTermDTO             = null;

  protected static MdAttributeHashDAO           mdAttributeHashDTO             = null;

  protected static MdAttributeSymmetricDAO      mdAttributeSymmetricDTO        = null;

  protected static MdAttributeCharacterDAO      mdAttributeCharacterDTO        = null;

  protected static MdAttributeVirtualDAO        mdAttributeVirtualCharacterDTO = null;

  protected static MdAttributeTextDAO           mdAttributeTextDTO             = null;

  protected static MdAttributeClobDAO           mdAttributeClobDTO             = null;

  protected static VaultDAO                     vault                          = null;

  protected static MdAttributeLongDAO           relMdAttributeLongDTO          = null;

  protected static MdAttributeCharacterDAO      structMdAttributeCharDTO       = null;

  protected static BusinessDAO                  hearts                         = null;

  protected static BusinessDAO                  clubs                          = null;

  protected static BusinessDAO                  spades                         = null;

  protected static BusinessDAO                  diamonds                       = null;

  protected static List<BusinessDAO>            suits                          = new LinkedList<BusinessDAO>();

  private static final String                   toStringPrepend                = "Test toString: ";

  protected static final byte[]                 bytes                          = { 1, 2, 3, 4 };

  protected static final byte[]                 bytes2                         = { 2, 3, 4, 5, 6 };

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

  @BeforeClass
  @Request
  public static void modelSetUp()
  {
    modelSetUpInTransaction();
    finalizeSetup();
  }

  @Transaction
  public static void modelSetUpInTransaction()
  {
    // create a new TestUser type with a phone number struct
    testUserMd = MdBusinessDAO.newInstance();
    testUserMd.setValue(MdBusinessInfo.NAME, "TestUser");
    testUserMd.setValue(MdBusinessInfo.PACKAGE, pack);
    testUserMd.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    testUserMd.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "User Subclass");
    testUserMd.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, UserInfo.ID_VALUE);
    testUserMd.apply();

    testUserType = testUserMd.definesType();

    tommyUser = BusinessDAO.newInstance(testUserType);
    tommyUser.setValue(UserInfo.USERNAME, "Tommy");
    tommyUser.setValue(UserInfo.PASSWORD, "music");
    tommyUser.apply();

    littleBillyTables = BusinessDAO.newInstance(testUserType);
    littleBillyTables.setValue(UserInfo.USERNAME, "Billy");
    littleBillyTables.setValue(UserInfo.PASSWORD, "Tables");
    littleBillyTables.apply();

    suitMaster = MdBusinessDAO.newInstance();
    suitMaster.setValue(MdBusinessInfo.NAME, "SuitMaster");
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    suitMaster.apply();

    suitMdEnumeration = MdEnumerationDAO.newInstance();
    suitMdEnumeration.setValue(MdEnumerationInfo.NAME, "SuitEnum");
    suitMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, pack);
    suitMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration");
    suitMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    suitMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getOid());
    suitMdEnumeration.apply();

    suitMdEnumerationType = pack + ".SuitEnum";

    enumMdAttribute = MdAttributeCharacterDAO.newInstance();
    enumMdAttribute.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.SIZE, "32");
    enumMdAttribute.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    enumMdAttribute.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    enumMdAttribute.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    enumMdAttribute.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, suitMaster.getOid());
    enumMdAttribute.apply();

    structMdBusiness = MdStructDAO.newInstance();
    structMdBusiness.setValue(MdStructInfo.NAME, "StructTest");
    structMdBusiness.setValue(MdStructInfo.PACKAGE, pack);
    structMdBusiness.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A strct");
    structMdBusiness.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "junit struct");
    structMdBusiness.apply();

    structMdAttributeCharDTO = MdAttributeCharacterDAO.newInstance();
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.NAME, "structChar");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    structMdAttributeCharDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "struct string");
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    structMdAttributeCharDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, structMdBusiness.getOid());
    structMdAttributeCharDTO.apply();

    childMdBusiness = MdBusinessDAO.newInstance();
    childMdBusiness.setValue(MdBusinessInfo.NAME, "ChildTest");
    childMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    childMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test child");
    childMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    childMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    childMdBusiness.apply();

    MdAttributeStructDAO phoneNumber = MdAttributeStructDAO.newInstance();
    phoneNumber.setValue(MdAttributeStructInfo.NAME, "phoneNumber");
    phoneNumber.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Phone Number");
    phoneNumber.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testUserMd.getOid());
    phoneNumber.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    phoneNumber.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    phoneNumber.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getOid());
    phoneNumber.apply();

    mdAttributeCharacterDTO_2 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacterDTO_2.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, childMdBusiness.getOid());
    mdAttributeCharacterDTO_2.apply();

    String source = "package com.test.controller;\n" + "public class ParentTest extends ParentTestBase\n" + "{" + "public ParentTest()" + "{" + "   super();" + "}" + "public static ParentTest get(String oid)" + "{" + "  return (ParentTest) " + Business.class.getName() + ".get(oid);" + "}" + "public String toString()" + "{" + "  return \"" + toStringPrepend + "\" + getOid();" + "}" + "}";
    
    parentMdBusiness = MdBusinessDAO.newInstance();
    parentMdBusiness.setValue(MdBusinessInfo.NAME, "ParentTest");
    parentMdBusiness.setValue(MdBusinessInfo.PACKAGE, pack);
    parentMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test parent");
    parentMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    parentMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.addItem(MdBusinessInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
    parentMdBusiness.setValue(MdElementInfo.STUB_SOURCE, source);
    parentMdBusiness.apply();

    mdAttributeBooleanDTO = MdAttributeBooleanDAO.newInstance();
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeBooleanDTO.apply();

    MdAttributeBooleanDAO mdAttributeBooleanImmutableDTO = MdAttributeBooleanDAO.newInstance();
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.NAME, "anImmutableBoolean");
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Immutable Boolean");
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanImmutableDTO.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.DEFAULT_VALUE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanImmutableDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeBooleanImmutableDTO.apply();

    mdAttributeCharacterDTO = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character desc");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeCharacterDTO.apply();

    mdAttributeConversionDTO = MdAttributeCharacterDAO.newInstance();
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.NAME, "aHiddenCharacter");
    mdAttributeConversionDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hidden Character");
    mdAttributeConversionDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hidden Character desc");
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.GENERATE_ACCESSOR, MdAttributeBooleanInfo.FALSE);
    mdAttributeConversionDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeConversionDTO.apply();

    MdAttributeCharacterDAO mdAttributeCharGroupIndex1_DTO = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacterGroupIndex1");
    mdAttributeCharGroupIndex1_DTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character Group Index 1");
    mdAttributeCharGroupIndex1_DTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character for a group index");
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharGroupIndex1_DTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeCharGroupIndex1_DTO.apply();

    MdAttributeCharacterDAO mdAttributeCharGroupIndex2_DTO = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacterGroupIndex2");
    mdAttributeCharGroupIndex2_DTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character Group Index 2");
    mdAttributeCharGroupIndex2_DTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character for a group index");
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharGroupIndex2_DTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeCharGroupIndex2_DTO.apply();

    MdIndexDAO mdIndexDTO = MdIndexDAO.newInstance();
    mdIndexDTO.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndexDTO.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Group Index");
    mdIndexDTO.setValue(MdIndexInfo.MD_ENTITY, parentMdBusiness.getOid());
    mdIndexDTO.apply();
    
    mdIndexDTO.addAttribute(mdAttributeCharGroupIndex1_DTO, 0);
    mdIndexDTO.addAttribute(mdAttributeCharGroupIndex2_DTO, 1);
    mdIndexDTO.setValue(MdIndexInfo.ACTIVE, MdAttributeBooleanInfo.TRUE);
    mdIndexDTO.apply();

    mdAttributeDecimalDTO = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.NAME, "aDecimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal desc");
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.LENGTH, "16");
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.DECIMAL, "4");
    mdAttributeDecimalDTO.apply();

    mdAttributeDoubleDTO = MdAttributeDoubleDAO.newInstance();
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    mdAttributeDoubleDTO.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    mdAttributeDoubleDTO.setStructValue(MdAttributeDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double desc");
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDoubleDTO.apply();

    mdAttributeFloatDTO = MdAttributeFloatDAO.newInstance();
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.NAME, "aFloat");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float Desc");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.LENGTH, "16");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DECIMAL, "4");
    mdAttributeFloatDTO.apply();

    mdAttributeIntegerDTO = MdAttributeIntegerDAO.newInstance();
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.NAME, "anInteger");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer Desc");
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeIntegerDTO.apply();

    mdAttributeLongDTO = MdAttributeLongDAO.newInstance();
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.NAME, "aLong");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long Desc");
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeLongDTO.apply();

    mdAttributeDateDTO = MdAttributeDateDAO.newInstance();
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.NAME, "aDate");
    mdAttributeDateDTO.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date");
    mdAttributeDateDTO.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date Desc");
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeDateDTO.apply();

    mdAttributeDateTimeDTO = MdAttributeDateTimeDAO.newInstance();
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.NAME, "aDateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime Desc");
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeDateTimeDTO.apply();

    mdAttributeTimeDTO = MdAttributeTimeDAO.newInstance();
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.NAME, "aTime");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time Desc");
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeTimeDTO.apply();

    // add an MdAttributeBlob to the new type
    mdAttributeBlobDTO = MdAttributeBlobDAO.newInstance();
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.NAME, "aBlob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "some blob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob desc");
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeBlobDTO.apply();

    mdAttributeEnumerationDTO = MdAttributeEnumerationDAO.newInstance();
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.NAME, "anEnum");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated Attribute");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated desc");
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitMdEnumeration.getOid());
    mdAttributeEnumerationDTO.apply();

    mdAttributeHashDTO = MdAttributeHashDAO.newInstance();
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.NAME, "aHash");
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hashed Attributed");
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hash Desc");
    mdAttributeHashDTO.addItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getOid());
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeHashDTO.apply();

    mdAttributeFileDTO = MdAttributeFileDAO.newInstance();
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.NAME, "aFile");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File Desc");
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeFileDTO.apply();

    // class for ref object attribute
    refClass = MdBusinessDAO.newInstance();
    refClass.setValue(MdBusinessInfo.NAME, "RefClass");
    refClass.setValue(MdBusinessInfo.PACKAGE, pack);
    refClass.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    refClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A ref class");
    refClass.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit ref object");
    refClass.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    refClass.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    refClass.apply();

    mdAttributeReferenceDTO = MdAttributeReferenceDAO.newInstance();
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.NAME, "aReference");
    mdAttributeReferenceDTO.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference");
    mdAttributeReferenceDTO.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference Desc");
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, refClass.getOid());
    mdAttributeReferenceDTO.apply();

    // class for term object attribute
    termClass = MdTermDAO.newInstance();
    termClass.setValue(MdBusinessInfo.NAME, "TermClass");
    termClass.setValue(MdBusinessInfo.PACKAGE, pack);
    termClass.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    termClass.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A term class");
    termClass.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit term object");
    termClass.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    termClass.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    termClass.apply();

    mdAttributeTermDTO = MdAttributeTermDAO.newInstance();
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.NAME, "aTerm");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term");
    mdAttributeTermDTO.setStructValue(MdAttributeTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term Desc");
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTermDTO.setValue(MdAttributeTermInfo.REF_MD_ENTITY, termClass.getOid());
    mdAttributeTermDTO.apply();

    mdAttributeMultiReferenceDTO = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.NAME, "aMultiReference");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, termClass.getOid());
    mdAttributeMultiReferenceDTO.apply();

    mdAttributeMultiTermDTO = MdAttributeMultiTermDAO.newInstance();
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.NAME, "aMultiTerm");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, termClass.getOid());
    mdAttributeMultiTermDTO.apply();

    mdAttributeStructDTO = MdAttributeStructDAO.newInstance();
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.NAME, "aStruct");
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Desc");
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getOid());
    mdAttributeStructDTO.apply();

    mdAttributeSymmetricDTO = MdAttributeSymmetricDAO.newInstance();
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.NAME, "aSym");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Attribute");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Desc");
    mdAttributeSymmetricDTO.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getOid());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetricDTO.apply();

    mdAttributeTextDTO = MdAttributeTextDAO.newInstance();
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.NAME, "aText");
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text Desc");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeTextDTO.apply();

    mdAttributeClobDTO = MdAttributeClobDAO.newInstance();
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.NAME, "aClob");
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob Desc");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdBusiness.getOid());
    mdAttributeClobDTO.apply();

    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, "DtoRel");
    mdRelationship.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Test Relationship");
    mdRelationship.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdRelationship.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, parentMdBusiness.getOid());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "parent dto");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, childMdBusiness.getOid());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "child dto");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "testParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "testChild");
    mdRelationship.apply();

    addAttributesToQuery(parentMdBusiness.getOid());

    // Add attributes to the relationship to query
    BusinessDAO businessDTO = MdAttributeCharacterDAO.newInstance();
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "relChar");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdRelationship.getOid());
    businessDTO.apply();

    relMdAttributeLongDTO = MdAttributeLongDAO.newInstance();
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.NAME, "relLong");
    relMdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A long to query.");
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "123321");
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    relMdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdRelationship.getOid());
    relMdAttributeLongDTO.apply();

    businessDTO = MdAttributeTimeDAO.newInstance();
    businessDTO.setValue(MdAttributeTimeInfo.NAME, "relTime");
    businessDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A time to query.");
    businessDTO.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdRelationship.getOid());
    businessDTO.apply();

    businessDTO = MdAttributeBooleanDAO.newInstance();
    businessDTO.setValue(MdAttributeBooleanInfo.NAME, "relBoolean");
    businessDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A boolean to query.");
    businessDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdRelationship.getOid());
    businessDTO.apply();

    mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.NAME, "TestView");
    mdView.setValue(MdViewInfo.PACKAGE, pack);
    mdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test view");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    mdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    mdView.apply();

    mdViewType = mdView.definesType();

    mdAttributeVirtualCharacterDTO = MdAttributeVirtualDAO.newInstance();
    mdAttributeVirtualCharacterDTO.setValue(MdAttributeVirtualInfo.MD_ATTRIBUTE_CONCRETE, mdAttributeCharacterDTO.getOid());
    mdAttributeVirtualCharacterDTO.setValue(MdAttributeVirtualInfo.DEFINING_MD_VIEW, mdView.getOid());
    mdAttributeVirtualCharacterDTO.apply();

    String objectIdConst = TypeGenerator.buildAttributeConstant(mdViewType, "objectId");
    String queryBooleanConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryBoolean");
    String queryCharConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryChar");
    String aCharConst = TypeGenerator.buildAttributeConstant(mdViewType, "aCharacter");
    String queryLongConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryLong");
    String queryTimeConst = TypeGenerator.buildAttributeConstant(mdViewType, "queryTime");

    businessDTO = MdAttributeCharacterDAO.newInstance();
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "objectId");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "64");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Original Object Id");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdView.getOid());
    businessDTO.apply();

    addAttributesToQuery(mdView.getOid());

    String queryStubSource = "package " + pack + "; \n" + "\n" + "public class TestViewQuery extends " + pack + ".TestViewQueryBase \n" + "{\n" + "\n" + "  private ParentTestQuery parentTest;\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "     \n" + "     parentTest = new ParentTestQuery(componentQueryFactory);\n" + "\n" + "     this.map(" + objectIdConst + ", parentTest.getOid());\n" + "     this.map(" + queryBooleanConst + ", parentTest.getQueryBoolean());\n" + "     this.map(" + queryCharConst + ", parentTest.getQueryChar());\n" + "     this.map(" + aCharConst + ", parentTest.getACharacter());\n" + "     this.map(" + queryLongConst + ", parentTest.getQueryLong());\n"
        + "     this.map(" + queryTimeConst + ", parentTest.getQueryTime());\n" + "\n" + "     this.buildSelectClause();\n" + "  }\n" + "}\n";

    mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, queryStubSource);
    mdView.apply();

    vault = VaultDAO.newInstance();
    vault.setValue(VaultInfo.VAULT_NAME, "vault1");
    vault.apply();

    childMdBusinessType = childMdBusiness.definesType();
    parentMdBusinessType = parentMdBusiness.definesType();
    mdRelationshipType = mdRelationship.definesType();
    refType = refClass.definesType();
    termType = termClass.definesType();
    structType = structMdBusiness.definesType();

    suitMasterType = suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME);

  }

  private static void addAttributesToQuery(String mdClassId)
  {
    // START attributes to the parent mdentity to query
    BusinessDAO businessDTO = MdAttributeCharacterDAO.newInstance();
    businessDTO.setValue(MdAttributeCharacterInfo.NAME, "queryChar");
    businessDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    businessDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    businessDTO.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClassId);
    businessDTO.apply();

    businessDTO = MdAttributeLongDAO.newInstance();
    businessDTO.setValue(MdAttributeLongInfo.NAME, "queryLong");
    businessDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A long to query.");
    businessDTO.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClassId);
    businessDTO.apply();

    businessDTO = MdAttributeTimeDAO.newInstance();
    businessDTO.setValue(MdAttributeTimeInfo.NAME, "queryTime");
    businessDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A time to query.");
    businessDTO.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    businessDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdClassId);
    businessDTO.apply();

    businessDTO = MdAttributeBooleanDAO.newInstance();
    businessDTO.setValue(MdAttributeBooleanInfo.NAME, "queryBoolean");
    businessDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A boolean to query.");
    businessDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    businessDTO.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    businessDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClassId);
    businessDTO.apply();
    // END add attributes to query
  }

  @Transaction
  public static void finalizeSetup()
  {
    suitEnumNames = new LinkedList<String>();
    suitEnumDisplayLabels = new LinkedList<String>();

    hearts = BusinessDAO.newInstance(suitMasterType);
    hearts.setValue("refChar", "Some other string");
    hearts.setValue(EnumerationMasterInfo.NAME, "HEARTS");
    hearts.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    hearts.apply();
    suitEnumNames.add(hearts.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(hearts.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    clubs = BusinessDAO.newInstance(suitMasterType);
    clubs.setValue("refChar", "Some other string: Clubs");
    clubs.setValue(EnumerationMasterInfo.NAME, "CLUBS");
    clubs.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    clubs.apply();
    suitEnumNames.add(clubs.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(clubs.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    spades = BusinessDAO.newInstance(suitMasterType);
    spades.setValue("refChar", "Some other string: Spades");
    spades.setValue(EnumerationMasterInfo.NAME, "SPADES");
    spades.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Spades");
    spades.apply();
    
    suitEnumNames.add(spades.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(spades.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    diamonds = BusinessDAO.newInstance(suitMasterType);
    diamonds.setValue("refChar", "Some other string: Diamonds");
    diamonds.setValue(EnumerationMasterInfo.NAME, "DIAMONDS");
    diamonds.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    diamonds.apply();
    suitEnumNames.add(diamonds.getValue(EnumerationMasterInfo.NAME));
    suitEnumDisplayLabels.add(diamonds.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));

    suits.add(hearts);
    suits.add(clubs);
    suits.add(spades);
    suits.add(diamonds);

    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, diamonds.getOid());
    mdAttributeEnumerationDTO.apply();
  }

  @Request
  @AfterClass
  public static void modelTearDown()
  {
    TestFixtureFactory.delete(mdView);

    TestFixtureFactory.delete(mdRelationship);

    TestFixtureFactory.delete(childMdBusiness);

    TestFixtureFactory.delete(parentMdBusiness);

    TestFixtureFactory.delete(structMdBusiness);

    TestFixtureFactory.delete(refClass);

    TestFixtureFactory.delete(termClass);

    TestFixtureFactory.delete(suitMaster);

    TestFixtureFactory.delete(vault);

    suits.clear();

    TestFixtureFactory.delete(littleBillyTables);

    TestFixtureFactory.delete(tommyUser);

    TestFixtureFactory.delete(testUserMd);
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
    relInstance = clientRequest.addParent(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
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
      clientRequest.delete(parentInstance.getOid());
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

  @Request
  @Test
  public void testUserRoleMap()
  {
    clientRequest.assignMember(tommyUser.getOid(), RoleDAOIF.ROLE_ADMIN_ROLE, RoleDAOIF.DEVELOPER_ROLE);

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyRequest = getRequest(tommySession);

    Map<String, String> roleMap = tommyRequest.getSessionUserRoles();

    Assert.assertEquals(roleMap.size(), 2);
    Assert.assertTrue(roleMap.containsKey(RoleDAOIF.ROLE_ADMIN_ROLE));
    Assert.assertTrue(roleMap.containsKey(RoleDAOIF.DEVELOPER_ROLE));

    tommySession.logout();

    clientRequest.removeMember(tommyUser.getOid(), RoleDAOIF.ROLE_ADMIN_ROLE, RoleDAOIF.DEVELOPER_ROLE);
  }

  @Request
  @Test
  public void testGetAllBusinessDTOsForEnumItems()
  {
    Class<?> masterEnumClass = LoaderDecorator.load(suitMasterType + EntityDTOBaseGenerator.DTO_SUFFIX);

    List<? extends BusinessDTO> enumItems = clientRequest.getAllEnumerations(suitMdEnumerationType);

    Assert.assertEquals("Did not return the correct number of enumeration items.", suitEnumNames.size(), enumItems.size());

    for (BusinessDTO enumItem : enumItems)
    {
      if (!masterEnumClass.isInstance(enumItem))
      {
        Assert.fail(BusinessDTO.class.getName() + "s representing enumeration items were returned of the wrong type.");
      }
      else
      {
        if (!suitEnumNames.contains(enumItem.getValue(EnumerationMasterInfo.NAME)))
        {
          Assert.fail("Did not return all of the enumerations for the given [" + MdEnumerationInfo.CLASS + "]");
        }
        else
        {
          int index = suitEnumNames.indexOf(enumItem.getValue(EnumerationMasterInfo.NAME));
          Assert.assertEquals("Returned the wrong enumeration name display label.", suitEnumDisplayLabels.get(index), enumItem.getStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
        }
      }
    }

  }

  @Request
  @Test
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

    Assert.assertEquals("Did not return the correct number of enumeration items.", requestedNames.length, enumItems.size());

    for (BusinessDTO enumItem : enumItems)
    {
      if (!masterEnumClass.isInstance(enumItem))
      {
        Assert.fail(BusinessDTO.class.getName() + "s representing enumeration items were returned of the wrong type.");
      }
      else
      {
        if (!requestedNamesList.contains(enumItem.getValue(EnumerationMasterInfo.NAME)))
        {
          Assert.fail("Did not return all of the enumerations for the given [" + MdEnumerationInfo.CLASS + "]");
        }
      }
    }

  }

  @Request
  @Test
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
      Assert.assertEquals("Wrong localized message.", expectedMessage, e.getMessage());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testEmptyValueProblem()
  {
    BusinessDTO parent = clientRequest.newBusiness(parentMdBusinessType);

    try
    {
      clientRequest.update(parent);
      Assert.fail("Able to apply an object without supplying values to required attributes");
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
            Assert.assertEquals("A Character", emptyValueProblemDTO.getAttributeDisplayLabel());
            Assert.assertEquals(parentMdBusinessType, emptyValueProblemDTO.getDefiningType());
            Assert.assertEquals("A simple test parent", emptyValueProblemDTO.getDefiningTypeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aHiddenCharacter"))
          {
            aHiddenCharacterProblem = true;
            Assert.assertEquals("A Hidden Character", emptyValueProblemDTO.getAttributeDisplayLabel());
            Assert.assertEquals(parentMdBusinessType, emptyValueProblemDTO.getDefiningType());
            Assert.assertEquals("A simple test parent", emptyValueProblemDTO.getDefiningTypeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aCharacterGroupIndex1"))
          {
            groupIndex1Problem = true;
            Assert.assertEquals("A Character Group Index 1", emptyValueProblemDTO.getAttributeDisplayLabel());
          }
          else if (emptyValueProblemDTO.getAttributeName().equals("aCharacterGroupIndex2"))
          {
            groupIndex2Problem = true;
            Assert.assertEquals("A Character Group Index 2", emptyValueProblemDTO.getAttributeDisplayLabel());
          }
        }
      }

      if (problemList.size() == 4 && aCharacterProblem && aHiddenCharacterProblem && groupIndex1Problem && groupIndex2Problem)
      {
        // we want to land here
      }
      else
      {
        Assert.fail("The wrong number and/or types of problems were thrown.");
      }

      // Make sure the metadata is able to identify the attribute that violates
      // the constraint
    }
    finally
    {
      if (!parent.isNewInstance())
      {
        clientRequest.delete(parent.getOid());
      }
    }
  }

  @Request
  @Test
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
      Assert.fail("Able to violate a uniqueness constraint defined on an attribute.");
    }
    catch (DuplicateDataExceptionDTO e)
    {
      // we want to land here
      // Make sure the metadata is able to identify the attribute that violates
      // the constraint
      // if (!e.getDeveloperMessage().contains("[aCharacter]")) {
      // Assert.fail("Expected to find the string \"[aCharacter]\" within the
      // exception's developer message: ["
      // + e.getDeveloperMessage() + "].");
      // }
      String check = "[A Character]";
      if (!e.getMessage().contains(check))
      {
        Assert.fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
    }
    finally
    {
      clientRequest.delete(parent1.getOid());
      if (!parent2.isNewInstance())
      {
        clientRequest.delete(parent2.getOid());
      }
    }
  }

  @Request
  @Test
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
      Assert.fail("Able to violate a uniqueness constraint defined on an attribute.");
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
        Assert.fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
      check = "[A Character Group Index 2]";
      if (!e.getMessage().contains(check))
      {
        Assert.fail("Expected to find the string \"" + check + "\" within the exception's message: [" + e.getMessage() + "].");
      }
    }
    finally
    {
      clientRequest.delete(parent1.getOid());
      if (!parent2.isNewInstance())
      {
        clientRequest.delete(parent2.getOid());
      }
    }

  }

  @Request
  @Test
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
      Assert.assertEquals(results.size(), 1);

      BusinessDTO result = results.get(0);
      Assert.assertEquals(charVal, result.getValue("aCharacter"));
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @Request
  @Test
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
      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getOid(), child1.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getOid(), child2.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getOid(), child3.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<BusinessDTO> definedObjects = new LinkedList<BusinessDTO>();
      definedObjects.add(child1);
      definedObjects.add(child2);
      definedObjects.add(child3);

      // retrieve the children and check for oid matches
      List<? extends BusinessDTO> returnedObjects = (List<? extends BusinessDTO>) clientRequest.getChildren(parent.getOid(), mdRelationshipType);
      for (BusinessDTO defRel : definedObjects)
      {
        boolean foundMatch = false;
        for (BusinessDTO returnRel : returnedObjects)
        {
          // look for a relationship match
          if (defRel.getOid().equals(returnRel.getOid()))
          {
            foundMatch = true;
          }
        }
        if (!foundMatch)
        {
          Assert.fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getOid());

      if (child1 != null)
        clientRequest.delete(child1.getOid());

      if (child2 != null)
        clientRequest.delete(child2.getOid());

      if (child3 != null)
        clientRequest.delete(child3.getOid());
    }
  }

  @Request
  @Test
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
      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent3.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<BusinessDTO> definedObjects = new LinkedList<BusinessDTO>();
      definedObjects.add(parent1);
      definedObjects.add(parent2);
      definedObjects.add(parent3);

      // retrieve the children and check for oid matches
      List<? extends BusinessDTO> returnedObjects = (List<? extends BusinessDTO>) clientRequest.getParents(child.getOid(), mdRelationshipType);
      for (BusinessDTO defRel : definedObjects)
      {
        boolean foundMatch = false;
        for (BusinessDTO returnRel : returnedObjects)
        {
          // look for a relationship match
          if (defRel.getOid().equals(returnRel.getOid()))
          {
            foundMatch = true;
          }
        }
        if (!foundMatch)
        {
          Assert.fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getOid());

      if (parent2 != null)
        clientRequest.delete(parent2.getOid());

      if (parent3 != null)
        clientRequest.delete(parent3.getOid());

      if (child != null)
        clientRequest.delete(child.getOid());
    }
  }

  /**
   * Creates a few relationships and then gets the children.
   */
  @SuppressWarnings("unchecked")
  @Request
  @Test
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
      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getOid(), child1.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getOid(), child2.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getOid(), child3.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<RelationshipDTO> definedRels = new LinkedList<RelationshipDTO>();
      definedRels.add(relDTO1);
      definedRels.add(relDTO2);
      definedRels.add(relDTO3);

      // retrieve the children and check for oid matches
      List<RelationshipDTO> returnedRels = (List<RelationshipDTO>) clientRequest.getChildRelationships(parent.getOid(), mdRelationshipType);
      for (RelationshipDTO defRel : definedRels)
      {
        boolean foundMatch = false;
        for (RelationshipDTO returnRel : returnedRels)
        {
          // look for a relationship match
          if (defRel.getOid().equals(returnRel.getOid()))
          {
            if (defRel.getChildId().equals(returnRel.getChildId()) && defRel.getParentId().equals(returnRel.getParentId()))
            {
              foundMatch = true;
            }
          }
        }
        if (!foundMatch)
        {
          Assert.fail("The returned list of RelationshipDTO objects from the facade's getChildRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getOid());

      if (child1 != null)
        clientRequest.delete(child1.getOid());

      if (child2 != null)
        clientRequest.delete(child2.getOid());

      if (child3 != null)
        clientRequest.delete(child3.getOid());
    }
  }

  /**
   * Creates a few relationships and then gets the parents.
   */
  @SuppressWarnings("unchecked")
  @Request
  @Test
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
      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent3.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      List<RelationshipDTO> definedRels = new LinkedList<RelationshipDTO>();
      definedRels.add(relDTO1);
      definedRels.add(relDTO2);
      definedRels.add(relDTO3);

      // retrieve the children and check for oid matches
      List<RelationshipDTO> returnedRels = (List<RelationshipDTO>) clientRequest.getParentRelationships(child.getOid(), mdRelationshipType);
      for (RelationshipDTO defRel : definedRels)
      {
        boolean foundMatch = false;
        for (RelationshipDTO returnRel : returnedRels)
        {
          // look for a relationship match
          if (defRel.getOid().equals(returnRel.getOid()))
          {
            if (defRel.getChildId().equals(returnRel.getChildId()) && defRel.getParentId().equals(returnRel.getParentId()))
            {
              foundMatch = true;
            }
          }
        }
        if (!foundMatch)
        {
          Assert.fail("The returned list of RelationshipDTO objects from the facade's getParentRelationships() is invalid");
        }
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getOid());

      if (parent2 != null)
        clientRequest.delete(parent2.getOid());

      if (parent3 != null)
        clientRequest.delete(parent3.getOid());

      if (child != null)
        clientRequest.delete(child.getOid());
    }
  }

  /**
   * Adds a child to a relationship.
   */
  @Request
  @Test
  public void testAddChild()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;
    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getOid());

      if (parent != null)
        clientRequest.delete(parent.getOid());
    }
  }

  /**
   * Deletes a child in a relationship.
   */
  @Request
  @Test
  public void testDeleteChild()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      // delete the child
      clientRequest.lock(relDTO);
      clientRequest.deleteChild(relDTO.getOid());
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getOid());

      if (parent != null)
        clientRequest.delete(parent.getOid());
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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

      RelationshipDTO relDTO1 = clientRequest.addChild(parent.getOid(), child1.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addChild(parent.getOid(), child2.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addChild(parent.getOid(), child3.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      // delete the children
      clientRequest.deleteChildren(parent.getOid(), mdRelationshipType);

      // make sure the children were deleted
      List<RelationshipDTO> retrievedChildren = (List<RelationshipDTO>) clientRequest.getChildRelationships(parent.getOid(), mdRelationshipType);

      // there should not be any children
      if (retrievedChildren.size() != 0)
      {
        Assert.fail("A facade call to deleteChildren() failed to delete all children.");
      }
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getOid());

      if (child1 != null)
        clientRequest.delete(child1.getOid());

      if (child2 != null)
        clientRequest.delete(child2.getOid());

      if (child3 != null)
        clientRequest.delete(child3.getOid());
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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

      RelationshipDTO relDTO1 = clientRequest.addParent(parent1.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO1);
      RelationshipDTO relDTO2 = clientRequest.addParent(parent2.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO2);
      RelationshipDTO relDTO3 = clientRequest.addParent(parent2.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO3);

      // delete the parents
      clientRequest.deleteParents(child.getOid(), mdRelationshipType);

      // make sure the parents were deleted
      List<RelationshipDTO> retrievedParents = (List<RelationshipDTO>) clientRequest.getParentRelationships(child.getOid(), mdRelationshipType);

      // there should not be any parents
      if (retrievedParents.size() != 0)
      {
        Assert.fail("A facade call to deleteParents() failed to delete all parents.");
      }
    }
    finally
    {
      if (parent1 != null)
        clientRequest.delete(parent1.getOid());

      if (parent2 != null)
        clientRequest.delete(parent2.getOid());

      if (parent3 != null)
        clientRequest.delete(parent3.getOid());

      if (child != null)
        clientRequest.delete(child.getOid());
    }
  }

  @Request
  @Test
  public void testDeleteParent()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      // delete the parent
      clientRequest.lock(relDTO);
      clientRequest.deleteParent(relDTO.getOid());
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getOid());

      if (parent != null)
        clientRequest.delete(parent.getOid());
    }
  }

  /**
   * Adds an invalid child to a relationship.
   */
  @Request
  @Test
  public void testAddChildInvalid()
  {
    BusinessDTO child = null;
    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relDTO = clientRequest.addChild(child.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      Assert.fail("An invalid relationship occured through Facade.addChild()");
    }
    catch (UnexpectedTypeExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (child != null)
      {
        clientRequest.delete(child.getOid());
      }
    }
  }

  /**
   * Adds a valid parent to a relationship.
   */
  @Request
  @Test
  public void testAddParent()
  {
    BusinessDTO child = null;
    BusinessDTO parent = null;

    try
    {
      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);
    }
    finally
    {
      if (child != null)
        clientRequest.delete(child.getOid());

      if (parent != null)
        clientRequest.delete(parent.getOid());
    }
  }

  /**
   * Adds an invalid parent to a relationship.
   */
  @Request
  @Test
  public void testAddParentInvalid()
  {
    BusinessDTO parent = null;
    try
    {
      parent = this.createParentInstance(clientRequest);

      RelationshipDTO relDTO = clientRequest.addParent(parent.getOid(), parent.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relDTO);

      Assert.fail("An invalid relationship occured through Facade.addParent()");
    }
    catch (UnexpectedTypeExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (parent != null)
        clientRequest.delete(parent.getOid());
    }
  }

  /**
   * Updates an entity through the controller.
   */
  @Request
  @Test
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

      Assert.assertEquals(testDTO.getValue("aCharacter"), "hello");
      Assert.assertEquals(testDTO.getValue("aText"), "purple monkey dishwasher");
      Assert.assertEquals(testDTO.getValue("aClob"), "a very large purple monkey dishwasher");
      Assert.assertEquals(testDTO.getValue("aFloat"), "12.34");
      Assert.assertEquals(testDTO.getValue("aBoolean"), MdAttributeBooleanInfo.TRUE);
      Assert.assertEquals(testDTO.getValue("anInteger"), "976");
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getOid());
      }
    }
  }

  /**
   * Updates an entity through the controller.
   */
  @Request
  @Test
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
        Assert.fail("Wrong number of problems returned");
      }
      else
      {
        ProblemDTOIF problemDTOIF = problemList.get(0);

        if (! ( problemDTOIF instanceof ImmutableAttributeProblemDTO ))
        {
          Assert.fail("Wrong problem class returned for an immutable attribute violation.");
        }
      }
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getOid());
      }
    }
  }

  /**
   * Updates an entity in an invalid fashion by setting an attribute incorrectly
   */
  @Request
  @Test
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

      Assert.fail("The controller allowed for an erroneous attribute to be set.");
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
      Assert.fail("Did not find [" + AttributeProblemDTO.CLASS + "] in the problem list.");
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.lock(testDTO);
        clientRequest.delete(testDTO.getOid());
      }
    }
  }

  /**
   * Deletes an entity.
   */
  @Request
  @Test
  public void testDeleteEntity()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      clientRequest.lock(businessDTO);
      clientRequest.delete(businessDTO.getOid());

      // Now try to get the object that was created. If it was deleted, it
      // should error out.
      clientRequest.get(businessDTO.getOid());
      Assert.fail("An entity was not correctly deleted");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tries to double-delete an entity, which should fail.
   */
  @Request
  @Test
  public void testDeleteEntityInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.delete("999999999999999999999999999999999-this.class.should.not.exist.Blah");
      Assert.fail("An entity was able to deleted that doesn't exist.");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Creates a valid instance of a type.
   */
  @Request
  @Test
  public void testNewInstance()
  {
    BusinessDTO businessDTO = null;

    try
    {
      businessDTO = clientRequest.newBusiness(parentMdBusinessType);

      if (!businessDTO.getType().equals(parentMdBusinessType))
      {
        Assert.fail("A new instance type did not match its defining entity.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Attempts to create an invalid instance of a fake type.
   */
  @Request
  @Test
  public void testNewInstanceInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.newBusiness("this.class.should.not.exist.Blah");
      Assert.fail("A new instance was created that doesn't exist.");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstances()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertTrue("The [getAllInstances] method should return one or more results", resultSet.size() >= 1);
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }
    }
  }

  /**
   * Ensures that the oid changes between a new <code>BusinessDTO</code> and when
   * the DTO is applied
   */
  @Request
  @Test
  public void testSameId()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.initParentInstance();
      String oid = businessDTO.getOid();

      clientRequest.createBusiness(businessDTO);

      Assert.assertFalse("OID of a new " + BusinessDTO.class.getName() + " did not change when it was applied.", oid.equals(businessDTO.getOid()));
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstancesEmpty()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertEquals("The [getAllInstances] method should have no results", 0, resultSet.size());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstancesNullSortAttribute()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, null, false, 0, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertEquals("The [getAllInstances] method should have no results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstancesNegativePageSize()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", false, -1, 0);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertEquals("The [getAllInstances] method should have no results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstancesNegativePageNumber()
  {
    try
    {
      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", false, 0, -1);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertEquals("The [getAllInstances] method should have all of the results results", 2, resultSet.size());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Get all instances.
   */
  @Request
  @Test
  public void testGetAllInstancesInvalidPageNumber()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      EntityQueryDTO query = clientRequest.getAllInstances(parentMdBusinessType, "keyName", true, 2, 10);

      List<? extends EntityDTO> resultSet = query.getResultSet();

      Assert.assertEquals("The [getAllInstances] method should have no results for an out of range page", 0, resultSet.size());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }
    }
  }

  /**
   * Gets a valid instance.
   */
  @Request
  @Test
  public void testInstance()
  {
    BusinessDTO businessDTO = null;

    try
    {
      // create the entity and then delete it
      businessDTO = this.createParentInstance(clientRequest);

      BusinessDTO retrieved = (BusinessDTO) clientRequest.get(businessDTO.getOid());

      Assert.assertEquals(businessDTO.getOid(), retrieved.getOid());

    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (businessDTO != null)
      {
        clientRequest.delete(businessDTO.getOid());
      }
    }
  }

  /**
   * Attempts to grab an invalid instance.
   */
  @Request
  @Test
  public void testInstanceInvalid()
  {
    try
    {
      // try to delete an entity that doesn't exist
      clientRequest.get("");
      Assert.fail("An instance was retrieved that doesn't exist.");
    }
    catch (ProgrammingErrorExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  /**
   * Tests a valid login through the controller.
   */
  @Request
  @Test
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
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getOid());
      }
    }
  }

  /**
   * Tests a valid login through the controller.
   */
  @Request
  @Test
  public void testLoginAnonymous()
  {
    ClientSession anonymousSession = null;

    try
    {
      anonymousSession = this.createAnonymousSession();
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
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
  @Request
  @Test
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
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getOid());
      }
    }
  }

  @Request
  @Test
  public void testGetSessionUser()
  {
    MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(UserInfo.CLASS);
    MdAttributeDAOIF mdAttributeIF = mdBusinessIF.definesAttribute(UserInfo.USERNAME);

    clientRequest.grantTypePermission(tommyUser.getOid(), mdBusinessIF.getOid(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getOid(), mdAttributeIF.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    try
    {
      BusinessDTO userDTO = tommyProxy.getSessionUser();

      Assert.assertEquals("Tommy", userDTO.getValue(UserInfo.USERNAME));
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdBusinessIF.getOid(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeIF.getOid(), Operation.READ.name());
    }

  }

  /**
   * Log in an invalid user.
   */
  @Request
  @Test
  public void testLoginInvalid()
  {
    try
    {
      ClientSession.createUserSession("not_a_user", "not_a_password", new Locale[] { CommonProperties.getDefaultLocale() });
      Assert.fail("Server did not throw an exception for invalid username and password.");
    }
    catch (InvalidLoginExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail("Wrong exception for invalid login: " + e.getMessage());
    }
  }

  @Request
  @Test
  public void testEnumerationDTO()
  {
    try
    {
      BusinessDTO user = clientRequest.newBusiness(parentMdBusinessType);
      List<? extends BusinessDTO> suit = user.getEnumValues("anEnum");

      // make sure there is only one Locale
      Assert.assertEquals(suit.size(), 1);

      BusinessDTO locale = suit.get(0);

      // test the values on the locale. English is the default.
      Assert.assertEquals(locale.getOid(), diamonds.getOid());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testNoAccessorConversion()
  {
    BusinessDTO parent = null;

    try
    {
      parent = this.createParentInstance(clientRequest);
      String value = parent.getValue("aHiddenCharacter");

      Assert.assertTrue(value != null && !value.equals(""));
    }
    finally
    {
      if (parent != null)
      {
        clientRequest.delete(parent.getOid());
      }
    }
  }

  @Request
  @Test
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
          Assert.fail("A new instance did not store blob bytes correctly.");
        }
      }

      // create the instance and check the bytes
      clientRequest.createBusiness(instance);
      retVal = instance.getBlob("aBlob");
      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != retVal[i])
        {
          Assert.fail("An updated instance did not store the blob bytes correctly.");
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (instance != null && !instance.isNewInstance())
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getOid());
      }
    }
  }

  @Request
  @Test
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
      instance.addMultiItem(attributeName, term.getOid());
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getOid());

        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getOid());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getOid());
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getOid());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiReference").invoke(test);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getOid());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getOid());
    }
  }

  @Request
  @Test
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
      instance.addMultiItem(attributeName, term.getOid());
      clientRequest.createBusiness(instance);

      try
      {
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getOid());

        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getOid());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getOid());
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        BusinessDTO test = (BusinessDTO) clientRequest.get(instance.getOid());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiTerm").invoke(test);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
        clientRequest.lock(instance);
        clientRequest.delete(instance.getOid());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getOid());
    }
  }

  @Request
  @Test
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
        Assert.fail("The values for a created struct do not match the set values.");
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
        Assert.fail("The values for an updated struct do not match the set values.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (user != null)
      {
        clientRequest.lock(user);
        clientRequest.delete(user.getOid());
      }
    }
  }

  @Request
  @Test
  public void testTypeReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getOid(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_CHILD.name());
    }
  }

  @Request
  @Test
  public void testTypeReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getOid(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testInvalidTypeReadChildRelationshipsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read child relationships without READ_CHILD permissions.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());
    }
  }

  @Request
  @Test
  public void testTypeReadChildrenPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getOid(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_CHILD.name());
    }
  }

  @Request
  @Test
  public void testTypeReadChildrenPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getOid(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testInvalidTypeReadChildrenPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getChildren(parentInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read child relationships without READ_CHILD permissions.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());
    }
  }

  @Request
  @Test
  public void testTypeReadParentRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testTypeReadParentRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testInvalidTypeReadParentRelationshipsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParentRelationships(parentInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read parent relationships without READ_PARENT permissions.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());
    }
  }

  @Request
  @Test
  public void testTypeReadParentsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testTypeReadParentsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testInvalidTypeReadParentsPermissions()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(clientRequest);

    childInstance = clientRequest.newBusiness(childMdBusinessType);
    clientRequest.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    try
    {
      tommyProxy.getParents(parentInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read parent relationships without READ_PARENT permissions.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());
    }
  }

  @Request
  @Test
  public void testOwnerReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_CHILD.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getOid(), mdRelationshipType);
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ_CHILD permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_CHILD.name());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerReadChildRelationshipsPermissions1()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getChildRelationships(parentInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read child relationships without READ_CHILD permissions on owner.");
    }
    catch (ReadChildPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_CHILD.name());
    }
  }

  @Request
  @Test
  public void testOwnerReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.ADD_CHILD.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = tommyProxy.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    tommyProxy.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.ADD_CHILD.name());

    try
    {
      tommyProxy.get(relationshipDTO.getOid());
    }
    catch (ReadPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read child relationships with READ permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerReadChildRelationshipsPermissions2()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.ADD_CHILD.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    // Tommy is not the owner of the relationship instance
    RelationshipDTO relationshipDTO = clientRequest.addChild(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.ADD_CHILD.name());

    try
    {
      tommyProxy.get(relationshipDTO.getOid());
      Assert.fail("Able to read child relationships with READ permissions on owner, but user is not the owner.");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ.name());
    }
  }

  @Request
  @Test
  public void testOwnerReadParentRelationshipsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParentRelationships(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerReadParentRelationshipsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParentRelationships(childInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read parent relationships without READ_PARENT permissions on owner.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testOwnerReadParentsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParents(childInstance.getOid(), mdRelationshipType);
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      Assert.fail("Unable to read parent relationships with READ_PARENT permissions. \n" + e.getMessage());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testInvalidOwnerReadParentsPermissions()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    parentInstance = this.createParentInstance(tommyProxy);

    childInstance = tommyProxy.newBusiness(childMdBusinessType);
    tommyProxy.createBusiness(childInstance);

    RelationshipDTO relationshipDTO = clientRequest.addParent(parentInstance.getOid(), childInstance.getOid(), mdRelationshipType);
    clientRequest.createRelationship(relationshipDTO);

    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.CREATE.name());
    clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.CREATE.name());

    try
    {
      tommyProxy.getParents(childInstance.getOid(), mdRelationshipType);
      Assert.fail("Able to read parent relationships without READ_PARENT permissions on owner.");
    }
    catch (ReadParentPermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.deleteChild(relationshipDTO.getOid());
      clientRequest.delete(parentInstance.getOid());
      clientRequest.delete(childInstance.getOid());

      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, mdRelationship.getOid(), Operation.READ_PARENT.name());
    }
  }

  @Request
  @Test
  public void testGrantGrantTypePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.GRANT.name());

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
      tommyRequest.grantTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getOid());

      tommyRequest.revokeTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
      tommyRequest.revokeTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.GRANT.name());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
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

  @Request
  @Test
  public void testInvalidGrantGrantPermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
    }
    catch (GrantTypePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testInvalidGrantRevokePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;
    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
    }
    catch (RevokeTypePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantGrantAttributePermission()
  {
    BusinessDTO testObject = null;

    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    ClientSession billySession = null;
    ClientRequestIF billyRequest = null;

    try
    {
      clientRequest.grantTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      clientRequest.grantAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.GRANT.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributePermission(littleBillyTables.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());

      billySession = this.createSession("Billy", "Tables");
      billyRequest = getRequest(billySession);

      billyRequest.lock(testObject);
      testObject.setValue("refChar", "I am all that is man!");
      billyRequest.update(testObject);

      billyRequest.lock(testObject);
      billyRequest.delete(testObject.getOid());

      tommyRequest.revokeAttributePermission(littleBillyTables.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());

      clientRequest.revokeTypePermission(littleBillyTables.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.GRANT.name());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
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

  @Request
  @Test
  public void testInvalidGrantGrantAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.grantAttributePermission(littleBillyTables.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());
    }
    catch (GrantAttributePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testInvalidGrantRevokeAttributePermission()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyRequest = null;

    try
    {
      tommySession = this.createSession("Tommy", "music");
      tommyRequest = getRequest(tommySession);

      tommyRequest.revokeAttributePermission(littleBillyTables.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());
    }
    catch (RevokeAttributePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeReadWritePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name());
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      BusinessDTO businessDTO = (BusinessDTO) tommyProxy.get(testObject.getOid());

      // make sure the readable and writable flag is set to true
      if (!businessDTO.isReadable() || !businessDTO.isWritable())
      {
        Assert.fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.lock(testObject);
        tommyProxy.delete(testObject.getOid());
      }

      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeNoWritePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      BusinessDTO businessDTO = (BusinessDTO) tommyProxy.get(testObject.getOid());

      // make sure the readable and writable flag is set to true
      if (businessDTO.isWritable())
      {
        Assert.fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getOid());
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeNoReadPermission_DeveloperMessage()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.assignMember(tommyUser.getOid(), RoleDAOIF.DEVELOPER_ROLE);
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.get(testObject.getOid());

      Assert.fail("Read or Write permission on a type were not properly set in the DTO");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here
      // Make sure there is a developer message
      Assert.assertTrue(!e.getDeveloperMessage().equals(""));
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getOid());
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());
      clientRequest.removeMember(tommyUser.getOid(), RoleDAOIF.DEVELOPER_ROLE);

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeNoReadPermission_NoDeveloperMessage()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name());
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());      
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.get(testObject.getOid());

      Assert.fail("Read or Write permission on a type were not properly set in the DTO");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here
      // Make sure there is no developer message
      Assert.assertTrue(e.getDeveloperMessage().equals(""));
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      clientRequest.lock(testObject);
      clientRequest.delete(testObject.getOid());
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.WRITE.name());
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.DELETE.name());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }


  @Request
  @Test
  public void testGrantAttributePermission()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      clientRequest.grantAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypePermissionInvalid()
  {
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), "not_a_proper_permission");

      Assert.fail("An invalid type of permission was added to a user.");
    }
    catch (InvalidEnumerationNameDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testGrantTypePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      // delete the object
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantAttributePermissions()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.grantAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantAttributePermissionsInvalid()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      tommyProxy.lock(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      Assert.fail("User was able to update an attribute value without permission.");
    }
    catch (AttributeWritePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Throwable e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
    finally
    {
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());

      clientRequest.revokeTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypePermissionsInvalid()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.READ.name(), Operation.WRITE.name());

      testObject = this.createParentInstance(clientRequest);

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);
      // delete the object
      tommyProxy.lock(testObject);
      tommyProxy.delete(testObject.getOid());

      Assert.fail("A user was able to delete an object without permission.");
    }
    catch (DeletePermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (Throwable e)
    {
      Assert.fail(e.getLocalizedMessage());
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
      clientRequest.delete(testObject.getOid());
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testQueryUserExactMatch()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy");

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      Assert.fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getOid().equals(tommyUser.getOid()))
    {
      Assert.fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testQueryUserExactMatchComplicated()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.EQUALS, "Tommy");

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      Assert.fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getOid().equals(tommyUser.getOid()))
    {
      Assert.fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
  public void testQueryUserWildCard()
  {
    BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(testUserType);
    queryDTO.addCondition(UserInfo.USERNAME, QueryConditions.LIKE, "Tom" + QueryConditions.WILDCARD);

    List<BusinessDTO> users = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

    if (users.size() != 1)
    {
      Assert.fail("A query did not return the correct number of results.");
    }

    BusinessDTO user = users.get(0);
    if (!user.getOid().equals(tommyUser.getOid()))
    {
      Assert.fail("The user 'Tommy' couldn't be found with a query.");
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("queryLong", QueryConditions.LT, "101");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("queryLong", QueryConditions.GT_EQ, "100");
      queryDTO.addCondition("queryLong", QueryConditions.GT_EQ, "99");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.NOT_EQUALS, "facade query");
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      ViewDTO instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryChar", QueryConditions.NOT_EQUALS, "facade query");
      parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      BusinessDTO instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      parents = (List<BusinessDTO>) clientRequest.queryBusinesses(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getOid().equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      ViewDTO instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      parents = (List<ViewDTO>) clientRequest.queryViews(queryDTO).getResultSet();

      if (parents.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = parents.get(0);
      if (!instance.getValue("objectId").equals(parentInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.LT, "13:00:00");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "12:00:00");
      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "11:59:59");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relLong", QueryConditions.LT, "101");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relLong", QueryConditions.GT_EQ, "100");
      queryDTO.addCondition("relLong", QueryConditions.NOT_EQUALS, "123321");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("relChar", QueryConditions.NOT_EQUALS, "facade query");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test NOT_EQUALS
      queryDTO.addCondition("relBoolean", QueryConditions.NOT_EQUALS, MdAttributeBooleanInfo.FALSE);
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @SuppressWarnings("unchecked")
  @Request
  @Test
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
        Assert.fail("A query did not return the correct number of results.");
      }

      RelationshipDTO instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test LT
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.LT, "13:00:00");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }

      // test GT_EQ
      queryDTO.clearConditions();

      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "12:00:00");
      queryDTO.addCondition("relTime", QueryConditions.GT_EQ, "11:59:59");
      rels = (List<RelationshipDTO>) clientRequest.queryRelationships(queryDTO).getResultSet();

      if (rels.size() != 1)
      {
        Assert.fail("A query did not return the correct number of results.");
      }

      instance = rels.get(0);
      if (!instance.getOid().equals(relInstance.getOid()))
      {
        Assert.fail("A query did not find a proper match.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testBusinesObjectQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.READ.name());

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
      Assert.fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testViewObjectQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());

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
      Assert.fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testViewVirtualAttributeReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());
    clientRequest.grantAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewDTO testViewDTO = (ViewDTO) tommyProxy.newMutable(mdViewType);
      testViewDTO.setValue("aCharacter", "some value");
      tommyProxy.createSessionComponent(testViewDTO);

      ViewDTO viewDTO = (ViewDTO) tommyProxy.get(testViewDTO.getOid());

      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(viewDTO, "aCharacter");

      // make sure the readable and writable flag is set to true
      if (!attributeDTO.isReadable())
      {
        Assert.fail("Read permission on a virtual attribute failed.  Permission was set on the concrete attribute.");
      }
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      Assert.fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (ProblemExceptionDTO pe)
    {
      List<? extends ProblemDTOIF> problemList = pe.getProblems();

      for (ProblemDTOIF problemDTOIF : problemList)
      {
        Assert.fail(problemDTOIF.getMessage());
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());
      clientRequest.revokeAttributePermission(tommyUser.getOid(), mdAttributeCharacterDTO.getOid(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testViewVirtualAttributeN0ReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      ViewDTO testViewDTO = (ViewDTO) tommyProxy.newMutable(mdViewType);
      testViewDTO.setValue("aCharacter", "some value");
      tommyProxy.createSessionComponent(testViewDTO);

      ViewDTO viewDTO = (ViewDTO) tommyProxy.get(testViewDTO.getOid());

      AttributeDTO attributeDTO = ComponentDTOFacade.getAttributeDTO(viewDTO, "aCharacter");

      // make sure the readable and writable flag is set to true
      if (attributeDTO.isReadable())
      {
        Assert.fail("Read permission on a virtual attribute failed.  Permission was not set on the concrete attribute but the virtual attribute was readable.");
      }
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      Assert.fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (ProblemExceptionDTO pe)
    {
      List<? extends ProblemDTOIF> problemList = pe.getProblems();

      for (ProblemDTOIF problemDTOIF : problemList)
      {
        Assert.fail(problemDTOIF.getMessage());
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdView.getOid(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testBusinesObjectNoQueryReadPermission()
  {
    clientRequest.revokeTypePermission(tommyUser.getOid(), parentMdBusiness.getOid(), Operation.READ.name());

    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    createQueryInstances();
    try
    {
      BusinessQueryDTO queryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);

      // test EQUALS
      queryDTO.addCondition("queryBoolean", QueryConditions.EQUALS, MdAttributeBooleanInfo.TRUE);

      tommyProxy.queryBusinesses(queryDTO);

      Assert.fail("Able to perform a query on a type without adequate read permissions.");
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

  @Request
  @Test
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

      Assert.fail("Able to perform a query on a type without adequate read permissions.");
    }
    catch (ReadTypePermissionExceptionDTO e)
    {
      // This is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      destroyQueryInstances();
    }
  }

  @Request
  @Test
  public void testRelationshipQueryReadPermission()
  {
    clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());

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
      Assert.fail("Unable to perform a query on a type with adequate read permissions.");
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.READ.name());
      destroyQueryInstances();
    }
  }

  @Request
  @Test
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
      Assert.fail(e.getMessage());
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
  // @Request @Test public void testHashDTO()
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
  // Assert.fail(e.getMessage());
  // }
  //
  // // compare the hashes
  // if (hashDTO.encryptionEquals(manualHash, true) != true)
  // {
  // Assert.fail("HashDTO did not propery represent a hashed attribute (manual
  // hash).");
  // }
  //
  // if (hashDTO.encryptionEquals("music", false) != true)
  // {
  // Assert.fail("HashDTO did not propery represent a hashed attribute (auto
  // hash).");
  // }
  // }
  // catch (Exception e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // }

  /**
   * Checks if a DTO can correctly lock an object and have the value of
   * EntityDTO.isLockedByCurrentUser() return true if the object is locked by
   * the current user and false otherwise.
   */
  @Request
  @Test
  public void testCurrentUserLockValue()
  {
    BusinessDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), childMdBusiness.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(testObject);

      tommyProxy.lock(testObject);

      // make sure Tommy has the lock value set to true
      if (!testObject.isLockedByCurrentUser())
      {
        Assert.fail("The Tommy user is not marked as locking an object which it did.");
      }

      // now make sure the SYSTEM user has the lock value set to false
      // (since it
      // didn't lock it, Tommy did)
      BusinessDTO temp = (BusinessDTO) clientRequest.get(testObject.getOid());
      if (temp.isLockedByCurrentUser())
      {
        Assert.fail("The SYSTEM user is marked as locking an object which it did not.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);

    Assert.assertEquals(parentMdBusiness.getStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDisplayLabel());
    Assert.assertEquals(parentMdBusiness.getStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDescription());
    Assert.assertEquals(parentMdBusiness.getOid(), instance.getMd().getOid());
  }

  @Request
  @Test
  public void testRelationshipMetadata()
  {
    RelationshipDTO instance = (RelationshipDTO) clientRequest.newMutable(mdRelationshipType);

    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDisplayLabel());
    Assert.assertEquals(mdRelationship.getStructValue(MdRelationshipInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDescription());
    Assert.assertEquals(mdRelationship.getOid(), instance.getMd().getOid());

    Assert.assertEquals(parentMdBusinessType, instance.getMd().getParentMdBusiness());
    Assert.assertEquals(childMdBusinessType, instance.getMd().getChildMdBusiness());
  }

  @Request
  @Test
  public void testBooleanMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeBooleanMdDTO md = (AttributeBooleanMdDTO) ComponentDTOFacade.getAttributeDTO(instance, "aBoolean").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBooleanDTO, md);
    Assert.assertEquals(mdAttributeBooleanDTO.getStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getPositiveDisplayLabel());
    Assert.assertEquals(mdAttributeBooleanDTO.getStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getNegativeDisplayLabel());
  }

  @Request
  @Test
  public void testBlobMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aBlob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBlobDTO, md);
  }

  @Request
  @Test
  public void testReferenceMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeReferenceMdDTO md = ComponentDTOFacade.getAttributeReferenceDTO(instance, "aReference").getAttributeMdDTO();

    checkAttributeMd(mdAttributeReferenceDTO, md);

    Assert.assertEquals(md.getReferencedMdBusiness(), refType);
  }

  @Request
  @Test
  public void testTermMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeTermMdDTO md = ComponentDTOFacade.getAttributeTermDTO(instance, "aTerm").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTermDTO, md);

    Assert.assertEquals(md.getReferencedMdBusiness(), termType);
  }

  @Request
  @Test
  public void testMultiReferenceMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMultiReferenceMdDTO md = ComponentDTOFacade.getAttributeMultiReferenceDTO(instance, "aMultiReference").getAttributeMdDTO();

    checkAttributeMd(mdAttributeMultiReferenceDTO, md);

    Assert.assertEquals(md.getReferencedMdBusiness(), termType);
  }

  @Request
  @Test
  public void testMultiTermMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMultiTermMdDTO md = ComponentDTOFacade.getAttributeMultiTermDTO(instance, "aMultiTerm").getAttributeMdDTO();

    checkAttributeMd(mdAttributeMultiTermDTO, md);

    Assert.assertEquals(md.getReferencedMdBusiness(), termType);
  }

  @Request
  @Test
  public void testDateTimeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDateTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateTimeDTO, md);
  }

  @Request
  @Test
  public void testDateMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDate").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateDTO, md);
  }

  @Request
  @Test
  public void testTimeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTimeDTO, md);
  }

  @Request
  @Test
  public void testIdMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = instance.getOidMd();

    Assert.assertEquals(md.getName(), EntityInfo.OID);
    Assert.assertEquals(md.isRequired(), true);
    Assert.assertEquals(md.isSystem(), true);
  }

  @Request
  @Test
  public void testTypeMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = instance.getTypeMd();

    Assert.assertEquals(md.getName(), EntityInfo.TYPE);
    Assert.assertEquals(md.isRequired(), true);
    Assert.assertEquals(md.isSystem(), true);
  }

  @Request
  @Test
  public void testIntegerMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "anInteger").getAttributeMdDTO();

    checkAttributeMd(mdAttributeIntegerDTO, md);
  }

  @Request
  @Test
  public void testLongMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "aLong").getAttributeMdDTO();

    checkAttributeMd(mdAttributeLongDTO, md);
  }

  @Request
  @Test
  public void testDecimalMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDecimal").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDecimalDTO, md);
  }

  @Request
  @Test
  public void testDoubleMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDouble").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDoubleDTO, md);
  }

  @Request
  @Test
  public void testFloatMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aFloat").getAttributeMdDTO();

    checkAttributeMd(mdAttributeFloatDTO, md);
  }

  @Request
  @Test
  public void testTextMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aText").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTextDTO, md);
  }

  @Request
  @Test
  public void testClobMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aClob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeClobDTO, md);
  }

  @Request
  @Test
  public void testCharacterMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeCharacterMdDTO md = ComponentDTOFacade.getAttributeCharacterDTO(instance, "aCharacter").getAttributeMdDTO();

    checkAttributeMd(mdAttributeCharacterDTO, md);
    Assert.assertEquals(Integer.parseInt(mdAttributeCharacterDTO.getValue(MdAttributeCharacterInfo.SIZE)), md.getSize());
  }

  @Request
  @Test
  public void testStructMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeStructMdDTO md = ComponentDTOFacade.getAttributeStructDTO(instance, "aStruct").getAttributeMdDTO();

    checkAttributeMd(mdAttributeStructDTO, md);
    Assert.assertEquals(EntityTypes.PHONE_NUMBER.getType(), md.getDefiningMdStruct());
  }

  @Request
  @Test
  public void testEnumerationMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEnumerationMdDTO md = ComponentDTOFacade.getAttributeEnumerationDTO(instance, "anEnum").getAttributeMdDTO();

    checkAttributeMd(mdAttributeEnumerationDTO, md);
    Assert.assertEquals(Boolean.parseBoolean(mdAttributeEnumerationDTO.getValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE)), md.selectMultiple());
    Assert.assertEquals("anEnum", md.getName());
    Assert.assertEquals(suitMdEnumerationType, md.getReferencedMdEnumeration());
    Assert.assertEquals(suitMdEnumerationType + TypeGeneratorInfo.DTO_SUFFIX, md.getJavaType().getName());

    // check all enum values
    Map<String, String> enumNameMap = md.getEnumItems();
    for (BusinessDAO suit : suits)
    {
      String expectedName = suit.getValue(EnumerationMasterInfo.NAME);
      if (!enumNameMap.containsKey(expectedName))
      {
        Assert.fail("The enumeration values on the enumeration metadata was incorrect.");
      }
    }
  }

  @Request
  @Test
  public void testHashMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeHashDTO(instance, "aHash").getAttributeMdDTO();

    checkAttributeMd(mdAttributeHashDTO, md);
    Assert.assertEquals(HashMethods.MD5.getMessageDigest(), md.getEncryptionMethod());
  }

  @Request
  @Test
  public void testSymmetricMetadata()
  {
    BusinessDTO instance = clientRequest.newBusiness(parentMdBusinessType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeSymmetricDTO(instance, "aSym").getAttributeMdDTO();

    checkAttributeMd(mdAttributeSymmetricDTO, md);
    Assert.assertEquals(SymmetricMethods.DES.getTransformation(), md.getEncryptionMethod());
  }

  @Request
  @Test
  public void testNewVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    InputStream stream = clientRequest.getSecureFile(fileDTO.getOid());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        Assert.assertEquals(bytes1.read(), bytes2.read());
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      Assert.fail(e.getLocalizedMessage());
    }
  }

  @Request
  @Test
  public void testGetVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getOid());
    clientRequest.createBusiness(businessDTO);

    try
    {
      // Load file into the client file cache
      clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getOid()).close();
    }
    catch (IOException e1)
    {
      Assert.fail(e1.getMessage());
    }

    InputStream stream = clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getOid());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        Assert.assertEquals(bytes1.read(), bytes2.read());
        i++;
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      Assert.fail(e.getLocalizedMessage());
    }

    Assert.assertEquals(i, bytes.length);
  }

  @Request
  @Test
  public void testGetVaultFileNotCached()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getOid());
    clientRequest.createBusiness(businessDTO);

    InputStream stream = clientRequest.getSecureFile("aFile", businessDTO.getType(), fileDTO.getOid());

    // Ensure that the file is the same
    BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
    BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));
    int i = 0;

    try
    {
      while (bytes1.ready() || bytes2.ready())
      {
        Assert.assertEquals(bytes1.read(), bytes2.read());
        i++;
      }

      bytes1.close();
      bytes2.close();
    }
    catch (IOException e)
    {
      Assert.fail(e.getLocalizedMessage());
    }

    Assert.assertEquals(i, bytes.length);

  }

  @Request
  @Test
  public void testNoPermissionGetVaultFile()
  {
    ClientSession tommySession = this.createSession("Tommy", "music");
    ClientRequestIF tommyProxy = getRequest(tommySession);

    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));

    BusinessDTO businessDTO = this.initParentInstance();
    businessDTO.setValue("aFile", fileDTO.getOid());
    clientRequest.createBusiness(businessDTO);

    try
    {
      tommyProxy.getSecureFile("aFile", businessDTO.getType(), businessDTO.getOid());

      Assert.fail("Able to retrieve a cached secure file without permissions");
    }
    catch (ReadPermissionExceptionDTO e)
    {
      // We want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
    }
  }

  @Request
  @Test
  public void testDeleteVaultFile()
  {
    BusinessDTO fileDTO = clientRequest.newSecureFile("file", "txt", new ByteArrayInputStream(bytes));
    String fileId = fileDTO.getOid();

    clientRequest.delete(fileId);

    try
    {
      clientRequest.getSecureFile(fileId);
      Assert.fail("Able to retrieve a file that should have been deleted");
    }
    catch (DataNotFoundExceptionDTO e)
    {
      // we want to land here.
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testNewFile()
  {
    // FIXME fix for web services
    // if (! ( clientRequest instanceof WebServiceClientRequest ))
    {
      BusinessDTO fileDTO = clientRequest.newFile("test/", "file", "txt", new ByteArrayInputStream(bytes));

      try
      {
        InputStream stream = clientRequest.getFile(fileDTO.getOid());

        // Ensure that the file is the same
        BufferedReader bytes1 = new BufferedReader(new InputStreamReader(stream));
        BufferedReader bytes2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(bytes)));

        try
        {
          while (bytes1.ready() || bytes2.ready())
          {
            Assert.assertEquals(bytes1.read(), bytes2.read());
          }

          bytes1.close();
          bytes2.close();
        }
        catch (IOException e)
        {
          Assert.fail(e.getLocalizedMessage());
        }
      }
      finally
      {
        clientRequest.delete(fileDTO.getOid());
      }
    }
  }

  @Request
  @Test
  public void testDeleteFile()
  {
    // FIXME fix for web services
    // if (! ( clientRequest instanceof WebServiceClientRequest ))
    {
      BusinessDTO fileDTO = clientRequest.newFile("test/", "file", "txt", new ByteArrayInputStream(bytes));
      String fileId = fileDTO.getOid();

      clientRequest.delete(fileId);

      try
      {
        clientRequest.getSecureFile(fileId);
        Assert.fail("Able to retrieve a file that should have been deleted");
      }
      catch (DataNotFoundExceptionDTO e)
      {
        // we want to land here.
      }
      catch (Throwable e)
      {
        Assert.fail(e.getMessage());
      }
    }
  }

  /**
   * This method does all the checks for attribute metadata on DTOs. All checks,
   * except for attribute specific metadata is consolidated here (it's better
   * than copying/pasting the same checks into a dozen different tests).
   * 
   * @param mdAttribute
   * @param mdDTO
   */
  protected static void checkAttributeMd(MdAttributeDAOIF mdAttribute, AttributeMdDTO md)
  {
    Assert.assertEquals(mdAttribute.getStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getDisplayLabel());
    Assert.assertEquals(mdAttribute.getStructValue(MdAttributeConcreteInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), md.getDescription());
    Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.REQUIRED)), md.isRequired());
    Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.IMMUTABLE)), md.isImmutable());
    Assert.assertEquals(mdAttribute.getOid(), md.getOid());
    Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeConcreteInfo.SYSTEM)), md.isSystem());
    Assert.assertEquals(mdAttribute.getValue(MdAttributeConcreteInfo.NAME), md.getName());

    if (md instanceof AttributeNumberMdDTO)
    {
      AttributeNumberMdDTO numberMdDTO = (AttributeNumberMdDTO) md;

      Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_ZERO)), numberMdDTO.rejectZero());
      Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_NEGATIVE)), numberMdDTO.rejectNegative());
      Assert.assertEquals(Boolean.parseBoolean(mdAttribute.getValue(MdAttributeNumberInfo.REJECT_POSITIVE)), numberMdDTO.rejectPositive());
    }

    if (md instanceof AttributeDecMdDTO)
    {
      AttributeDecMdDTO decMdDTO = (AttributeDecMdDTO) md;

      Assert.assertEquals(Integer.parseInt(mdAttribute.getValue(MdAttributeDecInfo.LENGTH)), decMdDTO.getTotalLength());
      Assert.assertEquals(Integer.parseInt(mdAttribute.getValue(MdAttributeDecInfo.DECIMAL)), decMdDTO.getDecimalLength());
    }

  }

  /**
   *
   */
  @Request
  @Test
  public void testDirectionalDeleteChild()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.DELETE_CHILD.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteChild(relationshipDTO.getOid());
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getOid());
      clientRequest.delete(child.getOid());
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.DELETE_CHILD.name());
    }
  }

  @Request
  @Test
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

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteChild(relationshipDTO.getOid());

      Assert.fail("The user Tommy does not have permission to delete a child");
    }
    catch (DeleteChildPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getOid());
      clientRequest.delete(child.getOid());
    }
  }

  @Request
  @Test
  public void testDirectionalDeleteParent()
  {
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    BusinessDTO parent = null;
    BusinessDTO child = null;
    try
    {
      clientRequest.grantTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.DELETE_PARENT.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      parent = this.createParentInstance(clientRequest);

      child = clientRequest.newBusiness(childMdBusinessType);
      clientRequest.createBusiness(child);

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteParent(relationshipDTO.getOid());
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      if (parent != null)
      {
        clientRequest.delete(parent.getOid());
      }
      if (child != null)
      {
        clientRequest.delete(child.getOid());
      }
      clientRequest.revokeTypePermission(tommyUser.getOid(), mdRelationship.getOid(), Operation.DELETE_PARENT.name());
    }
  }

  @Request
  @Test
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

      RelationshipDTO relationshipDTO = clientRequest.addChild(parent.getOid(), child.getOid(), mdRelationshipType);
      clientRequest.createRelationship(relationshipDTO);

      tommyProxy.deleteParent(relationshipDTO.getOid());

      Assert.fail("The user Tommy does not have permission to delete a child");
    }
    catch (DeleteParentPermissionExceptionDTO e)
    {
      // we want to land here
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.delete(parent.getOid());
      clientRequest.delete(child.getOid());
    }
  }

  /**
   * Makes sure that a queryDTO for a given type returns the attributes that the
   * query type defines.
   */
  @Request
  @Test
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

  @Request
  @Test
  public void testToString()
  {
    BusinessDTO businessDTO = null;

    try
    {
      businessDTO = clientRequest.newBusiness(parentMdBusinessType);

      String expectedToString = toStringPrepend + businessDTO.getOid();
      String toString = businessDTO.toString();

      if (!expectedToString.equals(toString))
      {
        Assert.fail("The toString() value on an EntityDTO is incorrect.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
  }

  // TODO enable once the state machines have been fixed (bugzilla task for
  // naifeh)
  // @Request @Test public void test_StateInfoOnBusinessDTO()
  // {
  // BusinessDTO instance = clientRequest.newBusiness(childMdBusinessType);
  // clientRequest.createBusiness(instance);
  //
  // Assert.assertEquals(instance.getState(),
  // state1.getValue(StateMasterIF.STATE_NAME));
  //
  // List<String> transitions = instance.getTransitions();
  // Assert.assertEquals(transitions.size(), 2);
  //
  // String tranName1 = transition.getValue(StateMasterIF.TRANSITION_NAME);
  // String tranName2 = transition2.getValue(StateMasterIF.TRANSITION_NAME);
  //
  // String tran1 = transitions.get(0);
  // String tran2 = transitions.get(1);
  //
  // if (!tran1.equals(tranName1) && !tran1.equals(tranName2))
  // {
  // Assert.fail("Invalid transistion information was returned");
  // }
  //
  // if (!tran2.equals(tranName1) && !tran2.equals(tranName2))
  // {
  // Assert.fail("Invalid transistion information was returned");
  // }
  //
  // if (tran1.equals(tran2))
  // {
  // Assert.fail("Invalid transistion information was returned");
  // }
  // }

  @Request
  @Test
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

    Assert.assertEquals(queryDTO.isCountEnabled(), true);
    Assert.assertEquals(queryDTO.getCount(), 10);
    Assert.assertEquals(queryDTO.getPageNumber(), 2);
    Assert.assertEquals(queryDTO.getPageSize(), 3);
    Assert.assertEquals(queryDTO.getResultSet().size(), 3);
  }

  @Request
  @Test
  public void testStructQueryDTO()
  {
    StructQueryDTO queryDTO = (StructQueryDTO) clientRequest.getQuery(structType);

    AttributeCharacterDTO charDTO = (AttributeCharacterDTO) queryDTO.getAttributeDTO(structMdAttributeCharDTO.getValue(MdAttributeCharacterInfo.NAME));
    Assert.assertNotNull(charDTO);

    Assert.assertEquals(queryDTO.getType(), structType);
  }

  @Request
  @Test
  public void testRelationshipQueryDTO()
  {
    RelationshipQueryDTO queryDTO = (RelationshipQueryDTO) clientRequest.getQuery(mdRelationshipType);

    AttributeLongDTO longDTO = (AttributeLongDTO) queryDTO.getAttributeDTO(relMdAttributeLongDTO.getValue(MdAttributeCharacterInfo.NAME));
    Assert.assertNotNull(longDTO);
    Assert.assertEquals(longDTO.getValue(), "123321"); // test the default value

    Assert.assertEquals(queryDTO.getParentMdBusiness(), parentMdBusinessType);
    Assert.assertEquals(queryDTO.getChildMdBusiness(), childMdBusinessType);
    Assert.assertEquals(queryDTO.getType(), mdRelationshipType);
  }

  @Request
  @Test
  public void testBusinessQueryDTO()
  {
    BusinessQueryDTO parentQueryDTO = (BusinessQueryDTO) clientRequest.getQuery(parentMdBusinessType);
    List<TypeInMdRelationshipAsParent> asParents = parentQueryDTO.getTypeInMdRelationshipAsParentList();
    Assert.assertEquals(asParents.size(), 1);

    TypeInMdRelationshipAsParent asParent = asParents.get(0);
    Assert.assertEquals(asParent.getParentDisplayLabel(), mdRelationship.getStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(asParent.getRelationshipType(), mdRelationshipType);

    BusinessQueryDTO childQueryDTO = (BusinessQueryDTO) clientRequest.getQuery(childMdBusinessType);
    List<TypeInMdRelationshipAsChild> asChilds = childQueryDTO.getTypeInMdRelationshipAsChildList();
    Assert.assertEquals(asChilds.size(), 1);

    TypeInMdRelationshipAsChild asChild = asChilds.get(0);
    Assert.assertEquals(asChild.getChildDisplayLabel(), mdRelationship.getStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE));
    Assert.assertEquals(asChild.getRelationshipType(), mdRelationshipType);
  }

  @Request
  @Test
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

      Assert.assertEquals(1, resultSet.size());
      clientRequest.delete(resultSet.get(0).getOid());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Request
  @Test
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
      Assert.fail(e.getMessage());
    }
    catch (ImportDomainExecuteExceptionDTO e)
    {
      // This is expected
    }
    catch (ServerSideException e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      tommySession.logout();
    }
  }

  @Request
  @Test
  public void testAccessAdminValid()
  {
    try
    {
      clientRequest.checkAdminScreenAccess();
    }
    catch (AdminScreenAccessExceptionDTO e)
    {
      String error = "The SYSTEM user failed to access the admin screen.";
      Assert.fail(error);
    }
  }

  @Request
  @Test
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

  @Request
  @Test
  public void testProxyLoggedIn()
  {
    ClientSession anonymousSession = this.createAnonymousSession();
    ClientRequestIF publicProxy = getRequest(anonymousSession);

    ClientSession userSession = null;
    ClientRequestIF userProxy = null;

    try
    {
      Assert.assertEquals(true, publicProxy.isLoggedIn());
      Assert.assertEquals(true, publicProxy.isPublicUser());

      anonymousSession.changeLogin("Tommy", "music");

      Assert.assertEquals(true, publicProxy.isLoggedIn());
      Assert.assertEquals(false, publicProxy.isPublicUser());

      anonymousSession.logout();

      userSession = this.createSession("Tommy", "music");
      userProxy = getRequest(userSession);

      Assert.assertEquals(false, publicProxy.isLoggedIn());
      Assert.assertEquals(false, publicProxy.isPublicUser());

      Assert.assertEquals(true, userProxy.isLoggedIn());
      Assert.assertEquals(false, userProxy.isPublicUser());

      userSession.logout();

      Assert.assertEquals(false, publicProxy.isLoggedIn());
      Assert.assertEquals(false, publicProxy.isPublicUser());
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
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

  /**
   * Creates a valid instance of a type.
   */
  @Request
  @Test
  public void testNewDisconnectedEntity()
  {
    BusinessDTO user = clientRequest.newBusiness(testUserType);
    user.setValue(UserInfo.USERNAME, "Test");
    user.setValue(UserInfo.PASSWORD, "Test");
    clientRequest.createBusiness(user);

    try
    {
      clientRequest.grantTypePermission(user.getOid(), parentMdBusiness.getOid(), Operation.READ.name());
      clientRequest.grantTypePermission(user.getOid(), parentMdBusiness.getOid(), Operation.READ_ALL.name());

      ClientSession session = this.createSession("Test", "Test");

      try
      {
        ClientRequestIF request = this.getRequest(session);

        EntityDTO entityDTO = request.newDisconnectedEntity(parentMdBusinessType);

        Assert.assertEquals(parentMdBusinessType, entityDTO.getType());

        // Ensure that the disconnected flag is set to true
        Assert.assertTrue(entityDTO.isDisconnected());

        // Ensure the the user can set values
        entityDTO.setValue("aBoolean", MdAttributeBooleanInfo.TRUE);

        // Ensure that the user cannot apply the object
        try
        {
          request.createBusiness((BusinessDTO) entityDTO);
        }
        catch (CreatePermissionExceptionDTO e)
        {
          // This is expected
        }
      }
      finally
      {
        if (session != null)
        {
          session.logout();
        }
      }
    }
    finally
    {
      clientRequest.delete(user.getOid());
    }
  }

}
