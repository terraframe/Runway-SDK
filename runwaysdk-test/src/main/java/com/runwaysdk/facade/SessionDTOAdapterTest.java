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

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.ProblemExceptionDTO;
import com.runwaysdk.business.AttributeProblemDTO;
import com.runwaysdk.business.BusinessDTO;
import com.runwaysdk.business.ComponentDTOFacade;
import com.runwaysdk.business.InvalidEnumerationNameDTO;
import com.runwaysdk.business.MutableDTO;
import com.runwaysdk.business.ProblemDTOIF;
import com.runwaysdk.business.SessionDTO;
import com.runwaysdk.business.rbac.Operation;
import com.runwaysdk.business.rbac.RoleDAOIF;
import com.runwaysdk.business.rbac.UserDAO;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
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
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdSessionInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.MetadataInfo;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.UserInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.ProgrammingErrorExceptionDTO;
import com.runwaysdk.dataaccess.cache.DataNotFoundExceptionDTO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDAO;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdSessionDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.session.AttributeReadPermissionExceptionDTO;
import com.runwaysdk.session.DeletePermissionExceptionDTO;
import com.runwaysdk.session.ReadPermissionExceptionDTO;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeReferenceMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;

public abstract class SessionDTOAdapterTest
{
  protected static String                       label;

  protected static ClientSession                systemSession;

  protected static ClientRequestIF              clientRequest                = null;

  protected static String                       pack                         = "com.test.controller";

  protected static BusinessDAO                  newUser                      = null;

  protected static MdSessionDAO                 childMdSession               = null;

  protected static String                       childMdSessionTypeName       = "ChildTest";

  protected static String                       childMdSessionType           = null;

  protected static MdSessionDAO                 parentMdSession              = null;

  protected static String                       parentMdSessionTypeName      = "ParentTest";

  protected static String                       parentMdSessionType          = null;

  protected static SessionDTO                   childInstance                = null;

  protected static SessionDTO                   parentInstance               = null;

  protected static MdAttributeCharacterDAO      mdAttributeCharacterDTO_2    = null;

  protected static MdAttributeBlobDAO           mdAttributeBlobDTO           = null;

  protected static MdAttributeBooleanDAO        mdAttributeBooleanDTO        = null;

  protected static MdAttributeIntegerDAO        mdAttributeIntegerDTO        = null;

  protected static MdAttributeLongDAO           mdAttributeLongDTO           = null;

  protected static MdAttributeDecimalDAO        mdAttributeDecimalDTO        = null;

  protected static MdAttributeDoubleDAO         mdAttributeDoubleDTO         = null;

  protected static MdAttributeFloatDAO          mdAttributeFloatDTO          = null;

  protected static MdAttributeDateTimeDAO       mdAttributeDateTimeDTO       = null;

  protected static MdAttributeFileDAO           mdAttributeFileDTO           = null;

  protected static MdAttributeDateDAO           mdAttributeDateDTO           = null;

  protected static MdAttributeTimeDAO           mdAttributeTimeDTO           = null;

  protected static MdBusinessDAO                suitMaster                   = null;

  protected static String                       suitMasterTypeName           = "SuitMaster";

  protected static MdStructDAO                  structMdBusiness             = null;

  protected static String                       structMdBusinessTypeName     = "StructTest";

  protected static String                       structType                   = null;

  protected static MdAttributeCharacterDAO      structMdAttributeCharDTO     = null;

  protected static MdEnumerationDAO             suitMdEnumeration            = null;

  protected static String                       suitMdEnumerationTypeName    = "SuitEnum";

  protected static String                       suitMdEnumerationType        = null;

  protected static MdAttributeCharacterDAO      enumMdAttribute              = null;

  protected static MdAttributeEnumerationDAO    mdAttributeEnumerationDTO    = null;

  protected static MdAttributeMultiReferenceDAO mdAttributeMultiReferenceDTO = null;

  protected static MdAttributeMultiTermDAO      mdAttributeMultiTermDTO      = null;

