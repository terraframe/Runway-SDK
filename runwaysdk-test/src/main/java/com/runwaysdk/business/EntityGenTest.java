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
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.security.provider.Sun;

import com.runwaysdk.ClientSession;
import com.runwaysdk.SystemException;
import com.runwaysdk.business.generation.CompilerException;
import com.runwaysdk.business.generation.GenerationManager;
import com.runwaysdk.business.generation.dto.ComponentDTOGenerator;
import com.runwaysdk.business.state.MdStateMachineDAO;
import com.runwaysdk.business.state.StateMasterDAO;
import com.runwaysdk.business.state.StateMasterDAOIF;
import com.runwaysdk.constants.ClientRequestIF;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.Constants;
import com.runwaysdk.constants.DatabaseProperties;
import com.runwaysdk.constants.EntityCacheMaster;
import com.runwaysdk.constants.EntityTypes;
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
import com.runwaysdk.constants.MdClassInfo;
import com.runwaysdk.constants.MdElementInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStateMachineInfo;
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
import com.runwaysdk.dataaccess.TransitionDAO;
import com.runwaysdk.dataaccess.UnexpectedTypeException;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.XMLImporter;
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
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.generation.LoaderDecoratorExceptionIF;
import com.runwaysdk.generation.loader.LoaderDecorator;
import com.runwaysdk.generation.loader.Reloadable;
import com.runwaysdk.query.OIterator;
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
import com.runwaysdk.util.FileIO;

@SuppressWarnings("unchecked")
public class EntityGenTest extends TestCase
{
  final static Logger logger = LoggerFactory.getLogger(EntityGenTest.class);
  
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

