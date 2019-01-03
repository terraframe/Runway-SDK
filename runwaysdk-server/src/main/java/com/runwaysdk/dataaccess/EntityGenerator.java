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
package com.runwaysdk.dataaccess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.runwaysdk.RunwayException;
import com.runwaysdk.constants.CommonProperties;
import com.runwaysdk.constants.EntityInfo;
import com.runwaysdk.dataaccess.metadata.MdElementDAO;
import com.runwaysdk.dataaccess.metadata.MdRelationshipDAO;


/**
 *
 *
 * @author Eric Grunzke
 */
public class EntityGenerator
{
  private static Random random;

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException
  {
    // TODO remove all test like references from this class (i.e., MasterTestSetup)
    /*
    // Create an new class (MasterTestSetup.REFERENCE_CLASS) that we can reference with Reference Attributes
    MdBusiness referenceMdBusiness = MdBusiness.newInstance();
    referenceMdBusiness.setValue(MdBusinessIF.NAME,                   MasterTestSetup.REFERENCE_CLASS.getTypeName());
    referenceMdBusiness.setValue(MdBusinessIF.PACKAGE,                MasterTestSetup.REFERENCE_CLASS.getPackageName());
    referenceMdBusiness.setValue(MdBusinessIF.REMOVE,                 MdAttributeBooleanIF.TRUE);
    referenceMdBusiness.setValue(MdBusinessIF.DISPLAY_LABEL,          "JUnitRefType");
    referenceMdBusiness.setValue(MdBusinessIF.DESCRIPTION,            "JUnit Reference Type");
    referenceMdBusiness.setValue(MdBusinessIF.EXTENDABLE,             MdAttributeBooleanIF.TRUE);
    referenceMdBusiness.setValue(MdBusinessIF.ABSTRACT,               MdAttributeBooleanIF.FALSE);
    referenceMdBusiness.apply();

    // Add an attribute to the reference type
    MdAttributeCharacter refMdAttributeCharacter = MdAttributeCharacter.newInstance();
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.NAME,               "refChar");
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.SIZE,               "32");
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,      "A string");
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,      "I wish I was a reference field!");
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    refMdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY, referenceMdBusiness.getOid());
    refMdAttributeCharacter.apply();

    // Create the TEST class.
    MdBusiness testMdBusiness = MdBusiness.newInstance();
    testMdBusiness.setValue(MdBusinessIF.NAME,                   MasterTestSetup.TEST_CLASS.getTypeName());
    testMdBusiness.setValue(MdBusinessIF.PACKAGE,                MasterTestSetup.TEST_CLASS.getPackageName());
    testMdBusiness.setValue(MdBusinessIF.REMOVE,                 MdAttributeBooleanIF.TRUE);
    testMdBusiness.setValue(MdBusinessIF.DISPLAY_LABEL,          "JUnit Test Type");
    testMdBusiness.setValue(MdBusinessIF.DESCRIPTION,            "Temporary JUnit Test Type");
    testMdBusiness.setValue(MdBusinessIF.EXTENDABLE,             MdAttributeBooleanIF.TRUE);
    testMdBusiness.setValue(MdBusinessIF.ABSTRACT,               MdAttributeBooleanIF.FALSE);
    testMdBusiness.setValue(MdBusinessIF.CACHE_ALGORITHM, EntityCache.NOTHING.getOid());
    testMdBusiness.apply();

    // Add atributes to the test type
    MdAttributeCharacter mdAttributeCharacter = MdAttributeCharacter.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.NAME,                TestFixtureConstants.ATTRIBUTE_CHARACTER);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.SIZE,                "16");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,       "Required Character Length 16");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,       "Yo diggity");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REQUIRED,            MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.addItem(MdAttributeCharacterIF.INDEX_TYPE,           IndexTypes.UNIQUE_INDEX.getOid());
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY,  testMdBusiness.getOid());
    mdAttributeCharacter.apply();

    mdAttributeCharacter = MdAttributeCharacter.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.NAME,                "testCharacterChangeSize");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.SIZE,                "16");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,       "Required Character Length 16, but change size to 32");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,       "Yo diggity dog");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REQUIRED,            MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY,  testMdBusiness.getOid());
    mdAttributeCharacter.apply();

    mdAttributeCharacter = MdAttributeCharacter.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.NAME,                "testChar64");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.SIZE,                "36");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,       "Character Length 64");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,       "");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REQUIRED,            MdAttributeBooleanIF.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY,  testMdBusiness.getOid());
    mdAttributeCharacter.apply();

    MdAttributeText mdAttributeText = MdAttributeText.newInstance();
    mdAttributeText.setValue(MdAttributeTextIF.NAME,                "testText");
    mdAttributeText.setValue(MdAttributeTextIF.DISPLAY_LABEL,       "Text field");
    mdAttributeText.setValue(MdAttributeTextIF.DEFAULT_VALUE,       "");
    mdAttributeText.setValue(MdAttributeTextIF.REQUIRED,            MdAttributeBooleanIF.FALSE);
    mdAttributeText.setValue(MdAttributeTextIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttributeText.setValue(MdAttributeTextIF.DEFINING_MD_ENTITY,  testMdBusiness.getOid());
    mdAttributeText.apply();

    MdAttributeInteger mdAttributeInteger = MdAttributeInteger.newInstance();
    mdAttributeInteger.setValue(MdAttributeIF.NAME,               "testInteger");
    mdAttributeInteger.setValue(MdAttributeIF.DISPLAY_LABEL,      "Integer");
    mdAttributeInteger.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeInteger.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeInteger.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeInteger.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeInteger.apply();

    MdAttributeLong mdAttributeLong = MdAttributeLong.newInstance();
    mdAttributeLong.setValue(MdAttributeIF.NAME,               "testLong");
    mdAttributeLong.setValue(MdAttributeIF.DISPLAY_LABEL,      "Long");
    mdAttributeLong.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeLong.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeLong.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeLong.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeLong.apply();

    MdAttributeFloat mdAttributeFloat = MdAttributeFloat.newInstance();
    mdAttributeFloat.setValue(MdAttributeDecIF.NAME,               "testFloat");
    mdAttributeFloat.setValue(MdAttributeDecIF.DISPLAY_LABEL,      "Float");
    mdAttributeFloat.setValue(MdAttributeDecIF.DEFAULT_VALUE,      "");
    mdAttributeFloat.setValue(MdAttributeDecIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeFloat.setValue(MdAttributeDecIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeFloat.setValue(MdAttributeDecIF.LENGTH,             "10");
    mdAttributeFloat.setValue(MdAttributeDecIF.DECIMAL,            "2");
    mdAttributeFloat.setValue(MdAttributeDecIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeFloat.apply();

    MdAttributeDecimal mdAttributeDecimal = MdAttributeDecimal.newInstance();
    mdAttributeDecimal.setValue(MdAttributeDecIF.NAME,               "testDecimal");
    mdAttributeDecimal.setValue(MdAttributeDecIF.DISPLAY_LABEL,      "Decimal");
    mdAttributeDecimal.setValue(MdAttributeDecIF.DEFAULT_VALUE,      "");
    mdAttributeDecimal.setValue(MdAttributeDecIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeDecimal.setValue(MdAttributeDecIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeDecimal.setValue(MdAttributeDecIF.LENGTH,             "13");
    mdAttributeDecimal.setValue(MdAttributeDecIF.DECIMAL,            "3");
    mdAttributeDecimal.setValue(MdAttributeDecIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeDecimal.apply();

    MdAttributeDouble mdAttributeDouble = MdAttributeDouble.newInstance();
    mdAttributeDouble.setValue(MdAttributeDecIF.NAME,               "testDouble");
    mdAttributeDouble.setValue(MdAttributeDecIF.DISPLAY_LABEL,      "Double");
    mdAttributeDouble.setValue(MdAttributeDecIF.DEFAULT_VALUE,      "");
    mdAttributeDouble.setValue(MdAttributeDecIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeDouble.setValue(MdAttributeDecIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeDouble.setValue(MdAttributeDecIF.LENGTH,             "16");
    mdAttributeDouble.setValue(MdAttributeDecIF.DECIMAL,            "4");
    mdAttributeDouble.setValue(MdAttributeDecIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeDouble.apply();

    MdAttributeTime mdAttributeTime = MdAttributeTime.newInstance();
    mdAttributeTime.setValue(MdAttributeIF.NAME,               "testTime");
    mdAttributeTime.setValue(MdAttributeIF.DISPLAY_LABEL,      "Time (HH:MM:SS)");
    mdAttributeTime.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeTime.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeTime.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeTime.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeTime.apply();

    MdAttributeDate mdAttributeDate = MdAttributeDate.newInstance();
    mdAttributeDate.setValue(MdAttributeIF.NAME,               "testDate");
    mdAttributeDate.setValue(MdAttributeIF.DISPLAY_LABEL,      "Date (YYYY-MM-DD)");
    mdAttributeDate.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeDate.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeDate.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeDate.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeDate.apply();

    MdAttributeDateTime mdAttributeDateTime = MdAttributeDateTime.newInstance();
    mdAttributeDateTime.setValue(MdAttributeIF.NAME,               "testDateTime");
    mdAttributeDateTime.setValue(MdAttributeIF.DISPLAY_LABEL,      "DateTime (YYYY-MM-DD HH:MM:SS)");
    mdAttributeDateTime.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeDateTime.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeDateTime.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeDateTime.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeDateTime.apply();

    MdAttributeBoolean mdAttributeBoolean = MdAttributeBoolean.newInstance();
    mdAttributeBoolean.setValue(MdAttributeIF.NAME,               TestFixConst.ATTRIBUTE_BOOLEAN);
    mdAttributeBoolean.setValue(MdAttributeIF.DISPLAY_LABEL,      "Our first Boolean");
    mdAttributeBoolean.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeBoolean.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeBoolean.setValue(MdAttributeIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeBoolean.apply();

    mdAttributeCharacter = MdAttributeCharacter.newInstance();
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.NAME,               "testImmutable");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.SIZE,               "32");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,      "An Immutable String");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,      "You can't change this");
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.IMMUTABLE,          MdAttributeBooleanIF.TRUE);
    mdAttributeCharacter.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeCharacter.apply();

    MdAttributeReference mdAttributeReference = MdAttributeReference.newInstance();
    mdAttributeReference.setValue(MdAttributeReferenceIF.NAME,               "testReference");
    mdAttributeReference.setValue(MdAttributeReferenceIF.DISPLAY_LABEL,      "A Reference");
    mdAttributeReference.setValue(MdAttributeReferenceIF.DEFAULT_VALUE,      "");
    mdAttributeReference.setValue(MdAttributeReferenceIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeReference.setValue(MdAttributeReferenceIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeReference.setValue(MdAttributeReferenceIF.REF_MD_CLASS,       referenceMdBusiness.getOid());
    mdAttributeReference.setValue(MdAttributeReferenceIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttributeReference.apply();

    String stateClassTypeName = "US_State";
    String stateClassPackage = "com.test";

    String stateEnumTypeName = "US_State_Enum";
    String stateEnumPackage = "com.test";

    MdBusinessIF EnumerationAttributeMdBusinessIF = MdBusiness.getMdBusiness(ClassTypes.ENUMERATION_MASTER.getType());

    // New Type (STATE) extends Enumeration_Attribute
    MdBusiness stateEnumMdBusiness = MdBusiness.newInstance();
    stateEnumMdBusiness.setValue(MdBusinessIF.NAME,             stateClassTypeName);
    stateEnumMdBusiness.setValue(MdBusinessIF.PACKAGE,          stateClassPackage);
    stateEnumMdBusiness.setValue(MdBusinessIF.REMOVE,           MdAttributeBooleanIF.TRUE);
    stateEnumMdBusiness.setValue(MdBusinessIF.DISPLAY_LABEL,    "State");
    stateEnumMdBusiness.setValue(MdBusinessIF.DESCRIPTION,      "States of the Union");
    stateEnumMdBusiness.setValue(MdBusinessIF.EXTENDABLE,       MdAttributeBooleanIF.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessIF.ABSTRACT,         MdAttributeBooleanIF.FALSE);
    stateEnumMdBusiness.setValue(MdBusinessIF.SUPER_MD_ENTITY,  EnumerationAttributeMdBusinessIF.getOid());
    stateEnumMdBusiness.apply();

    // Instantiate an md_enumeration to define State
    MdEnumeration stateMdEnumeration = MdEnumeration.newInstance();
    stateMdEnumeration.setValue(MdEnumerationIF.NAME,                stateEnumTypeName);
    stateMdEnumeration.setValue(MdEnumerationIF.PACKAGE,             stateEnumPackage);
    stateMdEnumeration.setValue(MdEnumerationIF.DISPLAY_LABEL,       "All States in the United States");
    stateMdEnumeration.setValue(MdEnumerationIF.DESCRIPTION,         "Test");
    stateMdEnumeration.setValue(MdEnumerationIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    stateMdEnumeration.setValue(MdEnumerationIF.INCLUDE_ALL,         MdAttributeBooleanIF.TRUE);
    stateMdEnumeration.setValue(MdEnumerationIF.ATTRIBUTE_MD_CLASS,  stateEnumMdBusiness.getOid());
    stateMdEnumeration.apply();

    // Define attributes on the enumeration
    MdAttributeCharacter mdAttrChar = MdAttributeCharacter.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterIF.NAME,               "stateCode");
    mdAttrChar.setValue(MdAttributeCharacterIF.SIZE,               "2");
    mdAttrChar.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,      "State Postal Code");
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,      "");
    mdAttrChar.setValue(MdAttributeCharacterIF.REQUIRED,           MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.INDEX_TYPE,         IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY, stateEnumMdBusiness.getOid());
    mdAttrChar.apply();

    mdAttrChar = MdAttributeCharacter.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterIF.NAME,               "stateName");
    mdAttrChar.setValue(MdAttributeCharacterIF.SIZE,               "32");
    mdAttrChar.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,      "State Name");
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFAULT_VALUE,      "");
    mdAttrChar.setValue(MdAttributeCharacterIF.REQUIRED,           MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.INDEX_TYPE,         IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY, stateEnumMdBusiness.getOid());
    mdAttrChar.apply();

    mdAttributeInteger = MdAttributeInteger.newInstance();
    mdAttributeInteger.setValue(MdAttributeIF.NAME,               "enumInt");
    mdAttributeInteger.setValue(MdAttributeIF.DISPLAY_LABEL,      "Enumeration Integer");
    mdAttributeInteger.setValue(MdAttributeIF.DEFAULT_VALUE,      "");
    mdAttributeInteger.setValue(MdAttributeIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttributeInteger.setValue(MdAttributeIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttributeInteger.setValue(MdAttributeIF.DEFINING_MD_ENTITY, stateEnumMdBusiness.getOid());
    mdAttributeInteger.apply();

    // Define the options for the enumeration
    EntityDAO entityDAO = EntityDAO.newInstance(stateClassTypeName+"."+stateClassPackage);
    entityDAO.setValue("enumInt", "1");
    entityDAO.setValue("stateCode", "CA");
    entityDAO.setValue("stateName", "California");
    entityDAO.apply();

    entityDAO = EntityDAO.newInstance(stateClassTypeName+"."+stateClassPackage);
    entityDAO.setValue("enumInt", "2");
    entityDAO.setValue("stateCode", "CO");
    entityDAO.setValue("stateName", "Colorado");
    entityDAO.apply();

    entityDAO = EntityDAO.newInstance(stateClassTypeName+"."+stateClassPackage);
    entityDAO.setValue("enumInt", "3");
    entityDAO.setValue("stateCode", "CT");
    entityDAO.setValue("stateName", "Connecticut");
    entityDAO.apply();

    // Add the enumeration as a multi-select Attribute on the TEST type
    MdAttributeEnumeration mdAttrEnum = MdAttributeEnumeration.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationIF.NAME,               "testMultiEnum");
    mdAttrEnum.setValue(MdAttributeEnumerationIF.DISPLAY_LABEL,      "Multiple select state attribute");
    mdAttrEnum.setValue(MdAttributeEnumerationIF.REQUIRED,           MdAttributeBooleanIF.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationIF.MD_ENUMERATION,     stateMdEnumeration.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationIF.SELECT_MULTIPLE, MdAttributeBooleanIF.TRUE);
    String multiAttrMdID = mdAttrEnum.apply();

    // Add the enumeration as a single-select Attribute on the TEST type
    mdAttrEnum = MdAttributeEnumeration.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationIF.NAME,               "testSingleEnum");
    mdAttrEnum.setValue(MdAttributeEnumerationIF.DISPLAY_LABEL,      "Single select state attribute");
    mdAttrEnum.setValue(MdAttributeEnumerationIF.REQUIRED,           MdAttributeBooleanIF.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationIF.MD_ENUMERATION,     stateMdEnumeration.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationIF.SELECT_MULTIPLE,    MdAttributeBooleanIF.FALSE);
    String singleAttrMdID = mdAttrEnum.apply();

    MdBusiness newStructMdBusiness = MdBusiness.newInstance();
    newStructMdBusiness.setValue(MdBusinessIF.NAME,               "Address");
    newStructMdBusiness.setValue(MdBusinessIF.PACKAGE,            "temporary.test");
    newStructMdBusiness.setValue(MdBusinessIF.REMOVE,             MdAttributeBooleanIF.TRUE);
    newStructMdBusiness.setValue(MdBusinessIF.DISPLAY_LABEL,      "Address");
    newStructMdBusiness.setValue(MdBusinessIF.DESCRIPTION,        "Address");
    newStructMdBusiness.setValue(MdBusinessIF.EXTENDABLE,         MdAttributeBooleanIF.FALSE);
    newStructMdBusiness.setValue(MdBusinessIF.ABSTRACT,           MdAttributeBooleanIF.FALSE);
    newStructMdBusiness.apply();

    mdAttrChar = MdAttributeCharacter.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterIF.NAME,                "streetName");
    mdAttrChar.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,       "Street Name");
    mdAttrChar.setValue(MdAttributeCharacterIF.REQUIRED,            MdAttributeBooleanIF.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY,  newStructMdBusiness.getOid());
    mdAttrChar.setValue(MdAttributeCharacterIF.SIZE,                "32");
    mdAttrChar.apply();

    mdAttrChar = MdAttributeCharacter.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterIF.NAME,                "houseNumber");
    mdAttrChar.setValue(MdAttributeCharacterIF.DISPLAY_LABEL,       "House Number");
    mdAttrChar.setValue(MdAttributeCharacterIF.REQUIRED,            MdAttributeBooleanIF.FALSE);
    mdAttrChar.setValue(MdAttributeCharacterIF.REMOVE,              MdAttributeBooleanIF.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterIF.DEFINING_MD_ENTITY,  newStructMdBusiness.getOid());
    mdAttrChar.setValue(MdAttributeCharacterIF.SIZE,                "8");
    mdAttrChar.apply();

    MdAttributeStruct mdAttrStruct = MdAttributeStruct.newInstance();
    mdAttrStruct.setValue(MdAttributeStructIF.NAME,              "address");
    mdAttrStruct.setValue(MdAttributeStructIF.DISPLAY_LABEL,     "Address");
    mdAttrStruct.setValue(MdAttributeStructIF.REQUIRED,          MdAttributeBooleanIF.FALSE);
    mdAttrStruct.setValue(MdAttributeStructIF.REMOVE,            MdAttributeBooleanIF.TRUE);
    mdAttrStruct.setValue(MdAttributeStructIF.DEFINING_MD_ENTITY, testMdBusiness.getOid());
    mdAttrStruct.setValue(MdAttributeStructIF.REF_MD_CLASS_BASIC, newStructMdBusiness.getOid());
    mdAttrStruct.apply();

    generate(MasterTestSetup.REFERENCE_CLASS.getType(), 5);
    long before = System.currentTimeMillis();
    generate(MasterTestSetup.TEST_CLASS.getType(), 20);
    long after = System.currentTimeMillis();

    System.out.print("Done in " + (after-before) + " ms!  Press Enter to Delete...");
    InputStreamReader inStream = new InputStreamReader(System.in);
    BufferedReader stdin = new BufferedReader(inStream);
    stdin.readLine();

    inStream.close();
    stdin.close();

    before = System.currentTimeMillis();
    newStructMdBusiness.getEntityDAO().delete();
    EntityDAO.get(singleAttrMdID).getEntityDAO().delete();
    EntityDAO.get(multiAttrMdID).getEntityDAO().delete();
    stateMdEnumeration.getEntityDAO().delete();
    stateEnumMdBusiness.getEntityDAO().delete();
    MdBusiness.getMdBusiness(MasterTestSetup.TEST_CLASS.getType()).getEntityDAO().delete();
    MdBusiness.getMdBusiness(MasterTestSetup.REFERENCE_CLASS.getType()).getEntityDAO().delete();
    after = System.currentTimeMillis();
    System.out.println("Deleted in " + (after-before) + " ms.");
    */
  }

