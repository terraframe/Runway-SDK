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
package com.runwaysdk.business;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.runwaysdk.ClientSession;
import com.runwaysdk.business.generation.dto.ClassDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeConcreteInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFileInfo;
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
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdSessionInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.TransientDAO;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdSessionDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.generation.loader.GeneratedLoader;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.session.Request;
import com.runwaysdk.session.RequestType;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;

import sun.security.provider.Sun;

@SuppressWarnings("unchecked")
public abstract class SessionComponentGenTest
{
  public static final String               path           = TestConstants.Path.XMLFiles + "/";

  public static final String               EXCEPTION_XML  = path + "customException.xml";

  private static final String              pack           = "test.generated";

  protected static MdSessionDAO            car;

  protected static MdSessionDAO            collection;

  protected static MdSessionDAO            collectionSub;

  private static MdAttributeBlobDAO        collectionBlob;

  private static MdAttributeCharacterDAO   collectionCharacter;

  private static MdAttributeCharacterDAO   structCharacter;

  private static MdAttributeIntegerDAO     collectionInteger;

  private static MdAttributeLongDAO        collectionLong;

  private static MdAttributeDecimalDAO     collectionDecimal;

  private static MdAttributeDoubleDAO      collectionDouble;

  private static MdAttributeFloatDAO       collectionFloat;

  private static MdAttributeDateTimeDAO    collectionDateTime;

  private static MdAttributeDateDAO        collectionDate;

  private static MdAttributeTimeDAO        collectionTime;

  private static MdAttributeBooleanDAO     collectionBoolean;

  private static MdAttributeReferenceDAO   collectionReference;

  private static MdAttributeEnumerationDAO collectionEnumeration;

  private static MdAttributeStructDAO      collectionStruct;

  private static MdAttributeHashDAO        collectionHash;

  private static MdAttributeSymmetricDAO   collectionSymmetric;

  private static MdAttributeTextDAO        collectionText;

  private static MdAttributeClobDAO        collectionClob;

  private static MdAttributeFileDAO        collectionFile;

  private static MdAttributeBlobDAO        enumBlob;

  private static MdStructDAO               struct;

  private static MdBusinessDAO             reference;

  private static String                    collectionType;

  private static MdEnumerationDAO          suitEnum;

  private static MdBusinessDAO             suitMaster;

  private static String                    heartsId;

  private static String                    heartName      = "HEARTS";

  private static String                    clubsId;

  private static String                    clubName       = "CLUBS";

  private static String                    diamondName    = "DIAMONDS";

  private static String                    spadesName     = "SPADES";

  private static String                    collectionDTO;

  private static ClientSession             systemSession  = null;

  private static ClientRequestIF           _clientReaqest = null;

  protected static String                  superMdSessionAttributeNameHack;

  public static void sessionClassSetUp()
  {
    suitMaster = MdBusinessDAO.newInstance();
    suitMaster.setValue(MdBusinessInfo.NAME, "SuitMaster");
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    suitMaster.apply();

    suitEnum = MdEnumerationDAO.newInstance();
    suitEnum.setValue(MdEnumerationInfo.NAME, "SuitEnum");
    suitEnum.setValue(MdEnumerationInfo.PACKAGE, pack);
    suitEnum.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration");
    suitEnum.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    suitEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getId());
    suitEnum.apply();

    struct = MdStructDAO.newInstance();
    struct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Standalone Class");
    struct.setValue(MdStructInfo.PACKAGE, pack);
    struct.setValue(MdStructInfo.NAME, "Standalone");
    struct.apply();

