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
package com.runwaysdk.dataaccess;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ProblemException;
import com.runwaysdk.ProblemIF;
import com.runwaysdk.constants.EntityTypes;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBlobInfo;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeClobInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdTreeInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeLengthByteException;
import com.runwaysdk.dataaccess.attributes.AttributeLengthCharacterException;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.ImmutableAttributeProblem;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.SystemAttributeProblem;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeBlob;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeBoolean;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeCharacter;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeClob;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeDate;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeDateTime;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeDecimal;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeDouble;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeEnumeration;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeFloat;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeInteger;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeLong;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeReference;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeText;
import com.runwaysdk.dataaccess.attributes.tranzient.AttributeTime;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.database.general.AbstractDatabase;
import com.runwaysdk.dataaccess.database.general.PostgreSQL;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.io.TestFixtureFactory.TestFixConst;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinedInSubclassException;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeDefinitionException;
import com.runwaysdk.dataaccess.metadata.DuplicateAttributeInInheritedHierarchyException;
import com.runwaysdk.dataaccess.metadata.MdAttributeBlobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeBooleanDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeClobDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDateTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDecimalDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeDoubleDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeFloatDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdClassDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTreeDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

public class TransientAttributeTest
{
  private static final TypeInfo                       stateClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State");

  private static final TypeInfo                       stateEnum  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State_Enum");

  /**
   * Contains the MdAttribute for each attribute defined in classSetUp(). Is
   * referenced by the classTearDown() method, where all attribtues are deleted
   */
  private static LinkedList<MdAttributeConcreteDAOIF> definitions;

  /**
   *
   */
  protected static MdViewDAO                          mdView;

  /**
   * MdView that defines all of the attributes.
   */
  protected static MdViewDAO                          attributeMdView;

  /**
   * MdBusiness class used for a reference attribute.
   */
  protected static MdBusinessDAO                      referenceMdBusiness;

  private static MdTreeDAO                            someTree;

  private TransientDAO                                transientDAO;

  /**
   * MdBusinessIF instance that defines STATE.
   */
  private static MdBusinessDAOIF                      stateEnumerationMdBusiness;

  /**
   * MdEnumerationIF instance that defines STATE.
   */
  private static MdEnumerationDAOIF                   stateMdEnumeration;

  /**
   * Attribute name of the multiple select enumeration
   */
  private static final String                         MULTIPLE   = "enumStateMultiple";

  /**
   * Metadata that defines of the multiple select enumeration.
   */
  private static MdAttributeEnumerationDAO            mdAttrEnumMultiple;

  /**
   * Metadata that defines of the single select enumeration
   */
  private static MdAttributeEnumerationDAO            mdAttrEnumSingle;

  /**
   * Attribute name of the single select enumeration
   */
  private static final String                         SINGLE     = "enumStateSingle";

  /**
   * The ID of the California option of the STATE enumeration.
   */
  private static String                               californiaItemId;

  /**
   * The ID of the Colorado option of the STATE enumeration.
   */
  private static String                               coloradoItemId;

  /**
   * The ID of the Connecticut option of the STATE enumeration.
   */
  private static String                               connecticutItemId;

  @Request
  @BeforeClass
  public static void classSetUp()
  {
    definitions = new LinkedList<MdAttributeConcreteDAOIF>();

    mdView = SessionMasterTestSetup.mdView;
    attributeMdView = SessionMasterTestSetup.attributeMdView;
    referenceMdBusiness = SessionMasterTestSetup.referenceMdBusiness;

    TypeInfo someTreeInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "SomeTree");
    someTree = MdTreeDAO.newInstance();
    someTree.setValue(MdTreeInfo.NAME, someTreeInfo.getTypeName());
    someTree.setValue(MdTreeInfo.COMPOSITION, MdAttributeBooleanInfo.TRUE);
    someTree.setValue(MdTreeInfo.PACKAGE, someTreeInfo.getPackageName());
    someTree.setStructValue(MdTreeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "some tree Relationship");
    someTree.setValue(MdTreeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    someTree.setValue(MdTreeInfo.PARENT_MD_BUSINESS, referenceMdBusiness.getId());
    someTree.setValue(MdTreeInfo.PARENT_CARDINALITY, "1");
    someTree.setStructValue(MdTreeInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 1");
    someTree.setValue(MdTreeInfo.CHILD_MD_BUSINESS, referenceMdBusiness.getId());
    someTree.setValue(MdTreeInfo.CHILD_CARDINALITY, "*");
    someTree.setStructValue(MdTreeInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "blah 2");
    someTree.setValue(MdTreeInfo.PARENT_METHOD, "someParentAccessor");
    someTree.setValue(MdTreeInfo.CHILD_METHOD, "someChildAccessor");
    someTree.setValue(MdTreeInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    someTree.setValue(MdTreeInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    someTree.apply();

    MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());

    // home phone
    MdAttributeStructDAO mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "homePhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "homePhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    // work phone
    mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "workPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "workPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    // cell phone
    mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "cellPhone");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "cellPhone attr");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, phoneNumber.getId());
    mdAttrStruct.apply();
    definitions.add(mdAttrStruct);

