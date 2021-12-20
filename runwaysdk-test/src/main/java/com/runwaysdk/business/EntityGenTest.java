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
package com.runwaysdk.business;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runwaysdk.ClasspathTestRunner;
import com.runwaysdk.ClientSession;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.EntityCacheMaster;
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
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLocalTextInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeSymmetricInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdTypeInfo;
import com.runwaysdk.constants.ServerConstants;
import com.runwaysdk.constants.SymmetricMethods;
import com.runwaysdk.constants.TestConstants;
import com.runwaysdk.constants.TypeGeneratorInfo;
import com.runwaysdk.constants.VisibilityModifier;
import com.runwaysdk.dataaccess.AttributeEnumerationIF;
import com.runwaysdk.dataaccess.AttributeReferenceIF;
import com.runwaysdk.dataaccess.AttributeStructIF;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.BusinessDAOIF;
import com.runwaysdk.dataaccess.EntityDAO;
import com.runwaysdk.dataaccess.MdAttributeConcreteDAOIF;
import com.runwaysdk.dataaccess.MdAttributeDecDAOIF;
import com.runwaysdk.dataaccess.MdAttributeNumberDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.RelationshipDAOIF;
import com.runwaysdk.dataaccess.StructDAO;
import com.runwaysdk.dataaccess.StructDAOIF;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeSymmetricDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdPackage;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.generation.loader.GeneratedLoader;
import com.runwaysdk.query.OIterator;
import com.runwaysdk.session.Request;
import com.runwaysdk.transport.attributes.AttributeStructDTO;
import com.runwaysdk.transport.metadata.AttributeCharacterMdDTO;
import com.runwaysdk.transport.metadata.AttributeDecMdDTO;
import com.runwaysdk.transport.metadata.AttributeEncryptionMdDTO;
import com.runwaysdk.transport.metadata.AttributeEnumerationMdDTO;
import com.runwaysdk.transport.metadata.AttributeMdDTO;
import com.runwaysdk.transport.metadata.AttributeNumberMdDTO;
import com.runwaysdk.transport.metadata.AttributeStructMdDTO;
import com.runwaysdk.transport.metadata.AttributeTermMdDTO;
import com.runwaysdk.util.Base64;

import sun.security.provider.Sun;

@SuppressWarnings("unchecked")
@RunWith(ClasspathTestRunner.class)
public class EntityGenTest
{
  final static Logger                         logger          = LoggerFactory.getLogger(EntityGenTest.class);

  public static final String                  path            = TestConstants.Path.XMLFiles + "/";

  public static final String                  EXCEPTION_XML   = path + "customException.xml";

  private static final String                 pack            = "test.generated";

  private static MdBusinessDAO                car;

  private static MdBusinessDAO                collection;

  private static MdBusinessDAO                collectionSub;

  private static MdAttributeBlobDAO           collectionBlob;

  private static MdAttributeCharacterDAO      collectionCharacter;

  private static MdAttributeCharacterDAO      structCharacter;

  private static MdAttributeIntegerDAO        collectionInteger;

  private static MdAttributeLongDAO           collectionLong;

  private static MdAttributeDecimalDAO        collectionDecimal;

  private static MdAttributeDoubleDAO         collectionDouble;

  private static MdAttributeFloatDAO          collectionFloat;

  private static MdAttributeDateTimeDAO       collectionDateTime;

  private static MdAttributeDateDAO           collectionDate;

  private static MdAttributeTimeDAO           collectionTime;

  private static MdAttributeBooleanDAO        collectionBoolean;

  private static MdAttributeReferenceDAO      collectionReference;

  private static MdAttributeEnumerationDAO    collectionEnumeration;

  private static MdAttributeStructDAO         collectionStruct;

  private static MdAttributeHashDAO           collectionHash;

  private static MdAttributeLocalCharacterDAO collectionLocalChar;

  private static MdAttributeLocalTextDAO      collectionLocalText;

  private static MdAttributeSymmetricDAO      collectionSymmetric;

  private static MdAttributeTextDAO           collectionText;

  private static MdAttributeClobDAO           collectionClob;

  private static MdAttributeFileDAO           collectionFile;

  private static MdAttributeTermDAO           collectionTerm;

  private static MdAttributeBlobDAO           enumBlob;

  private static MdStructDAO                  struct;

  private static MdBusinessDAO                reference;

  private static MdBusinessDAO                term;

  private static String                       collectionType;

  private static MdEnumerationDAO             suitEnum;

  private static MdBusinessDAO                suitMaster;

  private static String                       heartsId;

  private static String                       heartName       = "HEARTS";

  private static String                       clubsId;

  private static String                       clubName        = "CLUBS";

  private static String                       diamondsName    = "DIAMONDS";

  private static String                       spadesName      = "SPADES";

  private static MdRelationshipDAO            mdRelationship;

  private static String                       collectionDTO;

  private static String                       referenceDTO;

  private static String                       relationshipDTO;

  private static String                       collectionSubDTO;

  private static ClientSession                systemSession   = null;

  private static ClientRequestIF              clientRequestIF = null;

  private static String                       suitEnumDTO;

  private boolean                             didSetup        = false;

  private boolean                             didTeardown     = false;

  @Request
  @Before
  public void setUp()
  {
    // if (didSetup == false)
    // {
    // didSetup = true;
    // classSetUp();
    // }

    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequestIF = systemSession.getRequest();

    if (!MdBusinessDAO.isDefined(pack + ".Car"))
    {
      makeCar();
    }
  }

  @Request
  @After
  public void tearDown()
  {
    // if (didTeardown == false)
    // {
    // didTeardown = true;
    // classTearDown();
    // }

    systemSession.logout();
  }