  /**
   * Generates random instances of a given class in the system. The number of
   * instances and the class are passed in as parameters. All attributes on
   * generated instances will have random values. It is important to note that,
   * inter-class dependencies (IE Reference fields and reference obejcts) are
   * <b>not</b> checked, and must be managed by the caller. For example,
   * generating instances of class A that has a reference field that points to
   * class B <b>will</b> crash if no instances of B exist. Thus, it is the
   * responsibility of the caller to ensure that at least one instance of B is
   * live in the system for the instances of A to point to.
   *
   * @param entityType type of the entity
   * @param instances
   *          The number of instances to generate
   * @return A List of IDs of every generated instance
   */
  public static List<String> generate(String entityType, int instances)
  {
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(entityType);
    List<String> ids = new ArrayList<String>(instances);
    List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdEntityIF.getAllDefinedMdAttributes();

    boolean isRootOfHierarchy = mdEntityIF.isRootOfHierarchy();
    for (int i=0; i<instances; i++)
    {
      BusinessDAO entity = BusinessDAO.newInstance(entityType);
      for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
      {
        if ((mdAttribute.isSystem())
        || (mdAttribute.definesAttribute().equalsIgnoreCase(EntityInfo.OID) && !isRootOfHierarchy ))
          continue;
        mdAttribute.setRandomValue(entity);
      }
      try
      {
        ids.add(entity.apply());
      }
      catch (RunwayException e)
      {
        System.out.println("Caught exception - recreating with new random values. Exception: ");
        System.out.println(e.toString());
        i--;
      }
    }
    return ids;
  }
  public static void generateRelationships(String relationshipType, int instances)
  {
    generateRelationships(MdRelationshipDAO.getMdRelationshipDAO(relationshipType), instances);
  }

