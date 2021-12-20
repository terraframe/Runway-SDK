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
package com.runwaysdk.query;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import com.runwaysdk.business.generation.EntityQueryAPIGenerator;
import com.runwaysdk.business.generation.TypeGenerator;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeDateInfo;
import com.runwaysdk.constants.MdAttributeDateTimeInfo;
import com.runwaysdk.constants.MdAttributeDecimalInfo;
import com.runwaysdk.constants.MdAttributeDoubleInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeFloatInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalCharacterInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeLongInfo;
import com.runwaysdk.constants.MdAttributeReferenceInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdAttributeTermInfo;
import com.runwaysdk.constants.MdAttributeTextInfo;
import com.runwaysdk.constants.MdAttributeTimeInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdRelationshipInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.MdViewInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.BusinessDAO;
import com.runwaysdk.dataaccess.EntityMasterTestSetup;
import com.runwaysdk.dataaccess.EnumerationItemDAO;
import com.runwaysdk.dataaccess.MdBusinessDAOIF;
import com.runwaysdk.dataaccess.MdClassDAOIF;
import com.runwaysdk.dataaccess.RelationshipDAO;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
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
import com.runwaysdk.dataaccess.metadata.MdAttributeLocalCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeLongDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeReferenceDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTermDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTextDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeTimeDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTermDAO;
import com.runwaysdk.dataaccess.metadata.MdViewDAO;
import com.runwaysdk.dataaccess.transaction.Transaction;
import com.runwaysdk.session.Request;

/**
 * Tests the query API
 */
public class QueryMasterSetup
{
  protected static final String             NUM_OF_FLOAT_DECIMALS   = "2";

  // ////////////////////////////////////////////////

  protected String                          testQueryType           = null;

  protected static BusinessDAO              testQueryObject1        = null;

  protected static MdBusinessDAOIF          selectedMdBusiness      = null;

  // ////////////////////////////////////////////////

  protected static final TypeInfo           parentQueryInfo         = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ParentQueryObject");

  protected static MdBusinessDAO            parentMdBusiness        = null;

  protected static final String             PARENT_PREFIX           = "query";

  // /////////////////////////////////////////////////

