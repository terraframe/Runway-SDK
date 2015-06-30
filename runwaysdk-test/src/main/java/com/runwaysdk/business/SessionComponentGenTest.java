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
package com.runwaysdk.business;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestResult;
import sun.security.provider.Sun;

import com.runwaysdk.ClientSession;
import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.generation.GenerationManager;
import com.runwaysdk.business.generation.dto.ClassDTOBaseGenerator;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.HashMethods;
import com.runwaysdk.constants.LocalProperties;
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
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdSessionInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
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
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
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
import com.runwaysdk.util.FileIO;

@SuppressWarnings("unchecked")
public abstract class SessionComponentGenTest extends TestCase
{
  @Override
  public TestResult run()
  {
    return super.run();
  }

  @Override
  public void run(TestResult testResult)
  {
    super.run(testResult);
  }

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

  private static String                    collectionSubDTO;

  private static ClientSession             systemSession  = null;

  private static ClientRequestIF           _clientReaqest = null;

  protected static String                  superMdSessionAttributeNameHack;

  protected static void classSetUp()
  {
    suitMaster = MdBusinessDAO.newInstance();
    suitMaster.setValue(MdBusinessInfo.NAME, "SuitMaster");
    suitMaster.setValue(MdBusinessInfo.PACKAGE, pack);
    suitMaster.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Enumeration Master List");
    suitMaster.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    suitMaster.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, EnumerationMasterInfo.ID_VALUE);
    suitMaster.setGenerateMdController(false);
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
    struct.setGenerateMdController(false);
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
    reference.setGenerateMdController(false);
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
    collectionSubDTO = collectionSub.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
  }

  protected static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  protected void setUp()
  {
    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    _clientReaqest = systemSession.getRequest();
  }

  @Override
  protected void tearDown() throws Exception
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

  public void ignoreInstance() throws Exception
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
      fail("An applied instance did not match the retrieved instance.");
    }
  }

  public void ignoreLoad() throws Exception
  {
    this.load(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void load(String sessionId) throws Exception
  {
    makeCar();

    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The Top Speed");
    topSpeed.apply();

    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);

    Object object = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(object, 200);
    carClass.getMethod("apply").invoke(object);
    String id = (String) carClass.getMethod("getId").invoke(object);

    SessionComponent sessionComponent = SessionComponent.get(id);

    if (!sessionComponent.getValue("topSpeed").equals("200"))
      fail("setTopSpeed was not invoked correctly");

    topSpeed.delete();
  }

  public void ignoreReLoad() throws Exception
  {
    makeCar();

    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);

    try
    {
      Method setTopSpeedMethod = carClass.getMethod("setTopSpeed", Integer.TYPE);
      Object newObject = carClass.getConstructor().newInstance();
      setTopSpeedMethod.invoke(newObject, 200);
      fail("The class invoked a method that doesn't exist yet");
    }
    catch (NoSuchMethodException e)
    {
      // This is expected
    }

    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The Top Speed");
    topSpeed.apply();

    carClass = LoaderDecorator.load(type);

    // After adding the topSpeed attribute, setTopSpeed should exist
    Object newerObject = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(newerObject, 200);
    carClass.getMethod("apply").invoke(newerObject);

    topSpeed.delete();
  }

  public void ignoreReLoadBlob() throws Exception
  {
    makeCar();

    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);
    byte[] data = "Some blob data".getBytes();

    try
    {
      Method setBlobDataMethod = carClass.getMethod("setBlobData", byte[].class);
      Object newObject = carClass.getConstructor().newInstance();
      setBlobDataMethod.invoke(newObject, data);
      fail("The class invoked a method that doesn't exist yet");
    }
    catch (NoSuchMethodException e)
    {
      // This is expected
    }

    MdAttributeBlobDAO blobdata = MdAttributeBlobDAO.newInstance();
    blobdata.setValue(MdAttributeBlobInfo.NAME, "blobData");
    blobdata.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, car.getId());
    blobdata.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A blob attribute");
    blobdata.apply();

    carClass = LoaderDecorator.load(type);

    // After adding the blobData attribute, setBlobData should exist
    Object newerObject = carClass.getConstructor().newInstance();
    carClass.getMethod("setBlobData", byte[].class).invoke(newerObject, data);
    carClass.getMethod("apply").invoke(newerObject);

    blobdata.delete();
  }

  public void ignoreDelete()
  {
    makeCar();
    car.delete();

    if (MdSessionDAO.isDefined(pack + ".Car"))
      fail("Car was not deleted!");

    makeCar();
  }

  public void ignoreGetBlob() throws Exception
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
        fail("Stored and Retrieved blobs are different sizes.");
      }
      for (int i = 0; i < in.length; i++)
      {
        if (in[i] != out[i])
        {
          fail("Stored and Retrieved blobs have different values.");
        }
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreBoolean() throws Exception
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
        fail("Stored and Retrieved booleans have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetBooleanNull() throws Exception
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
        fail("A Boolean getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreCharacter() throws Exception
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
        fail("Stored and Retrieved Characters have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDecimal() throws Exception
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
        fail("Stored and Retrieved Decimals have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDecimalNull() throws Exception
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
        fail("A Decimal getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDouble() throws Exception
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
        fail("Stored and Retrieved Doubles have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDoubleNull() throws Exception
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
        fail("A Double getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetFloat() throws Exception
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
        fail("Stored and Retrieved Floats have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetFloatNull() throws Exception
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
        fail("A Float getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetHash() throws Exception
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
        fail("Stored Hash did not equal equivalent value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetSymmetric() throws Exception
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
        fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetInteger() throws Exception
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
        fail("Stored and Retrieved Integers have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreIntegerNull() throws Exception
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
        fail("An Integer getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetLong() throws Exception
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
        fail("Stored and Retrieved Longs have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreLongNull() throws Exception
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
        fail("A Long getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetDate() throws Exception
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
        fail("Stored and Retrieved Dates have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetDateNull() throws Exception
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
        fail("A Date getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetDateTime() throws Exception
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
        fail("Stored and Retrieved DateTimes have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetDateTimeNull() throws Exception
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
        fail("A DateTime getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetTime() throws Exception
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
        fail("Stored and Retrieved Times have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetTimeNull() throws Exception
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
        fail("A Time getter method was supposed to return null.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetText() throws Exception
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
        fail("Stored and Retrieved Texts have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreSetStructEnumeration() throws Exception
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

    assertEquals(1, enums.length);
    assertEquals(heartsId, enums[0].getId());
  }

  public void ignoreGetStructEnumeration() throws Exception
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
    List out = (List) structClass.getMethod("getStructEnumeration").invoke(struct);

    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getId").invoke(head);
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    try
    {
      assertEquals(input, structChar);
      assertEquals(1, out.size());
      assertEquals(heartsId, outId);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreStructBoolean() throws Exception
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
        fail("Stored and Retrieved StructBooleans have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreReference() throws Exception
  {
    this.ignoreReference(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreReference(String sessionId) throws Exception
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
        fail("Stored and Retrieved References are different.");
      }
    }
    finally
    {
      referenceClass.getMethod("delete").invoke(in);
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreAddEnum() throws Exception
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
        fail("Expected 1 enum value, found " + ids.size());
      }
      String out = BusinessDAO.get(ids.iterator().next()).getValue(EnumerationMasterInfo.NAME);

      if (!out.equals(diamondName))
      {
        fail("Stored and Retrieved enums have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetEnum() throws Exception
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
        fail("Stored and Retrieved enums have different values.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreRemoveEnum() throws Exception
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
        fail("Failed to remove an enumerated value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreClearEnum() throws Exception
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
        fail("Failed to clear an enumerated value.");
      }
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDeleteDTO() throws Exception
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

      fail("Delete businessDTO did not delete the entity");
    }
    catch (Exception e)
    {
      if (e.getCause() != null && ( e.getCause() instanceof DataNotFoundException ))
      {
        // Expect to land here
      }
      else
      {
        fail(e.getMessage());
      }

    }
  }

  public void ignoreGetBlobDTO() throws Exception
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
        fail("Stored and Retrieved blobs are different sizes.");
      }

      for (int i = 0; i < in.length; i++)
      {
        if (in[i] != out[i])
        {
          fail("Stored and Retrieved blobs have different values.");
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
  public void ignoreGetBooleanDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetBooleanNullDTO() throws Exception
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
      assertNull(null, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetCharacterDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignorePublish() throws Exception
  {
    // Make sure we can instantiate the subclass
    Class<?> collectionSubClass = LoaderDecorator.load(collectionSubDTO);
    Constructor get = collectionSubClass.getConstructor(ClientRequestIF.class);
    get.newInstance(_clientReaqest);

    collection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
    collection.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    collection.apply();

    try
    {
      try
      {
        Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
        get = collectionClass.getConstructor(ClientRequestIF.class);
        get.newInstance(_clientReaqest);

        fail("Able to load a DTO class that was set to not be published");
      }
      catch (RuntimeException ex)
      {
        if (!(ex instanceof LoaderDecoratorExceptionIF))
        {
          throw ex;
        }
      }
      try
      {
        // Make sure we cannot instantiate the subclass
        collectionSubClass = LoaderDecorator.load(collectionSubDTO);
        get = collectionSubClass.getConstructor(ClientRequestIF.class);
        get.newInstance(_clientReaqest);

        fail("Able to load a DTO class that was set to not be published");
      }
      catch (RuntimeException ex)
      {
        if (!(ex instanceof LoaderDecoratorExceptionIF))
        {
          throw ex;
        }
      }
    }
    finally
    {
      collection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
      collection.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
      collection.apply();

      // Make sure we can instantiate the subclass
      collectionSubClass = LoaderDecorator.load(collectionSubDTO);
      get = collectionSubClass.getConstructor(ClientRequestIF.class);
      get.newInstance(_clientReaqest);
    }
  }

  public void ignorePublishReference() throws Exception
  {
    this.publishReference(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void publishReference(String sessionId) throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());
    Object referenceObject = referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(referenceObject);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setAReference", referenceClass).invoke(object, referenceObject);
    collectionClass.getMethod("apply").invoke(object);
    String id = (String) collectionClass.getMethod("getId").invoke(object);

    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object objectDTO = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, _clientReaqest, id);
    collectionDTOclass.getMethod("getAReference").invoke(objectDTO);

    reference = MdBusinessDAO.get(reference.getId()).getBusinessDAO();
    reference.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    reference.apply();

    // Delete and rebuild the objects, as objects stored in the user's session
    // will
    // have a different class loader than the new one.
    collectionClass.getMethod("delete").invoke(object);
    referenceClass.getMethod("delete").invoke(referenceObject);

    referenceClass = LoaderDecorator.load(reference.definesType());
    referenceObject = referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(referenceObject);

    collectionClass = LoaderDecorator.load(collectionType);
    object = collectionClass.getConstructor().newInstance();
    collectionClass.getMethod("setAReference", referenceClass).invoke(object, referenceObject);
    collectionClass.getMethod("apply").invoke(object);
    id = (String) collectionClass.getMethod("getId").invoke(object);

    try
    {
      collectionDTOclass = LoaderDecorator.load(collectionDTO);
      objectDTO = collectionDTOclass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, _clientReaqest, id);
      collectionDTOclass.getMethod("getAReference").invoke(objectDTO);
    }
    catch (NoSuchMethodException e)
    {
      // this is expected
    }
    finally
    {
      reference = MdBusinessDAO.get(reference.getId()).getBusinessDAO();
      reference.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
      reference.apply();

      collectionClass.getMethod("delete").invoke(object);
      referenceClass.getMethod("delete").invoke(referenceObject);
    }
  }

  public void ignoreAttributeGetterVisibility() throws Exception
  {
    this.attributeGetterVisibility(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void attributeGetterVisibility(String sessionId) throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    Class<?> colletionClass = LoaderDecorator.load(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("getACharacter").invoke(object);

    collectionCharacter.addItem(MdAttributeConcreteInfo.GETTER_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    collectionCharacter.apply();
    LoaderDecorator.reload();
    colletionClass = LoaderDecorator.load(collection.definesType());
    colletionBaseClass = colletionClass.getSuperclass();

    try
    {
      modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
      if (!Modifier.isProtected(modifiers))
      {
        fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");
      }

      try
      {
        collectionDTOclass = LoaderDecorator.load(collectionDTO);
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
        collectionDTOclass.getMethod("getACharacter").invoke(object);

        fail("Able to access a getter method on a DTO for an attribute that is [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].");
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
      LoaderDecorator.reload();
      colletionClass = LoaderDecorator.load(collection.definesType());
      colletionBaseClass = colletionClass.getSuperclass();

      modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
      if (!Modifier.isPublic(modifiers))
      {
        fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
      }

      // Make sure the accessor method is back
      collectionDTOclass = LoaderDecorator.load(collectionDTO);
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
      collectionDTOclass.getMethod("getACharacter").invoke(object);
    }
  }

  public void ignoreAttributeSetterVisibility() throws Exception
  {
    this.attributeSetterVisibility(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void attributeSetterVisibility(String sessionId) throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    Class<?> colletionClass = LoaderDecorator.load(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");

    collectionCharacter.addItem(MdAttributeConcreteInfo.SETTER_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    collectionCharacter.apply();
    LoaderDecorator.reload();
    colletionClass = LoaderDecorator.load(collection.definesType());
    colletionBaseClass = colletionClass.getSuperclass();

    try
    {

      modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
      if (!Modifier.isProtected(modifiers))
      {
        fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");
      }

      try
      {
        collectionDTOclass = LoaderDecorator.load(collectionDTO);
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
        collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");

        fail("Able to access a setter method on a DTO for an attribute that is [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].");
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
      LoaderDecorator.reload();
      colletionClass = LoaderDecorator.load(collection.definesType());
      colletionBaseClass = colletionClass.getSuperclass();

      modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
      if (!Modifier.isPublic(modifiers))
      {
        fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
      }

      // Make sure the accessor method is back
      collectionDTOclass = LoaderDecorator.load(collectionDTO);
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
      collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");
    }
  }

  public void ignoreDateDTO() throws Exception
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
      assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDateNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDateTimeDTO() throws Exception
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
      assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDateTimeNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDecimalDTO() throws Exception
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
      assertEquals(out.doubleValue(), in.doubleValue());
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDecimalNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDoubleDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreDoubleNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreFloatDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreFloatNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreIntegerDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreIntegerNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreLongDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreLongNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreReferenceDTO() throws Exception
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
      assertEquals(in.getId(), out.getId());
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
      referenceClass.getMethod("delete").invoke(in);
    }
  }

  public void ignoreSymmetricDTO() throws Exception
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
      assertEquals("", out); // the return value should be empty for a DTO (for
                             // security)
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreGetTextDTO() throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreTimeDTO() throws Exception
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
      assertEquals(sdf.format(in), sdf.format(out));
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreTimeNullDTO() throws Exception
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
      assertNull(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreIsReadableDTO() throws Exception
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
      assertTrue(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreIsWritableDTO() throws Exception
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
      assertTrue(out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreEnumDTO() throws Exception
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

    List enums = (List) collectionDTOclass.getMethod("getAnEnum").invoke(object);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();

    try
    {
      assertEquals(heartName, out);
    }
    finally
    {
      collectionDTOclass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreRemoveEnumDTO() throws Exception
  {
    this.ignoreRemoveEnumDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreRemoveEnumDTO(String sessionId) throws Exception
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
      assertEquals(0, out.size());
    }
    finally
    {
      collectionClass.getMethod("delete").invoke(object);
    }
  }

  public void ignoreSetStructBusiness() throws Exception
  {
    this.ignoreSetStructBusiness(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreSetStructBusiness(String sessionId) throws Exception
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
      assertEquals(in, out);
    }
    finally
    {
      object.delete();
    }
  }

  public void ignoreSetStructDTO() throws Exception
  {
    this.ignoreSetStructDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreSetStructDTO(String sessionId) throws Exception
  {
    String in = MdAttributeBooleanInfo.TRUE;
    SessionDTO objectDTO = (SessionDTO) _clientReaqest.newMutable(collection.definesType());

    ComponentDTOFacade.getAttributeStructDTO(objectDTO, "aStruct").setValue("structBoolean", in);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(objectDTO, "aStruct");
    assertEquals(in, struct.getValue("structBoolean"));

    _clientReaqest.createSessionComponent(objectDTO);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    SessionComponent object = (SessionComponent) get.invoke(null, objectDTO.getId());

    TransientDAO transientDAO = object.getTransientDAO();
    String out = transientDAO.getStructValue("aStruct", "structBoolean");

    try
    {
      assertEquals(in, out);
    }
    finally
    {
      object.delete();
    }
  }

  public void ignoreAddRemoveEnumerationDTO() throws Exception
  {
    this.ignoreAddRemoveEnumerationDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreAddRemoveEnumerationDTO(String sessionId) throws Exception
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
      assertEquals(1, enums.length);
      assertEquals(clubsId, enums[0].getId());
    }
    finally
    {
      object.delete();
    }
  }

  public void ignoreUpdateDTO() throws Exception
  {
    this.ignoreUpdateDTO(_clientReaqest.getSessionId());
  }

  @Request(RequestType.SESSION)
  public void ignoreUpdateDTO(String sessionId) throws Exception
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
      assertEquals(1, enums.length);
      assertEquals(clubsId, enums[0].getId());
      assertEquals(Long.toString(longIn), outLong);
      assertEquals(Boolean.toString(booleanIn), outBoolean);
    }
    finally
    {
      sessionComponent.delete();
    }
  }

  /**
   * Overwrites the source in Collection.java to add some references to
   * test.generated.Car, and then atempts to delete Car. This tests the
   * compiler, ensuring that it finds the dependency.
   *
   * @throws Exception
   */
  public void ignoreDeletedClassStillReferenced() throws Exception
  {
    makeCar();

    String originalCollectionStubSource = new String();
    originalCollectionStubSource = collection.getValue(MdClassInfo.STUB_SOURCE);

    // Build the new source for Collection.java
    String collectionStubSource = "package test.generated;\n" + "import test.generated.Car;\n" + "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS + "{\n" + "  private Car car;\n" + "  public Collection()\n" + "  {\n" + "    super();\n"
        + "    car = new Car();\n" + "  }\n" + "  public static Collection get(String id)\n" + "  {\n" + "     " + this.buildGetMethod() + "  }\n" + "}";

    // Write the new stub, and compile tom ake sure it's valid
    MdSessionDAO updateCollection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
    updateCollection.setValue(MdClassInfo.STUB_SOURCE, collectionStubSource);
    updateCollection.apply();

    try
    {
      // This should cause a compiler exception, since Collection.java now
      // contains references to the Car type we're deleting
      TestFixtureFactory.delete(car);

      // If we get to here, the exception didn't get thrown.
      GenerationManager.generate(collection);
      fail("Class " + car.definesType() + " was deleted even though it is referenced in business code.");
    }
    catch (CompilerException e)
    {
      // This is expected
    }

    // Overwrite our changes to Collection.java
    collection.setValue(MdClassInfo.STUB_SOURCE, originalCollectionStubSource);

    // This will recompile everything. This should work now.
    this.cleanup_testDeletedClassStillReferenced(originalCollectionStubSource);
  }

  protected abstract String buildGetMethod();

  @Transaction
  private void cleanup_testDeletedClassStillReferenced(String stubSource)
  {
    MdSessionDAO updateCollection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
    updateCollection.setValue(MdClassInfo.STUB_SOURCE, stubSource);
    updateCollection.apply();

    TestFixtureFactory.delete(car);
  }

  /**
   * Overwrites the source in Collection.java to add some references to
   * test.generated.Car, and then attempts to delete Car. This tests the
   * compiler, ensuring that it finds the dependency.
   *
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testDeletedAttributeStillReferenced() throws Exception
  {
    makeCar();

    // Add a new 'Top Speed' attribute to car
    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL,  MdAttributeLocalInfo.DEFAULT_LOCALE,  "The Top Speed");
    topSpeed.apply();

    // Get the bytes that represent the new .class file
    File carClassFile = new File(LocalProperties.getServerGenBin(), "/test/generated/Car.class");
    byte[] fileBytes = FileIO.readBytes(carClassFile);

    // The bytes in the database should match those on the filesystem
    byte[] dbBytes = car.getBlob(MdClassInfo.STUB_CLASS);
    ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(dbBytes));
    Map<String, byte[]> map = (Map<String, byte[]>) input.readObject();
    byte[] carBytes = map.get("Car.class");

    for (int i = 0; i < fileBytes.length; i++)
    {
      if (fileBytes[i] != carBytes[i])
      {
        throw new SystemException("Database byte[] does not match filesystem byte[]!");
      }
    }

    String originalCollectionStubSource = collection.getValue(MdClassInfo.STUB_SOURCE);

    // Build the new source for Collection.java
    String collectionStubSource =
      "package test.generated;\n"+
      "import test.generated.Car;\n"+
      "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS +"\n"+
      "{\n"+
      "  private Car car;\n"+
      "  public Collection()\n"+
      "  {\n"+
      "    super();\n"+
      "    car = new Car();\n"+
      "    car.setTopSpeed(120);\n"+
      "  }\n"+
      "  public static Collection get(String id)\n"+
      "  {\n"+
      "     "+this.buildGetMethod()+
      "  }\n"+
      "}";

    // Write the new stub, and compile to make sure it's valid
    MdSessionDAO updateCollection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
    updateCollection.setValue(MdClassInfo.STUB_SOURCE, collectionStubSource);
    updateCollection.apply();

    try
    {
      // This should cause a compiler exception, since Collection.java now
      // contains references to the setter for the attribute we're deleting
      topSpeed.delete();

      // If we get to here, the exception didn't get thrown.
      GenerationManager.generate(collection);
      fail("Class " + car.definesType() + " was deleted even though it is referenced in business code.");
    }
    catch (CompilerException e)
    {
      // This is expected
    }

    // Now acquire the new Car.class bytes - they should not have changed
    carClassFile = new File(LocalProperties.getServerGenBin(), "test/generated/Car.class");
    byte[] newFileBytes = FileIO.readBytes(carClassFile);

    // Check new file bytes against old file bytes
    for (int i = 0; i < fileBytes.length; i++)
    {
      if (fileBytes[i] != newFileBytes[i])
      {
        throw new SystemException("New file byte[] does not match old file byte[]!");
      }
    }

    // The critical test is to see if the source/class for Car got rolled back
    // to a safe state - namely one that still has setTopSpeed(int)
    LoaderDecorator.reload();

    car = (MdSessionDAO) MdTypeDAO.getMdTypeDAO("test.generated.Car");
    Class<?> carClass = LoaderDecorator.load(car.definesType());

    // After adding the topSpeed attribute, setTopSpeed should exist
    Object newerCar = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(newerCar, 200);

    // This will recompile everything. This should work now.
    this.cleanup_testDeletedAttributeStillReferenced(topSpeed, originalCollectionStubSource);
  }

  @Transaction
  private void cleanup_testDeletedAttributeStillReferenced(MdAttributeIntegerDAO topSpeed, String stubSource)
  {
    MdSessionDAO updateCollection = MdSessionDAO.get(collection.getId()).getBusinessDAO();
    updateCollection.setValue(MdClassInfo.STUB_SOURCE, stubSource);
    updateCollection.apply();

    TestFixtureFactory.delete(topSpeed);
  }

  public void ignoreTypeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    // test on a new instance
    assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());

    // test on an applied instance (to make sure the proxies persisted the
    // metadata)
    collectionClass.getMethod("apply").invoke(object);
    assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());
    assertEquals(collection.getId(), object.getMd().getId());
  }

  public void ignoreBlobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ABlob" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionBlob, mdDTO);
  }

  public void ignoreDateTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADateTime" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDateTime, mdDTO);
  }

  public void ignoreDateMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADate" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDate, mdDTO);
  }

  public void ignoreTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ATime" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionTime, mdDTO);
  }

  public void ignoreBooleanMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ABoolean" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionBoolean, mdDTO);
  }

  public void ignoreReferenceMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AReference" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionReference, mdDTO);

    assertEquals(collectionReference.isSystem(), mdDTO.isSystem());
  }

  public void ignoreIntegerMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AnInteger" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionInteger, mdDTO);
    assertEquals(Boolean.parseBoolean(collectionInteger.isPositiveRejected()), mdDTO.rejectPositive());
  }

  public void ignoreLongMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ALong" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionLong, mdDTO);
  }

  public void ignoreCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ACharacter" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionCharacter, mdDTO);
    assertEquals(Integer.parseInt(collectionCharacter.getSize()), mdDTO.getSize());
  }

  public void ignoreStructMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeStructMdDTO mdDTO = (AttributeStructMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AStruct" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionStruct, mdDTO);
    assertEquals(collectionStruct.getMdStructDAOIF().definesType(), mdDTO.getDefiningMdStruct());
  }

  public void ignoreDecimalMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADecimal" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDecimal, mdDTO);
  }

  public void ignoreDoubleMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ADouble" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionDouble, mdDTO);
  }

  public void ignoreFloatMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AFloat" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionFloat, mdDTO);
  }

  public void ignoreEnumerationMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEnumerationMdDTO mdDTO = (AttributeEnumerationMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AnEnum" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionEnumeration, mdDTO);
    assertEquals(collectionEnumeration.selectMultiple(), mdDTO.selectMultiple());
  }

  public void ignoreTextMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AText" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionText, mdDTO);
  }

  public void ignoreClobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AClob" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionClob, mdDTO);
  }

  public void ignoreHashMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "AHash" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionHash, mdDTO);
    assertEquals(collectionHash.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  public void ignoreSymmetricMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "ASym" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(object);

    checkAttributeMd(collectionSymmetric, mdDTO);
    assertEquals(collectionSymmetric.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  public void ignoreStructCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
    SessionDTO object = (SessionDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(_clientReaqest);
    StructDTO structDTO = (StructDTO) collectionClass.getMethod("getAStruct").invoke(object);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) structClass.getMethod(ClassDTOBaseGenerator.ATTRIBUTE_MD_PREFIX + "StructCharacter" + ClassDTOBaseGenerator.ATTRIBUTE_MD_SUFFIX).invoke(structDTO);

    checkAttributeMd(structCharacter, mdDTO);
    assertEquals(Integer.parseInt(structCharacter.getSize()), mdDTO.getSize());
  }

  public void ignoreFileMetadata() throws Exception
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
    assertEquals(mdAttribute.getDisplayLabel(CommonProperties.getDefaultLocale()), mdDTO.getDisplayLabel());
    assertEquals(mdAttribute.getDescription(CommonProperties.getDefaultLocale()), mdDTO.getDescription());
    assertEquals(mdAttribute.isImmutable(), mdDTO.isImmutable());
    assertEquals(mdAttribute.isRequired(), mdDTO.isRequired());
    assertEquals(mdAttribute.getId(), mdDTO.getId());
    assertEquals(mdAttribute.isSystem(), mdDTO.isSystem());
    assertEquals(mdAttribute.definesAttribute(), mdDTO.getName());

    if (mdDTO instanceof AttributeNumberMdDTO)
    {
      AttributeNumberMdDTO numberMdDTO = (AttributeNumberMdDTO) mdDTO;
      MdAttributeNumberDAOIF mdAttributeNumber = (MdAttributeNumberDAOIF) mdAttribute;

      assertEquals(Boolean.parseBoolean(mdAttributeNumber.isZeroRejected()), numberMdDTO.rejectZero());
      assertEquals(Boolean.parseBoolean(mdAttributeNumber.isNegativeRejected()), numberMdDTO.rejectNegative());
      assertEquals(Boolean.parseBoolean(mdAttributeNumber.isPositiveRejected()), numberMdDTO.rejectPositive());
    }

    if (mdDTO instanceof AttributeDecMdDTO)
    {
      AttributeDecMdDTO decMdDTO = (AttributeDecMdDTO) mdDTO;
      MdAttributeDecDAOIF mdAttributeDec = (MdAttributeDecDAOIF) mdAttribute;

      assertEquals(Integer.parseInt(mdAttributeDec.getLength()), decMdDTO.getTotalLength());
      assertEquals(Integer.parseInt(mdAttributeDec.getDecimal()), decMdDTO.getDecimalLength());
    }
  }
}
