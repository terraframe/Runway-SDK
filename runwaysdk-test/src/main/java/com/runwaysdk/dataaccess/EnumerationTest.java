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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.runwaysdk.ProblemException;
import com.runwaysdk.constants.EnumerationMasterInfo;
import com.runwaysdk.constants.IndexTypes;
import com.runwaysdk.constants.MdAttributeBooleanInfo;
import com.runwaysdk.constants.MdAttributeCharacterInfo;
import com.runwaysdk.constants.MdAttributeEnumerationInfo;
import com.runwaysdk.constants.MdAttributeIntegerInfo;
import com.runwaysdk.constants.MdAttributeLocalInfo;
import com.runwaysdk.constants.MdAttributeStructInfo;
import com.runwaysdk.constants.MdBusinessInfo;
import com.runwaysdk.constants.MdEnumerationInfo;
import com.runwaysdk.constants.MdIndexInfo;
import com.runwaysdk.constants.MdStructInfo;
import com.runwaysdk.constants.TypeInfo;
import com.runwaysdk.dataaccess.attributes.AttributeValueException;
import com.runwaysdk.dataaccess.attributes.EmptyValueProblem;
import com.runwaysdk.dataaccess.attributes.InvalidReferenceException;
import com.runwaysdk.dataaccess.attributes.entity.AttributeEnumeration;
import com.runwaysdk.dataaccess.cache.DataNotFoundException;
import com.runwaysdk.dataaccess.database.Database;
import com.runwaysdk.dataaccess.io.TestFixtureFactory;
import com.runwaysdk.dataaccess.metadata.AttributeInvalidUniquenessConstraintException;
import com.runwaysdk.dataaccess.metadata.DuplicateMdEnumerationDefinitionException;
import com.runwaysdk.dataaccess.metadata.InheritanceException;
import com.runwaysdk.dataaccess.metadata.MdAttributeCharacterDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeIntegerDAO;
import com.runwaysdk.dataaccess.metadata.MdAttributeStructDAO;
import com.runwaysdk.dataaccess.metadata.MdBusinessDAO;
import com.runwaysdk.dataaccess.metadata.MdEnumerationDAO;
import com.runwaysdk.dataaccess.metadata.MdIndexDAO;
import com.runwaysdk.dataaccess.metadata.MdStructDAO;
import com.runwaysdk.dataaccess.metadata.MdTypeDAO;
import com.runwaysdk.session.Request;

public class EnumerationTest
{
  private static final TypeInfo            stateClass           = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State");