  public static void generateRelationships(MdRelationshipDAOIF mdRelationshipIF, int instances)
  {
    // Get the child Cardinality - convert '*' to infinity
    float childCardinality;
    if (mdRelationshipIF.getChildCardinality().equals("*"))
      childCardinality = Float.POSITIVE_INFINITY;
    else
      childCardinality = Integer.parseInt(mdRelationshipIF.getChildCardinality());

    // Get the parent Cardinality - convert '*' to infinity
    float parentCardinality;
    if (mdRelationshipIF.getParentCardinality().equals("*"))
      parentCardinality = Float.POSITIVE_INFINITY;
    else
      parentCardinality = Integer.parseInt(mdRelationshipIF.getParentCardinality());

    String relationshipType = mdRelationshipIF.definesType();

    MdBusinessDAOIF parentMd = mdRelationshipIF.getParentMdBusiness();
    MdBusinessDAOIF childMd = mdRelationshipIF.getChildMdBusiness();

    List<String> parentOids = EntityDAO.getEntityIdsFromDB(parentMd);
    List<String> childOids = EntityDAO.getEntityIdsFromDB(childMd);

    // Make sure that the number of requested instances isn't too high
    if (parentOids.size() * childCardinality < instances ||
        childOids.size() * parentCardinality < instances)
    {
      String error = "There are not enough instances of the part or child classes to generate "
          + instances + " [" + mdRelationshipIF.getDisplayLabel(CommonProperties.getDefaultLocale() ) + "] relationship instances.";
      throw new CoreException(error);
    }

    // We reassign parentCardinality so that * to * relationships don't all have the same parent
    if (parentCardinality==Float.POSITIVE_INFINITY)
      parentCardinality=1;
    int created = 0;
    Iterator<String> parentIterator = new LinkedList<String>().iterator();

    // Keep iterating until we get enough instances.
    while (true)
    {
      // Iterating over all child ids ensures an even distribution of relationships
      for (String childOid : childOids)
      {
        for (int i = 0; i < parentCardinality; i++)
        {
          // The cardinality and number of parents are unrelated. If the iterator
          // is at the end, shuffle the parent list and restart the iterator.
          if (!parentIterator.hasNext())
          {
            Collections.shuffle(parentOids);
            parentIterator = parentOids.iterator();
          }
          RelationshipDAO relationship = RelationshipDAO.newInstance(parentIterator.next(), childOid, relationshipType);
          try
          {
            // FIXME forced uniqueness on parentOid-childOid
            generateInstance(relationship).apply();
          }
          catch (Exception e) {}

          // Once we have enough instances, break out
          if (++created >= instances)
            return;
        }
      }
    }
  }

  public static BusinessDAO generateInstance(String classType)
  {
    BusinessDAO object = BusinessDAO.newInstance(classType);

    return (BusinessDAO) generateInstance(object);
  }

  private static EntityDAO generateInstance(EntityDAO object)
  {
    String entityType = object.getType();
    MdElementDAOIF mdEntityIF = MdElementDAO.getMdElementDAO(entityType);
    List<? extends MdAttributeConcreteDAOIF> mdAttributes = mdEntityIF.getAllDefinedMdAttributes();
    boolean isRootOfHierarchy = mdEntityIF.isRootOfHierarchy();
    for (MdAttributeConcreteDAOIF mdAttribute : mdAttributes)
    {
      if ((mdAttribute.isSystem())
      || (mdAttribute.definesAttribute().equalsIgnoreCase(EntityInfo.OID) && !isRootOfHierarchy ))
        continue;
      mdAttribute.setRandomValue(object);
    }
    return object;
  }

  private static void setUp()
  {
    random = new Random();
  }

  public static Random getRandom()
  {
    if (random==null) setUp();
    return random;
  }
}