  protected static final TypeInfo           compareQueryInfo        = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "CompareQueryObject");

  protected static MdBusinessDAO            compareMdBusiness       = null;

  protected static BusinessDAO              compareQueryObject      = null;

  protected static final String             COMPARE_PREFIX          = "com";

  // /////////////////////////////////////////////////

  protected static final TypeInfo           childQueryInfo          = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ChildQueryObject");

  protected static MdBusinessDAO            childMdBusiness         = null;

  // /////////////////////////////////////////////////

  protected static final TypeInfo           connectionQueryInfo     = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ConnectionRel");

  protected static MdRelationshipDAO        connectionMdRel         = null;

  protected static RelationshipDAO          connectionInstance1     = null;

  protected static final String             CON_PREFIX              = "conQuery";

  // /////////////////////////////////////////////////

  protected static final TypeInfo           badConnectionQueryInfo  = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "BadConnectionRel");

  protected static MdRelationshipDAO        badConnectionMdRel      = null;

  // /////////////////////////////////////////////////

  protected static MdBusinessDAO            relMdBusiness           = null;

  protected static TypeInfo                 relQueryInfo            = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "RelQueryObject");

  protected static BusinessDAO              relQueryObject1         = null;

  protected static final String             REL_PREFIX              = "relQuery";

  // /////////////////////////////////////////////////

  protected static final TypeInfo           parentRefQueryInfo      = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ParentRefQueryObject");

  protected static MdBusinessDAO            parentRefMdBusiness     = null;

  protected static BusinessDAO              childRefQueryObject     = null;

  protected static BusinessDAO              childRefQueryObject2    = null;

  protected static final TypeInfo           childRefQueryInfo       = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ChildRefQueryObject");

  protected static MdBusinessDAO            childRefMdBusiness      = null;

  protected static final String             REF_PREFIX              = "refQuery";

  protected String                          testRefQueryType        = null;

  // /////////////////////////////////////////////////

  protected static final TypeInfo           stateClass              = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State");

  protected static final String             ENUM_PREFIX             = "enumQuery";

  protected static final TypeInfo           stateEnum_all           = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State_Enum");

  protected static final TypeInfo           stateEnum_east          = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_East_State_Enum");

  protected static final TypeInfo           stateEnum_west          = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_West_State_Enum");

  protected static MdBusinessDAO            stateEnumMdBusiness     = null;

  protected static MdEnumerationDAO         stateMdEnumeration_all  = null;

  protected static MdEnumerationDAO         stateMdEnumeration_east = null;

  protected static MdEnumerationDAO         stateMdEnumeration_west = null;

  protected static List<EnumerationItemDAO> enumerationItems;

  protected static List<EnumerationItemDAO> enumerationItems_east;

  protected static List<EnumerationItemDAO> enumerationItems_west;

  protected static MdViewDAO                mdView;

  protected static final String             VIEW_PREFIX             = "view";

  protected static final TypeInfo           viewQueryInfo           = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestView");

  // /////////////////////////////////////////////////

  protected static final TypeInfo           structInfo              = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TestStructQuery");

  protected static MdStructDAO              mdStruct                = null;

  protected static final String             BASIC_PREFIX            = "structQuery";

  // /////////////////////////////////////////////////

  protected static String                   californiaItemId        = null;

  protected static String                   coloradoItemId          = null;

  protected static String                   connecticutItemId       = null;

  protected static String                   kansasItemId            = null;

  // ////////////////////////////////////////////////

  protected static MdTermDAO                mdTerm;

  protected static BusinessDAO              termQueryObject         = null;

  protected static final TypeInfo           termQueryInfo           = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TermQueryObject");

  protected static final String             TERM_PREFIX             = "termQuery";

  public QueryMasterSetup(String type, String refType)
  {
    testQueryType = type;
    testRefQueryType = refType;
  }

  public String getTestQueryType()
  {
    return this.testQueryType;
  }

  @Request
  @Before
  public void setUp() throws Exception
  {
    this.setUpTransaction();
  }

  @Transaction
  public void setUpTransaction()
  {
//    System.out.println("Setup for - " + this.getTest().countTestCases() + " tests");

    enumerationItems = new LinkedList<EnumerationItemDAO>();
    enumerationItems_east = new LinkedList<EnumerationItemDAO>();
    enumerationItems_west = new LinkedList<EnumerationItemDAO>();

    // create the class to partake in a relationship (the child)
    relMdBusiness = MdBusinessDAO.newInstance();
    relMdBusiness.setValue(MdBusinessInfo.NAME, relQueryInfo.getTypeName());
    relMdBusiness.setValue(MdBusinessInfo.PACKAGE, relQueryInfo.getPackageName());
    relMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    relMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship Child type");
    relMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A child in a relationship.");
    relMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    relMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    relMdBusiness.apply();

    loadAttributePrimitives(relMdBusiness, REL_PREFIX);

    // create a struct
    mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.NAME, structInfo.getTypeName());
    mdStruct.setValue(MdStructInfo.PACKAGE, structInfo.getPackageName());
    mdStruct.setValue(MdStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Basic Query Class");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A struct to query.");
    mdStruct.apply();

    loadAttributePrimitives(mdStruct, BASIC_PREFIX);

    // create an enumeration
    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    stateEnumMdBusiness = MdBusinessDAO.newInstance();
    stateEnumMdBusiness.setValue(MdBusinessInfo.NAME, stateClass.getTypeName());
    stateEnumMdBusiness.setValue(MdBusinessInfo.PACKAGE, stateClass.getPackageName());
    stateEnumMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State");
    stateEnumMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "States of the Union");
    stateEnumMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
    stateEnumMdBusiness.apply();

    loadAttributePrimitives(stateEnumMdBusiness, ENUM_PREFIX);

    stateMdEnumeration_all = MdEnumerationDAO.newInstance();
    stateMdEnumeration_all.setValue(MdEnumerationInfo.NAME, stateEnum_all.getTypeName());
    stateMdEnumeration_all.setValue(MdEnumerationInfo.PACKAGE, stateEnum_all.getPackageName());
    stateMdEnumeration_all.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States in the United States");
    stateMdEnumeration_all.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    stateMdEnumeration_all.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    stateMdEnumeration_all.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    stateMdEnumeration_all.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusiness.getOid());
    stateMdEnumeration_all.apply();

    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "stateCode");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "2");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Postal Code");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getOid());
    mdAttrChar.apply();

    mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "stateName");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "32");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Name");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getOid());
    mdAttrChar.apply();

    EnumerationItemDAO enumItemCA = EnumerationItemDAO.newInstance(stateClass.getType());
    enumItemCA.setValue("stateCode", "CA");
    enumItemCA.setValue("stateName", "California");
    enumItemCA.setValue(EnumerationMasterInfo.NAME, "CA");
    enumItemCA.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "California");
    enumItemCA.setValue("enumQueryBoolean", MdAttributeBooleanInfo.TRUE);
    enumItemCA.setValue("enumQueryCharacter", "enum character value");
    enumItemCA.setValue("enumQueryText", "enum text value");
    enumItemCA.setValue("enumQueryClob", "enum clob value");
    enumItemCA.setValue("enumQueryDateTime", "2006-11-06 12:00:00");
    enumItemCA.setValue("enumQueryDate", "2006-11-06");
    enumItemCA.setValue("enumQueryTime", "12:00:00");
    enumItemCA.setValue("enumQueryInteger", "200");
    enumItemCA.setValue("enumQueryLong", "200");
    enumItemCA.setValue("enumQueryFloat", "200.5");
    enumItemCA.setValue("enumQueryDecimal", "200.5");
    enumItemCA.setValue("enumQueryDouble", "200.5");
    californiaItemId = enumItemCA.apply();
    enumerationItems.add(enumItemCA);
    enumerationItems_west.add(enumItemCA);

    EnumerationItemDAO enumItemCO = EnumerationItemDAO.newInstance(stateClass.getType());
    enumItemCO.setValue("stateCode", "CO");
    enumItemCO.setValue("stateName", "Colorado");
    enumItemCO.setValue(EnumerationMasterInfo.NAME, "CO");
    enumItemCO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Colorado");
    enumItemCO.setValue("enumQueryBoolean", MdAttributeBooleanInfo.TRUE);
    enumItemCO.setValue("enumQueryCharacter", "enum character value");
    enumItemCO.setValue("enumQueryText", "enum text value");
    enumItemCO.setValue("enumQueryClob", "enum clob value");
    enumItemCO.setValue("enumQueryDateTime", "2006-11-06 12:00:00");
    enumItemCO.setValue("enumQueryDate", "2006-11-06");
    enumItemCO.setValue("enumQueryTime", "12:00:00");
    enumItemCO.setValue("enumQueryInteger", "200");
    enumItemCO.setValue("enumQueryLong", "200");
    enumItemCO.setValue("enumQueryFloat", "200.5");
    enumItemCO.setValue("enumQueryDecimal", "200.5");
    enumItemCO.setValue("enumQueryDouble", "200.5");
    coloradoItemId = enumItemCO.apply();
    enumerationItems.add(enumItemCO);
    enumerationItems_west.add(enumItemCO);

    EnumerationItemDAO enumItemCT = EnumerationItemDAO.newInstance(stateClass.getType());
    enumItemCT.setValue("stateCode", "CT");
    enumItemCT.setValue("stateName", "Connecticut");
    enumItemCT.setValue(EnumerationMasterInfo.NAME, "CT");
    enumItemCT.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Connecticut");
    connecticutItemId = enumItemCT.apply();
    enumerationItems.add(enumItemCT);
    enumerationItems_east.add(enumItemCT);

    EnumerationItemDAO enumItemKS = EnumerationItemDAO.newInstance(stateClass.getType());
    enumItemKS.setValue("stateCode", "KS");
    enumItemKS.setValue("stateName", "Kansas");
    enumItemKS.setValue(EnumerationMasterInfo.NAME, "KS");
    enumItemKS.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Kansas");
    kansasItemId = enumItemKS.apply();
    enumerationItems.add(enumItemKS);
    enumerationItems_east.add(enumItemKS);

    stateMdEnumeration_east = MdEnumerationDAO.newInstance();
    stateMdEnumeration_east.setValue(MdEnumerationInfo.NAME, stateEnum_east.getTypeName());
    stateMdEnumeration_east.setValue(MdEnumerationInfo.PACKAGE, stateEnum_east.getPackageName());
    stateMdEnumeration_east.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Eastern States in the United States");
    stateMdEnumeration_east.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    stateMdEnumeration_east.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    stateMdEnumeration_east.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
    stateMdEnumeration_east.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusiness.getOid());
    stateMdEnumeration_east.apply();

    stateMdEnumeration_east.addEnumItem(enumItemCT);
    stateMdEnumeration_east.addEnumItem(enumItemKS);

    stateMdEnumeration_west = MdEnumerationDAO.newInstance();
    stateMdEnumeration_west.setValue(MdEnumerationInfo.NAME, stateEnum_west.getTypeName());
    stateMdEnumeration_west.setValue(MdEnumerationInfo.PACKAGE, stateEnum_east.getPackageName());
    stateMdEnumeration_west.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Eastern States in the United States");
    stateMdEnumeration_west.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    stateMdEnumeration_west.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    stateMdEnumeration_west.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.FALSE);
    stateMdEnumeration_west.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusiness.getOid());
    stateMdEnumeration_west.apply();

    stateMdEnumeration_west.addEnumItem(enumItemCA);
    stateMdEnumeration_west.addEnumItem(enumItemCO);

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "structQueryEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An enumeration to query.");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdStruct.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration_all.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "relQueryEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An enumeration to query.");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, relMdBusiness.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration_all.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    parentRefMdBusiness = MdBusinessDAO.newInstance();
    parentRefMdBusiness.setValue(MdBusinessInfo.NAME, parentRefQueryInfo.getTypeName());
    parentRefMdBusiness.setValue(MdBusinessInfo.PACKAGE, parentRefQueryInfo.getPackageName());
    parentRefMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentRefMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Query Ref Type");
    parentRefMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary ref type to perform queries on.");
    parentRefMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentRefMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentRefMdBusiness.apply();

    // child ref query object
    childRefMdBusiness = MdBusinessDAO.newInstance();
    childRefMdBusiness.setValue(MdBusinessInfo.NAME, childRefQueryInfo.getTypeName());
    childRefMdBusiness.setValue(MdBusinessInfo.PACKAGE, childRefQueryInfo.getPackageName());
    childRefMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childRefMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Query Ref Type");
    childRefMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary ref type to perform queries on.");
    childRefMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childRefMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    childRefMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, parentRefMdBusiness.getOid());
    childRefMdBusiness.apply();

    MdBusinessDAOIF selectedRefMdBusiness = MdBusinessDAO.getMdBusinessDAO(testRefQueryType);

    mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "refQueryEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An enumeration to query.");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, selectedRefMdBusiness.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration_all.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    loadAttributePrimitives(selectedRefMdBusiness, REF_PREFIX);

    // parent query object
    parentMdBusiness = MdBusinessDAO.newInstance();
    parentMdBusiness.setValue(MdBusinessInfo.NAME, parentQueryInfo.getTypeName());
    parentMdBusiness.setValue(MdBusinessInfo.PACKAGE, parentQueryInfo.getPackageName());
    parentMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Parent Test Query Type");
    parentMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary type to perform queries on.");
    parentMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    parentMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    parentMdBusiness.apply();

    // child query object
    childMdBusiness = MdBusinessDAO.newInstance();
    childMdBusiness.setValue(MdBusinessInfo.NAME, childQueryInfo.getTypeName());
    childMdBusiness.setValue(MdBusinessInfo.PACKAGE, childQueryInfo.getPackageName());
    childMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Child Test Query Type");
    childMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Temporary type to perform queries on.");
    childMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    childMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    childMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, parentMdBusiness.getOid());
    childMdBusiness.apply();

    mdTerm = TestFixtureFactory.createMdTerm(termQueryInfo.getPackageName(), termQueryInfo.getTypeName());
    mdTerm.apply();

    TestFixtureFactory.addCharacterAttribute(mdTerm, "termName").apply();

    // now define the attributes on the parent or child MdBusiness, depending
    // on the chosen type
    selectedMdBusiness = MdBusinessDAO.getMdBusinessDAO(testQueryType);

    loadAttributePrimitives(selectedMdBusiness, PARENT_PREFIX);

    mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "queryEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An enumeration to query.");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, selectedMdBusiness.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration_all.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    MdAttributeReferenceDAO mdAttributeReference = MdAttributeReferenceDAO.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceInfo.NAME, "reference");
    mdAttributeReference.setStructValue(MdAttributeReferenceInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Reference");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFAULT_VALUE, "");
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceInfo.REF_MD_ENTITY, childRefMdBusiness.getOid());
    mdAttributeReference.setValue(MdAttributeReferenceInfo.DEFINING_MD_CLASS, selectedMdBusiness.getOid());
    mdAttributeReference.apply();

    MdAttributeTermDAO mdAttributeTerm = MdAttributeTermDAO.newInstance();
    mdAttributeTerm.setValue(MdAttributeTermInfo.NAME, "term");
    mdAttributeTerm.setStructValue(MdAttributeTermInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term");
    mdAttributeTerm.setValue(MdAttributeTermInfo.DEFAULT_VALUE, "");
    mdAttributeTerm.setValue(MdAttributeTermInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTerm.setValue(MdAttributeTermInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTerm.setValue(MdAttributeTermInfo.REF_MD_ENTITY, mdTerm.getOid());
    mdAttributeTerm.setValue(MdAttributeTermInfo.DEFINING_MD_CLASS, selectedMdBusiness.getOid());
    mdAttributeTerm.apply();

    MdAttributeStructDAO mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "queryStruct");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A struct to query.");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, selectedMdBusiness.getOid());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getOid());
    mdAttrStruct.apply();

    MdAttributeLocalCharacterDAO mdAttrLocalCharacter = MdAttributeLocalCharacterDAO.newInstance();
    mdAttrLocalCharacter.setValue(MdAttributeLocalCharacterInfo.NAME, "queryLocalChar");
    mdAttrLocalCharacter.setStructValue(MdAttributeLocalCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A struct to query.");
    mdAttrLocalCharacter.setValue(MdAttributeLocalCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrLocalCharacter.setValue(MdAttributeLocalCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrLocalCharacter.setValue(MdAttributeLocalCharacterInfo.DEFINING_MD_CLASS, selectedMdBusiness.getOid());
    mdAttrLocalCharacter.apply();

    MdAttributeStructDAO mdAttrStructRef = MdAttributeStructDAO.newInstance();
    mdAttrStructRef.setValue(MdAttributeStructInfo.NAME, "queryStruct");
    mdAttrStructRef.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A struct to query.");
    mdAttrStructRef.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStructRef.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStructRef.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, childRefMdBusiness.getOid());
    mdAttrStructRef.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getOid());
    mdAttrStructRef.apply();

    // define the metadata for the entity to use in attribute comparison
    compareMdBusiness = MdBusinessDAO.newInstance();
    compareMdBusiness.setValue(MdBusinessInfo.NAME, compareQueryInfo.getTypeName());
    compareMdBusiness.setValue(MdBusinessInfo.PACKAGE, compareQueryInfo.getPackageName());
    compareMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    compareMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Relationship Child type");
    compareMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A compare entity.");
    compareMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    compareMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    compareMdBusiness.apply();

    loadAttributePrimitives(compareMdBusiness, COMPARE_PREFIX);

    // define the metadata for the relationship called "Connection"
    // This will be mainly used for the positive tests
    connectionMdRel = MdRelationshipDAO.newInstance();
    connectionMdRel.setValue(MdRelationshipInfo.NAME, connectionQueryInfo.getTypeName());
    connectionMdRel.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    connectionMdRel.setValue(MdRelationshipInfo.PACKAGE, connectionQueryInfo.getPackageName());
    connectionMdRel.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Relationship to query.");
    connectionMdRel.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    connectionMdRel.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    connectionMdRel.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    connectionMdRel.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, selectedMdBusiness.getOid());
    connectionMdRel.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    connectionMdRel.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, selectedMdBusiness.definesType());
    connectionMdRel.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, relMdBusiness.getOid());
    connectionMdRel.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    connectionMdRel.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, relMdBusiness.definesType());
    connectionMdRel.setValue(MdRelationshipInfo.PARENT_METHOD, "QueryParent3");
    connectionMdRel.setValue(MdRelationshipInfo.CHILD_METHOD, "QueryChild3");
    connectionMdRel.apply();

    mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "conQueryEnumeration");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An enumeration to query.");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, connectionMdRel.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumeration_all.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    loadAttributePrimitives(connectionMdRel, CON_PREFIX);

    // define the metadata for the relationship called "BadConnection"
    // This will be mainly used for the negative tests (no instances of this
    // will be created)
    badConnectionMdRel = MdRelationshipDAO.newInstance();
    badConnectionMdRel.setValue(MdRelationshipInfo.NAME, badConnectionQueryInfo.getTypeName());
    badConnectionMdRel.setValue(MdRelationshipInfo.COMPOSITION, MdAttributeBooleanInfo.FALSE);
    badConnectionMdRel.setValue(MdRelationshipInfo.PACKAGE, badConnectionQueryInfo.getPackageName());
    badConnectionMdRel.setStructValue(MdRelationshipInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A Bad Relationship to query.");
    badConnectionMdRel.setValue(MdRelationshipInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    badConnectionMdRel.setValue(MdRelationshipInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    badConnectionMdRel.setValue(MdRelationshipInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    badConnectionMdRel.setValue(MdRelationshipInfo.PARENT_MD_BUSINESS, selectedMdBusiness.getOid());
    badConnectionMdRel.setValue(MdRelationshipInfo.PARENT_CARDINALITY, "*");
    badConnectionMdRel.setStructValue(MdRelationshipInfo.PARENT_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, selectedMdBusiness.definesType());
    badConnectionMdRel.setValue(MdRelationshipInfo.CHILD_MD_BUSINESS, relMdBusiness.getOid());
    badConnectionMdRel.setValue(MdRelationshipInfo.CHILD_CARDINALITY, "*");
    badConnectionMdRel.setStructValue(MdRelationshipInfo.CHILD_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, relMdBusiness.definesType());
    badConnectionMdRel.setValue(MdRelationshipInfo.PARENT_METHOD, "BadQueryParent3");
    badConnectionMdRel.setValue(MdRelationshipInfo.CHILD_METHOD, "BadQueryChild3");
    badConnectionMdRel.apply();

    // /////////////////////////////
    // define the object instances.
    // /////////////////////////////

    compareQueryObject = BusinessDAO.newInstance(compareQueryInfo.getType());
    compareQueryObject.setValue("comBoolean", MdAttributeBooleanInfo.FALSE);
    compareQueryObject.setValue("comCharacter", "some character value");
    compareQueryObject.setValue("comText", "some text value");
    compareQueryObject.setValue("comClob", "some clob value");
    compareQueryObject.setValue("comDateTime", "2006-12-06 13:00:00");
    compareQueryObject.setValue("comDate", "2006-12-06");
    compareQueryObject.setValue("comTime", "13:00:00");
    compareQueryObject.setValue("comInteger", "100");
    compareQueryObject.setValue("comLong", "100");
    compareQueryObject.setValue("comFloat", "100.5");
    compareQueryObject.setValue("comDecimal", "100.5");
    compareQueryObject.setValue("comDouble", "100.5");
    compareQueryObject.apply();

    childRefQueryObject = BusinessDAO.newInstance(childRefQueryInfo.getType());
    childRefQueryObject.addItem("refQueryEnumeration", connecticutItemId);
    childRefQueryObject.setValue("refQueryBoolean", MdAttributeBooleanInfo.TRUE);
    childRefQueryObject.setValue("refQueryCharacter", "ref character value");
    childRefQueryObject.setValue("refQueryText", "ref text value");
    childRefQueryObject.setValue("refQueryClob", "ref clob value");
    childRefQueryObject.setValue("refQueryDateTime", "2007-11-06 12:00:00");
    childRefQueryObject.setValue("refQueryDate", "2007-11-06");
    childRefQueryObject.setValue("refQueryTime", "12:00:00");
    childRefQueryObject.setValue("refQueryInteger", "200");
    childRefQueryObject.setValue("refQueryLong", "200");
    childRefQueryObject.setValue("refQueryFloat", "200.5");
    childRefQueryObject.setValue("refQueryDecimal", "200.5");
    childRefQueryObject.setValue("refQueryDouble", "200.5");

    // set struct values
    childRefQueryObject.setStructValue("queryStruct", "structQueryBoolean", MdAttributeBooleanInfo.TRUE);
    childRefQueryObject.setStructValue("queryStruct", "structQueryCharacter", "basic character value");
    childRefQueryObject.setStructValue("queryStruct", "structQueryText", "basic text value");
    childRefQueryObject.setStructValue("queryStruct", "structQueryClob", "basic clob value");
    childRefQueryObject.setStructValue("queryStruct", "structQueryDateTime", "2007-11-06 12:00:00");
    childRefQueryObject.setStructValue("queryStruct", "structQueryDate", "2007-11-06");
    childRefQueryObject.setStructValue("queryStruct", "structQueryTime", "12:00:00");
    childRefQueryObject.setStructValue("queryStruct", "structQueryInteger", "200");
    childRefQueryObject.setStructValue("queryStruct", "structQueryLong", "200");
    childRefQueryObject.setStructValue("queryStruct", "structQueryFloat", "200.5");
    childRefQueryObject.setStructValue("queryStruct", "structQueryDecimal", "200.5");
    childRefQueryObject.setStructValue("queryStruct", "structQueryDouble", "200.5");
    childRefQueryObject.apply();

    termQueryObject = BusinessDAO.newInstance(termQueryInfo.getType());
    termQueryObject.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Term 1");
    termQueryObject.setValue("termName", "Test Term");
    termQueryObject.apply();

    childRefQueryObject2 = BusinessDAO.newInstance(childRefQueryInfo.getType());
    childRefQueryObject2.addItem("refQueryEnumeration", connecticutItemId);
    childRefQueryObject2.setValue("refQueryBoolean", MdAttributeBooleanInfo.TRUE);
    childRefQueryObject2.setValue("refQueryCharacter", "ref character value");
    childRefQueryObject2.setValue("refQueryText", "ref text value");
    childRefQueryObject2.setValue("refQueryClob", "ref clob value");
    childRefQueryObject2.setValue("refQueryDateTime", "2006-11-06 12:00:00");
    childRefQueryObject2.setValue("refQueryDate", "2006-11-06");
    childRefQueryObject2.setValue("refQueryTime", "12:00:00");
    childRefQueryObject2.setValue("refQueryInteger", "200");
    childRefQueryObject2.setValue("refQueryLong", "200");
    childRefQueryObject2.setValue("refQueryFloat", "200.5");
    childRefQueryObject2.setValue("refQueryDecimal", "200.5");
    childRefQueryObject2.setValue("refQueryDouble", "200.5");

    // set struct values
    childRefQueryObject2.setStructValue("queryStruct", "structQueryBoolean", MdAttributeBooleanInfo.TRUE);
    childRefQueryObject2.setStructValue("queryStruct", "structQueryCharacter", "basic character value");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryText", "basic text value");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryClob", "basic clob value");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryDateTime", "2006-11-06 12:00:00");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryDate", "2006-11-06");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryTime", "12:00:00");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryInteger", "200");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryLong", "200");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryFloat", "200.5");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryDecimal", "200.5");
    childRefQueryObject2.setStructValue("queryStruct", "structQueryDouble", "200.5");
    childRefQueryObject2.apply();

    testQueryObject1 = BusinessDAO.newInstance(childMdBusiness.definesType());
    testQueryObject1.addItem("queryEnumeration", coloradoItemId);
    testQueryObject1.addItem("queryEnumeration", californiaItemId);
    testQueryObject1.setValue("queryBoolean", MdAttributeBooleanInfo.TRUE);
    testQueryObject1.setValue("queryCharacter", "some character value");
    testQueryObject1.setValue("queryText", "some text value");
    testQueryObject1.setValue("queryClob", "some clob value");
    testQueryObject1.setValue("queryDateTime", "2006-12-06 13:00:00");
    testQueryObject1.setValue("queryDate", "2006-12-06");
    testQueryObject1.setValue("queryTime", "13:00:00");
    testQueryObject1.setValue("queryInteger", "100");
    testQueryObject1.setValue("queryLong", "100");
    testQueryObject1.setValue("queryFloat", "100.5");
    testQueryObject1.setValue("queryDecimal", "100.5");
    testQueryObject1.setValue("queryDouble", "100.5");
    testQueryObject1.setValue("reference", childRefQueryObject.getOid());
    testQueryObject1.setValue("term", termQueryObject.getOid());

    // set struct values
    testQueryObject1.addStructItem("queryStruct", "structQueryEnumeration", connecticutItemId);
    testQueryObject1.setStructValue("queryStruct", "structQueryBoolean", MdAttributeBooleanInfo.TRUE);
    testQueryObject1.setStructValue("queryStruct", "structQueryCharacter", "basic character value");
    testQueryObject1.setStructValue("queryStruct", "structQueryText", "basic text value");
    testQueryObject1.setStructValue("queryStruct", "structQueryClob", "basic clob value");
    testQueryObject1.setStructValue("queryStruct", "structQueryDateTime", "2008-11-06 12:00:00");
    testQueryObject1.setStructValue("queryStruct", "structQueryDate", "2008-11-06");
    testQueryObject1.setStructValue("queryStruct", "structQueryTime", "12:00:00");
    testQueryObject1.setStructValue("queryStruct", "structQueryInteger", "300");
    testQueryObject1.setStructValue("queryStruct", "structQueryLong", "300");
    testQueryObject1.setStructValue("queryStruct", "structQueryFloat", "300.5");
    testQueryObject1.setStructValue("queryStruct", "structQueryDecimal", "300.5");
    testQueryObject1.setStructValue("queryStruct", "structQueryDouble", "300.5");
    testQueryObject1.apply();

    // create the relationship child and relationship instance.
    relQueryObject1 = BusinessDAO.newInstance(relQueryInfo.getType());
    relQueryObject1.addItem("relQueryEnumeration", californiaItemId);
    relQueryObject1.setValue("relQueryBoolean", MdAttributeBooleanInfo.TRUE);
    relQueryObject1.setValue("relQueryCharacter", "child character value");
    relQueryObject1.setValue("relQueryText", "child text value");
    relQueryObject1.setValue("relQueryClob", "child clob value");
    relQueryObject1.setValue("relQueryDateTime", "2004-12-06 13:00:00");
    relQueryObject1.setValue("relQueryDate", "2004-12-06");
    relQueryObject1.setValue("relQueryTime", "13:00:00");
    relQueryObject1.setValue("relQueryInteger", "500");
    relQueryObject1.setValue("relQueryLong", "500");
    relQueryObject1.setValue("relQueryFloat", "500.5");
    relQueryObject1.setValue("relQueryDecimal", "500.5");
    relQueryObject1.setValue("relQueryDouble", "500.5");
    relQueryObject1.apply();

    connectionInstance1 = relQueryObject1.addParent(testQueryObject1.getOid(), connectionMdRel.definesType());

    connectionInstance1.addItem("conQueryEnumeration", californiaItemId);
    connectionInstance1.setValue("conQueryBoolean", MdAttributeBooleanInfo.TRUE);
    connectionInstance1.setValue("conQueryCharacter", "con character value");
    connectionInstance1.setValue("conQueryText", "con text value");
    connectionInstance1.setValue("conQueryClob", "con clob value");
    connectionInstance1.setValue("conQueryDateTime", "2009-12-06 13:00:00");
    connectionInstance1.setValue("conQueryDate", "2009-12-06");
    connectionInstance1.setValue("conQueryTime", "13:00:00");
    connectionInstance1.setValue("conQueryInteger", "400");
    connectionInstance1.setValue("conQueryLong", "400");
    connectionInstance1.setValue("conQueryFloat", "400.5");
    connectionInstance1.setValue("conQueryDecimal", "400.5");
    connectionInstance1.setValue("conQueryDouble", "400.5");

    connectionInstance1.apply();

    // define the metadata for the entity to use in attribute comparison
    mdView = MdViewDAO.newInstance();
    mdView.setValue(MdViewInfo.NAME, viewQueryInfo.getTypeName());
    mdView.setValue(MdViewInfo.PACKAGE, viewQueryInfo.getPackageName());
    mdView.setValue(MdViewInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdView.setStructValue(MdViewInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A test View");
    mdView.setStructValue(MdViewInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "A test View class used for testing for querying for views.");
    mdView.setValue(MdViewInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
    mdView.setValue(MdViewInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);

    String childQueryClass = EntityQueryAPIGenerator.getQueryClass(childMdBusiness.definesType());
    String refChildQueryClass = EntityQueryAPIGenerator.getQueryClass(childRefMdBusiness.definesType());

    String queryBooleanConst = TypeGenerator.buildAttributeConstant(mdView, "queryBoolean");
    String queryCharacterConst = TypeGenerator.buildAttributeConstant(mdView, "queryCharacter");

    String charFromLocalCharConst = TypeGenerator.buildAttributeConstant(mdView, "charFromLocalChar");

    String queryTextConst = TypeGenerator.buildAttributeConstant(mdView, "refQueryText");
    String queryClobConst = TypeGenerator.buildAttributeConstant(mdView, "refQueryClob");
    String queryIntegerConst = TypeGenerator.buildAttributeConstant(mdView, "refQueryInteger");

    String queryStubSource = "package temporary.junit.test; \n" + "\n" + "public class TestViewQuery extends temporary.junit.test.TestViewQueryBase \n" + "{\n" + "\n" + "  private " + childQueryClass + " childQuery;\n" + "  private " + refChildQueryClass + " refChildQuery;\n" + "\n" + "  public TestViewQuery(" + QueryFactory.class.getName() + " componentQueryFactory)\n" + "  {\n" + "     super(componentQueryFactory);\n" + "     \n" + "     childQuery = new " + childQueryClass + "(componentQueryFactory);\n" + "     refChildQuery = new " + refChildQueryClass + "(componentQueryFactory);\n" + "\n" + "     this.map(" + queryBooleanConst + ", childQuery.getQueryBoolean());\n" + "     this.map(" + queryCharacterConst + ", childQuery.getQueryCharacter());\n" + "     this.map(" + queryTextConst
        + ", childQuery.getReference().getRefQueryText());\n" + "     this.map(" + queryClobConst + ", childQuery.getReference().getRefQueryClob());\n" + "     this.map(" + queryIntegerConst + ", childQuery.getReference().getRefQueryInteger());\n" + "     this.map(" + charFromLocalCharConst + ", childQuery.getQueryLocalChar().localize());\n" + "\n" + "     this.buildSelectClause();\n" + "  }\n" + "}\n";
    mdView.setValue(MdViewInfo.QUERY_STUB_SOURCE, queryStubSource);

    mdView.apply();

    loadAttributePrimitives(mdView, PARENT_PREFIX);
    loadAttributePrimitives(mdView, REF_PREFIX);

    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, "charFromLocalChar");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, Integer.toString(MdAttributeCharacterInfo.MAX_CHARACTER_SIZE));
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Char From Local Char");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Query me");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdView.getOid());
    mdAttributeCharacter.apply();

  }

  @Request
  @After
  public void tearDown() throws Exception
  {
    this.tearDownTransaction();
  }

  @Transaction
  public void tearDownTransaction()
  {
    TestFixtureFactory.delete(compareMdBusiness);
    TestFixtureFactory.delete(connectionMdRel);
    TestFixtureFactory.delete(relMdBusiness);
    TestFixtureFactory.delete(childRefMdBusiness);
    TestFixtureFactory.delete(parentRefMdBusiness);
    TestFixtureFactory.delete(childMdBusiness);
    TestFixtureFactory.delete(parentMdBusiness);
    TestFixtureFactory.delete(mdStruct);
    TestFixtureFactory.delete(stateMdEnumeration_east);
    TestFixtureFactory.delete(stateMdEnumeration_west);
    TestFixtureFactory.delete(stateMdEnumeration_all);
    TestFixtureFactory.delete(stateEnumMdBusiness);
    TestFixtureFactory.delete(mdView);
    TestFixtureFactory.delete(mdTerm);
  }

  public static void loadAttributePrimitives(MdClassDAOIF mdClassIF, String prefix)
  {
    MdAttributeBooleanDAO mdAttributeBoolean = MdAttributeBooleanDAO.newInstance();
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.NAME, prefix + "Boolean");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A boolean.");
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.POSITIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setStructValue(MdAttributeBooleanInfo.NEGATIVE_DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeBoolean.setValue(MdAttributeBooleanInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeBoolean.apply();

    MdAttributeCharacterDAO mdAttributeCharacter = MdAttributeCharacterDAO.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.NAME, prefix + "Character");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.SIZE, "25");
    mdAttributeCharacter.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A character.");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "Query me");
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeCharacter.apply();

    MdAttributeTextDAO mdAttributeText = MdAttributeTextDAO.newInstance();
    mdAttributeText.setValue(MdAttributeTextInfo.NAME, prefix + "Text");
    mdAttributeText.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A text");
    mdAttributeText.setValue(MdAttributeTextInfo.DEFAULT_VALUE, "");
    mdAttributeText.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeText.setValue(MdAttributeTextInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeText.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeText.apply();

    MdAttributeClobDAO mdAttributeClob = MdAttributeClobDAO.newInstance();
    mdAttributeClob.setValue(MdAttributeTextInfo.NAME, prefix + "Clob");
    mdAttributeClob.setStructValue(MdAttributeTextInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A clob");
    mdAttributeClob.setValue(MdAttributeTextInfo.DEFAULT_VALUE, "");
    mdAttributeClob.setValue(MdAttributeTextInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeClob.setValue(MdAttributeTextInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeClob.setValue(MdAttributeTextInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeClob.apply();

    MdAttributeDateTimeDAO mdAttributeDateTime = MdAttributeDateTimeDAO.newInstance();
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.NAME, prefix + "DateTime");
    mdAttributeDateTime.setStructValue(MdAttributeDateTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A datetime");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFAULT_VALUE, "");
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDateTime.setValue(MdAttributeDateTimeInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeDateTime.apply();

    MdAttributeDateDAO mdAttributeDate = MdAttributeDateDAO.newInstance();
    mdAttributeDate.setValue(MdAttributeDateInfo.NAME, prefix + "Date");
    mdAttributeDate.setStructValue(MdAttributeDateInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A date");
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFAULT_VALUE, "");
    mdAttributeDate.setValue(MdAttributeDateInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDate.setValue(MdAttributeDateInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDate.setValue(MdAttributeDateInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeDate.apply();

    MdAttributeTimeDAO mdAttributeTime = MdAttributeTimeDAO.newInstance();
    mdAttributeTime.setValue(MdAttributeTimeInfo.NAME, prefix + "Time");
    mdAttributeTime.setStructValue(MdAttributeTimeInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A time");
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFAULT_VALUE, "");
    mdAttributeTime.setValue(MdAttributeTimeInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeTime.setValue(MdAttributeTimeInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeTime.apply();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, prefix + "Integer");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "An integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeInteger.apply();

    MdAttributeLongDAO mdAttributeLong = MdAttributeLongDAO.newInstance();
    mdAttributeLong.setValue(MdAttributeLongInfo.NAME, prefix + "Long");
    mdAttributeLong.setStructValue(MdAttributeLongInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A long");
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFAULT_VALUE, "");
    mdAttributeLong.setValue(MdAttributeLongInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeLong.setValue(MdAttributeLongInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeLong.setValue(MdAttributeLongInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeLong.apply();

    MdAttributeFloatDAO mdAttributeFloat = MdAttributeFloatDAO.newInstance();
    mdAttributeFloat.setValue(MdAttributeFloatInfo.NAME, prefix + "Float");
    mdAttributeFloat.setStructValue(MdAttributeFloatInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A float to query.");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFAULT_VALUE, "");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.LENGTH, "10");
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DECIMAL, NUM_OF_FLOAT_DECIMALS);
    mdAttributeFloat.setValue(MdAttributeFloatInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeFloat.apply();

    MdAttributeDecimalDAO mdAttributeDecimal = MdAttributeDecimalDAO.newInstance();
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.NAME, prefix + "Decimal");
    mdAttributeDecimal.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "A decimal");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFAULT_VALUE, "");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.LENGTH, "10");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DECIMAL, "2");
    mdAttributeDecimal.setValue(MdAttributeDecimalInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeDecimal.apply();

    MdAttributeDoubleDAO mdAttributeDouble = MdAttributeDoubleDAO.newInstance();
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.NAME, prefix + "Double");
    mdAttributeDouble.setStructValue(MdAttributeDecimalInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Double");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFAULT_VALUE, "");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.LENGTH, "10");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DECIMAL, "2");
    mdAttributeDouble.setValue(MdAttributeDoubleInfo.DEFINING_MD_CLASS, mdClassIF.getOid());
    mdAttributeDouble.apply();
  }

  /**
   * Returns a reflection array of the enumeration constants that have the given
   * name strings.
   * 
   * @param enumClass
   *          Class object of the enum.
   * @param enumConstants
   *          Array of enumeration constant objects.
   * @param enumNames
   *          String names of enumeartion constants.
   * @return reflection array of the enumeration constants that have the given
   *         name strings.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected static Object getStateEnumConstants(Class enumClass, Object[] enumConstants, String... enumNames)
  {
    Object array = Array.newInstance(enumClass, enumNames.length);
    try
    {
      int loopMatchCount = 0;

      for (String enumName : enumNames)
      {
        for (Object enumConstant : enumConstants)
        {
          String constantEnumName = (String) enumClass.getMethod("name").invoke(enumConstant);

          if (constantEnumName.equals(enumName))
          {
            Array.set(array, loopMatchCount, enumConstant);
            loopMatchCount++;
          }
        }
      }
    }
    catch (Exception e)
    {
      Assert.fail(e.getMessage());
    }
    return array;
  }
}