  private static final TypeInfo            stateEnum            = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "US_State_Enum");

  private static final TypeInfo            voltronClass         = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "VoltronLions");

  private static final TypeInfo            voltronMdEnumeration = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "VoltronForce");

  /**
   * <code>testObject</code> is a BusinessDAO that is mapped to a new instance
   * of the MasterTestSetup.TEST_CLASS class for each test. Enumerated
   * attributes are set and tested on it.
   */
  private BusinessDAO                      testObject;

  /**
   * MdBusinessIF instance that defines STATE.
   */
  private static MdBusinessDAOIF           stateEnumMdBusinessIF;

  /**
   * MdBusinessIF instance that defines the Voltron Lion master list.
   */
  private static MdBusinessDAOIF           voltronMdBusinessIF;

  /**
   * MdEnumerationIF instance that defines the Voltron Lion Enumeration.
   */
  private static MdEnumerationDAOIF        voltronMdEnumerationIF;

  /**
   * MdEnumerationIF instance that defines STATE.
   */
  private static MdEnumerationDAOIF        stateMdEnumerationIF;

  /**
   * MdStructIF instance that defines a struct used on a struct attribute.
   */
  private static MdStructDAOIF             mdStructIF;

  /**
   * MdAttributeStructIF that defines a struct attribute on the test class.
   */
  private static MdAttributeStructDAOIF    mdAttributeStructIF;

  /**
   * OID of the MD_ATTRIBUTE that describes the multi-select attribute
   */
  private static String                    multiAttrMdID;

  /**
   * OID of the MD_ATTRIBUTE that describes the single-select attribute
   */
  private static String                    singleAttrMdID;

  /**
   * The OID of the California option of the STATE enumeration.
   */
  private static String                    californiaItemId;

  /**
   * The OID of the Colorado option of the STATE enumeration.
   */
  private static String                    coloradoItemId;

  /**
   * The OID of the Connecticut option of the STATE enumeration.
   */
  private static String                    connecticutItemId;

  /**
   * Attribute name of the multiple select enumeration
   */
  private static final String              MULTIPLE             = "enumStateMultiple";

  /**
   * Metadata that defines of the multiple select enumeration.
   */
  private static MdAttributeEnumerationDAO mdAttrEnumMultiple;

  /**
   * Metadata that defines of the single select enumeration
   */
  private static MdAttributeEnumerationDAO mdAttrEnumSingle;

  /**
   * Attribute name of the single select enumeration
   */
  private static final String              SINGLE               = "enumStateSingle";

  /**
   * Provides setup operations required for all of the tests in this class.
   * Specifically, <code>classSetUp()</code> defines a new Enumerated Type
   * (STATE) and adds it as an attribute on the MasterTestSetup.TEST_CLASS type.
   */
  @Request
  @BeforeClass
  public static void classSetUp()
  {
    BusinessDAO businessDAO;
    MdAttributeCharacterDAO mdAttrChar;
    MdBusinessDAOIF testMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

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
    stateEnumMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
    stateEnumMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    stateEnumMdBusiness.apply();

    stateEnumMdBusinessIF = stateEnumMdBusiness;

    // Instantiate an md_enumeration to define State
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, stateEnum.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, stateEnum.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "All States in the United States");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusinessIF.getOid());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();
    stateMdEnumerationIF = mdEnumeration;

    // Define attributes on the enumeration
    mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "stateCode");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "2");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "State Postal Code");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getOid());
    mdAttrChar.apply();

    MdAttributeIntegerDAO mdAttributeInteger = MdAttributeIntegerDAO.newInstance();
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.NAME, "enumInt");
    mdAttributeInteger.setStructValue(MdAttributeIntegerInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Enumeration Integer");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFAULT_VALUE, "");
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttributeInteger.setValue(MdAttributeIntegerInfo.DEFINING_MD_CLASS, stateEnumMdBusiness.getOid());
    mdAttributeInteger.apply();

    // Define the options for the enumeration
    businessDAO = BusinessDAO.newInstance(stateClass.getType());
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

    // Add the enumeration as a multi-select Attribute on the TEST type
    mdAttrEnumMultiple = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.NAME, MULTIPLE);
    mdAttrEnumMultiple.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Multiple select state attribute");
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
    mdAttrEnumMultiple.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    multiAttrMdID = mdAttrEnumMultiple.apply();

    // Add the enumeration as a single-select Attribute on the TEST type
    mdAttrEnumSingle = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.NAME, SINGLE);
    mdAttrEnumSingle.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Single select state attribute");
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
    mdAttrEnumSingle.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    singleAttrMdID = mdAttrEnumSingle.apply();

    // Create a struct attribute with an enummeration on it.
    MdStructDAO mdStruct = MdStructDAO.newInstance();
    mdStruct.setValue(MdStructInfo.NAME, "SomeStruct");
    mdStruct.setValue(MdStructInfo.PACKAGE, "temporary.test");
    mdStruct.setValue(MdStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdStruct.setStructValue(MdStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Struct");
    mdStruct.setStructValue(MdStructInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Struct");
    mdStruct.setValue(MdStructInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdStruct.apply();
    mdStructIF = mdStruct;

    MdAttributeEnumerationDAO mdAttrEnumSingleBasic = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.NAME, SINGLE);
    mdAttrEnumSingleBasic.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Single select state attribute");
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdStruct.getOid());
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
    mdAttrEnumSingleBasic.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
    mdAttrEnumSingleBasic.apply();

    MdAttributeStructDAO mdAttrStruct = MdAttributeStructDAO.newInstance();
    mdAttrStruct.setValue(MdAttributeStructInfo.NAME, "someStruct");
    mdAttrStruct.setStructValue(MdAttributeStructInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some Struct");
    mdAttrStruct.setValue(MdAttributeStructInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrStruct.setValue(MdAttributeStructInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrStruct.setValue(MdAttributeStructInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
    mdAttrStruct.setValue(MdAttributeStructInfo.MD_STRUCT, mdStruct.getOid());
    mdAttrStruct.apply();
    mdAttributeStructIF = mdAttrStruct;
  }

  /**
   * Deletes the previously defined STATE Enumeration after all tests have
   * completed.
   */
  @Request
  @AfterClass
  public static void classTearDown()
  {
    BusinessDAO.get(singleAttrMdID).getBusinessDAO().delete();
    BusinessDAO.get(multiAttrMdID).getBusinessDAO().delete();

    TestFixtureFactory.delete(stateMdEnumerationIF);
    TestFixtureFactory.delete(stateEnumMdBusinessIF);
    TestFixtureFactory.delete(mdAttributeStructIF);
    TestFixtureFactory.delete(mdStructIF);
  }

  /**
   * Set the testObject to a new Instance of the MasterTestSetup.TEST_CLASS
   * class.
   */
  @Request
  @Before
  public void setUp() throws Exception
  {
    testObject = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
  }

  /**
   * If testObject was applied, it is removed from the database.
   * 
   * 
   */
  @Request
  @After
  public void tearDown() throws Exception
  {
    if (testObject != null && !testObject.isNew())
    {
      testObject.delete();
    }

    if (MdTypeDAO.isDefined(voltronClass.getType()))
    {
      TestFixtureFactory.delete(voltronMdBusinessIF);
    }

    if (MdTypeDAO.isDefined(voltronMdEnumeration.getType()))
    {
      TestFixtureFactory.delete(voltronMdEnumerationIF);
    }

  }

  /**
   * Creates an enumeration master list that is used by some of the test cases.
   * 
   */
  private void createVoltronMasterList()
  {
    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    // New Type (STATE) extends Enumeration_Attribute
    MdBusinessDAO voltronMdBusiness = MdBusinessDAO.newInstance();
    voltronMdBusiness.setValue(MdBusinessInfo.NAME, voltronClass.getTypeName());
    voltronMdBusiness.setValue(MdBusinessInfo.PACKAGE, voltronClass.getPackageName());
    voltronMdBusiness.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    voltronMdBusiness.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Voltron Lions");
    voltronMdBusiness.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Voltron Lion Force");
    voltronMdBusiness.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
    voltronMdBusiness.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
    voltronMdBusiness.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
    voltronMdBusiness.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    voltronMdBusiness.apply();
    voltronMdBusinessIF = voltronMdBusiness;

    // Define attributes on the enumeration
    MdAttributeCharacterDAO mdAttrChar = MdAttributeCharacterDAO.newInstance();
    mdAttrChar.setValue(MdAttributeCharacterInfo.NAME, "lionColor");
    mdAttrChar.setValue(MdAttributeCharacterInfo.SIZE, "16");
    mdAttrChar.setStructValue(MdAttributeCharacterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Name");
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFAULT_VALUE, "");
    mdAttrChar.setValue(MdAttributeCharacterInfo.REQUIRED, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.addItem(MdAttributeCharacterInfo.INDEX_TYPE, IndexTypes.NO_INDEX.getOid());
    mdAttrChar.setValue(MdAttributeCharacterInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrChar.setValue(MdAttributeCharacterInfo.DEFINING_MD_CLASS, voltronMdBusiness.getOid());
    mdAttrChar.apply();

    BusinessDAO businessDAO = BusinessDAO.newInstance(voltronClass.getType());
    businessDAO.setValue("lionColor", "Black");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "BLACK");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Black Lion");
    businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(voltronClass.getType());
    businessDAO.setValue("lionColor", "Red");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "RED");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Red Lion");
    businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(voltronClass.getType());
    businessDAO.setValue("lionColor", "Green");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "GREEN");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Green Lion");
    businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(voltronClass.getType());
    businessDAO.setValue("lionColor", "Blue");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "BLUE");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Blue Lion");
    businessDAO.apply();

    businessDAO = BusinessDAO.newInstance(voltronClass.getType());
    businessDAO.setValue("lionColor", "Yellow");
    businessDAO.setValue(EnumerationMasterInfo.NAME, "YELLOW");
    businessDAO.setStructValue(EnumerationMasterInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Yellow Lion");
    businessDAO.apply();
  }

  private void createVoltronMdEnumeration()
  {

    // Instantiate an md_enumeration to define State
    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, voltronMdEnumeration.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, voltronMdEnumeration.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Voltron Lion Force");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, voltronMdBusinessIF.getOid());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
    mdEnumeration.apply();
    voltronMdEnumerationIF = mdEnumeration;
  }

  /**
   * Ensure that everything works properly when an enumeration item is added to
   * an attribute on a struct that is used on a struct attribute of a class.
   * 
   * @throws Exception
   */
  @Request
  @Test
  public void testGetStructEnumeration() throws Exception
  {
    BusinessDAO businessDAO = BusinessDAO.newInstance(testObject.getType());
    businessDAO.addStructItem("someStruct", SINGLE, californiaItemId);
    String oid = businessDAO.apply();

    try
    {
      BusinessDAOIF entity = BusinessDAO.get(oid);
      StructDAOIF structDAO = ( (AttributeStructIF) entity.getAttributeIF("someStruct") ).getStructDAO();
      AttributeEnumerationIF attrEnumIF = (AttributeEnumerationIF) structDAO.getAttributeIF(SINGLE);

      // Make sure the enumeration item can be dereferenced.
      BusinessDAOIF[] out = attrEnumIF.dereference();
      Assert.assertEquals("Unable to dereference an enumeration item on an attribute that belongs to a struct.", 1, out.length);
      Assert.assertEquals("Dereferenced the wront enumeration item on an attribute that belongs to a struct.", californiaItemId, out[0].getOid());

      Set<String> cachedList = attrEnumIF.getCachedEnumItemIdSet();
      Assert.assertEquals("The size of the enumItem cache on an AttributeEnumeration is wrong.", 1, cachedList.size());
      Assert.assertEquals("The enumItem cache on an AttributeEnumeration references the wrong enumeration item.", californiaItemId, cachedList.iterator().next());

      String cacheColumnName = ( (MdAttributeEnumerationDAOIF) attrEnumIF.getMdAttribute() ).getCacheColumnName();
      String databaseCachedStates = Database.getEnumCacheFieldInTable(mdStructIF.getTableName(), cacheColumnName, structDAO.getOid());

      Assert.assertEquals("The enumItem cache database column on an AttributeEnumeration references the wrong enumeration item.", californiaItemId, databaseCachedStates);
    }
    catch (Throwable e)
    {
      Assert.fail(e.getMessage());
    }
    finally
    {
      businessDAO.delete();
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
      testObject.setValue(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState(coloradoItemId);
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
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.addItem(MULTIPLE, coloradoItemId);
      testObject.addItem(MULTIPLE, connecticutItemId);
      testObject.apply();

      checkEnumState(californiaItemId, coloradoItemId, connecticutItemId);
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
      testObject.apply();

      checkEnumState(coloradoItemId);
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
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);
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
  public void testReplace()
  {
    try
    {
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);

      Collection<String> values = new TreeSet<String>();
      values.add(coloradoItemId);
      values.add(californiaItemId);
      values.add(connecticutItemId);

      testObject.replaceItems(MULTIPLE, values);

      Assert.assertTrue(testObject.getAttributeIF(MULTIPLE).isModified());

      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId, connecticutItemId);
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
  public void testReplaceNoChange()
  {
    try
    {
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);

      Collection<String> values = new TreeSet<String>();
      values.add(coloradoItemId);
      values.add(californiaItemId);

      testObject.replaceItems(MULTIPLE, values);

      Assert.assertFalse(testObject.getAttributeIF(MULTIPLE).isModified());

      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);
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
  public void testReplaceSingle()
  {
    try
    {
      testObject.addItem(SINGLE, connecticutItemId);
      testObject.apply();

      AttributeEnumeration attribute = (AttributeEnumeration) testObject.getAttribute(SINGLE);

      Set<String> list = attribute.getCachedEnumItemIdSet();
      if (list.size() != 1)
        Assert.fail("Single-select Enumeration allowed multiple items.");
      if (!list.contains(connecticutItemId))
        Assert.fail("Single-select Enumeration did not update item selection.");

      Collection<String> values = new TreeSet<String>();
      values.add(coloradoItemId);

      testObject.replaceItems(SINGLE, values);

      Assert.assertTrue(testObject.getAttributeIF(SINGLE).isModified());

      testObject.apply();

      attribute = (AttributeEnumeration) testObject.getAttribute(SINGLE);

      list = attribute.getCachedEnumItemIdSet();
      if (list.size() != 1)
        Assert.fail("Single-select Enumeration allowed multiple items.");
      if (!list.contains(coloradoItemId))
        Assert.fail("Single-select Enumeration did not update item selection.");
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
  public void testInvalidReplaceSingle()
  {
    try
    {
      testObject.addItem(SINGLE, connecticutItemId);
      testObject.apply();

      AttributeEnumeration attribute = (AttributeEnumeration) testObject.getAttribute(SINGLE);

      Set<String> list = attribute.getCachedEnumItemIdSet();
      if (list.size() != 1)
        Assert.fail("Single-select Enumeration allowed multiple items.");
      if (!list.contains(connecticutItemId))
        Assert.fail("Single-select Enumeration did not update item selection.");

      Collection<String> values = new TreeSet<String>();
      values.add(coloradoItemId);
      values.add(connecticutItemId);

      try
      {
        testObject.replaceItems(SINGLE, values);
        testObject.apply();

        Assert.fail("Able to replace a single select enumerated item with a multiple enumerated ids");
      }
      catch (DataAccessException e)
      {
        // Expected to land here
      }

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
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState(californiaItemId);
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
  }

  /**
   * Tests adding an enumeration to a class with existing instances. Make sure
   * this does not break anything.
   */
  @Request
  @Test
  public void testEnumAttrToClassWithInstances()
  {
    MdBusinessDAOIF testMdBusiness = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeEnumerationDAO mdAttrEnum = null;

    try
    {
      testObject.apply();

      // Add the enumeration as a single-select Attribute on the TEST type
      mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "someTestStateEnum");
      mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Single select state attribute");
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusiness.getOid());
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
      mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.FALSE);
      mdAttrEnum.apply();

      testObject = BusinessDAO.get(testObject.getOid()).getBusinessDAO();

      testObject.addItem("someTestStateEnum", californiaItemId);
      testObject.apply();

      // Caching checks
      checkNewInstance("someTestStateEnum", mdAttrEnum, californiaItemId);

      // Database checks
      checkLinkEnumeration(stateMdEnumerationIF.getTableName(), "someTestStateEnum", californiaItemId);

      testObject.removeItem("someTestStateEnum", coloradoItemId);

    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
    finally
    {
      if (mdAttrEnum != null && mdAttrEnum.isAppliedToDB())
      {
        mdAttrEnum.delete();
      }
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
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState();
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
      testObject.removeItem(SINGLE, coloradoItemId);
      testObject.apply();
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
      testObject.addItem(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState(coloradoItemId);
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
    testObject.apply();
    testObject.addItem(SINGLE, connecticutItemId);
    testObject.apply();
    testObject.addItem(SINGLE, californiaItemId);
    testObject.apply();

    AttributeEnumeration attribute = (AttributeEnumeration) testObject.getAttribute(SINGLE);
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
      testObject.addItem(MULTIPLE, coloradoItemId);
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);

      testObject.addItem(MULTIPLE, coloradoItemId);
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.addItem(MULTIPLE, coloradoItemId);
      testObject.addItem(MULTIPLE, californiaItemId);
      testObject.apply();

      checkEnumState(coloradoItemId, californiaItemId);
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
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState();

      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.removeItem(MULTIPLE, coloradoItemId);
      testObject.apply();

      checkEnumState();
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
      testObject.apply();
      testObject.removeItem(MULTIPLE, "Not an OID");
      testObject.apply();

      checkEnumState(coloradoItemId);
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
      testObject.apply();
      testObject.addItem(MULTIPLE, "Not an OID");
      testObject.apply();

      checkEnumState(coloradoItemId);
      Assert.fail("State accepted an invalid item.");
    }
    catch (AttributeValueException e)
    {
      // This is expected
    }
  }

  /**
   * Tests completely removing the ENUM_STATE attribute. Instantiates several
   * copies of the MasterTestSetup.TEST_CLASS class, and then deletes the
   * enumeration. Checks to make sure STATE_ENUM is deleted correctly, then
   * return the class to a testable state.
   */
  @Request
  @Test
  public void testDeleteAttribute()
  {
    BusinessDAO testObjects[] = new BusinessDAO[4];
    String testIDs[] = new String[4];
    List<String> setOids = new LinkedList<String>();
    setOids.add(testObject.getAttribute(SINGLE).getValue());
    setOids.add(testObject.getAttribute(MULTIPLE).getValue());

    try
    {
      // Count the number of enumerated items in the test object
      int origEnumNum = 0;

      for (String setOid : setOids)
      {
        Database.getEnumItemIds(stateMdEnumerationIF.getTableName(), setOid).size();
      }

      testObjects[0] = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      testIDs[0] = testObjects[0].apply();
      setOids.add(testObjects[0].getAttribute(SINGLE).getValue());
      setOids.add(testObjects[0].getAttribute(MULTIPLE).getValue());

      testObjects[1] = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      testObjects[1].addItem(MULTIPLE, californiaItemId);
      testIDs[1] = testObjects[1].apply();
      setOids.add(testObjects[1].getAttribute(SINGLE).getValue());
      setOids.add(testObjects[1].getAttribute(MULTIPLE).getValue());

      testObjects[2] = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      testObjects[2].removeItem(MULTIPLE, coloradoItemId);
      testObjects[2].addItem(MULTIPLE, connecticutItemId);
      testIDs[2] = testObjects[2].apply();
      setOids.add(testObjects[2].getAttribute(SINGLE).getValue());
      setOids.add(testObjects[2].getAttribute(MULTIPLE).getValue());

      testObjects[3] = BusinessDAO.newInstance(EntityMasterTestSetup.TEST_CLASS.getType());
      testObjects[3].addItem(MULTIPLE, californiaItemId);
      testObjects[3].addItem(MULTIPLE, connecticutItemId);
      testIDs[3] = testObjects[3].apply();
      setOids.add(testObjects[3].getAttribute(SINGLE).getValue());
      setOids.add(testObjects[3].getAttribute(MULTIPLE).getValue());

      testObject = testObjects[0];
      checkEnumState(coloradoItemId);
      testObject = testObjects[1];
      checkEnumState(coloradoItemId, californiaItemId);
      testObject = testObjects[2];
      checkEnumState(connecticutItemId);
      testObject = testObjects[3];
      checkEnumState(coloradoItemId, californiaItemId, connecticutItemId);

      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

      List<String> db = BusinessDAO.getEntityIdsFromDB(mdBusinessIF);
      if (db.size() != 4)
        Assert.fail("Expected 4 entres in the " + mdBusinessIF.getTableName() + " table, found " + db.size());

      BusinessDAO.get(multiAttrMdID).getBusinessDAO().delete();

      db = EntityDAO.getEntityIdsDB(mdBusinessIF.definesType());
      if (db.size() != 4)
        Assert.fail("Expected 4 entres in the " + mdBusinessIF.getTableName() + " table, found " + db.size());

      List<String> fields = Database.getColumnNames(mdBusinessIF.getTableName());
      if (fields.contains(MULTIPLE))
        Assert.fail("Table ENUM_STATE was not deleted");

      // Count the number of rows in the stateMdEnumerationIF table
      int count = 0;

      for (String setOid : setOids)
      {
        count += Database.getEnumItemIds(stateMdEnumerationIF.getTableName(), setOid).size();
      }

      // We expect 4 entries in the link table - 1 for each object's instance of
      // the multi-select enum
      if (count != origEnumNum + testObjects.length)
        Assert.fail(stateMdEnumerationIF.getTableName() + " table still contains entries for deleted attribute");
    }
    catch (DataAccessException e)
    {
      Assert.fail(e.toString());
    }
    finally
    {
      // If it got deleted...
      MdBusinessDAOIF mdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

      if (!Database.getColumnNames(mdBusinessIF.getTableName()).contains(MULTIPLE))
      {
        // Add the enumeration back as an Attribute on the
        // MasterTestSetup.TEST_CLASS class
        MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, MULTIPLE);
        mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Some State Attribute");
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, mdBusinessIF.getOid());
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
        mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
        multiAttrMdID = mdAttrEnum.apply();
      }

      // Delete the testObjects[] array
      for (BusinessDAO o : testObjects)
      {
        if (o != null && !o.isNew())
        {
          o.delete();
        }
      }

      testObject = null;
    }
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testUniqueAttributeEnmeration()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "uniqueAttrEnum");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique Attriubte Enumeration");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.addItem(MdAttributeEnumerationInfo.INDEX_TYPE, IndexTypes.UNIQUE_INDEX.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);

    try
    {
      mdAttrEnum.apply();
    }
    catch (AttributeInvalidUniquenessConstraintException e)
    {
      // This is expected
      return;
    }

    mdAttrEnum.delete();
    Assert.fail("An enumeration attribute was defined to be unique.  Enumeraiton attributes cannot participate in a uniqueness constraint.");
  }

  /**
   * Tests that an attribute that should be unique should also be required.
   * 
   */
  @Request
  @Test
  public void testUniqueAttributeGroupEnumeration()
  {
    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "uniqueAttrGrpEnum");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Unique Attriubte Group Enumeration");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, coloradoItemId);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, stateMdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.apply();

    MdIndexDAO mdIndex = MdIndexDAO.newInstance();
    mdIndex.setValue(MdIndexInfo.MD_ENTITY, testMdBusinessIF.getOid());
    mdIndex.setValue(MdIndexInfo.UNIQUE, MdAttributeBooleanInfo.TRUE);
    mdIndex.setStructValue(MdIndexInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Index");
    mdIndex.apply();

    try
    {
      // add the unique group index
      mdIndex.addAttribute(mdAttrEnum, 0);
      Assert.fail("An enumeration attribute was defined to be part of a unique attribute group.  Enumeraiton attributes cannot participate in a uniqueness constraint.");
    }
    catch (AttributeInvalidUniquenessConstraintException e)
    {
      // This is expected
    }
    finally
    {
      mdIndex.delete();
      mdAttrEnum.delete();
    }
  }

  /**
   * Make sure that enumerations only use a subclass of
   * Constants.ROOT_ENUMERATION_ATTRIBUTE_CLASS to define the master item list.
   */
  @Request
  @Test
  public void testEnumerationUsesValidClass()
  {
    MdBusinessDAOIF mdBusinessTestIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    TypeInfo classInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TEST_ENUMERATION_DEF");

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, classInfo.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, classInfo.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration Definition");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Definition");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, mdBusinessTestIF.getOid());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdEnumeration.apply();
    }
    catch (InvalidReferenceException e)
    {
      // This is expected
      return;
    }
    // should this test fail, delete this object
    mdEnumeration.delete();
    Assert.fail("An enumeration was defined that did not use a class that is a subclass of " + EnumerationMasterInfo.CLASS + ".");
  }

  /**
   * Make sure that enumerations only use a subclass of
   * Constants.ROOT_ENUMERATION_ATTRIBUTE_CLASS to define the master item list.
   */
  @Request
  @Test
  public void testNoDuplicateEnumerationTypeName()
  {
    TypeInfo classInfo = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "TEST_ENUMERATION_DEF");

    TypeInfo classInfo2 = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE + "_2", "TEST_ENUMERATION_DEF");

    MdEnumerationDAO mdEnumeration = MdEnumerationDAO.newInstance();
    mdEnumeration.setValue(MdEnumerationInfo.NAME, classInfo.getTypeName());
    mdEnumeration.setValue(MdEnumerationInfo.PACKAGE, classInfo.getPackageName());
    mdEnumeration.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration Definition");
    mdEnumeration.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Definition");
    mdEnumeration.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusinessIF.getOid());
    mdEnumeration.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    mdEnumeration.apply();

    MdEnumerationDAO mdEnumeration2 = MdEnumerationDAO.newInstance();
    mdEnumeration2.setValue(MdEnumerationInfo.NAME, classInfo2.getTypeName());
    mdEnumeration2.setValue(MdEnumerationInfo.PACKAGE, classInfo2.getPackageName());
    mdEnumeration2.setStructValue(MdEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test Enumeration Definition");
    mdEnumeration2.setStructValue(MdEnumerationInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "JUnit Test Enumeration Definition");
    mdEnumeration2.setValue(MdEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdEnumeration2.setValue(MdEnumerationInfo.INCLUDE_ALL, MdAttributeBooleanInfo.TRUE);
    mdEnumeration2.setValue(MdEnumerationInfo.MASTER_MD_BUSINESS, stateEnumMdBusinessIF.getOid());
    mdEnumeration2.setValue(MdEnumerationInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);

    try
    {
      mdEnumeration2.apply();
    }
    catch (DuplicateMdEnumerationDefinitionException e)
    {
      // This is expected
      return;
    }
    finally
    {
      // should this test fail, delete this object
      mdEnumeration.delete();

      if (!mdEnumeration2.isNew())
      {
        mdEnumeration2.delete();
      }
    }

    Assert.fail("An enumeration was defined that did not use a class that is a subclass of [" + EnumerationMasterInfo.CLASS + "].");
  }

  /**
   * Tests to make sure that any Enumeration master list cannot be extended.
   */
  @Request
  @Test
  public void testEnumerationExtendable()
  {
    MdBusinessDAO mdBusiness1 = null;

    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    try
    {
      TypeInfo enumClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "EnumOrig");
      mdBusiness1 = MdBusinessDAO.newInstance();
      mdBusiness1.setValue(MdBusinessInfo.NAME, enumClass.getTypeName());
      mdBusiness1.setValue(MdBusinessInfo.PACKAGE, enumClass.getPackageName());
      mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "EnumOrig");
      mdBusiness1.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "States of the Union");
      mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.TRUE);
      mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
      mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.apply();

      Assert.fail("An enumerated type was (incorrectly) marked as extendable.");
    }
    catch (InheritanceException e)
    {
      // we want to fall in here
    }
    finally
    {
      if (mdBusiness1 != null && !mdBusiness1.isNew())
      {
        mdBusiness1.delete();
      }
    }
  }

  /**
   * Tests to make sure that any Enumeration master list cannot be extended.
   */
  @Request
  @Test
  public void testEnumerationExtended()
  {
    MdBusinessDAO mdBusiness1 = null;
    MdBusinessDAO mdBusiness2 = null;

    MdBusinessDAOIF enumMasterMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EnumerationMasterInfo.CLASS);

    try
    {
      TypeInfo enumClass = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "EnumOrig");
      mdBusiness1 = MdBusinessDAO.newInstance();
      mdBusiness1.setValue(MdBusinessInfo.NAME, enumClass.getTypeName());
      mdBusiness1.setValue(MdBusinessInfo.PACKAGE, enumClass.getPackageName());
      mdBusiness1.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness1.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "EnumOrig");
      mdBusiness1.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "States of the Union");
      mdBusiness1.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, enumMasterMdBusinessIF.getOid());
      mdBusiness1.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness1.apply();

      TypeInfo enum2Class = new TypeInfo(EntityMasterTestSetup.JUNIT_PACKAGE, "ExtEnum");
      mdBusiness2 = MdBusinessDAO.newInstance();
      mdBusiness2.setValue(MdBusinessInfo.NAME, enum2Class.getTypeName());
      mdBusiness2.setValue(MdBusinessInfo.PACKAGE, enum2Class.getPackageName());
      mdBusiness2.setValue(MdBusinessInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
      mdBusiness2.setStructValue(MdBusinessInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "ExtEnum");
      mdBusiness2.setStructValue(MdBusinessInfo.DESCRIPTION, MdAttributeLocalInfo.DEFAULT_LOCALE, "Test enum extending master list");
      mdBusiness2.setValue(MdBusinessInfo.EXTENDABLE, MdAttributeBooleanInfo.FALSE);
      mdBusiness2.setValue(MdBusinessInfo.ABSTRACT, MdAttributeBooleanInfo.FALSE);
      mdBusiness2.setValue(MdBusinessInfo.SUPER_MD_BUSINESS, mdBusiness1.getOid());
      mdBusiness2.setValue(MdBusinessInfo.GENERATE_SOURCE, MdAttributeBooleanInfo.FALSE);
      mdBusiness2.apply();

      Assert.fail("An enumerated type was (incorrectly) able to extend an enumerated masterlist");
    }
    catch (InheritanceException e)
    {
      // we want to fall in here
    }
    finally
    {
      if (mdBusiness1 != null && !mdBusiness1.isNew())
      {
        mdBusiness1.delete();
      }
      if (mdBusiness2 != null && !mdBusiness2.isNew())
      {
        mdBusiness2.delete();
      }
    }
  }

  /**
   * Tests to make sure that any MdEnumerations are deleted whenever their
   * master lists are deleted.
   */
  @Request
  @Test
  public void testDeleteMdEnumerationFromDeleteMasterList()
  {
    createVoltronMasterList();
    createVoltronMdEnumeration();

    TestFixtureFactory.delete(voltronMdBusinessIF);

    if (MdTypeDAO.isDefined(voltronMdEnumeration.getType()))
    {
      Assert.fail("Deleting the enumeration master list class failed to delete an MdEnumeration that uses it.");
    }

  }

  /**
   * Tests to make sure that any MdAttributeEnumerations are deleted whenever
   * its MdEnumeration is deleted.
   */
  @Request
  @Test
  public void testDeleteMdAttributeEnumerationFromDeleteMdEnumeration()
  {
    createVoltronMasterList();
    createVoltronMdEnumeration();

    MdBusinessDAOIF testMdBusinessIF = MdBusinessDAO.getMdBusinessDAO(EntityMasterTestSetup.TEST_CLASS.getType());

    // Add the enumeration as a multi-select Attribute on the TEST type
    MdAttributeEnumerationDAO mdAttrEnum = MdAttributeEnumerationDAO.newInstance();
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.NAME, "lionAttribute");
    mdAttrEnum.setStructValue(MdAttributeEnumerationInfo.DISPLAY_LABEL, MdAttributeLocalInfo.DEFAULT_LOCALE, "Voltron Lion Attribute");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFAULT_VALUE, "");
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REQUIRED, MdAttributeBooleanInfo.FALSE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.REMOVE, MdAttributeBooleanInfo.TRUE);
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.DEFINING_MD_CLASS, testMdBusinessIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.MD_ENUMERATION, voltronMdEnumerationIF.getOid());
    mdAttrEnum.setValue(MdAttributeEnumerationInfo.SELECT_MULTIPLE, MdAttributeBooleanInfo.TRUE);
    String mdAttrEnumID = mdAttrEnum.apply();

    voltronMdEnumerationIF.getBusinessDAO().delete();

    try
    {
      BusinessDAO.get(mdAttrEnumID);
    }
    catch (DataNotFoundException e)
    {
      // This is expected
    }
  }

  /**
   * Runs checks for expected output against a copy of the instance of the test
   * object and against direct queries to the database. Uses the variable
   * arguments feature from Java 1.5
   * 
   * @param states
   *          IDs of items expected to be in the enumeration
   */
  private void checkEnumState(String... states)
  {
    // Caching checks
    checkNewInstance(MULTIPLE, mdAttrEnumMultiple, states);

    // Database checks
    checkLinkEnumeration(stateMdEnumerationIF.getTableName(), MULTIPLE, states);
  }

  /**
   * Gets a new instance of the testObject and checks for the number and oid of
   * each state passed in (varibale number of arguments).
   * 
   * @param states
   *          IDs of items expected to be in the enumeration
   */
  private void checkNewInstance(String field, MdAttributeEnumerationDAO mdAttrEnum, String... states)
  {
    BusinessDAOIF check = BusinessDAO.get(testObject.getOid());
    AttributeEnumerationIF enumr = (AttributeEnumerationIF) check.getAttributeIF(field);
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

    MdBusinessDAOIF mdBusinessIF = check.getMdBusinessDAO();
    String tableName = mdBusinessIF.getTableName();
    String cachedColumnName = mdAttrEnum.getCacheColumnName();
    String databaseCachedStates = Database.getEnumCacheFieldInTable(tableName, cachedColumnName, testObject.getOid());

    String[] databaseCachedStateArray = null;

    if (databaseCachedStates.trim().equals(""))
    {
      databaseCachedStateArray = new String[0];
    }
    else
    {
      databaseCachedStateArray = databaseCachedStates.split(",");
    }

    if (databaseCachedStateArray.length != states.length)
    {
      Assert.fail("Instance of \"" + EntityMasterTestSetup.TEST_CLASS.getType() + "\" expected " + states.length + " states in cached database column for " + field + ", found " + databaseCachedStateArray.length);
    }

  }

  /**
   * Checks for the number and oid of each state passed in (varibale number of
   * arguments) directly against the database.
   * 
   * 
   * @param tableName
   *          name of the table that stores the enumeration mappings
   * @param field
   *          name of an AttriubteEnumeration
   * @param states
   *          IDs of items expected to be in the enumeration
   */
  private void checkLinkEnumeration(String tableName, String field, String... states)
  {
    Set<String> dbEnumItemIDs = Database.getEnumItemIds(tableName, testObject.getAttribute(field).getValue());
    if (dbEnumItemIDs.size() != states.length)
    {
      Assert.fail(tableName + " table in an unexpected state.  Expected " + states.length + " rows, found " + dbEnumItemIDs.size());
    }

    for (String state : states)
    {
      if (!dbEnumItemIDs.contains(state))
      {
        Assert.fail(tableName + " table in an unexpected state.  Did not find an expected value in the set.");
      }
    }
  }

}