    MdAttributeEnumerationDAO structEnumeration = MdAttributeEnumerationDAO.newInstance();
    structEnumeration.setValue(MdAttributeEnumerationInfo.NAME, "structEnumeration");
    structEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Enumeration");
    structEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, struct.getId());
    structEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitEnum.getId());
    structEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    structEnumeration.apply();

    enumBlob = MdAttributeBlobDAO.newInstance();
    enumBlob.setValue(MdAttributeBlobInfo.NAME, "pic");
    enumBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Pic");
    enumBlob.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Pic Desc");
    enumBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    enumBlob.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    enumBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, suitMaster.getId());
    enumBlob.apply();

    MdAttributeStructDAO enumStruct = MdAttributeStructDAO.newInstance();
    enumStruct.setValue(MdAttributeStructInfo.NAME, "enumStruct");
    enumStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct on an Enum");
    enumStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, suitMaster.getId());
    enumStruct.setValue(MdAttributeStructInfo.MD_STRUCT, struct.getId());
    enumStruct.apply();

    structCharacter = MdAttributeCharacterDAO.newInstance();
    structCharacter.setValue(MdAttributeCharacterInfo.NAME, "structCharacter");
    structCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Character");
    structCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Character Desc");
    structCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    structCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    structCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    structCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, struct.getId());
    structCharacter.apply();

    MdAttributeBooleanDAO structBoolean = MdAttributeBooleanDAO.newInstance();
    structBoolean.setValue(MdAttributeBooleanInfo.NAME, "structBoolean");
    structBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Boolean");
    structBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    structBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    structBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, struct.getId());
    structBoolean.apply();

    BusinessDAO enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, clubName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    enum_item.setBlob("pic", new byte[] { 1, 2, 3, 4 });
    clubsId = enum_item.apply();

    enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, diamondName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    enum_item.setBlob("pic", new byte[] { 2, 4, 6, 8 });
    enum_item.apply();

    enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, heartName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    enum_item.setBlob("pic", new byte[] { 3, 6, 9 });
    heartsId = enum_item.apply();

    enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, spadesName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Spades");
    enum_item.setBlob("pic", new byte[] { 4, 8 });
    enum_item.apply();

    reference = MdBusinessDAO.newInstance();
    reference.setValue(MdBusinessInfo.NAME, "Reference");
    reference.setValue(MdBusinessInfo.PACKAGE, pack);
    reference.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    reference.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Class");
    reference.apply();

    MdAttributeIntegerDAO referenceInt = MdAttributeIntegerDAO.newInstance();
    referenceInt.setValue(MdAttributeIntegerInfo.NAME, "referenceInt");
    referenceInt.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Integer");
    referenceInt.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, reference.getId());
    referenceInt.apply();

    collection.setValue(MdSessionInfo.NAME, "Collection");
    collection.setValue(MdSessionInfo.PACKAGE, pack);
    collection.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Class");
    collection.setStructValue(MdSessionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collection.apply();
    collectionType = collection.definesType();

    collectionSub.setValue(MdSessionInfo.NAME, "CollectionSub");
    collectionSub.setValue(MdSessionInfo.PACKAGE, pack);
    collectionSub.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collectionSub.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    collectionSub.setStructValue(MdSessionInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collectionSub.setValue(superMdSessionAttributeNameHack, collection.getId());
    collectionSub.apply();

    collectionBlob = MdAttributeBlobDAO.newInstance();
    collectionBlob.setValue(MdAttributeBlobInfo.NAME, "aBlob");
    collectionBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob");
    collectionBlob.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob desc");
    collectionBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionBlob.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, collection.getId());
    collectionBlob.apply();

    collectionBoolean = MdAttributeBooleanDAO.newInstance();
    collectionBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, collection.getId());
    collectionBoolean.apply();

    collectionCharacter = MdAttributeCharacterDAO.newInstance();
    collectionCharacter.setValue(MdAttributeCharacterInfo.NAME, "aCharacter");
    collectionCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character");
    collectionCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Character desc");
    collectionCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    collectionCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, collection.getId());
    collectionCharacter.apply();

    collectionDecimal = MdAttributeDecimalDAO.newInstance();
    collectionDecimal.setValue(MdAttributeDecimalInfo.NAME, "aDecimal");
    collectionDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    collectionDecimal.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal desc");
    collectionDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, collection.getId());
    collectionDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDecimal.setValue(MdAttributeDecimalInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDecimal.setValue(MdAttributeDecimalInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionDecimal.setValue(MdAttributeDecimalInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionDecimal.setValue(MdAttributeDecimalInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "16");
    collectionDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "4");
    collectionDecimal.apply();

    collectionDouble = MdAttributeDoubleDAO.newInstance();
    collectionDouble.setValue(MdAttributeDoubleInfo.NAME, "aDouble");
    collectionDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double");
    collectionDouble.setStructValue(MdAttributeDoubleInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Double desc");
    collectionDouble.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDouble.setValue(MdAttributeDoubleInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, collection.getId());
    collectionDouble.setValue(MdAttributeDoubleInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionDouble.setValue(MdAttributeDoubleInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionDouble.setValue(MdAttributeDoubleInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    collectionDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    collectionDouble.apply();

    collectionEnumeration = MdAttributeEnumerationDAO.newInstance();
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.NAME, "anEnum");
    collectionEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated Attribute");
    collectionEnumeration.setStructValue(MdAttributeEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Enumerated desc");
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, collection.getId());
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitEnum.getId());
    collectionEnumeration.apply();

    collectionFloat = MdAttributeFloatDAO.newInstance();
    collectionFloat.setValue(MdAttributeFloatInfo.NAME, "aFloat");
    collectionFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    collectionFloat.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float Desc");
    collectionFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, collection.getId());
    collectionFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionFloat.setValue(MdAttributeFloatInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionFloat.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionFloat.setValue(MdAttributeFloatInfo.LENGTH, "16");
    collectionFloat.setValue(MdAttributeFloatInfo.DECIMAL, "4");
    collectionFloat.apply();

    collectionHash = MdAttributeHashDAO.newInstance();
    collectionHash.setValue(MdAttributeHashInfo.NAME, "aHash");
    collectionHash.setValue(MdAttributeHashInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionHash.setValue(MdAttributeHashInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionHash.setStructValue(MdAttributeHashInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hashed Attributed");
    collectionHash.setStructValue(MdAttributeHashInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Hash Desc");
    collectionHash.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getId());
    collectionHash.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, collection.getId());
    collectionHash.apply();

    collectionInteger = MdAttributeIntegerDAO.newInstance();
    collectionInteger.setValue(MdAttributeIntegerInfo.NAME, "anInteger");
    collectionInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer");
    collectionInteger.setStructValue(MdAttributeIntegerInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Integer Desc");
    collectionInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionInteger.setValue(MdAttributeIntegerInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionInteger.setValue(MdAttributeIntegerInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionInteger.setValue(MdAttributeIntegerInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionInteger.setValue(MdAttributeIntegerInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, collection.getId());
    collectionInteger.apply();

    collectionLong = MdAttributeLongDAO.newInstance();
    collectionLong.setValue(MdAttributeLongInfo.NAME, "aLong");
    collectionLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    collectionLong.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long Desc");
    collectionLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getId());
    collectionLong.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLong.setValue(MdAttributeLongInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionLong.apply();

    collectionDate = MdAttributeDateDAO.newInstance();
    collectionDate.setValue(MdAttributeDateInfo.NAME, "aDate");
    collectionDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date");
    collectionDate.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date Desc");
    collectionDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDate.setValue(MdAttributeDateInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, collection.getId());
    collectionDate.apply();

    collectionDateTime = MdAttributeDateTimeDAO.newInstance();
    collectionDateTime.setValue(MdAttributeDateTimeInfo.NAME, "aDateTime");
    collectionDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    collectionDateTime.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime Desc");
    collectionDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDateTime.setValue(MdAttributeDateTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, collection.getId());
    collectionDateTime.apply();

    collectionTime = MdAttributeTimeDAO.newInstance();
    collectionTime.setValue(MdAttributeTimeInfo.NAME, "aTime");
    collectionTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    collectionTime.setStructValue(MdAttributeTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time Desc");
    collectionTime.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionTime.setValue(MdAttributeTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, collection.getId());
    collectionTime.apply();

    collectionReference = MdAttributeReferenceDAO.newInstance();
    collectionReference.setValue(MdAttributeReferenceInfo.NAME, "aReference");
    collectionReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference");
    collectionReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference Desc");
    collectionReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, collection.getId());
    collectionReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, reference.getId());
    collectionReference.apply();

    collectionFile = MdAttributeFileDAO.newInstance();
    collectionFile.setValue(MdAttributeFileInfo.NAME, "aFile");
    collectionFile.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File");
    collectionFile.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File Desc");
    collectionFile.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, collection.getId());
    collectionFile.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionFile.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionFile.apply();

    collectionStruct = MdAttributeStructDAO.newInstance();
    collectionStruct.setValue(MdAttributeStructInfo.NAME, "aStruct");
    collectionStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    collectionStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, collection.getId());
    collectionStruct.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Desc");
    collectionStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionStruct.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionStruct.setValue(MdAttributeStructInfo.MD_STRUCT, struct.getId());
    collectionStruct.apply();

    collectionSymmetric = MdAttributeSymmetricDAO.newInstance();
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.NAME, "aSym");
    collectionSymmetric.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Attribute");
    collectionSymmetric.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Desc");
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getId());
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, collection.getId());
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionSymmetric.apply();

    collectionText = MdAttributeTextDAO.newInstance();
    collectionText.setValue(MdAttributeTextInfo.NAME, "aText");
    collectionText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    collectionText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionText.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionText.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text Desc");
    collectionText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, collection.getId());
    collectionText.apply();

    collectionClob = MdAttributeClobDAO.newInstance();
    collectionClob.setValue(MdAttributeClobInfo.NAME, "aClob");
    collectionClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    collectionClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionClob.setValue(MdAttributeClobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionClob.setStructValue(MdAttributeClobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob Desc");
    collectionClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, collection.getId());
    collectionClob.apply();

    collectionDTO = collection.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  @Request
  @Before
  public void setUp()
  {
    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    _clientReaqest = systemSession.getRequest();
  }

  @Request
  @After
  public void tearDown() throws Exception
  {
    systemSession.logout();
  }

  private void makeCar()
  {
    if (!MdSessionDAO.isDefined(pack + ".Car"))
    {
      car = this.newMdSession();
      car.setValue(MdSessionInfo.NAME, "Car");
      car.setValue(MdSessionInfo.PACKAGE, pack);
      car.setValue(MdSessionInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      car.setValue(MdSessionInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      car.setStructValue(MdSessionInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A car");
      car.apply();
    }
  }

  protected abstract MdSessionDAO newMdSession();

  @Request
  @Test
  public void testInstance() throws Exception
  {
    this.instance(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void instance(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    SessionComponent sessionComponent = SessionComponent.get(id);

    if (!id.equals(sessionComponent.getId()))
    {
      Assert.fail("An applied instance did not match the retrieved instance.");
    }
  }

  @Request
  @Test
  public void testDelete()
  {
    makeCar();
    car.delete();

    if (MdSessionDAO.isDefined(pack + ".Car"))
      Assert.fail("Car was not deleted!");

    makeCar();
  }

  @Request
  @Test
  public void testGetBlob() throws Exception
  {
    this.getBlob(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBlob(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    byte[] out = (byte[]) collectionClass.getMethod("getABlob").invoke(object);

    try
    {
      if (in.length != out.length)
      {
        Assert.fail("Stored and Retrieved blobs are different sizes.");
      }
      for (int i = 0; i < in.length; i++)
      {
        if (in[i] != out[i])
        {
          Assert.fail("Stored and Retrieved blobs have different values.");
        }
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testBoolean() throws Exception
  {
    this.getBoolean(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBoolean(String sessionId) throws Exception
  {
    boolean in = false;
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved booleans have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetBooleanNull() throws Exception
  {
    this.getBooleanNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBooleanNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    Boolean nullObject = null;
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, nullObject);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Boolean getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testCharacter() throws Exception
  {
    this.character(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void character(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "Mr. Sparkle";
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getACharacter").invoke(object);

    try
    {
      if (!in.equals(out))
      {
        Assert.fail("Stored and Retrieved Characters have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDecimal() throws Exception
  {
    this.decimal(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void decimal(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    BigDecimal in = new BigDecimal(123456.789);

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    BigDecimal out = (BigDecimal) collectionClass.getMethod("getADecimal").invoke(object);

    try
    {
      if (in.subtract(out).abs().doubleValue() > .0000001)
      {
        Assert.fail("Stored and Retrieved Decimals have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDecimalNull() throws Exception
  {
    this.decimalNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void decimalNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    BigDecimal in = null;

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Double out = (Double) collectionClass.getMethod("getADecimal").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Decimal getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDouble() throws Exception
  {
    this.setDouble(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void setDouble(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    double in = 123456.789;
    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved Doubles have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDoubleNull() throws Exception
  {
    this.doubleNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void doubleNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Double in = null;
    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Double getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetFloat() throws Exception
  {
    this.getFloat(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getFloat(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    float in = 123456.789F;
    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved Floats have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetFloatNull() throws Exception
  {
    this.getFloatNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getFloatNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Float in = null;
    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Float getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetHash() throws Exception
  {
    this.getHash(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getHash(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "For breakfast, I had some Pringles, and some fudge-striped cook-ays";
    collectionClass.getMethod("setAHash", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
    digest.update(in.getBytes());

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    boolean out = (Boolean) collectionClass.getMethod("aHashEquals", String.class).invoke(object, "For breakfast, I had some Pringles, and some fudge-striped cook-ays");

    try
    {
      if (!out)
      {
        Assert.fail("Stored Hash did not equal equivalent value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetSymmetric() throws Exception
  {
    this.getSymmetric(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getSymmetric(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "My rims never spin - to the contrary";
    collectionClass.getMethod("setASym", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getASym").invoke(object);

    try
    {
      if (!in.equals(out))
      {
        Assert.fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetInteger() throws Exception
  {
    this.getInteger(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getInteger(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    int in = 1234;
    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    int out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved Integers have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testIntegerNull() throws Exception
  {
    this.integerNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void integerNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Integer in = null;
    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Integer out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("An Integer getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetLong() throws Exception
  {
    this.getLong(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getLong(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    long in = 123456789;
    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved Longs have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testLongNull() throws Exception
  {
    this.getLongNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getLongNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Long in = null;
    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Long getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetDate() throws Exception
  {
    this.getDate(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getDate(String sessionId) throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    try
    {
      if (!sdf.format(in).equals(sdf.format(out)))
      {
        Assert.fail("Stored and Retrieved Dates have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetDateNull() throws Exception
  {
    this.getDateNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getDateNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = null;
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Date getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetDateTime() throws Exception
  {
    this.getDateTime(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getDateTime(String sessionId) throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    try
    {
      if (!sdf.format(in).equals(sdf.format(out)))
      {
        Assert.fail("Stored and Retrieved DateTimes have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetDateTimeNull() throws Exception
  {
    this.getDateTimeNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getDateTimeNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = null;
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A DateTime getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetTime() throws Exception
  {
    this.getTime(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getTime(String sessionId) throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    try
    {
      if (!sdf.format(in).equals(sdf.format(out)))
      {
        Assert.fail("Stored and Retrieved Times have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetTimeNull() throws Exception
  {
    this.getTimeNull(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getTimeNull(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = null;
    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);

    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    try
    {
      if (out != null)
      {
        Assert.fail("A Time getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetText() throws Exception
  {
    this.getText(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getText(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";
    collectionClass.getMethod("setAText", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    try
    {
      if (!in.equals(out))
      {
        Assert.fail("Stored and Retrieved Texts have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testSetStructEnumeration() throws Exception
  {
    this.setStructEnumeration(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void setStructEnumeration(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    BusinessEnumeration in = (BusinessEnumeration) enumClass.getMethod("get", String.class).invoke(null, heartsId);

    Object object = collectionClass.getConstructor().newInstance();
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);

    SessionComponent sessionComponent = SessionComponent.get(id);
    TransientDAO transientDAO = sessionComponent.getTransientDAO();
    StructDAO structDAO = ( (AttributeStructIF) transientDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    Assert.assertEquals(1, enums.length);
    Assert.assertEquals(heartsId, enums[0].getId());
  }

  @Request
  @Test
  public void testGetStructEnumeration() throws Exception
  {
    this.getStructEnumeration(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getStructEnumeration(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    String input = "This is myself.";

    BusinessEnumeration in = (BusinessEnumeration) enumClass.getMethod("get", String.class).invoke(null, heartsId);

    Object object = collectionClass.getConstructor().newInstance();
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, input);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    struct = collectionClass.getMethod("getAStruct").invoke(object);
    List<?> out = (List<?>) structClass.getMethod("getStructEnumeration").invoke(struct);

    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getId").invoke(head);
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    try
    {
      Assert.assertEquals(input, structChar);
      Assert.assertEquals(1, out.size());
      Assert.assertEquals(heartsId, outId);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testStructBoolean() throws Exception
  {
    this.structBoolean(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void structBoolean(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Object object = collectionClass.getConstructor().newInstance();

    boolean in = true;
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    Method get = collectionClass.getMethod("get", String.class);
    object = get.invoke(null, id);
    struct = collectionClass.getMethod("getAStruct").invoke(object);

    boolean out = (Boolean) structClass.getMethod("getStructBoolean").invoke(struct);

    try
    {
      if (in != out)
      {
        Assert.fail("Stored and Retrieved StructBooleans have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testReference() throws Exception
  {
    this.testReference(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testReference(String sessionId) throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());
    Business in = (Business) referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(in);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    collectionClass.getMethod("setAReference", referenceClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    object = collectionClass.getMethod("get", String.class).invoke(null, id);
    Business out = (Business) collectionClass.getMethod("getAReference").invoke(object);

    try
    {
      if (!in.getId().equalsIgnoreCase(out.getId()))
      {
        Assert.fail("Stored and Retrieved References are different.");
      }
    }
    finally
    {
      referenceClass.getMethod("delete").invoke(in);
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testAddEnum() throws Exception
  {
    this.addEnum(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void addEnum(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    SessionComponent object = (SessionComponent) collectionClass.getConstructor().newInstance();

    Object in = enumClass.getDeclaredField(diamondName).get(null);

    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    object = (SessionComponent) collectionClass.getMethod("get", String.class).invoke(null, id);

    TransientDAO transientDAO = object.getTransientDAO();

    Set<String> ids = ( (AttributeEnumerationIF) transientDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    try
    {
      if (ids.size() != 1)
      {
        Assert.fail("Expected 1 enum value, found " + ids.size());
      }
      String out = BusinessDAO.get(ids.iterator().next()).getValue(EnumerationMasterInfo.NAME);

      if (!out.equals(diamondName))
      {
        Assert.fail("Stored and Retrieved enums have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetEnum() throws Exception
  {
    this.getEnum(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getEnum(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    Object in = enumClass.getDeclaredField(heartName).get(null);
    String inId = (String) enumClass.getMethod("getId").invoke(in);

    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    object = collectionClass.getMethod("get", String.class).invoke(null, id);
    List<BusinessEnumeration> out = (List<BusinessEnumeration>) collectionClass.getMethod("getAnEnum").invoke(object);
    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getId").invoke(head);

    try
    {
      if (!inId.equalsIgnoreCase(outId))
      {
        Assert.fail("Stored and Retrieved enums have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testRemoveEnum() throws Exception
  {
    this.removeEnum(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void removeEnum(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    SessionComponent object = (SessionComponent) collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, hearts);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    object = (SessionComponent) collectionClass.getMethod("get", String.class).invoke(null, id);
    collectionClass.getMethod("removeAnEnum", enumClass).invoke(object, hearts);
    collectionClass.getMethod("apply").invoke(object);

    object = (SessionComponent) collectionClass.getMethod("get", String.class).invoke(null, id);
    TransientDAO transientDAO = object.getTransientDAO();
    Set<String> out = ( (AttributeEnumerationIF) transientDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    try
    {
      if (out.size() != 0)
      {
        Assert.fail("Failed to remove an enumerated value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testClearEnum() throws Exception
  {
    this.clearEnum(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void clearEnum(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    SessionComponent object = (SessionComponent) collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, hearts);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    collectionClass.getMethod("clearAnEnum").invoke(object);
    collectionClass.getMethod("apply").invoke(object);

    object = (SessionComponent) collectionClass.getMethod("get", String.class).invoke(null, id);
    TransientDAO transientDAO = object.getTransientDAO();
    Set<String> out = ( (AttributeEnumerationIF) transientDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    try
    {
      if (out.size() != 0)
      {
        Assert.fail("Failed to clear an enumerated value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDeleteDTO() throws Exception
  {
    this.deleteDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void deleteDTO(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionClassDTO = LoaderDecorator.load(collectionDTO);
    Method get = collectionClassDTO.getMethod("get", ClientRequestIF.class, String.class);
    Object objectDTO = get.invoke(null, _clientReaqest, id);

    collectionClassDTO.getMethod("delete").invoke(objectDTO);

    try
    {
      object = collectionClass.getMethod("get", String.class).invoke(null, id);

      Assert.fail("Delete businessDTO did not delete the entity");
    }
    catch (Exception e)
    {
      if (e.getCause() != null && ( e.getCause() instanceof DataNotFoundException ))
      {
        // Expect to land here
      }
      else
      {
        Assert.fail(e.getMessage());
      }

    }
  }

  @Request
  @Test
  public void testGetBlobDTO() throws Exception
  {
    this.getBlobDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBlobDTO(String sessionId) throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    Object objectDTO = get.invoke(null, _clientReaqest, id);

    byte[] out = (byte[]) collectionDTOclass.getMethod("getABlob").invoke(objectDTO);

    try
    {
      if (in.length != out.length)
      {
        Assert.fail("Stored and Retrieved blobs are different sizes.");
      }

      for (int i = 0; i < in.length; i++)
      {
        if (in[i] != out[i])
        {
          Assert.fail("Stored and Retrieved blobs have different values.");
        }
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  /**
   * Test that boolean attributes methods work for DTO generation
   *
   * @throws Exception
   */
  @Request
  @Test
  public void testGetBooleanDTO() throws Exception
  {
    this.getBooleanDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBooleanDTO(String sessionId) throws Exception
  {
    boolean in = false;
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    Object objectDTO = get.invoke(null, _clientReaqest, id);
    boolean out = (Boolean) collectionDTOclass.getMethod("getABoolean").invoke(objectDTO);

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetBooleanNullDTO() throws Exception
  {
    this.getBooleanNullDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getBooleanNullDTO(String sessionId) throws Exception
  {
    Boolean in = null;
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    Object objectDTO = get.invoke(null, _clientReaqest, id);
    Boolean out = (Boolean) collectionDTOclass.getMethod("getABoolean").invoke(objectDTO);

    try
    {
      Assert.assertNull(null, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetCharacterDTO() throws Exception
  {
    this.getCharacterDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void getCharacterDTO(String sessionId) throws Exception
  {
    String in = "RunwaySDK";
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    Object objectDTO = get.invoke(null, _clientReaqest, id);

    String out = (String) collectionDTOclass.getMethod("getACharacter").invoke(objectDTO);

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testAttributeGetterVisibility() throws Exception
  {
    this.attributeGetterVisibility(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void attributeGetterVisibility(String sessionId) throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.isolatedClassLoader();

    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("getACharacter").invoke(object);

    collectionCharacter.addItem(MdAttributeConcreteInfo.GETTER_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    collectionCharacter.apply();

    loader = GeneratedLoader.isolatedClassLoader();
    colletionClass = loader.loadClass(collection.definesType());
    colletionBaseClass = colletionClass.getSuperclass();

    try
    {
      modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
      if (!Modifier.isProtected(modifiers))
      {
        Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");
      }

      try
      {
        collectionDTOclass = loader.loadClass(collectionDTO);
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
        collectionDTOclass.getMethod("getACharacter").invoke(object);

        Assert.fail("Able to access a getter method on a DTO for an attribute that is [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].");
      }
      catch (NoSuchMethodException e)
      {
        // this is expected
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      collectionCharacter.addItem(MdAttributeConcreteInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
      collectionCharacter.apply();

      loader = GeneratedLoader.isolatedClassLoader();
      colletionClass = loader.loadClass(collection.definesType());
      colletionBaseClass = colletionClass.getSuperclass();

      modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
      if (!Modifier.isPublic(modifiers))
      {
        Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
      }

      // Make sure the accessor method is back
      collectionDTOclass = loader.loadClass(collectionDTO);
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
      collectionDTOclass.getMethod("getACharacter").invoke(object);
    }
  }

  @Request
  @Test
  public void testAttributeSetterVisibility() throws Exception
  {
    this.attributeSetterVisibility(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void attributeSetterVisibility(String sessionId) throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.isolatedClassLoader();
    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");

    collectionCharacter.addItem(MdAttributeConcreteInfo.SETTER_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    collectionCharacter.apply();
    
    loader = GeneratedLoader.isolatedClassLoader();
    colletionClass = loader.loadClass(collection.definesType());
    colletionBaseClass = colletionClass.getSuperclass();

    try
    {

      modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
      if (!Modifier.isProtected(modifiers))
      {
        Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");
      }

      try
      {
        collectionDTOclass = loader.loadClass(collectionDTO);
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
        collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");

        Assert.fail("Able to access a setter method on a DTO for an attribute that is [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].");
      }
      catch (NoSuchMethodException e)
      {
        // this is expected
      }

    }
    finally
    {
      collectionCharacter.addItem(MdAttributeConcreteInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getId());
      collectionCharacter.apply();

      loader = GeneratedLoader.isolatedClassLoader();
      colletionClass = loader.loadClass(collection.definesType());
      colletionBaseClass = colletionClass.getSuperclass();

      modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
      if (!Modifier.isPublic(modifiers))
      {
        Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
      }

      // Make sure the accessor method is back
      collectionDTOclass = LoaderDecorator.load(collectionDTO);
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
      collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");
    }
  }

  @Request
  @Test
  public void testDateDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADate", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Date out = (Date) collectionDTOclass.getMethod("getADate").invoke(object);

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    try
    {
      Assert.assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDateNullDTO() throws Exception
  {
    Date in = null;

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADate", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Date out = (Date) collectionDTOclass.getMethod("getADate").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDateTimeDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Date out = (Date) collectionDTOclass.getMethod("getADateTime").invoke(object);

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    try
    {
      Assert.assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDateTimeNullDTO() throws Exception
  {
    Date in = null;

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Date out = (Date) collectionDTOclass.getMethod("getADateTime").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDecimalDTO() throws Exception
  {
    BigDecimal in = new BigDecimal(987654.321);
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    BigDecimal out = ( (BigDecimal) collectionDTOclass.getMethod("getADecimal").invoke(object) );

    try
    {
      Assert.assertEquals(out.doubleValue(), in.doubleValue(), 0);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDecimalNullDTO() throws Exception
  {
    BigDecimal in = null;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    BigDecimal out = ( (BigDecimal) collectionDTOclass.getMethod("getADecimal").invoke(object) );

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDoubleDTO() throws Exception
  {
    double in = 98765.4321;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    double out = (Double) collectionDTOclass.getMethod("getADouble").invoke(object);

    try
    {
      Assert.assertEquals(in, out, 0);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testDoubleNullDTO() throws Exception
  {
    Double in = null;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Double out = (Double) collectionDTOclass.getMethod("getADouble").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testFloatDTO() throws Exception
  {
    float in = 987.654321F;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    float out = (Float) collectionDTOclass.getMethod("getAFloat").invoke(object);

    try
    {
      Assert.assertEquals(in, out, 0);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testFloatNullDTO() throws Exception
  {
    Float in = null;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Float out = (Float) collectionDTOclass.getMethod("getAFloat").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testIntegerDTO() throws Exception
  {
    int in = 9876;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    int out = (Integer) collectionDTOclass.getMethod("getAnInteger").invoke(object);

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testIntegerNullDTO() throws Exception
  {
    Integer in = null;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Integer out = (Integer) collectionDTOclass.getMethod("getAnInteger").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testLongDTO() throws Exception
  {
    long in = 987654321;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setALong", Long.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    long out = (Long) collectionDTOclass.getMethod("getALong").invoke(object);

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testLongNullDTO() throws Exception
  {
    Long in = null;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setALong", Long.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    Long out = (Long) collectionDTOclass.getMethod("getALong").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testReferenceDTO() throws Exception
  {
    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    BusinessDTO in = (BusinessDTO) referenceClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    referenceClass.getMethod("apply").invoke(in);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAReference", referenceClass).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    BusinessDTO out = (BusinessDTO) collectionDTOclass.getMethod("getAReference").invoke(object);

    try
    {
      Assert.assertEquals(in.getId(), out.getId());
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
      referenceClass.getMethod("delete").invoke(in);
    }
  }

  @Request
  @Test
  public void testSymmetricDTO() throws Exception
  {
    String in = "You'll find that they're quite stationary";

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setASym", String.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    String out = (String) collectionDTOclass.getMethod("getASym").invoke(object);

    try
    {
      Assert.assertEquals("", out); // the return value should be empty for a
                                    // DTO (for
      // security)
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testGetTextDTO() throws Exception
  {
    String in = "Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setAText", String.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testTimeDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setATime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    try
    {
      Assert.assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testTimeNullDTO() throws Exception
  {
    Date in = null;

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setATime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    try
    {
      Assert.assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testIsReadableDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setATime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    boolean out = (Boolean) collectionClass.getMethod("isATimeReadable").invoke(object);

    try
    {
      Assert.assertTrue(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testIsWritableDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("setATime", Date.class).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);
    boolean out = (Boolean) collectionClass.getMethod("isATimeWritable").invoke(object);

    try
    {
      Assert.assertTrue(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testEnumDTO() throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Class<?> enumDTOclass = LoaderDecorator.load(suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

    Object in = enumDTOclass.getDeclaredField(heartName).get(null);

    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("addAnEnum", enumDTOclass).invoke(object, in);
    collectionDTOclass.getMethod("apply").invoke(object);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(object);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    object = get.invoke(null, _clientReaqest, id);

    List<?> enums = (List<?>) collectionDTOclass.getMethod("getAnEnum").invoke(object);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();

    try
    {
      Assert.assertEquals(heartName, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testRemoveEnumDTO() throws Exception
  {
    this.testRemoveEnumDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testRemoveEnumDTO(String sessionId) throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Class<?> enumDTOclass = LoaderDecorator.load(suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX);

    Object in = enumDTOclass.getDeclaredField(heartName).get(null);

    Object objectDTO = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    collectionDTOclass.getMethod("addAnEnum", enumDTOclass).invoke(objectDTO, in);
    collectionDTOclass.getMethod("apply").invoke(objectDTO);
    String id = (String) collectionDTOclass.getMethod("getId").invoke(objectDTO);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    objectDTO = get.invoke(null, _clientReaqest, id);

    // Remove the enum item
    collectionDTOclass.getMethod("removeAnEnum", enumDTOclass).invoke(objectDTO, in);
    collectionDTOclass.getMethod("apply").invoke(objectDTO);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    get = collectionClass.getMethod("get", String.class);
    SessionComponent object = (SessionComponent) get.invoke(null, id);

    TransientDAO transientDAO = object.getTransientDAO();
    Set<String> out = ( (AttributeEnumerationIF) transientDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    try
    {
      Assert.assertEquals(0, out.size());
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  @Request
  @Test
  public void testSetStructBusiness() throws Exception
  {
    this.testSetStructBusiness(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testSetStructBusiness(String sessionId) throws Exception
  {
    String in = MdAttributeBooleanInfo.TRUE;
    SessionComponent object = BusinessFacade.newSessionComponent(collection.definesType());
    Struct struct = object.getGenericStruct("aStruct");

    struct.setValue("structBoolean", in);
    object.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    object = (SessionComponent) get.invoke(null, object.getId());

    TransientDAO transientDAO = object.getTransientDAO();
    String out = transientDAO.getStructValue("aStruct", "structBoolean");

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      object.delete();
    }
  }

  @Request
  @Test
  public void testSetStructDTO() throws Exception
  {
    this.testSetStructDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testSetStructDTO(String sessionId) throws Exception
  {
    String in = MdAttributeBooleanInfo.TRUE;
    SessionDTO objectDTO = (SessionDTO) _clientReaqest.newMutable(collection.definesType());

    ComponentDTOFacade.getAttributeStructDTO(objectDTO, "aStruct").setValue("structBoolean", in);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(objectDTO, "aStruct");
    Assert.assertEquals(in, struct.getValue("structBoolean"));

    _clientReaqest.createSessionComponent(objectDTO);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    SessionComponent object = (SessionComponent) get.invoke(null, objectDTO.getId());

    TransientDAO transientDAO = object.getTransientDAO();
    String out = transientDAO.getStructValue("aStruct", "structBoolean");

    try
    {
      Assert.assertEquals(in, out);
    }
    finally
    {
      object.delete();
    }
  }

  @Request
  @Test
  public void testAddRemoveEnumerationDTO() throws Exception
  {
    this.testAddRemoveEnumerationDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testAddRemoveEnumerationDTO(String sessionId) throws Exception
  {
    SessionComponent object = BusinessFacade.newSessionComponent(collection.definesType());
    object.setValue("aLong", "142");
    Struct _struct = object.getGenericStruct("aStruct");
    _struct.setValue("structEnumeration", heartsId);
    _struct.setValue("structBoolean", Boolean.toString(false));
    object.apply();

    SessionDTO dto = (SessionDTO) _clientReaqest.get(object.getId());

    AttributeStructDTO structDTO = ComponentDTOFacade.getAttributeStructDTO(dto, "aStruct");
    structDTO.addEnumItem("structEnumeration", clubName);
    structDTO.removeEnumItem("structEnumeration", heartName);
    _clientReaqest.update(dto);

    // Check that the TransientDAO was updated
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    object = (SessionComponent) get.invoke(null, object.getId());

    TransientDAO transientDAO = object.getTransientDAO();
    StructDAO structDAO = ( (AttributeStructIF) transientDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    try
    {
      Assert.assertEquals(1, enums.length);
      Assert.assertEquals(clubsId, enums[0].getId());
    }
    finally
    {
      object.delete();
    }
  }

  @Request
  @Test
  public void testUpdateDTO() throws Exception
  {
    this.testUpdateDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void testUpdateDTO(String sessionId) throws Exception
  {
    long longIn = 142;
    boolean booleanIn = true;

    SessionComponent sessionComponent = BusinessFacade.newSessionComponent(collection.definesType());
    sessionComponent.setValue("aLong", longIn + "");
    Struct _struct = sessionComponent.getGenericStruct("aStruct");
    _struct.setValue("structEnumeration", heartsId);
    _struct.setValue("structBoolean", Boolean.toString(false));
    sessionComponent.apply();

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Class<?> enumClass = LoaderDecorator.load(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    Method get = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, _clientReaqest, sessionComponent.getId());

    Object struct = collectionDTOclass.getMethod("getAStruct").invoke(object);

    // Modify the BusinessDAO through the DTO layer
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, booleanIn);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, clubs);
    structClass.getMethod("removeStructEnumeration", enumClass).invoke(struct, hearts);
    collectionDTOclass.getMethod("setALong", Long.class).invoke(object, longIn);

    // Update the BusinessDAO
    Method updateDTO = collectionDTOclass.getMethod("apply");
    updateDTO.invoke(object);

    // Check that the BusinessDAO was updated
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    get = collectionClass.getMethod("get", String.class);
    sessionComponent = (SessionComponent) get.invoke(null, sessionComponent.getId());

    TransientDAO transientDAO = sessionComponent.getTransientDAO();

    StructDAO structDAO = ( (AttributeStructIF) transientDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    String outBoolean = transientDAO.getStructValue("aStruct", "structBoolean");
    String outLong = transientDAO.getValue("aLong");

    try
    {
      Assert.assertEquals(1, enums.length);
      Assert.assertEquals(clubsId, enums[0].getId());
      Assert.assertEquals(Long.toString(longIn), outLong);
      Assert.assertEquals(Boolean.toString(booleanIn), outBoolean);
    }
    finally
    {
      sessionComponent.delete();
    }
  }

  protected abstract String buildGetMethod();

  @Request
  @Test
  public void testTypeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    // test on a new instance
    Assert.assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    Assert.assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());

    // test on an applied instance (to make sure the proxies persisted the
    // metadata)
    collectionClass.getMethod("apply").invoke(object);
    Assert.assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    Assert.assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());
    Assert.assertEquals(collection.getId(), object.getMd().getId());
  }

  @Request
  @Test
  public void testBlobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ABlob" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionBlob, mdDTO);
  }

  @Request
  @Test
  public void testDateTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADateTime" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDateTime, mdDTO);
  }

  @Request
  @Test
  public void testDateMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADate" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDate, mdDTO);
  }

  @Request
  @Test
  public void testTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ATime" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionTime, mdDTO);
  }

  @Request
  @Test
  public void testBooleanMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ABoolean" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionBoolean, mdDTO);
  }

  @Request
  @Test
  public void testReferenceMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AReference" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionReference, mdDTO);

    Assert.assertEquals(collectionReference.isSystem(), mdDTO.isSystem());
  }

  @Request
  @Test
  public void testIntegerMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AnInteger" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionInteger, mdDTO);
    Assert.assertEquals(Boolean.parseBoolean(collectionInteger.isPositiveRejected()), mdDTO.rejectPositive());
  }

  @Request
  @Test
  public void testLongMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ALong" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionLong, mdDTO);
  }

  @Request
  @Test
  public void testCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ACharacter" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionCharacter, mdDTO);
    Assert.assertEquals(Integer.parseInt(collectionCharacter.getSize()), mdDTO.getSize());
  }

  @Request
  @Test
  public void testStructMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeStructMdDTO mdDTO = (AttributeStructMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AStruct" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionStruct, mdDTO);
    Assert.assertEquals(collectionStruct.getMdStructDAOIF().definesType(), mdDTO.getDefiningMdStruct());
  }

  @Request
  @Test
  public void testDecimalMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADecimal" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDecimal, mdDTO);
  }

  @Request
  @Test
  public void testDoubleMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADouble" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDouble, mdDTO);
  }

  @Request
  @Test
  public void testFloatMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AFloat" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionFloat, mdDTO);
  }

  @Request
  @Test
  public void testEnumerationMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEnumerationMdDTO mdDTO = (AttributeEnumerationMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AnEnum" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionEnumeration, mdDTO);
    Assert.assertEquals(collectionEnumeration.selectMultiple(), mdDTO.selectMultiple());
  }

  @Request
  @Test
  public void testTextMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AText" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionText, mdDTO);
  }

  @Request
  @Test
  public void testClobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AClob" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionClob, mdDTO);
  }

  @Request
  @Test
  public void testHashMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AHash" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionHash, mdDTO);
    Assert.assertEquals(collectionHash.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  @Request
  @Test
  public void testSymmetricMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ASym" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionSymmetric, mdDTO);
    Assert.assertEquals(collectionSymmetric.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  @Request
  @Test
  public void testStructCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    StructDTO structDTO = (StructDTO) collectionClass.getMethod("getAStruct").invoke(object);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) structClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "StructCharacter" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(structDTO);

    checkAttributeMd(structCharacter, mdDTO);
    Assert.assertEquals(Integer.parseInt(structCharacter.getSize()), mdDTO.getSize());
  }

  @Request
  @Test
  public void testFileMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AFile" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionFile, mdDTO);
  }

  /**
   * This method does all the checks for attribute metadata on DTOs. All checks,
   * except for attribute specific metadata, is consolidated here (it's better
   * than copying/pasting the same checks into a dozen different tests).
   *
   * @param mdAttribute
   * @param mdDTO
   */
  private void checkAttributeMd(MdAttributeConcreteDAOIF mdAttribute, AttributeMdDTO mdDTO)
  {
    Assert.assertEquals(mdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()), mdDTO.getDisplayLabel());
    Assert.assertEquals(mdAttribute.getDescription(CommonProperties.getDefaultLocale()), mdDTO.getDescription());
    Assert.assertEquals(mdAttribute.isImmutable(), mdDTO.isImmutable());
    Assert.assertEquals(mdAttribute.isRequired(), mdDTO.isRequired());
    Assert.assertEquals(mdAttribute.getId(), mdDTO.getId());
    Assert.assertEquals(mdAttribute.isSystem(), mdDTO.isSystem());
    Assert.assertEquals(mdAttribute.definesAttribute(), mdDTO.getName());

    if (mdDTO instanceof AttributeNumberMdDTO)
    {
      AttributeNumberMdDTO numberMdDTO = (AttributeNumberMdDTO) mdDTO;
      MdAttributeNumberDAOIF mdAttributeNumber = (MdAttributeNumberDAOIF) mdAttribute;

      Assert.assertEquals(Boolean.parseBoolean(mdAttributeNumber.isZeroRejected()), numberMdDTO.rejectZero());
      Assert.assertEquals(Boolean.parseBoolean(mdAttributeNumber.isNegativeRejected()), numberMdDTO.rejectNegative());
      Assert.assertEquals(Boolean.parseBoolean(mdAttributeNumber.isPositiveRejected()), numberMdDTO.rejectPositive());
    }

    if (mdDTO instanceof AttributeDecMdDTO)
    {
      AttributeDecMdDTO decMdDTO = (AttributeDecMdDTO) mdDTO;
      MdAttributeDecDAOIF mdAttributeDec = (MdAttributeDecDAOIF) mdAttribute;

      Assert.assertEquals(Integer.parseInt(mdAttributeDec.getLength()), decMdDTO.getTotalLength());
      Assert.assertEquals(Integer.parseInt(mdAttributeDec.getDecimal()), decMdDTO.getDecimalLength());
    }
  }
}