    MdAttributeTextDAO mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, "testText");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Text field");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFAULT_VALUE, "");
    mdAttributeText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeText.setValue(MdAttributeTextInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeText.apply();
    definitions.add(mdAttributeText);

    MdAttributeClobDAO mdAttributeClob = MdAttributeClobDAO.newInstance();
    mdAttributeClob.setValue(MdAttributeClobInfo.NAME, "testClob");
    mdAttributeClob.setStructValue(MdAttributeClobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Clob field");
    mdAttributeClob.setValue(MdAttributeClobInfo.DEFAULT_VALUE, "");
    mdAttributeClob.setValue(MdAttributeClobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClob.setValue(MdAttributeClobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeClob.setValue(MdAttributeClobInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeClob.apply();
    definitions.add(mdAttributeClob);

    // Add an attribute to the reference type
    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "refChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A string");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "I wish I was a reference field!");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, referenceMdBusiness.getId());
    mdAttributeCharacter.apply();

    // Add attributes to the test type
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, TestFixConst.ATTRIBUTE_CHARACTER);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required Character Length 16");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Yo diggity");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    // Some immutable attribute
    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testImmutableChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required Character Length 16");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Yo diggity");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SYSTEM, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    MdAttributeBlobDAO mdAttributeBlob = MdAttributeBlobDAO.newInstance();
    mdAttributeBlob.setValue(MdAttributeBlobInfo.NAME, "testBlob");
    mdAttributeBlob.setStructValue(MdAttributeBlobInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some blob");
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBlob.addItem(MdAttributeBlobInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getId());
    mdAttributeBlob.setValue(MdAttributeBlobInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBlob.setValue(MdAttributeBlobInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeBlob.apply();
    definitions.add(mdAttributeBlob);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testCharacterChangeSize");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Required Character Length 16, but change size to 32");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Yo diggity dog");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "testInteger");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeInteger.apply();
    definitions.add(mdAttributeInteger);

    MdAttributeLongDAO mdAttributeLong = MdAttributeLongDAO.newInstance();
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME, "testLong");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Long");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    mdAttributeLong.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeLong.apply();
    definitions.add(mdAttributeLong);

    MdAttributeFloatDAO mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "testFloat");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeFloat.apply();
    definitions.add(mdAttributeFloat);

    mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, "floatBounds");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, "2");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_POSITIVE, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_ZERO, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REJECT_NEGATIVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeFloat.apply();
    definitions.add(mdAttributeFloat);

    MdAttributeDecimalDAO mdAttributeDecimal = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.NAME, "testDecimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Decimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE, "");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "13");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "3");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeDecimal.apply();
    definitions.add(mdAttributeDecimal);

    MdAttributeDoubleDAO mdAttributeDouble = MdAttributeDoubleDAO.newInstance();
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.NAME, "testDouble");
    mdAttributeDouble.setStructValue(MdAttributeDoubleInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE, "");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "16");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "4");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeDouble.apply();
    definitions.add(mdAttributeDouble);

    MdAttributeTimeDAO mdAttributeTime = MdAttributeTimeDAO.newInstance();
    mdAttributeTime.setValue(MdAttributeTimeInfo.NAME, "testTime");
    mdAttributeTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Time (HH:MM:SS)");
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    mdAttributeTime.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeTime.apply();
    definitions.add(mdAttributeTime);

    MdAttributeDateDAO mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, "testDate");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Date (YYYY-MM-DD)");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDate.setValue(MdAttributeDateInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeDate.apply();
    definitions.add(mdAttributeDate);

    MdAttributeDateTimeDAO mdAttributeDateTime = MdAttributeDateTimeDAO.newInstance();
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.NAME, "testDateTime");
    mdAttributeDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "DateTime (YYYY-MM-DD HH:MM:SS)");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFAULT_VALUE, "");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeDateTime.apply();
    definitions.add(mdAttributeDateTime);

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceMdBusiness.getId());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeReference.apply();
    definitions.add(mdAttributeReference);

    MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Our first Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeBoolean.apply();
    definitions.add(mdAttributeBoolean);

    mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testImmutable");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An Immutable String");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "You can't change this");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.IMMUTABLE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttributeCharacter.apply();
    definitions.add(mdAttributeCharacter);

    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    // New Type (STATE) extends Enumeration_Attribute
    MdBusinessDAO stateEnumMdBusiness = MdBusinessDAO.newInstance();
    stateEnumMdBusiness.setValue(MdBusinessInfo.NAME, stateClass.getTypeName());
    stateEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE, stateClass.getPackageName());
    stateEnumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State");
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "States of the Union");
    stateEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getId());
    stateEnumMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.apply();
    stateEnumerationMdBusiness = stateEnumMdBusiness;

    // Instantiate an md_enumeration to define State
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, stateEnum.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, stateEnum.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States in the United States");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumerationMdBusiness.getId());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();
    stateMdEnumeration = mdEnumeration;

    // Define attributes on the enumeration
    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "stateCode");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "2");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Postal Code");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getId());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getId());
    mdAttrChar.apply();

    MdAttributeIntegerDAO enumMdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    enumMdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "enumInt");
    enumMdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration Integer");
    enumMdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    enumMdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    enumMdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    enumMdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getId());
    enumMdAttributeInteger.apply();

    // Define the options for the enumeration
    BusinessDAO businessDAO = BusinessDAO.newInstance(stateClass.getType());
    businessDAO.setValue("enumInt", "1");
    businessDAO.setValue("stateCode", "CA");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CA");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    californiaItemId = businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(stateClass.getType());
    businessDAO.setValue("enumInt", "2");
    businessDAO.setValue("stateCode", "CO");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CO");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    coloradoItemId = businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(stateClass.getType());
    businessDAO.setValue("enumInt", "3");
    businessDAO.setValue("stateCode", "CT");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "CT");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Connecticut");
    connecticutItemId = businessDAO.apply();

    mdAttrEnumMultiple = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.NAME, MULTIPLE);
    mdAttrEnumMultiple.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Multiple select state attribute");
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration.getId());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumMultiple.apply();
    // definitions.add(mdAttrEnumMultiple);

    // Add the enumeration as a single-select Attribute on the TEST type
    mdAttrEnumSingle = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME, SINGLE);
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Single select state attribute");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, attributeMdView.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration.getId());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnumSingle.apply();
    // definitions.add(mdAttrEnumMultiple);

  }

  @Request
  @AfterClass
  public static void classTearDown()
  {
    // Delete all of the attributes created by this class
    for (MdAttributeConcreteDAOIF mdAttributeIF : definitions)
    {
      TestFixtureFactory.delete(mdAttributeIF);
    }

    TestFixtureFactory.delete(stateEnumerationMdBusiness);
  }

  /**
   * Set the transientDAO to a new Instance of the
   * SessionMasterTestSetup.CHILD_SESSION_CLASS class.
   */
  @Request
  @Before
  protected void setUp() throws Exception
  {
    transientDAO = TransientDAO.newInstance(SessionMasterTestSetup.CHILD_SESSION_CLASS.getType());
  }

  /**
   * Set the transientDAO to a new Instance of the
   * SessionMasterTestSetup.CHILD_SESSION_CLASS class.
   */
  @Request
  @After
  protected void tearDown() throws Exception
  {
    transientDAO = null;
  }

  @Request
  @Test
  public void testDuplicateAttributeDefinition()
  {
    try
    {
      MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
      mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, attributeMdView.getId());
      mdAttributeCharacter.apply();

      Assert.fail("Able to add an attribute to a type that already has an attribute defined with that same name");
    }
    catch (DuplicateAttributeDefinitionException e)
    {
      // this is expected
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
  }

  @Request
  @Test
  public void testDuplicateAttributeInInheritedHierarchy()
  {
    MdClassDAOIF parentMdClassIF = MdClassDAO.getMdClassDAO(SessionMasterTestSetup.PARENT_SESSION_CLASS.getType());
    MdClassDAOIF childMdClassIF = MdClassDAO.getMdClassDAO(SessionMasterTestSetup.CHILD_SESSION_CLASS.getType());

    if (attributeMdView.definesType().equals(SessionMasterTestSetup.PARENT_SESSION_CLASS.getType()))
    {
      try
      {
        MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
        mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, childMdClassIF.getId());
        mdAttributeCharacter.apply();

        Assert.fail("Able to add an attribute to a type where the parent type already has an attribute defined with that same name");
      }
      catch (DuplicateAttributeInInheritedHierarchyException e)
      {
        // this is expected
      }
      catch (Throwable e)
      {
        Assert.fail(e.getMessage());
      }
    }
    else if (attributeMdView.definesType().equals(SessionMasterTestSetup.CHILD_SESSION_CLASS.getType()))
    {
      try
      {
        MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "testChar64");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "64");
        mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Character Length 64");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
        mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, parentMdClassIF.getId());
        mdAttributeCharacter.apply();

        Assert.fail("Able to add an attribute to a type where a child type already has an attribute defined with that same name");
      }
      catch (DuplicateAttributeDefinedInSubclassException e)
      {
        // this is expected
      }
      catch (Throwable e)
      {
        Assert.fail(e.getMessage());
      }
    }
    else
    {
      Assert.fail("Test class is of the wrong type.");
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined but not
   * required
   */
  @Request
  @Test
  public void testNotRequired()
  {
    try
    {
      transientDAO.setValue("testChar64", "A Value");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is not defined but not
   * required
   */
  @Request
  @Test
  public void testNotRequiredBlankValue()
  {
    try
    {
      transientDAO.setValue("testChar64", "");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  @Request
  @Test
  public void testRequired()
  {
    try
    {
      transientDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "A Value");
      transientDAO.apply();
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is defined and required
   */
  @Request
  @Test
  public void testRequiredValidateMethod()
  {
    try
    {
      transientDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "A Value");
      transientDAO.validateAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  @Request
  @Test
  public void testRequiredWithBlankValue()
  {
    try
    {
      transientDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "");
      transientDAO.apply();
      Assert.fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        Assert.fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
  }

  /**
   * Tests Attribute.validateRequired() where the value is required but not
   * defined. This is expected to fail, so the Exception is caught and compared
   * to its expected value.
   */
  @Request
  @Test
  public void testRequiredWithBlankValueValidateMethod()
  {
    try
    {
      requiredWithBlankValueValidateMethod();
      Assert.fail("Attribute.validateRequired() accepted a blank value on a required field.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        Assert.fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
  }

  @Transaction
  public void requiredWithBlankValueValidateMethod()
  {
    transientDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "");
    transientDAO.validateAttribute(TestFixConst.ATTRIBUTE_CHARACTER);
  }

  /**
   * Tests Attribute.validateSystem() by trying to modify a system-only
   * attribute.
   */
  @Request
  @Test
  public void testImmutable()
  {
    try
    {
      transientDAO.apply();
      immutable();
      Assert.fail("Attribute.validateMutable() allowed a user to modify an immutable attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof ImmutableAttributeProblem)
      {
        // This is expected
      }
      else
      {
        Assert.fail(e.getMessage());
      }
    }
  }

  @Transaction
  private void immutable()
  {
    transientDAO.setValue("testImmutable", "Totally mutable");
  }

  /**
   * Tests Attribute.validateMutable() by trying to modify an immutable
   * attribute.
   */
  @Request
  @Test
  public void testSystem()
  {
    try
    {
      system();
      Assert.fail("Attribute.validateSystem() allowed a user to modify a SYSTEM attribute.");
    }
    catch (ProblemException e)
    {
      List<ProblemIF> problemList = ( (ProblemException) e ).getProblems();

      if (problemList.size() == 1 && problemList.get(0) instanceof SystemAttributeProblem)
      {
        // This is expected
      }
      else
      {
        Assert.fail(e.getMessage());
      }
    }
  }

  @Transaction
  private void system()
  {
    transientDAO.setValue("testImmutableChar", "Your momma!");
  }

  /**
   * Tests a normal character and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testCharacter()
  {
    try
    {
      // set value and store it
      String key = TestFixConst.ATTRIBUTE_CHARACTER;
      String value = "New Value";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeCharacter retrieved = (AttributeCharacter) transientDAO.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a String that is too long
   */
  @Request
  @Test
  public void testCharWithLongString()
  {
    try
    {
      // TEST_CHARACTER has a limit of 10 characters
      transientDAO.setValue(TestFixConst.ATTRIBUTE_CHARACTER, "This string is too long.");
      Assert.fail("Accepted a String that was too large.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // So we expect to catch an AttributeLengthCharacterException
    }
  }

  /**
   * Changes the DB size of an attribute.
   */
  @Request
  @Test
  public void testCharChangeSize()
  {
    MdAttributeCharacterDAO mdAttributeCharacter = (MdAttributeCharacterDAO) ( transientDAO.getAttributeIF("testCharacterChangeSize").getMdAttribute() ).getBusinessDAO();
    try
    {
      // TEST_CHARACTER_CHANGE_SIZE should be able to take an attribute of
      // up to
      // 32 characters.
      mdAttributeCharacter.getAttribute(MdAttributeCharacterInfo.SIZE).setValue("32");
      mdAttributeCharacter.apply();
      // Test with a string of 32 characters
      transientDAO.setValue("testCharacterChangeSize", "01234567890123456789012345678901");
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
      // Assert.fail("Attribute testCharacterChangeSize originally was defined
      // to a size of 16. An attempt to redefine it to 32 failed.");
      return;
    }

    try
    {
      // Test with a string of 33 characters
      transientDAO.setValue("testCharacterChangeSize", "012345678901234567890123456789012");
      Assert.fail("Attribute testCharacterChangeSize originally was redefined to a size of 32, but it accepted a string of size 33.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal text and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testTextStoreRetrieve()
  {
    try
    {
      // set value and store it
      String key = "testText";
      String value = "This is the testing text, hopefully it works!!!";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeText retrieved = (AttributeText) transientDAO.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a CLOB and then stores->retrieves->compares the values to make sure
   * retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testClobStoreRetrieve()
  {
    try
    {
      // set value and store it
      String key = "testClob";
      String value = "This is the testing clob, hopefully it works!!!";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeClob retrieved = (AttributeClob) transientDAO.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests some text that is too long
   */
  @Request
  @Test
  public void testTooLongText()
  {
    if (! ( Database.instance() instanceof PostgreSQL ))
    {
      try
      {
        StringBuffer buffer = new StringBuffer();
        String stringFrag = "0123456789";
        int maxLoopIterations = ( AbstractDatabase.MAX_LENGTH / 10 ) + 1;

        for (int i = 0; i < maxLoopIterations; i++)
        {
          buffer.append(stringFrag);
        }

        // TEST_CHARACTER has a limit MdAttributeText.getMaxLength()
        transientDAO.setValue("testText", buffer.toString());

        Assert.fail("Accepted a String that was too large.");
      }
      catch (AttributeLengthCharacterException e)
      {
        // This is expected
      }
    }
  }

  /**
   * Tests a blank (which is acceptable) String
   */
  @Request
  @Test
  public void testCharWithEmptyString()
  {
    try
    {
      transientDAO.setValue("testChar64", "");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a normal boolean and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testBoolean()
  {
    try
    {
      // set value and store it
      String key = TestFixConst.ATTRIBUTE_BOOLEAN;
      String value = MdAttributeBooleanInfo.TRUE;
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeBoolean retrieved = (AttributeBoolean) transientDAO.getAttributeIF(key);

      if (Boolean.parseBoolean(retrieved.getValue()) != Boolean.parseBoolean( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests AttributeBoolean.validate() with an invalid value
   */
  @Request
  @Test
  public void testBooleanInvalid()
  {
    try
    {
      transientDAO.setValue(TestFixConst.ATTRIBUTE_BOOLEAN, "rawr");
      Assert.fail("AttributeBoolean accepted an invalid value.");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal integer and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testInteger()
  {
    try
    {
      // set value and store it
      String key = "testInteger";
      String value = "12";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeInteger retrieved = (AttributeInteger) transientDAO.getAttributeIF(key);

      if (Integer.parseInt(retrieved.getValue()) != Integer.parseInt( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an integer with an alphabetic String
   */
  @Request
  @Test
  public void testIntegerWithAlphas()
  {
    try
    {
      // Try to set an alphabetic string to an int
      transientDAO.setValue("testInteger", "This isn't a number");
      Assert.fail("An integer accepted alpha input");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests an unacceptably large number
   */
  @Request
  @Test
  public void testIntegerWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in an int
      transientDAO.setValue("testInteger", "2147483648");
      Assert.fail("Accepted a number too large to fit in an integer");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal long and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testLong()
  {
    try
    {
      // set value and store it
      String key = "testLong";
      String value = "20";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeLong retrieved = (AttributeLong) transientDAO.getAttributeIF(key);

      if (Long.parseLong(retrieved.getValue()) != Long.parseLong( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an alphabetic String
   */
  @Request
  @Test
  public void testLongWithAlphas()
  {
    try
    {
      // Try to set an alphabetic string to a long
      transientDAO.setValue("testLong", "This isn't a number");
      Assert.fail("Accepted alpha input into a long");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests an unacceptably large number
   */
  @Request
  @Test
  public void testLongWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in a long
      transientDAO.setValue("testLong", "1234567890123456789012345678901234567890");
      Assert.fail("Accepted a number too large to fit in a long");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal float and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testFloat()
  {
    try
    {
      // set value and store it
      String key = "testFloat";
      String value = "4.2";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeFloat retrieved = (AttributeFloat) transientDAO.getAttributeIF(key);

      if (Float.parseFloat(retrieved.getValue()) != Float.parseFloat( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a float
   */
  @Request
  @Test
  public void testFloatWithInt()
  {
    try
    {
      transientDAO.setValue("testFloat", "42");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number
   */
  @Request
  @Test
  public void testFloatWithExponent()
  {
    try
    {
      transientDAO.setValue("testFloat", "4.2E-5");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a large (but acceptable) number
   */
  @Request
  @Test
  public void testFloatWithLong()
  {
    try
    {
      transientDAO.setValue("testFloat", "123456789012345");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an unacceptably large float
   */
  @Request
  @Test
  public void testFloatWithLargeNumber()
  {
    try
    {
      // This number is far too large to fit in a float
      transientDAO.setValue("testFloat", "12345678901234567890123456789012345678901234567890");
      Assert.fail("Accepted a number too large to fit in a float");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal double and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testDouble()
  {
    try
    {
      // set value and store it
      String key = "testDouble";
      String value = "3.14";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeDouble retrieved = (AttributeDouble) transientDAO.getAttributeIF(key);

      if (Double.parseDouble(retrieved.getValue()) != Double.parseDouble( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a double
   */
  @Request
  @Test
  public void testDoubleWithInt()
  {
    try
    {
      transientDAO.setValue("testDouble", "-5");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number into a double
   */
  @Request
  @Test
  public void testDoubleWithExponent()
  {
    try
    {
      transientDAO.setValue("testDouble", "12.3E45");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an unacceptably large exponent double
   */
  @Request
  @Test
  public void testDoubleWithLargeExponent()
  {
    try
    {
      transientDAO.setValue("testDouble", "123.456E7890");
      Assert.fail("Accepted a number too large to fit in a double");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal decimal and then stores->retrieves->compares the values to
   * make sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testDecimal()
  {
    try
    {
      // set value and store it
      String key = "testDecimal";
      String value = "1.4";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeDecimal retrieved = (AttributeDecimal) transientDAO.getAttributeIF(key);

      if (Double.parseDouble(retrieved.getValue()) != Double.parseDouble( ( value )))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests an int cast to a decimal
   */
  @Request
  @Test
  public void testDecimalWithInt()
  {
    try
    {
      transientDAO.setValue("testDecimal", "-27");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a large (exponent) number into a decimal
   */
  @Request
  @Test
  public void testDecimalWithExponent()
  {
    try
    {
      transientDAO.setValue("testDecimal", "12.0E50");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a normal time and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testTime()
  {
    try
    {
      // set value and store it
      String key = "testTime";
      String value = "08:59:14";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeTime retrieved = (AttributeTime) transientDAO.getAttributeIF(key);
      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value.");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests with a completely invalid time
   */
  @Request
  @Test
  public void testTimeInvalid()
  {
    try
    {
      transientDAO.setValue("testTime", "This isn't a time");
      Assert.fail("Attribute accepted an invalid time");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a time that is too early
   */
  @Request
  @Test
  public void testTimeLowerBound()
  {
    try
    {
      transientDAO.setValue("testTime", "-00:00:01");
      Assert.fail("Accepted too early of a time");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a time that is too late
   */
  @Request
  @Test
  public void testTimeUpperBound()
  {
    try
    {
      transientDAO.setValue("testTime", "23:59:60");
      Assert.fail("Accepted too late of a time");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a normal date and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testDate()
  {
    try
    {
      // set value and store it
      String key = "testDate";
      String value = "2005-06-15";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // retrieve the applied object and test
      AttributeDate retrieved = (AttributeDate) transientDAO.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a completely invalid date
   */
  @Request
  @Test
  public void testDateInvalid()
  {
    try
    {
      // This is a datetime, not a date
      transientDAO.setValue("testDate", "2005-12-31 2:15:32");
      Assert.fail("Attribute accepted an invalid Date");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a date that is too early
   */
  @Request
  @Test
  public void testDateLowerBound()
  {
    try
    {
      transientDAO.setValue("testDate", "1752-01-01");
      Assert.fail("Accepted too early of a date");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a date that is too late
   */
  @Request
  @Test
  public void testDateUpperBound()
  {
    try
    {
      transientDAO.setValue("testDate", "2005-01-32");
      Assert.fail("Accepted too late of a date");
    }
    catch (DataAccessException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a normal date and then stores->retrieves->compares the values to make
   * sure retrieved value is the same as the input.
   */
  @Request
  @Test
  public void testDateTime()
  {
    try
    {
      // set value and store it
      String key = "testDateTime";
      String value = "2005-06-15 08:59:14";
      transientDAO.setValue(key, value);
      transientDAO.apply();

      // retrieve the applied object and test
      AttributeDateTime retrieved = (AttributeDateTime) transientDAO.getAttributeIF(key);

      if (!retrieved.getValue().equals(value))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests with a completely invalid date
   */
  @Request
  @Test
  public void testDateTimeInvalid()
  {
    try
    {
      // This is a date, not a time
      transientDAO.setValue("testDateTime", "2005-06-15");
      Assert.fail("Accepted an invalid String");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests a datetime that is too early
   */
  @Request
  @Test
  public void testDateTimeLowerBound()
  {
    try
    {
      transientDAO.setValue("testDateTime", "1752-01-01 00:00:00");
      Assert.fail("Accepted too early of a datetime");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a datetime that is too late
   */
  @Request
  @Test
  public void testDateTimeUpperBound()
  {
    try
    {
      transientDAO.setValue("testDateTime", "2005-13-01 23:59:59");
      Assert.fail("Accepted too late of a datetime");
    }
    catch (AttributeValueException e)
    {
      // We expect to catch an AttributeValueException
    }
  }

  /**
   * Tests setting a reference.
   */
  @Request
  @Test
  public void testReference()
  {
    BusinessDAO reference = BusinessDAO.newInstance(SessionMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();

    try
    {
      transientDAO.setValue("testReference", reference.getId());
      Assert.assertTrue(transientDAO.getAttributeIF("testReference") instanceof AttributeReference);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
    finally
    {
      TestFixtureFactory.delete(reference);
    }
  }

  /**
   * Tests setting a reference attribute to an invalid target object.
   */
  @Request
  @Test
  public void testSetInvalidReference()
  {
    BusinessDAO badReference = BusinessDAO.newInstance(SessionMasterTestSetup.SOME_CLASS.getType());
    badReference.apply();

    try
    {
      transientDAO.setValue("testReference", badReference.getId());
      transientDAO.getAttributeIF("testReference");
      Assert.fail("AttributeReference accepted a reference to an object of the wrong type.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(badReference);
    }
  }

  /**
   * Tests dereferencing a reference.
   */
  @Request
  @Test
  public void testDereferenceReference()
  {
    BusinessDAO reference = BusinessDAO.newInstance(EntityMasterTestSetup.REFERENCE_CLASS.getType());
    reference.apply();

    try
    {
      transientDAO.setValue("testReference", reference.getId());
      transientDAO.apply();
      AttributeReference fo = (AttributeReference) transientDAO.getAttributeIF("testReference");
      Assert.assertEquals(reference.getId(), transientDAO.getAttributeIF("testReference").getValue());
      Assert.assertEquals(reference.getId(), fo.dereference().getId());
      Assert.assertEquals(reference.getAttributeIF("refChar").getValue(), fo.dereference().getAttributeIF("refChar").getValue());
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
    finally
    {
      TestFixtureFactory.delete(reference);
    }
  }

  @Request
  @Test
  public void testStruct()
  {
    try
    {
      // create a new contact object and its attributes
      MdStructDAOIF phoneNumber = MdStructDAO.getMdStructDAO(EntityTypes.PHONE_NUMBER.getType());
      Map<String, ? extends MdAttributeDAOIF> chunks = phoneNumber.getAllDefinedMdAttributeMap();

      transientDAO.setStructValue("homePhone", chunks.get("areacode").definesAttribute(), "303");
      transientDAO.setStructValue("homePhone", chunks.get("prefix").definesAttribute(), "979");
      transientDAO.setStructValue("homePhone", chunks.get("suffix").definesAttribute(), "7745");

      transientDAO.setStructValue("cellPhone", chunks.get("areacode").definesAttribute(), "720");
      transientDAO.setStructValue("cellPhone", chunks.get("prefix").definesAttribute(), "363");
      transientDAO.setStructValue("cellPhone", chunks.get("suffix").definesAttribute(), "8174");

      transientDAO.setStructValue("workPhone", chunks.get("areacode").definesAttribute(), "606");
      transientDAO.setStructValue("workPhone", chunks.get("prefix").definesAttribute(), "980");
      transientDAO.setStructValue("workPhone", chunks.get("suffix").definesAttribute(), "4370");

      transientDAO.apply();

      // get the three phone numbers
      if (!transientDAO.getStructValue("homePhone", "areaCode").equals("303") || !transientDAO.getStructValue("homePhone", "prefix").equals("979") || !transientDAO.getStructValue("homePhone", "suffix").equals("7745"))
      {
        Assert.fail("Failed to correctly persist and retrieve a struct attribute value.");
      }

      if (!transientDAO.getStructValue("cellPhone", "areaCode").equals("720") || !transientDAO.getStructValue("cellPhone", "prefix").equals("363") || !transientDAO.getStructValue("cellPhone", "suffix").equals("8174"))
      {
        Assert.fail("Failed to correctly persist and retrieve a struct attribute value.");
      }

      if (!transientDAO.getStructValue("workPhone", "areaCode").equals("606") || !transientDAO.getStructValue("workPhone", "prefix").equals("980") || !transientDAO.getStructValue("workPhone", "suffix").equals("4370"))
      {
        Assert.fail("Failed to correctly persist and retrieve a struct attribute value.");
      }
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Make sure we cannot add an attribute to a class with the same name as an
   * attribute inherited from a supertype.
   */
  @Request
  @Test
  public void testDuplicateInheritedAttribute()
  {
    MdViewDAO other = null;
    MdAttributeFloatDAO float1DO = null;

    try
    {
      // Create the utensil data type
      TypeInfo otherClass = new TypeInfo(SessionMasterTestSetup.JUNIT_PACKAGE, "OtherTest");
      other = MdViewDAO.newInstance();
      other.setValue(MdViewInfo.NAME, otherClass.getTypeName());
      other.setValue(MdViewInfo.PACKAGE, otherClass.getPackageName());
      other.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      other.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, otherClass.getTypeName() + " Test Type");
      other.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary JUnit Test Type");
      other.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      other.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      other.setValue(MdViewInfo.SUPER_MD_VIEW, attributeMdView.getId());
      other.setValue(MdViewInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      other.apply();

      // first declaration of attribute Cost
      TypeInfo floatClass = new TypeInfo(SessionMasterTestSetup.JUNIT_PACKAGE, "testFloat");
      float1DO = MdAttributeFloatDAO.newInstance();
      float1DO.setValue(MdAttributeFloatInfo.NAME, floatClass.getTypeName());
      float1DO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
      float1DO.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
      float1DO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      float1DO.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.LENGTH, "10");
      float1DO.setValue(MdAttributeFloatInfo.DECIMAL, "2");
      float1DO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, other.getId());
      float1DO.apply();

      // if we hit the line below, then our check failed
      Assert.fail("A subclass was able to declare an attribute of the same name as held by its superclass.");
    }
    catch (DuplicateAttributeInInheritedHierarchyException e)
    {
      // we should land here and fail
    }
    finally
    {
      if (other != null && other.isAppliedToDB())
      {
        TestFixtureFactory.delete(other);
      }
    }
  }

  /**
   * A test to make sure that attribute names cannot exceed
   * Constants.MAX_DB_IDENTIFIER_SIZE in length.
   */
  @Request
  @Test
  public void testMaxIdentifierSize()
  {
    MdAttributeFloatDAO float1DO = null;

    try
    {
      // first declaration of attribute Cost
      TypeInfo floatClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "thisattributesizeiswaytoolong12thisattributesizeiswaytoolong12thisattributesizeiswaytoolong12");
      float1DO = MdAttributeFloatDAO.newInstance();
      float1DO.setValue(MdAttributeFloatInfo.NAME, floatClass.getTypeName());
      float1DO.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Float 1");
      float1DO.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
      float1DO.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      float1DO.setValue(MdAttributeFloatInfo.LENGTH, "10");
      float1DO.setValue(MdAttributeFloatInfo.DECIMAL, "2");
      float1DO.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, attributeMdView.getId());
      float1DO.apply();

      Assert.fail("An attribute identifier length is too long, but was accepted by the system.");
    }
    catch (AttributeLengthCharacterException e)
    {
      // This is expected
    }
    finally
    {
      if (float1DO != null && float1DO.isAppliedToDB())
      {
        TestFixtureFactory.delete(float1DO);
      }
    }
  }

  /**
   * A reference should only be allowed to reference a class, never an object.
   */
  @Request
  @Test
  public void testReferenceReferenceRelationship()
  {
    try
    {
      MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, someTree.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, attributeMdView.getId());
      mdAttributeReference.apply();

      Assert.fail("A reference was incorrectly able to reference a relationship.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
  }

  /**
   * Make sure MdAttributeReference only reference BusinessDAOs and not
   * Relationships
   */
  @Request
  @Test
  public void testReferenceReference()
  {
    BusinessDAO referenceDAO = BusinessDAO.newInstance(SessionMasterTestSetup.REFERENCE_CLASS.getType());
    referenceDAO.apply();

    try
    {
      MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
      mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "testReference");
      mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A reference");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, referenceDAO.getId());
      mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, attributeMdView.getId());
      mdAttributeReference.apply();

      Assert.fail("An " + MdAttributeReferenceDAO.CLASS + " was incorrectly able to reference a relatioship.");
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
    }
    finally
    {
      TestFixtureFactory.delete(referenceDAO);
    }
  }

  /**
   * Tests a valid blob using a byte array.
   */
  @Request
  @Test
  public void testBlobWithBytes()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);

      // make sure the cached blob value is correct
      if (!EntityAttributeTest.equalsBytes(value, blob.getBlobAsBytes()))
      {
        Assert.fail("The cached value for " + key + " does not equal the input value");
      }
      transientDAO.apply();

      // compare the input and the retrieved value
      AttributeBlob blobRetrieved = (AttributeBlob) transientDAO.getAttributeIF(key);

      if (!EntityAttributeTest.equalsBytes(value, blobRetrieved.getBlobAsBytes()))
      {
        Assert.fail("The stored database value for " + key + " does not equal the input value");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a valid blob using a byte array and performs manipulation on the
   * blob. This method won't apply the blob data to the database as it tests the
   * attribute's ability to manipulate data without using the JDBC API.
   */
  @Request
  @Test
  public void testBlobManipulationCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);

      // change the blob value
      byte[] newValue = { 'j' };
      blob.setBlobAsBytes(1, newValue, 0, 1);

      // compare to the target bytes
      byte[] target = { 'j', 'e', 'l', 'l', 'o' };

      if (!EntityAttributeTest.equalsBytes(target, blob.getBlobAsBytes()))
      {
        Assert.fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: first round.");
      }

      // change the blob value again
      byte[] newValue2 = { 'h', 'e', 't', 't', 'o' };
      blob.setBlobAsBytes(3, newValue2, 2, 2);

      // compare to the target2 bytes
      byte[] target2 = { 'j', 'e', 't', 't', 'o' };

      if (!EntityAttributeTest.equalsBytes(target2, blob.getBlobAsBytes()))
      {
        Assert.fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: second round.");
      }

      // final manipulation to write past the end of the current blob
      byte[] newValue3 = { 'N', 'N', 'N', 'N', 'o', 'n' };
      blob.setBlobAsBytes(5, newValue3, 4, 2);

      // compare to the target3 bytes
      byte[] target3 = { 'j', 'e', 't', 't', 'o', 'n' };

      if (!EntityAttributeTest.equalsBytes(target3, blob.getBlobAsBytes()))
      {
        Assert.fail("AttributeBlob.setBlobAsBytes() failed to manipulate the blob in cache: final round.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Truncates a blob in the cache and tests to make sure it is successful.
   */
  @Request
  @Test
  public void testBlobTruncateCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);
      blob.truncateBlob(3);
      int length = (int) blob.getBlobSize();

      if (length != 3)
      {
        Assert.fail("The cached blob value did not get truncated correctly.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Changes the size of a blob to test if the size is truncated if necessary.
   */
  @Request
  @Test
  public void testBlobChangeSize()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o', ' ', 'w', 'o', 'r', 'l', 'd' };
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);
      transientDAO.apply();

      byte[] value2 = { 'h', 'e', 'l', 'l', 'o' };
      blob.setBlobAsBytes(value2);
      transientDAO.apply();

      int length = (int) blob.getBlobSize();
      byte[] rValue = blob.getBlobAsBytes();

      if (length != value2.length || !EntityAttributeTest.equalsBytes(rValue, value2))
      {
        Assert.fail("The cached blob value did not get truncated correctly when the size changed.");
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a blob by storing bytes in the blob cache and then getting it back
   * and testing for correctness.
   */
  @Request
  @Test
  public void testBlobGetBytesCache()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = { 'h', 'e', 'l', 'l', 'o' };
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);

      byte[] allBytes = blob.getBlobAsBytes();

      // test all bytes
      if (value.length != allBytes.length)
      {
        Assert.fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < value.length; i++)
      {
        if (value[i] != allBytes[i])
        {
          Assert.fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }

      // test a sub set of bytes
      byte[] subBytes = blob.getBlobAsBytes(3, 3);
      byte[] target = { 'l', 'l', 'o' };

      if (subBytes.length != target.length)
      {
        Assert.fail("AttributeBlob.getBlobAsBytes() did not return the correct number of cached bytes.");
      }

      for (int i = 0; i < target.length; i++)
      {
        if (subBytes[i] != target[i])
        {
          Assert.fail("AttributeBlob.getBlobAsBytes() did not return the correct cached bytes.");
          break;
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests a blob attribute with a size that is too large.
   */
  @Request
  @Test
  public void testBlobInvalidSize()
  {
    try
    {
      // set value and store it
      String key = "testBlob";
      byte[] value = new byte[MdAttributeBlobDAO.getMaxLength() + 1];
      AttributeBlob blob = (AttributeBlob) transientDAO.getAttribute(key);
      blob.setBlobAsBytes(value);

      Assert.fail("An attribute blob was able to exceed the maximum size.");
    }
    catch (AttributeLengthByteException e)
    {
      // This is expected
    }
  }

  /**
   * Tests a single value on an enumerated attribute
   */
  @Request
  @Test
  public void testSingle()
  {
    try
    {
      transientDAO.setValue(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Sets and tests multiple values on an enumerated attribute
   */
  @Request
  @Test
  public void testAddMultiple()
  {
    try
    {
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.addItem(MULTIPLE, coloradoItemId);
      transientDAO.addItem(MULTIPLE, connecticutItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, californiaItemId, coloradoItemId, connecticutItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests the default value on an enumerated attribute
   */
  @Request
  @Test
  public void testDefault()
  {
    try
    {
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests adding a value an applying the object.
   */
  @Request
  @Test
  public void testAdd()
  {
    try
    {
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId, californiaItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests deletion of an item (with other items still remaining)
   */
  @Request
  @Test
  public void testDelete()
  {
    try
    {
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.apply();
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, californiaItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests the deletion of the default item (leaving the value blank)
   */
  @Request
  @Test
  public void testDeleteDefault()
  {
    try
    {
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests the deletion of the default item (leaving the value blank)
   */
  @Request
  @Test
  public void testDeleteRequired()
  {
    try
    {
      transientDAO.removeItem(SINGLE, coloradoItemId);
      transientDAO.apply();
      Assert.fail("An empty value on a required enumeration (" + SINGLE + ") was accepted.");
    }
    catch (ProblemException e)
    {
      ProblemException problemException = (ProblemException) e;

      if (problemException.getProblems().size() == 1 && problemException.getProblems().get(0) instanceof EmptyValueProblem)
      {
        // Expected to land here
      }
      else
      {
        Assert.fail(EmptyValueProblem.class.getName() + " was not thrown.");
      }
    }
  }

  /**
   * Tests explicitly adding the default item to the enumeration
   */
  @Request
  @Test
  public void testAddDefault()
  {
    try
    {
      transientDAO.addItem(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests adding an item to a single-select enumeration, which changes the
   * selection to the added item.
   */
  @Request
  @Test
  public void testAddToSingle()
  {
    transientDAO.apply();
    transientDAO.addItem(SINGLE, connecticutItemId);
    transientDAO.apply();
    transientDAO.addItem(SINGLE, californiaItemId);
    transientDAO.apply();

    AttributeEnumeration attribute = (AttributeEnumeration) transientDAO.getAttribute(SINGLE);
    Set<String> list = attribute.getCachedEnumItemIdSet();
    if (list.size() != 1)
      Assert.fail("Single-select Enumeration allowed multiple items.");
    if (!list.contains(californiaItemId))
      Assert.fail("Single-select Enumeration did not update item selection.");
  }

  /**
   * Tests adding the same items repeatedly to the enumeration
   */
  @Request
  @Test
  public void testRepeatedAdd()
  {
    try
    {
      transientDAO.addItem(MULTIPLE, coloradoItemId);
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId, californiaItemId);

      transientDAO.addItem(MULTIPLE, coloradoItemId);
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.addItem(MULTIPLE, coloradoItemId);
      transientDAO.addItem(MULTIPLE, californiaItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId, californiaItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests deleting the same item multiple times (thus deleting an item that
   * isn't present)
   */
  @Request
  @Test
  public void testRepeatedDelete()
  {
    try
    {
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple);

      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.removeItem(MULTIPLE, coloradoItemId);
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests deleting an item that isn't valid on the enumeration
   */
  @Request
  @Test
  public void testInvalidDelete()
  {
    try
    {
      transientDAO.apply();
      transientDAO.removeItem(MULTIPLE, "Not an ID");
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests deleting an item that isn't valid on the enumeration
   */
  @Request
  @Test
  public void testInvalidAdd()
  {
    try
    {
      transientDAO.apply();
      transientDAO.addItem(MULTIPLE, "Not an ID");
      transientDAO.apply();

      checkEnumState(transientDAO, MULTIPLE, mdAttrEnumMultiple, coloradoItemId);
      Assert.fail("State accepted an invalid item.");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Gets a new instance of the transientDAO and checks for the number and id of
   * each state passed in (varibale number of arguments).
   * 
   * @param states
   *          IDs of items expected to be in the enumeration
   */
  private void checkEnumState(TransientDAO transientDAO, String field, MdAttributeEnumerationDAO mdAttrEnum, String... states)
  {
    AttributeEnumerationIF enumr = (AttributeEnumerationIF) transientDAO.getAttributeIF(field);
    Set<String> l = enumr.getCachedEnumItemIdSet();

    if (l.size() != states.length)
    {
      Assert.fail("Instance of \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" expected " + states.length + " states in " + field + ", found " + l.size());
    }

    for (String state : states)
    {
      if (!l.contains(state))
      {
        Assert.fail("Instance of \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" does not contain all of the expected values in " + field);
      }
    }

  }

}