  protected static MdAttributeStructDAO         mdAttributeStructDTO         = null;

  protected static MdBusinessDAO                refClass                     = null;

  protected static String                       refClassTypeName             = "RefClass";

  protected static String                       refType                      = null;

  protected static MdTermDAO                    termClass                    = null;

  protected static String                       termClassTypeName            = "TermClass";

  protected static String                       termType                     = null;

  protected static MdAttributeReferenceDAO      mdAttributeReferenceDTO      = null;

  protected static MdAttributeHashDAO           mdAttributeHashDTO           = null;

  protected static MdAttributeSymmetricDAO      mdAttributeSymmetricDTO      = null;

  protected static MdAttributeCharacterDAO      mdAttributeCharacterDTO      = null;

  protected static MdAttributeTextDAO           mdAttributeTextDTO           = null;

  protected static MdAttributeTextDAO           mdAttributeClobDTO           = null;

  protected static BusinessDAO                  hearts                       = null;

  protected static BusinessDAO                  clubs                        = null;

  protected static BusinessDAO                  spades                       = null;

  protected static BusinessDAO                  diamonds                     = null;

  protected static List<BusinessDAO>            suits                        = new LinkedList<BusinessDAO>();

  protected static final String                 toStringPrepend              = "Test toString: ";

  protected static final byte[]                 bytes                        = { 1, 2, 3, 4 };

  protected static final byte[]                 bytes2                       = { 2, 3, 4, 5, 6 };

  protected static String                       source;

  protected ClientSession createAnonymousSession()
  {
    return ClientSession.createAnonymousSession(label, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientSession createSession(String userName, String password)
  {
    return ClientSession.createUserSession(label, userName, password, new Locale[] { CommonProperties.getDefaultLocale() });
  }

  protected ClientRequestIF getRequest(ClientSession clientSession)
  {
    return clientSession.getRequest();
  }

  @Request
  public static void modelSetUp()
  {
    suitMaster = MdBusinessDAO.newInstance();
    suitMaster.setValue(MdBusinessInfo.NAME, suitMasterTypeName);
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    suitMaster.apply();

    suitMdEnumeration = MdEnumerationDAO.newInstance();
    suitMdEnumeration.setValue(MdEnumerationInfo.NAME, suitMdEnumerationTypeName);
    suitMdEnumeration.setValue(MdEnumerationInfo.PACKAGE, pack);
    suitMdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration");
    suitMdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    suitMdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getOid());
    suitMdEnumeration.apply();

    suitMdEnumerationType = pack + "." + suitMdEnumerationTypeName;

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
    structMdBusiness.setValue(MdStructInfo.NAME, structMdBusinessTypeName);
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

    childMdSession.setValue(MdSessionInfo.NAME, childMdSessionTypeName);
    childMdSession.setValue(MdSessionInfo.PACKAGE, pack);
    childMdSession.setValue(MdSessionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childMdSession.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test child");
    childMdSession.setStructValue(MdSessionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    childMdSession.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childMdSession.setValue(MdSessionInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    childMdSession.apply();

    newUser = UserDAO.newInstance();
    newUser.setValue(UserInfo.USERNAME, "Tommy");
    newUser.setValue(UserInfo.PASSWORD, "music");
    newUser.apply();

    mdAttributeCharacterDTO_2 = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacterDTO_2.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacterDTO_2.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, childMdSession.getOid());
    mdAttributeCharacterDTO_2.apply();

    parentMdSession.setValue(MdSessionInfo.NAME, parentMdSessionTypeName);
    parentMdSession.setValue(MdSessionInfo.PACKAGE, pack);
    parentMdSession.setValue(MdSessionInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMdSession.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A simple test parent");
    parentMdSession.setStructValue(MdSessionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "temporary junit test object");
    parentMdSession.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMdSession.setValue(MdSessionInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMdSession.setValue(MdSessionInfo.STUB_SOURCE, source);
    parentMdSession.apply();

    // add an MdAttributeBlob to the new type
    mdAttributeBlobDTO = MdAttributeBlobDAO.newInstance();
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.NAME, "aBlob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "some blob");
    mdAttributeBlobDTO.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob desc");
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlobDTO.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeBlobDTO.apply();

    mdAttributeBooleanDTO = MdAttributeBooleanDAO.newInstance();
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBooleanDTO.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeBooleanDTO.apply();

    mdAttributeCharacterDTO = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    mdAttributeCharacterDTO.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character desc");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacterDTO.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeCharacterDTO.apply();

    mdAttributeDecimalDTO = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.NAME, "aDecimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    mdAttributeDecimalDTO.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal desc");
    mdAttributeDecimalDTO.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
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
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDoubleDTO.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDoubleDTO.apply();

    mdAttributeEnumerationDTO = MdAttributeEnumerationDAO.newInstance();
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.NAME, "anEnum");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated Attribute");
    mdAttributeEnumerationDTO.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated desc");
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeEnumerationDTO.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitMdEnumeration.getOid());
    mdAttributeEnumerationDTO.apply();

    mdAttributeFloatDTO = MdAttributeFloatDAO.newInstance();
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.NAME, "aFloat");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    mdAttributeFloatDTO.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float Desc");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.LENGTH, "16");
    mdAttributeFloatDTO.setValue(MdAttributeFloatInfo.DECIMAL, "4");
    mdAttributeFloatDTO.apply();

    mdAttributeHashDTO = MdAttributeHashDAO.newInstance();
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.NAME, "aHash");
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hashed Attributed");
    mdAttributeHashDTO.setStructValue(MdAttributeHashInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hash Desc");
    mdAttributeHashDTO.addItem(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getOid());
    mdAttributeHashDTO.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeHashDTO.apply();

    mdAttributeIntegerDTO = MdAttributeIntegerDAO.newInstance();
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.NAME, "anInteger");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    mdAttributeIntegerDTO.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer Desc");
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeIntegerDTO.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeIntegerDTO.apply();

    mdAttributeLongDTO = MdAttributeLongDAO.newInstance();
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.NAME, "aLong");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    mdAttributeLongDTO.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long Desc");
    mdAttributeLongDTO.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
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
    mdAttributeDateDTO.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeDateDTO.apply();