  @Request
  @BeforeClass
  public static void classSetUp()
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
    suitEnum.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, suitMaster.getOid());
    suitEnum.apply();

    struct = MdStructDAO.newInstance();
    struct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Standalone Class");
    struct.setValue(MdStructInfo.PACKAGE, pack);
    struct.setValue(MdStructInfo.NAME, "Standalone");
    struct.apply();

    MdAttributeEnumerationDAO structEnumeration = MdAttributeEnumerationDAO.newInstance();
    structEnumeration.setValue(MdAttributeEnumerationInfo.NAME, "structEnumeration");
    structEnumeration.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Enumeration");
    structEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, struct.getOid());
    structEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitEnum.getOid());
    structEnumeration.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    structEnumeration.apply();

    enumBlob = MdAttributeBlobDAO.newInstance();
    enumBlob.setValue(MdAttributeBlobInfo.NAME, "pic");
    enumBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Pic");
    enumBlob.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Suit Pic Desc");
    enumBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    enumBlob.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    enumBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, suitMaster.getOid());
    enumBlob.apply();

    MdAttributeStructDAO enumStruct = MdAttributeStructDAO.newInstance();
    enumStruct.setValue(MdAttributeStructInfo.NAME, "enumStruct");
    enumStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Struct on an Enum");
    enumStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, suitMaster.getOid());
    enumStruct.setValue(MdAttributeStructInfo.MD_STRUCT, struct.getOid());
    enumStruct.apply();

    structCharacter = MdAttributeCharacterDAO.newInstance();
    structCharacter.setValue(MdAttributeCharacterInfo.NAME, "structCharacter");
    structCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Character");
    structCharacter.setStructValue(MdAttributeCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Character Desc");
    structCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    structCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    structCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    structCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, struct.getOid());
    structCharacter.apply();

    MdAttributeBooleanDAO structBoolean = MdAttributeBooleanDAO.newInstance();
    structBoolean.setValue(MdAttributeBooleanInfo.NAME, "structBoolean");
    structBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    structBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    structBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Boolean");
    structBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, struct.getOid());
    structBoolean.apply();

    BusinessDAO enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, clubName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clubs");
    enum_item.setBlob("pic", new byte[] { 1, 2, 3, 4 });
    clubsId = enum_item.apply();

    enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, diamondsName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Diamonds");
    enum_item.setBlob("pic", new byte[] { 2, 4, 6, 8 });
    enum_item.apply();

    enum_item = BusinessDAO.newInstance(suitMaster.definesType());
    enum_item.setValue(EnumerationMasterInfo.NAME, heartName);
    enum_item.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Hearts");
    enum_item.setBlob("pic", new byte[] { 3, 6, 9 });
    heartsId = enum_item.apply();
    heartName = enum_item.getValue(EnumerationMasterInfo.NAME);

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

    term = MdTermDAO.newInstance();
    term.setValue(MdBusinessInfo.NAME, "Term");
    term.setValue(MdBusinessInfo.PACKAGE, pack);
    term.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Class");
    term.apply();

    MdAttributeIntegerDAO referenceInt = MdAttributeIntegerDAO.newInstance();
    referenceInt.setValue(MdAttributeIntegerInfo.NAME, "referenceInt");
    referenceInt.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Integer");
    referenceInt.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, reference.getOid());
    referenceInt.apply();

    collection = MdBusinessDAO.newInstance();
    collection.setValue(MdBusinessInfo.NAME, "Collection");
    collection.setValue(MdBusinessInfo.PACKAGE, pack);
    collection.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Class");
    collection.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collection.apply();
    collectionType = collection.definesType();

    collectionSub = MdBusinessDAO.newInstance();
    collectionSub.setValue(MdBusinessInfo.NAME, "CollectionSub");
    collectionSub.setValue(MdBusinessInfo.PACKAGE, pack);
    collectionSub.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collectionSub.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    collectionSub.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collectionSub.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, collection.getOid());
    collectionSub.apply();

    collectionBlob = MdAttributeBlobDAO.newInstance();
    collectionBlob.setValue(MdAttributeBlobInfo.NAME, "aBlob");
    collectionBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob");
    collectionBlob.setStructValue(MdAttributeBlobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Blob desc");
    collectionBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionBlob.setValue(MdAttributeBlobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionBlob.apply();

    collectionBoolean = MdAttributeBooleanDAO.newInstance();
    collectionBoolean.setValue(MdAttributeBooleanInfo.NAME, "aBoolean");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Boolean desc");
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    collectionBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, collection.getOid());
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
    collectionCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionCharacter.apply();

    collectionDecimal = MdAttributeDecimalDAO.newInstance();
    collectionDecimal.setValue(MdAttributeDecimalInfo.NAME, "aDecimal");
    collectionDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal");
    collectionDecimal.setStructValue(MdAttributeDecimalInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Decimal desc");
    collectionDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, collection.getOid());
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
    collectionDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, collection.getOid());
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
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionEnumeration.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, suitEnum.getOid());
    collectionEnumeration.apply();

    collectionFloat = MdAttributeFloatDAO.newInstance();
    collectionFloat.setValue(MdAttributeFloatInfo.NAME, "aFloat");
    collectionFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float");
    collectionFloat.setStructValue(MdAttributeFloatInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Float Desc");
    collectionFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, collection.getOid());
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
    collectionHash.setValue(MdAttributeHashInfo.HASH_METHOD, HashMethods.MD5.getOid());
    collectionHash.setValue(MdAttributeHashInfo.DEFINING_MD_CLASS, collection.getOid());
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
    collectionInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionInteger.apply();

    collectionLong = MdAttributeLongDAO.newInstance();
    collectionLong.setValue(MdAttributeLongInfo.NAME, "aLong");
    collectionLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long");
    collectionLong.setStructValue(MdAttributeLongInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Long Desc");
    collectionLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionLong.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLong.setValue(MdAttributeLongInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_ZERO, MdAttributeBooleanInfo.TRUE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    collectionLong.setValue(MdAttributeLongInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    collectionLong.apply();

    collectionLocalChar = MdAttributeLocalCharacterDAO.newInstance();
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.NAME, "aLocalCharacter");
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLocalChar.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Character");
    collectionLocalChar.setStructValue(MdAttributeLocalCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Character Desc");
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionLocalChar.apply();

    // collectionLocalChar.addDefaultLocale();

    collectionLocalText = MdAttributeLocalTextDAO.newInstance();
    collectionLocalText.setValue(MdAttributeLocalTextInfo.NAME, "aLocalText");
    collectionLocalText.setValue(MdAttributeLocalTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLocalText.setValue(MdAttributeLocalTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLocalText.setStructValue(MdAttributeLocalTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Text");
    collectionLocalText.setStructValue(MdAttributeLocalTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Text Desc");
    collectionLocalText.setValue(MdAttributeLocalTextInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionLocalText.apply();

    // collectionLocalText.addDefaultLocale();

    collectionDate = MdAttributeDateDAO.newInstance();
    collectionDate.setValue(MdAttributeDateInfo.NAME, "aDate");
    collectionDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date");
    collectionDate.setStructValue(MdAttributeDateInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Date Desc");
    collectionDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDate.setValue(MdAttributeDateInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionDate.apply();

    collectionDateTime = MdAttributeDateTimeDAO.newInstance();
    collectionDateTime.setValue(MdAttributeDateTimeInfo.NAME, "aDateTime");
    collectionDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime");
    collectionDateTime.setStructValue(MdAttributeDateTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A DateTime Desc");
    collectionDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionDateTime.setValue(MdAttributeDateTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionDateTime.apply();

    collectionTime = MdAttributeTimeDAO.newInstance();
    collectionTime.setValue(MdAttributeTimeInfo.NAME, "aTime");
    collectionTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time");
    collectionTime.setStructValue(MdAttributeTimeInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Time Desc");
    collectionTime.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionTime.setValue(MdAttributeTimeInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionTime.apply();

    collectionReference = MdAttributeReferenceDAO.newInstance();
    collectionReference.setValue(MdAttributeReferenceInfo.NAME, "aReference");
    collectionReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference");
    collectionReference.setStructValue(MdAttributeReferenceInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Reference Desc");
    collectionReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionReference.setValue(MdAttributeReferenceInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, reference.getOid());
    collectionReference.apply();

    collectionTerm = MdAttributeTermDAO.newInstance();
    collectionTerm.setValue(MdAttributeTermInfo.NAME, "aTerm");
    collectionTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term");
    collectionTerm.setStructValue(MdAttributeTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term Desc");
    collectionTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionTerm.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionTerm.setValue(MdAttributeTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, term.getOid());
    collectionTerm.apply();

    collectionFile = MdAttributeFileDAO.newInstance();
    collectionFile.setValue(MdAttributeFileInfo.NAME, "aFile");
    collectionFile.setStructValue(MdAttributeFileInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File");
    collectionFile.setStructValue(MdAttributeFileInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A File Desc");
    collectionFile.setValue(MdAttributeFileInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionFile.setValue(MdAttributeFileInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionFile.setValue(MdAttributeFileInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionFile.apply();

    collectionStruct = MdAttributeStructDAO.newInstance();
    collectionStruct.setValue(MdAttributeStructInfo.NAME, "aStruct");
    collectionStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct");
    collectionStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionStruct.setStructValue(MdAttributeStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Desc");
    collectionStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionStruct.setValue(MdAttributeStructInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionStruct.setValue(MdAttributeStructInfo.MD_STRUCT, struct.getOid());
    collectionStruct.apply();

    collectionSymmetric = MdAttributeSymmetricDAO.newInstance();
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.NAME, "aSym");
    collectionSymmetric.setStructValue(MdAttributeSymmetricInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Attribute");
    collectionSymmetric.setStructValue(MdAttributeSymmetricInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Symmetric Desc");
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.SYMMETRIC_METHOD, SymmetricMethods.DES.getOid());
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.SECRET_KEY_SIZE, "56");
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionSymmetric.setValue(MdAttributeSymmetricInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionSymmetric.apply();

    collectionText = MdAttributeTextDAO.newInstance();
    collectionText.setValue(MdAttributeTextInfo.NAME, "aText");
    collectionText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text");
    collectionText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionText.setValue(MdAttributeTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionText.setStructValue(MdAttributeTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Text Desc");
    collectionText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionText.apply();

    collectionClob = MdAttributeClobDAO.newInstance();
    collectionClob.setValue(MdAttributeClobInfo.NAME, "aClob");
    collectionClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob");
    collectionClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionClob.setValue(MdAttributeClobInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionClob.setStructValue(MdAttributeClobInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Clob Desc");
    collectionClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, collection.getOid());
    collectionClob.apply();

    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, "ARelationship");
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, collection.getOid());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "RelParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, reference.getOid());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "RelChild");
    mdRelationship.apply();

    collectionDTO = collection.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    collectionSubDTO = collectionSub.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    suitEnumDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    new MdPackage(pack).delete();
  }

  private static void makeCar()
  {
    car = MdBusinessDAO.newInstance();
    car.setValue(MdTypeInfo.NAME, "Car");
    car.setValue(MdTypeInfo.PACKAGE, pack);
    car.setValue(MdElementInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    car.setValue(MdElementInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    car.setStructValue(MdTypeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A car");
    car.apply();
  }

  @Request
  @Test
  public void testInstance() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    String oid = businessDAO.apply();

    Business business = Business.get(oid);

    if (!oid.equals(business.getOid()))
    {
      Assert.fail("An applied instance did not match the retrieved instance.");
    }
  }

  @Request
  @Test
  public void testNewStructWithClassType() throws Exception
  {
    try
    {
      BusinessFacade.newStruct(collectionType);
      Assert.fail("Allowed a Struct to be created for a business type");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testNewElementWithStructType() throws Exception
  {
    try
    {
      BusinessFacade.newBusiness(struct.definesType());
      Assert.fail("Allowed a Element to be created for a struct type");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
  }

  @Request
  @Test
  public void testLoad() throws Exception
  {
    int original = EntityDAO.getEntityIdsDB(car.definesType()).size();

    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getOid());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The Top Speed");
    topSpeed.apply();

    GeneratedLoader loader = GeneratedLoader.isolatedClassLoader();

    String type = car.definesType();
    Class<?> carClass = loader.loadClass(type);

    Object newCar = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(newCar, 200);
    carClass.getMethod("apply").invoke(newCar);

    List<String> ids = EntityDAO.getEntityIdsDB(car.definesType());

    if (ids.size() != ( original + 1 ))
      Assert.fail("Expected to find 1 Car, but found " + ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    if (!businessDAOIF.getValue("topSpeed").equals("200"))
      Assert.fail("setTopSpeed was not invoked correctly");

    topSpeed.delete();
  }

  @Request
  @Test
  public void testDelete()
  {
    MdBusinessDAO.get(car.getOid()).getBusinessDAO().delete();

    if (MdBusinessDAO.isDefined(pack + ".Car"))
      Assert.fail("Car was not deleted!");

    makeCar();
  }

  @Request
  @Test
  public void testSetBlob() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAOIF = BusinessDAO.get(oid);
    byte[] out = businessDAOIF.getBlob("aBlob");

    if (in.length != out.length)
      Assert.fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        Assert.fail("Stored and Retrieved blobs have different values.");
  }

  @Request
  @Test
  public void testGetBlob() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setBlob("aBlob", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    byte[] out = (byte[]) collectionClass.getMethod("getABlob").invoke(object);

    if (in.length != out.length)
      Assert.fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        Assert.fail("Stored and Retrieved blobs have different values.");
  }

  @Request
  @Test
  public void testSetBoolean() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    boolean in = true;
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    boolean out = Boolean.parseBoolean(businessDAO.getValue("aBoolean"));

    if (in != out)
      Assert.fail("Stored and Retrieved booleans have different values.");
  }

  @Request
  @Test
  public void testGetBoolean() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = false;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", Boolean.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    if (in != out)
      Assert.fail("Stored and Retrieved booleans have different values.");
  }

  @Request
  @Test
  public void testGetBooleanNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    if (out != null)
    {
      Assert.fail("A Boolean getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "Mr. Sparkle";
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aCharacter");

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Characters have different values.");
  }

  @Request
  @Test
  public void testGetCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "RunwaySDK";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aCharacter", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    String out = (String) collectionClass.getMethod("getACharacter").invoke(object);

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Characters have different values.");
  }

  @Request
  @Test
  public void testSetDecimal() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    BigDecimal in = new BigDecimal(123456.789);

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAOIF = BusinessDAO.get(oid);

    BigDecimal out = new BigDecimal(businessDAOIF.getValue("aDecimal"));

    if (in.subtract(out).abs().doubleValue() > .0000001)
      Assert.fail("Stored and Retrieved Decimals have different values.");
  }

  @Request
  @Test
  public void testGetDecimal() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    double in = 987654.321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", Double.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    double out = ( (BigDecimal) collectionClass.getMethod("getADecimal").invoke(object) ).doubleValue();

    if (in != out)
      Assert.fail("Stored and Retrieved Decimals have different values.");
  }

  @Request
  @Test
  public void testGetDecimalNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Double out = (Double) collectionClass.getMethod("getADecimal").invoke(object);

    if (out != null)
    {
      Assert.fail("A Decimal getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetDouble() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    double in = 123456.789;
    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    double out = Double.parseDouble(businessDAO.getValue("aDouble"));

    if (in != out)
      Assert.fail("Stored and Retrieved Doubles have different values.");
  }

  @Request
  @Test
  public void testGetDouble() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    double in = 98765.4321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", Double.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    if (in != out)
      Assert.fail("Stored and Retrieved Doubles have different values.");
  }

  @Request
  @Test
  public void testGetDoubleNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    if (out != null)
    {
      Assert.fail("A Double getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetFloat() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    float in = 123456.789F;
    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    float out = Float.parseFloat(businessDAO.getValue("aFloat"));

    Assert.assertEquals("Stored and Retrieved Floats have different values: [" + in + "][" + out + "]", in, out, 0.001);
  }

  @Request
  @Test
  public void testGetFloat() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    float in = 987.654321F;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", Float.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    Assert.assertEquals("Stored and Retrieved Floats have different values.", in, out, 0.001);
  }

  @Request
  @Test
  public void testGetFloatNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    if (out != null)
    {
      Assert.fail("A Float getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetHash() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "When you win, say nothing. When you lose, say less.";
    collectionClass.getMethod("setAHash", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
    digest.update(in.getBytes());
    String hash = Base64.encodeToString(digest.digest(), false);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aHash");

    if (!hash.equals(out))
      Assert.fail("Stored and Retrieved Hashes have different values.");
  }

  @Request
  @Test
  public void testHashEquals() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "For breakfast, I had some Pringles, and some fudge-striped cook-ays";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aHash", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    boolean out = (Boolean) collectionClass.getMethod("aHashEquals", String.class).invoke(object, "For breakfast, I had some Pringles, and some fudge-striped cook-ays");

    if (!out)
      Assert.fail("Stored Hash did not equal equivalent value.");
  }

  @Request
  @Test
  public void testSetSymmetric() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "My rims never spin - to the contrary";
    collectionClass.getMethod("setASym", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aSym");

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
  }

  @Request
  @Test
  public void testGetSymmetric() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "You'll find that they're quite stationary";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aSym", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    String out = (String) collectionClass.getMethod("getASym").invoke(object);

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
  }

  @Request
  @Test
  public void testSetInteger() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    int in = 1234;
    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    int out = Integer.parseInt(businessDAO.getValue("anInteger"));

    if (in != out)
      Assert.fail("Stored and Retrieved Integers have different values.");
  }

  @Request
  @Test
  public void testGetInteger() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    int in = 9876;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", Integer.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    int out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    if (in != out)
      Assert.fail("Stored and Retrieved Integers have different values.");
  }

  @Request
  @Test
  public void testGetIntegerNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Integer out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    if (out != null)
    {
      Assert.fail("An Integer getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetLong() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    long in = 123456789;
    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    long out = Long.parseLong(businessDAO.getValue("aLong"));

    if (in != out)
      Assert.fail("Stored and Retrieved Longs have different values.");
  }

  @Request
  @Test
  public void testGetLong() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    long in = 987654321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", Long.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    if (in != out)
      Assert.fail("Stored and Retrieved Longs have different values.");
  }

  @Request
  @Test
  public void testSetLocalCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Struct struct = (Struct) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      Assert.fail("Stored and Retrieved Local character have different values.");
    }
  }

  @Request
  @Test
  public void testGetLocalCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321);
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Struct struct = (Struct) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      Assert.fail("Stored and Retrieved Local character have different values.");
    }
  }

  @Request
  @Test
  public void testSetLocalText() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Struct struct = (Struct) collectionClass.getMethod("getALocalText").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      Assert.fail("Stored and Retrieved Local text have different values.");
    }
  }

  @Request
  @Test
  public void testGetLocalText() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321);
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Struct struct = (Struct) collectionClass.getMethod("getALocalText").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      Assert.fail("Stored and Retrieved local text have different values.");
    }
  }

  @Request
  @Test
  public void testGetLongNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    if (out != null)
    {
      Assert.fail("A Long getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetDate() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aDate"));

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved Dates have different values.");
  }

  @Request
  @Test
  public void testGetDate() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved Dates have different values.");
  }

  @Request
  @Test
  public void testGetDateNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    if (out != null)
    {
      Assert.fail("A Date getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetDateTime() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aDateTime"));

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved DateTimes have different values.");
  }

  @Request
  @Test
  public void testGetDateTime() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved DateTimes have different values.");
  }

  @Request
  @Test
  public void testGetDateTimeNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    if (out != null)
    {
      Assert.fail("A DateTime getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetTime() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);
    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aTime"));

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved Times have different values.");
  }

  @Request
  @Test
  public void testGetTime() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      Assert.fail("Stored and Retrieved Times have different values.");
  }

  @Request
  @Test
  public void testGetTimeNull() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);

    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    if (out != null)
    {
      Assert.fail("A Time getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  @Request
  @Test
  public void testSetText() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";
    collectionClass.getMethod("setAText", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aText");

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Texts have different values.");
  }

  @Request
  @Test
  public void testSetClob() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "CLOB: But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";
    collectionClass.getMethod("setAClob", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aClob");

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Clobs have different values.");
  }

  @Request
  @Test
  public void testGetText() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aText", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Texts have different values. In value: " + in + " Out value: " + out);
  }

  @Request
  @Test
  public void testGetClob() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "CLOB: Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aClob", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    String out = (String) collectionClass.getMethod("getAClob").invoke(object);

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved Clobs have different values. In value: " + in + " Out value: " + out);
  }

  @Request
  @Test
  public void testSetStructCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());
    Object object = collectionClass.getConstructor().newInstance();

    String in = "Dwight Schrute";
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aStruct", "structCharacter");

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved StructCharacters have different values.");
  }

  @Request
  @Test
  public void testGetStructCharacter() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Smethie wuz Here!!!!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    String out = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    if (!in.equals(out))
      Assert.fail("Stored and Retrieved StructCharacters have different values.");
  }

  @Request
  @Test
  public void testSetStructEnumeration() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());
    Class<?> enumClass = loader.loadClass(suitEnum.definesType());

    BusinessEnumeration in = (BusinessEnumeration) enumClass.getMethod("get", String.class).invoke(null, heartsId);
    Object object = collectionClass.getConstructor().newInstance();

    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    StructDAO structDAO = ( (AttributeStructIF) businessDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    Assert.assertEquals(1, enums.length);
    Assert.assertEquals(heartsId, enums[0].getOid());
  }

  @Request
  @Test
  public void testGetStructEnumeration() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String input = "This is myself.";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", input);
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    List<?> out = (List<?>) structClass.getMethod("getStructEnumeration").invoke(struct);

    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getOid").invoke(head);
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    Assert.assertEquals(input, structChar);
    Assert.assertEquals(1, out.size());
    Assert.assertEquals(heartsId, outId);
  }

  @Request
  @Test
  public void testSetStructBoolean() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());
    Object object = collectionClass.getConstructor().newInstance();

    boolean in = true;
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    boolean out = Boolean.parseBoolean(businessDAO.getStructValue("aStruct", "structBoolean"));

    if (in != out)
      Assert.fail("Stored and Retrieved StructBooleans have different values.");
  }

  @Request
  @Test
  public void testGetStructBoolean() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    boolean out = (Boolean) structClass.getMethod("getStructBoolean").invoke(struct);

    if (in != out)
      Assert.fail("Stored and Retrieved StructBooleans have different values.");
  }

  @Request
  @Test
  public void testSetReference() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType());
    Business in = (Business) referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(in);

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    collectionClass.getMethod("setAReference", referenceClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    BusinessDAOIF out = ( (AttributeReferenceIF) businessDAO.getAttributeIF("aReference") ).dereference();

    if (!in.getOid().equalsIgnoreCase(out.getOid()))
      Assert.fail("Stored and Retrieved References are different.");
  }

  @Request
  @Test
  public void testSetReferenceById() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType());
    Business in = (Business) referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(in);

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    collectionClass.getMethod("setAReferenceId", String.class).invoke(object, in.getOid());
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    BusinessDAOIF out = ( (AttributeReferenceIF) businessDAO.getAttributeIF("aReference") ).dereference();

    if (!in.getOid().equalsIgnoreCase(out.getOid()))
      Assert.fail("Stored and Retrieved References are different.");
  }

  @Request
  @Test
  public void testGetReference() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getOid());
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, oid);
    Business out = (Business) collectionClass.getMethod("getAReference").invoke(object);

    if (!in.getOid().equalsIgnoreCase(out.getOid()))
      Assert.fail("Stored and Retrieved References are different.");
  }

  @Request
  @Test
  public void testEnumDTO_getEnumNames() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    String in2 = clubsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    businessDAO.addItem("anEnum", in2);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO businessDTO = (BusinessDTO) get.invoke(null, clientRequestIF, oid);

    String method = "getAnEnum" + TypeGeneratorInfo.ATTRIBUTE_ENUMERATION_ENUM_NAMES_SUFFIX;
    List<String> enumNames = (List<String>) collectionClass.getMethod(method).invoke(businessDTO);

    Assert.assertEquals(2, enumNames.size());

    String enumName1 = enumNames.get(0);
    String enumName2 = enumNames.get(1);

    Assert.assertTrue(heartName.equals(enumName1) || heartName.equals(enumName2));
    Assert.assertTrue(clubName.equals(enumName1) || clubName.equals(enumName2));
  }

  @Request
  @Test
  public void testEnumDTO_getName() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> enumClass = loader.loadClass(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);

    Method getName = enumClass.getMethod("getName");
    String name = (String) getName.invoke(hearts);

    Assert.assertEquals(hearts.name(), name);
  }

  @Request
  @Test
  public void testEnumDTO_item() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> enumClass = loader.loadClass(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);

    Method item = enumClass.getMethod("item", ClientRequestIF.class);
    BusinessDTO heartsDTO = (BusinessDTO) item.invoke(hearts, clientRequestIF);

    Assert.assertEquals(heartsDTO.getValue("enumName"), hearts.name());
  }

  @Request
  @Test
  public void testEnumDTO_items() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> enumClass = loader.loadClass(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);
    Enum<?> clubs = (Enum<?>) valueOf.invoke(null, clubName);

    Object[] array = (Object[]) Array.newInstance(enumClass, 2);
    array[0] = hearts;
    array[1] = clubs;

    Method items = enumClass.getMethod("items", ClientRequestIF.class, array.getClass());
    List<? extends BusinessDTO> values = (List<? extends BusinessDTO>) items.invoke(hearts, clientRequestIF, array);

    Assert.assertEquals(values.size(), 2);

    boolean heartsFound = false;
    boolean clubsFound = false;
    for (BusinessDTO value : values)
    {
      String name = value.getValue("enumName");
      if (name.equals(heartName))
        heartsFound = true;
      else if (name.equals(clubName))
        clubsFound = true;
    }

    Assert.assertTrue(heartsFound && clubsFound);
  }

  @Request
  @Test
  public void testEnumDTO_allItems() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> enumClass = loader.loadClass(suitEnumDTO);

    Method items = enumClass.getMethod("allItems", ClientRequestIF.class);
    List<? extends BusinessDTO> values = (List<? extends BusinessDTO>) items.invoke(null, clientRequestIF);

    Assert.assertEquals(values.size(), 4);

    boolean heartsFound = false;
    boolean clubsFound = false;
    boolean spadesFound = false;
    boolean diamondsFound = false;
    for (BusinessDTO value : values)
    {
      String name = value.getValue("enumName");
      if (name.equals(heartName))
        heartsFound = true;
      else if (name.equals(clubName))
        clubsFound = true;
      else if (name.equals(diamondsName))
        diamondsFound = true;
      else if (name.equals(spadesName))
        spadesFound = true;
    }

    Assert.assertTrue(heartsFound && clubsFound && spadesFound && diamondsFound);
  }

  @Request
  @Test
  public void testAddEnum() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> enumClass = loader.loadClass(suitEnum.definesType());

    Object object = collectionClass.getConstructor().newInstance();

    Object in = enumClass.getDeclaredField(diamondsName).get(null);

    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Set<String> ids = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();
    if (ids.size() != 1)
      Assert.fail("Expected 1 enum value, found " + ids.size());

    String out = BusinessDAO.get(ids.iterator().next()).getValue(EnumerationMasterInfo.NAME);

    if (!out.equals(diamondsName))
      Assert.fail("Stored and Retrieved enums have different values.");
  }

  @Request
  @Test
  public void testGetEnum() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, oid);
    List<?> out = (List<?>) collectionClass.getMethod("getAnEnum").invoke(object);
    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getOid").invoke(head);

    if (!in.equalsIgnoreCase(outId))
      Assert.fail("Stored and Retrieved enums have different values.");
  }

  @Request
  @Test
  public void testRemoveEnum() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> enumClass = loader.loadClass(suitEnum.definesType());

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    Object object = collectionClass.getMethod("get", String.class).invoke(null, oid);
    collectionClass.getMethod("removeAnEnum", enumClass).invoke(object, hearts);
    collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(oid).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    if (out.size() != 0)
      Assert.fail("Failed to remove an enumerated value.");
  }

  @Request
  @Test
  public void testClearEnum() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, oid);
    collectionClass.getMethod("clearAnEnum").invoke(object);
    collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(oid).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    if (out.size() != 0)
      Assert.fail("Failed to clear an enumerated value.");
  }

  @Request
  @Test
  public void testAddChild() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> referenceClass = loader.loadClass(reference.definesType());

    Business mom = (Business) collectionClass.newInstance();
    mom.apply();
    Business kid = (Business) referenceClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship) collectionClass.getMethod("addRelChild", referenceClass).invoke(mom, kid);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getOid());

    if (!oracle.getParentOid().equals(mom.getOid()))
      Assert.fail("Parent reference mismatch in addChild");

    if (!oracle.getChildOid().equals(kid.getOid()))
      Assert.fail("Child reference mismatch in addChild");
  }

  @Request
  @Test
  public void testAddChildDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> referenceClass = loader.loadClass(referenceDTO);
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);

    // Create a new instance of a collection
    BusinessDTO mom = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    Method createDTO = collectionClass.getMethod("apply");
    createDTO.invoke(mom);

    // Create a new instance of a reference
    BusinessDTO kid = (BusinessDTO) referenceClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createRef = referenceClass.getMethod("apply");
    createRef.invoke(kid);

    // lock the parent
    collectionClass.getMethod("lock").invoke(mom);

    // Add a child (reference) to the parent (collection) object
    Method addChild = collectionClass.getMethod("addRelChild", referenceClass);
    RelationshipDTO instance = (RelationshipDTO) addChild.invoke(mom, kid);

    // Create the new child relationship
    Method createChild = relationshipClass.getMethod("apply");
    createChild.invoke(instance);

    RelationshipDAOIF oracle = RelationshipDAO.get(instance.getOid());

    Assert.assertEquals(oracle.getParentOid(), mom.getOid());
    Assert.assertEquals(oracle.getChildOid(), kid.getOid());
  }

  @Request
  @Test
  public void testAddParent() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> referenceClass = loader.loadClass(reference.definesType());

    Business mom = (Business) collectionClass.newInstance();
    mom.apply();
    Business kid = (Business) referenceClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship) referenceClass.getMethod("addRelParent", collectionClass).invoke(kid, mom);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getOid());

    if (!oracle.getParentOid().equals(mom.getOid()))
      Assert.fail("Parent reference mismatch in addParent");

    if (!oracle.getChildOid().equals(kid.getOid()))
      Assert.fail("Child reference mismatch in addParent");
  }

  @Request
  @Test
  public void testGetChildren() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType());
    rel.apply();

    Business businessMom = Business.get(mom.getOid());

    List<Business> list = ( (OIterator<Business>) collectionClass.getMethod("getAllRelChild").invoke(businessMom) ).getAll();
    if (list.size() != 1)
      Assert.fail("Expected getAllChildren to return 1, found " + list.size());
    Business oracle = list.iterator().next();

    if (!oracle.getOid().equals(kid.getOid()))
      Assert.fail("Child reference mismatch in getAllChildren");
  }

  @Request
  @Test
  public void testGetParents() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType());
    rel.apply();

    Business businessKid = Business.get(kid.getOid());

    List<Business> list = ( (OIterator<Business>) referenceClass.getMethod("getAllRelParent").invoke(businessKid) ).getAll();
    if (list.size() != 1)
      Assert.fail("Expected getAllPrents to return 1, found " + list.size());
    Business oracle = list.iterator().next();

    if (!oracle.getOid().equals(mom.getOid()))
      Assert.fail("Parent reference mismatch in getAllParents");
  }

  @Request
  @Test
  public void testRemoveAllChildren() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> referenceClass = loader.loadClass(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    BusinessDAO kid2 = BusinessDAO.newInstance(reference.definesType());
    kid2.apply();
    RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(mom.getOid(), kid2.getOid(), mdRelationship.definesType()).apply();

    Business businessMom = Business.get(mom.getOid());
    Business businessKid = Business.get(kid.getOid());
    collectionClass.getMethod("removeRelChild", referenceClass).invoke(businessMom, businessKid);

    List<RelationshipDAOIF> list = mom.getChildren(mdRelationship.definesType());
    if (list.size() != 1)
      Assert.fail("RemoveAllChilren expected 1 child, found " + list.size());

    RelationshipDAOIF rel = list.get(0);
    if (!rel.getParentOid().equals(mom.getOid()))
      Assert.fail("Unexpected parent after removeAllChildren.");

    if (!rel.getChildOid().equals(kid2.getOid()))
      Assert.fail("Unexpected child after removeAllChildren.");

  }

  @Request
  @Test
  public void testRemoveAllParents() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> referenceClass = loader.loadClass(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO dad = BusinessDAO.newInstance(collection.definesType());
    dad.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(dad.getOid(), kid.getOid(), mdRelationship.definesType()).apply();

    Business businessMom = Business.get(mom.getOid());
    Business businessKid = Business.get(kid.getOid());
    referenceClass.getMethod("removeRelParent", collectionClass).invoke(businessKid, businessMom);

    List<RelationshipDAOIF> list = kid.getParents(mdRelationship.definesType());
    if (list.size() != 1)
      Assert.fail("RemoveAllParents expected 1 parent, found " + list.size());

    RelationshipDAOIF rel = list.get(0);
    if (!rel.getParentOid().equals(dad.getOid()))
    {
      Assert.fail("Unexpected parent after removeAllParents.");
    }

    if (!rel.getChildOid().equals(kid.getOid()))
    {
      Assert.fail("Unexpected child after removeAllParents.");
    }

  }

  /**
   * Test for apply on a StructDAO that lives outside of an AttributeStruct
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testApplyStruct() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Haaaar Harr, BSG";

    Class<?> structClass = loader.loadClass(struct.definesType());
    Object struct = structClass.getConstructor().newInstance();

    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    structClass.getMethod("apply").invoke(struct);

    String oid = (String) structClass.getMethod("getOid").invoke(struct);
    StructDAOIF structDAO = StructDAO.get(oid);
    String out = structDAO.getValue("structCharacter");

    if (!in.equals(out))
    {
      Assert.fail("Stored and Retrieved StructCharacters have different values.");
    }
  }

  /**
   * Test to ensure apply on a StructDAO that lives inside of an AttributeStruct
   * balks (does nothing).
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testNoApplyStruct() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionType);
    Class<?> structClass = loader.loadClass(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, !in);
    structClass.getMethod("apply").invoke(struct);

    BusinessDAOIF businessDAOIF = BusinessDAO.get(oid);

    boolean out = Boolean.parseBoolean(businessDAOIF.getStructValue("aStruct", "structBoolean"));

    if (in != out)
      Assert.fail("Stored and Retrieved StructBooleans have different values.");
  }

  @Request
  @Test
  public void testAddParentDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> referenceClass = loader.loadClass(referenceDTO);
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);

    // Create a new instance of a collection
    BusinessDTO mom = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");
    createDTO.invoke(mom);

    // Create a new instance of a reference
    BusinessDTO kid = (BusinessDTO) referenceClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createRef = referenceClass.getMethod("apply");
    createRef.invoke(kid);

    // lock the child
    referenceClass.getMethod("lock").invoke(kid);

    // Add a parent (collection) to the child (reference) object
    Method addParent = referenceClass.getMethod("addRelParent", collectionClass);
    RelationshipDTO instance = (RelationshipDTO) addParent.invoke(kid, mom);

    // Create the new parent relationship
    Method createParent = relationshipClass.getMethod("apply");
    createParent.invoke(instance);

    RelationshipDAOIF oracle = RelationshipDAO.get(instance.getOid());

    Assert.assertEquals(oracle.getParentOid(), mom.getOid());
    Assert.assertEquals(oracle.getChildOid(), kid.getOid());
  }

  @Request
  @Test
  public void testDeleteDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("delete").invoke(object);

    try
    {
      BusinessDAO.get(oid);

      Assert.fail("Delete businessDTO did not delete the entity");
    }
    catch (DataNotFoundException e)
    {
      // Expect to land here
    }
  }

  @Request
  @Test
  public void testGetBlobDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setBlob("aBlob", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    byte[] out = (byte[]) collectionClass.getMethod("getABlob").invoke(object);

    if (in.length != out.length)
      Assert.fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        Assert.fail("Stored and Retrieved blobs have different values.");
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
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = false;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", Boolean.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetBooleanNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    Assert.assertNull(null, out);
  }

  @Request
  @Test
  public void testGetCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "RunwaySDK";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aCharacter", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    String out = (String) collectionClass.getMethod("getACharacter").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetChildDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType());
    rel.apply();

    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);
    Method get = relationshipClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, rel.getOid());

    Method getChild = relationshipClass.getMethod("getChild");
    BusinessDTO child = (BusinessDTO) getChild.invoke(object);

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> referenceClass = loader.loadClass(referenceDTO);
    Assert.assertTrue(referenceClass.isInstance(child));
    Assert.assertEquals(kid.getOid(), child.getOid());
  }

  @Request
  @Test
  public void testGetChildrenDTOCached() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    mdRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();

    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      updateRelationship.apply();
    }
    try
    {
      Class<?> collectionClass = loader.loadClass(collectionDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
      List<BusinessDTO> list = (List<BusinessDTO>) collectionClass.getMethod("getAllRelChild").invoke(mom);

      Assert.assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      Assert.assertTrue(oracle.getType().equals(reference.definesType()));
      Assert.assertEquals(oracle.getOid(), kidId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
        updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }

  @Request
  @Test
  public void testGetChildrenDTONotCached() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    mdRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();

    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_NOTHING.getOid()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      updateRelationship.apply();
    }
    try
    {
      Class<?> collectionClass = loader.loadClass(collectionDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
      List<BusinessDTO> list = (List<BusinessDTO>) collectionClass.getMethod("getAllRelChild").invoke(mom);

      Assert.assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      Assert.assertTrue(oracle.getType().equals(reference.definesType()));
      Assert.assertEquals(oracle.getOid(), kidId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
        updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }

  @Request
  @Test
  public void testGetChildRelationshipsDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);

    String momId = BusinessDAO.newInstance(collection.definesType()).apply();
    String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

    Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
    List<RelationshipDTO> list = (List<RelationshipDTO>) collectionClass.getMethod("getAllRelChildRelationships").invoke(mom);

    Assert.assertEquals(3, list.size());
    RelationshipDTO oracle = list.get(0);

    Assert.assertTrue(relationshipClass.isInstance(oracle));
    Assert.assertEquals(oracle.getChildOid(), kidId);
    Assert.assertEquals(oracle.getParentOid(), momId);
  }

  @Request
  @Test
  public void testAttributeGetterVisibility() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("getACharacter").getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("getACharacter").invoke(object);

    collectionCharacter.addItem(MdAttributeConcreteInfo.GETTER_VISIBILITY, VisibilityModifier.PROTECTED.getOid());
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
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
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
      collectionCharacter.addItem(MdAttributeConcreteInfo.GETTER_VISIBILITY, VisibilityModifier.PUBLIC.getOid());
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
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("getACharacter").invoke(object);
    }
  }

  @Request
  @Test
  public void testAttributeSetterVisibility() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> colletionBaseClass = colletionClass.getSuperclass();

    int modifiers = colletionBaseClass.getDeclaredMethod("setACharacter", String.class).getModifiers();
    if (!Modifier.isPublic(modifiers))
    {
      Assert.fail("Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }

    // Make sure the accessor method is there
    collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");

    collectionCharacter.addItem(MdAttributeConcreteInfo.SETTER_VISIBILITY, VisibilityModifier.PROTECTED.getOid());
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
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
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
      collectionCharacter.addItem(MdAttributeConcreteInfo.SETTER_VISIBILITY, VisibilityModifier.PUBLIC.getOid());
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
      collectionDTOclass = loader.loadClass(collectionDTO);
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");
    }
  }

  @Request
  @Test
  public void testParentMethodVisibility() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType());
    Class<?> referenceBaseClass = referenceClass.getSuperclass();
    Class<?> referenceDTOclass = loader.loadClass(referenceDTO);
    Class<?> relationshipDTOclass = loader.loadClass(relationshipDTO);
    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);

    // Check public visibility on the business class
    int modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("removeRelParent", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [removeRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [getAllRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [getRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    // Create some objects
    Object collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
    Object referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

    // Make sure the accessor methods are there on the DTO
    Object aRelationshipDTO = referenceDTOclass.getMethod("addRelParent", collectionDTOclass).invoke(referenceDTOObject, collectionDTOObject);
    relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
    referenceDTOclass.getMethod("getAllRelParent").invoke(referenceDTOObject);
    List<?> relationshipList = (List<?>) referenceDTOclass.getMethod("getAllRelParentRelationships").invoke(referenceDTOObject);
    referenceDTOclass.getMethod("removeRelParent", relationshipDTOclass).invoke(referenceDTOObject, relationshipList.get(0));
    referenceDTOclass.getMethod("removeAllRelParent").invoke(referenceDTOObject);

    // Delete them
    collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
    referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);

    // Change to PROTECTED Visibility
    MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
    updateRelationship.clearItems(MdRelationshipInfo.PARENT_VISIBILITY);
    updateRelationship.addItem(MdRelationshipInfo.PARENT_VISIBILITY, VisibilityModifier.PROTECTED.getOid());
    updateRelationship.apply();

    loader = GeneratedLoader.isolatedClassLoader();

    referenceClass = loader.loadClass(reference.definesType());
    referenceBaseClass = referenceClass.getSuperclass();
    referenceDTOclass = loader.loadClass(referenceDTO);
    relationshipDTOclass = loader.loadClass(relationshipDTO);
    colletionClass = loader.loadClass(collection.definesType());
    collectionDTOclass = loader.loadClass(collectionDTO);

    try
    {
      modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();

      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("removeRelParent", colletionClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [removeRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [getAllRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [getRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

    }
    finally
    {
      // Change to PUBLIC Visibility
      updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.PARENT_VISIBILITY);
      updateRelationship.addItem(MdRelationshipInfo.PARENT_VISIBILITY, VisibilityModifier.PUBLIC.getOid());
      updateRelationship.apply();

      loader = GeneratedLoader.isolatedClassLoader();

      referenceClass = loader.loadClass(reference.definesType());
      referenceBaseClass = referenceClass.getSuperclass();
      referenceDTOclass = loader.loadClass(referenceDTO);
      relationshipDTOclass = loader.loadClass(relationshipDTO);
      colletionClass = loader.loadClass(collection.definesType());
      collectionDTOclass = loader.loadClass(collectionDTO);

      // Check public visibility on the business class
      modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship  [addRelParent] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship  [addRelParent] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship  [getAllRelParentRel] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship  [getRelParentRel] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");
    }
  }

  @Request
  @Test
  public void testChildMethodVisibility() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType());
    Class<?> referenceDTOclass = loader.loadClass(referenceDTO);
    Class<?> relationshipDTOclass = loader.loadClass(relationshipDTO);
    Class<?> colletionClass = loader.loadClass(collection.definesType());
    Class<?> collectionBaseClass = colletionClass.getSuperclass();
    Class<?> collectionDTOclass = loader.loadClass(collectionDTO);

    // Check public visibility on the business class
    int modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      Assert.fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    // Create some objects
    Object collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
    Object referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

    // Make sure the accessor methods are there on the DTO
    Object aRelationshipDTO = collectionDTOclass.getMethod("addRelChild", referenceDTOclass).invoke(collectionDTOObject, referenceDTOObject);
    relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
    collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
    List<?> relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
    collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
    collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);

    // Delete them
    collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
    referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);

    // Change to PROTECTED Visibility
    MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
    updateRelationship.clearItems(MdRelationshipInfo.CHILD_VISIBILITY);
    updateRelationship.addItem(MdRelationshipInfo.CHILD_VISIBILITY, VisibilityModifier.PROTECTED.getOid());
    updateRelationship.apply();
    loader = GeneratedLoader.isolatedClassLoader();

    referenceClass = loader.loadClass(reference.definesType());
    referenceDTOclass = loader.loadClass(referenceDTO);
    relationshipDTOclass = loader.loadClass(relationshipDTO);
    colletionClass = loader.loadClass(collection.definesType());
    collectionBaseClass = colletionClass.getSuperclass();
    collectionDTOclass = loader.loadClass(collectionDTO);

    try
    {
      modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        Assert.fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      try
      {
        // Create some objects
        collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
        referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

        String childProtectedFail = "Able to access a child relationship method on a DTO for a relationship where the parent accessors are [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].";

        try
        {
          aRelationshipDTO = collectionDTOclass.getMethod("addRelChild", referenceDTOclass).invoke(collectionDTOObject, referenceDTOObject);
          relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
      }
      finally
      {
        // Delete them
        collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
        referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);
      }

      try
      {
        // Create some objects
        collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
        referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);
        ;

        String childProtectedFail = "Able to access a child relationship method on a DTO for a relationship where the parent accessors are [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].";

        try
        {
          aRelationshipDTO = collectionDTOclass.getMethod("addRelChild", referenceDTOclass).invoke(collectionDTOObject, referenceDTOObject);
          relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);
          Assert.fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
      }
      finally
      {
        // Delete them
        collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
        referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);
      }

    }
    finally
    {
      // Change to PUBLIC Visibility
      updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CHILD_VISIBILITY);
      updateRelationship.addItem(MdRelationshipInfo.CHILD_VISIBILITY, VisibilityModifier.PUBLIC.getOid());
      updateRelationship.apply();

      loader = GeneratedLoader.createClassLoader();

      referenceClass = loader.loadClass(reference.definesType());
      referenceDTOclass = loader.loadClass(referenceDTO);
      relationshipDTOclass = loader.loadClass(relationshipDTO);
      colletionClass = loader.loadClass(collection.definesType());
      collectionBaseClass = colletionClass.getSuperclass();
      collectionDTOclass = loader.loadClass(collectionDTO);

      // Check public visibility on the business class
      modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        Assert.fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      // Create some objects
      collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
      referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

      // Make sure the accessor methods are there on the DTO
      aRelationshipDTO = collectionDTOclass.getMethod("addRelChild", referenceDTOclass).invoke(collectionDTOObject, referenceDTOObject);
      relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
      collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
      relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
      collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
      collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);

      // Delete them
      collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
      referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);

    }
  }

  @Request
  @Test
  public void testGetDateDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    businessDAO.delete();

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testGetDateNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetDateTimeDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testGetDateTimeNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetDecimalDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    double in = 987654.321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", Double.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    double out = ( (BigDecimal) collectionClass.getMethod("getADecimal").invoke(object) ).doubleValue();

    Assert.assertEquals(in, out, 0);
  }

  @Request
  @Test
  public void testGetDecimalNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Double out = (Double) collectionClass.getMethod("getADecimal").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetDoubleDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    double in = 98765.4321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", Double.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    Assert.assertEquals(in, out, 0);
  }

  @Request
  @Test
  public void testGetDoubleNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetEnumDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    List<?> enums = (List<?>) collectionClass.getMethod("getAnEnum").invoke(object);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();

    Assert.assertEquals(heartName, out);
  }

  @Request
  @Test
  public void testGetFloatDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    float in = 987.654321F;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", Float.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    Assert.assertEquals(in, out, 0);
  }

  @Request
  @Test
  public void testGetFloatNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetIntegerDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    int in = 9876;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", Integer.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    int out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetIntegerNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Integer out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetLongDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    long in = 987654321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", Long.toString(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetLongNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Float out = (Float) collectionClass.getMethod("getALong").invoke(object);

    Assert.assertNull(out);
  }

  @Request
  @Test
  public void testGetParentDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType());
    rel.apply();

    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);
    Method get = relationshipClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, rel.getOid());

    Method getParent = relationshipClass.getMethod("getParent");
    BusinessDTO parent = (BusinessDTO) getParent.invoke(object);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Assert.assertTrue(collectionClass.isInstance(parent));
    Assert.assertEquals(mom.getOid(), parent.getOid());
  }

  @Request
  @Test
  public void testGetParentsDTONotCached() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getOid());
      updateRelationship.apply();
    }

    try
    {
      String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

      Class<?> referenceClass = loader.loadClass(referenceDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
      List<BusinessDTO> list = (List<BusinessDTO>) referenceClass.getMethod("getAllRelParent").invoke(kid);

      Assert.assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      Assert.assertTrue(oracle.getType().equals(collection.definesType()));
      Assert.assertEquals(oracle.getOid(), momId);
    }
    finally
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      updateRelationship.apply();
    }
  }

  @Request
  @Test
  public void testGetParentsDTOCached() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_EVERYTHING.getOid()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getOid());
      updateRelationship.apply();
    }
    try
    {
      String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

      Class<?> referenceClass = loader.loadClass(referenceDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
      List<BusinessDTO> list = (List<BusinessDTO>) referenceClass.getMethod("getAllRelParent").invoke(kid);

      Assert.assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      Assert.assertTrue(oracle.getType().equals(collection.definesType()));
      Assert.assertEquals(oracle.getOid(), momId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getOid()).getBusinessDAO();
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }

  @Request
  @Test
  public void testGetParentRelationshipsDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> referenceClass = loader.loadClass(referenceDTO);
    Class<?> relationshipClass = loader.loadClass(relationshipDTO);

    String momId = BusinessDAO.newInstance(collection.definesType()).apply();
    String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

    Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
    List<RelationshipDTO> list = (List<RelationshipDTO>) referenceClass.getMethod("getAllRelParentRelationships").invoke(kid);

    Assert.assertEquals(4, list.size());
    RelationshipDTO oracle = list.get(0);

    Assert.assertTrue(relationshipClass.isInstance(oracle));
    Assert.assertEquals(oracle.getParentOid(), momId);
    Assert.assertEquals(oracle.getChildOid(), kidId);
  }

  @Request
  @Test
  public void testGetReferenceDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getOid());
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    BusinessDTO out = (BusinessDTO) collectionClass.getMethod("getAReference").invoke(object);

    Assert.assertEquals(in.getOid(), out.getOid());
  }

  @Request
  @Test
  public void testGetStructBooleanDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String oid = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    boolean out = (Boolean) structClass.getMethod("getStructBoolean").invoke(struct);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetStructCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Smethie wuz Here!!!!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", in);
    String oid = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    String out = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetStructEnumerationDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String input = "This is myself.";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", input);
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    String oid = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    List<?> enums = (List<?>) structClass.getMethod("getStructEnumeration").invoke(struct);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    Assert.assertEquals(input, structChar);
    Assert.assertEquals(1, enums.size());
    Assert.assertEquals(heartName, out);
  }

  @Request
  @Test
  public void testGetSymmetricDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "You'll find that they're quite stationary";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aSym", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    String out = (String) collectionClass.getMethod("getASym").invoke(object);

    Assert.assertEquals("", out); // the return value should be empty for a DTO
                                  // (for
    // security)
  }

  @Request
  @Test
  public void testGetTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aText", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetClobDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "CLOB: Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aClob", in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    String out = (String) collectionClass.getMethod("getAClob").invoke(object);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetTimeDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testGetTimeNullDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", "");
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    Assert.assertNull(out);
  }

  // TODO this test is obsolete since it checks the value of a hash via a DTO.
  // However,
  // DTO hash values are empty for security reasons.
  // @Request @Test public void testHashEqualsDTO() throws Exception
  // {
  // String in = "For breakfast, I had some Pringles, and some fudge-striped
  // cook-ays";
  // BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
  // businessDAO.setValue("aHash", in);
  // String oid = businessDAO.apply();
  //
  // Class<?> collectionClass = loader.loadClass(collectionDTO);
  // Method getInstance = collectionClass.getMethod("getInstance", String.class,
  // ClientRequestIF.class,
  // String.class);
  // Object object = getInstance.invoke(null, sessionId, ClientRequest, oid);
  //
  // String hash = businessDAO.getValue("aHash");
  // String out = (String) collectionClass.getMethod("getAHash").invoke(object);
  //
  // Assert.assertEquals(hash, out);
  // }

  @Request
  @Test
  public void testIsReadableDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    boolean out = (Boolean) collectionClass.getMethod("isATimeReadable").invoke(object);

    Assert.assertTrue(out);
  }

  @Request
  @Test
  public void testIsWritableDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);
    boolean out = (Boolean) collectionClass.getMethod("isATimeWritable").invoke(object);

    Assert.assertTrue(out);
  }

  @Request
  @Test
  public void testRemoveEnumDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = heartsId;
    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> enumClass = loader.loadClass(suitDTO);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String oid = businessDAO.apply();

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    // Lock the object
    collectionClass.getMethod("lock").invoke(object);

    // Remove the enum item
    collectionClass.getMethod("removeAnEnum", enumClass).invoke(object, hearts);
    object = collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(oid).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    Assert.assertEquals(0, out.size());
  }

  @Request
  @Test
  public void testRemoveAllChildrenDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    BusinessDAO kid2 = BusinessDAO.newInstance(reference.definesType());
    kid2.apply();
    RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(mom.getOid(), kid2.getOid(), mdRelationship.definesType()).apply();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object businessMom = get.invoke(null, clientRequestIF, mom.getOid());

    collectionClass.getMethod("removeAllRelChild").invoke(businessMom);

    List<RelationshipDAOIF> list = BusinessDAO.get(mom.getOid()).getChildren(mdRelationship.definesType());

    Assert.assertEquals(0, list.size());
  }

  @Request
  @Test
  public void testRemoveAllParentsDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> referenceClass = loader.loadClass(reference.definesType() + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO dad = BusinessDAO.newInstance(collection.definesType());
    dad.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO.newInstance(mom.getOid(), kid.getOid(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(dad.getOid(), kid.getOid(), mdRelationship.definesType()).apply();

    List<RelationshipDAOIF> list = BusinessDAO.get(kid.getOid()).getParents(mdRelationship.definesType());
    Assert.assertEquals(2, list.size());

    Method get = referenceClass.getMethod("get", ClientRequestIF.class, String.class);
    Object businessKid = get.invoke(null, clientRequestIF, kid.getOid());

    referenceClass.getMethod("removeAllRelParent").invoke(businessKid);

    list = BusinessDAO.get(kid.getOid()).getParents(mdRelationship.definesType());
    Assert.assertEquals(0, list.size());
  }

  @Request
  @Test
  public void testSetBlobDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    byte[] out = businessDAO.getBlob("aBlob");

    Assert.assertEquals(in.length, out.length);

    for (int i = 0; i < in.length; i++)
      Assert.assertEquals(in[i], out[i]);
  }

  @Request
  @Test
  public void testSetBooleanDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    boolean in = true;
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    boolean out = Boolean.parseBoolean(businessDAO.getValue("aBoolean"));

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    String in = "Mr. Sparkle";
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aCharacter");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetDateDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aDate"));

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testSetDateTimeDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aDateTime"));

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testSetDecimalDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    BigDecimal in = new BigDecimal(123456.789);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);

    BigDecimal out = new BigDecimal(businessDAO.getValue("aDecimal"));

    if (in.subtract(out).abs().doubleValue() > .0000001)
      Assert.fail("Stored and Retrieved Decimals have different values.");
  }

  @Request
  @Test
  public void testSetDoubleDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    double in = 123456.789;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    double out = Double.parseDouble(businessDAO.getValue("aDouble"));

    Assert.assertEquals(in, out, 0.001);
  }

  @Request
  @Test
  public void testSetFloatDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    float in = 123456.7895F;

    System.out.println(in);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    float out = Float.parseFloat(businessDAO.getValue("aFloat"));

    Assert.assertEquals(in, out, 0.001);
  }

  @Request
  @Test
  public void testSetHashDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "When you win, say nothing. When you lose, say less.";

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAHash", String.class).invoke(object, in);
    createDTO.invoke(object);

    MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
    digest.update(in.getBytes());
    String hash = Base64.encodeToString(digest.digest(), false);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aHash");

    Assert.assertEquals(hash, out);
  }

  @Request
  @Test
  public void testSetIntegerDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    int in = 1234;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    int out = Integer.parseInt(businessDAO.getValue("anInteger"));

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetLongDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    long in = 123456789;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    long out = Long.parseLong(businessDAO.getValue("aLong"));

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetLocalCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionDTO);

    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    LocalStructDTO struct = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(in);

    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetDefaultLocalCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionDTO);

    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    LocalStructDTO struct = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetDefaultLocalCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    StructDTO struct = (StructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetLocalCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    LocalStructDTO localStructDTO = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = localStructDTO.getValue();

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetDefaultLocalTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    localStruct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetLocalTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(123456789L);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    localStruct.setValue(in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetLocalTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    String out = localStruct.getValue();

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testGetDefaultLocalTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    String out = localStruct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetReferenceDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> referenceClass = loader.loadClass(referenceDTO);
    BusinessDTO ref = (BusinessDTO) referenceClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createRef = referenceClass.getMethod("apply");

    createRef.invoke(ref);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAReference", referenceClass).invoke(object, ref);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    BusinessDAOIF out = ( (AttributeReferenceIF) businessDAO.getAttributeIF("aReference") ).dereference();

    Assert.assertEquals(ref.getOid(), out.getOid());
  }

  @Request
  @Test
  public void testSetStructBusiness()
  {
    String in = MdAttributeBooleanInfo.TRUE;
    Business object = BusinessFacade.newBusiness(collection.definesType());
    Struct struct = object.getGenericStruct("aStruct");

    struct.setValue("structBoolean", in);
    object.apply();

    BusinessDAOIF businessDAO = BusinessDAO.get(object.getOid());
    String out = businessDAO.getStructValue("aStruct", "structBoolean");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetStructDTO()
  {
    String in = MdAttributeBooleanInfo.TRUE;

    BusinessDTO object = clientRequestIF.newBusiness(collection.definesType());

    ComponentDTOFacade.getAttributeStructDTO(object, "aStruct").setValue("structBoolean", in);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(object, "aStruct");
    Assert.assertEquals(in, struct.getValue("structBoolean"));

    clientRequestIF.createBusiness(object);

    BusinessDAOIF businessDAO = BusinessDAO.get(object.getOid());
    String out = businessDAO.getStructValue("aStruct", "structBoolean");

    Assert.assertEquals(in, out);
  }

  /**
   * Test creating a StructDAO by itself through the DTO layer
   */
  @Request
  @Test
  public void testSetStandaloneDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean bIn = true;
    String cIn = "Im asl;dkgh39o";

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> enumClass = loader.loadClass(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    // Get the standalone object by itself
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = loader.loadClass(standaloneDTO);
    Object struct = structClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, bIn);
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, cIn);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, hearts);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, clubs);

    structClass.getMethod("apply").invoke(struct);

    String oid = (String) structClass.getMethod("getOid").invoke(struct);

    StructDAOIF structDAO = StructDAO.get(oid);
    boolean bOut = Boolean.parseBoolean(structDAO.getValue("structBoolean"));
    String cOut = structDAO.getValue("structCharacter");
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttributeIF("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    Assert.assertEquals(bIn, bOut);
    Assert.assertEquals(cIn, cOut);
    Assert.assertEquals(2, enums.length);
    Assert.assertTrue(enumNames.contains(heartName));
    Assert.assertTrue(enumNames.contains(clubName));
  }

  /**
   * Test the generic getter and setters for a standalone StructDAO in the DTO
   * layer
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testGenericSetStandaloneDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean bIn = true;
    String cIn = "Im asl;dkgh39o";

    // Get the standalone object by itself
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = loader.loadClass(standaloneDTO);
    StructDTO struct = (StructDTO) structClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    // Modify the boolean attribute of the struct
    struct.setValue("structBoolean", Boolean.toString(bIn));
    struct.setValue("structCharacter", cIn);
    struct.addEnumItem("structEnumeration", heartName);

    structClass.getMethod("apply").invoke(struct);

    String oid = (String) structClass.getMethod("getOid").invoke(struct);

    StructDAOIF structDAO = StructDAO.get(oid);
    boolean bOut = Boolean.parseBoolean(structDAO.getValue("structBoolean"));
    String cOut = structDAO.getValue("structCharacter");
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttributeIF("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    Assert.assertEquals(bIn, bOut);
    Assert.assertEquals(cIn, cOut);
    Assert.assertEquals(1, enums.length);
    Assert.assertTrue(enumNames.contains(heartName));
  }

  @Request
  @Test
  public void testSetStructBooleanDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    boolean in = true;

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    // Get the struct of the collection class
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = loader.loadClass(standaloneDTO);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    boolean out = Boolean.parseBoolean(businessDAO.getStructValue("aStruct", "structBoolean"));

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetStructCharacterDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "Dwight Schrute";

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    // Get the struct of the collection class
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = loader.loadClass(standaloneDTO);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getStructValue("aStruct", "structCharacter");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetStructEnumerationDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(standaloneDTO);
    Class<?> enumClass = loader.loadClass(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    // Get the struct of the collection class
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the boolean attribute of the struct
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, hearts);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, clubs);

    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    StructDAO structDAO = ( (AttributeStructIF) businessDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    Assert.assertEquals(2, enums.length);
    Assert.assertTrue(enumNames.contains(heartName));
    Assert.assertTrue(enumNames.contains(clubName));
  }

  @Request
  @Test
  public void testSetSymmetricDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "My rims never spin - to the contrary";

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setASym", String.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aSym");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetTextDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAText", String.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aText");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetClobDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    String in = "CLOB: But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAClob", String.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    String out = businessDAO.getValue("aClob");

    Assert.assertEquals(in, out);
  }

  @Request
  @Test
  public void testSetTimeDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String oid = (String) collectionClass.getMethod("getOid").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(oid);
    Date out = sdf.parse(businessDAO.getValue("aTime"));

    Assert.assertEquals(sdf.format(in), sdf.format(out));
  }

  @Request
  @Test
  public void testAddRemoveEnumerationDTO() throws Exception
  {
    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String oid = businessDAO.apply();

    BusinessDTO dto = (BusinessDTO) clientRequestIF.get(oid);
    clientRequestIF.lock(dto);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(dto, "aStruct");
    struct.addEnumItem("structEnumeration", clubName);
    struct.removeEnumItem("structEnumeration", heartName);
    clientRequestIF.update(dto);

    // Check that the BusinessDAO was updated
    BusinessDAOIF obj = BusinessDAO.get(oid);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    Assert.assertEquals(1, enums.length);
    Assert.assertEquals(clubsId, enums[0].getOid());
  }

  @Request
  @Test
  public void testUpdateDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    long longIn = 142;
    boolean booleanIn = true;

    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String oid = businessDAO.apply();

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(standaloneDTO);
    Class<?> enumClass = loader.loadClass(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, oid);

    // Lock the BusinessDAO
    collectionClass.getMethod("lock").invoke(object);

    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the BusinessDAO through the DTO layer
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, booleanIn);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, clubs);
    structClass.getMethod("removeStructEnumeration", enumClass).invoke(struct, hearts);
    collectionClass.getMethod("setALong", Long.class).invoke(object, longIn);

    // Update the BusinessDAO
    Method updateDTO = collectionClass.getMethod("apply");
    updateDTO.invoke(object);

    // Check that the BusinessDAO was updated
    BusinessDAOIF obj = BusinessDAO.get(oid);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    String outBoolean = obj.getStructValue("aStruct", "structBoolean");
    String outLong = obj.getValue("aLong");

    Assert.assertEquals(1, enums.length);
    Assert.assertEquals(clubsId, enums[0].getOid());
    Assert.assertEquals(Long.toString(longIn), outLong);
    Assert.assertEquals(Boolean.toString(booleanIn), outBoolean);

  }

  /**
   * Test updating an existing DTO using generic Getters and Setters to update
   * the values.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testGenericUpdateDTO() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    long longIn = 142;
    boolean booleanIn = true;

    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String oid = businessDAO.apply();

    Class<?> collectionClass = loader.loadClass(collectionDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequestIF, oid);

    // Lock the BusinessDAO
    collectionClass.getMethod("lock").invoke(object);

    StructDTO struct = (StructDTO) collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the BusinessDAO through the DTO layer
    struct.setValue("structBoolean", Boolean.toString(booleanIn));
    struct.addEnumItem("structEnumeration", clubName);
    struct.removeEnumItem("structEnumeration", heartName);
    object.setValue("aLong", Long.toString(longIn));

    // Update the BusinessDAO
    Method updateDTO = collectionClass.getMethod("apply");
    updateDTO.invoke(object);

    // Check that the BusinessDAO was updated
    BusinessDAOIF obj = BusinessDAO.get(oid);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    String outBoolean = obj.getStructValue("aStruct", "structBoolean");
    String outLong = obj.getValue("aLong");

    Assert.assertEquals(1, enums.length);
    Assert.assertEquals(clubsId, enums[0].getOid());
    Assert.assertEquals(Long.toString(longIn), outLong);
    Assert.assertEquals(Boolean.toString(booleanIn), outBoolean);
  }

  @Request
  @Test
  public void testTypeMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    // test on a new instance
    Assert.assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    Assert.assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());

    // test on an applied instance (to make sure the proxies persisted the
    // metadata)
    collectionClass.getMethod("apply").invoke(object);
    Assert.assertEquals(collection.getDisplayLabel(CommonProperties.getDefaultLocale()), object.getMd().getDisplayLabel());
    Assert.assertEquals(collection.getDescription(CommonProperties.getDefaultLocale()), object.getMd().getDescription());
    Assert.assertEquals(collection.getOid(), object.getMd().getOid());
  }

  @Request
  @Test
  public void testBlobMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getABlobMd").invoke(object);

    checkAttributeMd(collectionBlob, mdDTO);
  }

  @Request
  @Test
  public void testDateTimeMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getADateTimeMd").invoke(object);

    checkAttributeMd(collectionDateTime, mdDTO);
  }

  @Request
  @Test
  public void testDateMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getADateMd").invoke(object);

    checkAttributeMd(collectionDate, mdDTO);
  }

  @Request
  @Test
  public void testTimeMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATimeMd").invoke(object);

    checkAttributeMd(collectionTime, mdDTO);
  }

  @Request
  @Test
  public void testBooleanMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getABooleanMd").invoke(object);

    checkAttributeMd(collectionBoolean, mdDTO);
  }

  @Request
  @Test
  public void testReferenceMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getAReferenceMd").invoke(object);

    checkAttributeMd(collectionReference, mdDTO);

    Assert.assertEquals(collectionReference.isSystem(), mdDTO.isSystem());
  }

  @Request
  @Test
  public void testTermMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATermMd").invoke(object);

    checkAttributeMd(collectionTerm, mdDTO);

    Assert.assertEquals(collectionTerm.isSystem(), mdDTO.isSystem());
    Assert.assertTrue(mdDTO instanceof AttributeTermMdDTO);
  }

  @Request
  @Test
  public void testIntegerMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod("getAnIntegerMd").invoke(object);

    checkAttributeMd(collectionInteger, mdDTO);
    Assert.assertEquals(Boolean.parseBoolean(collectionInteger.isPositiveRejected()), mdDTO.rejectPositive());
  }

  @Request
  @Test
  public void testLongMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod("getALongMd").invoke(object);

    checkAttributeMd(collectionLong, mdDTO);
  }

  @Request
  @Test
  public void testCharacterMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) collectionClass.getMethod("getACharacterMd").invoke(object);

    checkAttributeMd(collectionCharacter, mdDTO);
    Assert.assertEquals(Integer.parseInt(collectionCharacter.getSize()), mdDTO.getSize());
  }

  @Request
  @Test
  public void testStructMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeStructMdDTO mdDTO = (AttributeStructMdDTO) collectionClass.getMethod("getAStructMd").invoke(object);

    checkAttributeMd(collectionStruct, mdDTO);
    Assert.assertEquals(collectionStruct.getMdStructDAOIF().definesType(), mdDTO.getDefiningMdStruct());
  }

  @Request
  @Test
  public void testDecimalMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getADecimalMd").invoke(object);

    checkAttributeMd(collectionDecimal, mdDTO);
  }

  @Request
  @Test
  public void testDoubleMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getADoubleMd").invoke(object);

    checkAttributeMd(collectionDouble, mdDTO);
  }

  @Request
  @Test
  public void testFloatMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getAFloatMd").invoke(object);

    checkAttributeMd(collectionFloat, mdDTO);
  }

  @Request
  @Test
  public void testEnumerationMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEnumerationMdDTO mdDTO = (AttributeEnumerationMdDTO) collectionClass.getMethod("getAnEnumMd").invoke(object);

    checkAttributeMd(collectionEnumeration, mdDTO);
    Assert.assertEquals(collectionEnumeration.selectMultiple(), mdDTO.selectMultiple());
  }

  @Request
  @Test
  public void testTextMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATextMd").invoke(object);

    checkAttributeMd(collectionText, mdDTO);
  }

  @Request
  @Test
  public void testClobMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getAClobMd").invoke(object);

    checkAttributeMd(collectionClob, mdDTO);
  }

  @Request
  @Test
  public void testHashMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod("getAHashMd").invoke(object);

    checkAttributeMd(collectionHash, mdDTO);
    Assert.assertEquals(collectionHash.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  @Request
  @Test
  public void testSymmetricMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod("getASymMd").invoke(object);

    checkAttributeMd(collectionSymmetric, mdDTO);
    Assert.assertEquals(collectionSymmetric.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  @Request
  @Test
  public void testStructCharacterMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    Class<?> structClass = loader.loadClass(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    StructDTO structDTO = (StructDTO) collectionClass.getMethod("getAStruct").invoke(object);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) structClass.getMethod("getStructCharacterMd").invoke(structDTO);

    checkAttributeMd(structCharacter, mdDTO);
    Assert.assertEquals(Integer.parseInt(structCharacter.getSize()), mdDTO.getSize());
  }

  @Request
  @Test
  public void testFileMetadata() throws Exception
  {
    GeneratedLoader loader = GeneratedLoader.createClassLoader();

    Class<?> collectionClass = loader.loadClass(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getAFileMd").invoke(object);

    checkAttributeMd(collectionFile, mdDTO);
  }

  /**
   * This method does all the checks for attribute metadata on DTOs. All checks,
   * except for attribute specific metadata is consolidated here (it's better
   * than copying/pasting the same checks into a dozen different tests). <<<<<<<
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
    Assert.assertEquals(mdAttribute.getOid(), mdDTO.getOid());
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