  /**
   * Launch-point for the standalone textui JUnit tests in this class.
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    if (DatabaseProperties.getDatabaseClass().equals("hsqldb"))
    {
      XMLImporter.main(new String[] { TestConstants.Path.schema_xsd, TestConstants.Path.metadata_xml });
    }
    junit.textui.TestRunner.run(EntityGenTest.suite());
  }

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

  private static volatile MdStateMachineDAO   mdState         = null;

  private static ClientSession                systemSession   = null;

  private static ClientRequestIF              clientRequestIF = null;

  private static StateMasterDAO               state1;

  private static StateMasterDAO               state2;

  private static StateMasterDAO               state3;

  private static String                       suitEnumDTO;

  private boolean                             didSetup        = false;

  private boolean                             didTeardown     = false;

  public static Test suite()
  {
    TestSuite suite = new TestSuite(EntityGenTest.class.getSimpleName());
    suite.addTestSuite(EntityGenTest.class);

//     TestSetup wrapper = new TestSetup(suite)
//     {
//     protected void setUp()
//     {
//     classSetUp();
//     }
//    
//     protected void tearDown()
//     {
//     classTearDown();
//     }
//     };
//    
//     return wrapper;

    return suite;
  }

  protected void setUp()
  {
    if (didSetup == false)
    {
      didSetup = true;
      classSetUp();
    }

    systemSession = ClientSession.createUserSession(ServerConstants.SYSTEM_USER_NAME, ServerConstants.SYSTEM_DEFAULT_PASSWORD, new Locale[] { CommonProperties.getDefaultLocale() });
    clientRequestIF = systemSession.getRequest();

    if (!MdBusinessDAO.isDefined(pack + ".Car"))
    {
      makeCar();
    }
  }

  protected void tearDown()
  {
    if (didTeardown == false)
    {
      didTeardown = true;
      classTearDown();
    }

    systemSession.logout();
  }

  private static void classSetUp()
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
    structBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    structBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    structBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Struct Boolean");
    structBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, struct.getId());
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
    reference.setGenerateMdController(false);
    reference.apply();

    term = MdTermDAO.newInstance();
    term.setValue(MdBusinessInfo.NAME, "Term");
    term.setValue(MdBusinessInfo.PACKAGE, pack);
    term.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    term.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term Class");
    term.setGenerateMdController(false);
    term.apply();

    MdAttributeIntegerDAO referenceInt = MdAttributeIntegerDAO.newInstance();
    referenceInt.setValue(MdAttributeIntegerInfo.NAME, "referenceInt");
    referenceInt.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference Integer");
    referenceInt.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, reference.getId());
    referenceInt.apply();

    collection = MdBusinessDAO.newInstance();
    collection.setValue(MdBusinessInfo.NAME, "Collection");
    collection.setValue(MdBusinessInfo.PACKAGE, pack);
    collection.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collection.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection Class");
    collection.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collection.setGenerateMdController(false);
    collection.apply();
    collectionType = collection.definesType();

    collectionSub = MdBusinessDAO.newInstance();
    collectionSub.setValue(MdBusinessInfo.NAME, "CollectionSub");
    collectionSub.setValue(MdBusinessInfo.PACKAGE, pack);
    collectionSub.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    collectionSub.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes");
    collectionSub.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "All Attributes Description");
    collectionSub.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, collection.getId());
    collectionSub.setGenerateMdController(false);
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

    collectionLocalChar = MdAttributeLocalCharacterDAO.newInstance();
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.NAME, "aLocalCharacter");
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLocalChar.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Character");
    collectionLocalChar.setStructValue(MdAttributeLocalCharacterInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Character Desc");
    collectionLocalChar.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, collection.getId());
    collectionLocalChar.apply();

    // collectionLocalChar.addDefaultLocale();

    collectionLocalText = MdAttributeLocalTextDAO.newInstance();
    collectionLocalText.setValue(MdAttributeLocalTextInfo.NAME, "aLocalText");
    collectionLocalText.setValue(MdAttributeLocalTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionLocalText.setValue(MdAttributeLocalTextInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionLocalText.setStructValue(MdAttributeLocalTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Text");
    collectionLocalText.setStructValue(MdAttributeLocalTextInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Local Text Desc");
    collectionLocalText.setValue(MdAttributeLocalTextInfo.DEFINING_MD_CLASS, collection.getId());
    collectionLocalText.apply();

    // collectionLocalText.addDefaultLocale();

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

    collectionTerm = MdAttributeTermDAO.newInstance();
    collectionTerm.setValue(MdAttributeTermInfo.NAME, "aTerm");
    collectionTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term");
    collectionTerm.setStructValue(MdAttributeTermInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Term Desc");
    collectionTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, collection.getId());
    collectionTerm.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    collectionTerm.setValue(MdAttributeTermInfo.IMMUTABLE, MdAttributeBooleanInfo.FALSE);
    collectionTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, term.getId());
    collectionTerm.apply();

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

    mdRelationship = MdRelationshipDAO.newInstance();
    mdRelationship.setValue(MdRelationshipInfo.NAME, "ARelationship");
    mdRelationship.setValue(MdRelationshipInfo.PACKAGE, pack);
    mdRelationship.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Relationship");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, collection.getId());
    mdRelationship.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    mdRelationship.setValue(MdRelationshipInfo.PARENT_METHOD, "RelParent");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, reference.getId());
    mdRelationship.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    mdRelationship.setValue(MdRelationshipInfo.CHILD_METHOD, "RelChild");
    mdRelationship.setGenerateMdController(false);
    mdRelationship.apply();

    mdState = MdStateMachineDAO.newInstance();
    mdState.setValue(MdStateMachineInfo.NAME, "CollectionState");
    mdState.setValue(MdStateMachineInfo.PACKAGE, pack);
    mdState.setStructValue(MdStateMachineInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Collection State");
    mdState.setValue(MdStateMachineInfo.SUPER_MD_BUSINESS, EntityTypes.STATE_MASTER.getId());
    mdState.setValue(MdStateMachineInfo.STATE_MACHINE_OWNER, collection.getId());
    mdState.apply();

    state1 = mdState.addState("Preparing", StateMasterDAOIF.Entry.DEFAULT_ENTRY_STATE.getId());
    state1.apply();

    state2 = mdState.addState("Collecting", StateMasterDAOIF.Entry.ENTRY_STATE.getId());
    state2.apply();

    state3 = mdState.addState("Finished", StateMasterDAOIF.Entry.NOT_ENTRY_STATE.getId());
    state3.apply();

    mdState.addTransition("Setup", state1.getId(), state2.getId()).apply();

    mdState.addTransition("Teardown", state2.getId(), state3.getId()).apply();

    collectionDTO = collection.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    collectionSubDTO = collectionSub.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    suitEnumDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
  }

  private static void classTearDown()
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

  public void testInstance() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    String id = businessDAO.apply();

    Business business = Business.get(id);

    if (!id.equals(business.getId()))
    {
      fail("An applied instance did not match the retrieved instance.");
    }
  }

  public void testNewStructWithClassType() throws Exception
  {
    try
    {
      BusinessFacade.newStruct(collectionType);
      fail("Allowed a Struct to be created for a business type");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
  }

  public void testNewElementWithStructType() throws Exception
  {
    try
    {
      BusinessFacade.newBusiness(struct.definesType());
      fail("Allowed a Element to be created for a struct type");
    }
    catch (UnexpectedTypeException e)
    {
      // This is expected
    }
  }

  public void testLoad() throws Exception
  {
    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The Top Speed");
    topSpeed.apply();

    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);

    Object newCar = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(newCar, 200);
    carClass.getMethod("apply").invoke(newCar);

    List<String> ids = EntityDAO.getEntityIdsDB(car.definesType());
    if (ids.size() != 1)
      fail("Expected to find 1 Car, but found " + ids.size());

    BusinessDAOIF businessDAOIF = BusinessDAO.get(ids.get(0));
    if (!businessDAOIF.getValue("topSpeed").equals("200"))
      fail("setTopSpeed was not invoked correctly");

    topSpeed.delete();
  }

  public void testReLoad() throws Exception
  {
    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);

    try
    {
      Method setTopSpeedMethod = carClass.getMethod("setTopSpeed", Integer.TYPE);
      Object newCar = carClass.getConstructor().newInstance();
      setTopSpeedMethod.invoke(newCar, 200);
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
    Object newerCar = carClass.getConstructor().newInstance();
    carClass.getMethod("setTopSpeed", Integer.class).invoke(newerCar, 200);
    carClass.getMethod("apply").invoke(newerCar);
  }

  public void testReLoadBlob() throws Exception
  {
    String type = car.definesType();
    Class<?> carClass = LoaderDecorator.load(type);
    byte[] data = "Some blob data".getBytes();

    try
    {
      Method setBlobDataMethod = carClass.getMethod("setBlobData", byte[].class);
      Object newCar = carClass.getConstructor().newInstance();
      setBlobDataMethod.invoke(newCar, data);
      fail("The class invoked a method that doesn't exist yet");
    }
    catch (NoSuchMethodException e)
    {
      // This is expected
    }

    MdAttributeBlobDAO blobdata = MdAttributeBlobDAO.newInstance();
    blobdata.setValue(MdAttributeConcreteInfo.NAME, "blobData");
    blobdata.setValue(MdAttributeConcreteInfo.DEFINING_MD_CLASS, car.getId());
    blobdata.setStructValue(MdAttributeConcreteInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A blob attribute");
    blobdata.apply();

    carClass = LoaderDecorator.load(type);

    // After adding the blobData attribute, setBlobData should exist
    Object newerCar = carClass.getConstructor().newInstance();
    carClass.getMethod("setBlobData", byte[].class).invoke(newerCar, data);
    carClass.getMethod("apply").invoke(newerCar);
  }

  public void testDelete()
  {
    MdBusinessDAO.get(car.getId()).getBusinessDAO().delete();

    if (MdBusinessDAO.isDefined(pack + ".Car"))
      fail("Car was not deleted!");

    makeCar();
  }

  public void testSetBlob() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAOIF = BusinessDAO.get(id);
    byte[] out = businessDAOIF.getBlob("aBlob");

    if (in.length != out.length)
      fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        fail("Stored and Retrieved blobs have different values.");
  }

  public void testGetBlob() throws Exception
  {
    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setBlob("aBlob", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    byte[] out = (byte[]) collectionClass.getMethod("getABlob").invoke(object);

    if (in.length != out.length)
      fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        fail("Stored and Retrieved blobs have different values.");
  }

  public void testSetBoolean() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    boolean in = true;
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    boolean out = Boolean.parseBoolean(businessDAO.getValue("aBoolean"));

    if (in != out)
      fail("Stored and Retrieved booleans have different values.");
  }

  public void testGetBoolean() throws Exception
  {
    boolean in = false;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", Boolean.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    if (in != out)
      fail("Stored and Retrieved booleans have different values.");
  }

  public void testGetBooleanNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    if (out != null)
    {
      fail("A Boolean getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetCharacter() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "Mr. Sparkle";
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aCharacter");

    if (!in.equals(out))
      fail("Stored and Retrieved Characters have different values.");
  }

  public void testGetCharacter() throws Exception
  {
    String in = "RunwaySDK";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aCharacter", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getACharacter").invoke(object);

    if (!in.equals(out))
      fail("Stored and Retrieved Characters have different values.");
  }

  public void testSetDecimal() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    BigDecimal in = new BigDecimal(123456.789);

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAOIF = BusinessDAO.get(id);

    BigDecimal out = new BigDecimal(businessDAOIF.getValue("aDecimal"));

    if (in.subtract(out).abs().doubleValue() > .0000001)
      fail("Stored and Retrieved Decimals have different values.");
  }

  public void testGetDecimal() throws Exception
  {
    double in = 987654.321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", Double.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    double out = ( (BigDecimal) collectionClass.getMethod("getADecimal").invoke(object) ).doubleValue();

    if (in != out)
      fail("Stored and Retrieved Decimals have different values.");
  }

  public void testGetDecimalNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Double out = (Double) collectionClass.getMethod("getADecimal").invoke(object);

    if (out != null)
    {
      fail("A Decimal getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetDouble() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    double in = 123456.789;
    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    double out = Double.parseDouble(businessDAO.getValue("aDouble"));

    if (in != out)
      fail("Stored and Retrieved Doubles have different values.");
  }

  public void testGetDouble() throws Exception
  {
    double in = 98765.4321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", Double.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    if (in != out)
      fail("Stored and Retrieved Doubles have different values.");
  }

  public void testGetDoubleNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    if (out != null)
    {
      fail("A Double getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetFloat() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    float in = 123456.789F;
    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    float out = Float.parseFloat(businessDAO.getValue("aFloat"));

    if (in != out)
      fail("Stored and Retrieved Floats have different values.");
  }

  public void testGetFloat() throws Exception
  {
    float in = 987.654321F;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", Float.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    if (in != out)
      fail("Stored and Retrieved Floats have different values.");
  }

  public void testGetFloatNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    if (out != null)
    {
      fail("A Float getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetHash() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "When you win, say nothing. When you lose, say less.";
    collectionClass.getMethod("setAHash", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
    digest.update(in.getBytes());
    String hash = Base64.encodeToString(digest.digest(), false);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aHash");

    if (!hash.equals(out))
      fail("Stored and Retrieved Hashes have different values.");
  }

  public void testHashEquals() throws Exception
  {
    String in = "For breakfast, I had some Pringles, and some fudge-striped cook-ays";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aHash", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    boolean out = (Boolean) collectionClass.getMethod("aHashEquals", String.class).invoke(object, "For breakfast, I had some Pringles, and some fudge-striped cook-ays");

    if (!out)
      fail("Stored Hash did not equal equivalent value.");
  }

  public void testSetSymmetric() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "My rims never spin - to the contrary";
    collectionClass.getMethod("setASym", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aSym");

    if (!in.equals(out))
      fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
  }

  public void testGetSymmetric() throws Exception
  {
    String in = "You'll find that they're quite stationary";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aSym", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getASym").invoke(object);

    if (!in.equals(out))
      fail("Stored and Retrieved Symmetric encrypted attributes have different values.");
  }

  public void testSetInteger() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    int in = 1234;
    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    int out = Integer.parseInt(businessDAO.getValue("anInteger"));

    if (in != out)
      fail("Stored and Retrieved Integers have different values.");
  }

  public void testGetInteger() throws Exception
  {
    int in = 9876;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", Integer.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    int out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    if (in != out)
      fail("Stored and Retrieved Integers have different values.");
  }

  public void testGetIntegerNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Integer out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    if (out != null)
    {
      fail("An Integer getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetLong() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    long in = 123456789;
    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    long out = Long.parseLong(businessDAO.getValue("aLong"));

    if (in != out)
      fail("Stored and Retrieved Longs have different values.");
  }

  public void testGetLong() throws Exception
  {
    long in = 987654321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", Long.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    if (in != out)
      fail("Stored and Retrieved Longs have different values.");
  }

  public void testSetLocalCharacter() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Struct struct = (Struct) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      fail("Stored and Retrieved Local character have different values.");
    }
  }

  public void testGetLocalCharacter() throws Exception
  {
    String in = Long.toString(987654321);
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Struct struct = (Struct) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      fail("Stored and Retrieved Local character have different values.");
    }
  }

  public void testSetLocalText() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Struct struct = (Struct) collectionClass.getMethod("getALocalText").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      fail("Stored and Retrieved Local text have different values.");
    }
  }

  public void testGetLocalText() throws Exception
  {
    String in = Long.toString(987654321);
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Struct struct = (Struct) collectionClass.getMethod("getALocalText").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    if (!in.equals(out))
    {
      fail("Stored and Retrieved local text have different values.");
    }
  }

  public void testGetLongNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    if (out != null)
    {
      fail("A Long getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetDate() throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aDate"));

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved Dates have different values.");
  }

  public void testGetDate() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved Dates have different values.");
  }

  public void testGetDateNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    if (out != null)
    {
      fail("A Date getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetDateTime() throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aDateTime"));

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved DateTimes have different values.");
  }

  public void testGetDateTime() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved DateTimes have different values.");
  }

  public void testGetDateTimeNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    if (out != null)
    {
      fail("A DateTime getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetTime() throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aTime"));

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved Times have different values.");
  }

  public void testGetTime() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    if (!sdf.format(in).equals(sdf.format(out)))
      fail("Stored and Retrieved Times have different values.");
  }

  public void testGetTimeNull() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);

    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    if (out != null)
    {
      fail("A Time getter method was supposed to return null.");
    }

    businessDAO.delete();
  }

  public void testSetText() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";
    collectionClass.getMethod("setAText", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aText");

    if (!in.equals(out))
      fail("Stored and Retrieved Texts have different values.");
  }

  public void testSetClob() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    String in = "CLOB: But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";
    collectionClass.getMethod("setAClob", String.class).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aClob");

    if (!in.equals(out))
      fail("Stored and Retrieved Clobs have different values.");
  }

  public void testGetText() throws Exception
  {
    String in = "Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aText", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    if (!in.equals(out))
      fail("Stored and Retrieved Texts have different values. In value: " + in + " Out value: " + out);
  }

  public void testGetClob() throws Exception
  {
    String in = "CLOB: Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aClob", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    String out = (String) collectionClass.getMethod("getAClob").invoke(object);

    if (!in.equals(out))
      fail("Stored and Retrieved Clobs have different values. In value: " + in + " Out value: " + out);
  }

  public void testSetStructCharacter() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Object object = collectionClass.getConstructor().newInstance();

    String in = "Dwight Schrute";
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aStruct", "structCharacter");

    if (!in.equals(out))
      fail("Stored and Retrieved StructCharacters have different values.");
  }

  public void testGetStructCharacter() throws Exception
  {
    String in = "Smethie wuz Here!!!!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    String out = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    if (!in.equals(out))
      fail("Stored and Retrieved StructCharacters have different values.");
  }

  public void testSetStructEnumeration() throws Exception
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
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    StructDAO structDAO = ( (AttributeStructIF) businessDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    assertEquals(1, enums.length);
    assertEquals(heartsId, enums[0].getId());
  }

  public void testGetStructEnumeration() throws Exception
  {
    String input = "This is myself.";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", input);
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    List<?> out = (List<?>) structClass.getMethod("getStructEnumeration").invoke(struct);

    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getId").invoke(head);
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    assertEquals(input, structChar);
    assertEquals(1, out.size());
    assertEquals(heartsId, outId);
  }

  public void testSetStructBoolean() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Object object = collectionClass.getConstructor().newInstance();

    boolean in = true;
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    boolean out = Boolean.parseBoolean(businessDAO.getStructValue("aStruct", "structBoolean"));

    if (in != out)
      fail("Stored and Retrieved StructBooleans have different values.");
  }

  public void testGetStructBoolean() throws Exception
  {
    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    boolean out = (Boolean) structClass.getMethod("getStructBoolean").invoke(struct);

    if (in != out)
      fail("Stored and Retrieved StructBooleans have different values.");
  }

  public void testSetReference() throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());
    Business in = (Business) referenceClass.getConstructor().newInstance();
    referenceClass.getMethod("apply").invoke(in);

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getConstructor().newInstance();

    collectionClass.getMethod("setAReference", referenceClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    BusinessDAOIF out = ( (AttributeReferenceIF) businessDAO.getAttributeIF("aReference") ).dereference();

    if (!in.getId().equalsIgnoreCase(out.getId()))
      fail("Stored and Retrieved References are different.");
  }

  public void testGetReference() throws Exception
  {
    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getId());
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, id);
    Business out = (Business) collectionClass.getMethod("getAReference").invoke(object);

    if (!in.getId().equalsIgnoreCase(out.getId()))
      fail("Stored and Retrieved References are different.");
  }

  public void testEnumDTO_getEnumNames() throws Exception
  {
    String in = heartsId;
    String in2 = clubsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    businessDAO.addItem("anEnum", in2);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO businessDTO = (BusinessDTO) get.invoke(null, clientRequestIF, id);

    String method = "getAnEnum" + TypeGeneratorInfo.ATTRIBUTE_ENUMERATION_ENUM_NAMES_SUFFIX;
    List<String> enumNames = (List<String>) collectionClass.getMethod(method).invoke(businessDTO);

    assertEquals(2, enumNames.size());

    String enumName1 = enumNames.get(0);
    String enumName2 = enumNames.get(1);

    assertTrue(heartName.equals(enumName1) || heartName.equals(enumName2));
    assertTrue(clubName.equals(enumName1) || clubName.equals(enumName2));
  }

  public void testEnumDTO_getName() throws Exception
  {
    Class<?> enumClass = LoaderDecorator.load(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);

    Method getName = enumClass.getMethod("getName");
    String name = (String) getName.invoke(hearts);

    assertEquals(hearts.name(), name);
  }

  public void testEnumDTO_item() throws Exception
  {
    Class<?> enumClass = LoaderDecorator.load(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);

    Method item = enumClass.getMethod("item", ClientRequestIF.class);
    BusinessDTO heartsDTO = (BusinessDTO) item.invoke(hearts, clientRequestIF);

    assertEquals(heartsDTO.getValue("enumName"), hearts.name());
  }

  public void testEnumDTO_items() throws Exception
  {
    Class<?> enumClass = LoaderDecorator.load(suitEnumDTO);

    Method valueOf = enumClass.getMethod("valueOf", String.class);
    Enum<?> hearts = (Enum<?>) valueOf.invoke(null, heartName);
    Enum<?> clubs = (Enum<?>) valueOf.invoke(null, clubName);

    Object[] array = (Object[]) Array.newInstance(enumClass, 2);
    array[0] = hearts;
    array[1] = clubs;

    Method items = enumClass.getMethod("items", ClientRequestIF.class, array.getClass());
    List<? extends BusinessDTO> values = (List<? extends BusinessDTO>) items.invoke(hearts, clientRequestIF, array);

    assertEquals(values.size(), 2);

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

    assertTrue(heartsFound && clubsFound);
  }

  public void testEnumDTO_allItems() throws Exception
  {
    Class<?> enumClass = LoaderDecorator.load(suitEnumDTO);

    Method items = enumClass.getMethod("allItems", ClientRequestIF.class);
    List<? extends BusinessDTO> values = (List<? extends BusinessDTO>) items.invoke(null, clientRequestIF);

    assertEquals(values.size(), 4);

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

    assertTrue(heartsFound && clubsFound && spadesFound && diamondsFound);
  }

  public void testAddEnum() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    Object object = collectionClass.getConstructor().newInstance();

    Object in = enumClass.getDeclaredField(diamondsName).get(null);

    collectionClass.getMethod("addAnEnum", enumClass).invoke(object, in);
    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Set<String> ids = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();
    if (ids.size() != 1)
      fail("Expected 1 enum value, found " + ids.size());

    String out = BusinessDAO.get(ids.iterator().next()).getValue(EnumerationMasterInfo.NAME);

    if (!out.equals(diamondsName))
      fail("Stored and Retrieved enums have different values.");
  }

  public void testGetEnum() throws Exception
  {
    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, id);
    List<?> out = (List<?>) collectionClass.getMethod("getAnEnum").invoke(object);
    BusinessEnumeration head = (BusinessEnumeration) out.get(0);
    String outId = (String) head.getClass().getMethod("getId").invoke(head);

    if (!in.equalsIgnoreCase(outId))
      fail("Stored and Retrieved enums have different values.");
  }

  public void testRemoveEnum() throws Exception
  {
    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> enumClass = LoaderDecorator.load(suitEnum.definesType());

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    Object object = collectionClass.getMethod("get", String.class).invoke(null, id);
    collectionClass.getMethod("removeAnEnum", enumClass).invoke(object, hearts);
    collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(id).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    if (out.size() != 0)
      fail("Failed to remove an enumerated value.");
  }

  public void testClearEnum() throws Exception
  {
    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Object object = collectionClass.getMethod("get", String.class).invoke(null, id);
    collectionClass.getMethod("clearAnEnum").invoke(object);
    collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(id).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    if (out.size() != 0)
      fail("Failed to clear an enumerated value.");
  }

  public void testAddChild() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());

    Business mom = (Business) collectionClass.newInstance();
    mom.apply();
    Business kid = (Business) referenceClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship) collectionClass.getMethod("addRelChild", referenceClass).invoke(mom, kid);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(mom.getId()))
      fail("Parent reference mismatch in addChild");

    if (!oracle.getChildId().equals(kid.getId()))
      fail("Child reference mismatch in addChild");
  }

  public void testAddChildDTO() throws Exception
  {
    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);

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

    RelationshipDAOIF oracle = RelationshipDAO.get(instance.getId());

    assertEquals(oracle.getParentId(), mom.getId());
    assertEquals(oracle.getChildId(), kid.getId());
  }

  public void testAddParent() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());

    Business mom = (Business) collectionClass.newInstance();
    mom.apply();
    Business kid = (Business) referenceClass.newInstance();
    kid.apply();

    Relationship rel = (Relationship) referenceClass.getMethod("addRelParent", collectionClass).invoke(kid, mom);
    rel.apply();
    RelationshipDAOIF oracle = RelationshipDAO.get(rel.getId());

    if (!oracle.getParentId().equals(mom.getId()))
      fail("Parent reference mismatch in addParent");

    if (!oracle.getChildId().equals(kid.getId()))
      fail("Child reference mismatch in addParent");
  }

  public void testGetChildren() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType());
    rel.apply();

    Business businessMom = Business.get(mom.getId());

    List<Business> list = ( (OIterator<Business>) collectionClass.getMethod("getAllRelChild").invoke(businessMom) ).getAll();
    if (list.size() != 1)
      fail("Expected getAllChildren to return 1, found " + list.size());
    Business oracle = list.iterator().next();

    if (!oracle.getId().equals(kid.getId()))
      fail("Child reference mismatch in getAllChildren");
  }

  public void testGetParents() throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType());
    rel.apply();

    Business businessKid = Business.get(kid.getId());

    List<Business> list = ( (OIterator<Business>) referenceClass.getMethod("getAllRelParent").invoke(businessKid) ).getAll();
    if (list.size() != 1)
      fail("Expected getAllPrents to return 1, found " + list.size());
    Business oracle = list.iterator().next();

    if (!oracle.getId().equals(mom.getId()))
      fail("Parent reference mismatch in getAllParents");
  }

  public void testRemoveAllChildren() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    BusinessDAO kid2 = BusinessDAO.newInstance(reference.definesType());
    kid2.apply();
    RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(mom.getId(), kid2.getId(), mdRelationship.definesType()).apply();

    Business businessMom = Business.get(mom.getId());
    Business businessKid = Business.get(kid.getId());
    collectionClass.getMethod("removeRelChild", referenceClass).invoke(businessMom, businessKid);

    List<RelationshipDAOIF> list = mom.getChildren(mdRelationship.definesType());
    if (list.size() != 1)
      fail("RemoveAllChilren expected 1 child, found " + list.size());

    RelationshipDAOIF rel = list.get(0);
    if (!rel.getParentId().equals(mom.getId()))
      fail("Unexpected parent after removeAllChildren.");

    if (!rel.getChildId().equals(kid2.getId()))
      fail("Unexpected child after removeAllChildren.");

  }

  public void testRemoveAllParents() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO dad = BusinessDAO.newInstance(collection.definesType());
    dad.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(dad.getId(), kid.getId(), mdRelationship.definesType()).apply();

    Business businessMom = Business.get(mom.getId());
    Business businessKid = Business.get(kid.getId());
    referenceClass.getMethod("removeRelParent", collectionClass).invoke(businessKid, businessMom);

    List<RelationshipDAOIF> list = kid.getParents(mdRelationship.definesType());
    if (list.size() != 1)
      fail("RemoveAllParents expected 1 parent, found " + list.size());

    RelationshipDAOIF rel = list.get(0);
    if (!rel.getParentId().equals(dad.getId()))
    {
      fail("Unexpected parent after removeAllParents.");
    }

    if (!rel.getChildId().equals(kid.getId()))
    {
      fail("Unexpected child after removeAllParents.");
    }

  }

  /**
   * Test for apply on a StructDAO that lives outside of an AttributeStruct
   * <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testApplyStruct() throws Exception
  {
    String in = "Haaaar Harr, BSG";

    Class<?> structClass = LoaderDecorator.load(struct.definesType());
    Object struct = structClass.getConstructor().newInstance();

    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    structClass.getMethod("apply").invoke(struct);

    String id = (String) structClass.getMethod("getId").invoke(struct);
    StructDAOIF structDAO = StructDAO.get(id);
    String out = structDAO.getValue("structCharacter");

    if (!in.equals(out))
    {
      fail("Stored and Retrieved StructCharacters have different values.");
    }
  }

  /**
   * Test to ensure apply on a StructDAO that lives inside of an AttributeStruct
   * balks (does nothing). <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testNoApplyStruct() throws Exception
  {
    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Class<?> structClass = LoaderDecorator.load(struct.definesType());

    Method get = collectionClass.getMethod("get", String.class);
    Object object = get.invoke(null, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, !in);
    structClass.getMethod("apply").invoke(struct);

    BusinessDAOIF businessDAOIF = BusinessDAO.get(id);

    boolean out = Boolean.parseBoolean(businessDAOIF.getStructValue("aStruct", "structBoolean"));

    if (in != out)
      fail("Stored and Retrieved StructBooleans have different values.");
  }

  public void testAddParentDTO() throws Exception
  {
    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);

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

    RelationshipDAOIF oracle = RelationshipDAO.get(instance.getId());

    assertEquals(oracle.getParentId(), mom.getId());
    assertEquals(oracle.getChildId(), kid.getId());
  }

  public void testDeleteDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    collectionClass.getMethod("lock").invoke(object);
    collectionClass.getMethod("delete").invoke(object);

    try
    {
      BusinessDAO.get(id);

      fail("Delete businessDTO did not delete the entity");
    }
    catch (DataNotFoundException e)
    {
      // Expect to land here
    }
  }

  public void testGetBlobDTO() throws Exception
  {
    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setBlob("aBlob", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    byte[] out = (byte[]) collectionClass.getMethod("getABlob").invoke(object);

    if (in.length != out.length)
      fail("Stored and Retrieved blobs are different sizes.");

    for (int i = 0; i < in.length; i++)
      if (in[i] != out[i])
        fail("Stored and Retrieved blobs have different values.");
  }

  /**
   * Test that boolean attributes methods work for DTO generation <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testGetBooleanDTO() throws Exception
  {
    boolean in = false;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", Boolean.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    assertEquals(in, out);
  }

  public void testGetBooleanNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aBoolean", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Boolean out = (Boolean) collectionClass.getMethod("getABoolean").invoke(object);

    assertNull(null, out);
  }

  public void testGetCharacterDTO() throws Exception
  {
    String in = "RunwaySDK";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aCharacter", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    String out = (String) collectionClass.getMethod("getACharacter").invoke(object);

    assertEquals(in, out);
  }

  public void testGetChildDTO() throws Exception
  {
    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType());
    rel.apply();

    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);
    Method get = relationshipClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, rel.getId());

    Method getChild = relationshipClass.getMethod("getChild");
    BusinessDTO child = (BusinessDTO) getChild.invoke(object);

    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    assertTrue(referenceClass.isInstance(child));
    assertEquals(kid.getId(), child.getId());
  }

  public void testGetChildrenDTOCached() throws Exception
  {
    mdRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();

    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_EVERYTHING.getId()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      updateRelationship.apply();
    }
    try
    {
      Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
      List<BusinessDTO> list = (List<BusinessDTO>) collectionClass.getMethod("getAllRelChild").invoke(mom);

      assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      assertTrue(oracle.getType().equals(reference.definesType()));
      assertEquals(oracle.getId(), kidId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
        updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }


  public void testGetChildrenDTONotCached() throws Exception
  {
    mdRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();

    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_NOTHING.getId()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      updateRelationship.apply();
    }
    try
    {
      Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
      List<BusinessDTO> list = (List<BusinessDTO>) collectionClass.getMethod("getAllRelChild").invoke(mom);

      assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      assertTrue(oracle.getType().equals(reference.definesType()));
      assertEquals(oracle.getId(), kidId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
        updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }

  public void testGetChildRelationshipsDTO() throws Exception
  {
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);

    String momId = BusinessDAO.newInstance(collection.definesType()).apply();
    String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

    Object mom = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, momId);
    List<RelationshipDTO> list = (List<RelationshipDTO>) collectionClass.getMethod("getAllRelChildRelationships").invoke(mom);

    assertEquals(3, list.size());
    RelationshipDTO oracle = list.get(0);

    assertTrue(relationshipClass.isInstance(oracle));
    assertEquals(oracle.getChildId(), kidId);
    assertEquals(oracle.getParentId(), momId);
  }

  public void testPublish() throws Exception
  {
    // Make sure we can instantiate the subclass
    Class<?> collectionSubClass = LoaderDecorator.load(collectionSubDTO);
    Constructor<?> get = collectionSubClass.getConstructor(ClientRequestIF.class);
    get.newInstance(clientRequestIF);

    collection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
    collection.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    collection.setGenerateMdController(false);
    collection.apply();

    try
    {
      try
      {
        Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
        get = collectionClass.getConstructor(ClientRequestIF.class);
        get.newInstance(clientRequestIF);

        fail("Able to load a DTO class that was set to not be published");
      }
      catch (RuntimeException ex)
      {
        if (! ( ex instanceof LoaderDecoratorExceptionIF ))
        {
          throw ex;
        }
      }
      try
      {
        // Make sure we cannot instantiate the subclass
        collectionSubClass = LoaderDecorator.load(collectionSubDTO);
        get = collectionSubClass.getConstructor(ClientRequestIF.class);
        get.newInstance(clientRequestIF);

        fail("Able to load a DTO class that was set to not be published");
      }
      catch (RuntimeException ex)
      {
        if (! ( ex instanceof LoaderDecoratorExceptionIF ))
        {
          throw ex;
        }
      }
    }
    finally
    {
      collection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
      collection.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
      collection.apply();

      // Make sure we can instantiate the subclass
      collectionSubClass = LoaderDecorator.load(collectionSubDTO);
      get = collectionSubClass.getConstructor(ClientRequestIF.class);
      get.newInstance(clientRequestIF);
    }
  }

  public void testPublishReference() throws Exception
  {
    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getId());
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);
    collectionClass.getMethod("getAReference").invoke(object);

    collectionClass.getMethod("getAllRelChild").invoke(object);

    reference = MdBusinessDAO.get(reference.getId()).getBusinessDAO();
    reference.setValue(MdClassInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    reference.apply();

    try
    {
      collectionClass = LoaderDecorator.load(collectionDTO);
      object = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);
      collectionClass.getMethod("getAReference").invoke(object);
    }
    catch (NoSuchMethodException e)
    {
      // this is expected
    }

    try
    {
      collectionClass = LoaderDecorator.load(collectionDTO);
      object = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);
      collectionClass.getMethod("getAllRelChild").invoke(object);
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

      businessDAO.delete();
      in.delete();
    }
  }

  public void testPublishRelationship() throws Exception
  {
    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getId());
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object collectionObject = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);
    collectionClass.getMethod("getAllRelChild").invoke(collectionObject);

    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    Object referenceObject = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, in.getId());
    referenceClass.getMethod("getAllRelParent").invoke(referenceObject);

    MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
    updateRelationship.setValue(MdRelationshipInfo.PUBLISH, MdAttributeBooleanInfo.FALSE);
    updateRelationship.apply();

    try
    {
      collectionClass = LoaderDecorator.load(collectionDTO);
      collectionObject = collectionClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);
      collectionClass.getMethod("getAllRelChild").invoke(collectionObject);
    }
    catch (NoSuchMethodException e)
    {
      // this is expected
    }

    try
    {
      referenceClass = LoaderDecorator.load(referenceDTO);
      referenceObject = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, in.getId());
      referenceClass.getMethod("getAllRelParent").invoke(referenceObject);
    }
    catch (NoSuchMethodException e)
    {
      // this is expected
    }
    finally
    {
      updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.setValue(MdRelationshipInfo.PUBLISH, MdAttributeBooleanInfo.TRUE);
      updateRelationship.apply();

      businessDAO.delete();
      in.delete();
    }
  }

  public void testChangeAttributeName() throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object dtoObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    Class<?> colletionClass = LoaderDecorator.load(collection.definesType());
    Object businessObject = colletionClass.getConstructor().newInstance();

    // Make sure the accessor method is there
    colletionClass.getMethod("getACharacter").invoke(businessObject);

    collectionCharacter = MdAttributeCharacterDAO.get(collectionCharacter.getId()).getBusinessDAO();
    
    collectionCharacter.setValue(MdAttributeConcreteInfo.NAME, "AChangedCharacter");
    collectionCharacter.apply();
    LoaderDecorator.reload();
    colletionClass = LoaderDecorator.load(collection.definesType());
    businessObject = colletionClass.getConstructor().newInstance();

    try
    {
      colletionClass.getMethod("getAChangedCharacter").invoke(businessObject);
    }
    catch (NoSuchMethodException e)
    {
      fail("The name of Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed on business class.");
    }

    try
    {
      collectionDTOclass = LoaderDecorator.load(collectionDTO);
      dtoObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("getAChangedCharacter").invoke(dtoObject);
    }
    catch (NoSuchMethodException e)
    {
      fail("The name of Attribute " + collectionCharacter.definesAttribute() + " on generated server base class was not properly changed on DTO class.");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      collectionCharacter.setValue(MdAttributeConcreteInfo.NAME, "aCharacter");
      collectionCharacter.apply();
      LoaderDecorator.reload();
    }
  }

  public void testAttributeGetterVisibility() throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

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
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
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
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("getACharacter").invoke(object);
    }
  }

  public void testAttributeSetterVisibility() throws Exception
  {
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);
    Object object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

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
        object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
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
      object = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("setACharacter", String.class).invoke(object, "123");
    }
  }

  public void testParentMethodVisibility() throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());
    Class<?> referenceBaseClass = referenceClass.getSuperclass();
    Class<?> referenceDTOclass = LoaderDecorator.load(referenceDTO);
    Class<?> relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
    Class<?> colletionClass = LoaderDecorator.load(collection.definesType());
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);

    // Check public visibility on the business class
    int modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("removeRelParent", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [removeRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [getAllRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [getRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

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
    MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
    updateRelationship.clearItems(MdRelationshipInfo.PARENT_VISIBILITY);
    updateRelationship.addItem(MdRelationshipInfo.PARENT_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    updateRelationship.apply();
    LoaderDecorator.reload();

    referenceClass = LoaderDecorator.load(reference.definesType());
    referenceBaseClass = referenceClass.getSuperclass();
    referenceDTOclass = LoaderDecorator.load(referenceDTO);
    relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
    colletionClass = LoaderDecorator.load(collection.definesType());
    collectionDTOclass = LoaderDecorator.load(collectionDTO);

    try
    {
      modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("removeRelParent", colletionClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [removeRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [addRelParent] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [getAllRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [getRelParentRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      try
      {
        // Create some objects
        collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
        referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
        referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

        String parentProtectedFail = "Able to access a parent relationship method on a DTO for a relationship where the parent accessors are [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].";

        try
        {
          aRelationshipDTO = referenceDTOclass.getMethod("addRelParent", collectionDTOclass).invoke(referenceDTOObject, collectionDTOObject);
          relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("getAllRelParent").invoke(referenceDTOObject);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) referenceDTOclass.getMethod("getAllRelParentRelationships").invoke(referenceDTOObject);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("removeRelParent", relationshipDTOclass).invoke(referenceDTOObject, relationshipList.get(0));
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("removeAllRelParent").invoke(referenceDTOObject);
          fail(parentProtectedFail);
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

        String parentProtectedFail = "Able to access a parent relationship method on a DTO for a relationship where the parent accessors are [" + VisibilityModifier.PROTECTED.getJavaModifier() + "].";

        try
        {
          aRelationshipDTO = referenceDTOclass.getMethod("addRelParent", collectionDTOclass).invoke(referenceDTOObject, collectionDTOObject);
          relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("getAllRelParent").invoke(referenceDTOObject);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) referenceDTOclass.getMethod("getAllRelParentRelationships").invoke(referenceDTOObject);
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("removeRelParent", relationshipDTOclass).invoke(referenceDTOObject, relationshipList.get(0));
          fail(parentProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          referenceDTOclass.getMethod("removeAllRelParent").invoke(referenceDTOObject);
          fail(parentProtectedFail);
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
      updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.PARENT_VISIBILITY);
      updateRelationship.addItem(MdRelationshipInfo.PARENT_VISIBILITY, VisibilityModifier.PUBLIC.getId());
      updateRelationship.apply();
      LoaderDecorator.reload();

      referenceClass = LoaderDecorator.load(reference.definesType());
      referenceBaseClass = referenceClass.getSuperclass();
      referenceDTOclass = LoaderDecorator.load(referenceDTO);
      relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
      colletionClass = LoaderDecorator.load(collection.definesType());
      collectionDTOclass = LoaderDecorator.load(collectionDTO);

      // Check public visibility on the business class
      modifiers = referenceBaseClass.getDeclaredMethod("addRelParent", colletionClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship  [addRelParent] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParent").getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship  [addRelParent] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getAllRelParentRel").getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship  [getAllRelParentRel] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = referenceBaseClass.getDeclaredMethod("getRelParentRel", colletionClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship  [getRelParentRel] on generated server base class [" + reference.definesType() + "]was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      // Create some objects
      collectionDTOObject = collectionDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      collectionDTOclass.getMethod("apply").invoke(collectionDTOObject);
      referenceDTOObject = referenceDTOclass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
      referenceDTOclass.getMethod("apply").invoke(referenceDTOObject);

      // Make sure the accessor methods are there on the DTO
      aRelationshipDTO = referenceDTOclass.getMethod("addRelParent", collectionDTOclass).invoke(referenceDTOObject, collectionDTOObject);
      relationshipDTOclass.getMethod("apply").invoke(aRelationshipDTO);

      referenceDTOclass.getMethod("getAllRelParent").invoke(referenceDTOObject);
      relationshipList = (List<?>) referenceDTOclass.getMethod("getAllRelParentRelationships").invoke(referenceDTOObject);
      referenceDTOclass.getMethod("removeRelParent", relationshipDTOclass).invoke(referenceDTOObject, relationshipList.get(0));
      referenceDTOclass.getMethod("removeAllRelParent").invoke(referenceDTOObject);

      // Delete them
      collectionDTOclass.getMethod("delete").invoke(collectionDTOObject);
      referenceDTOclass.getMethod("delete").invoke(referenceDTOObject);

    }
  }

  public void testChildMethodVisibility() throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType());
    Class<?> referenceDTOclass = LoaderDecorator.load(referenceDTO);
    Class<?> relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
    Class<?> colletionClass = LoaderDecorator.load(collection.definesType());
    Class<?> collectionBaseClass = colletionClass.getSuperclass();
    Class<?> collectionDTOclass = LoaderDecorator.load(collectionDTO);

    // Check public visibility on the business class
    int modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

    modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
    if (!Modifier.isPublic(modifiers))
      fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

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
    MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
    updateRelationship.clearItems(MdRelationshipInfo.CHILD_VISIBILITY);
    updateRelationship.addItem(MdRelationshipInfo.CHILD_VISIBILITY, VisibilityModifier.PROTECTED.getId());
    updateRelationship.apply();
    LoaderDecorator.reload();

    referenceClass = LoaderDecorator.load(reference.definesType());
    referenceDTOclass = LoaderDecorator.load(referenceDTO);
    relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
    colletionClass = LoaderDecorator.load(collection.definesType());
    collectionBaseClass = colletionClass.getSuperclass();
    collectionDTOclass = LoaderDecorator.load(collectionDTO);

    try
    {
      modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
      if (!Modifier.isProtected(modifiers))
        fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PROTECTED.getJavaModifier() + "] visibility.");

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
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);
          fail(childProtectedFail);
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
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("getAllRelChild").invoke(collectionDTOObject);
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          relationshipList = (List<?>) collectionDTOclass.getMethod("getAllRelChildRelationships").invoke(collectionDTOObject);
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeRelChild", relationshipDTOclass).invoke(collectionDTOObject, relationshipList.get(0));
          fail(childProtectedFail);
        }
        catch (NoSuchMethodException e)
        {
        }
        try
        {
          collectionDTOclass.getMethod("removeAllRelChild").invoke(collectionDTOObject);
          fail(childProtectedFail);
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
      updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CHILD_VISIBILITY);
      updateRelationship.addItem(MdRelationshipInfo.CHILD_VISIBILITY, VisibilityModifier.PUBLIC.getId());
      updateRelationship.apply();
      LoaderDecorator.reload();

      referenceClass = LoaderDecorator.load(reference.definesType());
      referenceDTOclass = LoaderDecorator.load(referenceDTO);
      relationshipDTOclass = LoaderDecorator.load(relationshipDTO);
      colletionClass = LoaderDecorator.load(collection.definesType());
      collectionBaseClass = colletionClass.getSuperclass();
      collectionDTOclass = LoaderDecorator.load(collectionDTO);

      // Check public visibility on the business class
      modifiers = collectionBaseClass.getDeclaredMethod("addRelChild", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship [addRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("removeRelChild", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship [removeRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChild").getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship [getAllRelChild] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getAllRelChildRel").getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship [getAllRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

      modifiers = collectionBaseClass.getDeclaredMethod("getRelChildRel", referenceClass).getModifiers();
      if (!Modifier.isPublic(modifiers))
        fail("Parent relationship [getRelChildRel] on generated server base class [" + reference.definesType() + "] was not properly changed to [" + VisibilityModifier.PUBLIC.getJavaModifier() + "] visibility.");

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

  public void testGetDateDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    businessDAO.delete();

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testGetDateNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDate", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Date out = (Date) collectionClass.getMethod("getADate").invoke(object);

    assertNull(out);
  }

  public void testGetDateTimeDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testGetDateTimeNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDateTime", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Date out = (Date) collectionClass.getMethod("getADateTime").invoke(object);

    assertNull(out);
  }

  public void testGetDecimalDTO() throws Exception
  {
    double in = 987654.321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", Double.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    double out = ( (BigDecimal) collectionClass.getMethod("getADecimal").invoke(object) ).doubleValue();

    assertEquals(in, out);
  }

  public void testGetDecimalNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDecimal", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Double out = (Double) collectionClass.getMethod("getADecimal").invoke(object);

    assertNull(out);
  }

  public void testGetDoubleDTO() throws Exception
  {
    double in = 98765.4321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", Double.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    assertEquals(in, out);
  }

  public void testGetDoubleNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aDouble", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Double out = (Double) collectionClass.getMethod("getADouble").invoke(object);

    assertNull(out);
  }

  public void testGetEnumDTO() throws Exception
  {
    String in = heartsId;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    List<?> enums = (List<?>) collectionClass.getMethod("getAnEnum").invoke(object);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();

    assertEquals(heartName, out);
  }

  public void testGetFloatDTO() throws Exception
  {
    float in = 987.654321F;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", Float.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    assertEquals(in, out);
  }

  public void testGetFloatNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aFloat", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Float out = (Float) collectionClass.getMethod("getAFloat").invoke(object);

    assertNull(out);
  }

  public void testGetIntegerDTO() throws Exception
  {
    int in = 9876;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", Integer.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    int out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    assertEquals(in, out);
  }

  public void testGetIntegerNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("anInteger", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Integer out = (Integer) collectionClass.getMethod("getAnInteger").invoke(object);

    assertNull(out);
  }

  public void testGetLongDTO() throws Exception
  {
    long in = 987654321;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", Long.toString(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    long out = (Long) collectionClass.getMethod("getALong").invoke(object);

    assertEquals(in, out);
  }

  public void testGetLongNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Float out = (Float) collectionClass.getMethod("getALong").invoke(object);

    assertNull(out);
  }

  public void testGetParentDTO() throws Exception
  {
    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO rel = RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType());
    rel.apply();

    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);
    Method get = relationshipClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, rel.getId());

    Method getParent = relationshipClass.getMethod("getParent");
    BusinessDTO parent = (BusinessDTO) getParent.invoke(object);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    assertTrue(collectionClass.isInstance(parent));
    assertEquals(mom.getId(), parent.getId());
  }

  public void testGetParentsDTONotCached() throws Exception
  {
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_NOTHING.getId());
      updateRelationship.apply();
    }

    try
    {
      String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

      Class<?> referenceClass = LoaderDecorator.load(referenceDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
      List<BusinessDTO> list = (List<BusinessDTO>) referenceClass.getMethod("getAllRelParent").invoke(kid);

      assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      assertTrue(oracle.getType().equals(collection.definesType()));
      assertEquals(oracle.getId(), momId);
    }
    finally
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.clearItems(MdRelationshipInfo.CACHE_ALGORITHM);
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      updateRelationship.apply();
    }
  }

  public void testGetParentsDTOCached() throws Exception
  {
    String oldCacheId = mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM);
    if (!oldCacheId.equals(EntityCacheMaster.CACHE_EVERYTHING.getId()))
    {
      MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
      updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, EntityCacheMaster.CACHE_EVERYTHING.getId());
      updateRelationship.apply();
    }
    try
    {
      String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

      Class<?> referenceClass = LoaderDecorator.load(referenceDTO);

      String momId = BusinessDAO.newInstance(collection.definesType()).apply();
      String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
      RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

      Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
      List<BusinessDTO> list = (List<BusinessDTO>) referenceClass.getMethod("getAllRelParent").invoke(kid);

      assertEquals(1, list.size());
      BusinessDTO oracle = list.get(0);

      assertTrue(oracle.getType().equals(collection.definesType()));
      assertEquals(oracle.getId(), momId);
    }
    finally
    {
      if (!oldCacheId.equals(mdRelationship.getValue(MdRelationshipInfo.CACHE_ALGORITHM)))
      {
        MdRelationshipDAO updateRelationship = MdRelationshipDAO.get(mdRelationship.getId()).getBusinessDAO();
        updateRelationship.addItem(MdRelationshipInfo.CACHE_ALGORITHM, oldCacheId);
        updateRelationship.apply();
      }
    }
  }

  public void testGetParentRelationshipsDTO() throws Exception
  {
    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String relationshipDTO = mdRelationship.definesType() + ComponentDTOGenerator.DTO_SUFFIX;

    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    Class<?> relationshipClass = LoaderDecorator.load(relationshipDTO);

    String momId = BusinessDAO.newInstance(collection.definesType()).apply();
    String kidId = BusinessDAO.newInstance(reference.definesType()).apply();

    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(momId, kidId, mdRelationship.definesType()).apply();

    Object kid = referenceClass.getMethod("get", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, kidId);
    List<RelationshipDTO> list = (List<RelationshipDTO>) referenceClass.getMethod("getAllRelParentRelationships").invoke(kid);

    assertEquals(4, list.size());
    RelationshipDTO oracle = list.get(0);

    assertTrue(relationshipClass.isInstance(oracle));
    assertEquals(oracle.getParentId(), momId);
    assertEquals(oracle.getChildId(), kidId);
  }

  public void testGetReferenceDTO() throws Exception
  {
    BusinessDAO in = BusinessDAO.newInstance(reference.definesType());
    in.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aReference", in.getId());
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    BusinessDTO out = (BusinessDTO) collectionClass.getMethod("getAReference").invoke(object);

    assertEquals(in.getId(), out.getId());
  }

  public void testGetStructBooleanDTO() throws Exception
  {
    boolean in = true;
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(in));
    String id = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    boolean out = (Boolean) structClass.getMethod("getStructBoolean").invoke(struct);

    assertEquals(in, out);
  }

  public void testGetStructCharacterDTO() throws Exception
  {
    String in = "Smethie wuz Here!!!!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", in);
    String id = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);
    String out = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    assertEquals(in, out);
  }

  public void testGetStructEnumerationDTO() throws Exception
  {
    String input = "This is myself.";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aStruct", "structCharacter", input);
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    String id = businessDAO.apply();

    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    List<?> enums = (List<?>) structClass.getMethod("getStructEnumeration").invoke(struct);
    EnumerationDTOIF head = (EnumerationDTOIF) enums.get(0);
    String out = (String) head.name();
    String structChar = (String) structClass.getMethod("getStructCharacter").invoke(struct);

    assertEquals(input, structChar);
    assertEquals(1, enums.size());
    assertEquals(heartName, out);
  }

  public void testGetSymmetricDTO() throws Exception
  {
    String in = "You'll find that they're quite stationary";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aSym", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    String out = (String) collectionClass.getMethod("getASym").invoke(object);

    assertEquals("", out); // the return value should be empty for a DTO (for
    // security)
  }

  public void testGetTextDTO() throws Exception
  {
    String in = "Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aText", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    String out = (String) collectionClass.getMethod("getAText").invoke(object);

    assertEquals(in, out);
  }

  public void testGetClobDTO() throws Exception
  {
    String in = "CLOB: Blood alone moves the wheels of history! Have you ever asked yourselves in an hour of meditation, which everyone finds during the day, how long we have been striving for greatness? Not only the years we've been at war ... the war of work. But from the moment, as a child, and we realized that the world could be conquered. It has been a lifetime struggle, a never-ending fight, I say to you. And you will understand that it is a privilege to fight! We are warriors! Salesmen of Northeastern Pennsylvania, I ask you, once more rise and be worthy of this historical hour!";
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aClob", in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    String out = (String) collectionClass.getMethod("getAClob").invoke(object);

    assertEquals(in, out);
  }

  public void testGetTimeDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testGetTimeNullDTO() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", "");
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    Date out = (Date) collectionClass.getMethod("getATime").invoke(object);

    assertNull(out);
  }

  // TODO this test is obsolete since it checks the value of a hash via a DTO.
  // However,
  // DTO hash values are empty for security reasons.
  // public void testHashEqualsDTO() throws Exception
  // {
  // String in = "For breakfast, I had some Pringles, and some fudge-striped
  // cook-ays";
  // BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
  // businessDAO.setValue("aHash", in);
  // String id = businessDAO.apply();
  //
  // Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
  // Method getInstance = collectionClass.getMethod("getInstance", String.class,
  // ClientRequestIF.class,
  // String.class);
  // Object object = getInstance.invoke(null, sessionId, ClientRequest, id);
  //
  // String hash = businessDAO.getValue("aHash");
  // String out = (String) collectionClass.getMethod("getAHash").invoke(object);
  //
  // assertEquals(hash, out);
  // }

  public void testIsReadableDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    boolean out = (Boolean) collectionClass.getMethod("isATimeReadable").invoke(object);

    assertTrue(out);
  }

  public void testIsWritableDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aTime", sdf.format(in));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);
    boolean out = (Boolean) collectionClass.getMethod("isATimeWritable").invoke(object);

    assertTrue(out);
  }

  public void testRemoveEnumDTO() throws Exception
  {
    String in = heartsId;
    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> enumClass = LoaderDecorator.load(suitDTO);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.addItem("anEnum", in);
    String id = businessDAO.apply();

    Object hearts = enumClass.getDeclaredField(heartName).get(null);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    // Lock the object
    collectionClass.getMethod("lock").invoke(object);

    // Remove the enum item
    collectionClass.getMethod("removeAnEnum", enumClass).invoke(object, hearts);
    object = collectionClass.getMethod("apply").invoke(object);

    businessDAO = BusinessDAO.get(id).getBusinessDAO();
    Set<String> out = ( (AttributeEnumerationIF) businessDAO.getAttributeIF("anEnum") ).getCachedEnumItemIdSet();

    assertEquals(0, out.size());
  }

  public void testRemoveAllChildrenDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    BusinessDAO kid2 = BusinessDAO.newInstance(reference.definesType());
    kid2.apply();
    RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(mom.getId(), kid2.getId(), mdRelationship.definesType()).apply();

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object businessMom = get.invoke(null, clientRequestIF, mom.getId());

    collectionClass.getMethod("removeAllRelChild").invoke(businessMom);

    List<RelationshipDAOIF> list = BusinessDAO.get(mom.getId()).getChildren(mdRelationship.definesType());

    assertEquals(0, list.size());
  }

  public void testRemoveAllParentsDTO() throws Exception
  {
    Class<?> referenceClass = LoaderDecorator.load(reference.definesType() + TypeGeneratorInfo.DTO_SUFFIX);

    BusinessDAO mom = BusinessDAO.newInstance(collection.definesType());
    mom.apply();
    BusinessDAO dad = BusinessDAO.newInstance(collection.definesType());
    dad.apply();
    BusinessDAO kid = BusinessDAO.newInstance(reference.definesType());
    kid.apply();
    RelationshipDAO.newInstance(mom.getId(), kid.getId(), mdRelationship.definesType()).apply();
    RelationshipDAO.newInstance(dad.getId(), kid.getId(), mdRelationship.definesType()).apply();

    List<RelationshipDAOIF> list = BusinessDAO.get(kid.getId()).getParents(mdRelationship.definesType());
    assertEquals(2, list.size());

    Method get = referenceClass.getMethod("get", ClientRequestIF.class, String.class);
    Object businessKid = get.invoke(null, clientRequestIF, kid.getId());

    referenceClass.getMethod("removeAllRelParent").invoke(businessKid);

    list = BusinessDAO.get(kid.getId()).getParents(mdRelationship.definesType());
    assertEquals(0, list.size());
  }

  public void testSetBlobDTO() throws Exception
  {
    byte[] in = { 0, 1, 1, 2, 3, 5, 8 };

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setABlob", byte[].class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    byte[] out = businessDAO.getBlob("aBlob");

    assertEquals(in.length, out.length);

    for (int i = 0; i < in.length; i++)
      assertEquals(in[i], out[i]);
  }

  public void testSetBooleanDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    boolean in = true;
    collectionClass.getMethod("setABoolean", Boolean.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    boolean out = Boolean.parseBoolean(businessDAO.getValue("aBoolean"));

    assertEquals(in, out);
  }

  public void testSetCharacterDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    String in = "Mr. Sparkle";
    collectionClass.getMethod("setACharacter", String.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aCharacter");

    assertEquals(in, out);
  }

  public void testSetDateDTO() throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADate", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aDate"));

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testSetDateTimeDTO() throws Exception
  {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATETIME_FORMAT);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    Date in = new Date(System.currentTimeMillis());
    collectionClass.getMethod("setADateTime", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aDateTime"));

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testSetDecimalDTO() throws Exception
  {
    BigDecimal in = new BigDecimal(123456.789);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setADecimal", BigDecimal.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);

    BigDecimal out = new BigDecimal(businessDAO.getValue("aDecimal"));

    if (in.subtract(out).abs().doubleValue() > .0000001)
      fail("Stored and Retrieved Decimals have different values.");
  }

  public void testSetDoubleDTO() throws Exception
  {
    double in = 123456.789;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setADouble", Double.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    double out = Double.parseDouble(businessDAO.getValue("aDouble"));

    assertEquals(in, out);
  }

  public void testSetFloatDTO() throws Exception
  {
    float in = 123456.789F;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAFloat", Float.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    float out = Float.parseFloat(businessDAO.getValue("aFloat"));

    assertEquals(in, out);
  }

  public void testSetHashDTO() throws Exception
  {
    String in = "When you win, say nothing. When you lose, say less.";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAHash", String.class).invoke(object, in);
    createDTO.invoke(object);

    MessageDigest digest = MessageDigest.getInstance("MD5", new Sun());
    digest.update(in.getBytes());
    String hash = Base64.encodeToString(digest.digest(), false);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aHash");

    assertEquals(hash, out);
  }

  public void testSetIntegerDTO() throws Exception
  {
    int in = 1234;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAnInteger", Integer.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    int out = Integer.parseInt(businessDAO.getValue("anInteger"));

    assertEquals(in, out);
  }

  public void testSetLongDTO() throws Exception
  {
    long in = 123456789;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setALong", Long.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    long out = Long.parseLong(businessDAO.getValue("aLong"));

    assertEquals(in, out);
  }

  public void testSetLocalCharacterDTO() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    LocalStructDTO struct = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(in);

    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testSetDefaultLocalCharacterDTO() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    LocalStructDTO struct = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    struct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);

    collectionClass.getMethod("apply").invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testGetDefaultLocalCharacterDTO() throws Exception
  {
    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    StructDTO struct = (StructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = struct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testGetLocalCharacterDTO() throws Exception
  {
    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalCharacter", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    LocalStructDTO localStructDTO = (LocalStructDTO) collectionClass.getMethod("getALocalCharacter").invoke(object);
    String out = localStructDTO.getValue();

    assertEquals(in, out);
  }

  public void testSetDefaultLocalTextDTO() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    localStruct.setValue(MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testSetLocalTextDTO() throws Exception
  {
    String in = Long.toString(123456789L);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    localStruct.setValue(in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testGetLocalTextDTO() throws Exception
  {
    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    String out = localStruct.getValue();

    assertEquals(in, out);
  }

  public void testGetDefaultLocalTextDTO() throws Exception
  {
    String in = Long.toString(987654321L);

    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setStructValue("aLocalText", MdAttributeLocalInfo.DEFAULT_LOCALE, in);
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

    LocalStructDTO localStruct = (LocalStructDTO) collectionClass.getMethod("getALocalText").invoke(object);
    String out = localStruct.getValue(MdAttributeLocalInfo.DEFAULT_LOCALE);

    assertEquals(in, out);
  }

  public void testSetReferenceDTO() throws Exception
  {
    String referenceDTO = reference.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> referenceClass = LoaderDecorator.load(referenceDTO);
    BusinessDTO ref = (BusinessDTO) referenceClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createRef = referenceClass.getMethod("apply");

    createRef.invoke(ref);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAReference", referenceClass).invoke(object, ref);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    BusinessDAOIF out = ( (AttributeReferenceIF) businessDAO.getAttributeIF("aReference") ).dereference();

    assertEquals(ref.getId(), out.getId());
  }

  public void testSetStructBusiness()
  {
    String in = MdAttributeBooleanInfo.TRUE;
    Business object = BusinessFacade.newBusiness(collection.definesType());
    Struct struct = object.getGenericStruct("aStruct");

    struct.setValue("structBoolean", in);
    object.apply();

    BusinessDAOIF businessDAO = BusinessDAO.get(object.getId());
    String out = businessDAO.getStructValue("aStruct", "structBoolean");

    assertEquals(in, out);
  }

  public void testSetStructDTO()
  {
    String in = MdAttributeBooleanInfo.TRUE;

    BusinessDTO object = clientRequestIF.newBusiness(collection.definesType());

    ComponentDTOFacade.getAttributeStructDTO(object, "aStruct").setValue("structBoolean", in);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(object, "aStruct");
    assertEquals(in, struct.getValue("structBoolean"));

    clientRequestIF.createBusiness(object);

    BusinessDAOIF businessDAO = BusinessDAO.get(object.getId());
    String out = businessDAO.getStructValue("aStruct", "structBoolean");

    assertEquals(in, out);
  }

  /**
   * Test creating a StructDAO by itself through the DTO layer
   */
  public void testSetStandaloneDTO() throws Exception
  {
    boolean bIn = true;
    String cIn = "Im asl;dkgh39o";

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> enumClass = LoaderDecorator.load(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    // Get the standalone object by itself
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Object struct = structClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, bIn);
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, cIn);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, hearts);
    structClass.getMethod("addStructEnumeration", enumClass).invoke(struct, clubs);

    structClass.getMethod("apply").invoke(struct);

    String id = (String) structClass.getMethod("getId").invoke(struct);

    StructDAOIF structDAO = StructDAO.get(id);
    boolean bOut = Boolean.parseBoolean(structDAO.getValue("structBoolean"));
    String cOut = structDAO.getValue("structCharacter");
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttributeIF("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    assertEquals(bIn, bOut);
    assertEquals(cIn, cOut);
    assertEquals(2, enums.length);
    assertTrue(enumNames.contains(heartName));
    assertTrue(enumNames.contains(clubName));
  }

  /**
   * Test the generic getter and setters for a standalone StructDAO in the DTO
   * layer <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testGenericSetStandaloneDTO() throws Exception
  {
    boolean bIn = true;
    String cIn = "Im asl;dkgh39o";

    // Get the standalone object by itself
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    StructDTO struct = (StructDTO) structClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    // Modify the boolean attribute of the struct
    struct.setValue("structBoolean", Boolean.toString(bIn));
    struct.setValue("structCharacter", cIn);
    struct.addEnumItem("structEnumeration", heartName);

    structClass.getMethod("apply").invoke(struct);

    String id = (String) structClass.getMethod("getId").invoke(struct);

    StructDAOIF structDAO = StructDAO.get(id);
    boolean bOut = Boolean.parseBoolean(structDAO.getValue("structBoolean"));
    String cOut = structDAO.getValue("structCharacter");
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttributeIF("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    assertEquals(bIn, bOut);
    assertEquals(cIn, cOut);
    assertEquals(1, enums.length);
    assertTrue(enumNames.contains(heartName));
  }

  public void testSetStructBooleanDTO() throws Exception
  {
    boolean in = true;

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    // Get the struct of the collection class
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructBoolean", Boolean.class).invoke(struct, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    boolean out = Boolean.parseBoolean(businessDAO.getStructValue("aStruct", "structBoolean"));

    assertEquals(in, out);
  }

  public void testSetStructCharacterDTO() throws Exception
  {
    String in = "Dwight Schrute";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    // Get the struct of the collection class
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Object struct = collectionClass.getMethod("getAStruct").invoke(object);

    // Modify the boolean attribute of the struct
    structClass.getMethod("setStructCharacter", String.class).invoke(struct, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getStructValue("aStruct", "structCharacter");

    assertEquals(in, out);
  }

  public void testSetStructEnumerationDTO() throws Exception
  {
    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Class<?> enumClass = LoaderDecorator.load(suitDTO);

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

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    StructDAO structDAO = ( (AttributeStructIF) businessDAO.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    List<String> enumNames = new LinkedList<String>();

    for (BusinessDAOIF business : enums)
    {
      enumNames.add(business.getValue("enumName"));
    }

    assertEquals(2, enums.length);
    assertTrue(enumNames.contains(heartName));
    assertTrue(enumNames.contains(clubName));
  }

  public void testSetSymmetricDTO() throws Exception
  {
    String in = "My rims never spin - to the contrary";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setASym", String.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aSym");

    assertEquals(in, out);
  }

  public void testSetTextDTO() throws Exception
  {
    String in = "But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAText", String.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aText");

    assertEquals(in, out);
  }

  public void testSetClobDTO() throws Exception
  {
    String in = "CLOB: But, in a larger sense, we can not dedicate -- we can not consecrate -- we can not hallow -- this ground. The brave men, living and dead, who struggled here, have consecrated it, far above our poor power to add or detract. The world will little note, nor long remember what we say here, but it can never forget what they did here. It is for us the living, rather, to be dedicated here to the unfinished work which they who fought here have thus far so nobly advanced. It is rather for us to be here dedicated to the great task remaining before us -- that from these honored dead we take increased devotion to that cause for which they gave the last full measure of devotion -- that we here highly resolve that these dead shall not have died in vain -- that this nation, under God, shall have a new birth of freedom -- and that government of the people, by the people, for the people, shall not perish from the earth.";

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setAClob", String.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    String out = businessDAO.getValue("aClob");

    assertEquals(in, out);
  }

  public void testSetTimeDTO() throws Exception
  {
    Date in = new Date(System.currentTimeMillis());
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT);

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Object object = collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    collectionClass.getMethod("setATime", Date.class).invoke(object, in);
    createDTO.invoke(object);

    String id = (String) collectionClass.getMethod("getId").invoke(object);
    BusinessDAOIF businessDAO = BusinessDAO.get(id);
    Date out = sdf.parse(businessDAO.getValue("aTime"));

    assertEquals(sdf.format(in), sdf.format(out));
  }

  public void testGetState() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Business object = (Business) collectionClass.getConstructor().newInstance();
    object.apply();

    assertEquals("Preparing", object.getState());
  }

  public void testGetStateDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    createDTO.invoke(object);

    assertEquals("Preparing", object.getState());
  }

  public void testTransition() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Business object = (Business) collectionClass.getConstructor().newInstance();
    object.apply();

    collectionClass.getMethod("setup").invoke(object);
    collectionClass.getMethod("teardown").invoke(object);

    assertEquals("Finished", object.getState());
  }

  public void testNewTransition() throws Exception
  {
    TransitionDAO newTransition = mdState.addTransition("BackAgain", state3.getId(), state1.getId());
    newTransition.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionType);
    Business object = (Business) collectionClass.getConstructor().newInstance();
    object.apply();

    try
    {
      collectionClass.getMethod("setup").invoke(object);
      collectionClass.getMethod("teardown").invoke(object);
      collectionClass.getMethod("backAgain").invoke(object);

      assertEquals("Preparing", object.getState());
    }
    finally
    {
      newTransition.delete();
    }

    try
    {
      collectionClass = LoaderDecorator.load(collectionType);
      collectionClass.getMethod("backAgain").invoke(object);
      fail("A transition was deleted yet the source code for the owning class was not properly regenerated.");
    }
    catch (NoSuchMethodException ex)
    {
      // this is expected
    }
  }

  public void testTransitionDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");

    createDTO.invoke(object);

    // Lock the BusinessDTO
    collectionClass.getMethod("lock").invoke(object);

    collectionClass.getMethod("setup").invoke(object);

    // Lock the BusinessDTO
    collectionClass.getMethod("unlock").invoke(object);

    assertEquals("Collecting", object.getState());
  }

  public void testStaticTransitionDTO() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    Method createDTO = collectionClass.getMethod("apply");
    createDTO.invoke(object);

    String id = object.getId();

    // Lock the BusinessDTO
    collectionClass.getMethod("lock", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);

    BusinessDTO output = (BusinessDTO) collectionClass.getMethod("setup", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);

    // unLock the BusinessDTO
    collectionClass.getMethod("unlock", ClientRequestIF.class, String.class).invoke(null, clientRequestIF, id);

    assertEquals("Collecting", output.getState());
  }

  public void testAddRemoveEnumerationDTO() throws Exception
  {
    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String id = businessDAO.apply();

    BusinessDTO dto = (BusinessDTO) clientRequestIF.get(id);
    clientRequestIF.lock(dto);

    AttributeStructDTO struct = ComponentDTOFacade.getAttributeStructDTO(dto, "aStruct");
    struct.addEnumItem("structEnumeration", clubName);
    struct.removeEnumItem("structEnumeration", heartName);
    clientRequestIF.update(dto);

    // Check that the BusinessDAO was updated
    BusinessDAOIF obj = BusinessDAO.get(id);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();

    assertEquals(1, enums.length);
    assertEquals(clubsId, enums[0].getId());
  }

  public void testUpdateDTO() throws Exception
  {
    long longIn = 142;
    boolean booleanIn = true;

    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String id = businessDAO.apply();

    String suitDTO = suitEnum.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    String standaloneDTO = struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX;
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(standaloneDTO);
    Class<?> enumClass = LoaderDecorator.load(suitDTO);

    EnumerationDTOIF hearts = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, heartName);
    EnumerationDTOIF clubs = (EnumerationDTOIF) enumClass.getMethod("valueOf", String.class).invoke(null, clubName);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    Object object = get.invoke(null, clientRequestIF, id);

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
    BusinessDAOIF obj = BusinessDAO.get(id);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    String outBoolean = obj.getStructValue("aStruct", "structBoolean");
    String outLong = obj.getValue("aLong");

    assertEquals(1, enums.length);
    assertEquals(clubsId, enums[0].getId());
    assertEquals(Long.toString(longIn), outLong);
    assertEquals(Boolean.toString(booleanIn), outBoolean);

  }

  /**
   * Test updating an existing DTO using generic Getters and Setters to update
   * the values. <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testGenericUpdateDTO() throws Exception
  {
    long longIn = 142;
    boolean booleanIn = true;

    // Create the existing BusinessDAO
    BusinessDAO businessDAO = BusinessDAO.newInstance(collectionType);
    businessDAO.setValue("aLong", "142");
    businessDAO.addStructItem("aStruct", "structEnumeration", heartsId);
    businessDAO.setStructValue("aStruct", "structBoolean", Boolean.toString(false));
    String id = businessDAO.apply();

    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);

    Method get = collectionClass.getMethod("get", ClientRequestIF.class, String.class);
    BusinessDTO object = (BusinessDTO) get.invoke(null, clientRequestIF, id);

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
    BusinessDAOIF obj = BusinessDAO.get(id);
    StructDAO structDAO = ( (AttributeStructIF) obj.getAttributeIF("aStruct") ).getStructDAO();
    BusinessDAOIF[] enums = ( (AttributeEnumerationIF) structDAO.getAttribute("structEnumeration") ).dereference();
    String outBoolean = obj.getStructValue("aStruct", "structBoolean");
    String outLong = obj.getValue("aLong");

    assertEquals(1, enums.length);
    assertEquals(clubsId, enums[0].getId());
    assertEquals(Long.toString(longIn), outLong);
    assertEquals(Boolean.toString(booleanIn), outBoolean);
  }

  /**
   * Overwrites the source in Collection.java to add some references to
   * test.generated.Car, and then atempts to delete Car. This tests the
   * compiler, ensuring that it finds the dependency. <<<<<<< HEAD
   * 
   * =======
   * 
   * >>>>>>> 65655b74ec4d31c744f0f083e818471b8f2b25ed
   * 
   * @throws Exception
   */
  public void testDeletedClassStillReferenced() throws Exception
  {
    String originalCollectionStubSource = new String();
    originalCollectionStubSource = collection.getValue(MdClassInfo.STUB_SOURCE);

    // Build the new source for Collection.java
    String collectionStubSource = "package test.generated;\n" + "import test.generated.Car;\n" + "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS + "{\n" + "  private Car car;\n" + "  public Collection()\n" + "  {\n" + "    super();\n" + "    car = new Car();\n" + "  }\n" + "  public static Collection get(String id)\n" + "  {\n" + "    return (Collection) " + Business.class.getName() + ".get(id);\n" + "  }\n" + "}";

    // Write the new stub, and compile tom ake sure it's valid
    MdBusinessDAO updateCollection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
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

    // This will recompile everything. This should work now.
    this.cleanup_testDeletedClassStillReferenced(originalCollectionStubSource);
  }

  @Transaction
  private void cleanup_testDeletedClassStillReferenced(String stubSource)
  {
    MdBusinessDAO updateCollection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
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
  public void testDeletedAttributeStillReferenced() throws Exception
  {
    // Add a new 'Top Speed' attribute to car
    MdAttributeIntegerDAO topSpeed = MdAttributeIntegerDAO.newInstance();
    topSpeed.setValue(MdAttributeIntegerInfo.NAME, "topSpeed");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "120");
    topSpeed.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, car.getId());
    topSpeed.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "The Top Speed");
    topSpeed.apply();

    // Get the bytes that represent the new .class file
    File carClassFile = new File(LocalProperties.getServerGenBin(), "test/generated/Car.class");
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
    String collectionStubSource = "package test.generated;\n" + "import test.generated.Car;\n" + "public class Collection extends Collection" + TypeGeneratorInfo.BASE_SUFFIX + Reloadable.IMPLEMENTS + "\n" + "{\n" + "  private Car car;\n" + "  public Collection()\n" + "  {\n" + "    super();\n" + "    car = new Car();\n" + "    car.setTopSpeed(120);\n" + "  }\n" + "  public static Collection get(String id)\n" + "  {\n" + "    return (Collection) " + Business.class.getName() + ".get(id);\n" + "  }\n" + "}";

    // Write the new stub, and compile to make sure it's valid
    MdBusinessDAO updateCollection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
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

    car = (MdBusinessDAO) MdTypeDAO.getMdTypeDAO("test.generated.Car");
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
    MdBusinessDAO updateCollection = MdBusinessDAO.get(collection.getId()).getBusinessDAO();
    updateCollection.setValue(MdClassInfo.STUB_SOURCE, stubSource);
    updateCollection.apply();

    TestFixtureFactory.delete(topSpeed);
  }

  public void testTypeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

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

  public void testBlobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getABlobMd").invoke(object);

    checkAttributeMd(collectionBlob, mdDTO);
  }

  public void testDateTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getADateTimeMd").invoke(object);

    checkAttributeMd(collectionDateTime, mdDTO);
  }

  public void testDateMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getADateMd").invoke(object);

    checkAttributeMd(collectionDate, mdDTO);
  }

  public void testTimeMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATimeMd").invoke(object);

    checkAttributeMd(collectionTime, mdDTO);
  }

  public void testBooleanMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getABooleanMd").invoke(object);

    checkAttributeMd(collectionBoolean, mdDTO);
  }

  public void testReferenceMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getAReferenceMd").invoke(object);

    checkAttributeMd(collectionReference, mdDTO);

    assertEquals(collectionReference.isSystem(), mdDTO.isSystem());
  }

  public void testTermMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATermMd").invoke(object);

    checkAttributeMd(collectionTerm, mdDTO);

    assertEquals(collectionTerm.isSystem(), mdDTO.isSystem());
    assertTrue(mdDTO instanceof AttributeTermMdDTO);
  }

  public void testIntegerMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod("getAnIntegerMd").invoke(object);

    checkAttributeMd(collectionInteger, mdDTO);
    assertEquals(Boolean.parseBoolean(collectionInteger.isPositiveRejected()), mdDTO.rejectPositive());
  }

  public void testLongMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeNumberMdDTO mdDTO = (AttributeNumberMdDTO) collectionClass.getMethod("getALongMd").invoke(object);

    checkAttributeMd(collectionLong, mdDTO);
  }

  public void testCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) collectionClass.getMethod("getACharacterMd").invoke(object);

    checkAttributeMd(collectionCharacter, mdDTO);
    assertEquals(Integer.parseInt(collectionCharacter.getSize()), mdDTO.getSize());
  }

  public void testStructMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeStructMdDTO mdDTO = (AttributeStructMdDTO) collectionClass.getMethod("getAStructMd").invoke(object);

    checkAttributeMd(collectionStruct, mdDTO);
    assertEquals(collectionStruct.getMdStructDAOIF().definesType(), mdDTO.getDefiningMdStruct());
  }

  public void testDecimalMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getADecimalMd").invoke(object);

    checkAttributeMd(collectionDecimal, mdDTO);
  }

  public void testDoubleMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getADoubleMd").invoke(object);

    checkAttributeMd(collectionDouble, mdDTO);
  }

  public void testFloatMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeDecMdDTO mdDTO = (AttributeDecMdDTO) collectionClass.getMethod("getAFloatMd").invoke(object);

    checkAttributeMd(collectionFloat, mdDTO);
  }

  public void testEnumerationMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEnumerationMdDTO mdDTO = (AttributeEnumerationMdDTO) collectionClass.getMethod("getAnEnumMd").invoke(object);

    checkAttributeMd(collectionEnumeration, mdDTO);
    assertEquals(collectionEnumeration.selectMultiple(), mdDTO.selectMultiple());
  }

  public void testTextMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getATextMd").invoke(object);

    checkAttributeMd(collectionText, mdDTO);
  }

  public void testClobMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeMdDTO mdDTO = (AttributeMdDTO) collectionClass.getMethod("getAClobMd").invoke(object);

    checkAttributeMd(collectionClob, mdDTO);
  }

  public void testHashMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod("getAHashMd").invoke(object);

    checkAttributeMd(collectionHash, mdDTO);
    assertEquals(collectionHash.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  public void testSymmetricMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);

    AttributeEncryptionMdDTO mdDTO = (AttributeEncryptionMdDTO) collectionClass.getMethod("getASymMd").invoke(object);

    checkAttributeMd(collectionSymmetric, mdDTO);
    assertEquals(collectionSymmetric.getEncryptionMethod(), mdDTO.getEncryptionMethod());
  }

  public void testStructCharacterMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
    Class<?> structClass = LoaderDecorator.load(struct.definesType() + ComponentDTOGenerator.DTO_SUFFIX);
    BusinessDTO object = (BusinessDTO) collectionClass.getConstructor(ClientRequestIF.class).newInstance(clientRequestIF);
    StructDTO structDTO = (StructDTO) collectionClass.getMethod("getAStruct").invoke(object);

    AttributeCharacterMdDTO mdDTO = (AttributeCharacterMdDTO) structClass.getMethod("getStructCharacterMd").invoke(structDTO);

    checkAttributeMd(structCharacter, mdDTO);
    assertEquals(Integer.parseInt(structCharacter.getSize()), mdDTO.getSize());
  }

  public void testFileMetadata() throws Exception
  {
    Class<?> collectionClass = LoaderDecorator.load(collectionDTO);
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