    mdAttributeDateTimeDTO = MdAttributeDateTimeDAO.newInstance();
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.NAME, "aDateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    mdAttributeDateTimeDTO.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime Desc");
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTimeDTO.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeDateTimeDTO.apply();

    mdAttributeFileDTO = MdAttributeFileDAO.newInstance();
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.NAME, "aFile");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File");
    mdAttributeFileDTO.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File Desc");
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFileDTO.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeFileDTO.apply();

    mdAttributeTimeDTO = MdAttributeTimeDAO.newInstance();
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.NAME, "aTime");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    mdAttributeTimeDTO.setStructValue(MdAttributeTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time Desc");
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTimeDTO.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeTimeDTO.apply();

    // class for ref object attribute
    refClass = MdBusinessDAO.newInstance();
    refClass.setValue(MdBusinessInfo.NAME, refClassTypeName);
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
    mdAttributeReferenceDTO.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
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

    mdAttributeMultiReferenceDTO = MdAttributeMultiReferenceDAO.newInstance();
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.NAME, "aMultiReference");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiReferenceDTO.setStructValue(MdAttributeMultiReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeMultiReferenceDTO.setValue(MdAttributeMultiReferenceInfo.REF_MD_ENTITY, termClass.getOid());
    mdAttributeMultiReferenceDTO.apply();

    mdAttributeMultiTermDTO = MdAttributeMultiTermDAO.newInstance();
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.NAME, "aMultiTerm");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference Attribute");
    mdAttributeMultiTermDTO.setStructValue(MdAttributeMultiTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A multi reference desc");
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeMultiTermDTO.setValue(MdAttributeMultiTermInfo.REF_MD_ENTITY, termClass.getOid());
    mdAttributeMultiTermDTO.apply();

    mdAttributeStructDTO = MdAttributeStructDAO.newInstance();
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.NAME, "aStruct");
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    mdAttributeStructDTO.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Desc");
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.MD_STRUCT, EntityTypes.PHONE_NUMBER.getOid());
    mdAttributeStructDTO.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeStructDTO.apply();

    mdAttributeSymmetricDTO = MdAttributeSymmetricDAO.newInstance();
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.NAME, "aSym");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Attribute");
    mdAttributeSymmetricDTO.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Desc");
    mdAttributeSymmetricDTO.addItem(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getOid());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetricDTO.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeSymmetricDTO.apply();

    mdAttributeTextDTO = MdAttributeTextDAO.newInstance();
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.NAME, "aText");
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeTextDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text Desc");
    mdAttributeTextDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeTextDTO.apply();

    mdAttributeClobDTO = MdAttributeTextDAO.newInstance();
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.NAME, "aClob");
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    mdAttributeClobDTO.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob Desc");
    mdAttributeClobDTO.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, parentMdSession.getOid());
    mdAttributeClobDTO.apply();

    childMdSessionType = childMdSession.definesType();
    parentMdSessionType = parentMdSession.definesType();
    refType = refClass.definesType();
    termType = termClass.definesType();
    structType = structMdBusiness.definesType();
  }

  @Request
  public static void finalizeSetup()
  {
    hearts = BusinessDAO.newInstance(suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME));
    hearts.setValue("refChar", "Some other string");
    hearts.setValue(EnumerationMasterInfo.NAME, "HEARTS");
    hearts.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    hearts.apply();

    clubs = BusinessDAO.newInstance(suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME));
    clubs.setValue("refChar", "Some other string: Clubs");
    clubs.setValue(EnumerationMasterInfo.NAME, "CLUBS");
    clubs.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    clubs.apply();

    spades = BusinessDAO.newInstance(suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME));
    spades.setValue("refChar", "Some other string: Spades");
    spades.setValue(EnumerationMasterInfo.NAME, "SPADES");
    spades.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Spades");
    spades.apply();

    diamonds = BusinessDAO.newInstance(suitMaster.getValue(MdTypeInfo.PACKAGE) + "." + suitMaster.getValue(MdTypeInfo.NAME));
    diamonds.setValue("refChar", "Some other string: Diamonds");
    diamonds.setValue(EnumerationMasterInfo.NAME, "DIAMONDS");
    diamonds.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    diamonds.apply();

    suits.add(hearts);
    suits.add(clubs);
    suits.add(spades);
    suits.add(diamonds);
  }

  @Request
  @AfterClass
  public static void modelTearDown()
  {
    TestFixtureFactory.delete(childMdSession);

    TestFixtureFactory.delete(parentMdSession);

    TestFixtureFactory.delete(structMdBusiness);

    TestFixtureFactory.delete(newUser);

    TestFixtureFactory.delete(termClass);

    TestFixtureFactory.delete(refClass);

    TestFixtureFactory.delete(suitMaster);

    suits.clear();
  }

  /**
   * Updates an entity in an invalid fashion by setting an attribute incorrectly
   */
  @Request
  @Test
  public void testUpateSessionComponentInvalid()
  {
    SessionDTO testDTO = null;

    try
    {
      testDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      clientRequest.createSessionComponent(testDTO);

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
    catch (Throwable t)
    {
      Assert.fail(t.getMessage());
    }
    finally
    {
      if (testDTO != null)
      {
        clientRequest.delete(testDTO.getOid());
      }
    }
  }

  /**
   * Deletes an entity.
   */
  @Request
  @Test
  public void testDeleteSessionComponent()
  {
    SessionDTO sessionDTO = null;

    try
    {
      // create the entity and then delete it
      sessionDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      clientRequest.createSessionComponent(sessionDTO);

      clientRequest.delete(sessionDTO.getOid());

      // Now try to get the object that was created. If it was deleted, it
      // should error out.
      clientRequest.get(sessionDTO.getOid());
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
  public void testDeleteSessionInvalid()
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
    SessionDTO sessionDTO = null;

    try
    {
      sessionDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);

      if (!sessionDTO.getType().equals(parentMdSessionType))
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
      clientRequest.newMutable("this.class.should.not.exist.Blah");
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
   * Gets a valid instance.
   */
  @Request
  @Test
  public void testInstance()
  {
    SessionDTO sessionDTO = null;

    try
    {
      // create the entity and then delete it
      sessionDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      clientRequest.createSessionComponent(sessionDTO);

      SessionDTO retrieved = (SessionDTO) clientRequest.get(sessionDTO.getOid());

      Assert.assertEquals(sessionDTO.getOid(), retrieved.getOid());

    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (sessionDTO != null)
      {
        clientRequest.delete(sessionDTO.getOid());
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

  @Request
  @Test
  public void testBlob()
  {
    SessionDTO instance = null;

    try
    {
      // create a new instance of the type
      instance = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
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
      clientRequest.createSessionComponent(instance);
      retVal = instance.getBlob("aBlob");
      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != retVal[i])
        {
          Assert.fail("An updated instance did not store the blob bytes correctly.");
        }
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (instance != null && !instance.isNewInstance())
      {
        clientRequest.delete(instance.getOid());
      }
    }
  }

  @Request
  @Test
  public void testStructDTO()
  {
    SessionDTO sessionDTO = null;

    try
    {
      sessionDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);

      AttributeStructDTO phone = ComponentDTOFacade.getAttributeStructDTO(sessionDTO, "aStruct");
      phone.setValue("areaCode", "303");
      phone.setValue("prefix", "979");
      phone.setValue("suffix", "8874");

      // create the SessionComponent
      clientRequest.createSessionComponent(sessionDTO);
      AttributeStructDTO createdPhone = ComponentDTOFacade.getAttributeStructDTO(sessionDTO, "aStruct");
      if (!phone.getValue("areaCode").equals(createdPhone.getValue("areaCode")) || !phone.getValue("prefix").equals(createdPhone.getValue("prefix")) || !phone.getValue("suffix").equals(createdPhone.getValue("suffix")))
      {
        Assert.fail("The values for a created struct do not match the set values.");
      }

      // update the user
      createdPhone = ComponentDTOFacade.getAttributeStructDTO(sessionDTO, "aStruct");
      createdPhone.setValue("areaCode", "720");
      createdPhone.setValue("prefix", "363");
      createdPhone.setValue("suffix", "7744");

      clientRequest.update(sessionDTO);
      AttributeStructDTO updatedPhone = ComponentDTOFacade.getAttributeStructDTO(sessionDTO, "aStruct");
      if (!createdPhone.getValue("areaCode").equals(updatedPhone.getValue("areaCode")) || !createdPhone.getValue("prefix").equals(updatedPhone.getValue("prefix")) || !createdPhone.getValue("suffix").equals(updatedPhone.getValue("suffix")))
      {
        Assert.fail("The values for an updated struct do not match the set values.");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (sessionDTO != null)
      {
        clientRequest.delete(sessionDTO.getOid());
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeReadWritePermission()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      SessionDTO sessionDTO = (SessionDTO) tommyProxy.get(testObject.getOid());

      // make sure the readable and writable flag is set to true
      if (!sessionDTO.isReadable() || !sessionDTO.isWritable())
      {
        Assert.fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }

      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.WRITE.name(), Operation.DELETE.name());

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
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      SessionDTO sessionDTO = (SessionDTO) tommyProxy.get(testObject.getOid());

      // make sure the readable and writable flag is set to true
      if (sessionDTO.isWritable())
      {
        Assert.fail("Read or Write permission on a type were not properly set in the DTO");
      }
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name());
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testGrantTypeNoReadPermission()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      tommyProxy.get(testObject.getOid());

      Assert.fail("Read or Write permission on a type were not properly set in the DTO");
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
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());
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
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.delete(testObject.getOid());

    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name(), Operation.CREATE.name());
      clientRequest.revokeAttributePermission(newUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.WRITE.name());
    }
  }

  @Request
  @Test
  public void testGrantTypePermissionInvalid()
  {
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), "not_a_proper_permission");

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
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) clientRequest.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      // delete the object
      tommyProxy.delete(testObject.getOid());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommySession != null)
      {
        tommySession.logout();
      }
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name(), Operation.CREATE.name());
    }
  }

  @Request
  @Test
  public void testGrantAttributePermissions()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());
      clientRequest.grantAttributePermission(newUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.update(testObject);

      tommyProxy.delete(testObject.getOid());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

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
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.WRITE.name());

      clientRequest.grantAttributePermission(newUser.getOid(), mdAttributeBlobDTO.getOid(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(parentMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      // delete the object
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
      // Logging out will garbage collect the object.
      if (tommySession != null)
      {
        tommySession.logout();
      }

      clientRequest.revokeTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.WRITE.name());
      clientRequest.revokeAttributePermission(newUser.getOid(), mdAttributeBlobDTO.getOid(), Operation.WRITE.name());
    }
  }

  @Request
  @Test
  public void testGrantAttributePermissionsInvalid()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.CREATE.name(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(childMdSessionType);
      testObject.setValue("refChar", "I am all that is man!");
      tommyProxy.createSessionComponent(testObject);

      // Value needs to be blank since the user does not have read permissions.
      testObject.getValue("refChar");
    }
    catch (AttributeReadPermissionExceptionDTO e)
    {
      // this is expected
    }
    finally
    {
      clientRequest.revokeTypePermission(newUser.getOid(), childMdSession.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name(), Operation.CREATE.name());

      clientRequest.revokeAttributePermission(newUser.getOid(), mdAttributeCharacterDTO_2.getOid(), Operation.READ.name(), Operation.DELETE.name(), Operation.WRITE.name());

      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testToString()
  {
    SessionDTO sessionDTO = null;

    try
    {
      sessionDTO = (SessionDTO) clientRequest.newMutable(parentMdSessionType);

      String expectedToString = toStringPrepend + sessionDTO.getOid();
      String toString = sessionDTO.toString();

      if (!expectedToString.equals(toString))
      {
        Assert.fail("The toString() value on an SessionDTO is incorrect.");
      }
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testGrantOwnerReadPermission()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());
      clientRequest.grantTypePermission(RoleDAOIF.OWNER_ID, parentMdSession.getOid(), Operation.READ.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(parentMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      tommyProxy.get(testObject.getOid());
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }
      clientRequest.revokeTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());
      clientRequest.revokeTypePermission(RoleDAOIF.OWNER_ID, parentMdSession.getOid(), Operation.READ.name());
      if (tommySession != null)
      {
        tommySession.logout();
      }
    }
  }

  @Request
  @Test
  public void testInvalidGrantOwnerReadPermission()
  {
    SessionDTO testObject = null;
    ClientSession tommySession = null;
    ClientRequestIF tommyProxy = null;
    try
    {
      clientRequest.grantTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());

      tommySession = this.createSession("Tommy", "music");
      tommyProxy = getRequest(tommySession);

      testObject = (SessionDTO) tommyProxy.newMutable(parentMdSessionType);
      tommyProxy.createSessionComponent(testObject);

      tommyProxy.get(testObject.getOid());

      Assert.fail("Read or Write permission on a type were not properly set in the DTO");
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
      if (tommyProxy != null)
      {
        tommyProxy.delete(testObject.getOid());
      }
      clientRequest.revokeTypePermission(newUser.getOid(), parentMdSession.getOid(), Operation.CREATE.name(), Operation.DELETE.name());
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
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);

    Assert.assertEquals(parentMdSession.getStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDisplayLabel());
    Assert.assertEquals(parentMdSession.getStructValue(MetadataInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE), instance.getMd().getDescription());
    Assert.assertEquals(parentMdSession.getOid(), instance.getMd().getOid());
  }

  @Request
  @Test
  public void testBooleanMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aBoolean").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBooleanDTO, md);
  }

  @Request
  @Test
  public void testBlobMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aBlob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeBlobDTO, md);
  }

  @Request
  @Test
  public void testReferenceMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeReferenceMdDTO md = ComponentDTOFacade.getAttributeReferenceDTO(instance, "aReference").getAttributeMdDTO();

    checkAttributeMd(mdAttributeReferenceDTO, md);

    Assert.assertEquals(md.getReferencedMdBusiness(), refType);
  }

  @Request
  @Test
  public void testDateTimeMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDateTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateTimeDTO, md);
  }

  @Request
  @Test
  public void testDateMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aDate").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDateDTO, md);
  }

  @Request
  @Test
  public void testTimeMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aTime").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTimeDTO, md);
  }

  @Request
  @Test
  public void testIdMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeCharacterMdDTO md = instance.getOidMd();

    Assert.assertEquals(md.getName(), EntityInfo.OID);
    Assert.assertEquals(md.isRequired(), true);
    Assert.assertEquals(md.isSystem(), true);
  }

  // TODO enable once type attribute has been moved to component
  /*
   * @Request @Test public void testTypeMetadata() { MutableDTO instance =
   * clientRequest.newMutable(parentMdSessionType); AttributeCharacterMdDTO md =
   * instance.getTypeMd();
   * 
   * Assert.assertEquals(md.getName(), EntityInfo.TYPE);
   * Assert.assertEquals(md.isRequired(), true);
   * Assert.assertEquals(md.isSystem(), true); }
   */

  @Request
  @Test
  public void testIntegerMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "anInteger").getAttributeMdDTO();

    checkAttributeMd(mdAttributeIntegerDTO, md);
  }

  @Request
  @Test
  public void testLongMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeNumberMdDTO md = ComponentDTOFacade.getAttributeNumberDTO(instance, "aLong").getAttributeMdDTO();

    checkAttributeMd(mdAttributeLongDTO, md);
  }

  @Request
  @Test
  public void testDecimalMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDecimal").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDecimalDTO, md);
  }

  @Request
  @Test
  public void testDoubleMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aDouble").getAttributeMdDTO();

    checkAttributeMd(mdAttributeDoubleDTO, md);
  }

  @Request
  @Test
  public void testFloatMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeDecMdDTO md = ComponentDTOFacade.getAttributeDecDTO(instance, "aFloat").getAttributeMdDTO();

    checkAttributeMd(mdAttributeFloatDTO, md);
  }

  @Request
  @Test
  public void testTextMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aText").getAttributeMdDTO();

    checkAttributeMd(mdAttributeTextDTO, md);
  }

  @Request
  @Test
  public void testClobMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeMdDTO md = ComponentDTOFacade.getAttributeDTO(instance, "aClob").getAttributeMdDTO();

    checkAttributeMd(mdAttributeClobDTO, md);
  }

  @Request
  @Test
  public void testCharacterMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeCharacterMdDTO md = ComponentDTOFacade.getAttributeCharacterDTO(instance, "aCharacter").getAttributeMdDTO();

    checkAttributeMd(mdAttributeCharacterDTO, md);
    Assert.assertEquals(Integer.parseInt(mdAttributeCharacterDTO.getValue(MdAttributeCharacterInfo.SIZE)), md.getSize());
  }

  @Request
  @Test
  public void testStructMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeStructMdDTO md = ComponentDTOFacade.getAttributeStructDTO(instance, "aStruct").getAttributeMdDTO();

    checkAttributeMd(mdAttributeStructDTO, md);
    Assert.assertEquals(EntityTypes.PHONE_NUMBER.getType(), md.getDefiningMdStruct());
  }

  @Request
  @Test
  public void testEnumerationMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
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
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeHashDTO(instance, "aHash").getAttributeMdDTO();

    checkAttributeMd(mdAttributeHashDTO, md);
    Assert.assertEquals(HashMethods.MD5.getMessageDigest(), md.getEncryptionMethod());
  }

  @Request
  @Test
  public void testSymmetricMetadata()
  {
    MutableDTO instance = clientRequest.newMutable(parentMdSessionType);
    AttributeEncryptionMdDTO md = ComponentDTOFacade.getAttributeSymmetricDTO(instance, "aSym").getAttributeMdDTO();

    checkAttributeMd(mdAttributeSymmetricDTO, md);
    Assert.assertEquals(SymmetricMethods.DES.getTransformation(), md.getEncryptionMethod());
  }

  /**
   * This method does all the checks for attribute metadata on DTOs. All checks,
   * except for attribute specific metadata is consolidated here (it's better
   * than copying/pasting the same checks into a dozen different tests).
   * 
   * @param mdAttribute
   * @param mdDTO
   */
  private void checkAttributeMd(MdAttributeDAO mdAttribute, AttributeMdDTO md)
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

  // @Request @Test public void testGrantOwnerReadPermission()
  // {
  // SessionDTO testObject = null;
  // String userSessionId = null;
  // try
  // {
  // clientRequest.grantTypePermission(newUser.getOid(),
  // Operation.CREATE.getOid(), childMdSession.getOid());
  // clientRequest.grantTypePermission(newUser.getOid(),
  // Operation.DELETE.getOid(), childMdSession.getOid());
  // clientRequest.grantTypePermission(RoleIF.OWNER_ID, Operation.READ.getOid(),
  // childMdSession.getOid());
  //
  // userSessionId = clientRequest.login("Tommy", "music");
  // testObject = (SessionDTO)clientRequest.newMutable(userSessionId,
  // childMdSessionType);
  // clientRequest.createSessionComponent(userSessionId, testObject);
  //
  // clientRequest.get(userSessionId, testObject.getOid());
  //
  // Assert.fail("Read or Write permission on a type were not properly set in
  // the DTO");
  // }
  // catch (TypePermissionException_READDTO e)
  // {
  // // We want to land here.
  // }
  // catch (Throwable e)
  // {
  // Assert.fail(e.getMessage());
  // }
  // finally
  // {
  // clientRequest.delete(userSessionId, testObject.getOid());
  // clientRequest.revokeTypePermission(newUser.getOid(),
  // Operation.CREATE.getOid(), childMdSession.getOid());
  // clientRequest.revokeTypePermission(newUser.getOid(),
  // Operation.DELETE.getOid(), childMdSession.getOid());
  // clientRequest.logout(userSessionId);
  // }
  // }

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
      SessionDTO instance = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getOid());
      clientRequest.createSessionComponent(instance);

      try
      {
        SessionDTO test = (SessionDTO) clientRequest.get(instance.getOid());

        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
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
      SessionDTO instance = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      instance.getClass().getMethod("clearAMultiReference").invoke(instance);
      instance.getClass().getMethod("addAMultiReference", term.getClass()).invoke(instance, term);
      clientRequest.createSessionComponent(instance);

      try
      {
        SessionDTO test = (SessionDTO) clientRequest.get(instance.getOid());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiReference").invoke(test);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
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
      SessionDTO instance = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      instance.clearMultiItems(attributeName);
      instance.addMultiItem(attributeName, term.getOid());
      clientRequest.createSessionComponent(instance);

      try
      {
        SessionDTO test = (SessionDTO) clientRequest.get(instance.getOid());

        List<String> results = test.getMultiItems(attributeName);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
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
      SessionDTO instance = (SessionDTO) clientRequest.newMutable(parentMdSessionType);
      instance.getClass().getMethod("clearAMultiTerm").invoke(instance);
      instance.getClass().getMethod("addAMultiTerm", term.getClass()).invoke(instance, term);
      clientRequest.createSessionComponent(instance);

      try
      {
        SessionDTO test = (SessionDTO) clientRequest.get(instance.getOid());

        List<String> results = (List<String>) test.getClass().getMethod("getAMultiTerm").invoke(test);

        Assert.assertEquals(1, results.size());
        Assert.assertEquals(term.getOid(), results.get(0));
      }
      finally
      {
        clientRequest.delete(instance.getOid());
      }
    }
    finally
    {
      clientRequest.lock(term);
      clientRequest.delete(term.getOid());
    }
  }
}
